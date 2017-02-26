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
 * Created by dthompson on 3/10/2016.
 */
public class SelfIntersectionTests {

   @BeforeMethod
   public void setUp() throws Exception {

   }

   @AfterMethod
   public void tearDown() throws Exception {

   }

   private static void AssertEquals(BoundingBox actual, BoundingBox expected) {
      Assert.assertNotNull(actual, "actual should not be null");
      Assert.assertNotNull(expected, "expected should not be null");
      Assert.assertEquals(actual.point1.X, expected.point1.X);
      Assert.assertEquals(actual.point1.Y, expected.point1.Y);
      Assert.assertEquals(actual.point1.Z, expected.point1.Z);
      Assert.assertEquals(actual.point2.X, expected.point2.X);
      Assert.assertEquals(actual.point2.Y, expected.point2.Y);
      Assert.assertEquals(actual.point2.Z, expected.point2.Z);
   }

   @Test
   public void testB1InsideB2() throws Exception {

      // arrange
      BoundingBox b1 = new BoundingBox(new Point(0, 0, 0), new Point(10, 10, 10));
      BoundingBox b2 = new BoundingBox(new Point(1, 1, 1), new Point(9, 9, 9));

      // act
      BoundingBox actual = BoundingBox.Intersection(b1, b2);
      BoundingBox expected = new BoundingBox(new Point(1, 1, 1), new Point(9, 9, 9));

      // assert
      AssertEquals(actual, expected);
   }

   @Test
   public void testB2InsideB1() throws Exception {

      // arrange
      BoundingBox b2 = new BoundingBox(new Point(0, 0, 0), new Point(10, 10, 10));
      BoundingBox b1 = new BoundingBox(new Point(1, 1, 1), new Point(9, 9, 9));

      // act
      BoundingBox actual = BoundingBox.Intersection(b1, b2);
      BoundingBox expected = new BoundingBox(new Point(1, 1, 1), new Point(9, 9, 9));

      // assert
      AssertEquals(actual, expected);
   }

   @Test
   public void testCompletelyOutside() throws Exception {

      // arrange
      BoundingBox b1 = new BoundingBox(new Point(0, 0, 0), new Point(1, 1, 1));
      BoundingBox b2 = new BoundingBox(new Point(2, 2, 2), new Point(3, 3, 3));

      // act
      BoundingBox actual = BoundingBox.Intersection(b1, b2);
      BoundingBox expected = null;

      // assert
      Assert.assertNull(actual);
   }

   @Test
   public void testIntersection1() throws Exception {

      // arrange
      BoundingBox b1 = new BoundingBox(new Point(0, 0, 0), new Point(2, 2, 2));
      BoundingBox b2 = new BoundingBox(new Point(1, 0, 0), new Point(3, 2, 2));

      // act
      BoundingBox actual1 = BoundingBox.Intersection(b1, b2);
      BoundingBox actual2 = BoundingBox.Intersection(b2, b1);
      BoundingBox expected = new BoundingBox(new Point(1, 0, 0), new Point(2, 2, 2));


      // assert
      AssertEquals(actual1, expected);
      AssertEquals(actual2, expected);
   }
}
