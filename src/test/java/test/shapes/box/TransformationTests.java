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
public class TransformationTests {

   @Test
   public void testNormal1() throws Exception {

      Material material = null;

      Transform[] inputTransforms = new Transform[2];
      inputTransforms[0] = Transform.RotateX(45);
      inputTransforms[1] = Transform.Translate(new Vector(-.5f, -.5f, -.5f));
      Transform compositeTransform[] = Transform.composite(inputTransforms);

      Box box = new Box(compositeTransform, material);

      Point3 origin = new Point3(0.25f, 0.25f, 10f);
      Vector direction = new Vector(0f, 0f, -1f);

      Ray ray = new Ray(origin, direction);

      boolean hits = box.Hits(ray);

      Intersection state = box.GetHitInfo(ray);

      Normal expectedNormalDirection = new Normal(0, .5f, .5f);
      expectedNormalDirection.Normalize();

      Assert.assertTrue(hits);

      AssertHelper.assertEquals(state.Normal, expectedNormalDirection);
   }

   @Test
   public void shouldHit2() throws Exception {

      Material material = null;

      Transform[] transforms = new Transform[1];
      transforms[0] = Transform.Scale(2.0f);
      Transform compositeTransform[] = Transform.composite(transforms);

      Box box = new Box(compositeTransform, material);

      Point3 origin = new Point3(0, 1, 3);
      Vector direction = new Vector(1, 0, -1);

      Ray ray = new Ray(origin, direction);

      boolean hits = box.Hits(ray);
      Intersection state = box.GetHitInfo(ray);

      Point3 expectedIntersectionPoint = new Point3(1, 1, 2);
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

      Point3 origin = new Point3(4, 1, 4);
      Vector direction = new Vector(-1, 0, -1);

      Ray ray = new Ray(origin, direction);

      boolean hits = box.Hits(ray);
      Intersection state = box.GetHitInfo(ray);

      Point3 expectedIntersectionPoint = new Point3(2, 1, 2);
      Normal expectedNormalDirection = new Normal(0, 0, 1);

      Assert.assertNotNull(state, "Intersection shouldn't be null");
      Assert.assertTrue(hits, "Should hit");
      Assert.assertNotNull(state.Location, "Location shouldn't be null if Hits...");
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

      Point3 origin = new Point3(5, 1, 2);
      Vector direction = new Vector(-1, 0, 0);

      Ray ray = new Ray(origin, direction);

      boolean hits = box.Hits(ray);
      Intersection state = box.GetHitInfo(ray);

      Point3 expectedIntersectionPoint = new Point3(2, 1, 2);
      Normal expectedNormalDirection = new Normal(0, 0, 1);

      Assert.assertTrue(hits, "Should hit");
      AssertHelper.assertEquals(state.Location, expectedIntersectionPoint);
      AssertHelper.assertEquals(state.Normal, expectedNormalDirection);
   }

}
