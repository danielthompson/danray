package test.shapes.box;

import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shapes.Box;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.*;
import net.danielthompson.danray.structures.Point;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.awt.*;

/**
 * Created by daniel on 2/15/15.
 */
public class IntersectionTests {

   @Test
   public void shouldHit1() throws Exception {

      Point p1 = new Point(0, 0, 0);
      Point p2 = new Point(2, 2, 2);
      Material material = null;

      Box box = new Box(p1, p2, material);

      Point origin = new Point(1, 1, 10);
      Vector direction = new Vector(0, 0, -1);

      Ray ray = new Ray(origin, direction);

      IntersectionState state = box.GetHitInfo(ray);

      Point expectedIntersectionPoint = new Point(1, 1, 2);
      Normal expectedNormalDirection = new Normal(0, 0, 1);

      Assert.assertTrue(state.Hits, "Should hit");
      Assert.assertEquals(state.IntersectionPoint, expectedIntersectionPoint);
      Assert.assertEquals(state.Normal, expectedNormalDirection);
   }

   @Test
   public void shouldHit2() throws Exception {

      Point p1 = new Point(0, 0, 0);
      Point p2 = new Point(2, 2, 2);
      Material material = null;

      Box box = new Box(p1, p2, material);

      Point origin = new Point(0, 1, 3);
      Vector direction = new Vector(1, 0, -1);

      Ray ray = new Ray(origin, direction);

      IntersectionState state = box.GetHitInfo(ray);

      Point expectedIntersectionPoint = new Point(1, 1, 2);
      Normal expectedNormalDirection = new Normal(0, 0, 1);

      Assert.assertTrue(state.Hits, "Should hit");
      Assert.assertEquals(state.IntersectionPoint, expectedIntersectionPoint);
      Assert.assertEquals(state.Normal, expectedNormalDirection);
   }

   @Test
   public void shouldHit3() throws Exception {

      Point p1 = new Point(0, 0, 0);
      Point p2 = new Point(2, 2, 2);

      Box box = new Box(p1, p2, null);

      Point origin = new Point(4, 1, 4);
      Vector direction = new Vector(-1, 0, -1);

      Ray ray = new Ray(origin, direction);

      IntersectionState state = box.GetHitInfo(ray);

      Point expectedIntersectionPoint = new Point(2, 1, 2);
      Normal expectedNormalDirection = new Normal(1, 0, 0);

      Assert.assertNotNull(state, "IntersectionState shouldn't be null");
      Assert.assertTrue(state.Hits, "Should hit");
      Assert.assertNotNull(state.IntersectionPoint, "IntersectionPoint shouldn't be null if hits...");
      Assert.assertEquals(state.IntersectionPoint, expectedIntersectionPoint, "Should intersect at " + expectedIntersectionPoint);
      Assert.assertEquals(state.Normal, expectedNormalDirection, "Should have normal pointing at " + expectedNormalDirection);
   }

   @Test
   public void shouldHit4() throws Exception {

      Point p1 = new Point(0, 0, 0);
      Point p2 = new Point(2, 2, 2);

      Box box = new Box(p1, p2, null);

      Point origin = new Point(5, 1, 2);
      Vector direction = new Vector(-1, 0, 0);

      Ray ray = new Ray(origin, direction);

      IntersectionState state = box.GetHitInfo(ray);

      Point expectedIntersectionPoint = new Point(2, 1, 2);
      Normal expectedNormalDirection = new Normal(1, 0, 0);

      Assert.assertTrue(state.Hits, "Should hit");
      Assert.assertEquals(state.IntersectionPoint, expectedIntersectionPoint, "Should intersect at " + expectedIntersectionPoint);
      Assert.assertEquals(state.Normal, expectedNormalDirection, "Should have normal pointing at " + expectedNormalDirection);
   }

   @Test
   public void shouldHit5() throws Exception {

      Point p0 = new Point(-100, -100, -100);
      Point p1 = new Point(100, 100, 100);

      Material material = new Material();
      material.setColor(new Color(30, 120, 120));
      material.setReflectivity(.25);
      material.setDiffuse(.75);

      Transform t1 = Transform.Translate(new Vector(300, 500, 500));
      //Transform t1 = new Transform();
      //Transform t2 = Transform.RotateZ(45);
      Transform t2 = new Transform();
      //Transform t3 = Transform.RotateY(45);
      //Transform t3 = new Transform();
      Transform t3 = Transform.Scale(2.0, 1.0, 1);

      Transform objectToWorld = t1.Apply(t2).Apply(t3);
      Transform worldToObject = t3.Apply(t2).Apply(t1).Invert();


      //Transform objectToWorld = null;
      //Transform worldToObject = null;

      Box box = new Box(p0, p1, material, objectToWorld, worldToObject);

      box.GetWorldBoundingBox();

      Ray ray = new Ray(new Point(300, 500, 700), new Vector(0, 0, -1));

      IntersectionState state = box.GetHitInfo(ray);

      if (state.Hits)
         ;
      /*
      light ray from current radiatable to closest drawable
      Origin: X 200.0, Y 1000.0, Z 1400.0, Direction: X 0.16854688639637852, Y -0.4839946235401404, Z -0.858685711695684
      mint = 0
      maxt = 1.7976931348623157E308
      */

      /* closest drawable location
      X 357.0277777777777, Y 549.0833333333334, Z 600.0

      expected t for hit
      931.656354379866
       */
   }

}
