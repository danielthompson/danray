package net.danielthompson.danray.acceleration;

import net.danielthompson.danray.shapes.Drawable;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.BoundingBox;
import net.danielthompson.danray.structures.Ray;

import java.util.*;

/**
 * Created by daniel on 11/30/14.
 */
public class KDNode {

   private KDNode _leftChild;
   private KDNode _rightChild;
   private List<Drawable> _objects;
   private BoundingBox _box;

   public double Split;
   public KDAxis Axis;

   public KDNode(List<Drawable> objects, KDAxis axis) {
      _objects = objects;
      Axis = axis;
   }

   public KDNode(List<Drawable> objects) {
      _objects = objects;
   }

   public KDNode getLeftChild() {
      return _leftChild;
   }

   public BoundingBox getBoundingBox() { return _box; }

   public void setBoundingBox(BoundingBox box) { _box = box;}

   public void setLeftChild(KDNode node) {
      _leftChild = node;
   }

   public KDNode getRightChild() {
      return _rightChild;
   }

   public void setRightChild(KDNode node) {
      _rightChild = node;
   }

   public void setSeparator(double separator) {
      Split = separator;
   }

   public boolean isLeaf() {
      return _leftChild == null && _rightChild == null;
   }

   public List<Drawable> getObjects() {
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

      return "Node [" + size + "] in " + Axis.toString();
   }
}