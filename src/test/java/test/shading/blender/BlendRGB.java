package test.shading.blender;

import net.danielthompson.danray.shading.Blender;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.awt.*;

/**
 * DanRay
 * User: dthompson
 * Date: 7/8/13
 * Time: 2:37 PM
 */
public class BlendRGB {
   @BeforeMethod
   public void setUp() throws Exception {

   }

   @AfterMethod
   public void tearDown() throws Exception {

   }

   @Test
   public void testBlend1() throws Exception {

      Color color1 = Color.red;
      Color color2 = Color.blue;
      Color blended = Blender.BlendRGB(color1, color2, .5f);
      int r = blended.getRed();
      int g = blended.getGreen();
      int b = blended.getBlue();

      System.out.println("Red = [" + r + "] Green = [" + g + "] Blue = [" + b + "]");
   }
}
