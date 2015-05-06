package net.danielthompson.danray;

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
      Date date = Calendar.getInstance().getTime();

      for (PrintStream stream : _streams) {
         stream.println(sdf.format(date) + text);
         stream.flush();
      }
   }


   public static void Flush() {
      for (PrintStream stream : _streams) {
         stream.flush();
         stream.close();
      }
   }
}
