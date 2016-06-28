package net.danielthompson.danray.shapes;

import net.danielthompson.danray.acceleration.BoundingEdge;
import net.danielthompson.danray.acceleration.KDAxis;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.BoundingBox;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Transform;

/**
 * Created by daniel on 2/16/15.
 */
public abstract class AbstractShape implements Shape {

   public int ID;

   public BoundingBox WorldBoundingBox;

   public Transform ObjectToWorld;
   public Transform WorldToObject;

   public Material Material;

   public boolean InCurrentKDNode;

   BoundingEdge[] xBoundingEdges;
   BoundingEdge[] yBoundingEdges;
   BoundingEdge[] zBoundingEdges;

   public AbstractShape(Material material) {
      this.Material = material;
   }

   @Override
   public BoundingBox GetWorldBoundingBox() {
      return WorldBoundingBox;
   }

   public int getID() {
      return ID;
   }

   @Override
   public void SetInCurrentKDNode(boolean value) {
      InCurrentKDNode = value;
   }

   @Override
   public double getMedian(KDAxis axis) {
      return 0;
   }

   @Override
   public IntersectionState getHitInfo(Ray ray) {
      return null;
   }

   @Override
   public boolean Hits(Ray ray) {
      return false;
   }

   @Override
   public Material GetMaterial() {
      return Material;
   }

   @Override
   public BoundingEdge[] GetBoundingEdges(KDAxis axis) {
      switch (axis) {
         case X:
            return xBoundingEdges;
         case Y:
            return yBoundingEdges;
         case Z:
            return zBoundingEdges;
      }

      return null;
   }

   @Override
   public void SetBoundingEdges(BoundingEdge[] edges, KDAxis axis) {
      switch (axis) {
         case X:
            xBoundingEdges = edges;
            break;
         case Y:
            yBoundingEdges = edges;
            break;
         case Z:
            zBoundingEdges = edges;
            break;
      }
   }
}
