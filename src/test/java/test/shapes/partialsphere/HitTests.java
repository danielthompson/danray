package test.shapes.partialsphere;

import net.danielthompson.danray.shapes.PartialSphere;
import net.danielthompson.danray.shapes.Sphere;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.Constants;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Vector;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * DanRay
 * User: dthompson
 * Date: 6/28/13
 * Time: 5:13 PM
 */
public class HitTests {


   @Test
   public void testOrthogonalHitX1() {
      PartialSphere sphere = new PartialSphere(null, null, Constants.PI, Constants.PI);

      Point Origin = new Point(2, 0, 0);
      Vector Direction = new Vector(1, 0, 0);
      Ray ray = new Ray(Origin, Direction);

      boolean hits = sphere.hits(ray);

      Assert.assertFalse(hits, "Vector shouldn't hit partial sphere.");
   }

   @Test
   public void testOrthogonalHitX2(){
      PartialSphere sphere = new PartialSphere(null, null, Constants.PI, Constants.PI);

      Point Origin = new Point(-2, 0, 0);
      Vector Direction = new Vector(1, 0, 0);
      Ray ray = new Ray(Origin, Direction);

      boolean hits = sphere.hits(ray);

      Assert.assertTrue(hits, "Vector should hit partial sphere.");
   }

   @Test
   public void testPartialHit1() {
      PartialSphere sphere = new PartialSphere(null, null, Constants.PIOver2, Constants.PI);

      Point Origin = new Point(.1f, 2, .1f);
      Vector Direction = new Vector(0, -1, 0);
      Ray ray = new Ray(Origin, Direction);

      boolean hits = sphere.hits(ray);
      Assert.assertTrue(hits, "Vector should hit partial sphere.");
   }

   @Test
   public void testPartialHit2(){
      PartialSphere sphere = new PartialSphere(null, null, Constants.PIOver2, Constants.PI);

      Point Origin = new Point(-.1f, 2, .1f);
      Vector Direction = new Vector(0, -1, 0);
      Ray ray = new Ray(Origin, Direction);

      boolean hits = sphere.hits(ray);

      Assert.assertFalse(hits, "Vector should not hit partial sphere.");
   }

   @Test
   public void testPartialHit3(){
      PartialSphere sphere = new PartialSphere(null, null, Constants.PIOver2, Constants.PI);

      Point Origin = new Point(-.1f, 2, -.1f);
      Vector Direction = new Vector(0, -1, 0);
      Ray ray = new Ray(Origin, Direction);

      boolean hits = sphere.hits(ray);

      Assert.assertTrue(hits, "Vector should hit partial sphere.");
   }

   @Test
   public void testPartialHit4(){
      PartialSphere sphere = new PartialSphere(null, null, Constants.PIOver2, Constants.PI);

      Point Origin = new Point(.1f, 2, -.1f);
      Vector Direction = new Vector(0, -1, 0);
      Ray ray = new Ray(Origin, Direction);

      boolean hits = sphere.hits(ray);

      Assert.assertTrue(hits, "Vector should hit partial sphere.");
   }

   @Test
   public void testPartialHit5() {
      PartialSphere sphere = new PartialSphere(null, null, 0, Constants.PI);

      Point Origin = new Point(.1f, 2, .1f);
      Vector Direction = new Vector(0, -1, 0);
      Ray ray = new Ray(Origin, Direction);

      boolean hits = sphere.hits(ray);
      Assert.assertFalse(hits, "Vector should not hit partial sphere.");
   }

   @Test
   public void testPartialHit6(){
      PartialSphere sphere = new PartialSphere(null, null, 0, Constants.PI);

      Point Origin = new Point(-.1f, 2, .1f);
      Vector Direction = new Vector(0, -1, 0);
      Ray ray = new Ray(Origin, Direction);

      boolean hits = sphere.hits(ray);

      Assert.assertFalse(hits, "Vector should not hit partial sphere.");
   }

   @Test
   public void testPartialHit7(){
      PartialSphere sphere = new PartialSphere(null, null, 0, Constants.PI);

      Point Origin = new Point(-.1f, 2, -.1f);
      Vector Direction = new Vector(0, -1, 0);
      Ray ray = new Ray(Origin, Direction);

      boolean hits = sphere.hits(ray);

      Assert.assertTrue(hits, "Vector should hit partial sphere.");
   }

   @Test
   public void testPartialHit8(){
      PartialSphere sphere = new PartialSphere(null, null, 0, Constants.PI);

      Point Origin = new Point(.1f, 2, -.1f);
      Vector Direction = new Vector(0, -1, 0);
      Ray ray = new Ray(Origin, Direction);

      boolean hits = sphere.hits(ray);

      Assert.assertTrue(hits, "Vector should hit partial sphere.");
   }

}
