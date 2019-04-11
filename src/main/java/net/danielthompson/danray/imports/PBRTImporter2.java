//package net.danielthompson.danray.imports;
//
//import net.danielthompson.danray.Logger;
//import net.danielthompson.danray.cameras.Camera;
//import net.danielthompson.danray.cameras.CameraSettings;
//import net.danielthompson.danray.cameras.PerspectiveCamera;
//import net.danielthompson.danray.imports.pbrt.Constants;
//import net.danielthompson.danray.imports.pbrt.PBRTArgument;
//import net.danielthompson.danray.imports.pbrt.PBRTDirective;
//import net.danielthompson.danray.scenes.AbstractScene;
//import net.danielthompson.danray.scenes.NaiveScene;
//import net.danielthompson.danray.structures.Point;
//import net.danielthompson.danray.structures.Transform;
//import net.danielthompson.danray.structures.Vector;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Scanner;
//
//public class PBRTImporter2 extends AbstractFileImporter<AbstractScene> {
//
//   public PBRTImporter2(File file) {
//      super(file);
//   }
//
//   private Transform _currentTransformationMatrix;
//   private Camera _camera;
//   private CameraSettings _cameraSettings;
//   private AbstractScene _abstractScene;
//
//   List<String> allDirectives = Arrays.asList(
//         Constants.Accelerator,
//         Constants.AreaLightSource,
//         Constants.AttributeBegin,
//         Constants.AttributeEnd,
//         Constants.Camera,
//         Constants.Film,
//         Constants.Integrator,
//         Constants.LightSource,
//         Constants.LookAt,
//         Constants.MakeNamedMaterial,
//         Constants.Material,
//         Constants.NamedMaterial,
//         Constants.PixelFilter,
//         Constants.Sampler,
//         Constants.Shape,
//         Constants.Texture,
//         Constants.TransformBegin,
//         Constants.TransformEnd,
//         Constants.Translate,
//         Constants.WorldBegin,
//         Constants.WorldEnd
//   );
//
//   List<String> sceneWideDirectives = Arrays.asList(
//         Constants.Accelerator,
//         //Constants.AreaLightSource,
//         //Constants.AttributeBegin,
//         //Constants.AttributeEnd,
//         Constants.Camera,
//         Constants.Film,
//         Constants.Integrator,
//         Constants.LookAt,
//         Constants.MakeNamedMaterial,
//         //Constants.NamedMaterial,
//         Constants.PixelFilter,
//         Constants.Sampler,
//         //Constants.Shape,
//         Constants.Texture,
//         Constants.TransformBegin,
//         Constants.TransformEnd,
//         Constants.Translate
//         //Constants.WorldBegin,
//         //Constants.WorldEnd
//   );
//
//   List<String> worldDirectives = Arrays.asList(
//         //Constants.Accelerator,
//         Constants.AreaLightSource,
//         Constants.AttributeBegin,
//         Constants.AttributeEnd,
//         //Constants.Camera,
//         //Constants.Film,
//         //Constants.Integrator,
//         Constants.LightSource,
//         Constants.LookAt,
//         Constants.MakeNamedMaterial,
//         Constants.Material,
//         Constants.NamedMaterial,
//         //Constants.PixelFilter,
//         //Constants.Sampler,
//         Constants.Shape,
//         Constants.Texture,
//         Constants.TransformBegin,
//         Constants.TransformEnd,
//         Constants.Translate
//         //Constants.WorldBegin,
//         //Constants.WorldEnd
//   );
//
//   @Override
//   public AbstractScene Process() {
//
//      try {
//         FileReader reader = new FileReader(file);
//         while (true) {
//            int result = reader.read();
//            if (result == -1)
//               break;
//            char input = (char)result;
//
//            switch (input) {
//               case ('L'): {
//
//               }
//
//            }
//         }
//      }
//      catch (IOException e) {
//         e.printStackTrace();
//      }
//
//
//
//      try {
//
//         List<List<String>> tokens = new ArrayList<>();
//
//         Scanner scanner = new Scanner(file);
//         // scan
//         if (scanner.hasNext())
//         {
//            int sourceLineNumber = 0;
//            int targetLineNumber = -1;
//            String line;
//            while (scanner.hasNextLine())
//            {
//               line = scanner.nextLine();
//               tokens.add(new ArrayList<>());
//               String word;
//
//               Scanner wordScanner = new Scanner(line);
//               while (wordScanner.hasNext())
//               {
//                  word = wordScanner.next();
//                  // strip out comments
//                  if (word.startsWith("#"))
//                     break;
//
//                  if (allDirectives.contains(word)) {
//                     // if this is a directive, then we move on to a new line
//                     targetLineNumber++;
//                  }
//
//                  // split brackets, if needed
//                  if (word.length() > 1) {
//                     int lastIndex = word.length() - 1;
//
//                     if (word.charAt(0) == '[') {
//                        tokens.get(targetLineNumber).add("[");
//                        tokens.get(targetLineNumber).add(word.substring(1, lastIndex));
//                     }
//                     else if (word.charAt(lastIndex) == ']') {
//                        tokens.get(targetLineNumber).add(word.substring(0, lastIndex - 1));
//                        tokens.get(targetLineNumber).add("]");
//                     }
//                     else {
//                        tokens.get(targetLineNumber).add(word);
//                     }
//                  }
//                  else {
//                     tokens.get(targetLineNumber).add(word);
//                  }
//
//               }
//
//               int lastIndex = tokens.size()- 1;
//
//               if (tokens.get(lastIndex).size() == 0)
//                  tokens.remove(lastIndex);
//
//               sourceLineNumber++;
//
//            }
//
//         }
//         else {
//            Logger.Log(Logger.Level.Error, "Input file " + file.getAbsolutePath() + " had no content, aborting.");
//            throw new RuntimeException();
//         }
//
//         // parse
//
//         List<PBRTDirective> sceneDirectives = new ArrayList<>();
//         List<PBRTDirective> worldDirectives = new ArrayList<>();
//
//         {
//            List<PBRTDirective> currentDirectives = sceneDirectives;
//
//            for (List<String> line : tokens) {
//               PBRTDirective currentDirective = new PBRTDirective();
//
//               if (line.size() == 0)
//                  continue;
//
//               currentDirective.Identifier = line.get(0);
//
//               if (currentDirective.Identifier.equals(Constants.WorldBegin))
//                  currentDirectives = worldDirectives;
//
//               if (line.size() == 1) {
//                  currentDirectives.add(currentDirective);
//                  continue;
//               }
//
//               String line1 = line.get(1);
//
//               if (IsQuoted(line1)) {
//                  currentDirective.Type = line1.substring(1, line1.length() - 1);
//               }
//               else {
//                  currentDirective.Arguments = new ArrayList<>();
//                  PBRTArgument argument = new PBRTArgument();
//                  //argument.Type = "float";
//                  argument.Values = new ArrayList<>();
//
//                  for (int i = 1; i < line.size(); i++) {
//                     argument.Values.add(line.get(i));
//                  }
//
//                  currentDirective.Arguments.add(argument);
//                  currentDirectives.add(currentDirective);
//                  continue;
//               }
//
//               if (line.size() == 2) {
//                  currentDirectives.add(currentDirective);
//                  continue;
//               }
//
//               currentDirective.Arguments = new ArrayList<>();
//               PBRTArgument currentArgument = new PBRTArgument();
//               boolean inValue = false;
//               int i = 2;
//               while (i < line.size()) {
//                  if (StartQuoted(line.get(i)) && EndQuoted(line.get(i + 1))) {
//                     // we're in an argument
//                     //currentArgument.Type = line.get(i).substring(1, line.get(i).length()/* - 1*/);
//                     currentArgument.Identifier = line.get(i + 1).substring(0, line.get(i + 1).length() - 1);
//                     inValue = true;
//                     i += 2;
//                     continue;
//                  }
//                  if (line.get(i) == "[") {
//                     inValue = true;
//                     i++;
//                     continue;
//                  }
//                  if (line.get(i) == "]") {
//                     inValue = false;
//                     i++;
//                     currentDirective.Arguments.add(currentArgument);
//                     currentArgument = new PBRTArgument();
//                     continue;
//                  }
//                  if (inValue) {
//                     if (IsQuoted(line.get(i))) {
//                        currentArgument.Values.add(line.get(i).substring(1, line.get(i).length() - 2));
//                     } else {
//                        currentArgument.Values.add(line.get(i));
//                     }
//                     i++;
//                     continue;
//                  }
//               }
//
//               if (inValue) {
//                  currentDirective.Arguments.add(currentArgument);
//               }
//
//               currentDirectives.add(currentDirective);
//            }
//         }
//      } catch (FileNotFoundException e) {
//         e.printStackTrace();
//      }
//
//      return _abstractScene;
//   }
//
//   private void parseSampler(Scanner scanner) {
//      String implementation = scanner.next().toLowerCase();
//      switch (implementation) {
//         case "\"halton\"": {
//
//         }
//      }
//   }
//
//   private void parseCamera(Scanner scanner) {
//      String implementation = scanner.next().toLowerCase();
//
//      switch (implementation) {
//         case "\"perspective\"": {
//            _camera = new PerspectiveCamera(_currentTransformationMatrix);
//            break;
//         }
//         default: {
//            // TODO fix
//            Logger.Log(Logger.Level.Warning, "Parser found " + implementation + " camera, but using Perspective because that's the only one implemented right now.");
//            _camera = new PerspectiveCamera(_currentTransformationMatrix);
//            break;
//         }
//      }
//
//      _cameraSettings = new CameraSettings();
//
//      String type = scanner.next();
//      String name = scanner.next();
//      float value = scanner.nextFloat();
//
//      switch (name) {
//         case "fov\"": {
//            _cameraSettings.FieldOfView = value;
//            break;
//         }
//         default: {
//            // TODO fix
//            Logger.Log(Logger.Level.Warning, "Parser found " + type + " " + name + " camera value, disregarding...");
//            break;
//         }
//      }
//
//      _abstractScene = new NaiveScene(_camera);
//
//   }
//
//   private boolean IsQuoted(String token) {
//      return (token.charAt(0) == '"' && token.charAt(token.length() - 1) == '"');
//   }
//
//   private boolean StartQuoted(String token) {
//      return (token.charAt(0) == '"' && token.charAt(token.length() - 1) != '"');
//   }
//
//   private boolean EndQuoted(String token) {
//      return (token.charAt(0) != '"' && token.charAt(token.length() - 1) == '"');
//   }
//
//   private void parseLookAt(Scanner scanner) {
//      String next;
//      List<Float> floats = new ArrayList<>();
//
//      for (int i = 0; i < 9; i++) {
//         if (scanner.hasNextFloat()) {
//            floats.add(scanner.nextFloat());
//         }
//         else {
//            next = scanner.next();
//            if (next.startsWith("#")) {
//               i--;
//               scanner.nextLine();
//            }
//         }
//      }
//
//      Point eye = new Point(floats.get(0), floats.get(1), floats.get(2));
//      Point lookat = new Point(floats.get(3), floats.get(4), floats.get(5));
//      Vector up  = new Vector(floats.get(6), floats.get(7), floats.get(8));
//
//      _currentTransformationMatrix = Transform.LookAt(eye, lookat, up);
//   }
//}
