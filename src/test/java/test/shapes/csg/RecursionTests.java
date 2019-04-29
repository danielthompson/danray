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

public class RecursionTests {

   private CSGShape shape;

   @BeforeMethod
   public void setUp() throws Exception {
      Transform[] inputTransforms;
      Transform[] compositeTransforms;

      inputTransforms = new Transform[]{
            Transform.Translate(new Vector(0, 0, 1)),
            Transform.Scale(4),
            Transform.Translate(0, -0.5f, 0)
      };
      compositeTransforms = Transform.composite(inputTransforms);

      Box box1 = new Box(compositeTransforms, null);

      inputTransforms = new Transform[]{
            Transform.Translate(new Vector(1, 0, 0)),
            Transform.Scale(4),
            Transform.Translate(0, -0.5f, 0)
      };
      compositeTransforms = Transform.composite(inputTransforms);

      Box box2 = new Box(compositeTransforms, null);

      inputTransforms = new Transform[]{
            Transform.identity
      };
      compositeTransforms = Transform.composite(inputTransforms);

      CSGShape intersectionShape = new CSGShape(compositeTransforms);
      intersectionShape.Operation = CSGOperation.Intersection;
      intersectionShape.LeftShape = box1;
      intersectionShape.RightShape = box2;

      inputTransforms = new Transform[]{
            Transform.Translate(new Vector(-1, 0, 2)),
            Transform.Scale(6, 1, 1),
            Transform.Translate(0, -0.5f, 0)
      };
      compositeTransforms = Transform.composite(inputTransforms);

      Box box3 = new Box(compositeTransforms, null);

      inputTransforms = new Transform[]{
            Transform.identity
      };
      compositeTransforms = Transform.composite(inputTransforms);

      shape = new CSGShape(compositeTransforms);
      shape.Operation = CSGOperation.Difference;
      shape.LeftShape = intersectionShape;
      shape.RightShape = box3;
   }

   @AfterMethod
   public void tearDown() throws Exception {
      shape = null;
   }

   @Test
   public void location1() {
      Point o = new Point(7.5f, 0, 0.5f);
      Vector d = new Vector(-1, 0, 0);
      Ray ray = new Ray(o, d);

      boolean hits = shape.Hits(ray);
      Assert.assertFalse(hits);
   }

   @Test
   public void location2() {
      Point o = new Point(7.5f, 0, 1.5f);
      Vector d = new Vector(-1, 0, 0);
      Ray ray = new Ray(o, d);

      boolean hits = shape.Hits(ray);
      Assert.assertTrue(hits);

      Intersection intersection = shape.GetHitInfo(ray);

      Point expectedPoint = new Point(4f, 0, 1.5f);
      Assert.assertEquals(intersection.Location, expectedPoint);
   }

   @Test
   public void normal2() {
      Point o = new Point(7.5f, 0, 1.5f);
      Vector d = new Vector(-1, 0, 0);
      Ray ray = new Ray(o, d);

      boolean hits = shape.Hits(ray);
      Intersection intersection = shape.GetHitInfo(ray);

      Normal expectedNormal = new Normal(1, 0, 0);
      Assert.assertEquals(intersection.Normal, expectedNormal);
   }

   @Test
   public void location3() {
      Point o = new Point(7.5f, 0, 2.5f);
      Vector d = new Vector(-1, 0, 0);
      Ray ray = new Ray(o, d);

      boolean hits = shape.Hits(ray);
      Assert.assertFalse(hits);
   }

   @Test
   public void location4() {
      Point o = new Point(7.5f, 0, 4.5f);
      Vector d = new Vector(-1, 0, 0);
      Ray ray = new Ray(o, d);

      boolean hits = shape.Hits(ray);
      Assert.assertFalse(hits);
   }

   @Test
   public void location5() {
      Point o = new Point(5.5f, 0, 6.5f);
      Vector d = new Vector(0, 0, -1);
      Ray ray = new Ray(o, d);

      boolean hits = shape.Hits(ray);
      Assert.assertFalse(hits);
   }

   @Test
   public void location6() {
      Point o = new Point(4.5f, 0, 6.5f);
      Vector d = new Vector(0, 0, -1);
      Ray ray = new Ray(o, d);

      boolean hits = shape.Hits(ray);
      Assert.assertFalse(hits);
   }

   @Test
   public void location7() {
      Point o = new Point(3.5f, 0, 6.5f);
      Vector d = new Vector(0, 0, -1);
      Ray ray = new Ray(o, d);

      boolean hits = shape.Hits(ray);
      Assert.assertTrue(hits);

      Intersection intersection = shape.GetHitInfo(ray);

      Point expectedPoint = new Point(3.5f, 0, 4f);
      Assert.assertEquals(intersection.Location, expectedPoint);
   }

   @Test
   public void normal7() {
      Point o = new Point(3.5f, 0, 6.5f);
      Vector d = new Vector(0, 0, -1);
      Ray ray = new Ray(o, d);

      boolean hits = shape.Hits(ray);
      Intersection intersection = shape.GetHitInfo(ray);

      Normal expectedNormal = new Normal(0, 0, 1);
      Assert.assertEquals(intersection.Normal, expectedNormal);
   }

   @Test
   public void location8() {
      Point o = new Point(-3.5f, 0, 3.5f);
      Vector d = new Vector(1, 0, 0);
      Ray ray = new Ray(o, d);

      boolean hits = shape.Hits(ray);
      Assert.assertTrue(hits);

      Intersection intersection = shape.GetHitInfo(ray);

      Point expectedPoint = new Point(1.0f, 0, 3.5f);
      Assert.assertEquals(intersection.Location, expectedPoint);
   }

   @Test
   public void normal8() {
      Point o = new Point(3.5f, 0, 6.5f);
      Vector d = new Vector(0, 0, -1);
      Ray ray = new Ray(o, d);

      boolean hits = shape.Hits(ray);
      Intersection intersection = shape.GetHitInfo(ray);

      Normal expectedNormal = new Normal(0, 0, 1);
      Assert.assertEquals(intersection.Normal, expectedNormal);
   }
}
