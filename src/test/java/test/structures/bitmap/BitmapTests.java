package test.structures.bitmap;

import net.danielthompson.danray.SceneBuilder;
import net.danielthompson.danray.structures.Bitmap;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BitmapTests {

   @Test
   public void test1() {
      BufferedImage expected = SceneBuilder.Skyboxes.Load("images/colors/rainbow.png");

      if (expected.getType() != BufferedImage.TYPE_4BYTE_ABGR) {
         BufferedImage convertedImg = new BufferedImage(expected.getWidth(), expected.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
         Graphics graphics = convertedImg.getGraphics();
         graphics.drawImage(expected, 0, 0, null);
         graphics.dispose();
         expected = convertedImg;
      }

      Bitmap bitmap = new Bitmap(expected);
      BufferedImage actual = bitmap.export();

      Assert.assertNotNull(actual);

      Assert.assertEquals(actual.getType(), expected.getType());

      Assert.assertEquals(actual.getWidth(), expected.getWidth());
      Assert.assertEquals(actual.getHeight(), expected.getHeight());

      for (int y = 0; y < expected.getHeight(); y++) {
         for (int x = 0; x < expected.getWidth(); x++) {
            final int expectedRGB = expected.getRGB(x, y);
            final int actualRGB = actual.getRGB(x, y);
            Assert.assertEquals(actualRGB, expectedRGB);
         }
      }
   }
}
