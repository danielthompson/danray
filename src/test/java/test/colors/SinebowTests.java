package test.colors;

import net.danielthompson.danray.SceneBuilder;
import org.testng.annotations.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SinebowTests {

   @Test
   public void load() {
      BufferedImage image = SceneBuilder.Sinebow.Load(SceneBuilder.Sinebow.Spectral);
      int width = image.getWidth();
      int steps = 10;
      int step = width / steps;

      int j = 0;

      for (int i = 0; i < steps; i++) {
         Color c = new Color(image.getRGB(i * step, 0));
         int r = c.getRed();
         int g = c.getGreen();
         int b = c.getBlue();
         System.out.println("public static Color Color" + j + " = new Color(" + r + ", " + g + ", " + b + ");");
         j++;
      }

   }
}
