package net.danielthompson.danray.acceleration;

import net.danielthompson.danray.presets.TracerOptions;
import net.danielthompson.danray.shapes.*;
import net.danielthompson.danray.states.Intersection;
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
   public Intersection getNearestShape(Ray ray, int x, int y) {
      // all rays by definition hit the root node

      Intersection state = TraverseTreeBetter(rootNode, ray, 0);

      return state;
   }


   public Intersection GetClosestDrawableOrPlaneToRay(List<AbstractShape> shapes, Ray ray) {

      List<AbstractShape> totalShapes = new ArrayList<>();

      totalShapes.addAll(shapes);

      return GetClosestDrawableToRay(totalShapes, ray);
   }

   private Intersection GetClosestDrawableToRay(List<AbstractShape> shapes, Ray ray) {
      Intersection closestStateToRay = null;
      for (AbstractShape shape : shapes) {
         Intersection state = shape.GetHitInfo(ray);

         if (state.Hits) {
            if (closestStateToRay == null) {
               closestStateToRay = state;
            }
            if (state.t < closestStateToRay.t) {
               closestStateToRay = state;
            }
         }
      }

      return closestStateToRay;
   }

   public Intersection TraverseTree(KDNode node, Ray ray) {
      if (node.isLeaf()) {
         Intersection closestState = GetClosestDrawableOrPlaneToRay(node.Shapes, ray);

         return closestState;
      }
      else {

         KDNode leftNode = node.LeftChild;
         KDNode rightNode = node.RightChild;

         Intersection leftState = leftNode.getHitInfo(ray);
         Intersection rightState = rightNode.getHitInfo(ray);

         boolean hitsLeft = leftState.Hits;
         boolean hitsRight = rightState.Hits;

         Intersection bestStateSoFar = null;

         if (hitsLeft) {
            Intersection bestCandidateState = TraverseTree(leftNode, ray);
            if (bestCandidateState != null && bestCandidateState.Hits)
               bestStateSoFar = bestCandidateState;
         }

         if (hitsRight) {
            Intersection bestCandidateState = TraverseTree(rightNode, ray);
            if (bestCandidateState != null && bestCandidateState.Hits && (bestStateSoFar == null || bestCandidateState.t < bestStateSoFar.t)) {
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

   public Intersection TraverseTreeBetter(KDNode node, Ray ray, int count) {

      count++;
      if (node.isLeaf()) {
         Intersection closestState = getNearestShapeIteratively(node.Shapes, ray);
         if (closestState == null)
            closestState = new Intersection();
         closestState.KDHeatCount = count;
         return closestState;
      }
      else {

         KDNode leftNode = node.LeftChild;
         KDNode rightNode = node.RightChild;

         Intersection leftState = BoundingBox.GetHitInfoNew(getPoint1(leftNode), getPoint2(leftNode), ray);
         Intersection rightState = BoundingBox.GetHitInfoNew(getPoint1(rightNode), getPoint2(rightNode), ray);

         //Intersection leftState = BoundingBox.GetHitInfoNew(leftNode.BoundingBox.point1, leftNode.BoundingBox.point2, ray);
         //Intersection rightState = BoundingBox.GetHitInfoNew(rightNode.BoundingBox.point1, rightNode.BoundingBox.point2, ray);

         //Intersection leftState = leftNode.GetHitInfo(ray);
         //Intersection rightState = rightNode.GetHitInfo(ray);

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
         Intersection nearState = leftState.t < rightState.t ? leftState : rightState;
         KDNode nearNode = leftState.t < rightState.t ? leftNode : rightNode;

         Intersection farState = leftState.t >= rightState.t ? leftState : rightState;
         KDNode farNode = leftState.t >= rightState.t ? leftNode : rightNode;

         Intersection bestCandidateState = new Intersection();
         bestCandidateState.Hits = false;
         bestCandidateState.KDHeatCount = count;

         if (nearState.Hits) {
            Intersection bestNearState = TraverseTreeBetter(nearNode, ray, count);
            if (bestNearState != null) {
               if (bestNearState.Hits)
                  bestCandidateState = bestNearState;
               /*} else {
                  bestCandidateState.KDHeatCount = bestCandidateState.KDHeatCount;
               }*/
            }
         }

         if (farState.Hits && !bestCandidateState.Hits) {
            Intersection bestFarState = TraverseTreeBetter(farNode, ray, count);
            if (bestFarState != null) {
               if (bestFarState.Hits) //{
                  bestCandidateState = (bestCandidateState.t >= 0 && bestCandidateState.t <= bestFarState.t)
                        ? bestCandidateState : bestFarState;
               /*} else {
                  fallBackState.KDHeatCount = bestCandidateState.KDHeatCount;
               } */
            }
         }


         return bestCandidateState;
      }
   }

   public Intersection TraverseTreeIterative(KDNode rootNode, Ray ray) {

      Intersection rootState = rootNode.getHitInfo(ray);

      if (rootState == null || !rootState.Hits)
         return rootState;

      else {
         float minT = rootState.t;
         // TODO fix - rootState.maxT got deleted!
         float maxT = rootState.t;

         Vector inverseDirection = new Vector(1.0f/ ray.Direction.X, 1.0f / ray.Direction.Y, 1.0f / ray.Direction.Z);
         KToDo[] todos = new KToDo[64];
         int todoPos = 0;

         todos[todoPos] = new KToDo(rootNode, ray.MinT, ray.MaxT);

         Intersection closestState = null;

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
               float tPlane = (node.Split - ray.Origin.getAxis(axis)) * inverseDirection.getAxis(axis);

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
      public float tMin, tMax;

      public KToDo(KDNode node, float tMin, float tMax) {
         this.Node = node;
         this.tMin = tMin;
         this.tMax = tMax;
      }
   }

   @Override
   public String compile(TracerOptions _tracerOptions) {

      String abstractCompile = super.compile(_tracerOptions);

      int numThreads = 1;
      if (_tracerOptions != null)
         numThreads = _tracerOptions.numThreads;

      rootNode = KDTree.BuildKDTree(Shapes, 20, 3, numThreads);

      int totalNodes = rootNode.GetCount();

      return abstractCompile + "kd-tree nodes " + totalNodes + ", min depth " + rootNode.GetMinDepth() + ", max depth " + rootNode.GetMaxDepth();
   }

   private class KDIntersectionState {
      public KDNode Node;
      public Intersection State;
   }
}