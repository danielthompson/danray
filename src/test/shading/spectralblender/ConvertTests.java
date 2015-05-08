package test.shading.spectralblender;

import net.danielthompson.danray.shading.Blender;
import net.danielthompson.danray.shading.SpectralBlender;
import net.danielthompson.danray.shading.SpectralPowerDistribution;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.awt.*;

/**
 * Created by dthompson on 08 May 15.
 */
public class ConvertTests {
   @BeforeMethod
   public void setUp() throws Exception {

   }

   @AfterMethod
   public void tearDown() throws Exception {

   }

   @Test
   public void testConvert1() throws Exception {
      SpectralPowerDistribution spd = new SpectralPowerDistribution();
      for (int i = 0; i < spd.Buckets.length; i++) {
         spd.Buckets[i] = .01f * (i);
      }

      Color c = SpectralBlender.Convert(spd);

      System.out.println(c);
      System.out.println("");
   }

   @Test
   public void testConvertTristimulus() throws Exception {
      float x = .5f;
      float y = .5f;
      float z = .6f;

      Color c = SpectralBlender.ConvertTristumulus(x, y, z);

      System.out.println("");
   }

}
