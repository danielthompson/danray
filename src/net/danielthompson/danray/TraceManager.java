package net.danielthompson.danray;

import net.danielthompson.danray.presets.RenderQualityPreset;
import net.danielthompson.danray.presets.TracerOptions;
import net.danielthompson.danray.runners.PixelRunner;
import net.danielthompson.danray.runners.SpectralPixelRunner;
import net.danielthompson.danray.runners.SpectralTilePixelRunner;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Scene;
import net.danielthompson.danray.structures.Statistics;
import net.danielthompson.danray.structures.Vector;
import net.danielthompson.danray.tracers.SpectralTracer;
import net.danielthompson.danray.tracers.Tracer;
import net.danielthompson.danray.ui.CanvasUpdateTimerTask;
import net.danielthompson.danray.ui.CountCanvas;
import net.danielthompson.danray.ui.InfoJFrame;
import net.danielthompson.danray.ui.MainCanvas;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

/**
 * Created by daniel on 3/4/14.
 */
public class TraceManager {
   private BufferedImage _traceImage;
   private MainCanvas _traceCanvas;
   private Graphics _traceGraphics;
   private Frame _traceFrame;

   private BufferedImage _countImage;
   private CountCanvas _countCanvas;
   private Graphics _countGraphics;
   private Frame _countFrame;
/*
   private Graphics _infoGraphics;
   private InfoFrame _infoFrame;
*/
   private InfoJFrame _infoJFrame;

   private CanvasUpdateTimerTask _updateTask;
   private Timer _timer;

   private RenderQualityPreset _qualityPreset;
   private TracerOptions _tracerOptions;
   private final float _samplesInverse;

   public int InitialRays;

   public static int ReflectedRays;

   public static int RefractedRays;

   public Statistics[][] Statistics;

   Scene _scene;

   Tracer _tracer;
   SpectralTracer _spectralTracer;

   int _xPointer;
   int _yPointer;

   private Date _renderStartTime;

   private String _outputDirectory;

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
      _tracer = new Tracer(_scene, renderQualityPreset.getMaxDepth());
      _spectralTracer = new SpectralTracer(_scene, 1);
      _timer = new Timer();

      Logger.AddOutput(System.out);
      CreateOutputDirectory();
      Logger.AddOutputFile(new File(_outputDirectory + File.separator + "log.txt"));

   }

   public void Compile() {

      Logger.Log("Scene has " + _scene._drawables.size() + " drawables, " + _scene._radiatables.size() + " radiatables.");
      Logger.Log("Scene is implemented with " + _scene.getImplementationType());
      Logger.Log("Compiling scene...");
      Date start = new Date();
      Logger.Log(_scene.Compile());
      Date end = new Date();
      String duration = getDurationString(start, end);
      Logger.Log("Finished compiling scene in " + duration);


   }

   public void Render() {
      SetupWindows();




      for (int i = 0; i < _scene.numFrames; i++) {

         //i = 180;

         SetupFrame();
         Trace(i);
         Log(i);
         Save(_traceImage, "trace" + getOutputString(i));
         Save(_countImage, "count" + getOutputString(i));
         TeardownFrame();
         //break;
      }
   }

   private String getOutputString(int frame) {
      return String.format("%06d", frame);
   }

   public void SetupWindows() {

      if (_tracerOptions.ShowWindows) {

         // count window

         if (_countFrame == null) {
            _countFrame = new Frame("Ray Density");
            _countCanvas = new CountCanvas();
            _countFrame.add("Center", _countCanvas);
            _countFrame.setSize(new Dimension(_qualityPreset.getX(), _qualityPreset.getY() + 22));
            _countFrame.setVisible(true);
         }

         else {

         }

         _countGraphics = _countCanvas.getGraphics();

         // tracer window

         if (_traceFrame == null) {

            _traceCanvas = new MainCanvas(this, null);
            _traceCanvas.addMouseListener(_traceCanvas);

            _traceFrame = new Frame("DanRay");
            _traceFrame.add("Center", _traceCanvas);
            _traceFrame.setSize(new Dimension(_qualityPreset.getX(), _qualityPreset.getY() + 22));
            _traceFrame.setVisible(true);

         }
         _traceGraphics = _traceCanvas.getGraphics();

         /*

         // info window

         _infoFrame = new InfoFrame("Info");
         _infoFrame.setSize(new Dimension(400, 200 + 22));
         _infoFrame.pack();
         _infoFrame.setVisible(true);

         _infoGraphics = _infoFrame.getGraphics();

         */
         if (_infoJFrame == null) {
            _infoJFrame = new InfoJFrame(this);
            _infoJFrame.pack();
            _infoJFrame.setLocation(_traceFrame.getWidth() + 10, 0);
            _infoJFrame.setVisible(true);

         }
         _infoJFrame.setCameraOrigin(_scene.Camera.getOrigin().X, _scene.Camera.getOrigin().Y, _scene.Camera.getOrigin().Z);
         _infoJFrame.setCameraDirection(_scene.Camera.getDirection().X, _scene.Camera.getDirection().Y, _scene.Camera.getDirection().Z);

         _updateTask = new CanvasUpdateTimerTask(_traceCanvas, _traceGraphics, _countCanvas, _countGraphics);

         _timer.schedule(_updateTask, 0, 800);
      }

   }

   public void setMouseXY(final int x, final int y) {
      _infoJFrame.setMouseLocation(x, y);
   }

   public void setMouseClickXY(final int x, final int y) {
      Ray[] cameraRays = _scene.Camera.getInitialStochasticRaysForPixel(x, y, 1);

      IntersectionState state = _scene.GetClosestDrawableToRay(cameraRays[0]);

      if (state != null) {

         double xx = state.IntersectionPoint.X;
         double yy = state.IntersectionPoint.Y;
         double zz = state.IntersectionPoint.Z;

         _infoJFrame.setSceneLocation(xx, yy, zz);
      }

      else {
         _infoJFrame.setNoSceneLocation();
      }


   }



   public void SetupFrame() {

      _countImage = new BufferedImage(_qualityPreset.getX(), _qualityPreset.getY(), BufferedImage.TYPE_INT_RGB);
      _traceImage = new BufferedImage(_qualityPreset.getX(), _qualityPreset.getY(), BufferedImage.TYPE_INT_RGB);

      for (int i = 0; i < _qualityPreset.getX(); ++i)
         for (int j = 0; j < _qualityPreset.getY(); ++j)
            _traceImage.setRGB(i, j, Color.gray.getRGB()); // Black Background

      if (_tracerOptions.ShowWindows) {
         _countCanvas.setImage(_countImage);
         _traceCanvas.setImage(_traceImage);

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

      Logger.Log("image is " + _qualityPreset.getX() + " x " + _qualityPreset.getY() );
      Logger.Log("pixel threshold: " + _qualityPreset.getConvergenceTerminationThreshold());
      Logger.Log("samples: " + _qualityPreset.getSamplesPerPixel() + "; super samples: " + _qualityPreset.getSuperSamplesPerPixel());
      Logger.Log("max depth: " + _qualityPreset.getMaxDepth());
      Logger.Log("depth of field: " + (_qualityPreset.getUseDepthOfField() ? "yes" : "no"));
      Logger.Log("threads: " + _qualityPreset.getNumberOfThreads());
   }

   public void TeardownFrame() {
      ;
   }

   public void Trace(int frame) {
      Logger.Log("Rendering frame " + frame + "...");

      _renderStartTime = new Date();

      _scene.Camera.setFrame(frame);

      //Runnable runner = new PixelRunner(this, _tracer, _scene, _qualityPreset, frame);
      Runnable runner = new SpectralTilePixelRunner(this, _spectralTracer, _scene, _qualityPreset, frame);

      if (_qualityPreset.getNumberOfThreads() <= 1) {
         runner.run();
      }
      else {
         java.util.List<Thread> threads = new ArrayList<Thread>(_qualityPreset.getNumberOfThreads());

         for (int i = 0; i < _qualityPreset.getNumberOfThreads(); i++) {

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

   private void Log(int frame) {
      Date end = new Date();
      long milliseconds = end.getTime() - _renderStartTime.getTime();

      DecimalFormat integerFormatter = new DecimalFormat("###,###,###,###");

      String duration = getDurationString(_renderStartTime, end);
      Logger.Log("Finished rendering frame " + frame + " in " + duration);
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
      SpectralPixelRunner runner = new SpectralPixelRunner(this, _spectralTracer, _scene, _qualityPreset, 0);
      runner.trace(pixel);
   }

   public void UpdateCanvas() {
      if (_tracerOptions.ShowWindows) {
         _traceCanvas.update(_traceGraphics);
         _countCanvas.update(_countGraphics);
      }
   }

   public void SetPixelColor(int[] pixel, Color color) {
      _traceImage.setRGB(pixel[0], pixel[1], color.getRGB());
   }

   public void SetRayCountForPixel(int[] pixel, int count) {

      float normalizedColor = count * _samplesInverse;
      int expandedColor = (int)(255.0f * normalizedColor);

      Color color = new Color(expandedColor, expandedColor, expandedColor);
      _countImage.setRGB(pixel[0], pixel[1], color.getRGB());
   }

   public void Save(BufferedImage image, String filename) {
      Date date = Calendar.getInstance().getTime();
      //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");


      try {
         ImageIO.write(image, "png", new File(_outputDirectory + File.separator + filename + /*" " + sdf.format(date) +*/ ".png"));
      }
      catch (IOException e) {
         System.err.println("image not saved.");
      }
   }



   private void CreateOutputDirectory() {
      Date date = Calendar.getInstance().getTime();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

      _outputDirectory = "traces" + File.separator + sdf.format(date);

      new File(_outputDirectory).mkdirs();
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