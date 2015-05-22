package net.danielthompson.danray.shapes;

import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.*;

/**
 * Created by daniel on 3/2/15.
 */
public class Cylinder extends DrawableBase {

   public double Radius;
   public double Height;

   public Transform WorldToObject;
   public Transform ObjectToWorld;

   public Material Material;

   private BoundingBox _worldBoundingBox;

   public Cylinder() {

   }

   public Cylinder(double radius, double height, Transform worldToObject, Transform objectToWorld, Material material) {
      Radius = radius;
      Height = height;
      WorldToObject = worldToObject;
      ObjectToWorld = objectToWorld;
      Material = material;
   }

   @Override
   public BoundingBox GetWorldBoundingBox() {
      if (_worldBoundingBox == null) {
         BoundingBox box = new BoundingBox(new Point(-Radius, 0, -Radius), new Point(Radius, Height, Radius));
         if (ObjectToWorld != null) {
            box = ObjectToWorld.Apply(box);
         }
         _worldBoundingBox = box;
      }

      return _worldBoundingBox;
   }

   @Override
   public IntersectionState GetHitInfo(Ray worldSpaceRay) {
      Ray objectSpaceRay = worldSpaceRay;

      if (WorldToObject != null) {
         objectSpaceRay = WorldToObject.Apply(worldSpaceRay);
      }

      IntersectionState state = new IntersectionState();

      // check for intersection with upper disk

      double tTop = (Height - objectSpaceRay.Origin.Y) / (objectSpaceRay.Direction.Y);

      Point topHitPoint = objectSpaceRay.GetPointAtT(tTop);

      boolean topHits = false;
      Normal topNormal = new Normal(0, 1, 0);

      if ((tTop > 0) && Math.sqrt(topHitPoint.X * topHitPoint.X + topHitPoint.Z * topHitPoint.Z) <= Radius) {
         topHits = true;
         state.Hits = true;
      }

      // check for intersection with lower disk

      double tBottom = (-objectSpaceRay.Origin.Y) / (objectSpaceRay.Direction.Y);

      Point bottomHitPoint = objectSpaceRay.GetPointAtT(tBottom);

      boolean bottomHits = false;
      Normal bottomNormal = new Normal(0, -1, 0);

      if ((tBottom > 0) && Math.sqrt(bottomHitPoint.X * bottomHitPoint.X + bottomHitPoint.Z * bottomHitPoint.Z) <= Radius) {
         bottomHits = true;
         state.Hits = true;
      }

      // check for intersection with cylinder

      double a = objectSpaceRay.Direction.X * objectSpaceRay.Direction.X + objectSpaceRay.Direction.Z * objectSpaceRay.Direction.Z;
      double b = 2 * (objectSpaceRay.Origin.X * objectSpaceRay.Direction.X + objectSpaceRay.Origin.Z * objectSpaceRay.Direction.Z);
      double c = objectSpaceRay.Origin.X * objectSpaceRay.Origin.X + objectSpaceRay.Origin.Z * objectSpaceRay.Origin.Z - (Radius * Radius);

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

      boolean t0Hits = false;
      Normal t0Normal = null;

      if (t0 > 0.0) {
         double py = objectSpaceRay.Origin.Y + t0 * objectSpaceRay.Direction.Y;
         if ((py <= Height) && (py >= 0)) {
            t0Hits = true;
            state.Hits = true;
            double px = objectSpaceRay.Origin.X + t0 * objectSpaceRay.Direction.X;
            double pz = objectSpaceRay.Origin.Z + t0 * objectSpaceRay.Direction.Z;
            t0Normal = new Normal(px, 0, pz);
         }
      }

      boolean t1Hits = false;
      Normal t1Normal = null;

      if (t1 > 0.0) {
         double py = objectSpaceRay.Origin.Y + t1 * objectSpaceRay.Direction.Y;
         if ((py <= Height) && (py >= 0)) {
            t1Hits = true;
            state.Hits = true;
            double px = objectSpaceRay.Origin.X + t1 * objectSpaceRay.Direction.X;
            double pz = objectSpaceRay.Origin.Z + t1 * objectSpaceRay.Direction.Z;
            t1Normal = new Normal(px, 0, pz);
         }
      }

      if (bottomHits && !topHits && !t0Hits && !t1Hits) {
         state.Hits = false;
      }
      else if (!bottomHits && topHits && !t0Hits && !t1Hits) {
         state.Hits = false;
      }
      else if (!bottomHits && !topHits && t0Hits && !t1Hits) {
         state.Hits = false;
      }
      else if (!bottomHits && !topHits && !t0Hits && t1Hits) {
         state.Hits = false;
      }

      // case 0 - misses
      if (state.Hits) {
         // case 1 - hits only bounded cylinder

         if (!topHits && !bottomHits && (t0Hits || t1Hits)) {
            if (t0Hits && t0 <= t1) {
               state.TMin = t0;
               state.Normal = t0Normal;
               state.IntersectionPoint = objectSpaceRay.ScaleFromOrigin(t0);
            }
            else if (t1Hits && t1 <= t0) {
               state.TMin = t1;
               state.Normal = t1Normal;
               state.IntersectionPoint = objectSpaceRay.ScaleFromOrigin(t1);
            }
         }
         // case 2 - hits only discs
         else if (topHits && bottomHits && !t0Hits && !t1Hits) {
            if (tTop < tBottom) {
               state.TMin = tTop;
               state.Normal = topNormal;
               state.IntersectionPoint = objectSpaceRay.ScaleFromOrigin(tTop);
            }
            else {
               state.TMin = tBottom;
               state.Normal = bottomNormal;
               state.IntersectionPoint = objectSpaceRay.ScaleFromOrigin(tBottom);
            }
         }

         // case 3 - hits top disc and hits bounded cylinder once
         else if (topHits && !bottomHits) {
            if (t0Hits && !t1Hits) {
               if (t0 <= tTop) {
                  state.TMin = t0;
                  state.Normal = t0Normal;
                  state.IntersectionPoint = objectSpaceRay.ScaleFromOrigin(t0);
               } else {
                  state.TMin = tTop;
                  state.Normal = topNormal;
                  state.IntersectionPoint = objectSpaceRay.ScaleFromOrigin(tTop);
               }
            }
            else {
               if (t1 <= tTop) {
                  state.TMin = t1;
                  state.Normal = t1Normal;
                  state.IntersectionPoint = objectSpaceRay.ScaleFromOrigin(t1);
               } else {
                  state.TMin = tTop;
                  state.Normal = topNormal;
                  state.IntersectionPoint = objectSpaceRay.ScaleFromOrigin(tTop);
               }
            }
         }


         // case 4 - hits bottom disc and hits bounded cylinder once

         else if (!topHits && bottomHits) {
            if (t0Hits && !t1Hits) {
               if (t0 <= tBottom) {
                  state.TMin = t0;
                  state.Normal = t0Normal;
                  state.IntersectionPoint = objectSpaceRay.ScaleFromOrigin(t0);
               } else {
                  state.TMin = tBottom;
                  state.Normal = bottomNormal;
                  state.IntersectionPoint = objectSpaceRay.ScaleFromOrigin(tBottom);
               }
            }
            else {
               if (t1 <= tBottom) {
                  state.TMin = t1;
                  state.Normal = t1Normal;
                  state.IntersectionPoint = objectSpaceRay.ScaleFromOrigin(t1);
               } else {
                  state.TMin = tBottom;
                  state.Normal = topNormal;
                  state.IntersectionPoint = objectSpaceRay.ScaleFromOrigin(tBottom);
               }
            }
         }
         else {
            ;
         }

         // other stuff
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
   public Material GetMaterial() {
      return Material;
   }


}
