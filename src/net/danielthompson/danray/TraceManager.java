package net.danielthompson.danray;

import net.danielthompson.danray.acceleration.KDScene;
import net.danielthompson.danray.presets.RenderQualityPreset;
import net.danielthompson.danray.presets.TracerOptions;
import net.danielthompson.danray.runners.PixelRunner;
import net.danielthompson.danray.runners.TileRunner;
import net.danielthompson.danray.shading.SpectralBlender;
import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Scene;
import net.danielthompson.danray.structures.Statistics;
import net.danielthompson.danray.structures.Vector;
import net.danielthompson.danray.samplers.SpectralTracer;
import net.danielthompson.danray.samplers.WhittedSampler;
import net.danielthompson.danray.ui.*;
import net.danielthompson.danray.ui.opengl.KDJFrame;
import net.danielthompson.danray.ui.opengl.OpenGLFrame;
import net.danielthompson.danray.utility.IOHelper;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

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

   private OpenGLFrame _glFrame;
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

   public static int ReflectedRays;

   public static int RefractedRays;

   public Statistics[][] Statistics;

   Scene _scene;

   WhittedSampler _tracer;
   SpectralTracer _spectralTracer;

   private KDJFrame _kdFrame;

   int _xPointer;
   int _yPointer;

   private Date _renderStartTime;

   private long _numPixels;
   private volatile long _numRenderedPixels;

   private float _numPixelsDivisor;

   private long _numPixelsStep;

   private int _kdNodeCount;
   private double _inverseKDNodeCount;

   /**
    * @param scene The scene to be rendered.
    * @param renderQualityPreset
    * @param tracerOptions
    */
   public TraceManager(Scene scene, RenderQualityPreset renderQualityPreset, TracerOptions tracerOptions) {
      _qualityPreset = renderQualityPreset;
      _tracerOptions = tracerOptions;
      _samplesInverse = 1.0f / (renderQualityPreset.getSuperSamplesPerPixel() * renderQualityPreset.getSamplesPerPixel());
      _scene = scene;
      _tracer = new WhittedSampler(_scene, renderQualityPreset.getMaxDepth());
      //_spectralTracer = new SpectralBDPathTracer(Scene, renderQualityPreset.getMaxDepth());
      //_spectralTracer = new SpectralPathTracer(Scene, renderQualityPreset.getMaxDepth());
      _spectralTracer = new SpectralTracer(_scene, renderQualityPreset.getMaxDepth());
      _timer = new Timer();

      _numPixels = renderQualityPreset.getX() * renderQualityPreset.getY();
      _numPixelsDivisor = 1.0f / _numPixels;
      _ioHelper = new IOHelper();
      _numPixelsStep = _numPixels / 100;

      Logger.AddOutput(System.out);
      _ioHelper.CreateOutputDirectory();
      Logger.AddOutputFile(_ioHelper.GetLogFile());

   }

   public void Compile() {

      int cores = Runtime.getRuntime().availableProcessors();
      Logger.Log("Detected " + cores + " cores.");

      if (_tracerOptions.numThreads == 0) {
         Logger.Log("No thread count specified at startup, defaulting to available cores.");
         _tracerOptions.numThreads = cores;
      }

      Logger.Log("Using " + _tracerOptions.numThreads + " threads.");

      Logger.Log("Scene has " + _scene.shapes.size() + " drawables, " + _scene.Radiatables.size() + " radiatables.");
      Logger.Log("Scene is implemented with " + _scene.ImplementationType);
      Logger.Log("Compiling scene...");
      Date start = new Date();
      Logger.Log(_scene.Compile(_tracerOptions));
      Date end = new Date();
      String duration = getDurationString(start, end);

      if (_scene instanceof KDScene) {
         _kdNodeCount = ((KDScene) _scene).rootNode.GetCount();
         _inverseKDNodeCount = 1.0 / (double)_kdNodeCount;
         _inverseKDNodeCount *= 1;
      }
      Logger.Log("Finished compiling scene in " + duration);
   }

   public void Render() {
      SetupWindows();

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
         }
         else {
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

   private String getOutputString(int frame) {
      return String.format("%06d", frame);
   }

   private String getOutputString(int k, int s, int t) {
      return "k" + String.format("%02d", k) + "s" + String.format("%02d", s) + "t" + String.format("%02d", t);
   }

   public void SetupWindows() {

      if (_tracerOptions.showOpenGLWindow) {

         // gl window

         if (_glFrame == null) {
            _glFrame = new OpenGLFrame(_scene);

            Dimension canvasSize = new Dimension(new Dimension(_qualityPreset.getX(), _qualityPreset.getY() + 22));

            _glFrame.setSize(canvasSize);
            _glFrame.setBounds(_qualityPreset.getX() + 10, 0, _qualityPreset.getX(), _qualityPreset.getY() + 22);
            _glFrame.setVisible(true);
         }

         // kd window
         if (_tracerOptions.showKDWindow && _scene instanceof KDScene) {

            if (_kdFrame == null) {
               _kdFrame = new KDJFrame((KDScene) _scene, _glFrame.Canvas);

               Dimension frameSize = new Dimension(200, 500);

               _kdFrame.setSize(frameSize);
               _kdFrame.setBounds(_qualityPreset.getX() * 2 + 10, 0, frameSize.width, frameSize.height + 22);
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
            JFrame frame = new JFrame("Spectrum Editor");

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
      Ray[] cameraRays = _scene.Camera.getInitialStochasticRaysForPixel(x, y, 1);

      IntersectionState state = _scene.GetClosestDrawableToRay(cameraRays[0]);

      if (state != null) {

         double xx = state.IntersectionPoint.X;
         double yy = state.IntersectionPoint.Y;
         double zz = state.IntersectionPoint.Z;

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

      this.Statistics = new Statistics[_qualityPreset.getX()][_qualityPreset.getY()];
      for (int i = 0; i < _qualityPreset.getX(); i++) {
         this.Statistics[i] = new Statistics[_qualityPreset.getY()];
         for (int j = 0; j < _qualityPreset.getY(); j++) {
            this.Statistics[i][j] = new Statistics();
         }
      }

      _xPointer = 0;
      _yPointer = 0;

      _minX = _minY = _minZ = Float.MAX_VALUE;
      _maxX = _maxY = _maxZ = -Float.MAX_VALUE;

      if (s >= 0 || t >= 0) {
         Logger.Log("rendering with lightpath length s = [" + s +"] and eyepath length t = [" + t + "]");
      }
      Logger.Log("image is " + _qualityPreset.getX() + " x " + _qualityPreset.getY() );
      Logger.Log("pixel threshold: " + _qualityPreset.getConvergenceTerminationThreshold());
      Logger.Log("samples: " + _qualityPreset.getSamplesPerPixel() + "; super samples: " + _qualityPreset.getSuperSamplesPerPixel());
      Logger.Log("max depth: " + _qualityPreset.getMaxDepth());
      Logger.Log("depth of field: " + (_qualityPreset.getUseDepthOfField() ? "yes" : "no"));

   }

   public void TeardownFrame() {
;
   }

   public void Trace(int frame, int s, int t) {
      Logger.Log("Rendering frame " + frame + "...");

      _renderStartTime = new Date();

      _scene.Camera.setFrame(frame);

      Runnable runner = new TileRunner(this, _tracer, _scene, _qualityPreset, frame);

      /*
      if (s >= 0 || t >= 0) {
         _spectralTracer.setDebug(s, t);
      }
      */

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
               Logger.Log("Caught interrupted exception.");
            }
         }
      }

      Main.Finished = true;
   }

   public void ToneMap(int frame) {

      Logger.Log("Starting tone mapping...");

      final float min = 0;
      final float max = 1.9999695f;

      final float xScale = max / (_maxX - _minX);
      Logger.Log("Min X = " + _minX + ", Max X = " + _maxX);
      Logger.Log("X Scale = " + xScale);

      final float yScale = max / (_maxY - _minY);
      Logger.Log("Min Y = " + _minY + ", Max Y = " + _maxY);
      Logger.Log("Y Scale = " + yScale);

      final float zScale = max / (_maxZ - _minZ);
      Logger.Log("Min Z = " + _minZ + ", Max X = " + _maxZ);
      Logger.Log("Z Scale = " + zScale);

      float scale = -Float.MAX_VALUE;

      /*if (xScale > scale)
         scale = xScale;

      if (yScale > scale)
         scale = yScale;

      if (zScale > scale)
         scale = zScale;*/

      for (int x = 0; x < _qualityPreset.getX(); x++) {
         for (int y = 0; y < _qualityPreset.getY(); y++) {
            // normalize XYZ

            float[] xyz = _tracePixelXYZ[x][y];

            xyz[0] *= yScale;
            xyz[1] = xyz[1] * yScale;
            xyz[2] *= yScale;

            // convert to RGB

            Color c = SpectralBlender.ConvertXYZtoRGB(xyz[0], xyz[1], xyz[2], null);

            // set in picture

            //_traceImage.setRGB(x, y, c.getRGB());

         }
      }

      _traceCanvas.update(_traceGraphics);

      Logger.Log("Tone mapping complete.");

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
         Logger.Log("Finished rendering lightpath length [s] = " + s + ", eyepath length [t] = " + t + " in " + duration);
      }
      else {
         Logger.Log("Finished rendering frame " + frame + " in " + duration);
      }
      int pixels = _qualityPreset.getX() * _qualityPreset.getY();
      Logger.Log(integerFormatter.format(pixels) +  " pixels, " + integerFormatter.format(InitialRays) +  " initial rays");

      DescriptiveStatistics drawableStats = getDrawableIntersectionStatistics();
      Logger.Log("drawable intersections: " + getStatisticsSummary(drawableStats));

      DescriptiveStatistics boundingStats = getBoundingIntersectionStatistics();
      Logger.Log("bounding intersections: " + getStatisticsSummary(boundingStats));
      Logger.Log("left traversal: " + getStatisticsSummary(getLeftTraversalStatistics()));
      Logger.Log("right traversals: " + getStatisticsSummary(getRightTraversalStatistics()));
      Logger.Log("both bounds hit: " + getStatisticsSummary(getBothBoundHitStatistics()));
      Logger.Log("one bound hit: " + getStatisticsSummary(getOneBoundHitStatistics()));
      Logger.Log("no bound hit: " + getStatisticsSummary(getNoBoundHitStatistics()));

      long drawableIntersectionRate = (long)(drawableStats.getSum() * 1000 / (double)milliseconds);
      long boundableIntersectionRate = (long)(boundingStats.getSum() * 1000 / (double)milliseconds);
      long totalIntersectionRate = drawableIntersectionRate + boundableIntersectionRate;

      Logger.Log(integerFormatter.format(drawableIntersectionRate) +  " drawable int/sec; " + integerFormatter.format(boundableIntersectionRate) + " boundable int/sec; " + integerFormatter.format(totalIntersectionRate) + " total int/sec; ");

      long fillRate = (long)(pixels * 1000 / (double)milliseconds);
      Logger.Log(integerFormatter.format(fillRate) + " pixels / sec fillrate");
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


   public DescriptiveStatistics getDrawableIntersectionStatistics() {
      DescriptiveStatistics stats = new DescriptiveStatistics();
      for (int i = 0; i < Statistics.length; i++) {
         for (int j = 0; j < Statistics[i].length; j++) {
            stats.addValue(Statistics[i][j].DrawableIntersections);

         }
      }
      return stats;
   }

   public DescriptiveStatistics getBoundingIntersectionStatistics() {
      DescriptiveStatistics stats = new DescriptiveStatistics();
      for (int i = 0; i < Statistics.length; i++) {
         for (int j = 0; j < Statistics[i].length; j++) {
            stats.addValue(Statistics[i][j].BoundingIntersections);

         }
      }
      return stats;
   }

   public DescriptiveStatistics getLeftTraversalStatistics() {
      DescriptiveStatistics stats = new DescriptiveStatistics();
      for (int i = 0; i < Statistics.length; i++) {
         for (int j = 0; j < Statistics[i].length; j++) {
            stats.addValue(Statistics[i][j].LeftNodesTraversed);

         }
      }
      return stats;
   }

   public DescriptiveStatistics getRightTraversalStatistics() {
      DescriptiveStatistics stats = new DescriptiveStatistics();
      for (int i = 0; i < Statistics.length; i++) {
         for (int j = 0; j < Statistics[i].length; j++) {
            stats.addValue(Statistics[i][j].RightNodesTraversed);

         }
      }
      return stats;
   }

   public DescriptiveStatistics getBothBoundHitStatistics() {
      DescriptiveStatistics stats = new DescriptiveStatistics();
      for (int i = 0; i < Statistics.length; i++) {
         for (int j = 0; j < Statistics[i].length; j++) {
            stats.addValue(Statistics[i][j].BothBoundHit);

         }
      }
      return stats;
   }

   public DescriptiveStatistics getOneBoundHitStatistics() {
      DescriptiveStatistics stats = new DescriptiveStatistics();
      for (int i = 0; i < Statistics.length; i++) {
         for (int j = 0; j < Statistics[i].length; j++) {
            stats.addValue(Statistics[i][j].OneBoundHit);

         }
      }
      return stats;
   }

   public DescriptiveStatistics getNoBoundHitStatistics() {
      DescriptiveStatistics stats = new DescriptiveStatistics();
      for (int i = 0; i < Statistics.length; i++) {
         for (int j = 0; j < Statistics[i].length; j++) {
            stats.addValue(Statistics[i][j].NoBoundsHit);

         }
      }
      return stats;
   }


   public String getStatisticsSummary(DescriptiveStatistics stats) {
      DecimalFormat integerFormatter = new DecimalFormat("###,###,###,###");
      DecimalFormat decimalFormatter = new DecimalFormat("###,###.##");
      String value = "total " + integerFormatter.format(stats.getSum());
      value += "; min " + integerFormatter.format(stats.getMin());
      value += "; max " + integerFormatter.format(stats.getMax());
      value += "; median " + integerFormatter.format(stats.getPercentile(50));
      value += "; avg " + decimalFormatter.format(stats.getMean());
      value += "; stdev " + decimalFormatter.format(stats.getStandardDeviation());

      return value;
   }

   //public long[] ge

   public long[] getBoundingIntersections() {
      long intersections = 0;
      long min = Long.MAX_VALUE;
      long max = 0;

      for (int i = 0; i < Statistics.length; i++) {
         for (int j = 0; j < Statistics[i].length; j++) {
            intersections += Statistics[i][j].BoundingIntersections;
            if (Statistics[i][j].BoundingIntersections < min)
               min = Statistics[i][j].BoundingIntersections;
            if (Statistics[i][j].BoundingIntersections > max) {
               max = Statistics[i][j].BoundingIntersections;
            }
         }
      }

      return new long[] { intersections, min, max };
   }

   public long[] getDrawableIntersections() {

      long intersections = 0;
      long min = Long.MAX_VALUE;
      long max = 0;

      for (int i = 0; i < Statistics.length; i++) {
         for (int j = 0; j < Statistics[i].length; j++) {
            intersections += Statistics[i][j].DrawableIntersections;
            if (Statistics[i][j].DrawableIntersections < min)
               min = Statistics[i][j].DrawableIntersections;
            if (Statistics[i][j].DrawableIntersections > max) {
               max = Statistics[i][j].DrawableIntersections;
            }
         }
      }

      return new long[] { intersections, min, max };
   }

   public void Trace(int[] pixel) {
      PixelRunner runner = new PixelRunner(this, _spectralTracer, _scene, _qualityPreset, 0);
      runner.trace(pixel);
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

   public void SetPixelColor(int[] pixel, Color color) {
      _traceImage.setRGB(pixel[0], pixel[1], color.getRGB());

      _numRenderedPixels++;

      if (_numRenderedPixels % _numPixelsStep == 0) {
         int percent = (int)(_numRenderedPixels * _numPixelsDivisor * 100);

         Logger.Log("Rendered " + percent + "%" );
      }


   }

   public void SetPixelSPD(int[] pixel, SpectralPowerDistribution spd) {
      Color c = SpectralBlender.ConvertSPDtoRGB(spd);
      SetPixelColor(pixel, c);
   }

   public void SetRayCountForPixel(int[] pixel, int count) {

      if (_tracerOptions.showCountWindow) {

         float normalizedColor = count * _samplesInverse;
         int expandedColor = (int) (255.0f * normalizedColor);

         Color color = new Color(expandedColor, expandedColor, expandedColor);
         _countImage.setRGB(pixel[0], pixel[1], color.getRGB());
      }
   }

   public void SetKDHeatForPixel(int[] pixel, int count) {
      if (_tracerOptions.showHeatWindow) {
         //System.out.println(count);
         double normalizedColor = count * _inverseKDNodeCount;
         int expandedColor = (int) (255.0f * normalizedColor);

         Color color = new Color(expandedColor, expandedColor, expandedColor);
         _heatImage.setRGB(pixel[0], pixel[1], color.getRGB());
      }
   }


   public void Finish() {
      Logger.Flush();
   }

   public void moveOrigin(int x, int y, int z) {
      Vector offset = new Vector(x, y, z);
      _scene.Camera.moveOrigin(offset);
      Render();
   }



}