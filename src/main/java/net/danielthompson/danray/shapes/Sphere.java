package net.danielthompson.danray.shapes;

import net.danielthompson.danray.acceleration.KDAxis;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shapes.csg.CSGShape;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.*;

import java.util.ArrayList;
import java.util.List;

/**
 * DanRay
 * User: dthompson
 * Date: 6/27/13
 * Time: 1:49 PM
 */
public class Sphere extends CSGShape {

   public Point3 Origin = new Point3(0, 0, 0);
   public float Radius;

   public Sphere() {
      this(null, null, null);
   }

   public Sphere(Material material) {
      this(null, null, material);
   }

   public Sphere(Transform[] transforms, Material material) {
      this(transforms[0], transforms[1], material);
   }

   public Sphere(Transform objectToWorld, Transform worldToObject, Material material) {
      super(objectToWorld, worldToObject);
      this.Material = material;
      Radius = 1;
      RecalculateWorldBoundingBox();
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

      float hits;

      if (t1 < Constants.Epsilon) {
         hits = (t0 >= Constants.Epsilon) ? t0 : Constants.NOHIT;
      }
      else if (Constants.WithinEpsilon(t1, 0)) {
         hits = t0 < Constants.Epsilon ? t1 : t0;
      }
      else {
         if (t0 < Constants.Epsilon) {
            hits = t1;
         }
         else if (Constants.WithinEpsilon(t0, 0)) {
            hits = t0;
         }
         else {
            hits = t0 < t1 ? t0 : t1;
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

      Intersection intersection = new Intersection();
      intersection.Hits = true;
      intersection.Shape = this;
      intersection.Location = objectSpaceIntersectionPoint;
      intersection.Normal = objectSpaceNormal;
      intersection.OriginInside = Inside(objectSpaceRay.Origin) || OnSurface(objectSpaceRay.Origin);
      intersection.Entering = objectSpaceNormal.Dot(objectSpaceRay.Direction) < 0;

//      if (intersection.Normal.dot(objectSpaceRay.Direction) > 0)
//         intersection.Normal.scale(-1);

      intersection.u = 0.5f + (float)Math.atan2(-objectSpaceNormal.Z, -objectSpaceNormal.X) * Constants.OneOver2Pi;
      intersection.v = 0.5f - (float)Math.asin(-objectSpaceNormal.Y) * Constants.OneOverPi;

      CalculateTangents(intersection);

      ToWorldSpace(intersection, worldSpaceRay);

      return intersection;
   }

   @Override
   public List<Intersection> GetAllHitPoints(Ray worldSpaceRay) {

      List<Intersection> intersections = new ArrayList<>();
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
         return intersections;
      }

      float root = (float) Math.sqrt(discriminant);
      float oneOverTwoA = .5f / a;
      float t0 = (-b + root) * oneOverTwoA;
      float t1 = (-b - root) * oneOverTwoA;

      float lowT = t0 < t1 ? t0 : t1;
      float highT = t0 > t1 ? t0 : t1;

      if (lowT > Constants.Epsilon) {
         // t0 Hits in front of the origin

         worldSpaceRay.MinT = lowT;

         if (ObjectToWorld != null && ObjectToWorld.HasScale()) {
            Point3 objectSpaceIntersectionPoint = objectSpaceRay.GetPointAtT(lowT);
            Point3 worldSpaceIntersectionPoint = ObjectToWorld.Apply(objectSpaceIntersectionPoint);
            worldSpaceRay.MinT = worldSpaceRay.GetTAtPoint(worldSpaceIntersectionPoint);
         }

         Intersection intersection = GetHitInfo(worldSpaceRay);

         intersection.t = worldSpaceRay.MinT;

         intersections.add(intersection);
      }

      if (highT > Constants.Epsilon) {
         // t1 Hits in front of the origin
         worldSpaceRay.MinT = highT;

         if (ObjectToWorld != null && ObjectToWorld.HasScale()) {
            Point3 objectSpaceIntersectionPoint = objectSpaceRay.GetPointAtT(highT);
            Point3 worldSpaceIntersectionPoint = ObjectToWorld.Apply(objectSpaceIntersectionPoint);
            worldSpaceRay.MinT = worldSpaceRay.GetTAtPoint(worldSpaceIntersectionPoint);
         }

         Intersection intersection = GetHitInfo(worldSpaceRay);
         intersection.t = worldSpaceRay.MinT;
         intersections.add(intersection);
      }

      return intersections;
   }

   @Override
   public float GetMedian(KDAxis axis) {
      return Origin.getAxis(axis);
   }

   @Override
   public boolean Inside(Point3 worldSpacePoint) {
      Point3 objectSpacePoint = worldSpacePoint;

      if (WorldToObject != null) {
         objectSpacePoint = WorldToObject.Apply(worldSpacePoint);
      }

      float dist = objectSpacePoint.squaredDistanceBetween(Origin);
      float r2 = Radius * Radius;

      boolean value = dist < r2 - Constants.DoubleEpsilon * 2f;
      //value = !value && Constants.WithinEpsilon(r2, dist);

//
//      return value;

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
      if (!(obj instanceof Sphere))
         return false;

      Sphere rhs = (Sphere) obj;

      return !(Material != null && rhs.Material == null) && !(Material == null && rhs.Material != null) && (Origin.equals(rhs.Origin) && Radius == rhs.Radius && (Material == null || Material.equals(rhs.Material)));

   }

}