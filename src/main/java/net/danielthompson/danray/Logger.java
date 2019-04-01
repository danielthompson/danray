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

   public enum Level {
      Info(1),
      Warning(2),
      Error(3),
      Debug(4);

      private int _value;

      Level(int value) {
         _value = value;
      }

      public int getNumVal() {
         return _value;
      }
   }

   public static Level LogLevel = Logger.Level.Error;

   private static SimpleDateFormat sdf = new SimpleDateFormat("[yyyy-MM-dd hh:mm:ss.SSS] ");

   private static final ArrayList<PrintStream> _streams = new ArrayList<PrintStream>();

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

   public static void Log(Level level, String text) {
      if (level._value <= LogLevel._value) {
         Date date = Calendar.getInstance().getTime();
         String logText = " [" + level.name().substring(0, 1) + "] " + sdf.format(date) + text;
         synchronized (_streams) {
            for (PrintStream stream : _streams) {
               stream.println(logText);
               stream.flush();
            }
         }
      }
   }

   public static void Log(Level level, int depth, String text) {
      String spacer = " ".repeat(depth);
      Log(level, spacer + text);
   }

   public static void Log(Level level, int depth, Point p) {
      String spacer = " ".repeat(depth);
      Log(level, spacer + "Point: (" + p.X + ", " + p.Y + ", " + p.Z + ")");
   }

   public static void Log(Level level, int depth, Normal n) {
      String spacer = " ".repeat(depth);
      Log(level, spacer + "Normal: (" + n.X + ", " + n.Y + ", " + n.Z + ")");
   }

   public static void Log(Level level, int depth, Ray r) {
      String spacer = " ".repeat(depth);
      Log(level, spacer + "Ray(Point({" + f(r.Origin.X) + ", " + f(r.Origin.Y) + ", " + f(r.Origin.Z) + "}), Vector(Point({" + f(r.Direction.X) + ", " + f(r.Direction.Y) + ", " + f(r.Direction.Z) + "})))");
      Log(level, spacer + "Origin: (" + r.Origin.X + ", " + r.Origin.Y + ", " + r.Origin.Z + ")");
      Log(level, spacer + "Direction: (" + r.Direction.X + ", " + r.Direction.Y + ", " + r.Direction.Z + ")");
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
