package test.camera.DepthOfFieldCamera;

import net.danielthompson.danray.cameras.Camera;
import net.danielthompson.danray.cameras.CameraSettings;
import net.danielthompson.danray.cameras.DepthOfFieldCamera;
import net.danielthompson.danray.cameras.SimplePointableCamera;
import net.danielthompson.danray.cameras.apertures.Aperture;
import net.danielthompson.danray.cameras.apertures.SquareAperture;
import net.danielthompson.danray.structures.Constants;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Vector;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.AssertHelper;

/**
 * Created by daniel on 1/19/14.
 */
public class WorldPointForPixelTests {
   @BeforeMethod
   public void setUp() throws Exception {

   }

   @AfterMethod
   public void tearDown() throws Exception {

   }

   @Test
   public void test1() throws Exception {
      Point origin = new Point(100, 100, 100);
      Vector direction = new Vector(0, 0, -1);
      Ray orientation = new Ray(origin, direction);

      Aperture aperture = new SquareAperture(5);

      CameraSettings settings = new CameraSettings();
      settings.X = 5;
      settings.Y = 5;
      settings.FocalLength = 1;
      settings.FocusDistance = 5;
      settings.Orientation = orientation;
      settings.Aperture = aperture;

      DepthOfFieldCamera camera = new DepthOfFieldCamera(settings);


      for (int i = 0; i < 1000; i++) {
         Point worldPoint = camera.GetStochasticWorldPointInApertureForPixel(0, 0);

         Assert.assertTrue(worldPoint.X > 97.5f);
         Assert.assertTrue(worldPoint.X < 102.5f);
         Assert.assertTrue(worldPoint.Y > 97.5f);
         Assert.assertTrue(worldPoint.Y < 102.5f);
         AssertHelper.assertEquals(worldPoint.Z, 100.0f);
      }

   }
//
//   @Test
//   public void test2() throws Exception {
//      Point origin = new Point(0, 0, 0);
//      Vector direction = new Vector(0, 0, -1);
//      Ray orientation = new Ray(origin, direction);
//
//      Aperture aperture = new SquareAperture(1);
//
//      CameraSettings settings = new CameraSettings();
//      settings.X = 10;
//      settings.Y = 6;
//      settings.FocalLength = 1;
//      settings.FocusDistance = 5;
//      settings.Orientation = orientation;
//      settings.Aperture = aperture;
//
//      DepthOfFieldCamera camera = new DepthOfFieldCamera(settings);
//
//      Point worldPoint = camera.getWorldPointForPixel(0, 0);
//      AssertHelper.assertEquals(worldPoint.X, -5.0);
//      AssertHelper.assertEquals(worldPoint.Y, 3.0);
//      AssertHelper.assertEquals(worldPoint.Z, 0.0);
//
//      Ray[] rays = camera.getRays(0, 0, 1);
//      AssertHelper.assertEquals(rays[0].Origin.X, -5.0);
//      AssertHelper.assertEquals(rays[0].Origin.Y, 3.0);
//      AssertHelper.assertEquals(rays[0].Origin.Z, 0.0);
//
//      rays = camera.getRays(0, 0, 2);
//      AssertHelper.assertEquals(rays[1].Origin.X, -5.0);
//      AssertHelper.assertEquals(rays[1].Origin.Y, 3.0);
//      AssertHelper.assertEquals(rays[1].Origin.Z, 0.0);
//
//   }

   @Test
   public void test3() throws Exception {
      Point origin = new Point(0, 0, 0);
      Vector direction = new Vector(0, 0, -1);
      Ray orientation = new Ray(origin, direction);

      CameraSettings settings = new CameraSettings();
      settings.X = 10;
      settings.Y = 6;
      settings.FocalLength = 1;
      settings.ZoomFactor = 2;
      settings.Orientation = orientation;

      Camera camera = new SimplePointableCamera(settings);

      Point worldPoint = camera.getWorldPointForPixel(0, 0);
      AssertHelper.assertEquals(worldPoint.X, -10.0f);
      AssertHelper.assertEquals(worldPoint.Y, 6.0f);
      AssertHelper.assertEquals(worldPoint.Z, 0.0f);

   }

   @Test
   public void test4() throws Exception {
      Point origin = new Point(0, 0, 0);
      Vector direction = new Vector(0, 0, -1);
      Ray orientation = new Ray(origin, direction);

      CameraSettings settings = new CameraSettings();
      settings.X = 10;
      settings.Y = 6;
      settings.FocalLength = 1;
      settings.ZoomFactor = 2;
      settings.Orientation = orientation;

      Camera camera = new SimplePointableCamera(settings);

      Point worldPoint = camera.getWorldPointForPixel(1, 1);
      AssertHelper.assertEquals(worldPoint.X, -8.0f);
      AssertHelper.assertEquals(worldPoint.Y, 4.0f);
      AssertHelper.assertEquals(worldPoint.Z, 0.0f);

   }

   @Test
   public void rotateTest1() throws Exception {
      Point origin = new Point(0, 0, 0);
      Vector direction = new Vector(0, -1, 0);
      Ray orientation = new Ray(origin, direction);

      CameraSettings settings = new CameraSettings();
      settings.X = 10;
      settings.Y = 6;
      settings.FocalLength = 1;
      settings.ZoomFactor = 1;
      settings.Orientation = orientation;

      Camera camera = new SimplePointableCamera(settings);

      Point worldPoint = camera.getWorldPointForPixel(1, 1);
      AssertHelper.assertEquals(worldPoint.X, -4.0f);
      AssertHelper.assertEquals(worldPoint.Y, 0.0f);
      AssertHelper.assertEquals(worldPoint.Z, -2.0f);

   }

   @Test
   public void rotateTest2() throws Exception {
      Point origin = new Point(0, 0, 0);
      Vector direction = new Vector(0, -1, -1);
      Ray orientation = new Ray(origin, direction);

      CameraSettings settings = new CameraSettings();
      settings.X = 10;
      settings.Y = 6;
      settings.FocalLength = 1;
      settings.ZoomFactor = 1;
      settings.Orientation = orientation;

      Camera camera = new SimplePointableCamera(settings);

      Point worldPoint = camera.getWorldPointForPixel(1, 1);
      AssertHelper.assertEquals(worldPoint.X, -4);
      AssertHelper.assertEquals(worldPoint.Y, Constants.Root2);
      AssertHelper.assertEquals(worldPoint.Z, -Constants.Root2);

   }

   @Test
   public void rotateTest3() throws Exception {
      Point origin = new Point(0, 0, 0);
      Vector direction = new Vector(0, -1, -Constants.Root3);
      Ray orientation = new Ray(origin, direction);

      CameraSettings settings = new CameraSettings();
      settings.X = 10;
      settings.Y = 6;
      settings.FocalLength = 1;
      settings.ZoomFactor = 1;
      settings.Orientation = orientation;

      Camera camera = new SimplePointableCamera(settings);

      Point worldPoint = camera.getWorldPointForPixel(1, 1);
      AssertHelper.assertEquals(worldPoint.X, -4);
      AssertHelper.assertEquals(worldPoint.Y, Constants.Root3);
      AssertHelper.assertEquals(worldPoint.Z, -1);

   }

   @Test
   public void rotateTest4() throws Exception {
      Point origin = new Point(0, 0, 0);
      Vector direction = new Vector(0, -1, -Constants.Root3);
      Ray orientation = new Ray(origin, direction);

      CameraSettings settings = new CameraSettings();
      settings.X = 10;
      settings.Y = 6;
      settings.FocalLength = 1;
      settings.ZoomFactor = 1;
      settings.Orientation = orientation;

      Camera camera = new SimplePointableCamera(settings);

      Point worldPoint = camera.getWorldPointForPixel(5, 3);
      AssertHelper.assertEquals(worldPoint.X, 0);
      AssertHelper.assertEquals(worldPoint.Y, 0);
      AssertHelper.assertEquals(worldPoint.Z, 0);

   }
}
