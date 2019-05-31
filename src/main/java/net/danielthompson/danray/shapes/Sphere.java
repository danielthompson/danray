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

   public Sphere(final Material material) {
      this(null, null, material);
   }

   public Sphere(final Transform[] transforms, final Material material) {
      this(transforms[0], transforms[1], material);
   }

   public Sphere(final Transform objectToWorld, final Transform worldToObject, final Material material) {
      super(objectToWorld, worldToObject);
      this.Material = material;
      Radius = 1;
      recalculateWorldBoundingBox();
   }

   public void recalculateWorldBoundingBox() {
      // TODO refactor this to be done in world space from the start
      final float p1x = Origin.x - Radius;
      final float p1y = Origin.y - Radius;
      final float p1z = Origin.z - Radius;
      Point3 p1 = new Point3(p1x, p1y, p1z);

      final float p2x = Origin.x + Radius;
      final float p2y = Origin.y + Radius;
      final float p2z = Origin.z + Radius;
      Point3 p2 = new Point3(p2x, p2y, p2z);

      WorldBoundingBox = new BoundingBox(p1, p2);

      if (ObjectToWorld != null) {
         WorldBoundingBox = ObjectToWorld.apply(WorldBoundingBox);
      }
   }

   @Override
   public boolean hits(final Ray worldSpaceRay) {
      Ray objectSpaceRay = worldSpaceRay;

      if (WorldToObject != null) {
         objectSpaceRay = WorldToObject.apply(worldSpaceRay);
      }

      final Vector3 v = Point3.minus(objectSpaceRay.Origin, Origin);

      final float a = objectSpaceRay.Direction.dot(objectSpaceRay.Direction);
      final float b = 2 * (objectSpaceRay.Direction.dot(v));
      final float c = v.dot(v) - (Radius * Radius);

      final float discriminant = (b * b) - (4 * a * c);

      if (discriminant < 0) {
         return false;
      }

      final float root = (float) Math.sqrt(discriminant);
      final float oneOverTwoA = .5f / a;
      final float t0 = (-b + root) * oneOverTwoA;
      final float t1 = (-b - root) * oneOverTwoA;

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

      worldSpaceRay.MinT = hits < worldSpaceRay.MinT ? hits : worldSpaceRay.MinT;
      return true;
   }

   @Override
   public Intersection intersect(final Ray worldSpaceRay) {

      Ray objectSpaceRay = worldSpaceRay;

      if (WorldToObject != null) {
         objectSpaceRay = WorldToObject.apply(worldSpaceRay);
      }

      Point3 objectSpaceIntersectionPoint = objectSpaceRay.getPointAtT(worldSpaceRay.MinT);

      Vector3 error = new Vector3(Math.abs(objectSpaceIntersectionPoint.x), Math.abs(objectSpaceIntersectionPoint.y), Math.abs(objectSpaceIntersectionPoint.z));
      error.scale(Constants.Gamma5);

      Vector3 direction = Point3.minus(objectSpaceIntersectionPoint, Origin);
      Normal objectSpaceNormal = new Normal(direction);

      float actualRadius = direction.length();
      float reprojectionFactor = Radius / actualRadius;

      objectSpaceIntersectionPoint.x *= reprojectionFactor;
      objectSpaceIntersectionPoint.y *= reprojectionFactor;
      objectSpaceIntersectionPoint.z *= reprojectionFactor;

      Intersection intersection = new Intersection();
      intersection.hits = true;
      intersection.shape = this;
      intersection.location = objectSpaceIntersectionPoint;
      intersection.error = error;
      intersection.normal = objectSpaceNormal;
      intersection.entering = objectSpaceNormal.dot(objectSpaceRay.Direction) < 0;

      intersection.u = 0.5f + (float)Math.atan2(-objectSpaceNormal.z, -objectSpaceNormal.x) * Constants.OneOver2Pi;
      intersection.v = 0.5f - (float)Math.asin(-objectSpaceNormal.y) * Constants.OneOverPi;

      calculateTangents(intersection);

      toWorldSpace(intersection);

      return intersection;
   }

   @Override
   public List<Intersection> intersectAll(final Ray worldSpaceRay) {

      List<Intersection> intersections = new ArrayList<>();
      Ray objectSpaceRay = worldSpaceRay;

      if (WorldToObject != null) {
         objectSpaceRay = WorldToObject.apply(worldSpaceRay);
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
         // t0 hits in front of the origin

         worldSpaceRay.MinT = lowT;

         if (ObjectToWorld != null && ObjectToWorld.hasScale()) {
            Point3 objectSpaceIntersectionPoint = objectSpaceRay.getPointAtT(lowT);
            Point3 worldSpaceIntersectionPoint = ObjectToWorld.apply(objectSpaceIntersectionPoint);
            worldSpaceRay.MinT = worldSpaceRay.getTAtPoint(worldSpaceIntersectionPoint);
         }

         Intersection intersection = intersect(worldSpaceRay);

         intersection.t = worldSpaceRay.MinT;

         intersections.add(intersection);
      }

      if (highT > Constants.Epsilon) {
         // t1 hits in front of the origin
         worldSpaceRay.MinT = highT;

         if (ObjectToWorld != null && ObjectToWorld.hasScale()) {
            Point3 objectSpaceIntersectionPoint = objectSpaceRay.getPointAtT(highT);
            Point3 worldSpaceIntersectionPoint = ObjectToWorld.apply(objectSpaceIntersectionPoint);
            worldSpaceRay.MinT = worldSpaceRay.getTAtPoint(worldSpaceIntersectionPoint);
         }

         final Intersection intersection = intersect(worldSpaceRay);
         intersection.t = worldSpaceRay.MinT;
         intersections.add(intersection);
      }

      return intersections;
   }

   @Override
   public float GetMedian(final KDAxis axis) {
      return Origin.getAxis(axis);
   }

   @Override
   public boolean inside(final Point3 worldSpacePoint) {
      Point3 objectSpacePoint = worldSpacePoint;

      if (WorldToObject != null) {
         objectSpacePoint = WorldToObject.apply(worldSpacePoint);
      }

      final float dist = objectSpacePoint.squaredDistanceBetween(Origin);
      final float r2 = Radius * Radius;

      final boolean value = dist < r2 - Constants.DoubleEpsilon * 2f;

      return value;
   }

   public boolean OnSurface(final Point3 point) {
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

      final Sphere rhs = (Sphere) obj;

      return !(Material != null && rhs.Material == null) && !(Material == null && rhs.Material != null) && (Origin.equals(rhs.Origin) && Radius == rhs.Radius && (Material == null || Material.equals(rhs.Material)));

   }

}