package net.danielthompson.danray.shapes;

import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.*;

import java.util.List;

/**
 * Created by daniel on 3/2/15.
 */
public class Cylinder extends AbstractShape {

   public float Radius;
   public float Height;

   public Cylinder(float radius, float height, Transform worldToObject, Transform objectToWorld, Material material) {
      super(material);
      Radius = radius;
      Height = height;
      WorldToObject = worldToObject;
      ObjectToWorld = objectToWorld;

      RecalculateWorldBoundingBox();
   }

   public Cylinder(Transform[] transforms, Material material) {
      this(1.0f, 1.0f, transforms[1], transforms[0], material);
   }

   @Override
   public void RecalculateWorldBoundingBox() {
      WorldBoundingBox = new BoundingBox(new Point(-Radius, 0, -Radius), new Point(Radius, Height, Radius));
      if (ObjectToWorld != null) {
         WorldBoundingBox = ObjectToWorld.Apply(WorldBoundingBox);
      }
   }

   @Override
   public Intersection GetHitInfo(Ray worldSpaceRay) {
      Ray objectSpaceRay = worldSpaceRay;

      if (WorldToObject != null) {
         objectSpaceRay = WorldToObject.Apply(worldSpaceRay);
      }

      Intersection state = new Intersection();

      // check for intersection with upper disk

      float tTop = (Height - objectSpaceRay.Origin.Y) / (objectSpaceRay.Direction.Y);

      Point topHitPoint = null;
      boolean topHits = false;
      Normal topNormal = null;

      if (objectSpaceRay.Direction.Y != 0) {
         topHitPoint = objectSpaceRay.GetPointAtT(tTop);

         topNormal = new Normal(0, 1, 0);

         if ((tTop > 0) && Math.sqrt(topHitPoint.X * topHitPoint.X + topHitPoint.Z * topHitPoint.Z) <= Radius) {
            topHits = true;
            state.Hits = true;
         }
      }

      // check for intersection with lower disk

      float tBottom = (-objectSpaceRay.Origin.Y) / (objectSpaceRay.Direction.Y);

      Point bottomHitPoint = null;
      boolean bottomHits = false;
      Normal bottomNormal = null;

      if (objectSpaceRay.Direction.Y != 0) {
         bottomHitPoint = objectSpaceRay.GetPointAtT(tBottom);
         bottomNormal = new Normal(0, -1, 0);

         if ((tBottom > 0) && Math.sqrt(bottomHitPoint.X * bottomHitPoint.X + bottomHitPoint.Z * bottomHitPoint.Z) <= Radius) {
            bottomHits = true;
            state.Hits = true;
         }
      }

      // check for intersection with cylinder

      float a = objectSpaceRay.Direction.X * objectSpaceRay.Direction.X + objectSpaceRay.Direction.Z * objectSpaceRay.Direction.Z;
      float b = 2 * (objectSpaceRay.Origin.X * objectSpaceRay.Direction.X + objectSpaceRay.Origin.Z * objectSpaceRay.Direction.Z);
      float c = objectSpaceRay.Origin.X * objectSpaceRay.Origin.X + objectSpaceRay.Origin.Z * objectSpaceRay.Origin.Z - (Radius * Radius);

      float discriminant = (b * b) - (4 * a * c);

      if (discriminant < 0) {
         state.Hits = false;
         state.Location = null;
         return state;
      }

      float root = (float) Math.sqrt(discriminant);

      float oneOverTwoA = .5f / a;

      float t0 = (-b + root) * oneOverTwoA;

      float t1 = (-b - root) * oneOverTwoA;

      boolean t0Hits = false;
      Normal t0Normal = null;

      if (t0 > 0.0) {
         float py = objectSpaceRay.Origin.Y + t0 * objectSpaceRay.Direction.Y;
         if ((py <= Height) && (py >= 0)) {
            t0Hits = true;
            state.Hits = true;
            float px = objectSpaceRay.Origin.X + t0 * objectSpaceRay.Direction.X;
            float pz = objectSpaceRay.Origin.Z + t0 * objectSpaceRay.Direction.Z;
            t0Normal = new Normal(px, 0, pz);
         }
      }

      boolean t1Hits = false;
      Normal t1Normal = null;

      if (t1 > 0.0) {
         float py = objectSpaceRay.Origin.Y + t1 * objectSpaceRay.Direction.Y;
         if ((py <= Height) && (py >= 0)) {
            t1Hits = true;
            state.Hits = true;
            float px = objectSpaceRay.Origin.X + t1 * objectSpaceRay.Direction.X;
            float pz = objectSpaceRay.Origin.Z + t1 * objectSpaceRay.Direction.Z;
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
         // case 1 - Hits only bounded cylinder

         if (!topHits && !bottomHits && (t0Hits || t1Hits)) {
            if (t0Hits && t0 <= t1) {
               state.t = t0;
               state.Normal = t0Normal;
               state.Location = objectSpaceRay.ScaleFromOrigin(t0);
            }
            else if (t1Hits && t1 <= t0) {
               state.t = t1;
               state.Normal = t1Normal;
               state.Location = objectSpaceRay.ScaleFromOrigin(t1);
            }
         }
         // case 2 - Hits only discs
         else if (topHits && bottomHits && !t0Hits && !t1Hits) {
            if (tTop < tBottom) {
               state.t = tTop;
               state.Normal = topNormal;
               state.Location = objectSpaceRay.ScaleFromOrigin(tTop);
            }
            else {
               state.t = tBottom;
               state.Normal = bottomNormal;
               state.Location = objectSpaceRay.ScaleFromOrigin(tBottom);
            }
         }

         // case 3 - Hits top disc and Hits bounded cylinder once
         else if (topHits && !bottomHits) {
            if (t0Hits && !t1Hits) {
               if (t0 <= tTop) {
                  state.t = t0;
                  state.Normal = t0Normal;
                  state.Location = objectSpaceRay.ScaleFromOrigin(t0);
               } else {
                  state.t = tTop;
                  state.Normal = topNormal;
                  state.Location = objectSpaceRay.ScaleFromOrigin(tTop);
               }
            }
            else {
               if (t1 <= tTop) {
                  state.t = t1;
                  state.Normal = t1Normal;
                  state.Location = objectSpaceRay.ScaleFromOrigin(t1);
               } else {
                  state.t = tTop;
                  state.Normal = topNormal;
                  state.Location = objectSpaceRay.ScaleFromOrigin(tTop);
               }
            }
         }


         // case 4 - Hits bottom disc and Hits bounded cylinder once

         else if (!topHits && bottomHits) {
            if (t0Hits && !t1Hits) {
               if (t0 <= tBottom) {
                  state.t = t0;
                  state.Normal = t0Normal;
                  state.Location = objectSpaceRay.ScaleFromOrigin(t0);
               } else {
                  state.t = tBottom;
                  state.Normal = bottomNormal;
                  state.Location = objectSpaceRay.ScaleFromOrigin(tBottom);
               }
            }
            else {
               if (t1 <= tBottom) {
                  state.t = t1;
                  state.Normal = t1Normal;
                  state.Location = objectSpaceRay.ScaleFromOrigin(t1);
               } else {
                  state.t = tBottom;
                  state.Normal = topNormal;
                  state.Location = objectSpaceRay.ScaleFromOrigin(tBottom);
               }
            }
         }
         else {
            ;
         }

         // other stuff
         if (ObjectToWorld != null) {
            state.Location = ObjectToWorld.Apply(state.Location);
            state.Normal = ObjectToWorld.Apply(state.Normal);
            if (ObjectToWorld.HasScale()) {
               state.t = worldSpaceRay.GetTAtPoint(state.Location);
            }
         }

         state.Normal.Normalize();
         state.Shape = this;
      }
      return state;

   }

   @Override
   public List<Intersection> GetAllHitPoints(Ray ray) {
      return null;
   }
}