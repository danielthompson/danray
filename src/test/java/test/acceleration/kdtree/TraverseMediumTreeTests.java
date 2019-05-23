package test.acceleration.kdtree;

import net.danielthompson.danray.shapes.AbstractShape;
import net.danielthompson.danray.shapes.Sphere;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.Point3;
import net.danielthompson.danray.acceleration.KDScene;
import net.danielthompson.danray.structures.Ray;
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
public class TraverseMediumTreeTests {
   @BeforeMethod
   public void setUp() throws Exception {

   }

   @AfterMethod
   public void tearDown() throws Exception {

   }

   @Test
   public void testTraverse() throws Exception {
      List<AbstractShape> objects = new ArrayList<AbstractShape>();

      Sphere sphere;

      for (int x = 0; x < 5; x++) {
         for (int y = 0; y < 5; y++) {
            for (int z = 0; z < 5; z++) {
               sphere = new Sphere();
               sphere.Origin = new Point3(x * 10, y * 10, z * 10);
               sphere.Radius = 1;

               objects.add(sphere);
            }
         }
      }

      KDScene scene = new KDScene(null);

      for (AbstractShape element : objects) {
         scene.addShape(element);
      }

      scene.compile(null);

      Point3 origin = new Point3(-10, 10, 10);
      Vector3 direction = new Vector3(1, 0, 0);

      Ray ray = new Ray(origin, direction);

      Intersection state = scene.getNearestShape(ray, 0, 0);

      Assert.assertNotNull(state, "intersection state should not be null");
      // TODO fix failing test
      //Assert.assertTrue(state.Hits, "state should hit");
      //Assert.assertNotNull(state.Shape);
      System.out.println(scene.drawableIntersections);

   }
}
