package net.danielthompson.danray.acceleration.compactkd;

import net.danielthompson.danray.acceleration.KDNode;
import net.danielthompson.danray.acceleration.KDTree;
import net.danielthompson.danray.cameras.Camera;
import net.danielthompson.danray.presets.TracerOptions;
import net.danielthompson.danray.scenes.AbstractScene;
import net.danielthompson.danray.shapes.Shape;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.BoundingBox;
import net.danielthompson.danray.structures.Point;
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
public class KDCompactScene extends AbstractScene {

   public KDCompactNode[] Nodes;

   public int Size;

   public KDCompactScene(Camera camera) {
      super(camera);
      ImplementationType = "kd-compact-tree";
   }

   @Override
   public IntersectionState getNearestShape(Ray ray) {
      // all rays by definition hit the root node

      IntersectionState state = TraverseTreeBetter(0, ray, 0);

      return state;
   }

   @Override
   public IntersectionState getNearestShapeBeyond(Ray ray, double t) {
      return null;
   }

   private IntersectionState GetClosestDrawableInNode(KDCompactNode node, Ray ray) {
      IntersectionState closestStateToRay = null;
      for (Shape shape : node.Shapes) {
         IntersectionState state = shape.getHitInfo(ray);

         if (state.Hits && (closestStateToRay == null || state.TMin < closestStateToRay.TMin)) {
            closestStateToRay = state;
         }
      }

      return closestStateToRay;
   }

   public IntersectionState TraverseTreeBetter(int index, Ray ray, int count) {

      int leftIndex = 2 * index + 1;
      int rightIndex = 2 * index + 2;

      count++;

      if (rightIndex > Size || Nodes[leftIndex] == null) {
         // it's a leaf
         IntersectionState closestState = GetClosestDrawableInNode(Nodes[index], ray);
         if (closestState == null)
            closestState = new IntersectionState();
         closestState.KDHeatCount = count;
         return closestState;
      }
      else {

         KDCompactNode leftNode = Nodes[leftIndex];
         KDCompactNode rightNode = Nodes[rightIndex];

         IntersectionState leftState = KDCompactScene.GetHitInfo(leftNode, ray);
         IntersectionState rightState = KDCompactScene.GetHitInfo(rightNode, ray);

         IntersectionState nearState = leftState.TMin < rightState.TMin ? leftState : rightState;
         int nearIndex = leftState.TMin < rightState.TMin ? leftIndex : rightIndex;
         //KDCompactNode nearNode = leftState.TMin < rightState.TMin ? leftNode : rightNode;

         IntersectionState farState = leftState.TMin >= rightState.TMin ? leftState : rightState;
         int farIndex = leftState.TMin >= rightState.TMin ? leftIndex : rightIndex;
         //KDCompactNode farNode = leftState.TMin >= rightState.TMin ? leftNode : rightNode;

         IntersectionState bestCandidateState = new IntersectionState();
         bestCandidateState.Hits = false;
         bestCandidateState.KDHeatCount = count;

         if (nearState.Hits) {
            IntersectionState bestNearState = TraverseTreeBetter(nearIndex, ray, count);
            if (bestNearState != null) {
               if (bestNearState.Hits)
                  bestCandidateState = bestNearState;
            }
         }

         if (farState.Hits) {
            IntersectionState bestFarState = TraverseTreeBetter(farIndex, ray, count);
            if (bestFarState != null) {
               if (bestFarState.Hits) //{
                  bestCandidateState = (bestCandidateState.TMin >= 0 && bestCandidateState.TMin <= bestFarState.TMin)
                        ? bestCandidateState : bestFarState;
            }
         }


         return bestCandidateState;
      }
   }

   public static IntersectionState GetHitInfo(KDCompactNode node, Ray ray) {
      double maxBoundFarT = Double.MAX_VALUE;
      double minBoundNearT = 0;

      IntersectionState state = new IntersectionState();
      state.Hits = true;

      // X
      double tNear = (node.p0x - ray.Origin.X) * ray.DirectionInverse.X;
      double tFar = (node.p1x - ray.Origin.X) * ray.DirectionInverse.X;

      double swap = tNear;
      tNear = tNear > tFar ? tFar : tNear;
      tFar = swap > tFar ? swap : tFar;

      minBoundNearT = (tNear > minBoundNearT) ? tNear : minBoundNearT;
      maxBoundFarT = (tFar < maxBoundFarT) ? tFar : maxBoundFarT;
      if (minBoundNearT > maxBoundFarT) {
         state.Hits = false;
         return state;
      }
      state.TMin = minBoundNearT;
      state.TMax = maxBoundFarT;

      // Y
      tNear = (node.p0y - ray.Origin.Y) * ray.DirectionInverse.Y;
      tFar = (node.p1y - ray.Origin.Y) * ray.DirectionInverse.Y;

      swap = tNear;
      tNear = tNear > tFar ? tFar : tNear;
      tFar = swap > tFar ? swap : tFar;

      minBoundNearT = (tNear > minBoundNearT) ? tNear : minBoundNearT;
      maxBoundFarT = (tFar < maxBoundFarT) ? tFar : maxBoundFarT;

      if (minBoundNearT > maxBoundFarT) {
         state.Hits = false;
         return state;
      }

      state.TMin = minBoundNearT;
      state.TMax = maxBoundFarT;

      // Z
      tNear = (node.p0z - ray.Origin.Z) * ray.DirectionInverse.Z;
      tFar = (node.p1z - ray.Origin.Z) * ray.DirectionInverse.Z;

      swap = tNear;
      tNear = tNear > tFar ? tFar : tNear;
      tFar = swap > tFar ? swap : tFar;

      minBoundNearT = (tNear > minBoundNearT) ? tNear : minBoundNearT;
      maxBoundFarT = (tFar < maxBoundFarT) ? tFar : maxBoundFarT;

      if (minBoundNearT > maxBoundFarT) {
         state.Hits = false;
         return state;
      }

      state.TMin = minBoundNearT;
      state.TMax = maxBoundFarT;

      return state;
   }

   @Override
   public String compile(TracerOptions _tracerOptions) {

      int numThreads = 1;
      if (_tracerOptions != null)
         numThreads = _tracerOptions.numThreads;

      Nodes = KDCompactTree.BuildKDTree(Shapes, 20, 3, 1);

      Size = Nodes.length;

      //int totalNodes = rootNode.GetCount();

      return "kd-compact-tree nodes ";// + totalNodes + ", min depth " + rootNode.GetMinDepth() + ", max depth " + rootNode.GetMaxDepth();
   }
}