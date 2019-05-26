package test.shapes.csg;

import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shapes.Box;
import net.danielthompson.danray.shapes.csg.CSGOperation;
import net.danielthompson.danray.shapes.csg.CSGShape;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.*;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import test.AssertHelper;

public class DifferenceTests {

   private CSGShape shape;

   @BeforeMethod
   public void setUp() throws Exception {
      shape = new CSGShape((Material)null);
      shape.Operation = CSGOperation.Difference;

      Transform[] inputTransforms = new Transform[]{
            Transform.translate(new Vector3(0, 0, 1)),
            Transform.scale(2),
            Transform.translate(0, -0.5f, 0)

      };
      Transform[] compositeTransforms = Transform.composite(inputTransforms);

      shape.LeftShape = new Box(compositeTransforms, null);

      inputTransforms = new Transform[]{
            Transform.translate(new Vector3(1, 0, 0)),
            Transform.scale(2),
            Transform.translate(0, -0.5f, 0)

      };
      compositeTransforms = Transform.composite(inputTransforms);

      shape.RightShape = new Box(compositeTransforms, null);
   }

   @AfterMethod
   public void tearDown() throws Exception {
      shape = null;
   }

   @Test
   public void shouldNotHit1() {
      Point3 origin = new Point3(1, 0, -1);
      Vector3 direction = new Vector3(1, 0, 1);
      Ray ray = new Ray(origin, direction);

      boolean hits = shape.hits(ray);

      Assert.assertFalse(hits);
   }

   @Test
   public void shouldNotHit2() {
      Point3 origin = new Point3(1, 0, -0.5f);
      Vector3 direction = new Vector3(1, 0, 1);
      Ray ray = new Ray(origin, direction);

      boolean hits = shape.hits(ray);

      Assert.assertFalse(hits);
   }

   @Test
   public void shouldHit1() {
      Point3 origin = new Point3(0, 0, -2);
      Vector3 direction = new Vector3(2, 0, 5);
      Ray ray = new Ray(origin, direction);

      boolean hits = shape.hits(ray);
      Assert.assertTrue(hits);
   }

   @Test
   public void shouldHit2() {
      Point3 origin = new Point3(0, 0, -2);
      Vector3 direction = new Vector3(2, 0, 5);
      Ray ray = new Ray(origin, direction);

      boolean hits = shape.hits(ray);

      Intersection intersection = shape.intersect(ray);


      Assert.assertNotNull(intersection);
      Assert.assertTrue(intersection.hits);
   }

   @Test
   public void shouldHit2Normal() {
      Point3 origin = new Point3(0, 0, -2);
      Vector3 direction = new Vector3(2, 0, 5);
      Ray ray = new Ray(origin, direction);

      boolean hits = shape.hits(ray);

      Intersection intersection = shape.intersect(ray);

      Normal expectedNormal = new Normal(0, 0, -1);

      Assert.assertNotNull(intersection.normal);
      AssertHelper.assertEquals(intersection.normal, expectedNormal);
   }

   @Test
   public void shouldHit2Location() {
      Point3 origin = new Point3(0, 0, -2);
      Vector3 direction = new Vector3(2, 0, 5);
      Ray ray = new Ray(origin, direction);

      boolean hits = shape.hits(ray);

      Intersection intersection = shape.intersect(ray);

      Point3 expectedHitPoint = new Point3(1.6f, 0, 2);

      Assert.assertNotNull(intersection.location);
      AssertHelper.assertEquals(intersection.location, expectedHitPoint);
   }

   @Test
   public void normal1() {
      Point3 o = new Point3(.5f, 0, 5);
      Vector3 d = new Vector3(0, 0, -1);
      Ray ray = new Ray(o, d);

      boolean hits = shape.hits(ray);
      Intersection intersection = shape.intersect(ray);

      Normal expectedNormal = new Normal(0, 0, 1);
      Assert.assertEquals(intersection.normal, expectedNormal);
   }

   @Test
   public void normal2() {
      Point3 o = new Point3(.5f, 0, -5);
      Vector3 d = new Vector3(0, 0, 1);
      Ray ray = new Ray(o, d);

      boolean hits = shape.hits(ray);
      Intersection intersection = shape.intersect(ray);

      Normal expectedNormal = new Normal(0, 0, -1);
      Assert.assertEquals(intersection.normal, expectedNormal);
   }

   @Test
   public void location1() {
      Point3 o = new Point3(.5f, 0, 5);
      Vector3 d = new Vector3(0, 0, -1);
      Ray ray = new Ray(o, d);

      boolean hits = shape.hits(ray);
      Intersection intersection = shape.intersect(ray);

      Point3 expectedPoint = new Point3(0.5f, 0, 3);
      Assert.assertEquals(intersection.location, expectedPoint);
   }

   @Test
   public void location2() {
      Point3 o = new Point3(.5f, 0, -5);
      Vector3 d = new Vector3(0, 0, 1);
      Ray ray = new Ray(o, d);

      boolean hits = shape.hits(ray);
      Intersection intersection = shape.intersect(ray);

      Point3 expectedPoint = new Point3(0.5f, 0, 1);
      Assert.assertEquals(intersection.location, expectedPoint);
   }

   @Test
   public void location3() {
      Point3 o = new Point3(2.5f, 0, 1.5f);
      Vector3 d = new Vector3(-1, 0, 0);
      Ray ray = new Ray(o, d);

      boolean hits = shape.hits(ray);
      Intersection intersection = shape.intersect(ray);

      Point3 expectedPoint = new Point3(1f, 0, 1.5f);
      Assert.assertEquals(intersection.location, expectedPoint);
   }

   @Test
   public void normal3() {
      Point3 o = new Point3(2.5f, 0, 1.5f);
      Vector3 d = new Vector3(-1, 0, 0);
      Ray ray = new Ray(o, d);

      boolean hits = shape.hits(ray);
      Intersection intersection = shape.intersect(ray);

      Normal expectedNormal = new Normal(1, 0, 0);
      Assert.assertEquals(intersection.normal, expectedNormal);
   }

   @Test
   public void location4() {
      Point3 o = new Point3(3.5f, 0, 1.5f);
      Vector3 d = new Vector3(-1, 0, 0);
      Ray ray = new Ray(o, d);

      boolean hits = shape.hits(ray);
      Intersection intersection = shape.intersect(ray);

      Point3 expectedPoint = new Point3(1f, 0, 1.5f);
      Assert.assertEquals(intersection.location, expectedPoint);
   }

   @Test
   public void normal4() {
      Point3 o = new Point3(3.5f, 0, 1.5f);
      Vector3 d = new Vector3(-1, 0, 0);
      Ray ray = new Ray(o, d);

      boolean hits = shape.hits(ray);
      Intersection intersection = shape.intersect(ray);

      Normal expectedNormal = new Normal(1, 0, 0);
      Assert.assertEquals(intersection.normal, expectedNormal);
   }

   @Test
   public void location5() {
      Point3 o = new Point3(2.5f, 0, 0.5f);
      Vector3 d = new Vector3(-1, 0, 0);
      Ray ray = new Ray(o, d);

      boolean hits = shape.hits(ray);
      Assert.assertFalse(hits);
   }

   @Test
   public void location6() {
      Point3 o = new Point3(3.5f, 0, 0.5f);
      Vector3 d = new Vector3(-1, 0, 0);
      Ray ray = new Ray(o, d);

      boolean hits = shape.hits(ray);
      Assert.assertFalse(hits);
   }

   @Test
   public void location7() {
      Point3 o = new Point3(1.5f, 0, 1.5f);
      Vector3 d = new Vector3(-1, 0, 0);
      Ray ray = new Ray(o, d);

      boolean hits = shape.hits(ray);
      Intersection intersection = shape.intersect(ray);

      Point3 expectedPoint = new Point3(1f, 0, 1.5f);
      Assert.assertEquals(intersection.location, expectedPoint);
   }

   @Test
   public void normal7() {
      Point3 o = new Point3(1.5f, 0, 1.5f);
      Vector3 d = new Vector3(-1, 0, 0);
      Ray ray = new Ray(o, d);

      boolean hits = shape.hits(ray);
      Intersection intersection = shape.intersect(ray);

      Normal expectedNormal = new Normal(1, 0, 0);
      Assert.assertEquals(intersection.normal, expectedNormal);
   }
}
