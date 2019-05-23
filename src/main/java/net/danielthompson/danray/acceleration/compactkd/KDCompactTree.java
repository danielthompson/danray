package net.danielthompson.danray.acceleration.compactkd;

import net.danielthompson.danray.acceleration.KDNode;
import net.danielthompson.danray.acceleration.KDTree;
import net.danielthompson.danray.shapes.AbstractShape;

import java.util.*;

/**
 * Created by daniel on 12/6/14.
 */
public class KDCompactTree {

   /**
    * Builds a KDTree from the list of objects and returns the root node.
    * @param objects The list of objects to build into a tree.
    * @param maxDepth The number of nodes from the root to the deepest leaf, inclusive
    * @param maxLeafSize The maximum number of objects in a leaf
    * @return
    */
   public static KDCompactNode[] BuildKDTree(List<AbstractShape> objects, int maxDepth, int maxLeafSize, int numThreads) {

      KDNode rootNode = KDTree.BuildKDTree(objects, maxDepth, maxLeafSize, numThreads);

      int depth = rootNode.GetMaxDepth();

      int size = (int) Math.pow(2, depth);

      KDCompactNode[] tree = new KDCompactNode[size];

      //KDCompactNode newNode = GetKDCompactNode(rootNode);
      //tree[0] = newNode;

      PutNode(tree, rootNode, 0);

      return tree;
   }

   public static void PutNode(KDCompactNode[] tree, KDNode oldNode, int index) {

      tree[index] = GetKDCompactNode(oldNode);

      // base case
      if (oldNode.isLeaf()) {
         // done
         ;
      }
      // recursive case
      else {
         PutNode(tree, oldNode.LeftChild, index * 2 + 1);
         PutNode(tree, oldNode.RightChild, index * 2 + 2);
      }
   }

   public static KDCompactNode GetKDCompactNode(KDNode node) {
      KDCompactNode newNode = new KDCompactNode(node.Shapes, node.Axis);

      newNode.Split = node.Split;
      newNode.p0x = (float) node.BoundingBox.point1.x;
      newNode.p0y = (float) node.BoundingBox.point1.y;
      newNode.p0z = (float) node.BoundingBox.point1.z;
      newNode.p1x = (float) node.BoundingBox.point2.x;
      newNode.p1y = (float) node.BoundingBox.point2.y;
      newNode.p1z = (float) node.BoundingBox.point2.z;

      return newNode;

   }
}