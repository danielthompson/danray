package test.acceleration.kdtree;

import net.danielthompson.danray.shapes.AbstractShape;
import net.danielthompson.danray.shapes.Sphere;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.Point3;
import net.danielthompson.danray.acceleration.KDScene;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Transform;
import net.danielthompson.danray.structures.Vector3;
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
      List<AbstractShape> objects = new ArrayList<AbstractShape>();

      Transform[] inputTransforms;
      Transform[] compositeTransforms;

      // sphere 1

      inputTransforms = new Transform[1];
      inputTransforms[0] = Transform.Translate(new Vector3(-1, 5, 0));
      compositeTransforms = Transform.composite(inputTransforms);

      sphere1 = new Sphere(compositeTransforms, null);
      objects.add(sphere1);

      // sphere 1

      inputTransforms = new Transform[1];
      inputTransforms[0] = Transform.Translate(new Vector3(-1, 13, 0));
      compositeTransforms = Transform.composite(inputTransforms);

      sphere2 = new Sphere(compositeTransforms, null);
      objects.add(sphere2);

      // sphere 3

      inputTransforms = new Transform[1];
      inputTransforms[0] = Transform.Translate(new Vector3(6, 10, 0));
      compositeTransforms = Transform.composite(inputTransforms);

      sphere3 = new Sphere(compositeTransforms, null);
      objects.add(sphere3);

      // sphere 3

      inputTransforms = new Transform[1];
      inputTransforms[0] = Transform.Translate(new Vector3(6, 7, 0));
      compositeTransforms = Transform.composite(inputTransforms);

      sphere4 = new Sphere(compositeTransforms, null);

      objects.add(sphere4);

      scene = new KDScene(null);

      for (AbstractShape element : objects) {
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

      Point3 origin = new Point3(8, 12, 0);
      Vector3 direction = new Vector3(-1, -1, 0);

      Ray ray = new Ray(origin, direction);

      Intersection state = scene.getNearestShape(ray, 0, 0);

      Assert.assertNotNull(state, "intersection state should not be null");
      Assert.assertTrue(state.Hits, "state should hit");
      Assert.assertNotNull(state.Shape);
      Assert.assertEquals(state.Shape, sphere3);
      System.out.println(scene.drawableIntersections);
   }

   @Test
   public void testTraverse2() throws Exception {
      Point3 origin = new Point3(13, 15, 0);
      Vector3 direction = new Vector3(-7, -5, 0);

      Ray ray = new Ray(origin, direction);

      Intersection state = scene.getNearestShape(ray, 0, 0);

      Assert.assertNotNull(state, "intersection state should not be null");
      Assert.assertTrue(state.Hits, "state should hit");
      Assert.assertNotNull(state.Shape);
      Assert.assertEquals(state.Shape, sphere3);
      System.out.println(scene.drawableIntersections);
   }

   @Test
   public void testTraverse3() throws Exception {
      Point3 origin = new Point3(8, 5, 0);
      Vector3 direction = new Vector3(-1, 1, 0);

      Ray ray = new Ray(origin, direction);

      Intersection state = scene.getNearestShape(ray, 0, 0);

      Assert.assertNotNull(state, "intersection state should not be null");
      Assert.assertTrue(state.Hits, "state should hit");
      Assert.assertNotNull(state.Shape);
      Assert.assertEquals(state.Shape, sphere4);
      System.out.println(scene.drawableIntersections);
   }

   @Test
   public void testTraverse4() throws Exception {
      Point3 origin = new Point3(13, 7, 0);
      Vector3 direction = new Vector3(-7, 3, 0);

      Ray ray = new Ray(origin, direction);

      Intersection state = scene.getNearestShape(ray, 0, 0);

      Assert.assertNotNull(state, "intersection state should not be null");
      Assert.assertTrue(state.Hits, "state should hit");
      Assert.assertNotNull(state.Shape);
      Assert.assertEquals(state.Shape, sphere3);
      System.out.println(scene.drawableIntersections);
   }

   @Test
   public void testTraverse5() throws Exception {
      Point3 origin = new Point3(4, 1, 0);
      Vector3 direction = new Vector3(2, 7, 0);

      Ray ray = new Ray(origin, direction);

      Intersection state = scene.getNearestShape(ray, 0, 0);

      Assert.assertNotNull(state, "intersection state should not be null");
      Assert.assertTrue(state.Hits, "state should hit");
      Assert.assertNotNull(state.Shape);
      Assert.assertEquals(state.Shape, sphere4);
      System.out.println(scene.drawableIntersections);
   }
}
