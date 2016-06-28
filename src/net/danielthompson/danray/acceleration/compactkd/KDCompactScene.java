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

   //public KDNode rootNode;

   public KDCompactScene(Camera camera) {
      super(camera);
      ImplementationType = "kd-compact-tree";
   }

   @Override
   public IntersectionState getNearestShape(Ray ray) {
      // all rays by definition hit the root node

      IntersectionState state = null;//TraverseTreeBetter(rootNode, ray, 0);

      return state;
   }

   @Override
   public IntersectionState getNearestShapeBeyond(Ray ray, double t) {
      return null;
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

         IntersectionState leftState = BoundingBox.GetHitInfoNew(leftNode.BoundingBox.point1, leftNode.BoundingBox.point2, ray);
         IntersectionState rightState = BoundingBox.GetHitInfoNew(rightNode.BoundingBox.point1, rightNode.BoundingBox.point2, ray);

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


   @Override
   public String compile(TracerOptions _tracerOptions) {

      int numThreads = 1;
      if (_tracerOptions != null)
         numThreads = _tracerOptions.numThreads;

      Nodes = KDCompactTree.BuildKDTree(Shapes, 20, 3, 1);

      //int totalNodes = rootNode.GetCount();

      return "kd-compact-tree nodes ";// + totalNodes + ", min depth " + rootNode.GetMinDepth() + ", max depth " + rootNode.GetMaxDepth();
   }
}