package test.acceleration.kdnode;

import net.danielthompson.danray.acceleration.KDAxis;
import net.danielthompson.danray.acceleration.KDNode;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by daniel on 1/18/15.
 */
public class GetMaxDepthTests {
   @Test
   public void GetMaxDepthTest1() {
      KDNode rootNode = new KDNode(null, KDAxis.X);

      int actual = rootNode.GetMaxDepth();

      int expected = 1;

      Assert.assertEquals(actual, expected);

   }

   @Test
   public void GetMaxDepthTest2() {
      KDNode rootNode = new KDNode(null, KDAxis.X);

      rootNode.LeftChild = new KDNode(null, KDAxis.Y);
      rootNode.RightChild = new KDNode(null, KDAxis.Y);

      int actual = rootNode.GetMaxDepth();

      int expected = 2;

      Assert.assertEquals(actual, expected);

   }

   @Test
   public void GetMaxDepthTest3() {
      KDNode rootNode = new KDNode(null, KDAxis.X);

      KDNode rightChild = new KDNode(null, KDAxis.Y);
      KDNode leftChild = new KDNode(null, KDAxis.Y);

      rootNode.LeftChild = rightChild;
      rootNode.RightChild = leftChild;

      KDNode rightGrandChild = new KDNode(null, KDAxis.Z);
      KDNode leftGrandChild = new KDNode(null, KDAxis.Z);

      rightChild.LeftChild = (rightGrandChild);
      rightChild.RightChild = (leftGrandChild);

      int actual = rootNode.GetMaxDepth();

      int expected = 3;

      Assert.assertEquals(actual, expected);

   }
}
