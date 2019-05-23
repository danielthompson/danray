package test.shapes.cylinder;

import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shapes.Cylinder;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.*;
import net.danielthompson.danray.structures.Point3;
import org.testng.Assert;
import org.testng.annotations.Test;
import test.AssertHelper;

/**
 * Created by daniel on 2/15/15.
 */
public class IntersectionTests {

   @Test
   public void shouldNotHit1() throws Exception {

      Material material = null;
      Transform worldToObject = null;
      Transform objectToWorld = null;

      Cylinder cylinder = new Cylinder(1, 1, worldToObject, objectToWorld, material);

      Point3 origin = new Point3(5, 5, 5);
      Vector3 direction = new Vector3(0, 0, -1);

      Ray ray = new Ray(origin, direction);

      Intersection state = cylinder.GetHitInfo(ray);

      Assert.assertFalse(state.Hits, "Shouldn't hit");
   }

   @Test
   public void shouldHit1TopDown1() throws Exception {

      Material material = null;
      Transform worldToObject = null;
      Transform objectToWorld = null;

      Cylinder cylinder = new Cylinder(1, 1, worldToObject, objectToWorld, material);

      Point3 origin = new Point3(0, 5, 0);
      Vector3 direction = new Vector3(0, -1, 0);

      Ray ray = new Ray(origin, direction);

      Intersection state = cylinder.GetHitInfo(ray);

      Point3 expectedIntersectionPoint = new Point3(0, 1, 0);
      Normal expectedNormalDirection = new Normal(0, 1, 0);

      Assert.assertTrue(state.Hits, "Should hit");

      AssertHelper.assertEquals(state.location, expectedIntersectionPoint);
      AssertHelper.assertEquals(state.Normal, expectedNormalDirection);
   }

   @Test
   public void shouldHit1BottomUp1() throws Exception {

      Material material = null;
      Transform worldToObject = null;
      Transform objectToWorld = null;

      Cylinder cylinder = new Cylinder(1, 1, worldToObject, objectToWorld, material);

      Point3 origin = new Point3(0, -5, 0);
      Vector3 direction = new Vector3(0, 1, 0);

      Ray ray = new Ray(origin, direction);

      Intersection state = cylinder.GetHitInfo(ray);

      Point3 expectedIntersectionPoint = new Point3(0, 0, 0);
      Normal expectedNormalDirection = new Normal(0, -1, 0);

      Assert.assertTrue(state.Hits, "Should hit");

      AssertHelper.assertEquals(state.location, expectedIntersectionPoint);
      AssertHelper.assertEquals(state.Normal, expectedNormalDirection);
   }

   @Test
   public void shouldHit1BottomUp2() throws Exception {

      Material material = null;
      Transform worldToObject = null;
      Transform objectToWorld = null;

      Cylinder cylinder = new Cylinder(1, 1, worldToObject, objectToWorld, material);

      Point3 origin = new Point3(0, -5, 0);
      Vector3 direction = new Vector3(0, 1, 0);

      Ray ray = new Ray(origin, direction);

      Intersection state = cylinder.GetHitInfo(ray);

      Point3 expectedIntersectionPoint = new Point3(0, 0, 0);
      Normal expectedNormalDirection = new Normal(0, -1, 0);

      Assert.assertTrue(state.Hits, "Should hit");

      AssertHelper.assertEquals(state.location, expectedIntersectionPoint);
      AssertHelper.assertEquals(state.Normal, expectedNormalDirection);
   }

   @Test
   public void shouldHit1TopDown2() throws Exception {

      Material material = null;
      Transform worldToObject = null;
      Transform objectToWorld = null;

      Cylinder cylinder = new Cylinder(1, 1, worldToObject, objectToWorld, material);

      Point3 origin = new Point3(1, 5, 0);
      Vector3 direction = new Vector3(-.25f, -1, 0);

      Ray ray = new Ray(origin, direction);

      Intersection state = cylinder.GetHitInfo(ray);

      Point3 expectedIntersectionPoint = new Point3(0, 1, 0);
      Normal expectedNormalDirection = new Normal(0, 1, 0);

      Assert.assertTrue(state.Hits, "Should hit");

      AssertHelper.assertEquals(state.location, expectedIntersectionPoint);
      AssertHelper.assertEquals(state.Normal, expectedNormalDirection);
   }

   @Test
   public void shouldNotHit20() throws Exception {

      Material material = null;
      Transform worldToObject = null;
      Transform objectToWorld = null;

      Cylinder cylinder = new Cylinder(1, 1, worldToObject, objectToWorld, material);

      Point3 origin = new Point3(5, 5, 5);
      Vector3 direction = new Vector3(0, 0, 1);

      Ray ray = new Ray(origin, direction);

      Intersection state = cylinder.GetHitInfo(ray);

      Assert.assertFalse(state.Hits, "Shouldn't hit");
   }

   @Test
   public void shouldHit20() throws Exception {

      Material material = null;
      Transform worldToObject = null;
      Transform objectToWorld = null;

      Cylinder cylinder = new Cylinder(1, 1, worldToObject, objectToWorld, material);

      Point3 origin = new Point3(0, .5f, -10);
      Vector3 direction = new Vector3(0, 0, 1);

      Ray ray = new Ray(origin, direction);

      Intersection state = cylinder.GetHitInfo(ray);

      Point3 expectedIntersectionPoint = new Point3(0, .5f, -1);
      Normal expectedNormalDirection = new Normal(0, 0, -1);

      Assert.assertTrue(state.Hits, "Should hit");

      AssertHelper.assertEquals(state.location, expectedIntersectionPoint);
      AssertHelper.assertEquals(state.Normal, expectedNormalDirection);
   }

   @Test
   public void shouldHit30() throws Exception {

      Material material = null;
      Transform worldToObject = null;
      Transform objectToWorld = null;

      Cylinder cylinder = new Cylinder(1, 1, worldToObject, objectToWorld, material);

      Point3 origin = new Point3(2, 0, 0);
      Vector3 direction = new Vector3(-2, 1, 0);

      Ray ray = new Ray(origin, direction);

      Intersection state = cylinder.GetHitInfo(ray);

      Point3 expectedIntersectionPoint = new Point3(1, .5f, 0);
      Normal expectedNormalDirection = new Normal(1, 0, 0);

      Assert.assertTrue(state.Hits, "Should hit");

      AssertHelper.assertEquals(state.location, expectedIntersectionPoint);
      AssertHelper.assertEquals(state.Normal, expectedNormalDirection);
   }

   @Test
   public void shouldHit31() throws Exception {

      Material material = null;
      Transform worldToObject = null;
      Transform objectToWorld = null;

      Cylinder cylinder = new Cylinder(1, 1, worldToObject, objectToWorld, material);

      Point3 origin = new Point3(2, 1, 0);
      Vector3 direction = new Vector3(-2, -1, 0);

      Ray ray = new Ray(origin, direction);

      Intersection state = cylinder.GetHitInfo(ray);

      Point3 expectedIntersectionPoint = new Point3(1, .5f, 0);
      Normal expectedNormalDirection = new Normal(1, 0, 0);

      Assert.assertTrue(state.Hits, "Should hit");

      AssertHelper.assertEquals(state.location, expectedIntersectionPoint);
      AssertHelper.assertEquals(state.Normal, expectedNormalDirection);
   }

   @Test
   public void shouldHit40() throws Exception {

      Material material = null;
      Transform worldToObject = null;
      Transform objectToWorld = null;

      Cylinder cylinder = new Cylinder(1, 1, worldToObject, objectToWorld, material);

      Point3 origin = new Point3(-2, 2, 0);
      Vector3 direction = new Vector3(2, -1, 0);

      Ray ray = new Ray(origin, direction);

      Intersection state = cylinder.GetHitInfo(ray);

      Point3 expectedIntersectionPoint = new Point3(0, 1, 0);
      Normal expectedNormalDirection = new Normal(0, 1, 0);

      Assert.assertTrue(state.Hits, "Should hit");

      AssertHelper.assertEquals(state.location, expectedIntersectionPoint);
      AssertHelper.assertEquals(state.Normal, expectedNormalDirection);
   }


   @Test
   public void shouldHit41() throws Exception {

      Material material = null;
      Transform worldToObject = null;
      Transform objectToWorld = null;

      Cylinder cylinder = new Cylinder(1, 1, worldToObject, objectToWorld, material);

      Point3 origin = new Point3(2, -1, 0);
      Vector3 direction = new Vector3(-2, 1, 0);

      Ray ray = new Ray(origin, direction);

      Intersection state = cylinder.GetHitInfo(ray);

      Point3 expectedIntersectionPoint = new Point3(0, 0, 0);
      Normal expectedNormalDirection = new Normal(0, -1, 0);

      Assert.assertTrue(state.Hits, "Should hit");

      AssertHelper.assertEquals(state.location, expectedIntersectionPoint);
      AssertHelper.assertEquals(state.Normal, expectedNormalDirection);
   }


}
