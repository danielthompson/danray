package net.danielthompson.danray.acceleration;

import net.danielthompson.danray.presets.TracerOptions;
import net.danielthompson.danray.shapes.*;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.cameras.Camera;
import net.danielthompson.danray.scenes.AbstractScene;
import net.danielthompson.danray.structures.*;


import java.util.ArrayList;
import java.util.List;

/**
 * DanRay
 * User: dthompson
 * Date: 6/28/13
 * Time: 5:24 PM
 */
public class KDScene extends AbstractScene {

   public KDNode rootNode;

   public long drawableIntersections;

   public KDScene(Camera camera) {
      super(camera);
      ImplementationType = "kd-tree";
   }

   @Override
   public IntersectionState getNearestShape(Ray ray) {
      // all rays by definition hit the root node

      IntersectionState state = TraverseTreeBetter(rootNode, ray, 0);

      return state;
   }

   @Override
   public IntersectionState getNearestShapeBeyond(Ray ray, double t) {
      return null;
   }

   public IntersectionState GetClosestDrawableOrPlaneToRay(List<Shape> shapes, Ray ray) {

      List<Shape> totalShapes = new ArrayList<>();

      totalShapes.addAll(shapes);

      return GetClosestDrawableToRay(totalShapes, ray);
   }

   private IntersectionState GetClosestDrawableInNode(KDNode node, Ray ray) {
      IntersectionState closestStateToRay = null;
      for (Shape shape : node.Shapes) {
         IntersectionState state = shape.getHitInfo(ray);

         if (state.Hits && (closestStateToRay == null || state.TMin < closestStateToRay.TMin)) {
            closestStateToRay = state;
         }
      }

      return closestStateToRay;
   }

   private IntersectionState GetClosestDrawableToRay(List<Shape> shapes, Ray ray) {
      IntersectionState closestStateToRay = null;
      for (Shape shape : shapes) {
         IntersectionState state = shape.getHitInfo(ray);

         if (state.Hits) {
            if (closestStateToRay == null) {
               closestStateToRay = state;
            }
            if (state.TMin < closestStateToRay.TMin) {
               closestStateToRay = state;
            }
         }
      }

      return closestStateToRay;
   }

   public IntersectionState TraverseTree(KDNode node, Ray ray) {
      if (node.isLeaf()) {
         IntersectionState closestState = GetClosestDrawableOrPlaneToRay(node.Shapes, ray);

         return closestState;
      }
      else {

         KDNode leftNode = node.LeftChild;
         KDNode rightNode = node.RightChild;

         IntersectionState leftState = leftNode.getHitInfo(ray);
         IntersectionState rightState = rightNode.getHitInfo(ray);

         boolean hitsLeft = leftState.Hits;
         boolean hitsRight = rightState.Hits;

         IntersectionState bestStateSoFar = null;

         if (hitsLeft) {
            IntersectionState bestCandidateState = TraverseTree(leftNode, ray);
            if (bestCandidateState != null && bestCandidateState.Hits)
               bestStateSoFar = bestCandidateState;
         }

         if (hitsRight) {
            IntersectionState bestCandidateState = TraverseTree(rightNode, ray);
            if (bestCandidateState != null && bestCandidateState.Hits && (bestStateSoFar == null || bestCandidateState.TMin < bestStateSoFar.TMin)) {
               bestStateSoFar = bestCandidateState;
            }
         }

         return bestStateSoFar;
      }
   }

   public Point getPoint1(KDNode node) {
      return node.BoundingBox.point1;
   }

   public Point getPoint2(KDNode node) {
      return node.BoundingBox.point2;
   }

   public IntersectionState TraverseTreeBetter(KDNode node, Ray ray, int count) {

      count++;
      if (node.isLeaf()) {
         IntersectionState closestState = GetClosestDrawableInNode(node, ray);
         if (closestState == null)
            closestState = new IntersectionState();
         closestState.KDHeatCount = count;
         return closestState;
      }
      else {

         KDNode leftNode = node.LeftChild;
         KDNode rightNode = node.RightChild;

         IntersectionState leftState = BoundingBox.GetHitInfoNew(getPoint1(leftNode), getPoint2(leftNode), ray);
         IntersectionState rightState = BoundingBox.GetHitInfoNew(getPoint1(rightNode), getPoint2(rightNode), ray);

         //IntersectionState leftState = BoundingBox.GetHitInfoNew(leftNode.BoundingBox.point1, leftNode.BoundingBox.point2, ray);
         //IntersectionState rightState = BoundingBox.GetHitInfoNew(rightNode.BoundingBox.point1, rightNode.BoundingBox.point2, ray);

         //IntersectionState leftState = leftNode.getHitInfo(ray);
         //IntersectionState rightState = rightNode.getHitInfo(ray);

         /*

         boolean hitsLeft = leftState.Hits;
         statistics.BoundingIntersections++;
         boolean hitsRight = rightState.Hits;
         statistics.BoundingIntersections++;

         statistics.BoundsHitLeft = hitsLeft ? statistics.BoundsHitLeft : statistics.BoundsHitLeft + 1;
         statistics.BoundsHitRight = hitsRight ? statistics.BoundsHitRight : statistics.BoundsHitRight + 1;

         if (hitsLeft) {
            if (hitsRight)
               statistics.BothBoundHit++;
            else
               statistics.OneBoundHit++;
         }
         else {
            if (hitsRight) {
               statistics.OneBoundHit++;
            }
            else {
               statistics.NoBoundsHit++;
            }
         }

         if (leftState.TMin == rightState.TMin)
            ;
         */
         IntersectionState nearState = leftState.TMin < rightState.TMin ? leftState : rightState;
         KDNode nearNode = leftState.TMin < rightState.TMin ? leftNode : rightNode;

         IntersectionState farState = leftState.TMin >= rightState.TMin ? leftState : rightState;
         KDNode farNode = leftState.TMin >= rightState.TMin ? leftNode : rightNode;

         IntersectionState bestCandidateState = new IntersectionState();
         bestCandidateState.Hits = false;
         bestCandidateState.KDHeatCount = count;

         if (nearState.Hits) {
            IntersectionState bestNearState = TraverseTreeBetter(nearNode, ray, count);
            if (bestNearState != null) {
               if (bestNearState.Hits)
                  bestCandidateState = bestNearState;
               /*} else {
                  bestCandidateState.KDHeatCount = bestCandidateState.KDHeatCount;
               }*/
            }
         }

         if (farState.Hits) {
            IntersectionState bestFarState = TraverseTreeBetter(farNode, ray, count);
            if (bestFarState != null) {
               if (bestFarState.Hits) //{
                  bestCandidateState = (bestCandidateState.TMin >= 0 && bestCandidateState.TMin <= bestFarState.TMin)
                        ? bestCandidateState : bestFarState;
               /*} else {
                  fallBackState.KDHeatCount = bestCandidateState.KDHeatCount;
               } */
            }
         }


         return bestCandidateState;
      }
   }

   public IntersectionState TraverseTreeIterative(KDNode rootNode, Ray ray) {

      IntersectionState rootState = rootNode.getHitInfo(ray);

      if (rootState == null || !rootState.Hits)
         return rootState;

      else {
         double minT = rootState.TMin;
         double maxT = rootState.TMax;

         Vector inverseDirection = new Vector(1.0 / ray.Direction.X, 1.0 / ray.Direction.Y, 1.0 / ray.Direction.Z);
         KToDo[] todos = new KToDo[64];
         int todoPos = 0;

         todos[todoPos] = new KToDo(rootNode, ray.MinT, ray.MaxT);

         IntersectionState closestState = null;

         KDNode node = todos[todoPos].Node;

         while (node != null) {
            if (ray.MaxT < minT)
               break;
            if (node.isLeaf()) {
               closestState = GetClosestDrawableOrPlaneToRay(node.Shapes, ray);

               if (todoPos > 0) {
                  --todoPos;
                  node = todos[todoPos].Node;
                  minT = todos[todoPos].tMin;
                  maxT = todos[todoPos].tMax;
               }
               else {
                  break;
               }
            }
            else {
               KDAxis axis = node.Axis;
               double tPlane = (node.Split - ray.Origin.getAxis(axis)) * inverseDirection.getAxis(axis);

               KDNode firstChild, secondChild;

               boolean belowFirst = (ray.Origin.getAxis(axis) > node.Split) || (ray.Origin.getAxis(axis) == node.Split && ray.Direction.getAxis(axis) <= 0);

               if (belowFirst) {
                  firstChild = node.LeftChild;
                  secondChild = node.RightChild;
               }
               else {
                  firstChild = node.RightChild;
                  secondChild = node.LeftChild;
               }

               // if we only have to look at the first one
               if (tPlane > maxT || tPlane <= 0) {
                  node = firstChild;
               }
               // if we only have to look at the second one
               else if (tPlane < minT) {
                  node = secondChild;
               }
               // if we have to look at both
               else {
                  todos[todoPos] = new KToDo(secondChild, tPlane, maxT);
                  ++todoPos;
                  node = firstChild;
                  maxT = tPlane;
               }
            }
         }

         return closestState;
      }

   }

   private class KToDo {
      public KDNode Node;
      public double tMin, tMax;

      public KToDo(KDNode node, double tMin, double tMax) {
         this.Node = node;
         this.tMin = tMin;
         this.tMax = tMax;
      }
   }

   @Override
   public String compile(TracerOptions _tracerOptions) {

      int numThreads = 1;
      if (_tracerOptions != null)
         numThreads = _tracerOptions.numThreads;

      rootNode = KDTree.BuildKDTree(Shapes, 20, 3, numThreads);

      int totalNodes = rootNode.GetCount();

      return "kd-tree nodes " + totalNodes + ", min depth " + rootNode.GetMinDepth() + ", max depth " + rootNode.GetMaxDepth();
   }

   private class KDIntersectionState {
      public KDNode Node;
      public IntersectionState State;
   }
}