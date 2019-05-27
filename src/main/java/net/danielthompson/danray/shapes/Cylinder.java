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

      recalculateWorldBoundingBox();
   }

   public Cylinder(Transform[] transforms, Material material) {
      this(1.0f, 1.0f, transforms[1], transforms[0], material);
   }

   @Override
   public void recalculateWorldBoundingBox() {
      WorldBoundingBox = new BoundingBox(new Point3(-Radius, 0, -Radius), new Point3(Radius, Height, Radius));
      if (ObjectToWorld != null) {
         WorldBoundingBox = ObjectToWorld.apply(WorldBoundingBox);
      }
   }

   @Override
   public Intersection intersect(Ray worldSpaceRay) {
      Ray objectSpaceRay = worldSpaceRay;

      if (WorldToObject != null) {
         objectSpaceRay = WorldToObject.apply(worldSpaceRay);
      }

      Intersection state = new Intersection();

      // check for intersection with upper disk

      float tTop = (Height - objectSpaceRay.Origin.y) / (objectSpaceRay.Direction.y);

      Point3 topHitPoint = null;
      boolean topHits = false;
      Normal topNormal = null;

      if (objectSpaceRay.Direction.y != 0) {
         topHitPoint = objectSpaceRay.getPointAtT(tTop);

         topNormal = new Normal(0, 1, 0);

         if ((tTop > 0) && Math.sqrt(topHitPoint.x * topHitPoint.x + topHitPoint.z * topHitPoint.z) <= Radius) {
            topHits = true;
            state.hits = true;
         }
      }

      // check for intersection with lower disk

      float tBottom = (-objectSpaceRay.Origin.y) / (objectSpaceRay.Direction.y);

      Point3 bottomHitPoint = null;
      boolean bottomHits = false;
      Normal bottomNormal = null;

      if (objectSpaceRay.Direction.y != 0) {
         bottomHitPoint = objectSpaceRay.getPointAtT(tBottom);
         bottomNormal = new Normal(0, -1, 0);

         if ((tBottom > 0) && Math.sqrt(bottomHitPoint.x * bottomHitPoint.x + bottomHitPoint.z * bottomHitPoint.z) <= Radius) {
            bottomHits = true;
            state.hits = true;
         }
      }

      // check for intersection with cylinder

      float a = objectSpaceRay.Direction.x * objectSpaceRay.Direction.x + objectSpaceRay.Direction.z * objectSpaceRay.Direction.z;
      float b = 2 * (objectSpaceRay.Origin.x * objectSpaceRay.Direction.x + objectSpaceRay.Origin.z * objectSpaceRay.Direction.z);
      float c = objectSpaceRay.Origin.x * objectSpaceRay.Origin.x + objectSpaceRay.Origin.z * objectSpaceRay.Origin.z - (Radius * Radius);

      float discriminant = (b * b) - (4 * a * c);

      if (discriminant < 0) {
         state.hits = false;
         state.location = null;
         return state;
      }

      float root = (float) Math.sqrt(discriminant);

      float oneOverTwoA = .5f / a;

      float t0 = (-b + root) * oneOverTwoA;

      float t1 = (-b - root) * oneOverTwoA;

      boolean t0Hits = false;
      Normal t0Normal = null;

      if (t0 > 0.0) {
         float py = objectSpaceRay.Origin.y + t0 * objectSpaceRay.Direction.y;
         if ((py <= Height) && (py >= 0)) {
            t0Hits = true;
            state.hits = true;
            float px = objectSpaceRay.Origin.x + t0 * objectSpaceRay.Direction.x;
            float pz = objectSpaceRay.Origin.z + t0 * objectSpaceRay.Direction.z;
            t0Normal = new Normal(px, 0, pz);
         }
      }

      boolean t1Hits = false;
      Normal t1Normal = null;

      if (t1 > 0.0) {
         float py = objectSpaceRay.Origin.y + t1 * objectSpaceRay.Direction.y;
         if ((py <= Height) && (py >= 0)) {
            t1Hits = true;
            state.hits = true;
            float px = objectSpaceRay.Origin.x + t1 * objectSpaceRay.Direction.x;
            float pz = objectSpaceRay.Origin.z + t1 * objectSpaceRay.Direction.z;
            t1Normal = new Normal(px, 0, pz);
         }
      }

      if (bottomHits && !topHits && !t0Hits && !t1Hits) {
         state.hits = false;
      }
      else if (!bottomHits && topHits && !t0Hits && !t1Hits) {
         state.hits = false;
      }
      else if (!bottomHits && !topHits && t0Hits && !t1Hits) {
         state.hits = false;
      }
      else if (!bottomHits && !topHits && !t0Hits && t1Hits) {
         state.hits = false;
      }

      // case 0 - misses
      if (state.hits) {
         // case 1 - hits only bounded cylinder

         if (!topHits && !bottomHits && (t0Hits || t1Hits)) {
            if (t0Hits && t0 <= t1) {
               state.t = t0;
               state.normal = t0Normal;
               state.location = objectSpaceRay.scaleFromOrigin(t0);
            }
            else if (t1Hits && t1 <= t0) {
               state.t = t1;
               state.normal = t1Normal;
               state.location = objectSpaceRay.scaleFromOrigin(t1);
            }
         }
         // case 2 - hits only discs
         else if (topHits && bottomHits && !t0Hits && !t1Hits) {
            if (tTop < tBottom) {
               state.t = tTop;
               state.normal = topNormal;
               state.location = objectSpaceRay.scaleFromOrigin(tTop);
            }
            else {
               state.t = tBottom;
               state.normal = bottomNormal;
               state.location = objectSpaceRay.scaleFromOrigin(tBottom);
            }
         }

         // case 3 - hits top disc and hits bounded cylinder once
         else if (topHits && !bottomHits) {
            if (t0Hits && !t1Hits) {
               if (t0 <= tTop) {
                  state.t = t0;
                  state.normal = t0Normal;
                  state.location = objectSpaceRay.scaleFromOrigin(t0);
               } else {
                  state.t = tTop;
                  state.normal = topNormal;
                  state.location = objectSpaceRay.scaleFromOrigin(tTop);
               }
            }
            else {
               if (t1 <= tTop) {
                  state.t = t1;
                  state.normal = t1Normal;
                  state.location = objectSpaceRay.scaleFromOrigin(t1);
               } else {
                  state.t = tTop;
                  state.normal = topNormal;
                  state.location = objectSpaceRay.scaleFromOrigin(tTop);
               }
            }
         }


         // case 4 - hits bottom disc and hits bounded cylinder once

         else if (!topHits && bottomHits) {
            if (t0Hits && !t1Hits) {
               if (t0 <= tBottom) {
                  state.t = t0;
                  state.normal = t0Normal;
                  state.location = objectSpaceRay.scaleFromOrigin(t0);
               } else {
                  state.t = tBottom;
                  state.normal = bottomNormal;
                  state.location = objectSpaceRay.scaleFromOrigin(tBottom);
               }
            }
            else {
               if (t1 <= tBottom) {
                  state.t = t1;
                  state.normal = t1Normal;
                  state.location = objectSpaceRay.scaleFromOrigin(t1);
               } else {
                  state.t = tBottom;
                  state.normal = topNormal;
                  state.location = objectSpaceRay.scaleFromOrigin(tBottom);
               }
            }
         }
         else {
            ;
         }

         // other stuff
         if (ObjectToWorld != null) {
            state.location = ObjectToWorld.apply(state.location);
            state.normal = ObjectToWorld.apply(state.normal);
            if (ObjectToWorld.hasScale()) {
               state.t = worldSpaceRay.getTAtPoint(state.location);
            }
         }

         state.normal.normalize();
         state.shape = this;
      }
      return state;

   }

   @Override
   public List<Intersection> intersectAll(Ray ray) {
      return null;
   }
}