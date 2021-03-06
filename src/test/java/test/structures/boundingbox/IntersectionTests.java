package test.structures.boundingbox;


import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.*;
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
      Intersection state = box.GetHitInfo(ray);

      // assert
      Assert.assertNotNull(state, "intersection state shouldn't be null");
      Assert.assertTrue(state.Hits, "state should hit");
      Assert.assertEquals(state.t, 0.0f, Constants.UnitTestDelta);
      //Assert.assertEquals(state.TMax, 5.0f, Constants.UnitTestDelta * 5.0f);

   }
}
