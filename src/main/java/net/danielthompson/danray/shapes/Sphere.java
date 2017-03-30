package net.danielthompson.danray.shapes;

import net.danielthompson.danray.acceleration.KDAxis;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.states.IntersectionState;
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
      this(0.0f, null, null, null);
   }

   public Sphere(Material material) {
      this(0.0f, null, null, material);
   }

   public Sphere(float radius, Transform worldToObject, Transform objectToWorld, Material material) {
      super(material);
      Radius = radius;
      WorldToObject = worldToObject;
      ObjectToWorld = objectToWorld;

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
   public IntersectionState getHitInfo(Ray worldSpaceRay) {

      Ray objectSpaceRay = worldSpaceRay;

      if (WorldToObject != null) {
         objectSpaceRay = WorldToObject.Apply(worldSpaceRay);
      }

      Point intersectionPoint = objectSpaceRay.GetPointAtT(objectSpaceRay.MinT);

      Normal normal = new Normal(Point.Minus(intersectionPoint, Origin));

      if (ObjectToWorld != null) {
         intersectionPoint = ObjectToWorld.Apply(intersectionPoint);
         normal = ObjectToWorld.Apply(normal);
//         if (ObjectToWorld.HasScale()) {
//            worldSpaceRay.TMin = worldSpaceRay.GetTAtPoint(intersectionPoint);
//         }
      }

      normal.Normalize();

      IntersectionState state = new IntersectionState();
      state.Hits = true;
      state.Normal = normal;
      state.IntersectionPoint = intersectionPoint;
      state.Shape = this;

      return state;
   }

   @Override
   public boolean hits(Ray worldSpaceRay) {
      Ray objectSpaceRay = worldSpaceRay;

      if (WorldToObject != null) {
         objectSpaceRay = WorldToObject.Apply(worldSpaceRay);
      }

      float a = objectSpaceRay.Direction.Dot(objectSpaceRay.Direction);
      float b = 2 * (objectSpaceRay.Direction.Dot(Point.Minus(objectSpaceRay.Origin, Origin)));
      float c = Point.Minus(objectSpaceRay.Origin, Origin).Dot(Point.Minus(objectSpaceRay.Origin, Origin)) - (Radius * Radius);

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

      worldSpaceRay.MinT = hits;
      return true;
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

      return (Origin.equals(rhs.Origin) && Radius == rhs.Radius && Material.equals(rhs.Material));
   }

}