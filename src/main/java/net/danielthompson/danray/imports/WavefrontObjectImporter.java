package net.danielthompson.danray.imports;

import net.danielthompson.danray.shapes.TriangleMesh;
import net.danielthompson.danray.structures.Point;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * User: daniel
 * Date: 7/13/13
 * Time: 15:30
 */
public class WavefrontObjectImporter {

   private File _file;

   private List<Point> _vertices;
   private List<List<Point>> _faces;

   public WavefrontObjectImporter(File file) {
      _file = file;
      _vertices = new ArrayList<Point>();
      _faces = new ArrayList<List<Point>>();

   }

   public TriangleMesh Process() {
      try {
         Scanner scanner = new Scanner(_file);
         while (scanner.hasNext()) {
            String token = scanner.next();
            if (token.equals("#"))
               scanner.nextLine();
            else {
               /*switch (token) {
                  case ("v"): {
                     ParseVertex(scanner.nextLine());
                     break;
                  }
                  case ("f"): {
                     ParseFace(scanner.nextLine());
                     break;
                  }
               }*/
            }
         }
      }
      catch (FileNotFoundException e) {
         System.out.println("Can't find file " + _file.getAbsolutePath());
         return null;
      }

      return new TriangleMesh(_vertices, _faces);
   }

   private void ParseVertex(String line) {
      String vertices[] = line.trim().split("\\s+");

      float vertex0 = Float.parseFloat(vertices[0]);
      float vertex1 = Float.parseFloat(vertices[1]);
      float vertex2 = Float.parseFloat(vertices[2]);

      _vertices.add(new Point(vertex0, vertex1, vertex2));
   }

   private void ParseFace(String line) {
      String faces[] = line.trim().split("\\s+");

      int face0 = Integer.parseInt(faces[0]) - 1;
      int face1 = Integer.parseInt(faces[1]) - 1;
      int face2 = Integer.parseInt(faces[2]) - 1;

      ArrayList<Point> face = new ArrayList<Point>();
      face.add(_vertices.get(face0));
      face.add(_vertices.get(face1));
      face.add(_vertices.get(face2));
      _faces.add(face);
   }
}