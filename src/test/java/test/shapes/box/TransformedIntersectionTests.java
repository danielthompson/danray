package test.shapes.box;

import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shapes.Box;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.*;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by daniel on 2/15/15.
 */
public class TransformedIntersectionTests {

   @Test
   public void shouldHit1() throws Exception {

      Point p1 = new Point(-1, -1, -1);
      Point p2 = new Point(1, 1, 1);
      Material material = null;

      Transform transform = Transform.Scale(2, 1, 1);

      Box box = new Box(p1, p2, material, transform, transform.Invert());

      Point origin = new Point(1.1, 0, 10);
      Vector direction = new Vector(0, 0, -1);

      Ray ray = new Ray(origin, direction);

      IntersectionState state = box.getHitInfo(ray);

      Point expectedIntersectionPoint = new Point(1.1, 0, 1);
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

      IntersectionState state = box.getHitInfo(ray);

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

      IntersectionState state = box.getHitInfo(ray);

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

      IntersectionState state = box.getHitInfo(ray);

      Point expectedIntersectionPoint = new Point(2, 1, 2);
      Normal expectedNormalDirection = new Normal(1, 0, 0);

      Assert.assertTrue(state.Hits, "Should hit");
      Assert.assertEquals(state.IntersectionPoint, expectedIntersectionPoint, "Should intersect at " + expectedIntersectionPoint);
      Assert.assertEquals(state.Normal, expectedNormalDirection, "Should have normal pointing at " + expectedNormalDirection);
   }

}
