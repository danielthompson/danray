package net.danielthompson.danray.acceleration;

import net.danielthompson.danray.shapes.Shape;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.BoundingBox;
import net.danielthompson.danray.structures.Ray;

import java.util.*;

/**
 * Created by daniel on 11/30/14.
 */
public class KDNode {

   public KDNode _leftChild;
   public KDNode _rightChild;
   public List<Shape> _objects;
   public BoundingBox _box;

   public double Split;
   public KDAxis Axis;

   public KDNode(List<Shape> objects, KDAxis axis) {
      _objects = objects;
      Axis = axis;
   }

   public KDNode(List<Shape> objects) {
      _objects = objects;
   }

   public boolean isLeaf() {
      return _leftChild == null && _rightChild == null;
   }

   public List<Shape> getObjects() {
      return _objects;
   }

   public int GetMaxDepth() {
      if (isLeaf()) {
         return 1;
      }

      else {
         int rightDepth = 1 + _rightChild.GetMaxDepth();
         int leftDepth = 1 + _leftChild.GetMaxDepth();

         return Math.max(rightDepth, leftDepth);
      }
   }

   public int GetMinDepth() {
      if (isLeaf()) {
         return 1;
      }

      else {
         int rightDepth = 1 + _rightChild.GetMinDepth();
         int leftDepth = 1 + _leftChild.GetMinDepth();

         return Math.min(rightDepth, leftDepth);
      }
   }

   public int GetCount() {
      if (isLeaf()) {
         return 1;
      }
      else {
         int rightDepth = 1 + _rightChild.GetCount();
         int leftDepth = 1 + _leftChild.GetCount();

         return rightDepth + leftDepth;
      }
   }

   public boolean IsHitBy(Ray ray) {
      return _box.Hits(ray);
   }

   public IntersectionState getHitInfo(Ray ray) {
      return _box.GetHitInfo(ray);
   }

   @Override
   public String toString() {

      String size = "0";

      if (_objects != null) {
         size = "" + _objects.size();
      }

      String axis = "";

      if (Axis != null) {
         axis = " in " + Axis.toString();
      }

      return "Node [" + size + "]" + axis;
   }
}