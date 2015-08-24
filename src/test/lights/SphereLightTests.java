package test.lights;

import net.danielthompson.danray.lights.SpectralSphereLight;
import net.danielthompson.danray.shading.*;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Vector;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.awt.*;


/**
 * Created by dthompson on 08 May 15.
 */
public class SphereLightTests {
   @BeforeMethod
   public void setUp() throws Exception {

   }

   @AfterMethod
   public void tearDown() throws Exception {

   }

   @Test
   public void testGetPDF() throws Exception {

      SpectralSphereLight light = new SpectralSphereLight(1);
      light.Origin = new Point(0, 0, 0);
      light.Radius = 1;

      Point pointOnLight = new Point(1, 0, 0);

      Point pointInSpace = new Point(2, 0, 0);

      Vector direction = Vector.Minus(pointOnLight, pointInSpace);

      double expectedPDF = 1.0;
      double actualPDF = light.getPDF(pointInSpace, direction);

      Assert.assertEquals(actualPDF, expectedPDF);

   }


}
