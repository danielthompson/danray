//package test.shapes.box;
//
//import junit.framework.Assert;
//import net.danielthompson.danray.shapes.Box;
//import net.danielthompson.danray.structures.*;
//import org.testng.annotations.AfterMethod;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.DataProvider;
//import org.testng.annotations.Test;
//
//import java.util.ArrayList;
//
///**
// * Created by daniel on 2/16/15.
// */
//public class GetTransformedBoundingBoxTests {
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
//   @DataProvider(name = "TransformedBoundingBoxProvider")
//   public Object[][] TransformedBoundingBoxProvider() {
//
//      ArrayList<Object[]> list = new ArrayList<Object[]>();
//
//      Transform t;
//      Box box;
//      BoundingBox expected;
//
//      // translation
//
//      t = Transform.Translate(new Vector(1, 1, 1));
//      box = new Box(new Point(0, 0, 0), new Point(1, 1, 1), null, t, t.Invert());
//      expected = new BoundingBox(new Point(1, 1, 1), new Point(2, 2, 2));
//
//      list.add(new Object[] {box, expected});
//
//      // scaling
//
//      t = Transform.scale(1, 1, 1);
//      box = new Box(new Point(0, 0, 0), new Point(1, 1, 1), null, t, t.Invert());
//      expected = new BoundingBox(new Point(0, 0, 0), new Point(1, 1, 1));
//
//      list.add(new Object[] {box, expected});
//
//      t = Transform.scale(2, 1, 1);
//      box = new Box(new Point(0, 0, 0), new Point(1, 1, 1), null, t, t.Invert());
//      expected = new BoundingBox(new Point(0, 0, 0), new Point(2, 1, 1));
//
//      list.add(new Object[] {box, expected});
//
//      t = Transform.scale(2, 1.5, -1);
//      box = new Box(new Point(-1, -1, -1), new Point(1, 1, 1), null, t, t.Invert());
//      expected = new BoundingBox(new Point(-2, -1.5, 1), new Point(2, 1.5, -1));
//
//      list.add(new Object[] {box, expected});
//
//      // rotation
//
//      t = Transform.RotateX(90);
//      box = new Box(new Point(-1, -1, -1), new Point(1, 1, 1), null, t, t.Invert());
//      expected = new BoundingBox(new Point(-1, -1, -1), new Point(1, 1, 1));
//
//      list.add(new Object[] {box, expected});
//
//      t = Transform.RotateX(180);
//      box = new Box(new Point(-1, -1, -1), new Point(1, 1, 1), null, t, t.Invert());
//      expected = new BoundingBox(new Point(-1, -1, -1), new Point(1, 1, 1));
//
//      list.add(new Object[] {box, expected});
//
//      t = Transform.RotateX(270);
//      box = new Box(new Point(-1, -1, -1), new Point(1, 1, 1), null, t, t.Invert());
//      expected = new BoundingBox(new Point(-1, -1, -1), new Point(1, 1, 1));
//
//      list.add(new Object[] {box, expected});
//
//      t = Transform.RotateX(360);
//      box = new Box(new Point(-1, -1, -1), new Point(1, 1, 1), null, t, t.Invert());
//      expected = new BoundingBox(new Point(-1, -1, -1), new Point(1, 1, 1));
//
//      list.add(new Object[] {box, expected});
//
//      float sqrt2 = Constants.Root2;
//
//      t = Transform.RotateX(45);
//      box = new Box(new Point(-1, -1, -1), new Point(1, 1, 1), null, t, t.Invert());
//      expected = new BoundingBox(new Point(-1, -sqrt2, -sqrt2), new Point(1, sqrt2, sqrt2));
//
//      list.add(new Object[] {box, expected});
//
//      t = Transform.RotateX(135);
//      box = new Box(new Point(-1, -1, -1), new Point(1, 1, 1), null, t, t.Invert());
//      expected = new BoundingBox(new Point(-1, -sqrt2, -sqrt2), new Point(1, sqrt2, sqrt2));
//
//      list.add(new Object[] {box, expected});
//
//      // multiple rotations
//
//      t = Transform.RotateX(135).RotateX(135).RotateX(135);
//      box = new Box(new Point(-1, -1, -1), new Point(1, 1, 1), null, t, t.Invert());
//      expected = new BoundingBox(new Point(-1, -sqrt2, -sqrt2), new Point(1, sqrt2, sqrt2));
//
//      list.add(new Object[] {box, expected});
//
//      t = Transform.RotateX(45).RotateY(45);
//      box = new Box(new Point(-1, -1, -1), new Point(1, 1, 1), null, t, t.Invert());
//      expected = new BoundingBox(new Point(-sqrt2, -1, -sqrt2), new Point(sqrt2, 1, sqrt2));
//
//      list.add(new Object[] {box, expected});
//
//      int count = list.size();
//
//      Object[][] cases = new Object[count][2];
//
//      for (int i = 0; i < count; i++) {
//         cases[i] = list.get(i);
//      }
//
//      return cases;
//   }
//
//   @Test(dataProvider = "TransformedBoundingBoxProvider")
//   public void TestTransformedBoundingBox(Box box, BoundingBox expected) {
//
//      BoundingBox actual = box.GetWorldBoundingBox();
//
//      Assert.assertNotNull(actual);
//
//      Point ep1 = expected.point1;
//      Point ep2 = expected.point2;
//
//      Point ap1 = actual.point1;
//      Point ap2 = actual.point2;
//
//      Assert.assertEquals(ep1.x, ap1.x, Constants.Epsilon);
//      Assert.assertEquals(ep1.y, ap1.y, Constants.Epsilon);
//      Assert.assertEquals(ep1.z, ap1.z, Constants.Epsilon);
//      Assert.assertEquals(ep2.x, ap2.x, Constants.Epsilon);
//      Assert.assertEquals(ep2.y, ap2.y, Constants.Epsilon);
//      Assert.assertEquals(ep2.z, ap2.z, Constants.Epsilon);
//   }
//
//}
