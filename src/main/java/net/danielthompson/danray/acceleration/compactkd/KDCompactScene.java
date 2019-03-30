package net.danielthompson.danray.acceleration.compactkd;

import net.danielthompson.danray.cameras.Camera;
import net.danielthompson.danray.presets.TracerOptions;
import net.danielthompson.danray.scenes.AbstractScene;
import net.danielthompson.danray.shapes.AbstractShape;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.Ray;

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
   public Intersection getNearestShape(Ray ray, int x, int y) {
      // all rays by definition hit the root node

      Intersection state = TraverseTreeBetter(0, ray, 0);

      return state;
   }

   private Intersection GetClosestDrawableInNode(KDCompactNode node, Ray ray) {
      Intersection closestStateToRay = null;
      for (AbstractShape shape : node.Shapes) {
         Intersection state = shape.getHitInfo(ray);

         if (state.Hits && (closestStateToRay == null || state.t < closestStateToRay.t)) {
            closestStateToRay = state;
         }
      }

      return closestStateToRay;
   }

   public Intersection TraverseTreeBetter(int index, Ray ray, int count) {

      int leftIndex = 2 * index + 1;
      int rightIndex = 2 * index + 2;

      count++;

      if (rightIndex > Size || Nodes[leftIndex] == null) {
         // it's a leaf
         Intersection closestState = GetClosestDrawableInNode(Nodes[index], ray);
         if (closestState == null)
            closestState = new Intersection();
         closestState.KDHeatCount = count;
         return closestState;
      }
      else {

         KDCompactNode leftNode = Nodes[leftIndex];
         KDCompactNode rightNode = Nodes[rightIndex];

         Intersection leftState = KDCompactScene.GetHitInfo(leftNode, ray);
         Intersection rightState = KDCompactScene.GetHitInfo(rightNode, ray);

         Intersection nearState = leftState.t < rightState.t ? leftState : rightState;
         int nearIndex = leftState.t < rightState.t ? leftIndex : rightIndex;
         //KDCompactNode nearNode = leftState.TMin < rightState.TMin ? leftNode : rightNode;

         Intersection farState = leftState.t >= rightState.t ? leftState : rightState;
         int farIndex = leftState.t >= rightState.t ? leftIndex : rightIndex;
         //KDCompactNode farNode = leftState.TMin >= rightState.TMin ? leftNode : rightNode;

         Intersection bestCandidateState = new Intersection();
         bestCandidateState.Hits = false;
         bestCandidateState.KDHeatCount = count;

         if (nearState.Hits) {
            Intersection bestNearState = TraverseTreeBetter(nearIndex, ray, count);
            if (bestNearState != null) {
               if (bestNearState.Hits)
                  bestCandidateState = bestNearState;
            }
         }

         if (farState.Hits) {
            Intersection bestFarState = TraverseTreeBetter(farIndex, ray, count);
            if (bestFarState != null) {
               if (bestFarState.Hits) //{
                  bestCandidateState = (bestCandidateState.t >= 0 && bestCandidateState.t <= bestFarState.t)
                        ? bestCandidateState : bestFarState;
            }
         }


         return bestCandidateState;
      }
   }

   public static Intersection GetHitInfo(KDCompactNode node, Ray ray) {
      float maxBoundFarT = Float.MAX_VALUE;
      float minBoundNearT = 0;

      Intersection state = new Intersection();
      state.Hits = true;

      // X
      float tNear = (node.p0x - ray.Origin.X) * ray.DirectionInverse.X;
      float tFar = (node.p1x - ray.Origin.X) * ray.DirectionInverse.X;

      float swap = tNear;
      tNear = tNear > tFar ? tFar : tNear;
      tFar = swap > tFar ? swap : tFar;

      minBoundNearT = (tNear > minBoundNearT) ? tNear : minBoundNearT;
      maxBoundFarT = (tFar < maxBoundFarT) ? tFar : maxBoundFarT;
      if (minBoundNearT > maxBoundFarT) {
         state.Hits = false;
         return state;
      }
      state.t = minBoundNearT;

      // fix
      state.t = maxBoundFarT;

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

      // TODO fix max T?
      state.t = maxBoundFarT;
      state.t = minBoundNearT;

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

      // TODO fix max t values?
      state.t = maxBoundFarT;
      state.t = minBoundNearT;

      return state;
   }

   @Override
   public String compile(TracerOptions _tracerOptions) {

      String abstractCompile = super.compile(_tracerOptions);

      int numThreads = 1;
      if (_tracerOptions != null)
         numThreads = _tracerOptions.numThreads;

      Nodes = KDCompactTree.BuildKDTree(Shapes, 20, 3, 1);

      Size = Nodes.length;

      //int totalNodes = rootNode.GetCount();

      return abstractCompile + "kd-compact-tree nodes ";// + totalNodes + ", min depth " + rootNode.GetMinDepth() + ", max depth " + rootNode.GetMaxDepth();
   }
}