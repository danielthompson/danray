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

      BoundingEdge[] xEdges = getMinMaxBoundingEdges(objects, KDAxis.X);
      BoundingEdge[] yEdges = getMinMaxBoundingEdges(objects, KDAxis.Y);
      BoundingEdge[] zEdges = getMinMaxBoundingEdges(objects, KDAxis.Z);

      minPoint = new Point(xEdges[0].Value, yEdges[0].Value, zEdges[0].Value);
      maxPoint = new Point(xEdges[1].Value, yEdges[1].Value, zEdges[1].Value);

      BoundingBox initialBoundingBox = new BoundingBox(minPoint, maxPoint);

      rootNode.setBoundingBox(initialBoundingBox);

      BuildKDTreeSAH(rootNode, 1);

      return rootNode;
   }

   private static class KDAxisHeuristic {
      public KDAxis Axis;
      public List<Shape> LessThanObjects;
      public List<Shape> GreaterThanObjects;

      public BoundingBox LessThanBoundingBox;
      public BoundingBox GreaterThanBoundingBox;

      public double SurfaceAreaHeuristic;
      public double Separator;
   }

   private static void BuildKDTree(KDNode node, int depth) {

      // base case

      if (node.getObjects().size() <= maxLeafSize) {
         //node.Axis = null;
         return;
      }

      // recursive case

      else {

         KDAxisHeuristic[] heuristics = new KDAxisHeuristic[3];

         heuristics[0] = new KDAxisHeuristic();
         heuristics[0].Axis = node.getBoundingBox().getLargestExtent();

         heuristics[1] = new KDAxisHeuristic();
         heuristics[1].Axis = getNextAxis(heuristics[0].Axis);

         heuristics[2] = new KDAxisHeuristic();
         heuristics[2].Axis = getNextAxis(heuristics[1].Axis);

         /*

         KDAxis[] axes = new KDAxis[3];
         axes[0] = node.getBoundingBox().getLargestExtent();
         axes[1] = getNextAxis(axes[0]);
         axes[2] = getNextAxis(axes[1]);

         double[] surfaceAreaHeuristic = new double[3];

         boolean foundGoodSplit = false;

         List<Drawable> lessThanList = new ArrayList<Drawable>();
         List<Drawable> greaterThanList = new ArrayList<Drawable>();

         KDAxis axis = axes[0];*/
         BoundingBox box = node.getBoundingBox();

         double separator = 0;

         KDAxis axis = null;

         boolean foundGoodSplit = false;

         double minSurfaceAreaHeuristic = Double.MAX_VALUE;
         int minSurfaceAreaHeuristicIndex = -1;

         for (int i = 0; i < heuristics.length; i++) {

            axis = heuristics[i].Axis;

            heuristics[i].LessThanObjects = new ArrayList<Shape>();
            heuristics[i].GreaterThanObjects = new ArrayList<Shape>();

            separator = getSplit(node.getObjects(), axis, box);
            heuristics[i].Separator = separator;

            BoundingBox lessThanBoundingBox = null;
            BoundingBox greaterThanBoundingBox = null;

            for (Shape shape : node.getObjects()) {
               BoundingBox drawableBox = shape.GetWorldBoundingBox();
               double lowerBound = drawableBox.getLowerBoundInAxis(axis);

               double upperBound = drawableBox.getUpperBoundInAxis(axis);

               if (upperBound <= separator) {
                  if (lessThanBoundingBox == null)
                     lessThanBoundingBox = drawableBox;
                  else
                     lessThanBoundingBox = BoundingBox.GetBoundingBox(lessThanBoundingBox, drawableBox);

                  heuristics[i].LessThanObjects.add(shape);
               }

               else if (lowerBound >= separator) {
                  if (greaterThanBoundingBox == null)
                     greaterThanBoundingBox = drawableBox;
                  else
                     greaterThanBoundingBox = BoundingBox.GetBoundingBox(greaterThanBoundingBox, drawableBox);
                  heuristics[i].GreaterThanObjects.add(shape);
               }
               else {
                  if (lessThanBoundingBox == null)
                     lessThanBoundingBox = drawableBox;
                  else
                     lessThanBoundingBox = BoundingBox.GetBoundingBox(lessThanBoundingBox, drawableBox);

                  heuristics[i].LessThanObjects.add(shape);

                  if (greaterThanBoundingBox == null)
                     greaterThanBoundingBox = drawableBox;
                  else
                     greaterThanBoundingBox = BoundingBox.GetBoundingBox(greaterThanBoundingBox, drawableBox);

                  heuristics[i].GreaterThanObjects.add(shape);
               }

            }

            heuristics[i].LessThanBoundingBox = lessThanBoundingBox;
            heuristics[i].GreaterThanBoundingBox = greaterThanBoundingBox;


            // check to see if it's a good split

            if (heuristics[i].LessThanObjects.size() == 0 || heuristics[i].GreaterThanObjects.size() == 0)
               continue;

            if (heuristics[i].LessThanObjects.size() >= node.getObjects().size() || heuristics[i].GreaterThanObjects.size() >= node.getObjects().size())
               continue;

            if (heuristics[i].LessThanObjects.size() == heuristics[i].GreaterThanObjects.size()) {

               //Collections.sort(lessThanList, null);
               //Collections.sort(greaterThanList, null);

               boolean same = true;

               Iterator<Shape> p1 = heuristics[i].LessThanObjects.iterator();
               Iterator<Shape> p2 = heuristics[i].GreaterThanObjects.iterator();

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

            BoundingBox[] boxes = SubdivideBoundingBox(box, axis, separator);
            BoundingBox box1 = ReduceBoundingBox(boxes[0], heuristics[i].LessThanObjects);
            BoundingBox box2 = ReduceBoundingBox(boxes[1], heuristics[i].GreaterThanObjects);

            heuristics[i].LessThanBoundingBox = box1;
            heuristics[i].GreaterThanBoundingBox = box2;

            double leftSAH = box1.getSurfaceArea() / heuristics[i].LessThanObjects.size();
            double rightSAH = box2.getSurfaceArea() / heuristics[i].GreaterThanObjects.size();

            heuristics[i].SurfaceAreaHeuristic = leftSAH + rightSAH;
            if (heuristics[i].SurfaceAreaHeuristic < minSurfaceAreaHeuristic) {
               minSurfaceAreaHeuristic = heuristics[i].SurfaceAreaHeuristic;
               minSurfaceAreaHeuristicIndex = i;
               foundGoodSplit = true;
            }

         }

         if (foundGoodSplit) {

            KDAxisHeuristic heuristic = heuristics[minSurfaceAreaHeuristicIndex];

            KDNode leftNode = new KDNode(heuristic.LessThanObjects, getNextAxis(heuristic.Axis));
            leftNode.setBoundingBox(heuristics[minSurfaceAreaHeuristicIndex].LessThanBoundingBox);

            KDNode rightNode = new KDNode(heuristic.GreaterThanObjects, getNextAxis(heuristic.Axis));
            rightNode.setBoundingBox(heuristics[minSurfaceAreaHeuristicIndex].GreaterThanBoundingBox);

            node.setLeftChild(leftNode);
            node.setRightChild(rightNode);
            node.setSeparator(heuristic.Separator);
            node.Axis = heuristic.Axis;

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
                  BuildKDTree(leftNode, depth + 1);
               if (rightNode.getObjects().size() != node.getObjects().size())
                  BuildKDTree(rightNode, depth + 1);
            }
         }
      }
   }


   private static class KDAxisHeuristicImproved {
      public KDAxis Axis;
      public double SurfaceAreaHeuristic;
      public double Separator;
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
         axes.add(node.getBoundingBox().getLargestExtent());
         axes.add(getNextAxis(axes.get(0)));
         axes.add(getNextAxis(axes.get(1)));

         for (KDAxis axis : axes) {
            BoundingEdge[] splits = getSortedBoundingEdges(node.getObjects(), axis);
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

                  //Collections.sort(lessThanList, null);
                  //Collections.sort(greaterThanList, null);

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

               double leftSAH = lessThanBoundingBox.getSurfaceArea() / lessThanList.size();
               double rightSAH = greaterThanBoundingBox.getSurfaceArea() / greaterThanList.size();

               KDAxisHeuristicImproved heuristic = new KDAxisHeuristicImproved();
               heuristic.Axis = axis;
               heuristic.Separator = split;
               heuristic.SurfaceAreaHeuristic = leftSAH + rightSAH;
               heuristicList.add(heuristic);

            }

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

               if (upperBound <= best.Separator) {
                  lessThanList.add(shape);
               }

               else if (lowerBound >= best.Separator) {
                  greaterThanList.add(shape);
               }
               else {
                  lessThanList.add(shape);
                  greaterThanList.add(shape);
               }

            }

            BoundingBox[] boxes = SubdivideBoundingBox(node.getBoundingBox(), best.Axis, best.Separator);

            KDNode leftNode = new KDNode(lessThanList, getNextAxis(best.Axis));
            leftNode.setBoundingBox(boxes[0]);

            KDNode rightNode = new KDNode(greaterThanList, getNextAxis(best.Axis));
            rightNode.setBoundingBox(boxes[1]);

            node.setLeftChild(leftNode);
            node.setRightChild(rightNode);
            node.setSeparator(best.Separator);
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

   public static double getSplit(List<Shape> objects, KDAxis axis, BoundingBox box) {
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
   public static BoundingEdge[] getSortedBoundingEdges(List<Shape> objects, KDAxis axis) {
      BoundingEdge[] edges = new BoundingEdge[objects.size() * 2];

      for (int i = 0; i < objects.size(); i++) {

         Shape shape = objects.get(i);
         double bound = shape.GetWorldBoundingBox().getLowerBoundInAxis(axis);

         BoundingEdge edge = new BoundingEdge();
         edge.Shape = shape;
         edge.Lower = true;
         edge.Value = bound;
         edges[2 * i] = edge;

         bound = shape.GetWorldBoundingBox().getUpperBoundInAxis(axis);

         edge = new BoundingEdge();
         edge.Shape = shape;
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
   private static BoundingEdge[] getMinMaxBoundingEdges(List<Shape> objects, KDAxis axis) {

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
   private static BoundingBox ReduceBoundingBox(BoundingBox box, List<Shape> objects) {

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
