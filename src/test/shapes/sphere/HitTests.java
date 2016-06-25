package shapes.sphere;

import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.shapes.Sphere;
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
   public void testOrthogonalHitX1() throws Exception {
      Sphere sphere = new Sphere();
      sphere.Origin = new Point(0, 0, 0);
      sphere.Radius = 1;

      Point Origin = new Point(2, 0, 0);
      Vector Direction = new Vector(1, 0, 0);
      Ray ray = new Ray(Origin, Direction);

      Assert.assertFalse(sphere.getHitInfo(ray).Hits, "Vector shouldn't hit sphere.");
   }

   @Test
   public void testOrthogonalHitX2() throws Exception {
      Sphere sphere = new Sphere();
      sphere.Origin = new Point(0, 0, 0);
      sphere.Radius = 1;

      Point Origin = new Point(-2, 0, 0);
      Vector Direction = new Vector(1, 0, 0);
      Ray ray = new Ray(Origin, Direction);

      Assert.assertTrue(sphere.getHitInfo(ray).Hits, "Vector should hit sphere.");
   }

   @Test
   public void testOrthogonalNoHitX1() throws Exception {
      Sphere sphere = new Sphere();
      sphere.Origin = new Point(0, 0, 0);
      sphere.Radius = 1;

      Point Origin = new Point(2, 0, 0);
      Vector Direction = new Vector(1, 0, 0);
      Ray ray = new Ray(Origin, Direction);

      Assert.assertFalse(sphere.getHitInfo(ray).Hits, "Vector shouldn't hit sphere.");
   }

   @Test
   public void testOrthogonalNoHitX2() throws Exception {
      Sphere sphere = new Sphere();
      sphere.Origin = new Point(0, 0, 0);
      sphere.Radius = 1;

      Point Origin = new Point(-2, 0, 0);
      Vector Direction = new Vector(-1, 0, 0);
      Ray ray = new Ray(Origin, Direction);

      Assert.assertFalse(sphere.getHitInfo(ray).Hits, "Vector shouldn't hit sphere.");
   }

   @Test
   public void testHit2() throws Exception {
      Sphere sphere = new Sphere();
      sphere.Origin = new Point(0, 0, 0);
      sphere.Radius = 1;

      Point Origin = new Point(10, 0, 0);
      Vector Direction = new Vector(1, 0, 0);
      Ray ray = new Ray(Origin, Direction);

      Assert.assertFalse(sphere.Inside(Origin), "Point " + Origin + " is outside sphere.");
      Assert.assertFalse(sphere.getHitInfo(ray).Hits, "Vector shouldn't hit sphere.");
   }

   @Test
   public void testHit3() throws Exception {
      Sphere sphere = new Sphere();
      sphere.Origin = new Point(0, 0, 0);
      sphere.Radius = 1;

      Point Origin = new Point(0, 10, 0);
      Vector Direction = new Vector(-1, 0, 0);
      Ray ray = new Ray(Origin, Direction);

      Assert.assertFalse(sphere.getHitInfo(ray).Hits, "Vector shouldn't hit sphere.");
   }

   @Test
   public void testHit4() throws Exception {
      Sphere sphere = new Sphere();
      sphere.Origin = new Point(0, 0, 0);
      sphere.Radius = 1;

      Point Origin = new Point(0, 10, 0);
      Vector Direction = new Vector(0, -1, 0);
      Ray ray = new Ray(Origin, Direction);

      Assert.assertTrue(sphere.getHitInfo(ray).Hits, "Vector should hit sphere.");
   }

   @Test
   public void testHit5() throws Exception {
      Sphere sphere = new Sphere();
      sphere.Origin = new Point(0, 0, 0);
      sphere.Radius = 1;

      Point Origin = new Point(0, 10, 0);
      Vector Direction = new Vector(0, 10, 0);
      Ray ray = new Ray(Origin, Direction);

      Assert.assertFalse(sphere.getHitInfo(ray).Hits, "Vector shouldn't hit sphere.");
   }

   @Test
   public void testHit6() throws Exception {
      Sphere sphere = new Sphere();
      sphere.Origin = new Point(0, 0, 0);
      sphere.Radius = 1;

      Point Origin = new Point(10, 10, 0);
      Vector Direction = new Vector(-1, -1, 0);
      Ray ray = new Ray(Origin, Direction);

      Assert.assertTrue(sphere.getHitInfo(ray).Hits, "Vector should hit sphere.");
   }

   @Test
   public void testHit8() throws Exception {
      Sphere sphere = new Sphere();
      sphere.Origin = new Point(0, 0, 0);
      sphere.Radius = 1;

      Point Origin = new Point(-10, -10, 0);
      Vector Direction = new Vector(1, 1, 0);
      Ray ray = new Ray(Origin, Direction);

      Assert.assertTrue(sphere.getHitInfo(ray).Hits, "Vector should hit sphere.");
   }

   @Test
   public void testHit9() throws Exception {
      Sphere sphere = new Sphere();
      sphere.Origin = new Point(0, 0, 0);
      sphere.Radius = 1;

      Point Origin = new Point(-10, -10, 2);
      Vector Direction = new Vector(1, 1, 0);
      Ray ray = new Ray(Origin, Direction);

      Assert.assertFalse(sphere.getHitInfo(ray).Hits, "Vector shouldn't hit sphere.");
   }

   @Test
   public void testHit10() throws Exception {
      Sphere sphere = new Sphere();
      sphere.Origin = new Point(0, 0, 0);
      sphere.Radius = 1;

      Point Origin = new Point(0, -10, 0);
      Vector Direction = new Vector(0, 1, 0);
      Ray ray = new Ray(Origin, Direction);

      Assert.assertTrue(sphere.getHitInfo(ray).Hits, "Vector should hit sphere.");
   }
}
