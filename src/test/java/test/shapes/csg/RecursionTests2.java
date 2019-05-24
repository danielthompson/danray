package test.shapes.csg;

import net.danielthompson.danray.shapes.Box;
import net.danielthompson.danray.shapes.csg.CSGOperation;
import net.danielthompson.danray.shapes.csg.CSGShape;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.*;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class RecursionTests2 {

   private CSGShape shape;

   @BeforeMethod
   public void setUp() throws Exception {
      Transform[] inputTransforms;
      Transform[] compositeTransforms;

      CSGShape intersectionShape;
      CSGShape unionShape;

      // left - intersection

      {
         inputTransforms = new Transform[]{
               Transform.Translate(new Vector3(0, 0, 1)),
               Transform.Scale(4),
               Transform.Translate(0, -0.5f, 0)
         };
         compositeTransforms = Transform.composite(inputTransforms);

         Box left = new Box(compositeTransforms, null);

         inputTransforms = new Transform[]{
               Transform.Translate(new Vector3(1, 0, 0)),
               Transform.Scale(4),
               Transform.Translate(0, -0.5f, 0)
         };
         compositeTransforms = Transform.composite(inputTransforms);

         Box right = new Box(compositeTransforms, null);

         inputTransforms = new Transform[]{
               Transform.identity
         };
         compositeTransforms = Transform.composite(inputTransforms);

         intersectionShape = new CSGShape(compositeTransforms);
         intersectionShape.Operation = CSGOperation.Intersection;
         intersectionShape.LeftShape = left;
         intersectionShape.RightShape = right;
      }

      // right - union

      {
         inputTransforms = new Transform[]{
               Transform.Translate(new Vector3(-1, 0, 2)),
               Transform.Scale(7, 1, 1),
               Transform.Translate(0, -0.5f, 0)
         };
         compositeTransforms = Transform.composite(inputTransforms);

         Box left = new Box(compositeTransforms, null);

         inputTransforms = new Transform[]{
               Transform.Translate(new Vector3(2, 0, -1)),
               Transform.Scale(1, 1, 7),
               Transform.Translate(0, -0.5f, 0)
         };
         compositeTransforms = Transform.composite(inputTransforms);

         Box right = new Box(compositeTransforms, null);

         inputTransforms = new Transform[]{
               Transform.identity
         };
         compositeTransforms = Transform.composite(inputTransforms);

         unionShape = new CSGShape(compositeTransforms);
         unionShape.Operation = CSGOperation.Union;
         unionShape.LeftShape = left;
         unionShape.RightShape = right;
      }

      shape = new CSGShape(compositeTransforms);
      shape.Operation = CSGOperation.Difference;
      shape.LeftShape = intersectionShape;
      shape.RightShape = unionShape;
   }

   @AfterMethod
   public void tearDown() throws Exception {
      shape = null;
   }

   @Test
   public void location1() {
      Point3 o = new Point3(8.5f, 0, 0.5f);
      Vector3 d = new Vector3(-1, 0, 0);
      Ray ray = new Ray(o, d);

      boolean hits = shape.Hits(ray);
      Assert.assertFalse(hits);
   }

   @Test
   public void location2() {
      Point3 o = new Point3(8.5f, 0, 1.5f);
      Vector3 d = new Vector3(-1, 0, 0);
      Ray ray = new Ray(o, d);

      boolean hits = shape.Hits(ray);
      Assert.assertTrue(hits);

      Intersection intersection = shape.GetHitInfo(ray);

      Point3 expectedPoint = new Point3(4f, 0, 1.5f);
      Assert.assertEquals(intersection.location, expectedPoint);
   }

   @Test
   public void normal2() {
      Point3 o = new Point3(8.5f, 0, 1.5f);
      Vector3 d = new Vector3(-1, 0, 0);
      Ray ray = new Ray(o, d);

      boolean hits = shape.Hits(ray);
      Intersection intersection = shape.GetHitInfo(ray);

      Normal expectedNormal = new Normal(1, 0, 0);
      Assert.assertEquals(intersection.normal, expectedNormal);
   }

   @Test
   public void location3() {
      Point3 o = new Point3(8.5f, 0, 2.5f);
      Vector3 d = new Vector3(-1, 0, 0);
      Ray ray = new Ray(o, d);

      boolean hits = shape.Hits(ray);
      Assert.assertFalse(hits);
   }

   @Test
   public void location4() {
      Point3 o = new Point3(8.5f, 0, 4.5f);
      Vector3 d = new Vector3(-1, 0, 0);
      Ray ray = new Ray(o, d);

      boolean hits = shape.Hits(ray);
      Assert.assertFalse(hits);
   }

   @Test
   public void location5() {
      Point3 o = new Point3(5.5f, 0, 6.5f);
      Vector3 d = new Vector3(0, 0, -1);
      Ray ray = new Ray(o, d);

      boolean hits = shape.Hits(ray);
      Assert.assertFalse(hits);
   }

   @Test
   public void location6() {
      Point3 o = new Point3(4.5f, 0, 6.5f);
      Vector3 d = new Vector3(0, 0, -1);
      Ray ray = new Ray(o, d);

      boolean hits = shape.Hits(ray);
      Assert.assertFalse(hits);
   }

   @Test
   public void location7() {
      Point3 o = new Point3(3.5f, 0, 6.5f);
      Vector3 d = new Vector3(0, 0, -1);
      Ray ray = new Ray(o, d);

      boolean hits = shape.Hits(ray);
      Assert.assertTrue(hits);

      Intersection intersection = shape.GetHitInfo(ray);

      Point3 expectedPoint = new Point3(3.5f, 0, 4f);
      Assert.assertEquals(intersection.location, expectedPoint);
   }

   @Test
   public void normal7() {
      Point3 o = new Point3(3.5f, 0, 6.5f);
      Vector3 d = new Vector3(0, 0, -1);
      Ray ray = new Ray(o, d);

      boolean hits = shape.Hits(ray);
      Intersection intersection = shape.GetHitInfo(ray);

      Normal expectedNormal = new Normal(0, 0, 1);
      Assert.assertEquals(intersection.normal, expectedNormal);
   }

   @Test
   public void location8() {
      Point3 o = new Point3(-3.5f, 0, 3.5f);
      Vector3 d = new Vector3(1, 0, 0);
      Ray ray = new Ray(o, d);

      boolean hits = shape.Hits(ray);
      Assert.assertTrue(hits);

      Intersection intersection = shape.GetHitInfo(ray);

      Point3 expectedPoint = new Point3(1.0f, 0, 3.5f);
      Assert.assertEquals(intersection.location, expectedPoint);
   }

   @Test
   public void normal8() {
      Point3 o = new Point3(-3.5f, 0, 3.5f);
      Vector3 d = new Vector3(1, 0, 0);
      Ray ray = new Ray(o, d);

      boolean hits = shape.Hits(ray);
      Intersection intersection = shape.GetHitInfo(ray);

      Normal expectedNormal = new Normal(-1, 0, 0);
      Assert.assertEquals(intersection.normal, expectedNormal);
   }

   @Test
   public void location9() {
      Point3 o = new Point3(2.5f, 0, 6.5f);
      Vector3 d = new Vector3(0, 0, -1);
      Ray ray = new Ray(o, d);

      boolean hits = shape.Hits(ray);
      Assert.assertFalse(hits);
   }

   @Test
   public void location10() {
      Point3 o = new Point3(8.5f, 0, 7f);
      Vector3 d = new Vector3(-1f, 0, -1f);
      Ray ray = new Ray(o, d);

      boolean hits = shape.Hits(ray);
      Assert.assertTrue(hits);

      Intersection intersection = shape.GetHitInfo(ray);

      Point3 expectedPoint = new Point3(3.5f, 0, 2f);
      Assert.assertEquals(intersection.location, expectedPoint);
   }

   @Test
   public void normal10() {
      Point3 o = new Point3(8.5f, 0, 7f);
      Vector3 d = new Vector3(-1f, 0, -1f);
      Ray ray = new Ray(o, d);

      boolean hits = shape.Hits(ray);
      Intersection intersection = shape.GetHitInfo(ray);

      Normal expectedNormal = new Normal(0, 0, 1);
      Assert.assertEquals(intersection.normal, expectedNormal);
   }
}
