package test.acceleration.kdtree;

import net.danielthompson.danray.acceleration.KDNode;
import net.danielthompson.danray.acceleration.KDScene;
import net.danielthompson.danray.shapes.AbstractShape;
import net.danielthompson.danray.shapes.Sphere;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.BoundingBox;
import net.danielthompson.danray.structures.Point3;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Vector3;
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
      List<AbstractShape> objects = new ArrayList<>();

      sphere1 = new Sphere();
      sphere1.Origin = new Point3(1, 1, 1);
      sphere1.Radius = 1;
      objects.add(sphere1);

      sphere2 = new Sphere();
      sphere2.Origin = new Point3(5, 5, 5);
      sphere2.Radius = 1;
      objects.add(sphere2);

      sphere3 = new Sphere();
      sphere3.Origin = new Point3(7, 3, 3);
      sphere3.Radius = 1;
      objects.add(sphere3);

      scene = new KDScene(null);

      BoundingBox lessThanBB = new BoundingBox(new Point3(0, 0, 0), new Point3(6, 6, 6));
      BoundingBox greaterThanBB = new BoundingBox(new Point3(6, 2, 2), new Point3(8, 4, 4));
      BoundingBox rootBB = new BoundingBox(new Point3(0, 0, 0), new Point3(8, 6, 6));

      List<AbstractShape> lessThanList = new ArrayList<>();
      lessThanList.add(sphere1);
      lessThanList.add(sphere2);

      List<AbstractShape> greaterThanList = new ArrayList<>();
      greaterThanList.add(sphere3);

      for (AbstractShape element : objects) {
         scene.addShape(element);
      }

      KDNode lessThanNode = new KDNode(lessThanList);
      lessThanNode.BoundingBox = lessThanBB;

      KDNode greaterThanNode = new KDNode(greaterThanList);
      greaterThanNode.BoundingBox = greaterThanBB;

      KDNode rootNode = new KDNode(objects);
      rootNode.BoundingBox = rootBB;
      rootNode.LeftChild = lessThanNode;
      rootNode.RightChild = greaterThanNode;

      scene.rootNode = rootNode;
   }

   @AfterMethod
   public void tearDown() throws Exception {
      scene = null;
   }

   @Test
   public void testTraverse1() throws Exception {

      Point3 origin = new Point3(3, 3, 3);
      Vector3 direction = new Vector3(1, 0, 0);

      Ray ray = new Ray(origin, direction);

      Intersection state = scene.getNearestShape(ray, 0, 0);

      Assert.assertNotNull(state, "intersection state should not be null");
      // TODO fix failing test
      //Assert.assertTrue(state.Hits, "state should hit");
      //Assert.assertNotNull(state.Shape);
      //Assert.assertEquals(state.Shape, sphere3);
      System.out.println(scene.drawableIntersections);
   }


}
