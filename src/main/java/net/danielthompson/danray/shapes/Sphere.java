package net.danielthompson.danray.shapes;

import net.danielthompson.danray.acceleration.KDAxis;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.*;

/**
 * DanRay
 * User: dthompson
 * Date: 6/27/13
 * Time: 1:49 PM
 */
public class Sphere extends AbstractShape {

   public Point Origin = new Point(0, 0, 0);
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
      super(material);
      Radius = 1;
      ObjectToWorld = objectToWorld;
      WorldToObject = worldToObject;
      RecalculateWorldBoundingBox();
   }

   public void RecalculateWorldBoundingBox() {

      float p1x = Origin.X - Radius;
      float p1y = Origin.Y - Radius;
      float p1z = Origin.Z - Radius;
      Point p1 = new Point(p1x, p1y, p1z);

      float p2x = Origin.X + Radius;
      float p2y = Origin.Y + Radius;
      float p2z = Origin.Z + Radius;
      Point p2 = new Point(p2x, p2y, p2z);

      WorldBoundingBox = new BoundingBox(p1, p2);

      if (ObjectToWorld != null) {
         WorldBoundingBox = ObjectToWorld.Apply(WorldBoundingBox);
      }
   }

   @Override
   public boolean hits(Ray worldSpaceRay) {
      Ray objectSpaceRay = worldSpaceRay;

      if (WorldToObject != null) {
         objectSpaceRay = WorldToObject.Apply(worldSpaceRay);
      }

      Vector v = Point.Minus(objectSpaceRay.Origin, Origin);

      float a = objectSpaceRay.Direction.Dot(objectSpaceRay.Direction);
      float b = 2 * (objectSpaceRay.Direction.Dot(v));
      float c = v.Dot(v) - (Radius * Radius);

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
         Point objectSpaceIntersectionPoint = objectSpaceRay.GetPointAtT(hits);
         Point worldSpaceIntersectionPoint = ObjectToWorld.Apply(objectSpaceIntersectionPoint);
         hits = worldSpaceRay.GetTAtPoint(worldSpaceIntersectionPoint);
      }

      worldSpaceRay.MinT = hits < worldSpaceRay.MinT ? hits : worldSpaceRay.MinT;
      return true;
   }


   @Override
   public Intersection getHitInfo(Ray worldSpaceRay) {

      Point worldSpaceIntersectionPoint = worldSpaceRay.GetPointAtT(worldSpaceRay.MinT);
      Point objectSpaceIntersectionPoint = worldSpaceIntersectionPoint;

      if (WorldToObject != null) {
         objectSpaceIntersectionPoint = WorldToObject.Apply(worldSpaceIntersectionPoint);
      }

      Vector direction = Point.Minus(objectSpaceIntersectionPoint, Origin);
      Normal objectSpaceNormal = new Normal(direction);

      Intersection intersection = new Intersection();
      intersection.Hits = true;
      intersection.Shape = this;
      intersection.Location = objectSpaceIntersectionPoint;
      intersection.Normal = objectSpaceNormal;

      calculateTangents(intersection);

      ToWorldSpace(intersection, worldSpaceRay);

      return intersection;
   }

   @Override
   public float getMedian(KDAxis axis) {
      return Origin.getAxis(axis);
   }

   public boolean Inside(Point point) {
      float dist = point.SquaredDistanceBetween(Origin);
      float r2 = Radius * Radius;

      boolean value = dist < r2;

      return value;
   }

   public boolean OnSurface(Point point) {
      float difference = Radius * Radius - point.SquaredDistanceBetween(Origin);
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