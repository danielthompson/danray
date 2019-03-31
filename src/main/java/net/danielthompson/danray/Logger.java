package net.danielthompson.danray;

import net.danielthompson.danray.structures.Normal;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Ray;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by daniel on 1/3/15.
 */
public class Logger {

   private static SimpleDateFormat sdf = new SimpleDateFormat("[yyyy-MM-dd hh:mm:ss.SSS] ");

   private static ArrayList<PrintStream> _streams = new ArrayList<PrintStream>();

   public static void AddOutput(PrintStream stream) {
      _streams.add(stream);
   }

   public static void AddOutputFile(File file) {
      try {
         PrintStream stream = new PrintStream(file);
         _streams.add(stream);
      }
      catch (FileNotFoundException e) {
         System.out.println("Couldn't add output file:");
         System.out.println(e);
      }
   }

   public static void Log(String text) {
      if (true) {
         Date date = Calendar.getInstance().getTime();

         synchronized (_streams) {
            for (PrintStream stream : _streams) {
               stream.println(sdf.format(date) + text);
               stream.flush();
            }
         }
      }
   }

   public static void Log(int depth, String text) {
      String spacer = " ".repeat(depth);
      Log(spacer + text);
   }

   public static void Log(Point p) {
      Log("X: " + p.X + " Y: " + p.Y + " Z: " + p.Z);
   }

   public static void Log(int depth, Point p) {
      String spacer = " ".repeat(depth);
      Log(spacer + "Point: (" + p.X + ", " + p.Y + ", " + p.Z + ")");
   }

   public static void Log(int depth, Normal n) {
      String spacer = " ".repeat(depth);
      Log(spacer + "Normal: (" + n.X + ", " + n.Y + ", " + n.Z + ")");
   }

   public static void Log(int depth, Ray r) {
      String spacer = " ".repeat(depth);
      Log(spacer + "Ray(Point({" + f(r.Origin.X) + ", " + f(r.Origin.Y) + ", " + f(r.Origin.Z) + "}), Vector(Point({" + f(r.Direction.X) + ", " + f(r.Direction.Y) + ", " + f(r.Direction.Z) + "})))");
      Log(spacer + "Origin: (" + r.Origin.X + ", " + r.Origin.Y + ", " + r.Origin.Z + ")");
      Log(spacer + "Direction: (" + r.Direction.X + ", " + r.Direction.Y + ", " + r.Direction.Z + ")");
   }

   private static String f(float f) {
      return String.format("%.3f", f);
   }

   public static void Flush() {
      for (PrintStream stream : _streams) {
         stream.flush();
         stream.close();
      }
   }
}
