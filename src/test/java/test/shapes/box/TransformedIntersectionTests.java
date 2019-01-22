package test.shapes.box;

import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shapes.Box;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import test.AssertHelper;

/**
 * Created by daniel on 2/15/15.
 */
public class TransformedIntersectionTests {

   @Test
   public void shouldHit1() throws Exception {

      Material material = null;

      Transform[] transforms = new Transform[2];
      transforms[0] = Transform.Scale(4.0f, 2.0f, 2.0f);
      transforms[1] = Transform.Translate(new Vector(-0.5f, -0.5f, -0.5f));
      Transform compositeTransform[] = Transform.composite(transforms);

      Box box = new Box(compositeTransform, material);

      Point origin = new Point(1.1f, 0, 10);
      Vector direction = new Vector(0, 0, -1);

      Ray ray = new Ray(origin, direction);

      boolean hits = box.hits(ray);

      Intersection state = box.getHitInfo(ray);

      Point expectedIntersectionPoint = new Point(1.1f, 0, 1);
      Normal expectedNormalDirection = new Normal(0, 0, 1);

      Assert.assertTrue(hits, "Should hit");
      AssertHelper.assertEquals(state.Location, expectedIntersectionPoint);
      AssertHelper.assertEquals(state.Normal, expectedNormalDirection);
   }

   @Test
   public void shouldHit2() throws Exception {

      Material material = null;

      Transform[] transforms = new Transform[1];
      transforms[0] = Transform.Scale(2.0f);
      Transform compositeTransform[] = Transform.composite(transforms);

      Box box = new Box(compositeTransform, material);

      Point origin = new Point(0, 1, 3);
      Vector direction = new Vector(1, 0, -1);

      Ray ray = new Ray(origin, direction);

      boolean hits = box.hits(ray);
      Intersection state = box.getHitInfo(ray);

      Point expectedIntersectionPoint = new Point(1, 1, 2);
      Normal expectedNormalDirection = new Normal(0, 0, 1);

      Assert.assertTrue(hits, "Should hit");
      AssertHelper.assertEquals(state.Location, expectedIntersectionPoint);
      AssertHelper.assertEquals(state.Normal, expectedNormalDirection);
   }

   @Test
   public void shouldHit3() throws Exception {

      Material material = null;

      Transform[] transforms = new Transform[1];
      transforms[0] = Transform.Scale(2.0f);
      Transform compositeTransform[] = Transform.composite(transforms);

      Box box = new Box(compositeTransform, material);

      Point origin = new Point(4, 1, 4);
      Vector direction = new Vector(-1, 0, -1);

      Ray ray = new Ray(origin, direction);

      boolean hits = box.hits(ray);
      Intersection state = box.getHitInfo(ray);

      Point expectedIntersectionPoint = new Point(2, 1, 2);
      Normal expectedNormalDirection = new Normal(0, 0, 1);

      Assert.assertNotNull(state, "Intersection shouldn't be null");
      Assert.assertTrue(hits, "Should hit");
      Assert.assertNotNull(state.Location, "Location shouldn't be null if hits...");
      AssertHelper.assertEquals(state.Location, expectedIntersectionPoint);
      AssertHelper.assertEquals(state.Normal, expectedNormalDirection);
   }

   @Test
   public void shouldHit4() throws Exception {

      Material material = null;

      Transform[] transforms = new Transform[1];
      transforms[0] = Transform.Scale(2.0f);
      Transform compositeTransform[] = Transform.composite(transforms);

      Box box = new Box(compositeTransform, material);

      Point origin = new Point(5, 1, 2);
      Vector direction = new Vector(-1, 0, 0);

      Ray ray = new Ray(origin, direction);

      boolean hits = box.hits(ray);
      Intersection state = box.getHitInfo(ray);

      Point expectedIntersectionPoint = new Point(2, 1, 2);
      Normal expectedNormalDirection = new Normal(0, 0, 1);

      Assert.assertTrue(hits, "Should hit");
      AssertHelper.assertEquals(state.Location, expectedIntersectionPoint);
      AssertHelper.assertEquals(state.Normal, expectedNormalDirection);
   }

}
