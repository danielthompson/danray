package test.shading.spectralblender;

import net.danielthompson.danray.shading.fullspectrum.*;
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
      FullSpectralPowerDistribution spd = new FullSpectralPowerDistribution();
      for (int i = 0; i < spd.Buckets.length; i++) {
         spd.Buckets[i] = (48 - i) * 2;
      }

      Color c = FullSpectralBlender.ConvertSPDtoRGB(spd);

      System.out.println(c);
      System.out.println("");
   }

   @Test
   public void testConvertTristimulus() throws Exception {
      float x = 3f;
      float y = 3f;
      float z = 3f;

      FullSpectralBlender.setFilmSpeed(1);

      Color c = FullSpectralBlender.ConvertXYZtoRGB(x, y, z, null);

      System.out.println("");
   }

   @Test
   public void testD65OnLemon() throws Exception {
      FullSpectralBlender.setFilmSpeed(.1f);

      for (float i = 0; i < 20f; i += .05f) {

         FullSpectralPowerDistribution d65 = new FullSpectralPowerDistribution(RelativeFullSpectralPowerDistributionLibrary.D65, i);

         FullSpectralReflectanceCurve lemon = FullSpectralReflectanceCurveLibrary.LemonSkin;

         FullSpectralPowerDistribution result = d65.reflectOff(lemon);



         Color c = FullSpectralBlender.ConvertSPDtoRGB(result);

         System.out.println("power = " + i + ", color = " + c.toString());
      }

      System.out.println("");
   }

}
