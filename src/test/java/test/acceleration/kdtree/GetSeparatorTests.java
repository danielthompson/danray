//package test.acceleration.kdtree;
//
//import net.danielthompson.danray.acceleration.KDAxis;
//import net.danielthompson.danray.acceleration.KDTree;
//import net.danielthompson.danray.shapes.Shape;
//import net.danielthompson.danray.shapes.Sphere;
//import net.danielthompson.danray.structures.BoundingBox;
//import net.danielthompson.danray.structures.Point;
//import org.testng.Assert;
//import org.testng.annotations.AfterMethod;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Test;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by daniel on 12/13/14.
// */
//public class GetSeparatorTests {
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
//   public void testGetSeparatorWithTwoCloseSpheres() throws Exception {
//
//      List<Shape> list = new ArrayList<Shape>();
//
//      Sphere sphere = new Sphere();
//      sphere.Origin = new Point(1, 1, 0);
//      sphere.Radius = 1;
//
//      list.add(sphere);
//
//      Sphere sphere2 = new Sphere();
//      sphere2.Origin = new Point(2, 4, 0);
//      sphere2.Radius = 1;
//
//      list.add(sphere2);
//
//      BoundingBox box = BoundingBox.GetBoundingBox(sphere.GetWorldBoundingBox(), sphere2.GetWorldBoundingBox());
//
//      float actualResult = KDTree.getSeparator(list, KDAxis.X, box);
//
//      Assert.assertEquals(actualResult, 1.0);
//
//   }
//
//   @Test
//   public void testGetSeparatorWithTwoFarSpheres() throws Exception {
//
//      List<Shape> list = new ArrayList<Shape>();
//
//      Sphere sphere = new Sphere();
//      sphere.Origin = new Point(1, 1, 0);
//      sphere.Radius = 1;
//      sphere.RecalculateWorldBoundingBox();
//
//      list.add(sphere);
//
//      Sphere sphere2 = new Sphere();
//      sphere2.Origin = new Point(4, 4, 0);
//      sphere2.Radius = 1;
//      sphere2.RecalculateWorldBoundingBox();
//
//      list.add(sphere2);
//
//      BoundingBox box = BoundingBox.GetBoundingBox(sphere.GetWorldBoundingBox(), sphere2.GetWorldBoundingBox());
//
//      float actualResult = KDTree.getSeparator(list, KDAxis.X, box);
//
//      Assert.assertEquals(actualResult, 2.0);
//
//   }
//}
