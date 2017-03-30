//package test.shapes.triangle;
//
//import net.danielthompson.danray.shapes.Triangle;
//import net.danielthompson.danray.states.IntersectionState;
//import net.danielthompson.danray.structures.Point;
//import net.danielthompson.danray.structures.Ray;
//import net.danielthompson.danray.structures.Vector;
//import org.testng.Assert;
//import org.testng.annotations.AfterMethod;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Test;
//
///**
// * DanRay
// * User: dthompson
// * Date: 7/12/13
// * Time: 5:56 PM
// */
//public class GetHitInfoTests {
//
//   @BeforeMethod
//   public void setUp() throws Exception {
//
//   }
//
//   @AfterMethod
//   public void tearDown() throws Exception {
//
//   }
//
//   @Test
//   public void testHitInfo1() throws Exception {
//
//      Point vertex0 = new Point(0, 0, 0);
//      Point vertex1 = new Point(2, 1, 0);
//      Point vertex2 = new Point(0, 2, 0);
//
//      Triangle triangle = new Triangle(vertex0, vertex1, vertex2, null);
//
//      Point rayOrigin = new Point(1, 1, 1);
//      Vector rayDirection = new Vector(0, 0, -1);
//
//      Ray incomingRay = new Ray(rayOrigin, rayDirection);
//
//      IntersectionState state = triangle.getHitInfo(incomingRay);
//
//      Assert.assertTrue(state.hits);
//
//      Point expectedIntersection = new Point(1, 1, 0);
//
//      Assert.assertEquals(state.IntersectionPoint, expectedIntersection);
//   }
//
//   @Test
//   public void testHitInfo2() throws Exception {
//
//      Point vertex0 = new Point(0, 0, 0);
//      Point vertex1 = new Point(2, 1, 0);
//      Point vertex2 = new Point(0, 2, 0);
//
//      Triangle triangle = new Triangle(vertex0, vertex1, vertex2, null);
//
//      Point rayOrigin = new Point(2, 2, 2);
//      Vector rayDirection = new Vector(-1, -1, -1);
//
//      Ray incomingRay = new Ray(rayOrigin, rayDirection);
//
//      IntersectionState state = triangle.getHitInfo(incomingRay);
//
//      Assert.assertTrue(state.hits);
//
//      Point expectedIntersection = new Point(0, 0, 0);
//
//      Assert.assertEquals(state.IntersectionPoint, expectedIntersection);
//   }
//
//   @Test
//   public void testHitInfo3() throws Exception {
//
//      Point vertex0 = new Point(0, 0, 0);
//      Point vertex1 = new Point(2, 1, 0);
//      Point vertex2 = new Point(0, 2, 0);
//
//      Triangle triangle = new Triangle(vertex0, vertex1, vertex2, null);
//
//      Point rayOrigin = new Point(3, 3, 2);
//      Vector rayDirection = new Vector(-1, -1, -1);
//
//      Ray incomingRay = new Ray(rayOrigin, rayDirection);
//
//      IntersectionState state = triangle.getHitInfo(incomingRay);
//
//      Assert.assertTrue(state.hits);
//
//      Point expectedIntersection = new Point(1, 1, 0);
//
//      Assert.assertEquals(state.IntersectionPoint, expectedIntersection);
//   }
//
//   @Test
//   public void testHitInfoNoHit1() throws Exception {
//
//      Point vertex0 = new Point(0, 0, 0);
//      Point vertex1 = new Point(2, 1, 0);
//      Point vertex2 = new Point(0, 2, 0);
//
//      Triangle triangle = new Triangle(vertex0, vertex1, vertex2, null);
//
//      Point rayOrigin = new Point(3, 3, 2);
//      Vector rayDirection = new Vector(1, 1, 1);
//
//      Ray incomingRay = new Ray(rayOrigin, rayDirection);
//
//      IntersectionState state = triangle.getHitInfo(incomingRay);
//
//      Assert.assertFalse(state.hits);
//   }
//}
