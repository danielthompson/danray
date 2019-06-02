package net.danielthompson.danray;

import net.danielthompson.danray.config.*;
import net.danielthompson.danray.scenes.AbstractScene;

/**
 * DanRay
 * User: dthompson
 * Date: 6/28/13
 * Time: 10:32 AM
 */
public class Main {

   private static final RenderQuality lowQuality = new LowQuality();
   private static final RenderQuality mediumQuality = new MediumQuality();
   private static final RenderQuality highQuality = new HighQuality();

   public static boolean Finished = false;

   private static TraceManager traceManager;

   static public void main(String[] args) {

      AbstractScene scene;

      RenderQuality quality = lowQuality;
//      RenderQuality quality = mediumQuality;
//      RenderQuality quality = highQuality;

      scene = SceneBuilder.NumericalStabilityTest2(quality.x, quality.y);
//      scene = SceneBuilder.NumericalStabilityTest(quality.x, quality.y);

      final TracerOptions options = parseArgs(args);
      //options.numThreads = 1;

      traceManager = new TraceManager(scene, quality, options);
      traceManager.Compile();
      traceManager.Render();
      traceManager.Finish();
   }

   public static void Retrace(int[] pixel) {
      traceManager.Trace(pixel);
   }

   private static TracerOptions parseArgs(final String[] args) {
      TracerOptions options = new TracerOptions();

      for (int i = 0; i < args.length; i++) {
         String arg = args[i];

         switch (arg) {
            case "-countWindow":
               options.showCountWindow = true;
               break;
            case "-displayallpaths":
               options.displayAllPaths = true;
               break;
            case "-heatWindow":
               options.showHeatWindow = true;
               break;
            case "-infoWindow":
               options.showInfoWindow = true;
               break;
            case "-kdWindow":
               options.showKDWindow = true;
               break;
            case "-logLevel":
               Logger.Level level = Logger.Level.Error;
               if (i + 1 < args.length) {
                  try {
                     level = Logger.Level.valueOf(args[i + 1]);
                  } catch (IllegalArgumentException e) {
                     Logger.Log(Logger.Level.Warning, "Couldn't parse log level: [" + args[i + 1]+ "], defaulting to Error.");
                  }
               }
               else {
                  Logger.Log(Logger.Level.Warning, "Log level specified but value missing, defaulting to Error.");
               }

               Logger.LogLevel = level;
               break;
            case "-notrace":
               options.noTrace = true;
               break;
            case "-openGLWindow":
               options.showOpenGLWindow = true;
               break;
            case "-spectrumWindow":
               options.showSpectrumWindow = true;
               break;
            case "-threads":
               if (i + 1 < args.length) {
                  int numThreads = 0;
                  try {
                     numThreads = Integer.parseInt(args[i + 1]);
                  } catch (NumberFormatException e) {
                     Logger.Log(Logger.Level.Warning, "Couldn't parse number of threads: [" + args[i + 1]+ "].");
                  }

                  options.numThreads = numThreads;
               }
               else {
                  Logger.Log(Logger.Level.Warning, "Threads specified but number missing.");
               }
               break;
            case "-tracerWindow":
               options.showTracerWindow = true;
               break;
         }
      }
      return options;
   }
}