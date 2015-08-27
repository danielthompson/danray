package test.lights;

import net.danielthompson.danray.lights.SpectralSphereLight;
import net.danielthompson.danray.shading.*;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.*;
import net.danielthompson.danray.structures.Point;
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


   @Test
   public void testGetRayInPDF1() throws Exception {

      SpectralSphereLight light = new SpectralSphereLight(1);
      light.Origin = new Point(0, 0, 0);
      light.Radius = 1;

      double maxTheta = -Double.MAX_VALUE;
      double minTheta = Double.MAX_VALUE;

      for (int i = 0; i < 1000000; i++) {

         Ray ray = light.getRandomRayInPDF();

         // the angle between these must be <= 90

         Vector directionFromOriginToSurfacePoint = Vector.Minus(ray.Origin, light.Origin);
         Vector directionFromSurfacePoint = ray.Direction;

         double cosTheta = directionFromOriginToSurfacePoint.Dot(directionFromSurfacePoint);

         if (cosTheta > maxTheta)
            maxTheta = cosTheta;
         if (cosTheta < minTheta)
            minTheta = cosTheta;

      }
      System.err.println("Min Theta: [" + minTheta + "]");
      System.err.println("Max Theta: [" + maxTheta + "]");

      Assert.assertTrue(minTheta >= 0.0);
      Assert.assertTrue(maxTheta <= 1.0);
   }


   @Test
   public void testGetRayInPDF2() throws Exception {

      SpectralSphereLight light = new SpectralSphereLight(1);
      light.Origin = new Point(0, 0, 0);
      light.Radius = 1;

      Scene scene = new NaiveScene(null);

      scene.addDrawableObject(light);
      scene.addRadiatableObject(light);

      for (int i = 0; i < 1000000; i++) {

         Ray ray = light.getRandomRayInPDF();

         IntersectionState state = scene.GetClosestDrawableToRay(ray);

         Assert.assertNull(state);


      }

   }
}