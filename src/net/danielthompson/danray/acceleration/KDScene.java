package net.danielthompson.danray.acceleration;

import net.danielthompson.danray.lights.Radiatable;
import net.danielthompson.danray.shapes.*;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.cameras.Camera;
import net.danielthompson.danray.structures.Scene;
import net.danielthompson.danray.structures.Statistics;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Vector;


import java.util.ArrayList;
import java.util.List;

/**
 * DanRay
 * User: dthompson
 * Date: 6/28/13
 * Time: 5:24 PM
 */
public class KDScene extends Scene {


   private List<Drawable> _planes;


   public KDNode rootNode;

   public long drawableIntersections;

   public Statistics statistics;

   public KDScene(Camera camera) {
      super(camera);
   }

   @Override
   public void addDrawableObject(Drawable drawable) {
      if (drawable instanceof ImplicitPlane) {
         _planes.add(drawable);
      }
      else {
         _drawables.add(drawable);
      }
   }

   @Override
   public void addRadiatableObject(Radiatable radiatable) {
      _radiatables.add(radiatable);
   }

   @Override
   public IntersectionState GetClosestDrawableToRay(Ray ray) {

      // all rays by definition hit the root node
      statistics = new Statistics();

      IntersectionState state = TraverseTree(rootNode, ray);


      //IntersectionState planeState = GetClosestPlaneToRay(ray);

      /*if (planeState != null && planeState.Hits) {
         if (state != null && state.Hits) {
            if (planeState.TMin < state.TMin)
               return planeState;
            else
               return state;

         }
         return planeState;
      }*/
      return state;
   }

   @Override
   public String getImplementationType() {
      return "kd-tree";
   }

   public IntersectionState GetClosestPlaneToRay(Ray ray) {
      return GetClosestDrawableToRay(_planes, ray);
   }

   public IntersectionState GetClosestDrawableOrPlaneToRay(List<Drawable> drawables, Ray ray) {

      List<Drawable> totalDrawables = new ArrayList<>();

      //totalDrawables.addAll(_planes);
      totalDrawables.addAll(drawables);

      return GetClosestDrawableToRay(totalDrawables, ray);
   }

   private IntersectionState GetClosestDrawableToRay(List<Drawable> drawables, Ray ray) {
      IntersectionState closestStateToRay = null;
      statistics = new Statistics();
      for (Drawable drawable : drawables) {
         IntersectionState state = drawable.GetHitInfo(ray);
         statistics.DrawableIntersections++;
         state.Statistics = statistics;

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
         IntersectionState closestState = GetClosestDrawableOrPlaneToRay(node.getObjects(), ray);
         return closestState;
      }
      else {

         KDNode leftNode = node.getLeftChild();
         KDNode rightNode = node.getRightChild();

         IntersectionState leftState = leftNode.getHitInfo(ray);
         IntersectionState rightState = rightNode.getHitInfo(ray);

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

         IntersectionState bestStateSoFar = null;

         if (leftState.Hits) {
            IntersectionState bestCandidateState = TraverseTree(leftNode, ray);
            if (bestCandidateState != null && bestCandidateState.Hits)
               bestStateSoFar = bestCandidateState;
         }

         if (rightState.Hits) {
            IntersectionState bestCandidateState = TraverseTree(rightNode, ray);
            if (bestCandidateState != null && bestCandidateState.Hits && (bestStateSoFar == null || bestCandidateState.TMin < bestStateSoFar.TMin)) {
               bestStateSoFar = bestCandidateState;
            }
         }

         return bestStateSoFar;
      }
   }


   public IntersectionState TraverseTreeBetter(KDNode node, Ray ray) {
      if (node.isLeaf()) {
         IntersectionState closestState = GetClosestDrawableOrPlaneToRay(node.getObjects(), ray);
         return closestState;
      }
      else {

         KDNode leftNode = node.getLeftChild();
         KDNode rightNode = node.getRightChild();

         IntersectionState leftState = leftNode.getHitInfo(ray);
         IntersectionState rightState = rightNode.getHitInfo(ray);

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

         IntersectionState nearState = leftState.TMin < rightState.TMin ? leftState : rightState;
         KDNode nearNode = leftState.TMin < rightState.TMin ? leftNode : rightNode;

         IntersectionState farState = leftState.TMin > rightState.TMin ? leftState : rightState;
         KDNode farNode = leftState.TMin > rightState.TMin ? leftNode : rightNode;

         boolean nearNodeHitsButMissesDrawables = false;

         if (nearState.Hits) {
            IntersectionState bestCandidateState = TraverseTreeBetter(nearNode, ray);
            if (bestCandidateState != null && bestCandidateState.Hits)
               return bestCandidateState;
            else
               nearNodeHitsButMissesDrawables = true;
         }

         if (nearNodeHitsButMissesDrawables)
            ;

         if (farState.Hits) {
            IntersectionState bestCandidateState = TraverseTreeBetter(farNode, ray);
            if (bestCandidateState != null && bestCandidateState.Hits) {
               return bestCandidateState;
            }
         }

         return null;
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
               closestState = GetClosestDrawableOrPlaneToRay(node.getObjects(), ray);

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
                  firstChild = node.getLeftChild();
                  secondChild = node.getRightChild();
               }
               else {
                  firstChild = node.getRightChild();
                  secondChild = node.getLeftChild();
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
   public String Compile() {
      rootNode = KDTree.BuildKDTree(_drawables, 5, 2);
      return "kd-tree min depth " + rootNode.GetMinDepth() + ", max depth " + rootNode.GetMaxDepth();
   }

   private class KDIntersectionState {
      public KDNode Node;
      public IntersectionState State;
   }
}