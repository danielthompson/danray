package net.danielthompson.danray.acceleration;

import net.danielthompson.danray.shapes.AbstractShape;

import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.BoundingBox;
import net.danielthompson.danray.structures.Ray;

import java.util.*;

/**
 * Created by daniel on 11/30/14.
 */
public class KDNode {

   public KDNode LeftChild;
   public KDNode RightChild;
   public List<AbstractShape> Shapes;
   public BoundingBox BoundingBox;

   public double Split;
   public KDAxis Axis;

   public KDNode(List<AbstractShape> shapes, KDAxis axis) {
      Shapes = shapes;
      Axis = axis;
   }



   public KDNode(List<AbstractShape> objects) {
      Shapes = objects;
   }

   public boolean isLeaf() {
      return LeftChild == null && RightChild == null;
   }

   public int GetMaxDepth() {
      if (isLeaf()) {
         return 1;
      }

      else {
         int rightDepth = 1 + RightChild.GetMaxDepth();
         int leftDepth = 1 + LeftChild.GetMaxDepth();

         return Math.max(rightDepth, leftDepth);
      }
   }

   public int GetMinDepth() {
      if (isLeaf()) {
         return 1;
      }

      else {
         int rightDepth = 1 + RightChild.GetMinDepth();
         int leftDepth = 1 + LeftChild.GetMinDepth();

         return Math.min(rightDepth, leftDepth);
      }
   }

   public int GetCount() {
      if (isLeaf()) {
         return 1;
      }
      else {
         int rightDepth = 1 + RightChild.GetCount();
         int leftDepth = 1 + LeftChild.GetCount();

         return rightDepth + leftDepth;
      }
   }

   public boolean IsHitBy(Ray ray) {
      return BoundingBox.Hits(ray);
   }

   public IntersectionState getHitInfo(Ray ray) {
      //return BoundingBox.GetHitInfo(ray);
      return BoundingBox.GetHitInfoNew(BoundingBox.point1, BoundingBox.point2, ray);
   }

   @Override
   public String toString() {

      String size = "0";

      if (Shapes != null) {
         size = "" + Shapes.size();
      }

      String axis = "";

      if (Axis != null) {
         axis = " in " + Axis.toString();
      }

      return "Node [" + size + "]" + axis;
   }
}