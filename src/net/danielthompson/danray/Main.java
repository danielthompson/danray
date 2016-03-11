package net.danielthompson.danray;

import net.danielthompson.danray.presets.*;
import net.danielthompson.danray.structures.Scene;


/**
 * DanRay
 * User: dthompson
 * Date: 6/28/13
 * Time: 10:32 AM
 */
public class Main {

   private static final RenderQualityPreset _preset = new MediumQuality();

   static final boolean UseDepthOfField = false;

   public static boolean Finished = false;

   static TraceManager traceManager;

   static public void main(String[] args) {

      Scene scene;

      //scene = SceneBuilder.ManyRandomSpheres(_preset.getX(), _preset.getY());
      scene = SceneBuilder.ManyRegularSpheres(_preset.getX(), _preset.getY());
      //scene = SceneBuilder.SomeRegularSpheres(_preset.getX(), _preset.getY());
      //scene = SceneBuilder.SpheresInAnXPattern(_preset.getX(), _preset.getY());
      //scene = SceneBuilder.AreaLightSourceTest(_preset.getX(), _preset.getY());
      //scene = SceneBuilder.PlaneAndBox(_preset.getX(), _preset.getY());
      //scene = SceneBuilder.DepthOfFieldTest(_preset.getX(), _preset.getY());
      //scene = SceneBuilder.ReflectiveTriangleMeshWithLight(_preset.getX(), _preset.getY());
      //scene = SceneBuilder.FourReflectiveSphereWithLights(_preset.getX(), _preset.getY());
      //scene = SceneBuilder.FourReflectiveSphereWithLightsPointable(_preset.getX(), _preset.getY());
      //scene = SceneBuilder.DiffuseAndSpecularSpheres(_preset.getX(), _preset.getY());
      //scene = SceneBuilder.TwoSpheresWithLights(_preset.getX(), _preset.getY());
      //scene = SceneBuilder.TwoTransparentReflectiveSpheresWithLights(_preset.getX(), _preset.getY());
      //scene = SceneBuilder.SpectralLemon(_preset.getX(), _preset.getY());
      //scene = SceneBuilder.TwoSpectralSpheres(_preset.getX(), _preset.getY());

      TracerOptions options = parseArgs(args);

      traceManager = new TraceManager(scene, _preset, options);
      traceManager.Compile();
      traceManager.Render();
      traceManager.Finish();
   }

   public static void Retrace(int[] pixel) {
      traceManager.Trace(pixel);
   }

   private static TracerOptions parseArgs(String[] args) {
      TracerOptions options = new TracerOptions();

      for (int i = 0; i < args.length; i++) {
         String arg = args[i];
         switch (arg) {
            case "-tracerWindow":
               options.showTracerWindow = true;
               break;
            case "-countWindow":
               options.showCountWindow = true;
               break;
            case "-openGLWindow":
               options.showOpenGLWindow = true;
               break;
            case "-kdWindow":
               options.showKDWindow = true;
               break;
            case "-spectrumWindow":
               options.showSpectrumWindow = true;
               break;
            case "-infoWindow":
               options.showInfoWindow = false;
               break;
            case "-threads":
               if (i + 1 < args.length) {
                  int numThreads = 0;
                  try {
                     numThreads = Integer.parseInt(args[i + 1]);
                  } catch (NumberFormatException e) {
                     Logger.Log("Couldn't parse number of threads: [" + args[i + 1]+ "].");
                  }

                  options.numThreads = numThreads;
               }
               else {
                  Logger.Log("Threads specified but number missing.");
               }
               break;
            case "-displayallpaths": {
               options.displayAllPaths = true;
               break;
            }

         }
      }

      return options;
   }
}