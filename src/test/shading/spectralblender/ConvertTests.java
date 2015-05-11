package test.shading.spectralblender;

import net.danielthompson.danray.shading.*;
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
         spd.Buckets[i] = (48 - i) * 2;
      }

      Color c = SpectralBlender.Convert(spd);

      System.out.println(c);
      System.out.println("");
   }

   @Test
   public void testConvertTristimulus() throws Exception {
      float x = 3f;
      float y = 3f;
      float z = 3f;

      SpectralBlender.setFilmSpeed(1);

      Color c = SpectralBlender.ConvertTristumulus(x, y, z);

      System.out.println("");
   }

   @Test
   public void testD65OnLemon() throws Exception {
      SpectralBlender.setFilmSpeed(.1f);

      for (float i = 0; i < 20f; i += .05f) {

         SpectralPowerDistribution d65 = new SpectralPowerDistribution(RelativeSpectralPowerDistributionLibrary.D65, i);

         SpectralReflectanceCurve lemon = SpectralReflectanceCurveLibrary.LemonSkin;

         SpectralPowerDistribution result = d65.reflectOff(lemon);



         Color c = SpectralBlender.Convert(result);

         System.out.println("power = " + i + ", color = " + c.toString());
      }

      System.out.println("");
   }

}
