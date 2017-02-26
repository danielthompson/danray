package test.structures.boundingbox;


import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.BoundingBox;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Vector;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Created by daniel on 12/7/14.
 */
public class IntersectionTests {
   @BeforeMethod
   public void setUp() throws Exception {

   }

   @AfterMethod
   public void tearDown() throws Exception {

   }

   @Test
   public void testRayIntersection1() throws Exception {

      // arrange
      BoundingBox box = new BoundingBox(new Point(0, 0, 0), new Point(10, 10, 10));
      Point origin = new Point(5, 5, 5);
      Vector direction = new Vector(1, 0, 0);
      Ray ray = new Ray(origin, direction);

      // act
      IntersectionState state = box.GetHitInfo(ray);

      // assert
      Assert.assertNotNull(state, "intersection state shouldn't be null");
      Assert.assertTrue(state.Hits, "state should hit");
      Assert.assertEquals(state.TMin, 5.0, "TMin should be 5");

   }
}
