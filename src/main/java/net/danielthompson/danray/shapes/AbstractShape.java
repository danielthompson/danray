package net.danielthompson.danray.shapes;

import net.danielthompson.danray.acceleration.BoundingEdge;
import net.danielthompson.danray.acceleration.KDAxis;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.*;
import org.lwjgl.system.CallbackI;

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

   protected AbstractShape(Material material) {
      this(null, null, material);
   }

   protected AbstractShape(Transform[] transforms, Material material) {
      this(transforms[0], transforms[1], material);
   }

   protected AbstractShape(Transform objectToWorld, Transform worldToObject, Material material) {
      this.Material = material;
      this.ObjectToWorld = objectToWorld;
      this.WorldToObject = worldToObject;
   }

   public abstract void RecalculateWorldBoundingBox();

   public boolean Hits(Ray worldSpaceRay) {
      throw new java.lang.UnsupportedOperationException();
   }

   /**
    * Assumes that hits(Ray worldSpaceRay) has already been called and that it does actually hit.
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
      // TODO fix such that tangentU and tangentV are actually in du & dv directions (once texture mapping is implemented)

      Vector3 v1 = Constants.PositiveX.cross(intersection.normal);
      Vector3 v2 = Constants.PositiveY.cross(intersection.normal);

      intersection.tangentU = (v1.lengthSquared() > v2.lengthSquared()) ? v1 : v2;
      intersection.tangentV = intersection.tangentU.cross(intersection.normal);
      intersection.tangentU = intersection.tangentV.cross(intersection.normal);
   }

   Point3 OffsetRayOrigin(final Point3 p, final Vector3 pError, final Normal n) {
      float d = pError.dot(n.abs());
      Vector3 offset = new Vector3(n);
      offset.scale(d);

      Point3 po = Point3.plus(p, offset);

      if (offset.x > 0)
         po.x = Math.nextUp(po.x);
      else if (offset.x < 0)
         po.x = Math.nextDown(po.x);

      if (offset.y > 0)
         po.y = Math.nextUp(po.y);
      else if (offset.y < 0)
         po.y = Math.nextDown(po.y);

      if (offset.z > 0)
         po.z = Math.nextUp(po.z);
      else if (offset.z < 0)
         po.z = Math.nextDown(po.z);

      return po;
   }

   protected void ToWorldSpace(Intersection intersection, Ray worldSpaceRay) {
      if (ObjectToWorld != null) {
         //intersection.location = ObjectToWorld.apply(intersection.location);
         ObjectToWorld.applyInPlace(intersection.normal);
         ObjectToWorld.applyInPlace(intersection.tangentU);
         ObjectToWorld.applyInPlace(intersection.tangentV);
         if (ObjectToWorld.hasScale()) {
            intersection.normal.normalize();
            intersection.tangentU.normalize();
            intersection.tangentV.normalize();
            //intersection.t = worldSpaceRay.getTAtPoint(intersection.location);
         }
         final Vector3 error = new Vector3(0, 0, 0);
         ObjectToWorld.applyInPlace(intersection.location, error);
         intersection.location = OffsetRayOrigin(intersection.location, error, intersection.normal);

         if (ObjectToWorld.hasScale()) {
            intersection.t = worldSpaceRay.getTAtPoint(intersection.location);
         }

      }
   }

//   protected void ToWorldSpace(Intersection intersection, Ray worldSpaceRay) {
//      if (ObjectToWorld != null) {
//         intersection.location = ObjectToWorld.apply(intersection.location);
//         intersection.normal = ObjectToWorld.apply(intersection.normal);
//         intersection.tangentU = ObjectToWorld.apply(intersection.tangentU);
//         intersection.tangentV = ObjectToWorld.apply(intersection.tangentV);
//         if (ObjectToWorld.hasScale()) {
//            intersection.normal.normalize();
//            intersection.tangentU.normalize();
//            intersection.tangentV.normalize();
//            intersection.t = worldSpaceRay.getTAtPoint(intersection.location);
//         }
//      }
//   }

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
