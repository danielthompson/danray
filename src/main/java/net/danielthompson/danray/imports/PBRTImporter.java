package net.danielthompson.danray.imports;

import net.danielthompson.danray.Logger;
import net.danielthompson.danray.cameras.Camera;
import net.danielthompson.danray.cameras.CameraSettings;
import net.danielthompson.danray.cameras.PerspectiveCamera;
import net.danielthompson.danray.imports.pbrt.Constants;
import net.danielthompson.danray.scenes.AbstractScene;
import net.danielthompson.danray.scenes.NaiveScene;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Transform;
import net.danielthompson.danray.structures.Vector;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class PBRTImporter extends AbstractFileImporter<AbstractScene> {

   public PBRTImporter(File file) {
      super(file);
   }

   private Transform _currentTransformationMatrix;
   private Camera _camera;
   private CameraSettings _cameraSettings;
   private AbstractScene _abstractScene;

   List<String> allDirectives = Arrays.asList(
         Constants.Accelerator,
         Constants.AreaLightSource,
         Constants.AttributeBegin,
         Constants.AttributeEnd,
         Constants.Camera,
         Constants.Film,
         Constants.Integrator,
         Constants.LookAt,
         Constants.MakeNamedMaterial,
         Constants.NamedMaterial,
         Constants.PixelFilter,
         Constants.Sampler,
         Constants.Shape,
         Constants.TransformBegin,
         Constants.TransformEnd,
         Constants.Translate
   );

   List<String> sceneWideDirectives = Arrays.asList(
         Constants.Accelerator,
         //Constants.AreaLightSource,
         //Constants.AttributeBegin,
         //Constants.AttributeEnd,
         Constants.Camera,
         Constants.Film,
         Constants.Integrator,
         Constants.LookAt,
         Constants.MakeNamedMaterial,
         //Constants.NamedMaterial,
         Constants.PixelFilter,
         Constants.Sampler,
         //Constants.Shape,
         Constants.TransformBegin,
         Constants.TransformEnd,
         Constants.Translate
   );

   List<String> worldDirectives = Arrays.asList(
         //Constants.Accelerator,
         Constants.AreaLightSource,
         Constants.AttributeBegin,
         Constants.AttributeEnd,
         //Constants.Camera,
         //Constants.Film,
         //Constants.Integrator,
         Constants.LookAt,
         Constants.MakeNamedMaterial,
         Constants.NamedMaterial,
         //Constants.PixelFilter,
         //Constants.Sampler,
         Constants.Shape,
         Constants.TransformBegin,
         Constants.TransformEnd,
         Constants.Translate
         //Constants.WorldBegin,
         //Constants.WorldEnd
   );

   @Override
   public AbstractScene Process() {

      try {

         List<List<String>> tokens = new ArrayList<>();

         Scanner scanner = new Scanner(file);
         // scan
         if (scanner.hasNext())
         {
            int sourceLineNumber = 0;
            int targetLineNumber = -1;
            String line;
            while (scanner.hasNextLine())
            {
               line = scanner.nextLine();
               tokens.add(new ArrayList<>());
               String word;

               Scanner wordScanner = new Scanner(line);
               while (wordScanner.hasNext())
               {
                  word = wordScanner.next();
                  // strip out comments
                  if (word.startsWith("#"))
                     break;

                  if (allDirectives.contains(word)) {
                     // if this is a directive, then we move on to a new line
                     targetLineNumber++;
                  }

                  // split brackets, if needed
                  if (word.length() > 1) {
                     int lastIndex = word.length() - 1;

                     if (word.charAt(0) == '[') {
                        tokens.get(targetLineNumber).add("[");
                        tokens.get(targetLineNumber).add(word.substring(1, lastIndex));
                     }
                     else if (word.charAt(lastIndex) == ']') {
                        tokens.get(targetLineNumber).add(word.substring(0, lastIndex - 1));
                        tokens.get(targetLineNumber).add("]");
                     }
                     else {
                        tokens.get(targetLineNumber).add(word);
                     }
                  }
                  else {
                     tokens.get(targetLineNumber).add(word);
                  }

               }

               sourceLineNumber++;

            }

         }
         else {
            //throw std::invalid_argument("Couldn't open file " + Filename);
         }

         Scanner scanner2 = new Scanner(file);


         boolean inDirective = false;

         String commentSkipRegex = "#.*$";
         Pattern commentSkipPattern = Pattern.compile(commentSkipRegex);

         String next;

         while (scanner.hasNext()) {
            next = scanner.next();
            if (next.startsWith("#")) {
               scanner.nextLine();
               continue;
            }
            switch (next) {
               case "LookAt": {
                  parseLookAt(scanner);
                  break;
               }
               case "Camera": {
                  parseCamera(scanner);
                  break;
               }
               case "Sampler": {
                  parseSampler(scanner);
                  break;
               }
               default: {
                  Logger.Log(Logger.Level.Warning, "Skipping [" + next + "] during parse...");
                  break;
               }
            }

         }

      } catch (FileNotFoundException e) {
         e.printStackTrace();
      }

      return _abstractScene;
   }

   private void parseSampler(Scanner scanner) {
      String implementation = scanner.next().toLowerCase();
      switch (implementation) {
         case "\"halton\"": {

         }
      }
   }

   private void parseCamera(Scanner scanner) {
      String implementation = scanner.next().toLowerCase();

      switch (implementation) {
         case "\"perspective\"": {
            _camera = new PerspectiveCamera(_currentTransformationMatrix);
            break;
         }
         default: {
            // TODO fix
            Logger.Log(Logger.Level.Warning, "Parser found " + implementation + " camera, but using Perspective because that's the only one implemented right now.");
            _camera = new PerspectiveCamera(_currentTransformationMatrix);
            break;
         }
      }

      _cameraSettings = new CameraSettings();

      String type = scanner.next();
      String name = scanner.next();
      float value = scanner.nextFloat();

      switch (name) {
         case "fov\"": {
            _cameraSettings.FieldOfView = value;
            break;
         }
         default: {
            // TODO fix
            Logger.Log(Logger.Level.Warning, "Parser found " + type + " " + name + " camera value, disregarding...");
            break;
         }
      }

      _abstractScene = new NaiveScene(_camera);

   }

   private void parseLookAt(Scanner scanner) {
      String next;
      List<Float> floats = new ArrayList<>();

      for (int i = 0; i < 9; i++) {
         if (scanner.hasNextFloat()) {
            floats.add(scanner.nextFloat());
         }
         else {
            next = scanner.next();
            if (next.startsWith("#")) {
               i--;
               scanner.nextLine();
            }
         }
      }

      Point eye = new Point(floats.get(0), floats.get(1), floats.get(2));
      Point lookat = new Point(floats.get(3), floats.get(4), floats.get(5));
      Vector up  = new Vector(floats.get(6), floats.get(7), floats.get(8));

      _currentTransformationMatrix = Transform.LookAt(eye, lookat, up);
   }
}
