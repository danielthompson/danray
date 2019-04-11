package net.danielthompson.danray.imports;

import net.danielthompson.danray.Logger;
import net.danielthompson.danray.cameras.Camera;
import net.danielthompson.danray.cameras.CameraSettings;
import net.danielthompson.danray.cameras.PerspectiveCamera;
import net.danielthompson.danray.imports.pbrt.Constants;
import net.danielthompson.danray.imports.pbrt.PBRTDirective;
import net.danielthompson.danray.imports.pbrt.directives.*;
import net.danielthompson.danray.scenes.AbstractScene;
import net.danielthompson.danray.scenes.NaiveScene;
import net.danielthompson.danray.structures.Pair;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Transform;
import net.danielthompson.danray.structures.Vector;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PBRTImporter extends AbstractFileImporter<AbstractScene> {

   public PBRTImporter(File file) {
      super(file);
      _currentTransformationMatrix = Transform.identity;
   }

   private Transform _currentTransformationMatrix;
   private Camera _camera;
   private CameraSettings _cameraSettings;
   private AbstractScene _abstractScene;

   @Override
   public AbstractScene Process() {
      List<List<String>> tokens = Lex();
      Pair<List<PBRTDirective>, List<PBRTDirective>> parsedDirectives = Parse(tokens);

      List<PBRTDirective> worldDirectives = parsedDirectives.Item1;
      List<PBRTDirective> sceneDirectives = parsedDirectives.Item2;

      Camera camera;



      for (PBRTDirective directive : sceneDirectives) {
         switch (directive.Identifier) {
            case Constants.LookAt: {
               Transform transform = ((LookAtDirective)directive).Parse();
               _currentTransformationMatrix.Apply(transform);
               break;
            }
            case Constants.Camera: {
               CameraDirective cameraDirective = (CameraDirective)directive;
               //camera = cameraDirective.Parse();
               break;
            }
         }
      }

      return null;
   }

   public List<List<String>> Lex(String s) {
      Scanner scanner = new Scanner(s);
      return Lex(scanner);
   }

   public List<List<String>> Lex() {
      Scanner scanner = null;
      try {
         scanner = new Scanner(file);
         return Lex(scanner);
      }
      catch (FileNotFoundException e) {
         e.printStackTrace();
         throw new RuntimeException();
      }
   }

   public List<List<String>> Lex(Scanner scanner) {

      List<List<String>> tokens = new ArrayList<>();

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

               if (Constants.AllDirectives.contains(word)) {
                  // if this is a directive, then we move on to a new line
                  targetLineNumber++;
                  tokens.get(targetLineNumber).add(word);
                  continue;
               }

               // split brackets, if needed
               if (word.length() > 1) {
                  int lastIndex = word.length() - 1;

                  if (StartBracketed(word)) {
                     tokens.get(targetLineNumber).add("[");
                     tokens.get(targetLineNumber).add(word.substring(1, lastIndex));
                  }
                  else if (EndBracketed(word)) {
                     tokens.get(targetLineNumber).add(word.substring(0, lastIndex));
                     tokens.get(targetLineNumber).add("]");
                  }
                  else if (IsBracketed(word)) {
                     tokens.get(targetLineNumber).add("[");
                     tokens.get(targetLineNumber).add(word.substring(1, lastIndex));
                     tokens.get(targetLineNumber).add("]");
                  }
                  else if (StartQuoted(word)) {
                     tokens.get(targetLineNumber).add("\"");
                     tokens.get(targetLineNumber).add(word.substring(1));
                  }
                  else if (EndQuoted(word)) {
                     tokens.get(targetLineNumber).add(word.substring(0, lastIndex));
                     tokens.get(targetLineNumber).add("\"");
                  }
                  else if (IsQuoted(word)) {
                     tokens.get(targetLineNumber).add("\"");
                     tokens.get(targetLineNumber).add(word.substring(1, lastIndex));
                     tokens.get(targetLineNumber).add("\"");
                  }

                  else {
                     tokens.get(targetLineNumber).add(word);
                  }
               }
               else {
                  tokens.get(targetLineNumber).add(word);
               }

            }

            int lastIndex = tokens.size()- 1;

            if (tokens.get(lastIndex).size() == 0)
               tokens.remove(lastIndex);

            sourceLineNumber++;

         }

      }
      else {
         Logger.Log(Logger.Level.Error, "Input file " + file.getAbsolutePath() + " had no content, aborting.");
         throw new RuntimeException();
      }

      return tokens;
   }

   public Pair<List<PBRTDirective>, List<PBRTDirective>> Parse(List<List<String>> tokens) {
      List<PBRTDirective> sceneDirectives = new ArrayList<>();
      List<PBRTDirective> worldDirectives = new ArrayList<>();
      List<PBRTDirective> currentDirectives = sceneDirectives;

      for (List<String> line : tokens) {

         if (line.size() == 0)
            continue;

         String directiveName = line.get(0);

         PBRTDirective directive = null;

         switch (directiveName) {
            case Constants.AttributeBegin: {
               directive = new AttributeBeginDirective();
               break;
            }
            case Constants.AttributeEnd: {
               directive = new AttributeEndDirective();
               break;
            }
            case Constants.Camera: {
               directive = new CameraDirective(line);
               break;
            }
            case Constants.Film: {
               directive = new FilmDirective(line);
               break;
            }
            case Constants.Integrator: {
               directive = new IntegratorDirective(line);
               break;
            }
            case Constants.LightSource: {
               directive = new LightSourceDirective(line);
               break;
            }
            case Constants.LookAt: {
               directive = new LookAtDirective(line);
               break;
            }
            case Constants.Material: {
               directive = new MaterialDirective(line);
               break;
            }
            case Constants.Sampler: {
               directive = new SamplerDirective(line);
               break;
            }
            case Constants.Shape: {
               directive = new ShapeDirective(line);
               break;
            }
            case Constants.Texture: {
               directive = new TextureDirective(line);
               break;
            }
            case Constants.Translate: {
               directive = new TranslateDirective(line);
               break;
            }
            case Constants.WorldBegin: {
               currentDirectives = worldDirectives;
               break;
            }
            case Constants.WorldEnd: {
               currentDirectives = null;
               break;
            }
            default: {
               Logger.Log(Logger.Level.Error, "Found unknown directive during parsing: " + directiveName);
               throw new RuntimeException();
            }
         }

         if (currentDirectives != null)
            currentDirectives.add(directive);
      }

      Pair<List<PBRTDirective>, List<PBRTDirective>> returnValues = new Pair<>();
      returnValues.Item1 = worldDirectives;
      returnValues.Item2 = sceneDirectives;

      return returnValues;
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

   private boolean IsBracketed(String token) {
      return (token.charAt(0) == '[' && token.charAt(token.length() - 1) == ']');
   }

   private boolean StartBracketed(String token) {
      return (token.charAt(0) == '[' && token.charAt(token.length() - 1) != ']');
   }

   private boolean EndBracketed(String token) {
      return (token.charAt(0) != '[' && token.charAt(token.length() - 1) == ']');
   }

   private boolean IsQuoted(String token) {
      return (token.charAt(0) == '"' && token.charAt(token.length() - 1) == '"');
   }

   private boolean StartQuoted(String token) {
      return (token.charAt(0) == '"' && token.charAt(token.length() - 1) != '"');
   }

   private boolean EndQuoted(String token) {
      return (token.charAt(0) != '"' && token.charAt(token.length() - 1) == '"');
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
