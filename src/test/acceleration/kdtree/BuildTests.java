package test.acceleration.kdtree;

import net.danielthompson.danray.acceleration.KDNode;
import net.danielthompson.danray.acceleration.KDTree;
import net.danielthompson.danray.shapes.Shape;
import net.danielthompson.danray.shapes.Sphere;
import net.danielthompson.danray.structures.Point;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 11/30/14.
 */
public class BuildTests {

   @BeforeMethod
   public void setUp() throws Exception {

   }

   @AfterMethod
   public void tearDown() throws Exception {

   }

   @Test
   public void testBuildNodeWithOneSphere() throws Exception {
      List<Shape> objects = new ArrayList<Shape>();

      Sphere sphere;

      for (int i = 0; i < 1; i++) {
         sphere = new Sphere();
         sphere.Origin = new Point(Math.random() * 100, Math.random() * 100, Math.random() * 100);
         sphere.Radius = 1;

         objects.add(sphere);
      }

      KDNode rootNode = KDTree.BuildKDTree(objects, 20, 4);

      Assert.assertNotNull(rootNode);
      Assert.assertTrue(rootNode.isLeaf());
      Assert.assertNull(rootNode._leftChild);
      Assert.assertNull(rootNode._rightChild);
   }

   @Test
   public void testBuildNodeWithTwoSpheres() throws Exception {
      List<Shape> objects = new ArrayList<Shape>();

      Sphere sphere;

      for (int i = 0; i < 2; i++) {
         sphere = new Sphere();
         sphere.Origin = new Point(Math.random() * 100, Math.random() * 100, Math.random() * 100);
         sphere.Radius = 1;

         objects.add(sphere);
      }

      KDNode rootNode = KDTree.BuildKDTree(objects, 20, 4);

      Assert.assertNotNull(rootNode);
      Assert.assertTrue(rootNode.isLeaf());
      Assert.assertNull(rootNode._leftChild);
      Assert.assertNull(rootNode._rightChild);
   }

   @Test
   public void testBuildNodeWithThreeSpheres() throws Exception {
      List<Shape> objects = new ArrayList<Shape>();

      Sphere sphere;

      for (int i = 0; i < 3; i++) {
         sphere = new Sphere();
         sphere.Origin = new Point(Math.random() * 100, Math.random() * 100, Math.random() * 100);
         sphere.Radius = 1;

         objects.add(sphere);
      }

      KDNode rootNode = KDTree.BuildKDTree(objects, 20, 4);

      Assert.assertNotNull(rootNode);
      Assert.assertTrue(rootNode.isLeaf());
      Assert.assertNull(rootNode._leftChild);
      Assert.assertNull(rootNode._rightChild);
   }

   @Test
   public void testBuildNodeWithFiveSpheres() throws Exception {
      List<Shape> objects = new ArrayList<Shape>();

      Sphere sphere;

      for (int i = 0; i < 5; i++) {
         sphere = new Sphere();
         sphere.Origin = new Point(Math.random() * 100, Math.random() * 100, Math.random() * 100);
         sphere.Radius = 1;

         objects.add(sphere);
      }

      KDNode rootNode = KDTree.BuildKDTree(objects, 20, 4);

      Assert.assertNotNull(rootNode, "root should not be null");
      Assert.assertFalse(rootNode.isLeaf(), "root should not be leaf with 5 elements");
      Assert.assertNotNull(rootNode._leftChild, "left child should not be null with 5 elements");
      Assert.assertNotNull(rootNode._rightChild, "right child should not be null with 5 elements");
   }

   @Test
   public void testBuildNodeWithFiftySpheres() throws Exception {
      List<Shape> objects = new ArrayList<Shape>();

      Sphere sphere;

      for (int i = 0; i < 50; i++) {
         sphere = new Sphere();
         sphere.Origin = new Point(Math.random() * 100, Math.random() * 100, Math.random() * 100);
         sphere.Radius = 1;

         objects.add(sphere);
      }

      KDNode rootNode = KDTree.BuildKDTree(objects, 20, 4);

      Assert.assertNotNull(rootNode, "root should not be null");
      Assert.assertFalse(rootNode.isLeaf(), "root should not be leaf with 50 elements");
      Assert.assertNotNull(rootNode._leftChild, "left child should not be null with 50 elements");
      Assert.assertNotNull(rootNode._rightChild, "right child should not be null with 50 elements");
   }

   @Test
   public void testBuildNodeWith500Spheres() throws Exception {
      List<Shape> objects = new ArrayList<Shape>();

      Sphere sphere;

      for (int i = 0; i < 500; i++) {
         sphere = new Sphere();
         sphere.Origin = new Point(Math.random() * 100, Math.random() * 100, Math.random() * 100);
         sphere.Radius = 1;

         objects.add(sphere);
      }

      KDNode rootNode = KDTree.BuildKDTree(objects, 20, 4);

      Assert.assertNotNull(rootNode, "root should not be null");
      Assert.assertFalse(rootNode.isLeaf(), "root should not be leaf with 500 elements");
      Assert.assertNotNull(rootNode._leftChild, "left child should not be null with 500 elements");
      Assert.assertNotNull(rootNode._rightChild, "right child should not be null with 500 elements");
   }

   @Test
   public void testBuildNodeWith5000Spheres() throws Exception {
      List<Shape> objects = new ArrayList<Shape>();

      Sphere sphere;

      for (int i = 0; i < 5000; i++) {
         sphere = new Sphere();
         sphere.Origin = new Point(Math.random() * 100, Math.random() * 100, Math.random() * 100);
         sphere.Radius = 1;

         objects.add(sphere);
      }

      KDNode rootNode = KDTree.BuildKDTree(objects, 20, 4);

      Assert.assertNotNull(rootNode, "root should not be null");
      Assert.assertFalse(rootNode.isLeaf(), "root should not be leaf with 5000 elements");
      Assert.assertNotNull(rootNode._leftChild, "left child should not be null with 5000 elements");
      Assert.assertNotNull(rootNode._rightChild, "right child should not be null with 5000 elements");

      int depth = rootNode.GetMaxDepth();

      Assert.assertTrue(depth >= 12, "depth should be at least log2(5000) = 12.28");
      Assert.assertTrue(depth <= 20, "depth shouldn't really be more than 15 or so");
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
