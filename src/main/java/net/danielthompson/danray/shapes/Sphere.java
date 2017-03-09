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

      IntersectionState state = new IntersectionState();

      float a = objectSpaceRay.Direction.Dot(objectSpaceRay.Direction);
      float b = 2 * (objectSpaceRay.Direction.Dot(Point.Minus(objectSpaceRay.Origin, Origin)));
      float c = Point.Minus(objectSpaceRay.Origin, Origin).Dot(Point.Minus(objectSpaceRay.Origin, Origin)) - (Radius * Radius);

      float discriminant = (b * b) - (4 * a * c);

      if (discriminant < 0) {
         state.Hits = false;
         state.IntersectionPoint = null;
         return state;
      }

      float root = (float) Math.sqrt(discriminant);

      float oneOverTwoA = .5f / a;

      float t0 = (-b + root) * oneOverTwoA;

      float t1 = (-b - root) * oneOverTwoA;

      if (t1 < Constants.Epsilon) {
         if (t0 < Constants.Epsilon) {
            state.Hits = false;
            state.IntersectionPoint = null;

         }
         else if (Constants.WithinEpsilon(t0, 0)) {
            state.Hits = true;
            state.IntersectionPoint = objectSpaceRay.Origin;
            state.TMin = t0;

         }
         else /*if (t0 > -Constants.Epsilon)*/ {
            state.Hits = true;
            state.IntersectionPoint = objectSpaceRay.ScaleFromOrigin(t0);
            state.TMin = t0;
         }
      }
      else if (Constants.WithinEpsilon(t1, 0)) {
         state.Hits = true;
         state.IntersectionPoint = objectSpaceRay.Origin;
         state.Shape = this;

         if (t0 < Constants.Epsilon) {
            state.TMin = t1;
         }
         else {
            state.TMin = t0;
         }
      }
      else {
         state.Hits = true;
         if (t0 < Constants.Epsilon) {
            state.IntersectionPoint = objectSpaceRay.ScaleFromOrigin(t1);
            state.TMin = t1;
         }
         else if (Constants.WithinEpsilon(t0, 0)) {
            state.IntersectionPoint = objectSpaceRay.Origin;
            state.TMin = t0;
         }
         else {
            if (t0 < t1) {
               state.IntersectionPoint = objectSpaceRay.ScaleFromOrigin(t0);
               state.TMin = t0;
            }
            else {
               state.IntersectionPoint = objectSpaceRay.ScaleFromOrigin(t1);
               state.TMin = t1;
            }
         }
      }

      if (state.Hits) {
         state.Normal = new Normal(Point.Minus(state.IntersectionPoint, Origin));

         if (ObjectToWorld != null) {
            state.IntersectionPoint = ObjectToWorld.Apply(state.IntersectionPoint);
            state.Normal = ObjectToWorld.Apply(state.Normal);
            if (ObjectToWorld.HasScale()) {
               state.TMin = worldSpaceRay.GetTAtPoint(state.IntersectionPoint);
            }
         }

         state.Normal.Normalize();
         state.Shape = this;
      }

      return state;
   }

   @Override
   public boolean Hits(Ray ray) {
      return getHitInfo(ray).Hits;
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