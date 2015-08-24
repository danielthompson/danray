package net.danielthompson.danray.shapes;

import net.danielthompson.danray.acceleration.KDAxis;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.*;
import net.danielthompson.danray.structures.Point;

/**
 * DanRay
 * User: dthompson
 * Date: 6/27/13
 * Time: 1:49 PM
 */
public class Sphere extends DrawableBase {

   public Point Origin = new Point(0, 0, 0);
   public double Radius;
   public Transform WorldToObject;
   public Transform ObjectToWorld;

   public Material _material;

   public Sphere() {

   }

   public Sphere(Material material) {
      _material = material;
   }

   public Sphere(double radius, Transform worldToObject, Transform objectToWorld, Material material) {
      Radius = radius;
      WorldToObject = worldToObject;
      ObjectToWorld = objectToWorld;
      _material = material;
   }

   @Override
   public Material GetMaterial() {
      return _material;
   }

   @Override
   public IntersectionState GetHitInfo(Ray worldSpaceRay) {

      Ray objectSpaceRay = worldSpaceRay;
      
      if (WorldToObject != null) {
         objectSpaceRay = WorldToObject.Apply(worldSpaceRay);
      }

      IntersectionState state = new IntersectionState();

      double a = objectSpaceRay.Direction.Dot(objectSpaceRay.Direction);
      double b = 2 * (objectSpaceRay.Direction.Dot(Point.Minus(objectSpaceRay.Origin, Origin)));
      double c = Point.Minus(objectSpaceRay.Origin, Origin).Dot(Point.Minus(objectSpaceRay.Origin, Origin)) - (Radius * Radius);

      double discriminant = (b * b) - (4 * a * c);

      if (discriminant < 0) {
         state.Hits = false;
         state.IntersectionPoint = null;
         return state;
      }

      double root = Math.sqrt(discriminant);

      double oneOverTwoA = .5 / a;

      double t0 = (-b + root) * oneOverTwoA;

      double t1 = (-b - root) * oneOverTwoA;

      if (t1 < Constants.NumericalDelta) {
         if (t0 < Constants.NumericalDelta) {
            state.Hits = false;
            state.IntersectionPoint = null;

         }
         else if (Constants.WithinDelta(t0, 0)) {
            state.Hits = true;
            state.IntersectionPoint = objectSpaceRay.Origin;
            state.TMin = t0;

         }
         else /*if (t0 > -Constants.NumericalDelta)*/ {
            state.Hits = true;
            state.IntersectionPoint = objectSpaceRay.ScaleFromOrigin(t0);
            state.TMin = t0;
         }
      }
      else if (Constants.WithinDelta(t1, 0)) {
         state.Hits = true;
         state.IntersectionPoint = objectSpaceRay.Origin;
         state.Drawable = this;

         if (t0 < Constants.NumericalDelta) {
            state.TMin = t1;
         }
         else {
            state.TMin = t0;
         }
      }
      else {
         state.Hits = true;
         if (t0 < Constants.NumericalDelta) {
            state.IntersectionPoint = objectSpaceRay.ScaleFromOrigin(t1);
            state.TMin = t1;
         }
         else if (Constants.WithinDelta(t0, 0)) {
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
         state.Drawable = this;
      }

      return state;
   }

   @Override
   public double getSurfaceArea() {
      return 4 * Math.PI * Radius * Radius;
   }

   @Override
   public boolean Hits(Ray ray) {
      return GetHitInfo(ray).Hits;
   }

   public BoundingBox GetWorldBoundingBox() {

      double p1x = Origin.X - Radius;
      double p1y = Origin.Y - Radius;
      double p1z = Origin.Z - Radius;
      Point p1 = new Point(p1x, p1y, p1z);

      double p2x = Origin.X + Radius;
      double p2y = Origin.Y + Radius;
      double p2z = Origin.Z + Radius;
      Point p2 = new Point(p2x, p2y, p2z);

      BoundingBox b = new BoundingBox(p1, p2);

      if (ObjectToWorld != null) {
         b = ObjectToWorld.Apply(b);
      }

      return b;
   }

   @Override
   public double getMedian(KDAxis axis) {
      return Origin.getAxis(axis);
   }

   public boolean Inside(Point point) {
      return (Radius * Radius > point.SquaredDistanceBetween(Origin));

   }

   public boolean equals(Object obj) {
      if (obj == null)
         return false;
      if (obj == this)
         return true;
      if (!(obj instanceof Sphere))
         return false;

      Sphere rhs = (Sphere) obj;

      return (Origin.equals(rhs.Origin) && Radius == rhs.Radius && _material.equals(rhs._material));
   }

   @Override
   public double GetVolume() {
      return (4.0 / 3.0) * Math.PI * Radius * Radius * Radius;
   }


}