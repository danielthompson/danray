package net.danielthompson.danray.acceleration;

import net.danielthompson.danray.acceleration.Comparators.DrawableXComparator;
import net.danielthompson.danray.acceleration.Comparators.DrawableYComparator;
import net.danielthompson.danray.acceleration.Comparators.DrawableZComparator;
import net.danielthompson.danray.shapes.Shape;
import net.danielthompson.danray.structures.BoundingBox;
import net.danielthompson.danray.structures.Point;

import java.util.*;

/**
 * Created by daniel on 12/6/14.
 */
public class KDTree {

   private static int maxDepth;
   private static int maxLeafSize;

   private static BoundingEdge[] xEdges;
   private static BoundingEdge[] yEdges;
   private static BoundingEdge[] zEdges;

   /**
    * Builds a KDTree from the list of objects and returns the root node.
    * @param objects The list of objects to build into a tree.
    * @param maxDepth The number of nodes from the root to the deepest leaf, inclusive
    * @param maxLeafSize The maximum number of objects in a leaf
    * @return
    */
   public static KDNode BuildKDTree(List<Shape> objects, int maxDepth, int maxLeafSize) {

      KDTree.maxDepth = maxDepth;
      KDTree.maxLeafSize = maxLeafSize;

      KDNode rootNode = new KDNode(objects);

      Point minPoint = null; //new Point(-Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE);
      Point maxPoint = null; //new Point(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE);

      xEdges = getSortedBoundingEdges(objects, KDAxis.X);
      yEdges = getSortedBoundingEdges(objects, KDAxis.Y);
      zEdges = getSortedBoundingEdges(objects, KDAxis.Z);

      minPoint = new Point(xEdges[0].Value, yEdges[0].Value, zEdges[0].Value);
      maxPoint = new Point(xEdges[xEdges.length - 1].Value, yEdges[yEdges.length - 1].Value, zEdges[zEdges.length - 1].Value);

      BoundingBox initialBoundingBox = new BoundingBox(minPoint, maxPoint);

      rootNode._box = (initialBoundingBox);

      BuildKDTreeSAH(rootNode, 1);

      return rootNode;
   }

   private static class KDAxisHeuristicImproved {
      public KDAxis Axis;
      public double lessThanHeuristic;
      public double greaterThanHeuristic;
      public double SurfaceAreaHeuristic;
      public double Separator;
   }

   private static BoundingEdge[] getEdgesWithObjectsForAxis(List<Shape> objects, KDAxis axis) {
      BoundingEdge[] source = xEdges;

      if (axis == KDAxis.Y)
         source = yEdges;
      else if (axis == KDAxis.Z)
         source = zEdges;

      BoundingEdge[] dest = new BoundingEdge[2 * objects.size()];

      int destIndex = 0;

      for (int sourceIndex = 0; sourceIndex < source.length; sourceIndex++) {
         Shape sourceShape = source[sourceIndex].Shape;
         for (Shape object : objects) {
            if (object.equals(sourceShape)) {
               dest[destIndex++] = source[sourceIndex];
               break;
            }
         }
      }

      return dest;
   }

   private static void BuildKDTreeSAH(KDNode node, int depth) {

      // base case

      if (node.getObjects().size() <= maxLeafSize) {
         //node.Axis = null;
         return;
      }

      // recursive case

      else {
         List<KDAxisHeuristicImproved> heuristicList = new ArrayList<>();
         boolean foundGoodSplit = false;
         List<KDAxis> axes = new ArrayList<>();
         axes.add(node._box.getLargestExtent());
         axes.add(getNextAxis(axes.get(0)));
         axes.add(getNextAxis(axes.get(1)));

         for (KDAxis axis : axes) {
            //BoundingEdge[] splits = getSortedBoundingEdges(node.getObjects(), axis);
            BoundingEdge[] splits = getEdgesWithObjectsForAxis(node.getObjects(), axis);
            for (int i = 0; i < splits.length; i++) {
               double split = splits[i].Value;

               BoundingBox lessThanBoundingBox = null;
               BoundingBox greaterThanBoundingBox = null;

               List<Shape> lessThanList = new ArrayList<>();
               List<Shape> greaterThanList = new ArrayList<>();

               for (Shape shape : node.getObjects()) {
                  BoundingBox drawableBox = shape.GetWorldBoundingBox();
                  double lowerBound = drawableBox.getLowerBoundInAxis(axis);
                  double upperBound = drawableBox.getUpperBoundInAxis(axis);

                  if (upperBound <= split) {
                     if (lessThanBoundingBox == null)
                        lessThanBoundingBox = drawableBox;
                     else
                        lessThanBoundingBox = BoundingBox.GetBoundingBox(lessThanBoundingBox, drawableBox);
                     lessThanList.add(shape);
                  }

                  else if (lowerBound >= split) {
                     if (greaterThanBoundingBox == null)
                        greaterThanBoundingBox = drawableBox;
                     else
                        greaterThanBoundingBox = BoundingBox.GetBoundingBox(greaterThanBoundingBox, drawableBox);
                     greaterThanList.add(shape);
                  }
                  else {
                     if (lessThanBoundingBox == null)
                        lessThanBoundingBox = drawableBox;
                     else
                        lessThanBoundingBox = BoundingBox.GetBoundingBox(lessThanBoundingBox, drawableBox);

                     if (greaterThanBoundingBox == null)
                        greaterThanBoundingBox = drawableBox;
                     else
                        greaterThanBoundingBox = BoundingBox.GetBoundingBox(greaterThanBoundingBox, drawableBox);

                     lessThanList.add(shape);
                     greaterThanList.add(shape);
                  }

               }

               if (lessThanList.size() == 0 || greaterThanList.size() == 0)
                  continue;

               if (lessThanList.size() >= node.getObjects().size() || greaterThanList.size() >= node.getObjects().size())
                  continue;

               if (lessThanList.size() == greaterThanList.size()) {

                  boolean same = true;

                  Iterator<Shape> p1 = lessThanList.iterator();
                  Iterator<Shape> p2 = greaterThanList.iterator();

                  while (p1.hasNext() && p2.hasNext()) {
                     Shape d1 = p1.next();
                     Shape d2 = p2.next();

                     if (d1 != d2) {
                        same = false;
                        break;
                     }
                  }

                  if (same)
                     continue;
               }

               foundGoodSplit = true;

               ReduceBoundingBox(lessThanBoundingBox, lessThanList);
               ReduceBoundingBox(greaterThanBoundingBox, greaterThanList);

               KDAxisHeuristicImproved heuristic = new KDAxisHeuristicImproved();

               heuristic.lessThanHeuristic = lessThanBoundingBox.getSurfaceArea() * lessThanList.size();
               heuristic.greaterThanHeuristic = greaterThanBoundingBox.getSurfaceArea() * greaterThanList.size();
               heuristic.Axis = axis;
               heuristic.Separator = split;
               heuristic.SurfaceAreaHeuristic = heuristic.lessThanHeuristic / heuristic.greaterThanHeuristic;
               if (heuristic.SurfaceAreaHeuristic < 1) {
                  heuristic.SurfaceAreaHeuristic = 1 / heuristic.SurfaceAreaHeuristic;


               }
               heuristicList.add(heuristic);

            }

            if (foundGoodSplit)
               break;

         }

         if (foundGoodSplit && heuristicList.size() > 0) {

            KDAxisHeuristicImproved best = heuristicList.get(0);

            for (KDAxisHeuristicImproved heuristic : heuristicList) {
               if (heuristic.SurfaceAreaHeuristic < best.SurfaceAreaHeuristic)
                  best = heuristic;
            }

            List<Shape> lessThanList = new ArrayList<>();
            List<Shape> greaterThanList = new ArrayList<>();

            for (Shape shape : node.getObjects()) {
               BoundingBox drawableBox = shape.GetWorldBoundingBox();
               double lowerBound = drawableBox.getLowerBoundInAxis(best.Axis);
               double upperBound = drawableBox.getUpperBoundInAxis(best.Axis);

               if (upperBound > best.Separator)
                  greaterThanList.add(shape);

               if (lowerBound <= best.Separator)
                  lessThanList.add(shape);

               /*if (upperBound <= best.Separator) {
                  lessThanList.add(shape);
               }

               else if (lowerBound >= best.Separator) {
                  greaterThanList.add(shape);
               }
               else {
                  lessThanList.add(shape);
                  greaterThanList.add(shape);
               }*/

            }

            BoundingBox[] boxes = SubdivideBoundingBox(node._box, best.Axis, best.Separator);

            ReduceBoundingBox(boxes[0], lessThanList);
            ReduceBoundingBox(boxes[1], greaterThanList);

            KDNode leftNode = new KDNode(lessThanList, getNextAxis(best.Axis));
            leftNode._box = (boxes[0]);

            KDNode rightNode = new KDNode(greaterThanList, getNextAxis(best.Axis));
            rightNode._box = (boxes[1]);

            node._leftChild = leftNode;
            node._rightChild = rightNode;
            node.Split = (best.Separator);
            node.Axis = best.Axis;

            /*
            // create new bounding boxes for each child node

            BoundingBox[] boxes = SubdivideBoundingBox(box, axis, separator);

            BoundingBox box1 = ReduceBoundingBox(boxes[0], lessThanList);

            node.getLeftChild().setBoundingBox(box1);

            BoundingBox box2 = ReduceBoundingBox(boxes[1], greaterThanList);

            node.getRightChild().setBoundingBox(box2);
            */
            if (depth > 20) {
               ;
            }

            if (depth < maxDepth) {

               if (leftNode.getObjects().size() != node.getObjects().size())
                  BuildKDTreeSAH(leftNode, depth + 1);
               if (rightNode.getObjects().size() != node.getObjects().size())
                  BuildKDTreeSAH(rightNode, depth + 1);
            }
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



   public static double getSeparator(List<Shape> objects, KDAxis axis, BoundingBox box) {
      // find largest distance in the current axis from the node's objects to either bound
      // set the

      if (objects.size() > 3) {
         return getMedian(objects, axis);
      }
      else {

         double min = Double.MAX_VALUE;
         double max = Double.MIN_VALUE;

         for (Shape Shape : objects) {
            double DrawableAxisLocation = Shape.getMedian(axis);

            if (DrawableAxisLocation < min) {
               min = DrawableAxisLocation;
            }

            if (DrawableAxisLocation > max) {
               max = DrawableAxisLocation;
            }

         }

         if (box.getUpperBoundInAxis(axis) - max >= min - box.getLowerBoundInAxis(axis)) {

            return max;

         }
         else {
            return min;
         }
      }
   }

   static double getMedian(List<Shape> objects, KDAxis axis) {

      Collections.sort(objects, getComparator(axis));

      double median;

      if (objects.size() % 2 == 0) {
         Shape shape1 = objects.get(objects.size() / 2 - 1);
         Shape shape2 = objects.get(objects.size() / 2);
         median = (shape1.getMedian(axis) + shape2.getMedian(axis)) * .5;

      }
      else {
         median = objects.get((objects.size() / 2) + 1).getMedian(axis);
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

   private static Comparator<Shape> getComparator(KDAxis axis) {
      switch (axis) {
         case X:
            return new DrawableXComparator();
         case Y:
            return new DrawableYComparator();
         default:
            return new DrawableZComparator();
      }
   }
//
//   public static double getSplit(List<Shape> objects, KDAxis axis, BoundingBox box) {
//      // find largest distance in the current axis from the node's objects to either bound
//      // set the
//
//      BoundingEdge[] edges = getSortedBoundingEdges(objects, axis);
//
//      return edges[edges.length / 2].Value;
//   }

   /**
    *
    * @param objects
    * @param axis
    * @return
    */
   public static BoundingEdge[] getSortedBoundingEdges(List<Shape> objects, KDAxis axis) {
      BoundingEdge[] edges = new BoundingEdge[objects.size() * 2];

      BoundingEdge[] current;

      for (int i = 0; i < objects.size(); i++) {

         Shape shape = objects.get(i);
         double bound = shape.GetWorldBoundingBox().getLowerBoundInAxis(axis);

         BoundingEdge edgeLower = new BoundingEdge();
         edgeLower.Shape = shape;
         edgeLower.Lower = true;
         edgeLower.Value = bound;
         edges[2 * i] = edgeLower;

         current = new BoundingEdge[2];
         current[0] = edgeLower;

         bound = shape.GetWorldBoundingBox().getUpperBoundInAxis(axis);

         BoundingEdge edgeUpper = new BoundingEdge();
         edgeUpper.Shape = shape;
         edgeUpper.Lower = false;
         edgeUpper.Value = bound;
         edges[2 * i + 1] = edgeUpper;
         current[1] = edgeUpper;

         shape.SetBoundingEdges(current, axis);
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
   private static BoundingEdge[] getMinMaxBoundingEdges(List<Shape> objects, KDAxis axis) {

      BoundingEdge[] source = xEdges;

      if (axis == KDAxis.Y)
         source = yEdges;
      else if (axis == KDAxis.Z)
         source = zEdges;

      BoundingEdge[] minMaxEdges = new BoundingEdge[2];

      minMaxEdges[0] = source[0];
      minMaxEdges[1] = source[0];

      for (int i = 0; i < objects.size(); i++) {

         BoundingEdge[] edges = objects.get(i).GetBoundingEdges(axis);

         if (minMaxEdges[0].compareTo(edges[0]) > 0)
            minMaxEdges[0] = edges[0];

         if (minMaxEdges[1].compareTo(edges[1]) < 0)
            minMaxEdges[1] = edges[1];
      }

      return minMaxEdges;
   }



   /**
    * Reduces the size of the bounding box to fit the objects, if possible
    * @param box
    * @param objects
    */
   private static void ReduceBoundingBox(BoundingBox box, List<Shape> objects) {

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

      box.point1.X = xLowerBound;
      box.point1.Y = yLowerBound;
      box.point1.Z = zLowerBound;

      box.point2.X = xUpperBound;
      box.point2.Y = yUpperBound;
      box.point2.Z = zUpperBound;
   }
}
