package net.danielthompson.danray;

import net.danielthompson.danray.structures.Normal;
import net.danielthompson.danray.structures.Point3;
import net.danielthompson.danray.structures.Ray;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Logging mechanism. By default, logs to a stdout and a file in the current trace directory.
 */
public class Logger {

   /**
    * Severity of the logged event.
    */
   public enum Level {
      /**
       * Informational events.
       */
      Info(1),

      /**
       * Recoverable errors that do not cause program shutdown.
       */
      Warning(2),

      /**
       * Unrecoverable errors that cause program shutdown.
       */
      Error(3),

      /**
       * Diagnostic-level events useful for debugging. Use caution; this logs a ton of information and
       * slows the rendering process considerably.
       */
      Debug(4);

      private int _value;

      /**
       * Create a new Level enum with the specified level.
       * @param value
       */
      Level(int value) {
         _value = value;
      }
   }

   /**
    * The current diagnostic level.
    */
   static Level LogLevel = Logger.Level.Error;

   /**
    * The date format to use for log messages.
    */
   private static SimpleDateFormat sdf = new SimpleDateFormat("[yyyy-MM-dd hh:mm:ss.SSS] ");

   /**
    * The list of output streams to log to. By default, contains stdout and a file in the trace directory.
    */
   private static final ArrayList<PrintStream> _streams = new ArrayList<PrintStream>();

   /**
    * Add the given PrintStream to the list of output streams.
    * @param stream The PrintStream to add to the list of output streams.
    */
   static void AddOutput(PrintStream stream) {
      _streams.add(stream);
   }

   /**
    * Adds a PrintStream created from the given file to the list of output streams.
    * @param file The output File to add to the list of output streams.
    */
   static void AddOutputFile(File file) {
      try {
         PrintStream stream = new PrintStream(file);
         _streams.add(stream);
      }
      catch (FileNotFoundException e) {
         System.out.println("Couldn't add output file:");
         System.out.println(e);
      }
   }

   /**
    * Log the given message at the given severity level.
    * @param level The severity level of the event.
    * @param text The event text.
    */
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
      if (level._value <= LogLevel._value) {
         String spacer = " ".repeat(depth);
         Log(level, spacer + text);
      }
   }

   public static void Log(Level level, int depth, Point3 p) {
      if (level._value <= LogLevel._value) {
         String spacer = " ".repeat(depth);
         Log(level, spacer + "Point: (" + p.x + ", " + p.y + ", " + p.z + ")");
      }
   }

   public static void Log(Level level, int depth, Normal n) {
      if (level._value <= LogLevel._value) {
         String spacer = " ".repeat(depth);
         Log(level, spacer + "normal: (" + n.x + ", " + n.y + ", " + n.z + ")");
      }
   }

   public static void Log(Level level, int depth, Ray r) {
      if (level._value <= LogLevel._value) {
         String spacer = " ".repeat(depth);
         Log(level, spacer + "Ray(Point({" + f(r.Origin.x) + ", " + f(r.Origin.y) + ", " + f(r.Origin.z) + "}), Vector(Point({" + f(r.Direction.x) + ", " + f(r.Direction.y) + ", " + f(r.Direction.z) + "})))");
         Log(level, spacer + "Origin: (" + r.Origin.x + ", " + r.Origin.y + ", " + r.Origin.z + ")");
         Log(level, spacer + "Direction: (" + r.Direction.x + ", " + r.Direction.y + ", " + r.Direction.z + ")");
      }
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
