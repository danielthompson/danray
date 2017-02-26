package test.acceleration.kdtree;

import net.danielthompson.danray.shapes.Shape;
import net.danielthompson.danray.shapes.Sphere;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.acceleration.KDScene;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Vector;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 12/7/14.
 */
public class TraverseSmallTreeTests {

   Sphere sphere1;
   Sphere sphere2;
   Sphere sphere3;
   Sphere sphere4;

   KDScene scene;

   @BeforeMethod
   public void setUp() throws Exception {
      List<Shape> objects = new ArrayList<Shape>();

      sphere1 = new Sphere();
      sphere1.Origin = new Point(-1, 5, 0);
      sphere1.Radius = 1;
      objects.add(sphere1);

      sphere2 = new Sphere();
      sphere2.Origin = new Point(-1, 13, 0);
      sphere2.Radius = 1;
      objects.add(sphere2);

      sphere3 = new Sphere();
      sphere3.Origin = new Point(6, 10, 0);
      sphere3.Radius = 1;
      objects.add(sphere3);

      sphere4 = new Sphere();
      sphere4.Origin = new Point(6, 7, 0);
      sphere4.Radius = 1;
      objects.add(sphere4);

      scene = new KDScene(null);

      for (Shape element : objects) {
         scene.addShape(element);
      }

      scene.compile(null);
   }

   @AfterMethod
   public void tearDown() throws Exception {
      scene = null;
   }

   @Test
   public void testTraverse1() throws Exception {

      Point origin = new Point(8, 12, 0);
      Vector direction = new Vector(-1, -1, 0);

      Ray ray = new Ray(origin, direction);

      IntersectionState state = scene.getNearestShape(ray);

      Assert.assertNotNull(state, "intersection state should not be null");
      Assert.assertTrue(state.Hits, "state should hit");
      Assert.assertNotNull(state.Shape);
      Assert.assertEquals(state.Shape, sphere3);
      System.out.println(scene.drawableIntersections);
   }

   @Test
   public void testTraverse2() throws Exception {
      Point origin = new Point(13, 15, 0);
      Vector direction = new Vector(-7, -5, 0);

      Ray ray = new Ray(origin, direction);

      IntersectionState state = scene.getNearestShape(ray);

      Assert.assertNotNull(state, "intersection state should not be null");
      Assert.assertTrue(state.Hits, "state should hit");
      Assert.assertNotNull(state.Shape);
      Assert.assertEquals(state.Shape, sphere3);
      System.out.println(scene.drawableIntersections);
   }

   @Test
   public void testTraverse3() throws Exception {
      Point origin = new Point(8, 5, 0);
      Vector direction = new Vector(-1, 1, 0);

      Ray ray = new Ray(origin, direction);

      IntersectionState state = scene.getNearestShape(ray);

      Assert.assertNotNull(state, "intersection state should not be null");
      Assert.assertTrue(state.Hits, "state should hit");
      Assert.assertNotNull(state.Shape);
      Assert.assertEquals(state.Shape, sphere4);
      System.out.println(scene.drawableIntersections);
   }

   @Test
   public void testTraverse4() throws Exception {
      Point origin = new Point(13, 7, 0);
      Vector direction = new Vector(-7, 3, 0);

      Ray ray = new Ray(origin, direction);

      IntersectionState state = scene.getNearestShape(ray);

      Assert.assertNotNull(state, "intersection state should not be null");
      Assert.assertTrue(state.Hits, "state should hit");
      Assert.assertNotNull(state.Shape);
      Assert.assertEquals(state.Shape, sphere3);
      System.out.println(scene.drawableIntersections);
   }

   @Test
   public void testTraverse5() throws Exception {
      Point origin = new Point(4, 1, 0);
      Vector direction = new Vector(2, 7, 0);

      Ray ray = new Ray(origin, direction);

      IntersectionState state = scene.getNearestShape(ray);

      Assert.assertNotNull(state, "intersection state should not be null");
      Assert.assertTrue(state.Hits, "state should hit");
      Assert.assertNotNull(state.Shape);
      Assert.assertEquals(state.Shape, sphere4);
      System.out.println(scene.drawableIntersections);
   }
}
