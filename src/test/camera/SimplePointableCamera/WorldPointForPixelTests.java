package test.camera.SimplePointableCamera;

import net.danielthompson.danray.cameras.Camera;
import net.danielthompson.danray.cameras.CameraSettings;
import net.danielthompson.danray.cameras.SimplePointableCamera;
import net.danielthompson.danray.structures.Constants;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Vector;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

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

      CameraSettings settings = new CameraSettings();
      settings.X = 5;
      settings.Y = 5;
      settings.FocalLength = 1;
      settings.ZoomFactor = 1;
      settings.Orientation = orientation;

      Camera camera = new SimplePointableCamera(settings);

      Point worldPoint = camera.getWorldPointForPixel(0, 0);
      Assert.assertEquals(worldPoint.X, 97.5);
      Assert.assertEquals(worldPoint.Y, 102.5);
      Assert.assertEquals(worldPoint.Z, 100.0);

   }

   @Test
   public void test2() throws Exception {
      Point origin = new Point(0, 0, 0);
      Vector direction = new Vector(0, 0, -1);
      Ray orientation = new Ray(origin, direction);

      CameraSettings settings = new CameraSettings();
      settings.X = 10;
      settings.Y = 6;
      settings.FocalLength = 1;
      settings.ZoomFactor = 1;
      settings.Orientation = orientation;

      Camera camera = new SimplePointableCamera(settings);

      Point worldPoint = camera.getWorldPointForPixel(0, 0);
      Assert.assertEquals(worldPoint.X, -5.0);
      Assert.assertEquals(worldPoint.Y, 3.0);
      Assert.assertEquals(worldPoint.Z, 0.0);

   }

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
      Assert.assertEquals(worldPoint.X, -10.0);
      Assert.assertEquals(worldPoint.Y, 6.0);
      Assert.assertEquals(worldPoint.Z, 0.0);

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
      Assert.assertEquals(worldPoint.X, -8.0);
      Assert.assertEquals(worldPoint.Y, 4.0);
      Assert.assertEquals(worldPoint.Z, 0.0);

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
      Assert.assertEquals(worldPoint.X, -4.0, Constants.NumericalDelta);
      Assert.assertEquals(worldPoint.Y, 0.0, Constants.NumericalDelta);
      Assert.assertEquals(worldPoint.Z, -2.0, Constants.NumericalDelta);

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
      Assert.assertEquals(worldPoint.X, -4, Constants.NumericalDelta);
      Assert.assertEquals(worldPoint.Y, Math.sqrt(2), Constants.NumericalDelta);
      Assert.assertEquals(worldPoint.Z, -Math.sqrt(2), Constants.NumericalDelta);

   }

   @Test
   public void rotateTest3() throws Exception {
      Point origin = new Point(0, 0, 0);
      Vector direction = new Vector(0, -1, -Math.sqrt(3));
      Ray orientation = new Ray(origin, direction);

      CameraSettings settings = new CameraSettings();
      settings.X = 10;
      settings.Y = 6;
      settings.FocalLength = 1;
      settings.ZoomFactor = 1;
      settings.Orientation = orientation;

      Camera camera = new SimplePointableCamera(settings);

      Point worldPoint = camera.getWorldPointForPixel(1, 1);
      Assert.assertEquals(worldPoint.X, -4, Constants.NumericalDelta);
      Assert.assertEquals(worldPoint.Y, Math.sqrt(3), Constants.NumericalDelta);
      Assert.assertEquals(worldPoint.Z, -1, Constants.NumericalDelta);

   }

   @Test
   public void rotateTest4() throws Exception {
      Point origin = new Point(0, 0, 0);
      Vector direction = new Vector(0, -1, -Math.sqrt(3));
      Ray orientation = new Ray(origin, direction);

      CameraSettings settings = new CameraSettings();
      settings.X = 10;
      settings.Y = 6;
      settings.FocalLength = 1;
      settings.ZoomFactor = 1;
      settings.Orientation = orientation;

      Camera camera = new SimplePointableCamera(settings);

      Point worldPoint = camera.getWorldPointForPixel(5, 3);
      Assert.assertEquals(worldPoint.X, 0, Constants.NumericalDelta);
      Assert.assertEquals(worldPoint.Y, 0, Constants.NumericalDelta);
      Assert.assertEquals(worldPoint.Z, 0, Constants.NumericalDelta);

   }
}
