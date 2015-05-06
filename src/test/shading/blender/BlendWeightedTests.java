package test.shading.blender;

import net.danielthompson.danray.shading.Blender;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.awt.*;

/**
 * Created by daniel on 3/4/14.
 */
public class BlendWeightedTests {
   @BeforeMethod
   public void setUp() throws Exception {

   }

   @AfterMethod
   public void tearDown() throws Exception {

   }

   @Test
   public void testBlend1() throws Exception {

      Color color1 = Color.red;
      int r = color1.getRed();
      int g = color1.getGreen();
      int b = color1.getBlue();

      System.out.println("Red = [" + r + "] Green = [" + g + "] Blue = [" + b + "]");

      Color color2 = Color.blue;
      r = color2.getRed();
      g = color2.getGreen();
      b = color2.getBlue();

      System.out.println("Red = [" + r + "] Green = [" + g + "] Blue = [" + b + "]");
      Color blended = Blender.BlendWeighted(color1, 1, color2, 1);
      r = blended.getRed();
      g = blended.getGreen();
      b = blended.getBlue();

      System.out.println("Red = [" + r + "] Green = [" + g + "] Blue = [" + b + "]");

      Assert.assertTrue(r == 128);
      Assert.assertTrue(g == 0);
      Assert.assertTrue(b == 128);
   }

   @Test
   public void testBlend2() throws Exception {

      Color color1 = Color.red;
      int r = color1.getRed();
      int g = color1.getGreen();
      int b = color1.getBlue();

      System.out.println("Red = [" + r + "] Green = [" + g + "] Blue = [" + b + "]");

      Color color2 = Color.blue;
      r = color2.getRed();
      g = color2.getGreen();
      b = color2.getBlue();

      System.out.println("Red = [" + r + "] Green = [" + g + "] Blue = [" + b + "]");
      Color blended = Blender.BlendWeighted(color1, 1, color2, 2);
      r = blended.getRed();
      g = blended.getGreen();
      b = blended.getBlue();

      System.out.println("Red = [" + r + "] Green = [" + g + "] Blue = [" + b + "]");

      Assert.assertTrue(r == 85);
      Assert.assertTrue(g == 0);
      Assert.assertTrue(b == 170);
   }
}
