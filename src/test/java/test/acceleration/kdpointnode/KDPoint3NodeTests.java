package test.acceleration.kdpointnode;

import net.danielthompson.danray.acceleration.KDAxis;
import net.danielthompson.danray.acceleration.KDPointNode;
import net.danielthompson.danray.structures.Point3;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 11/30/14.
 */
public class KDPoint3NodeTests {

   @BeforeMethod
   public void setUp() throws Exception {

   }

   @AfterMethod
   public void tearDown() throws Exception {

   }

   @Test
   public void testBuildNodeWithOnePoint() throws Exception {
      List<Point3> objects = new ArrayList<Point3>();

      objects.add(new Point3(1, 1, 0));

      KDAxis expectedAxis = KDAxis.X;

      KDPointNode rootNode = KDPointNode.BuildKDTree(objects);

      Assert.assertNotNull(rootNode);
      Assert.assertTrue(rootNode.isLeaf());
      Assert.assertNull(rootNode.getLeftChild());
      Assert.assertNull(rootNode.getRightChild());
   }

   @Test
   public void testBuildNodeWithTwoPoints() throws Exception {
      List<Point3> objects = new ArrayList<Point3>();

      objects.add(new Point3(1, 1, 0));
      objects.add(new Point3(2, 1, 0));

      KDAxis expectedAxis = KDAxis.X;

      KDPointNode rootNode = KDPointNode.BuildKDTree(objects);

      Assert.assertNotNull(rootNode);
      Assert.assertTrue(rootNode.isLeaf());
      Assert.assertNull(rootNode.getLeftChild());
      Assert.assertNull(rootNode.getRightChild());
   }

   @Test
   public void testBuildNodeWithThreePoints() throws Exception {
      List<Point3> objects = new ArrayList<Point3>();

      objects.add(new Point3(1, 1, 0));
      objects.add(new Point3(2, 1, 0));
      objects.add(new Point3(3, 1, 0));

      KDAxis expectedAxis = KDAxis.X;

      KDPointNode rootNode = KDPointNode.BuildKDTree(objects);

      Assert.assertNotNull(rootNode);
      Assert.assertTrue(rootNode.isLeaf());
      Assert.assertNull(rootNode.getLeftChild());
      Assert.assertNull(rootNode.getRightChild());
   }

   @Test
   public void testBuildNodeWithFourPoints() throws Exception {
      List<Point3> objects = new ArrayList<Point3>();

      objects.add(new Point3(1, 1, 0));
      objects.add(new Point3(2, 1, 0));
      objects.add(new Point3(3, 1, 0));
      objects.add(new Point3(4, 1, 0));

      KDAxis expectedAxis = KDAxis.X;

      KDPointNode rootNode = KDPointNode.BuildKDTree(objects);

      Assert.assertNotNull(rootNode, "root node should not be null");
      Assert.assertFalse(rootNode.isLeaf(), " root node should not be a leaf");
      Assert.assertNotNull(rootNode.getLeftChild());
      Assert.assertNotNull(rootNode.getRightChild());
   }

   @Test
   public void testBuildNodeWith20Points() throws Exception {
      List<Point3> objects = new ArrayList<Point3>();

      for (int i = 0; i < 20; i++) {
         objects.add(new Point3((float)(Math.random() * 10), (float)(Math.random() * 10), 0));
      }

      KDAxis expectedAxis = KDAxis.X;

      KDPointNode rootNode = KDPointNode.BuildKDTree(objects);

      Assert.assertNotNull(rootNode, "root node should not be null");
      Assert.assertFalse(rootNode.isLeaf(), " root node should not be a leaf");
      Assert.assertNotNull(rootNode.getLeftChild());
      Assert.assertNotNull(rootNode.getRightChild());
   }

   /*
   public void testBuildNodeWithOneSphere() throws Exception {
      Sphere sphere = new Sphere();
      sphere.Origin = new Point(0, 0, 0);
      sphere.Radius = 1;

      KDPointNode rootNode = KDPointNode.BuildKDTree();

   }
   */
}
