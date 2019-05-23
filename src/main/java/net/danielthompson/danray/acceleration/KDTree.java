package net.danielthompson.danray.acceleration;

import net.danielthompson.danray.shapes.AbstractShape;

import net.danielthompson.danray.structures.BoundingBox;
import net.danielthompson.danray.structures.Point3;

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

   public static KDNode BuildKDTree(List<AbstractShape> objects, int maxDepth, int maxLeafSize) {

      return BuildKDTree(objects, maxDepth, maxLeafSize, 1);
   }

   /**
    * Builds a KDTree from the list of objects and returns the root node.
    * @param objects The list of objects to build into a tree.
    * @param maxDepth The number of nodes from the root to the deepest leaf, inclusive
    * @param maxLeafSize The maximum number of objects in a leaf
    * @return
    */
   public static KDNode BuildKDTree(List<AbstractShape> objects, int maxDepth, int maxLeafSize, int numThreads) {

      KDTree.maxDepth = maxDepth;
      KDTree.maxLeafSize = maxLeafSize;

      KDNode rootNode = new KDNode(objects);

      Point3 minPoint = null; //new Point(-Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE);
      Point3 maxPoint = null; //new Point(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);

      xEdges = getSortedBoundingEdges(objects, KDAxis.X);
      yEdges = getSortedBoundingEdges(objects, KDAxis.Y);
      zEdges = getSortedBoundingEdges(objects, KDAxis.Z);

      minPoint = new Point3(xEdges[0].Value, yEdges[0].Value, zEdges[0].Value);
      maxPoint = new Point3(xEdges[xEdges.length - 1].Value, yEdges[yEdges.length - 1].Value, zEdges[zEdges.length - 1].Value);

      BoundingBox initialBoundingBox = new BoundingBox(minPoint, maxPoint);

      rootNode.BoundingBox = (initialBoundingBox);

      BuildKDTreeSAH(rootNode, 1);

      return rootNode;
   }

   private static class KDAxisHeuristicImproved {
      public KDAxis Axis;
      public float lessThanHeuristic;
      public float greaterThanHeuristic;
      public float SurfaceAreaHeuristic;
      public float Separator;
   }

   private static BoundingEdge[] getEdgesWithObjectsForAxis(List<AbstractShape> objects, KDAxis axis) {
      BoundingEdge[] source = xEdges;

      if (axis == KDAxis.Y)
         source = yEdges;
      else if (axis == KDAxis.Z)
         source = zEdges;

      BoundingEdge[] dest = new BoundingEdge[2 * objects.size()];

      int destIndex = 0;

      for (int sourceIndex = 0; sourceIndex < source.length; sourceIndex++) {
         AbstractShape sourceShape = source[sourceIndex].Shape;
         for (AbstractShape object : objects) {
            if (object.equals(sourceShape)) {
               dest[destIndex++] = source[sourceIndex];
               break;
            }
         }
      }

      return dest;
   }

   public static void BuildKDTreeSAH(KDNode node, int depth) {

      // base case

      if (node.Shapes.size() <= maxLeafSize) {
         //node.Axis = null;
         return;
      }

      // recursive case

      else {
         List<KDAxisHeuristicImproved> heuristicList = new ArrayList<>();
         boolean foundGoodSplit = false;
         List<KDAxis> axes = new ArrayList<>();
         axes.add(node.BoundingBox.getLargestExtent());
         axes.add(getNextAxis(axes.get(0)));
         axes.add(getNextAxis(axes.get(1)));

         for (KDAxis axis : axes) {
            //BoundingEdge[] splits = getSortedBoundingEdges(node.Shapes, axis);
            BoundingEdge[] splits = getEdgesWithObjectsForAxis(node.Shapes, axis);
            for (int i = 0; i < splits.length; i = i + 1) {
               float split = splits[i].Value;

               BoundingBox lessThanBoundingBox = null;
               BoundingBox greaterThanBoundingBox = null;

               List<AbstractShape> lessThanList = new ArrayList<>();
               List<AbstractShape> greaterThanList = new ArrayList<>();

               for (AbstractShape shape : node.Shapes) {
                  BoundingBox drawableBox = shape.WorldBoundingBox;
                  float lowerBound = drawableBox.getLowerBoundInAxis(axis);
                  float upperBound = drawableBox.getUpperBoundInAxis(axis);

                  if (upperBound <= split) {
                     if (lessThanBoundingBox == null)
                        lessThanBoundingBox = new BoundingBox(drawableBox);
                     else {
                        BoundingBox.ExpandBoundingBox(lessThanBoundingBox, drawableBox);
                     }
                     lessThanList.add(shape);
                  }

                  else if (lowerBound >= split) {
                     if (greaterThanBoundingBox == null)
                        greaterThanBoundingBox = new BoundingBox(drawableBox);
                     else {
                        BoundingBox.ExpandBoundingBox(greaterThanBoundingBox, drawableBox);
                     }
                     greaterThanList.add(shape);
                  }
                  else {
                     if (lessThanBoundingBox == null)
                        lessThanBoundingBox = new BoundingBox(drawableBox);
                     else {
                        BoundingBox.ExpandBoundingBox(lessThanBoundingBox, drawableBox);
                     }

                     if (greaterThanBoundingBox == null)
                        greaterThanBoundingBox = new BoundingBox(drawableBox);
                     else {
                        BoundingBox.ExpandBoundingBox(greaterThanBoundingBox, drawableBox);
                     }

                     lessThanList.add(shape);
                     greaterThanList.add(shape);
                  }

               }

               if (lessThanList.size() == 0 || greaterThanList.size() == 0)
                  continue;

               if (lessThanList.size() == greaterThanList.size()) {

                  boolean same = true;

                  Iterator<AbstractShape> p1 = lessThanList.iterator();
                  Iterator<AbstractShape> p2 = greaterThanList.iterator();

                  while (p1.hasNext() && p2.hasNext()) {
                     AbstractShape d1 = p1.next();
                     AbstractShape d2 = p2.next();

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

            List<AbstractShape> lessThanList = new ArrayList<>();
            List<AbstractShape> greaterThanList = new ArrayList<>();

            for (AbstractShape shape : node.Shapes) {
               BoundingBox drawableBox = shape.WorldBoundingBox;
               float lowerBound = drawableBox.getLowerBoundInAxis(best.Axis);
               float upperBound = drawableBox.getUpperBoundInAxis(best.Axis);

               if (upperBound > best.Separator)
                  greaterThanList.add(shape);

               if (lowerBound <= best.Separator)
                  lessThanList.add(shape);

            }

            BoundingBox[] boxes = SubdivideBoundingBox(node.BoundingBox, best.Axis, best.Separator);

            ReduceBoundingBox(boxes[0], lessThanList);
            ReduceBoundingBox(boxes[1], greaterThanList);

            KDNode leftNode = new KDNode(lessThanList, getNextAxis(best.Axis));
            leftNode.BoundingBox = (boxes[0]);

            KDNode rightNode = new KDNode(greaterThanList, getNextAxis(best.Axis));
            rightNode.BoundingBox = (boxes[1]);

            node.LeftChild = leftNode;
            node.RightChild = rightNode;
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

               if (leftNode.Shapes.size() != node.Shapes.size())
                  BuildKDTreeSAH(leftNode, depth + 1);
               if (rightNode.Shapes.size() != node.Shapes.size())
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
   private static BoundingBox[] SubdivideBoundingBox(BoundingBox box, KDAxis axis, float separator) {
      BoundingBox lower;
      BoundingBox upper;

      float minX = box.getLowerBoundInAxis(KDAxis.X);
      float minY = box.getLowerBoundInAxis(KDAxis.Y);
      float minZ = box.getLowerBoundInAxis(KDAxis.Z);
      float maxX = box.getUpperBoundInAxis(KDAxis.X);
      float maxY = box.getUpperBoundInAxis(KDAxis.Y);
      float maxZ = box.getUpperBoundInAxis(KDAxis.Z);

      Point3 lowerMinPoint = new Point3(minX, minY, minZ);
      Point3 lowerMaxPoint = new Point3(maxX, maxY, maxZ);
      lowerMaxPoint.setAxis(axis, separator);

      Point3 upperMinPoint = new Point3(minX, minY, minZ);
      upperMinPoint.setAxis(axis, separator);
      Point3 upperMaxPoint = new Point3(maxX, maxY, maxZ);

      lower = new BoundingBox(lowerMinPoint, lowerMaxPoint);
      upper = new BoundingBox(upperMinPoint, upperMaxPoint);

      return new BoundingBox[] {lower, upper};
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


   /**
    *
    * @param objects
    * @param axis
    * @return
    */
   public static BoundingEdge[] getSortedBoundingEdges(List<AbstractShape> objects, KDAxis axis) {
      BoundingEdge[] edges = new BoundingEdge[objects.size() * 2];

      BoundingEdge[] current;

      for (int i = 0; i < objects.size(); i++) {

         AbstractShape shape = objects.get(i);
         float bound = shape.WorldBoundingBox.getLowerBoundInAxis(axis);

         BoundingEdge edgeLower = new BoundingEdge();
         edgeLower.Shape = shape;
         edgeLower.Lower = true;
         edgeLower.Value = bound;
         edges[2 * i] = edgeLower;

         current = new BoundingEdge[2];
         current[0] = edgeLower;

         bound = shape.WorldBoundingBox.getUpperBoundInAxis(axis);

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
    * Reduces the size of the bounding box to fit the objects, if possible
    * @param box
    * @param objects
    */
   private static void ReduceBoundingBox(BoundingBox box, List<AbstractShape> objects) {

      float xLowerBound = Float.MAX_VALUE;
      float yLowerBound = Float.MAX_VALUE;
      float zLowerBound = Float.MAX_VALUE;

      float xUpperBound = -Float.MAX_VALUE;
      float yUpperBound = -Float.MAX_VALUE;
      float zUpperBound = -Float.MAX_VALUE;

      for (AbstractShape shape : objects) {
         if (shape.WorldBoundingBox.point1.x < xLowerBound)
            xLowerBound = shape.WorldBoundingBox.point1.x;
         if (shape.WorldBoundingBox.point1.y < yLowerBound)
            yLowerBound = shape.WorldBoundingBox.point1.y;
         if (shape.WorldBoundingBox.point1.z < zLowerBound)
            zLowerBound = shape.WorldBoundingBox.point1.z;

         if (shape.WorldBoundingBox.point2.x > xUpperBound)
            xUpperBound = shape.WorldBoundingBox.point2.x;
         if (shape.WorldBoundingBox.point2.y > yUpperBound)
            yUpperBound = shape.WorldBoundingBox.point2.y;
         if (shape.WorldBoundingBox.point2.z > zUpperBound)
            zUpperBound = shape.WorldBoundingBox.point2.z;
      }

      if (xLowerBound > box.point1.x)
         box.point1.x = xLowerBound;
      if (yLowerBound > box.point1.y)
         box.point1.y = yLowerBound;
      if (zLowerBound > box.point1.z)
         box.point1.z = zLowerBound;

      if (xUpperBound < box.point2.x)
         box.point2.x = xUpperBound;
      if (yUpperBound < box.point2.y)
         box.point2.y = yUpperBound;
      if (zUpperBound < box.point2.z)
         box.point2.z = zUpperBound;

   }
}
