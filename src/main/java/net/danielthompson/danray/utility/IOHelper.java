package net.danielthompson.danray.utility;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by daniel on 4/2/16.
 */
public class IOHelper {

   private String _outputDirectory;
   private File _logFile;

   public void CreateOutputDirectory() {
      Date date = Calendar.getInstance().getTime();

      SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
      SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
      SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
      SimpleDateFormat timeFormat = new SimpleDateFormat("HH-mm-ss");

      _outputDirectory = "traces"
            + File.separator + yearFormat.format(date)
            + File.separator + monthFormat.format(date)
            + File.separator + dayFormat.format(date)
            + File.separator + timeFormat.format(date)
      ;

      new File(_outputDirectory).mkdirs();
      _logFile = new File(_outputDirectory + File.separator + "log.txt");
   }

   public File GetLogFile() {
      return _logFile;
   }

   public void Save(BufferedImage image, String filename) {
      Date date = Calendar.getInstance().getTime();
      //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");


      try {
         ImageIO.write(image, "png", new File(_outputDirectory + File.separator + filename + /*" " + sdf.format(date) +*/ ".png"));
      }
      catch (IOException e) {
         System.err.println("image not saved.");
      }
   }
}
