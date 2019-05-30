package net.danielthompson.danray.shapes.csg;

import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shapes.AbstractShape;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.BoundingBox;
import net.danielthompson.danray.structures.Point3;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Transform;

import java.util.ArrayList;
import java.util.List;

public class CSGShape extends AbstractShape {

   public CSGOperation Operation;
   public CSGShape LeftShape;
   public CSGShape RightShape;

   public boolean inside(final Point3 worldSpacePoint) {
      Point3 localSpacePoint = worldSpacePoint;
      if (WorldToObject != null) {
         localSpacePoint = WorldToObject.apply(worldSpacePoint);
      }

      final boolean leftInside = LeftShape.inside(localSpacePoint);
      final boolean rightInside = RightShape.inside(localSpacePoint);

      switch (Operation) {
         case Union:
            return leftInside || rightInside;
         case Difference:
            return leftInside && !rightInside;
         case Intersection:
            return leftInside && rightInside;
      }

      return false;
   }

   public CSGShape(final Material material) {
      super(material);
   }

   public CSGShape(final Transform[] transforms) {
      super(transforms, null);
   }

   public CSGShape(final Transform objectToWorld, final Transform worldToObject) {
      super(objectToWorld, worldToObject, null);
   }

   public boolean hits(final Ray worldSpaceRay) {
      Ray objectSpaceRay = worldSpaceRay;

      if (WorldToObject != null) {
         objectSpaceRay = WorldToObject.apply(worldSpaceRay);
      }

      // get all hitpoints - in order
      final List<Intersection> leftHitPoints = LeftShape.intersectAll(objectSpaceRay);
      final List<Intersection> rightHitPoints = RightShape.intersectAll(objectSpaceRay);

      int leftIndex = 0;
      int rightIndex = 0;

      while (true) {

         Intersection nextIntersection = null;
         Intersection leftIntersection = null;
         Intersection rightIntersection = null;

         if (leftHitPoints.size() > 0 && leftIndex < leftHitPoints.size()) {
            leftIntersection = leftHitPoints.get(leftIndex);
         }
         if (rightHitPoints.size() > 0 && rightIndex < rightHitPoints.size()) {
            rightIntersection = rightHitPoints.get(rightIndex);
         }

         if (leftIntersection != null && rightIntersection != null) {
            if (leftIntersection.t < rightIntersection.t) {
               nextIntersection = leftIntersection;
               leftIndex++;
            }
            else {
               nextIntersection = rightIntersection;
               rightIndex++;
            }
         }
         else if (leftIntersection != null) {
            nextIntersection = leftIntersection;
            leftIndex++;
         }
         else if (rightIntersection != null) {
            nextIntersection = rightIntersection;
            rightIndex++;
         }

         if (nextIntersection == null)
            return false;

         // foreach hitpoint
         switch (Operation) {
            case Union: {
               final boolean rightInside = RightShape.inside(nextIntersection.location);

               // if hp is on left and we're outside right, return it
               if (nextIntersection == leftIntersection && !rightInside)
               {
                  worldSpaceRay.MinT = nextIntersection.t;
                  return true;
               }

               final boolean leftInside = LeftShape.inside(nextIntersection.location);

               // if hp is on right and we're inside left, fliip normal and return it
               if (nextIntersection == rightIntersection && !leftInside /*&& !rightInside*/)
               {
                  worldSpaceRay.MinT = nextIntersection.t;
                  return true;
               }
               continue;
            }
            case Difference: {

               final boolean rightInside = RightShape.inside(nextIntersection.location);

               // if hp is on left and we're outside right, return it
               if (nextIntersection == leftIntersection && !rightInside)
               {
                  worldSpaceRay.MinT = nextIntersection.t;
                  return true;
               }

               final boolean leftInside = LeftShape.inside(nextIntersection.location);

               // if hp is on right and we're inside left, fliip normal and return it
               if (nextIntersection == rightIntersection && leftInside /*&& !rightInside*/)
               {
                  worldSpaceRay.MinT = nextIntersection.t;
                  return true;
               }
               continue;
            }
            case Intersection: {

               final boolean rightInside = RightShape.inside(nextIntersection.location);

               // if hp is on left and we're inside right, return it
               if (nextIntersection == leftIntersection && rightInside)
               {
                  worldSpaceRay.MinT = nextIntersection.t;
                  return true;
               }

               final boolean leftInside = LeftShape.inside(nextIntersection.location);

               // if hp is on right and we're inside left, return it
               if (nextIntersection == rightIntersection && leftInside)
               {
                  worldSpaceRay.MinT = nextIntersection.t;
                  return true;
               }
            }
         }
      }
   }

   @Override
   public Intersection intersect(final Ray worldSpaceRay) {
      Ray objectSpaceRay = worldSpaceRay;

      if (WorldToObject != null) {
         objectSpaceRay = WorldToObject.apply(worldSpaceRay);
      }

      // get all hitpoints - in order
      final List<Intersection> leftHitPoints = LeftShape.intersectAll(objectSpaceRay);
      final List<Intersection> rightHitPoints = RightShape.intersectAll(objectSpaceRay);

      int leftIndex = 0;
      int rightIndex = 0;

      while (true) {
         Intersection nextIntersection = null;
         Intersection leftIntersection = null;
         Intersection rightIntersection = null;

         if (leftHitPoints.size() > 0 && leftIndex < leftHitPoints.size()) {
            leftIntersection = leftHitPoints.get(leftIndex);
         }
         if (rightHitPoints.size() > 0 && rightIndex < rightHitPoints.size()) {
            rightIntersection = rightHitPoints.get(rightIndex);
         }

         if (leftIntersection != null && rightIntersection != null) {
            if (leftIntersection.t < rightIntersection.t) {
               nextIntersection = leftIntersection;
               leftIndex++;
            }
            else {
               nextIntersection = rightIntersection;
               rightIndex++;
            }
         }
         else if (leftIntersection != null) {
            nextIntersection = leftIntersection;
            leftIndex++;
         }
         else if (rightIntersection != null) {
            nextIntersection = rightIntersection;
            rightIndex++;
         }

         if (nextIntersection == null)
            return null;

         // foreach hitpoint
         switch (Operation) {
            case Union: {
               final boolean rightInside = RightShape.inside(nextIntersection.location);

               // if hp is on left and we're outside right, return it
               if (nextIntersection == leftIntersection && !rightInside)
               {
                  toWorldSpace(nextIntersection);
                  return nextIntersection;
               }

               final boolean leftInside = LeftShape.inside(nextIntersection.location);

               // if hp is on right and we're ont inside left
               if (nextIntersection == rightIntersection && !leftInside)
               {
                  toWorldSpace(nextIntersection);
                  //nextIntersection.normal.scale(-1);
                  return nextIntersection;
               }
               continue;
            }
            case Difference: {

               final boolean rightInside = RightShape.inside(nextIntersection.location);

               // if hp is on left and we're outside right, return it
               if (nextIntersection == leftIntersection && !rightInside)
               {
                  toWorldSpace(nextIntersection);
                  return nextIntersection;
               }

               final boolean leftInside = LeftShape.inside(nextIntersection.location);

               // if hp is on right and we're inside left
               if (nextIntersection == rightIntersection && leftInside)
               {
                  toWorldSpace(nextIntersection);
                  //nextIntersection.normal.scale(-1);
                  return nextIntersection;
               }
               continue;
            }
            case Intersection: {

               final boolean rightInside = RightShape.inside(nextIntersection.location);

               // if hp is on left and we're inside right, return it
               if (nextIntersection == leftIntersection && rightInside)
               {
                  toWorldSpace(nextIntersection);
                  return nextIntersection;
               }

               final boolean leftInside = LeftShape.inside(nextIntersection.location);

               // if hp is on right and we're inside left, return it
               if (nextIntersection == rightIntersection && leftInside)
               {
                  toWorldSpace(nextIntersection);
                  return nextIntersection;
               }
               continue;
            }
         }
      }
   }

   @Override
   public void recalculateWorldBoundingBox() {
      BoundingBox left = LeftShape.WorldBoundingBox;
      BoundingBox right = RightShape.WorldBoundingBox;

      switch (Operation) {
         case Union: {

         }
         case Intersection: {

         }
         case Difference:

      }


   }

   @Override
   public List<Intersection> intersectAll(final Ray worldSpaceRay) {
      Ray objectSpaceRay = worldSpaceRay;

      if (WorldToObject != null) {
         objectSpaceRay = WorldToObject.apply(worldSpaceRay);
      }

      // get all hitpoints - in order
      final List<Intersection> leftHitPoints = LeftShape.intersectAll(objectSpaceRay);
      final List<Intersection> rightHitPoints = RightShape.intersectAll(objectSpaceRay);

      final List<Intersection> intersections = new ArrayList<>();

      int leftIndex = 0;
      int rightIndex = 0;

      while (true) {

         Intersection nextIntersection = null;
         Intersection leftIntersection = null;
         Intersection rightIntersection = null;

         if (leftHitPoints.size() > 0 && leftIndex < leftHitPoints.size()) {
            leftIntersection = leftHitPoints.get(leftIndex);
         }
         if (rightHitPoints.size() > 0 && rightIndex < rightHitPoints.size()) {
            rightIntersection = rightHitPoints.get(rightIndex);
         }

         if (leftIntersection != null && rightIntersection != null) {
            if (leftIntersection.t < rightIntersection.t) {
               nextIntersection = leftIntersection;
               leftIndex++;
            }
            else {
               nextIntersection = rightIntersection;
               rightIndex++;
            }
         }
         else if (leftIntersection != null) {
            nextIntersection = leftIntersection;
            leftIndex++;
         }
         else if (rightIntersection != null) {
            nextIntersection = rightIntersection;
            rightIndex++;
         }

         if (nextIntersection == null)
            break;

         // foreach hitpoint
         switch (Operation) {
            case Union: {
               final boolean rightInside = RightShape.inside(nextIntersection.location);

               // if hp is on left and we're outside right, return it
               if (nextIntersection == leftIntersection && !rightInside)
               {
                  toWorldSpace(nextIntersection);
                  intersections.add(nextIntersection);
               }

               final boolean leftInside = LeftShape.inside(nextIntersection.location);

               // if hp is on right and we're not inside left
               if (nextIntersection == rightIntersection && !leftInside)
               {
                  toWorldSpace(nextIntersection);
                  intersections.add(nextIntersection);
               }
               continue;
            }
            case Difference: {

               final boolean rightInside = RightShape.inside(nextIntersection.location);

               // if hp is on left and we're outside right, return it
               if (nextIntersection == leftIntersection && !rightInside)
               {
                  toWorldSpace(nextIntersection);
                  intersections.add(nextIntersection);
               }

               final boolean leftInside = LeftShape.inside(nextIntersection.location);

               // if hp is on right and we're inside left, return it
               if (nextIntersection == rightIntersection && leftInside)
               {
                  toWorldSpace(nextIntersection);
                  intersections.add(nextIntersection);
               }
               continue;
            }
            case Intersection: {

               final boolean rightInside = RightShape.inside(nextIntersection.location);

               // if hp is on left and we're inside right, return it
               if (nextIntersection == leftIntersection && rightInside)
               {
                  toWorldSpace(nextIntersection);
                  intersections.add(nextIntersection);
               }

               final boolean leftInside = LeftShape.inside(nextIntersection.location);

               // if hp is on right and we're inside left, return it
               if (nextIntersection == rightIntersection && leftInside)
               {
                  toWorldSpace(nextIntersection);
                  intersections.add(nextIntersection);
               }
               continue;
            }
         }
      }

      return intersections;
   }
}
