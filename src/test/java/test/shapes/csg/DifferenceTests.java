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
            Transform.Translate(new Vector(0, 0, 1)),
            Transform.Scale(2),
            Transform.Translate(0, -0.5f, 0)

      };
      Transform[] compositeTransforms = Transform.composite(inputTransforms);

      shape.LeftShape = new Box(compositeTransforms, null);

      inputTransforms = new Transform[]{
            Transform.Translate(new Vector(1, 0, 0)),
            Transform.Scale(2),
            Transform.Translate(0, -0.5f, 0)

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
      Point origin = new Point(1, 0, -1);
      Vector direction = new Vector(1, 0, 1);
      Ray ray = new Ray(origin, direction);

      boolean hits = shape.Hits(ray);

      Assert.assertFalse(hits);
   }

   @Test
   public void shouldNotHit2() {
      Point origin = new Point(1, 0, -0.5f);
      Vector direction = new Vector(1, 0, 1);
      Ray ray = new Ray(origin, direction);

      boolean hits = shape.Hits(ray);

      Assert.assertFalse(hits);
   }

   @Test
   public void shouldHit1() {
      Point origin = new Point(0, 0, -2);
      Vector direction = new Vector(2, 0, 5);
      Ray ray = new Ray(origin, direction);

      boolean hits = shape.Hits(ray);
      Assert.assertTrue(hits);
   }

   @Test
   public void shouldHit2() {
      Point origin = new Point(0, 0, -2);
      Vector direction = new Vector(2, 0, 5);
      Ray ray = new Ray(origin, direction);

      boolean hits = shape.Hits(ray);

      Intersection intersection = shape.GetHitInfo(ray);


      Assert.assertNotNull(intersection);
      Assert.assertTrue(intersection.Hits);
   }

   @Test
   public void shouldHit2Normal() {
      Point origin = new Point(0, 0, -2);
      Vector direction = new Vector(2, 0, 5);
      Ray ray = new Ray(origin, direction);

      boolean hits = shape.Hits(ray);

      Intersection intersection = shape.GetHitInfo(ray);

      Normal expectedNormal = new Normal(0, 0, -1);

      Assert.assertNotNull(intersection.Normal);
      AssertHelper.assertEquals(intersection.Normal, expectedNormal);
   }

   @Test
   public void shouldHit2Location() {
      Point origin = new Point(0, 0, -2);
      Vector direction = new Vector(2, 0, 5);
      Ray ray = new Ray(origin, direction);

      boolean hits = shape.Hits(ray);

      Intersection intersection = shape.GetHitInfo(ray);

      Point expectedHitPoint = new Point(1.6f, 0, 2);

      Assert.assertNotNull(intersection.Location);
      AssertHelper.assertEquals(intersection.Location, expectedHitPoint);
   }

   @Test
   public void normal1() {
      Point o = new Point(.5f, 0, 5);
      Vector d = new Vector(0, 0, -1);
      Ray ray = new Ray(o, d);

      boolean hits = shape.Hits(ray);
      Intersection intersection = shape.GetHitInfo(ray);

      Normal expectedNormal = new Normal(0, 0, 1);
      Assert.assertEquals(intersection.Normal, expectedNormal);
   }

   @Test
   public void normal2() {
      Point o = new Point(.5f, 0, -5);
      Vector d = new Vector(0, 0, 1);
      Ray ray = new Ray(o, d);

      boolean hits = shape.Hits(ray);
      Intersection intersection = shape.GetHitInfo(ray);

      Normal expectedNormal = new Normal(0, 0, -1);
      Assert.assertEquals(intersection.Normal, expectedNormal);
   }

   @Test
   public void location1() {
      Point o = new Point(.5f, 0, 5);
      Vector d = new Vector(0, 0, -1);
      Ray ray = new Ray(o, d);

      boolean hits = shape.Hits(ray);
      Intersection intersection = shape.GetHitInfo(ray);

      Point expectedPoint = new Point(0.5f, 0, 3);
      AssertHelper.assertEquals(intersection.Location, expectedPoint);
   }

   @Test
   public void location2() {
      Point o = new Point(.5f, 0, -5);
      Vector d = new Vector(0, 0, 1);
      Ray ray = new Ray(o, d);

      boolean hits = shape.Hits(ray);
      Intersection intersection = shape.GetHitInfo(ray);

      Point expectedPoint = new Point(0.5f, 0, 1);
      AssertHelper.assertEquals(intersection.Location, expectedPoint);
   }

   @Test
   public void location3() {
      Point o = new Point(2.5f, 0, 1.5f);
      Vector d = new Vector(-1, 0, 0);
      Ray ray = new Ray(o, d);

      boolean hits = shape.Hits(ray);
      Intersection intersection = shape.GetHitInfo(ray);

      Point expectedPoint = new Point(1f, 0, 1.5f);
      AssertHelper.assertEquals(intersection.Location, expectedPoint);
   }

   @Test
   public void normal3() {
      Point o = new Point(2.5f, 0, 1.5f);
      Vector d = new Vector(-1, 0, 0);
      Ray ray = new Ray(o, d);

      boolean hits = shape.Hits(ray);
      Intersection intersection = shape.GetHitInfo(ray);

      Normal expectedNormal = new Normal(1, 0, 0);
      Assert.assertEquals(intersection.Normal, expectedNormal);
   }

   @Test
   public void location4() {
      Point o = new Point(3.5f, 0, 1.5f);
      Vector d = new Vector(-1, 0, 0);
      Ray ray = new Ray(o, d);

      boolean hits = shape.Hits(ray);
      Intersection intersection = shape.GetHitInfo(ray);

      Point expectedPoint = new Point(1f, 0, 1.5f);
      AssertHelper.assertEquals(intersection.Location, expectedPoint);
   }

   @Test
   public void normal4() {
      Point o = new Point(3.5f, 0, 1.5f);
      Vector d = new Vector(-1, 0, 0);
      Ray ray = new Ray(o, d);

      boolean hits = shape.Hits(ray);
      Intersection intersection = shape.GetHitInfo(ray);

      Normal expectedNormal = new Normal(1, 0, 0);
      Assert.assertEquals(intersection.Normal, expectedNormal);
   }

   @Test
   public void location5() {
      Point o = new Point(2.5f, 0, 0.5f);
      Vector d = new Vector(-1, 0, 0);
      Ray ray = new Ray(o, d);

      boolean hits = shape.Hits(ray);
      Assert.assertFalse(hits);
   }

   @Test
   public void location6() {
      Point o = new Point(3.5f, 0, 0.5f);
      Vector d = new Vector(-1, 0, 0);
      Ray ray = new Ray(o, d);

      boolean hits = shape.Hits(ray);
      Assert.assertFalse(hits);
   }

   @Test
   public void location7() {
      Point o = new Point(1.5f, 0, 1.5f);
      Vector d = new Vector(-1, 0, 0);
      Ray ray = new Ray(o, d);

      boolean hits = shape.Hits(ray);
      Intersection intersection = shape.GetHitInfo(ray);

      Point expectedPoint = new Point(1f, 0, 1.5f);
      AssertHelper.assertEquals(intersection.Location, expectedPoint);
   }

   @Test
   public void normal7() {
      Point o = new Point(1.5f, 0, 1.5f);
      Vector d = new Vector(-1, 0, 0);
      Ray ray = new Ray(o, d);

      boolean hits = shape.Hits(ray);
      Intersection intersection = shape.GetHitInfo(ray);

      Normal expectedNormal = new Normal(1, 0, 0);
      Assert.assertEquals(intersection.Normal, expectedNormal);
   }
}
