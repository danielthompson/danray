package net.danielthompson.danray.acceleration;

import net.danielthompson.danray.acceleration.Comparators.DrawableXComparator;
import net.danielthompson.danray.acceleration.Comparators.DrawableYComparator;
import net.danielthompson.danray.acceleration.Comparators.DrawableZComparator;
import net.danielthompson.danray.shapes.Drawable;
import net.danielthompson.danray.structures.BoundingBox;
import net.danielthompson.danray.structures.Point;

import java.util.*;

/**
 * Created by daniel on 12/6/14.
 */
public class KDTree {

   private static int maxDepth;
   private static int maxLeafSize;

   /**
    * Builds a KDTree from the list of objects and returns the root node.
    * @param objects The list of objects to build into a tree.
    * @param maxDepth The number of nodes from the root to the deepest leaf, inclusive
    * @param maxLeafSize The maximum number of objects in a leaf
    * @return
    */
   public static KDNode BuildKDTree(List<Drawable> objects, int maxDepth, int maxLeafSize) {

      KDTree.maxDepth = maxDepth;
      KDTree.maxLeafSize = maxLeafSize;

      KDNode rootNode = new KDNode(objects);

      Point minPoint = null; //new Point(-Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE);
      Point maxPoint = null; //new Point(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);

      BoundingEdge[] xEdges = getMinMaxBoundingEdges(objects, KDAxis.X);
      BoundingEdge[] yEdges = getMinMaxBoundingEdges(objects, KDAxis.Y);
      BoundingEdge[] zEdges = getMinMaxBoundingEdges(objects, KDAxis.Z);

      minPoint = new Point(xEdges[0].Value, yEdges[0].Value, zEdges[0].Value);
      maxPoint = new Point(xEdges[1].Value, yEdges[1].Value, zEdges[1].Value);

      BoundingBox initialBoundingBox = new BoundingBox(minPoint, maxPoint);

      rootNode.setBoundingBox(initialBoundingBox);

      BuildKDTree(rootNode, 1);

      return rootNode;
   }

   private static void BuildKDTree(KDNode node, int depth) {

      // base case

      if (node.getObjects().size() < maxLeafSize) {
         return;
      }

      // recursive case

      else {

         KDAxis axis = node.getBoundingBox().getLargestExtent();
         node.Axis = axis;

         BoundingBox box = node.getBoundingBox();

         double separator = getSplit(node.getObjects(), axis, box);
         node.setSeparator(separator);

         List<Drawable> lessThanList = new ArrayList<Drawable>();
         List<Drawable> greaterThanOrEqualToList = new ArrayList<Drawable>();

         for (Drawable drawable : node.getObjects()) {
            BoundingBox drawableBox = drawable.GetWorldBoundingBox();
            double lowerBound = drawableBox.getLowerBoundInAxis(axis);

            double upperBound = drawableBox.getUpperBoundInAxis(axis);

            if (upperBound < separator)
               lessThanList.add(drawable);

            else if (lowerBound >= separator)
               greaterThanOrEqualToList.add(drawable);

            else {
               lessThanList.add(drawable);
               greaterThanOrEqualToList.add(drawable);
            }

         }

         if (lessThanList.size() == greaterThanOrEqualToList.size()) {

            //Collections.sort(lessThanList, null);
            //Collections.sort(greaterThanOrEqualToList, null);

            boolean same = true;

            Iterator<Drawable> p1 = lessThanList.iterator();
            Iterator<Drawable> p2 = greaterThanOrEqualToList.iterator();

            while (p1.hasNext() && p2.hasNext()) {
               Drawable d1 = p1.next();
               Drawable d2 = p2.next();

               if (d1 != d2) {
                  same = false;
                  break;
               }
            }

            if (same)
               return;
         }

         node.setLeftChild(new KDNode(lessThanList, getNextAxis(axis)));
         node.setRightChild(new KDNode(greaterThanOrEqualToList, getNextAxis(axis)));

         // create new bounding boxes for each child node

         BoundingBox[] boxes = SubdivideBoundingBox(box, axis, separator);

         BoundingBox box1 = ReduceBoundingBox(boxes[0], lessThanList);

         node.getLeftChild().setBoundingBox(box1);

         BoundingBox box2 = ReduceBoundingBox(boxes[1], greaterThanOrEqualToList);

         node.getRightChild().setBoundingBox(box2);

         if (depth > 20) {
            ;
         }

         if (depth < maxDepth) {

            if (node.getLeftChild().getObjects().size() != node.getObjects().size())
               BuildKDTree(node.getLeftChild(), depth + 1);
            if (node.getRightChild().getObjects().size() != node.getObjects().size())
               BuildKDTree(node.getRightChild(), depth + 1);
         }
      }
   }

   /**
    * Splits the existing boundingbox through the given separator in the given axis. return[0] is the lower box, return[1] is upper.
    * @param box
    * @param axis
    * @param separator
    * @return
    */
   private static BoundingBox[] SubdivideBoundingBox(BoundingBox box, KDAxis axis, double separator) {
      BoundingBox lower;
      BoundingBox upper;

      double minX = box.getLowerBoundInAxis(KDAxis.X);
      double minY = box.getLowerBoundInAxis(KDAxis.Y);
      double minZ = box.getLowerBoundInAxis(KDAxis.Z);
      double maxX = box.getUpperBoundInAxis(KDAxis.X);
      double maxY = box.getUpperBoundInAxis(KDAxis.Y);
      double maxZ = box.getUpperBoundInAxis(KDAxis.Z);

      Point lowerMinPoint = new Point(minX, minY, minZ);
      Point lowerMaxPoint = new Point(maxX, maxY, maxZ);
      lowerMaxPoint.setAxis(axis, separator);

      Point upperMinPoint = new Point(minX, minY, minZ);
      upperMinPoint.setAxis(axis, separator);
      Point upperMaxPoint = new Point(maxX, maxY, maxZ);

      lower = new BoundingBox(lowerMinPoint, lowerMaxPoint);
      upper = new BoundingBox(upperMinPoint, upperMaxPoint);

      return new BoundingBox[] {lower, upper};
   }



   public static double getSeparator(List<Drawable> objects, KDAxis axis, BoundingBox box) {
      // find largest distance in the current axis from the node's objects to either bound
      // set the

      if (objects.size() > 3) {
         return getMedian(objects, axis);
      }
      else {

         double min = Double.MAX_VALUE;
         double max = Double.MIN_VALUE;

         for (Drawable Drawable : objects) {
            double DrawableAxisLocation = Drawable.getMedian(axis);

            if (DrawableAxisLocation < min) {
               min = DrawableAxisLocation;
            }

            if (DrawableAxisLocation > max) {
               max = DrawableAxisLocation;
            }

         }

         if (box.getUpperBoundInAxis(axis) - max > min - box.getLowerBoundInAxis(axis)) {

            return max;

         }
         else {
            return min;
         }
      }
   }

   static double getMedian(List<Drawable> objects, KDAxis axis) {

      Collections.sort(objects, getComparator(axis));

      double median;

      if (objects.size() % 2 == 0) {
         Drawable Drawable1 = objects.get(objects.size() / 2 - 1);
         Drawable Drawable2 = objects.get(objects.size() / 2);
         median = (Drawable1.getMedian(axis) + Drawable2.getMedian(axis)) * .5;

      }
      else {
         median = objects.get(objects.size() / 2).getMedian(axis);
      }

      return median;

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

   private static Comparator<Drawable> getComparator(KDAxis axis) {
      switch (axis) {
         case X:
            return new DrawableXComparator();
         case Y:
            return new DrawableYComparator();
         default:
            return new DrawableZComparator();
      }
   }

   public static double getSplit(List<Drawable> objects, KDAxis axis, BoundingBox box) {
      // find largest distance in the current axis from the node's objects to either bound
      // set the

      BoundingEdge[] edges = getSortedBoundingEdges(objects, axis);

      return edges[edges.length / 2].Value;
   }

   /**
    *
    * @param objects
    * @param axis
    * @return
    */
   public static BoundingEdge[] getSortedBoundingEdges(List<Drawable> objects, KDAxis axis) {
      BoundingEdge[] edges = new BoundingEdge[objects.size() * 2];

      for (int i = 0; i < objects.size(); i++) {

         Drawable drawable = objects.get(i);
         double bound = drawable.GetWorldBoundingBox().getLowerBoundInAxis(axis);

         BoundingEdge edge = new BoundingEdge();
         edge.Drawable = drawable;
         edge.Lower = true;
         edge.Value = bound;
         edges[2 * i] = edge;

         bound = drawable.GetWorldBoundingBox().getUpperBoundInAxis(axis);

         edge = new BoundingEdge();
         edge.Drawable = drawable;
         edge.Lower = false;
         edge.Value = bound;
         edges[2 * i + 1] = edge;
      }

      Arrays.sort(edges);

      return edges;
   }


   /**
    * Returns the minimum and maximum bounding edges for the given list in the given axis.
    * @param objects
    * @param axis
    * @return
    */
   private static BoundingEdge[] getMinMaxBoundingEdges(List<Drawable> objects, KDAxis axis) {

      BoundingEdge[] edges = getSortedBoundingEdges(objects, axis);

      BoundingEdge[] minMaxEdges = new BoundingEdge[2];

      minMaxEdges[0] = edges[0];
      minMaxEdges[1] = edges[edges.length - 1];

      return minMaxEdges;

   }

   /**
    * Reduces the size of the bounding box to fit the objects, if possible
    * @param box
    * @param objects
    */
   private static BoundingBox ReduceBoundingBox(BoundingBox box, List<Drawable> objects) {

      // x
      BoundingEdge[] edges = getMinMaxBoundingEdges(objects, KDAxis.X);

      double xLowerBound = box.getLowerBoundInAxis(KDAxis.X);
      if (edges[0].Value > xLowerBound && edges[0].Lower)
         xLowerBound = edges[0].Value;

      double xUpperBound = box.getUpperBoundInAxis(KDAxis.X);
      if (edges[1].Value < xUpperBound && !edges[1].Lower)
         xUpperBound = edges[1].Value;

      // y
      edges = getMinMaxBoundingEdges(objects, KDAxis.Y);

      double yLowerBound = box.getLowerBoundInAxis(KDAxis.Y);
      if (edges[0].Value > yLowerBound && edges[0].Lower)
         yLowerBound = edges[0].Value;

      double yUpperBound = box.getUpperBoundInAxis(KDAxis.Y);
      if (edges[1].Value < yUpperBound && !edges[1].Lower)
         yUpperBound = edges[1].Value;

      // z
      edges = getMinMaxBoundingEdges(objects, KDAxis.Z);

      double zLowerBound = box.getLowerBoundInAxis(KDAxis.Z);
      if (edges[0].Value > zLowerBound && edges[0].Lower)
         zLowerBound = edges[0].Value;

      double zUpperBound = box.getUpperBoundInAxis(KDAxis.Z);
      if (edges[1].Value < zUpperBound && !edges[1].Lower)
         zUpperBound = edges[1].Value;

      Point min = new Point(xLowerBound, yLowerBound, zLowerBound);
      Point max = new Point(xUpperBound, yUpperBound, zUpperBound);

      BoundingBox newBox = new BoundingBox(min, max);

      return newBox;
   }
}
