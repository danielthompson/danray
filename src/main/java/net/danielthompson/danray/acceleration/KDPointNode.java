package net.danielthompson.danray.acceleration;

import net.danielthompson.danray.acceleration.Comparators.PointXComparator;
import net.danielthompson.danray.acceleration.Comparators.PointYComparator;
import net.danielthompson.danray.acceleration.Comparators.PointZComparator;
import net.danielthompson.danray.structures.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by daniel on 11/30/14.
 */
public class KDPointNode {

   private KDPointNode _leftChild;
   private KDPointNode _rightChild;
   private List<Point> _objects;
   private double _separator;
   private KDAxis _axis;


   public KDPointNode(List<Point> objects, KDAxis axis) {
      _objects = objects;
      _axis = axis;
   }

   public KDAxis getAxis() {
      return _axis;
   }

   public KDPointNode getLeftChild() {
      return _leftChild;
   }

   public void setLeftChild(KDPointNode node) {
      _leftChild = node;
   }

   public KDPointNode getRightChild() {
      return _rightChild;
   }

   public void setRightChild(KDPointNode node) {
      _rightChild = node;
   }

   public void setSeparator(double separator) {
      _separator = separator;
   }

   public boolean isLeaf() {
      return _leftChild == null && _rightChild == null;
   }

   public List<Point> getObjects() {
      return _objects;
   }

   public static KDPointNode BuildKDTree(List<Point> objects) {

      KDPointNode rootNode = new KDPointNode(objects, KDAxis.X);

      BuildKDTree(rootNode, Double.MIN_VALUE, Double.MAX_VALUE, 0);

      return rootNode;
   }



   private static void BuildKDTree(KDPointNode node, double lowerBound, double upperBound, int depth) {

      // recursive case

      if (node.getObjects().size() > 3) {

         double separator = getSeparator(node.getObjects(), node.getAxis(), lowerBound, upperBound);
         node.setSeparator(separator);

         List<Point> lessThanList = new ArrayList<Point>();
         List<Point> greaterThanOrEqualToList = new ArrayList<Point>();

         for (Point point : node.getObjects()) {
            if (point.getAxis(node.getAxis()) >= separator) {
               greaterThanOrEqualToList.add(point);
            }

            if (point.getAxis(node.getAxis()) < separator) {
               lessThanList.add(point);
            }
         }

         node.setLeftChild(new KDPointNode(lessThanList, getNextAxis(node.getAxis())));
         node.setRightChild(new KDPointNode(greaterThanOrEqualToList, getNextAxis(node.getAxis())));

         if (depth > 20) {
            ;
         }

         BuildKDTree(node.getLeftChild(), lowerBound, separator, depth + 1);
         BuildKDTree(node.getRightChild(), separator, upperBound, depth + 1);
      }

      // base case

      else {

      }
   }



   private static double getSeparator(List<Point> objects, KDAxis axis, double lowerBound, double upperBound) {
      // find largest distance in the current axis from the node's objects to either bound
      // set the

      if (objects.size() > 3) {
         return getMedian(objects, axis);
      }
      else {

         double min = Double.MAX_VALUE;
         double max = Double.MIN_VALUE;

         for (Point point : objects) {
            double pointAxisLocation = point.getAxis(axis);

            if (pointAxisLocation < min) {
               min = pointAxisLocation;
            }

            if (pointAxisLocation > max) {
               max = pointAxisLocation;
            }

         }

         if (upperBound - max > min - lowerBound) {

            return max;

         }
         else {
            return min;
         }
      }

   }

   private static double getMedian(List<Point> objects, KDAxis axis) {

      Collections.sort(objects, getComparator(axis));

      double median;

      if (objects.size() % 2 == 0) {
         Point point1 = objects.get(objects.size() / 2 - 1);
         Point point2 = objects.get(objects.size() / 2);
         median = (point1.getAxis(axis) + point2.getAxis(axis)) * .5;

      }
      else {
         median = objects.get(objects.size() / 2).getAxis(axis);
      }

      return median;

   }

   private static Comparator<Point> getComparator(KDAxis axis) {
      switch (axis) {
         case X:
            return new PointXComparator();
         case Y:
            return new PointYComparator();
         default:
            return new PointZComparator();
      }
   }

   private static KDAxis getNextAxis(KDAxis axis) {
      switch (axis) {
         case X:
            return KDAxis.Y;
         case Y:
            return KDAxis.Z;
         default:
            return KDAxis.X;
      }
   }


}
