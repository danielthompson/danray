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
public class CloseEnoughTests {
   @BeforeMethod
   public void setUp() throws Exception {

   }

   @AfterMethod
   public void tearDown() throws Exception {

   }

   @Test
   public void testCloseEnough1() throws Exception {

      Color color1 = new Color(100, 100, 100);
      Color color2 = new Color(110, 110, 110);
      float percent = .11f;

      boolean actual = Blender.CloseEnough(color1, color2, percent);
      boolean expected = true;
      Assert.assertEquals(actual, expected);

   }

   @Test
   public void testCloseEnough2() throws Exception {

      Color color1 = new Color(100, 100, 100);
      Color color2 = new Color(110, 110, 110);
      float percent = .09f;

      boolean actual = Blender.CloseEnough(color1, color2, percent);
      boolean expected = true;
      Assert.assertEquals(actual, expected);

   }

   @Test
   public void testCloseEnough3() throws Exception {

      Color color1 = new Color(100, 100, 100);
      Color color2 = new Color(110, 110, 110);
      float percent = .10f;

      boolean actual = Blender.CloseEnough(color1, color2, percent);
      boolean expected = true;
      Assert.assertEquals(actual, expected);
   }

   @Test
   public void testCloseEnough4() throws Exception {

      Color color1 = new Color(100, 110, 100);
      Color color2 = new Color(110, 100, 110);
      float percent = .10f;

      boolean actual = Blender.CloseEnough(color1, color2, percent);
      boolean expected = true;
      Assert.assertEquals(actual, expected);
   }

   @Test
   public void testCloseEnough5() throws Exception {

      for (float percent = .10f; percent < .90f; percent += .01f) {
         Color color1 = new Color(200, 220, 200);
         Color color2 = new Color(220, 200, 220);

         boolean actual = Blender.CloseEnough(color1, color2, percent);
         boolean expected = true;
         Assert.assertEquals(actual, expected, "should be within percent " + percent);
      }
   }
}
