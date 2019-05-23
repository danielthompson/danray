package test.camera.aperture;

import net.danielthompson.danray.cameras.apertures.Aperture;
import net.danielthompson.danray.cameras.apertures.CircleAperture;
import net.danielthompson.danray.cameras.apertures.SquareAperture;
import net.danielthompson.danray.structures.Point3;
import net.danielthompson.danray.ui.MainCanvas;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by daniel on 7/28/14.
 */
public class SquareApertureTests {

   @Test
   public void DrawSquareAperture() {
      Aperture squareAperture = new SquareAperture(100);
      try {
         DrawAperture(squareAperture);
      }
      catch (HeadlessException e)
      {

      }
   }

   @Test
   public void DrawCircularAperture() {
      Aperture circularAperture = new CircleAperture(100);

      try {
         DrawAperture(circularAperture);
      }
      catch (HeadlessException e)
      {

      }
   }

   public void DrawAperture(Aperture aperture) {
      int X = 200;
      int Y = 200;

      int[][] accumulationBuffer = new int[200][200];

      BufferedImage image = new BufferedImage(X, Y, BufferedImage.TYPE_INT_RGB);
      Canvas canvas = new MainCanvas(image);

      Frame frame = new Frame("DanRay: Square aperture");
      frame.add("Center", canvas);
      frame.setSize(new Dimension(X, Y + 22));
      frame.setVisible(true);

      int max = 0;

      for (int i = 0; i < 100000; i++){
         Point3 point = aperture.GetOriginPoint();

         int x = (int)(point.x + 100);
         int y = (int)(point.y + 100);

         accumulationBuffer[x][y]++;
         if (accumulationBuffer[x][y] > max) {
            max = accumulationBuffer[x][y];
         }
      }

      float linearScaleFactor = 255.0f / (float)max;

      for (int x = 0; x < accumulationBuffer.length; x++) {
         for (int y = 0; y < accumulationBuffer[x].length; y++) {
            accumulationBuffer[x][y] = (int)((float)accumulationBuffer[x][y] * linearScaleFactor);
            int value = accumulationBuffer[x][y];
            Color newColor = new Color(value, value, value);
            image.setRGB(x, y, newColor.getRGB());
         }
      }

      try {
         ImageIO.write(image, "png", new File("aperture.png"));
      }
      catch (IOException e) {
         System.out.println("Couldn't save aperture picture");
      }

   }
}
