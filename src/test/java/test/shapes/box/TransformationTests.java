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
      inputTransforms[0] = Transform.rotateX(45);
      inputTransforms[1] = Transform.translate(new Vector3(-.5f, -.5f, -.5f));
      Transform compositeTransform[] = Transform.composite(inputTransforms);

      Box box = new Box(compositeTransform, material);

      Point3 origin = new Point3(0.25f, 0.25f, 10f);
      Vector3 direction = new Vector3(0f, 0f, -1f);

      Ray ray = new Ray(origin, direction);

      boolean hits = box.Hits(ray);

      Intersection state = box.GetHitInfo(ray);

      Normal expectedNormalDirection = new Normal(0, .5f, .5f);
      expectedNormalDirection.normalize();

      Assert.assertTrue(hits);

      AssertHelper.assertEquals(state.normal, expectedNormalDirection);
   }

   @Test
   public void shouldHit2() {

      Material material = null;

      Transform[] transforms = new Transform[1];
      transforms[0] = Transform.scale(2.0f);
      Transform compositeTransform[] = Transform.composite(transforms);

      Box box = new Box(compositeTransform, material);

      Point3 origin = new Point3(0, 1, 3);
      Vector3 direction = new Vector3(1, 0, -1);

      Ray ray = new Ray(origin, direction);

      boolean hits = box.Hits(ray);
      Intersection state = box.GetHitInfo(ray);

      Point3 expectedIntersectionPoint = new Point3(1, 1, 2);
      Normal expectedNormalDirection = new Normal(0, 0, 1);

      Assert.assertTrue(hits, "Should hit");
      Assert.assertEquals(state.location, expectedIntersectionPoint);
      Assert.assertEquals(state.normal, expectedNormalDirection);
   }

   @Test
   public void shouldHit3() throws Exception {

      Material material = null;

      Transform[] transforms = new Transform[1];
      transforms[0] = Transform.scale(2.0f);
      Transform compositeTransform[] = Transform.composite(transforms);

      Box box = new Box(compositeTransform, material);

      Point3 origin = new Point3(4, 1, 4);
      Vector3 direction = new Vector3(-1, 0, -1);

      Ray ray = new Ray(origin, direction);

      boolean hits = box.Hits(ray);
      Intersection state = box.GetHitInfo(ray);

      Point3 expectedIntersectionPoint = new Point3(2, 1, 2);
      Normal expectedNormalDirection = new Normal(0, 0, 1);

      Assert.assertNotNull(state, "intersection shouldn't be null");
      Assert.assertTrue(hits, "Should hit");
      Assert.assertNotNull(state.location, "location shouldn't be null if hits...");
      AssertHelper.assertEquals(state.location, expectedIntersectionPoint);
      AssertHelper.assertEquals(state.normal, expectedNormalDirection);
   }

   @Test
   public void shouldHit4() throws Exception {

      Material material = null;

      Transform[] transforms = new Transform[1];
      transforms[0] = Transform.scale(2.0f);
      Transform compositeTransform[] = Transform.composite(transforms);

      Box box = new Box(compositeTransform, material);

      Point3 origin = new Point3(5, 1, 2);
      Vector3 direction = new Vector3(-1, 0, 0);

      Ray ray = new Ray(origin, direction);

      boolean hits = box.Hits(ray);
      Intersection state = box.GetHitInfo(ray);

      Point3 expectedIntersectionPoint = new Point3(2, 1, 2);
      Normal expectedNormalDirection = new Normal(0, 0, 1);

      Assert.assertTrue(hits, "Should hit");
      AssertHelper.assertEquals(state.location, expectedIntersectionPoint);
      AssertHelper.assertEquals(state.normal, expectedNormalDirection);
   }

}
