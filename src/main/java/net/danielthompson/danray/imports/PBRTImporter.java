package net.danielthompson.danray.imports;

import net.danielthompson.danray.scenes.AbstractScene;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Transform;
import net.danielthompson.danray.structures.Vector;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Pattern;

public class PBRTImporter extends AbstractFileImporter<AbstractScene> {

   public PBRTImporter(File file) {
      super(file);
   }

   private Transform _currentTransformationMatrix;

   @Override
   public AbstractScene Process() {
      try {
         Scanner scanner = new Scanner(file);

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
            }

         }

      } catch (FileNotFoundException e) {
         e.printStackTrace();
      }

      return null;
   }

   private void parseLookAt(Scanner scanner) {
      String next;
      List<Float> floats = new ArrayList<>();

      while (scanner.hasNext()) {
         if (scanner.hasNextFloat()) {
            floats.add(scanner.nextFloat());
         }
         else {
            next = scanner.next();
            if (next.startsWith("#")) {
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
