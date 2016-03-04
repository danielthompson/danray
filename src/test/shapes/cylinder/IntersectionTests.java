package test.shapes.cylinder;

import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shapes.Cylinder;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.*;
import net.danielthompson.danray.structures.Point;
import org.testng.Assert;
import org.testng.annotations.Test;

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

      Point origin = new Point(5, 5, 5);
      Vector direction = new Vector(0, 0, -1);

      Ray ray = new Ray(origin, direction);

      IntersectionState state = cylinder.GetHitInfo(ray);

      Assert.assertFalse(state.Hits, "Shouldn't hit");
   }

   @Test
   public void shouldHit1TopDown1() throws Exception {

      Material material = null;
      Transform worldToObject = null;
      Transform objectToWorld = null;

      Cylinder cylinder = new Cylinder(1, 1, worldToObject, objectToWorld, material);

      Point origin = new Point(0, 5, 0);
      Vector direction = new Vector(0, -1, 0);

      Ray ray = new Ray(origin, direction);

      IntersectionState state = cylinder.GetHitInfo(ray);

      Point expectedIntersectionPoint = new Point(0, 1, 0);
      Normal expectedNormalDirection = new Normal(0, 1, 0);

      Assert.assertTrue(state.Hits, "Should hit");

      Assert.assertEquals(state.IntersectionPoint, expectedIntersectionPoint, "intersection point not right");
      Assert.assertEquals(state.Normal, expectedNormalDirection, "normal not right");
   }

   @Test
   public void shouldHit1BottomUp1() throws Exception {

      Material material = null;
      Transform worldToObject = null;
      Transform objectToWorld = null;

      Cylinder cylinder = new Cylinder(1, 1, worldToObject, objectToWorld, material);

      Point origin = new Point(0, -5, 0);
      Vector direction = new Vector(0, 1, 0);

      Ray ray = new Ray(origin, direction);

      IntersectionState state = cylinder.GetHitInfo(ray);

      Point expectedIntersectionPoint = new Point(0, 0, 0);
      Normal expectedNormalDirection = new Normal(0, -1, 0);

      Assert.assertTrue(state.Hits, "Should hit");

      Assert.assertEquals(state.IntersectionPoint, expectedIntersectionPoint, "intersection point not right");
      Assert.assertEquals(state.Normal, expectedNormalDirection, "normal not right");
   }

   @Test
   public void shouldHit1BottomUp2() throws Exception {

      Material material = null;
      Transform worldToObject = null;
      Transform objectToWorld = null;

      Cylinder cylinder = new Cylinder(1, 1, worldToObject, objectToWorld, material);

      Point origin = new Point(0, -5, 0);
      Vector direction = new Vector(0, 1, 0);

      Ray ray = new Ray(origin, direction);

      IntersectionState state = cylinder.GetHitInfo(ray);

      Point expectedIntersectionPoint = new Point(0, 0, 0);
      Normal expectedNormalDirection = new Normal(0, -1, 0);

      Assert.assertTrue(state.Hits, "Should hit");

      Assert.assertEquals(state.IntersectionPoint, expectedIntersectionPoint, "intersection point not right");
      Assert.assertEquals(state.Normal, expectedNormalDirection, "normal not right");
   }

   @Test
   public void shouldHit1TopDown2() throws Exception {

      Material material = null;
      Transform worldToObject = null;
      Transform objectToWorld = null;

      Cylinder cylinder = new Cylinder(1, 1, worldToObject, objectToWorld, material);

      Point origin = new Point(1, 5, 0);
      Vector direction = new Vector(-.25, -1, 0);

      Ray ray = new Ray(origin, direction);

      IntersectionState state = cylinder.GetHitInfo(ray);

      Point expectedIntersectionPoint = new Point(0, 1, 0);
      Normal expectedNormalDirection = new Normal(0, 1, 0);

      Assert.assertTrue(state.Hits, "Should hit");

      Assert.assertEquals(state.IntersectionPoint, expectedIntersectionPoint, "intersection point not right");
      Assert.assertEquals(state.Normal, expectedNormalDirection, "normal not right");
   }

   @Test
   public void shouldNotHit20() throws Exception {

      Material material = null;
      Transform worldToObject = null;
      Transform objectToWorld = null;

      Cylinder cylinder = new Cylinder(1, 1, worldToObject, objectToWorld, material);

      Point origin = new Point(5, 5, 5);
      Vector direction = new Vector(0, 0, 1);

      Ray ray = new Ray(origin, direction);

      IntersectionState state = cylinder.GetHitInfo(ray);

      Assert.assertFalse(state.Hits, "Shouldn't hit");
   }

   @Test
   public void shouldHit20() throws Exception {

      Material material = null;
      Transform worldToObject = null;
      Transform objectToWorld = null;

      Cylinder cylinder = new Cylinder(1, 1, worldToObject, objectToWorld, material);

      Point origin = new Point(0, .5, -10);
      Vector direction = new Vector(0, 0, 1);

      Ray ray = new Ray(origin, direction);

      IntersectionState state = cylinder.GetHitInfo(ray);

      Point expectedIntersectionPoint = new Point(0, .5, -1);
      Normal expectedNormalDirection = new Normal(0, 0, -1);

      Assert.assertTrue(state.Hits, "Should hit");

      Assert.assertEquals(state.IntersectionPoint, expectedIntersectionPoint, "intersection point not right");
      Assert.assertEquals(state.Normal, expectedNormalDirection, "normal not right");
   }

   @Test
   public void shouldHit30() throws Exception {

      Material material = null;
      Transform worldToObject = null;
      Transform objectToWorld = null;

      Cylinder cylinder = new Cylinder(1, 1, worldToObject, objectToWorld, material);

      Point origin = new Point(2, 0, 0);
      Vector direction = new Vector(-2, 1, 0);

      Ray ray = new Ray(origin, direction);

      IntersectionState state = cylinder.GetHitInfo(ray);

      Point expectedIntersectionPoint = new Point(1, .5, 0);
      Normal expectedNormalDirection = new Normal(1, 0, 0);

      Assert.assertTrue(state.Hits, "Should hit");

      Assert.assertEquals(state.IntersectionPoint, expectedIntersectionPoint, "intersection point not right");
      Assert.assertEquals(state.Normal, expectedNormalDirection, "normal not right");
   }

   @Test
   public void shouldHit31() throws Exception {

      Material material = null;
      Transform worldToObject = null;
      Transform objectToWorld = null;

      Cylinder cylinder = new Cylinder(1, 1, worldToObject, objectToWorld, material);

      Point origin = new Point(2, 1, 0);
      Vector direction = new Vector(-2, -1, 0);

      Ray ray = new Ray(origin, direction);

      IntersectionState state = cylinder.GetHitInfo(ray);

      Point expectedIntersectionPoint = new Point(1, .5, 0);
      Normal expectedNormalDirection = new Normal(1, 0, 0);

      Assert.assertTrue(state.Hits, "Should hit");

      Assert.assertEquals(state.IntersectionPoint, expectedIntersectionPoint, "intersection point not right");
      Assert.assertEquals(state.Normal, expectedNormalDirection, "normal not right");
   }

   @Test
   public void shouldHit40() throws Exception {

      Material material = null;
      Transform worldToObject = null;
      Transform objectToWorld = null;

      Cylinder cylinder = new Cylinder(1, 1, worldToObject, objectToWorld, material);

      Point origin = new Point(-2, 2, 0);
      Vector direction = new Vector(2, -1, 0);

      Ray ray = new Ray(origin, direction);

      IntersectionState state = cylinder.GetHitInfo(ray);

      Point expectedIntersectionPoint = new Point(0, 1, 0);
      Normal expectedNormalDirection = new Normal(0, 1, 0);

      Assert.assertTrue(state.Hits, "Should hit");

      Assert.assertEquals(state.IntersectionPoint, expectedIntersectionPoint, "intersection point not right");
      Assert.assertEquals(state.Normal, expectedNormalDirection, "normal not right");
   }


   @Test
   public void shouldHit41() throws Exception {

      Material material = null;
      Transform worldToObject = null;
      Transform objectToWorld = null;

      Cylinder cylinder = new Cylinder(1, 1, worldToObject, objectToWorld, material);

      Point origin = new Point(2, -1, 0);
      Vector direction = new Vector(-2, 1, 0);

      Ray ray = new Ray(origin, direction);

      IntersectionState state = cylinder.GetHitInfo(ray);

      Point expectedIntersectionPoint = new Point(0, 0, 0);
      Normal expectedNormalDirection = new Normal(0, -1, 0);

      Assert.assertTrue(state.Hits, "Should hit");

      Assert.assertEquals(state.IntersectionPoint, expectedIntersectionPoint, "intersection point not right");
      Assert.assertEquals(state.Normal, expectedNormalDirection, "normal not right");
   }


}
