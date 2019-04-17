package net.danielthompson.danray.shapes;

import net.danielthompson.danray.acceleration.BoundingEdge;
import net.danielthompson.danray.acceleration.KDAxis;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.*;

import java.util.List;


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

   public boolean Hits(Ray worldSpaceRay) {
      throw new java.lang.UnsupportedOperationException();
   }

   /**
    * Assumes that Hits(Ray worldSpaceRay) has already been called and that it does actually hit.
    *
    * @param ray
    * @return
    */
   public Intersection GetHitInfo(Ray ray) {
      return null;
   }

   public abstract List<Intersection> GetAllHitPoints(Ray ray);

   public List<Intersection> getAllHitInfos(Ray ray) {
      return null;
   }

   protected void CalculateTangents(Intersection intersection) {
      // TODO fix such that TangentU and TangentV are actually in du & dv directions (once texture mapping is implemented)

      Vector v1 = Constants.PositiveX.Cross(intersection.Normal);
      Vector v2 = Constants.PositiveY.Cross(intersection.Normal);

      intersection.TangentU = (v1.LengthSquared() > v2.LengthSquared()) ? v1 : v2;
      intersection.TangentV = intersection.TangentU.Cross(intersection.Normal);
      intersection.TangentU = intersection.TangentV.Cross(intersection.Normal);
   }

   protected void ToWorldSpace(Intersection intersection, Ray worldSpaceRay) {
      if (ObjectToWorld != null) {
         intersection.Location = ObjectToWorld.Apply(intersection.Location);
         intersection.Normal = ObjectToWorld.Apply(intersection.Normal);
         intersection.TangentU = ObjectToWorld.Apply(intersection.TangentU);
         intersection.TangentV = ObjectToWorld.Apply(intersection.TangentV);
         if (ObjectToWorld.HasScale()) {
            intersection.Normal.Normalize();
            intersection.TangentU.Normalize();
            intersection.TangentV.Normalize();
            intersection.t = worldSpaceRay.GetTAtPoint(intersection.Location);
         }
      }
   }

   public void SetInCurrentKDNode(boolean value) {
      InCurrentKDNode = value;
   }

   public float GetMedian(KDAxis axis) {
      return 0;
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
