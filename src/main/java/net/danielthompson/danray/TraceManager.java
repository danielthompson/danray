package net.danielthompson.danray;

import net.danielthompson.danray.acceleration.KDScene;
import net.danielthompson.danray.films.BoxFilterFilm;
import net.danielthompson.danray.films.TriangleFilterFilm;
import net.danielthompson.danray.integrators.*;
import net.danielthompson.danray.presets.RenderQualityPreset;
import net.danielthompson.danray.presets.TracerOptions;
import net.danielthompson.danray.runners.BottomUpTileRunner;
import net.danielthompson.danray.runners.PixelRunner;
import net.danielthompson.danray.films.AbstractFilm;
import net.danielthompson.danray.samplers.AbstractSampler;
import net.danielthompson.danray.samplers.CenterSampler;
import net.danielthompson.danray.samplers.GridSampler;
import net.danielthompson.danray.samplers.RandomSampler;
import net.danielthompson.danray.shading.fullspectrum.FullSpectralBlender;
import net.danielthompson.danray.shading.fullspectrum.FullSpectralPowerDistribution;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.scenes.AbstractScene;
import net.danielthompson.danray.structures.Vector;
import net.danielthompson.danray.ui.*;
import net.danielthompson.danray.ui.gl.common.KDJFrame;
import net.danielthompson.danray.ui.gl.common.GLFrame;
import net.danielthompson.danray.ui.lwjgl.GLRenderer;
import net.danielthompson.danray.utility.IOHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by daniel on 3/4/14.
 */
public class TraceManager {
   private BufferedImage _traceImage;
   private MainCanvas _traceCanvas;
   private Graphics _traceGraphics;
   private Frame _traceFrame;

   private float[][][] _tracePixelXYZ;

   private float _minX;
   private float _minY;
   private float _minZ;

   private float _maxX;
   private float _maxY;
   private float _maxZ;

   private BufferedImage _countImage;
   private ImageCanvas _countCanvas;
   private Graphics _countGraphics;
   private Frame _countFrame;

   private BufferedImage _heatImage;
   private ImageCanvas _heatCanvas;
   private Graphics _heatGraphics;
   private Frame _heatFrame;

   private GLFrame _glFrame;
/*
   private Graphics _infoGraphics;
   private InfoFrame _infoFrame;
*/
   private InfoJFrame _infoJFrame;

   private SPDEditorView _editorView;

   private CanvasUpdateTimerTask _updateTask;
   private Timer _timer;

   private IOHelper _ioHelper;

   private RenderQualityPreset _qualityPreset;
   private TracerOptions _tracerOptions;
   private final float _samplesInverse;

   public int InitialRays;


   private AbstractScene _scene;

   private AbstractIntegrator _integrator;

   private AbstractSampler _sampler;

   private KDJFrame _kdFrame;

   private Date _renderStartTime;

   private volatile long _numRenderedPixels;

   private float _numPixelsDivisor;

   private long _numPixelsStep;

   private float _inverseKDNodeCount;

   private AbstractFilm _film;

   /**
    * @param scene The scene to be rendered.
    * @param renderQualityPreset
    * @param tracerOptions
    */
   public TraceManager(AbstractScene scene, RenderQualityPreset renderQualityPreset, TracerOptions tracerOptions) {
      _qualityPreset = renderQualityPreset;
      _tracerOptions = tracerOptions;
      _samplesInverse = 1.0f / (renderQualityPreset.getSuperSamplesPerPixel() * renderQualityPreset.getSamplesPerPixel());
      _scene = scene;
      _integrator = new PathTraceIntegrator(_scene, renderQualityPreset.getMaxDepth());
//      _integrator = new IterativePathTraceIntegrator(_scene, renderQualityPreset.getMaxDepth());
//      _integrator = new IterativeMISPathTraceIntegrator(_scene, renderQualityPreset.getMaxDepth());
//      _integrator = new WhittedIntegrator(_scene, renderQualityPreset.getMaxDepth());
//      _integrator = new DepthIntegrator(_scene, 1);
//      _integrator = new NormalIntegrator(_scene, 1);
//      _sampler = new RandomSampler(renderQualityPreset.getSamplesPerPixel());
      _sampler = new GridSampler(renderQualityPreset.getSamplesPerPixel());
//      _sampler = new RandomSampler(renderQualityPreset.getSamplesPerPixel());
//      _sampler = new CenterSampler(renderQualityPreset.getSamplesPerPixel());
      _timer = new Timer();

      long numPixels = renderQualityPreset.getX() * renderQualityPreset.getY();
      _numPixelsDivisor = 1.0f / numPixels;
      _ioHelper = new IOHelper();
      _numPixelsStep = numPixels / 100;

      Logger.AddOutput(System.out);
      _ioHelper.CreateOutputDirectory();
      Logger.AddOutputFile(_ioHelper.GetLogFile());

   }

   public void Compile() {

      int cores = Runtime.getRuntime().availableProcessors();
      Logger.Log(Logger.Level.Info, "Detected " + cores + " cores.");

      _tracerOptions.numThreads = 6;

      if (_tracerOptions.numThreads == 0) {
         Logger.Log(Logger.Level.Info, "No thread count specified at startup, defaulting to available cores.");
         _tracerOptions.numThreads = cores;
      }

      Logger.Log(Logger.Level.Info, "Using " + _tracerOptions.numThreads + " threads.");

      Logger.Log(Logger.Level.Info, "Scene has " + _scene.Shapes.size() + " shapes, " + _scene.Lights.size() + " lights.");
      Logger.Log(Logger.Level.Info, "Scene is implemented with " + _scene.ImplementationType);
      Logger.Log(Logger.Level.Info, "Compiling scene...");
      Date start = new Date();
      Logger.Log(Logger.Level.Info, _scene.compile(_tracerOptions));
      Date end = new Date();
      String duration = getDurationString(start, end);

      if (_scene instanceof KDScene) {
         int kdNodeCount = ((KDScene) _scene).rootNode.GetCount();
         _inverseKDNodeCount = 1.0f / (float) kdNodeCount;
         _inverseKDNodeCount *= 1;
      }
      Logger.Log(Logger.Level.Info, "Finished compiling scene in " + duration);
   }

   public void Render() {
      SetupWindows();

      if (!_tracerOptions.noTrace) {

         for (int i = 0; i < _scene.numFrames; i++) {

            if (_tracerOptions.displayAllPaths) {

               for (int k = 2; k <= _qualityPreset.getMaxDepth(); k++) {
                  //for (int s = 1; s <= k + 1; s++) {
                  //int k = 2;
                  int s = 1;
                  int t = k + 1 - s;
                  SetupFrame(s, t);
                  Trace(i, s, t);
                  Log(i, s, t);
                  _ioHelper.Save(_traceImage, "trace" + getOutputString(k, s, t));
                  _ioHelper.Save(_countImage, "count" + getOutputString(k, s, t));
                  _ioHelper.Save(_heatImage, "heat" + getOutputString(k, s, t));
                  TeardownFrame();
                  //break;
                  //}
                  //break;
               }
            } else {
               SetupFrame(-1, -1);
               Trace(i, -1, -1);
               Log(i, -1, -1);
               _ioHelper.Save(_traceImage, "trace" + getOutputString(i));
               _ioHelper.Save(_countImage, "count" + getOutputString(i));
               _ioHelper.Save(_heatImage, "heat" + getOutputString(i));
               TeardownFrame();
            }
         }
      }
   }

   private String getOutputString(int frame) {
      return String.format("%06d", frame);
   }

   private String getOutputString(int k, int s, int t) {
      return "k" + String.format("%02d", k) + "s" + String.format("%02d", s) + "t" + String.format("%02d", t);
   }

   public void SetupWindows() {

      if (_tracerOptions.showOpenGLWindow) {

//         GLRenderer renderer = new GLRenderer(_scene);
//
//         renderer.run();

         // gl window

         if (_glFrame == null) {
            _glFrame = new GLFrame(_scene);

            Dimension canvasSize = new Dimension(new Dimension(_qualityPreset.getX(), _qualityPreset.getY() + 22));

            _glFrame.setSize(canvasSize);
            _glFrame.setBounds(10, 0, _qualityPreset.getX(), _qualityPreset.getY() + 22);
            _glFrame.setVisible(true);
         }

         // kd window
         if (_tracerOptions.showKDWindow && _scene instanceof KDScene) {

            if (_kdFrame == null) {
               _kdFrame = new KDJFrame((KDScene) _scene, _glFrame.Canvas);

               Dimension frameSize = new Dimension(200, 500);

               _kdFrame.setSize(frameSize);
               _kdFrame.setBounds(_qualityPreset.getX()  + 10, 0, frameSize.width, frameSize.height + 22);
               _kdFrame.setVisible(true);
            }
         }
      }

      // heat window
      if (_tracerOptions.showHeatWindow && _scene instanceof KDScene) {
         if (_heatFrame == null) {
            _heatFrame = new Frame("KD-tree Heatmap");
            _heatCanvas = new ImageCanvas();
            _heatFrame.add("Center", _heatCanvas);
            _heatFrame.setSize(new Dimension(_qualityPreset.getX(), _qualityPreset.getY() + 22));
            _heatFrame.setVisible(true);
         }

         _heatGraphics = _heatCanvas.getGraphics();
         TimerTask traceTask3 = new CanvasUpdateTimerTask(_heatCanvas, _heatGraphics);
         _timer.schedule(traceTask3, 1000, 800);
      }

      // count window
      if (_tracerOptions.showCountWindow) {
         if (_countFrame == null) {
            _countFrame = new Frame("Ray Density");
            _countCanvas = new ImageCanvas();
            _countFrame.add("Center", _countCanvas);
            _countFrame.setSize(new Dimension(_qualityPreset.getX(), _qualityPreset.getY() + 22));
            _countFrame.setVisible(true);
         }

         _countGraphics = _countCanvas.getGraphics();
         TimerTask traceTask2 = new CanvasUpdateTimerTask(_countCanvas, _countGraphics);
         _timer.schedule(traceTask2, 1000, 800);
      }

      // tracer window
      if (_tracerOptions.showTracerWindow) {
         if (_traceFrame == null) {

            _traceCanvas = new MainCanvas(this, null);
            _traceCanvas.addMouseListener(_traceCanvas);

            _traceFrame = new Frame("DanRay");
            _traceFrame.add("Center", _traceCanvas);
            _traceFrame.setSize(new Dimension(_qualityPreset.getX(), _qualityPreset.getY() + 22));
            _traceFrame.setVisible(true);

         }
         _traceGraphics = _traceCanvas.getGraphics();

         TimerTask traceTask = new CanvasUpdateTimerTask(_traceCanvas, _traceGraphics);
         _timer.schedule(traceTask, 1000, 800);
      }
      // info window

      if (_tracerOptions.showInfoWindow) {
         if (_infoJFrame == null) {
            _infoJFrame = new InfoJFrame(this);
            _infoJFrame.pack();
            _infoJFrame.setLocation(_traceFrame.getWidth() + 10, 0);
            _infoJFrame.setVisible(true);

         }
         _infoJFrame.setCameraOrigin(_scene.Camera.getOrigin().X, _scene.Camera.getOrigin().Y, _scene.Camera.getOrigin().Z);
         _infoJFrame.setCameraDirection(_scene.Camera.getDirection().X, _scene.Camera.getDirection().Y, _scene.Camera.getDirection().Z);
      }

      if (_tracerOptions.showSpectrumWindow) {

         if (_editorView == null) {
            JFrame frame = new JFrame("ReflectanceSpectrum Editor");

            _editorView = new SPDEditorView();

            frame.setContentPane(_editorView.mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocation(100, 0);
            frame.pack();
            frame.setVisible(true);
         }
      }
   }

   public void setMouseXY(final int x, final int y) {
      if (_infoJFrame != null)
         _infoJFrame.setMouseLocation(x, y);
   }

   public void setMouseClickXY(final int x, final int y) {
      Ray[] cameraRays = _scene.Camera.getRays(x, y, 1);

      Intersection state = _scene.getNearestShape(cameraRays[0], x, y);

      if (state != null) {

         float xx = state.Location.X;
         float yy = state.Location.Y;
         float zz = state.Location.Z;

         if (_infoJFrame != null)
            _infoJFrame.setSceneLocation(xx, yy, zz);
      }

      else {
         if (_infoJFrame != null)
            _infoJFrame.setNoSceneLocation();
      }


   }

   public void SetupFrame(int s, int t) {

      _countImage = new BufferedImage(_qualityPreset.getX(), _qualityPreset.getY(), BufferedImage.TYPE_INT_RGB);
      _traceImage = new BufferedImage(_qualityPreset.getX(), _qualityPreset.getY(), BufferedImage.TYPE_INT_RGB);
      _heatImage = new BufferedImage(_qualityPreset.getX(), _qualityPreset.getY(), BufferedImage.TYPE_INT_RGB);

      _film = new BoxFilterFilm(_traceImage);
//      _film = new TriangleFilterFilm(_traceImage);

      _tracePixelXYZ = new float[_qualityPreset.getX()][_qualityPreset.getY()][3];

      for (int i = 0; i < _qualityPreset.getX(); ++i)
         for (int j = 0; j < _qualityPreset.getY(); ++j)
            _traceImage.setRGB(i, j, Color.gray.getRGB()); // Black Background

      if (_tracerOptions.showCountWindow) {
         _countCanvas.setImage(_countImage);
      }

      if (_tracerOptions.showTracerWindow) {
         _traceCanvas.setImage(_traceImage);
      }

      if (_tracerOptions.showHeatWindow && _scene instanceof KDScene) {
         _heatCanvas.setImage(_heatImage);
      }

      //this.Statistics = new Statistics[_qualityPreset.getX()][_qualityPreset.getY()];
      for (int i = 0; i < _qualityPreset.getX(); i++) {
         //this.Statistics[i] = new Statistics[_qualityPreset.getY()];
         for (int j = 0; j < _qualityPreset.getY(); j++) {
            //this.Statistics[i][j] = new Statistics();
         }
      }

      _minX = _minY = _minZ = Float.MAX_VALUE;
      _maxX = _maxY = _maxZ = -Float.MAX_VALUE;

      if (s >= 0 || t >= 0) {
         Logger.Log(Logger.Level.Info, "rendering with lightpath length s = [" + s +"] and eyepath length t = [" + t + "]");
      }
      Logger.Log(Logger.Level.Info, "image is " + _qualityPreset.getX() + " x " + _qualityPreset.getY() );
      Logger.Log(Logger.Level.Info, "pixel threshold: " + _qualityPreset.getConvergenceTerminationThreshold());
      Logger.Log(Logger.Level.Info, "samples: " + _qualityPreset.getSamplesPerPixel() + "; super samples: " + _qualityPreset.getSuperSamplesPerPixel());
      Logger.Log(Logger.Level.Info, "max depth: " + _qualityPreset.getMaxDepth());
      Logger.Log(Logger.Level.Info, "depth of field: " + (_qualityPreset.getUseDepthOfField() ? "yes" : "no"));

   }

   public void TeardownFrame() {
;
   }

   public void Trace(int frame, int s, int t) {
      Logger.Log(Logger.Level.Info, "Rendering frame " + frame + "...");

      _renderStartTime = new Date();

      _scene.Camera.setFrame(frame);



      Runnable runner = new BottomUpTileRunner(this, _integrator, _scene, _qualityPreset, _film, _sampler, frame);

      /*
      if (s >= 0 || t >= 0) {
         _spectralTracer.setDebug(s, t);
      }
      */

//      _tracerOptions.numThreads = 1;
//
      if (_tracerOptions.numThreads <= 1) {
         runner.run();
      }
      else {
         java.util.List<Thread> threads = new ArrayList<Thread>(_tracerOptions.numThreads);

         for (int i = 0; i < _tracerOptions.numThreads; i++) {

            threads.add(new Thread(runner));
            threads.get(i).start();
         }

         for (Thread thread : threads) {
            try {
               thread.join();
            }
            catch (InterruptedException e) {
               Logger.Log(Logger.Level.Warning, "Caught interrupted exception.");
            }
         }
      }

      Main.Finished = true;
   }

   public void SetPixelXYZ(int[] pixel, float[] xyzBlendSoFar) {
      _tracePixelXYZ[pixel[0]][pixel[1]][0] = xyzBlendSoFar[0];
      _tracePixelXYZ[pixel[0]][pixel[1]][1] = xyzBlendSoFar[1];
      _tracePixelXYZ[pixel[0]][pixel[1]][2] = xyzBlendSoFar[2];

      if (xyzBlendSoFar[0] > _maxX)
         _maxX = xyzBlendSoFar[0];
      else if (xyzBlendSoFar[0] < _minX)
         _minX = xyzBlendSoFar[0];

      if (xyzBlendSoFar[1] > _maxY)
         _maxY = xyzBlendSoFar[1];
      else if (xyzBlendSoFar[1] < _minY)
         _minY = xyzBlendSoFar[1];

      if (xyzBlendSoFar[2] > _maxZ)
         _maxZ = xyzBlendSoFar[2];
      else if (xyzBlendSoFar[2] < _minZ)
         _minZ = xyzBlendSoFar[2];

   }

   private void Log(int frame, int s, int t) {
      Date end = new Date();
      long milliseconds = end.getTime() - _renderStartTime.getTime();

      DecimalFormat integerFormatter = new DecimalFormat("###,###,###,###");

      String duration = getDurationString(_renderStartTime, end);
      if (s >= 0 || t >= 0) {
         Logger.Log(Logger.Level.Info, "Finished rendering lightpath length [s] = " + s + ", eyepath length [t] = " + t + " in " + duration);
      }
      else {
         Logger.Log(Logger.Level.Info, "Finished rendering frame " + frame + " in " + duration);
      }
      int pixels = _qualityPreset.getX() * _qualityPreset.getY();
      Logger.Log(Logger.Level.Info, integerFormatter.format(pixels) +  " pixels, " + integerFormatter.format(InitialRays) +  " initial rays");

      long fillRate = (long)(pixels * 1000 / (float)milliseconds);
      Logger.Log(Logger.Level.Info, integerFormatter.format(fillRate) + " pixels / sec fillrate");
   }

   public String getDurationString(Date start, Date end) {
      long milliseconds = end.getTime() - start.getTime();
      return getDurationString(milliseconds);
   }

   public String getDurationString(long milliseconds) {

      long seconds = milliseconds / 1000;
      int ms = (int)(milliseconds % 1000);

      String duration = String.format("%dh %02dm %02ds %03dms", seconds/3600, (seconds%3600)/60, (seconds%60), ms);
      return duration;
   }


   public void Trace(int[] pixel) {
      PixelRunner runner = new PixelRunner(this, _integrator, _scene, _qualityPreset, _film, _sampler, 0);
      Logger.Log(Logger.Level.Info, "Tracing single pixel " + pixel[0] + " x " + pixel[1]);
      runner.trace(pixel[0], pixel[1]);
   }

   public void UpdateCanvas() {
      if (_tracerOptions.showTracerWindow) {
         _traceCanvas.update(_traceGraphics);
      }

      if (_tracerOptions.showCountWindow) {
         _countCanvas.update(_countGraphics);
      }

      if (_tracerOptions.showHeatWindow) {
         _heatCanvas.update(_heatGraphics);
      }
   }

   public void SetPixelColor(int x, int y, Color color) {
      _traceImage.setRGB(x, y, color.getRGB());

      _numRenderedPixels++;

      if (_numRenderedPixels % _numPixelsStep == 0) {
         int percent = (int)(_numRenderedPixels * _numPixelsDivisor * 100);

         Logger.Log(Logger.Level.Info, "Rendered " + percent + "%" );
      }


   }

   public void SetPixelSPD(int[] pixel, FullSpectralPowerDistribution spd) {
      Color c = FullSpectralBlender.ConvertSPDtoRGB(spd);
      SetPixelColor(pixel[0], pixel[1], c);
   }

   public void SetRayCountForPixel(int x, int y, int count) {

      if (_tracerOptions.showCountWindow) {

         float normalizedColor = count * _samplesInverse;
         int expandedColor = (int) (255.0f * normalizedColor);

         Color color = new Color(expandedColor, expandedColor, expandedColor);
         _countImage.setRGB(x, y, color.getRGB());
      }
   }

   public void SetKDHeatForPixel(int x, int y, int count) {
      if (_tracerOptions.showHeatWindow) {
         //System.out.println(count);
         float normalizedColor = count * _inverseKDNodeCount;
         int expandedColor = (int) (255.0f * normalizedColor);

         Color color = new Color(expandedColor, expandedColor, expandedColor);
         _heatImage.setRGB(x, y, color.getRGB());
      }
   }


   public void Finish() {
      Logger.Flush();
   }

   public void moveOriginAlongAxis(int x, int y, int z) {
      Vector delta = new Vector(x, y, z);
      _scene.Camera.moveOriginAlongAxis(delta);
      Render();
   }

   public void moveOriginAlongOrientation(int x, int y, int z) {
      Vector delta = new Vector(x, y, z);
      _scene.Camera.moveOriginAlongOrientation(delta);
      Render();
   }

   public void moveDirectionAlongAxis(float x, float y, float z) {
      _scene.Camera.moveDirectionAlongAxis(x, y, z);
      Render();
   }

   public void moveDirectionAlongOrientation(float x, float y, float z) {
      _scene.Camera.moveDirectionAlongOrientation(x, y, z);
      Render();
   }
}