package net.danielthompson.danray.shapes;

import net.danielthompson.danray.acceleration.BoundingEdge;
import net.danielthompson.danray.acceleration.KDAxis;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by daniel on 2/16/15.
 */
public abstract class AbstractShape {

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

   public abstract void RecalculateWorldBoundingBox();

   public void SetInCurrentKDNode(boolean value) {
      InCurrentKDNode = value;
   }

   public float getMedian(KDAxis axis) {
      return 0;
   }

   public IntersectionState getHitInfo(Ray ray) {
      return null;
   }

   public boolean hits(Ray worldSpaceRay) {
      throw new NotImplementedException();
   }

   public Point calculateIntersectionPoint(Ray worldSpaceRay) {
      Ray objectSpaceRay = worldSpaceRay;

      if (WorldToObject != null) {
         objectSpaceRay = WorldToObject.Apply(worldSpaceRay);
      }

      Point objectSpaceIntersectionPoint = objectSpaceRay.GetPointAtT(objectSpaceRay.MinT);

      Point worldSpaceIntersectionPoint = objectSpaceIntersectionPoint;
      if (ObjectToWorld != null) {
         worldSpaceIntersectionPoint = ObjectToWorld.Apply(objectSpaceIntersectionPoint);
      }

      return worldSpaceIntersectionPoint;
   }

   public Normal calculateIntersectionNormal(Ray worldSpaceRay, float t) {
      throw new NotImplementedException();
   }

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
