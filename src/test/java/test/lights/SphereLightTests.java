//package test.lights;
//
//import net.danielthompson.danray.scenes.NaiveScene;
//import net.danielthompson.danray.scenes.AbstractScene;
//import net.danielthompson.danray.states.IntersectionState;
//import net.danielthompson.danray.structures.*;
//import net.danielthompson.danray.structures.Point;
//import org.testng.Assert;
//import org.testng.annotations.AfterMethod;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Test;
//
//
///**
// * Created by dthompson on 08 May 15.
// */
//public class SphereLightTests {
//   @BeforeMethod
//   public void setUp() throws Exception {
//
//   }
//
//   @AfterMethod
//   public void tearDown() throws Exception {
//
//   }
//
//   @Test
//   public void testGetPDF() throws Exception {
//
//      SpectralSphereLight light = new SpectralSphereLight(1);
//      light.Origin = new Point(0, 0, 0);
//      light.Radius = 1;
//
//      Point pointOnLight = new Point(1, 0, 0);
//
//      Point pointInSpace = new Point(2, 0, 0);
//
//      Vector direction = Vector.Minus(pointOnLight, pointInSpace);
//
//      float expectedPDF = 1.0f;
//      float actualPDF = light.getPDF(pointInSpace, direction);
//
//      AssertHelper.assertEquals(actualPDF, expectedPDF);
//
//   }
//
//
//   @Test
//   public void testGetRayInPDF1() throws Exception {
//
//      SpectralSphereLight light = new SpectralSphereLight(1);
//      light.Origin = new Point(0, 0, 0);
//      light.Radius = 1;
//
//      float maxTheta = -Float.MAX_VALUE;
//      float minTheta = Float.MAX_VALUE;
//
//      for (int i = 0; i < 1000000; i++) {
//
//         Ray ray = light.getRandomRayInPDF();
//
//         // the angle between these must be <= 90
//
//         Vector directionFromOriginToSurfacePoint = Vector.Minus(ray.Origin, light.Origin);
//         Vector directionFromSurfacePoint = ray.Direction;
//
//         float cosTheta = directionFromOriginToSurfacePoint.Dot(directionFromSurfacePoint);
//
//         if (cosTheta > maxTheta)
//            maxTheta = cosTheta;
//         if (cosTheta < minTheta)
//            minTheta = cosTheta;
//
//      }
//      System.err.println("Min Theta: [" + minTheta + "]");
//      System.err.println("Max Theta: [" + maxTheta + "]");
//
//      Assert.assertTrue(minTheta >= 0.0);
//      Assert.assertTrue(maxTheta <= 1.0);
//   }
//
//
//   @Test
//   public void testGetRayInPDF2() throws Exception {
//
//      SpectralSphereLight light = new SpectralSphereLight(1);
//      light.Origin = new Point(0, 0, 0);
//      light.Radius = 1;
//
//      AbstractScene scene = new NaiveScene(null);
//
//      scene.addShape(light);
//      scene.addLight(light);
//
//      for (int i = 0; i < 1000000; i++) {
//
//         Ray ray = light.getRandomRayInPDF();
//
//         IntersectionState state = scene.getNearestShape(ray);
//
//         Assert.assertNull(state);
//
//
//      }
//
//   }
//}