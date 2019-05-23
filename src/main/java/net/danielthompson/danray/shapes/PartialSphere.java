package net.danielthompson.danray.shapes;

import net.danielthompson.danray.acceleration.KDAxis;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.*;

import java.util.List;

/**
 * DanRay
 * User: dthompson
 * Date: 6/27/13
 * Time: 1:49 PM
 */
public class PartialSphere extends AbstractShape {

   public Point3 Origin = new Point3(0, 0, 0);
   public float Radius;

   /**
    * Angle around the y axis that the partial sphere is filled. -π = not filled at all, π = totally filled.
    */
   public float Theta;

   /**
    * Angle around the x axis that the partial sphere is filled. 0 = not filled at all, π = totally filled.
    */
   public float Phi;

   public PartialSphere(Transform[] transforms, Material material, float theta, float phi) {
      super(material);
      Radius = 1;
      if (transforms != null) {
         ObjectToWorld = transforms[0];
         WorldToObject = transforms[1];
      }
      RecalculateWorldBoundingBox();

      if (Math.abs(theta) > Constants.PI)
         throw new IllegalArgumentException("Theta must be between -π and π");
      Theta = theta;
      if (phi < 0 || phi > Constants.PI)
         throw new IllegalArgumentException("Phi must be between 0 and π");
      Phi = phi;
   }

   public void RecalculateWorldBoundingBox() {

      float p1x = Origin.x - Radius;
      float p1y = Origin.y - Radius;
      float p1z = Origin.z - Radius;
      Point3 p1 = new Point3(p1x, p1y, p1z);

      float p2x = Origin.x + Radius;
      float p2y = Origin.y + Radius;
      float p2z = Origin.z + Radius;
      Point3 p2 = new Point3(p2x, p2y, p2z);

      WorldBoundingBox = new BoundingBox(p1, p2);

      if (ObjectToWorld != null) {
         WorldBoundingBox = ObjectToWorld.Apply(WorldBoundingBox);
      }
   }

   @Override
   public boolean Hits(Ray worldSpaceRay) {
      Ray objectSpaceRay = worldSpaceRay;

      if (WorldToObject != null) {
         objectSpaceRay = WorldToObject.Apply(worldSpaceRay);
      }

      Vector3 v = Point3.minus(objectSpaceRay.Origin, Origin);

      float a = objectSpaceRay.Direction.dot(objectSpaceRay.Direction);
      float b = 2 * (objectSpaceRay.Direction.dot(v));
      float c = v.dot(v) - (Radius * Radius);

      float discriminant = (b * b) - (4 * a * c);

      if (discriminant < 0) {
         return false;
      }

      float root = (float) Math.sqrt(discriminant);
      float oneOverTwoA = .5f / a;
      float t0 = (-b + root) * oneOverTwoA;
      float t1 = (-b - root) * oneOverTwoA;

      Point3 pt0 = objectSpaceRay.GetPointAtT(t0);
      Point3 pt1 = objectSpaceRay.GetPointAtT(t1);

      float theta0 = (float)Math.atan2(pt0.z, pt0.x);
      float phi0 = (float)Math.acos(pt0.y);

      float theta1 = (float)Math.atan2(pt1.z, pt1.x);
      float phi1 = (float)Math.acos(pt1.y);

      float hits = Constants.NOHIT;

      boolean flipNormals = false;

      if (t1 < Constants.Epsilon) {
         hits = (t0 >= Constants.Epsilon && theta0 <= Theta && phi0 <= Phi) ? t0 : Constants.NOHIT;
      }
      else if (Constants.WithinEpsilon(t1, 0)) {
         hits = (t0 < Constants.Epsilon && theta1 <= Theta && phi1 <= Phi) ? t1 : t0;
      }
      else {
         if (t0 < Constants.Epsilon && theta1 <= Theta && phi1 <= Phi) {
            hits = t1;
         }
         else if (Constants.WithinEpsilon(t0, 0) && theta0 <= Theta && phi0 <= Phi) {
            hits = t0;
         }
         else {
            // case 1: t0 < t1
            if (t0 < t1) {
               // case 1a: t0 real
               if (theta0 <= Theta && phi0 <= Phi) {
                  hits = t0;
               }
               // case 1b: t0 not real, t1 real
               else if (theta1 <= Theta && phi1 <= Phi) {
                  flipNormals = true;
                  hits = t1;
               }
               // case 1c: t0 not real, t1 not real
               else {
                  hits = Constants.NOHIT;
               }
            }
            // case 2: t1 < t0
            else if (t1 < t0) {
               // case 2a: t1 real - hitting closer boundary
               if (theta1 <= Theta && phi1 <= Phi)
                  hits = t1;
               // case 2b: t1 not real, t0 real - hitting further boundary
               else if (theta0 <= Theta && phi0 <= Phi) {
                  hits = t0;
                  flipNormals = true;
               }
               // case 2c: t1 not real, t1 not real
               else
                  hits = Constants.NOHIT;
            }
         }
      }

      if (hits == Constants.NOHIT)
         return false;

      if (hits < Constants.Epsilon)
         return false;

      // convert T back to world space
      if (ObjectToWorld != null && ObjectToWorld.HasScale()) {
         Point3 objectSpaceIntersectionPoint = objectSpaceRay.GetPointAtT(hits);
         Point3 worldSpaceIntersectionPoint = ObjectToWorld.Apply(objectSpaceIntersectionPoint);
         hits = worldSpaceRay.GetTAtPoint(worldSpaceIntersectionPoint);
      }

      worldSpaceRay.FlipNormals = flipNormals;

      worldSpaceRay.MinT = hits < worldSpaceRay.MinT ? hits : worldSpaceRay.MinT;
      return true;
   }

   @Override
   public Intersection GetHitInfo(Ray worldSpaceRay) {

      Ray objectSpaceRay = worldSpaceRay;

      if (WorldToObject != null) {
         objectSpaceRay = WorldToObject.Apply(worldSpaceRay);
      }

      Point3 worldSpaceIntersectionPoint = worldSpaceRay.GetPointAtT(worldSpaceRay.MinT);
      Point3 objectSpaceIntersectionPoint = worldSpaceIntersectionPoint;

      if (WorldToObject != null) {
         objectSpaceIntersectionPoint = WorldToObject.Apply(worldSpaceIntersectionPoint);
      }

      Vector3 direction = Point3.minus(objectSpaceIntersectionPoint, Origin);
      Normal objectSpaceNormal = new Normal(direction);

      if (worldSpaceRay.FlipNormals) {
         objectSpaceNormal.Scale(-1f);
      }

      Intersection intersection = new Intersection();
      intersection.hits = true;
      intersection.shape = this;
      intersection.location = objectSpaceIntersectionPoint;
      intersection.normal = objectSpaceNormal;
      intersection.originInside = Inside(objectSpaceRay.Origin) || OnSurface(objectSpaceRay.Origin);
      intersection.entering = objectSpaceNormal.Dot(objectSpaceRay.Direction) < 0;

      intersection.u = 0.5f + (float)Math.atan2(-objectSpaceNormal.z, -objectSpaceNormal.x) * Constants.OneOver2Pi;
      intersection.v = 0.5f - (float)Math.asin(-objectSpaceNormal.y) * Constants.OneOverPi;

      CalculateTangents(intersection);

      ToWorldSpace(intersection, worldSpaceRay);

      return intersection;
   }

   @Override
   public List<Intersection> GetAllHitPoints(Ray ray) {
      return null;
   }

   @Override
   public float GetMedian(KDAxis axis) {
      return Origin.getAxis(axis);
   }

   public boolean Inside(Point3 point) {
      float dist = point.squaredDistanceBetween(Origin);
      float r2 = Radius * Radius;

      boolean value = dist < r2;

      return value;
   }

   public boolean OnSurface(Point3 point) {
      float difference = Radius * Radius - point.squaredDistanceBetween(Origin);
      return Constants.WithinEpsilon(difference, 0);
   }

   public boolean equals(Object obj) {
      if (obj == null)
         return false;
      if (obj == this)
         return true;
      if (!(obj instanceof PartialSphere))
         return false;

      PartialSphere rhs = (PartialSphere) obj;

      return !(Material != null && rhs.Material == null) && !(Material == null && rhs.Material != null) && (Origin.equals(rhs.Origin) && Radius == rhs.Radius && (Material == null || Material.equals(rhs.Material)));

   }

}