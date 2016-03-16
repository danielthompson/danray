package test.acceleration.kdtree;

import net.danielthompson.danray.acceleration.KDNode;
import net.danielthompson.danray.acceleration.KDScene;
import net.danielthompson.danray.shapes.Shape;
import net.danielthompson.danray.shapes.Sphere;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.BoundingBox;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Vector;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dthompson on 3/16/2016.
 */
public class TraverseTinyTreeTests {


   Sphere sphere1;
   Sphere sphere2;
   Sphere sphere3;

   KDScene scene;



   @BeforeMethod
   public void setUp() throws Exception {
      List<Shape> objects = new ArrayList<Shape>();

      sphere1 = new Sphere();
      sphere1.Origin = new Point(1, 1, 1);
      sphere1.Radius = 1;
      objects.add(sphere1);

      sphere2 = new Sphere();
      sphere2.Origin = new Point(5, 5, 5);
      sphere2.Radius = 1;
      objects.add(sphere2);

      sphere3 = new Sphere();
      sphere3.Origin = new Point(7, 3, 3);
      sphere3.Radius = 1;
      objects.add(sphere3);

      scene = new KDScene(null);

      BoundingBox lessThanBB = new BoundingBox(new Point(0, 0, 0), new Point(6, 6, 6));
      BoundingBox greaterThanBB = new BoundingBox(new Point(6, 2, 2), new Point(8, 4, 4));
      BoundingBox rootBB = new BoundingBox(new Point(0, 0, 0), new Point(8, 6, 6));

      List<Shape> lessThanList = new ArrayList<Shape>();
      lessThanList.add(sphere1);
      lessThanList.add(sphere2);

      List<Shape> greaterThanList = new ArrayList<Shape>();
      greaterThanList.add(sphere3);

      for (Shape element : objects) {
         scene.addDrawableObject(element);
      }

      KDNode lessThanNode = new KDNode(lessThanList);
      lessThanNode._box = lessThanBB;

      KDNode greaterThanNode = new KDNode(greaterThanList);
      greaterThanNode._box = greaterThanBB;

      KDNode rootNode = new KDNode(objects);
      rootNode._box = rootBB;
      rootNode._leftChild = lessThanNode;
      rootNode._rightChild = greaterThanNode;

      scene.rootNode = rootNode;
   }

   @AfterMethod
   public void tearDown() throws Exception {
      scene = null;
   }

   @Test
   public void testTraverse1() throws Exception {

      Point origin = new Point(3, 3, 3);
      Vector direction = new Vector(1, 0, 0);

      Ray ray = new Ray(origin, direction);

      IntersectionState state = scene.GetClosestDrawableToRay(ray);

      Assert.assertNotNull(state, "intersection state should not be null");
      Assert.assertTrue(state.Hits, "state should hit");
      Assert.assertNotNull(state.Shape);
      Assert.assertEquals(state.Shape, sphere3);
      System.out.println(scene.drawableIntersections);
   }


}
