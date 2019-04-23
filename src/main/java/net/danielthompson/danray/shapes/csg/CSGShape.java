package net.danielthompson.danray.shapes.csg;

import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shapes.AbstractShape;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Transform;
import org.apache.commons.lang.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

public class CSGShape extends AbstractShape {

   public CSGOperation Operation;
   public CSGShape LeftShape;
   public CSGShape RightShape;

   public boolean Inside(Point p) {
      throw new NotImplementedException();
   }

   public CSGShape(Material material) {
      super(material);
   }

   public CSGShape(Transform[] transforms) {
      super(transforms, null);
   }

   public CSGShape(Transform objectToWorld, Transform worldToObject) {
      super(objectToWorld, worldToObject, null);
   }

   public boolean Hits(Ray worldSpaceRay) {

      Ray objectSpaceRay = worldSpaceRay;

      if (WorldToObject != null) {
         objectSpaceRay = WorldToObject.Apply(worldSpaceRay);
      }

      // get all hitpoints - in order
      List<Intersection> leftHitPoints = LeftShape.GetAllHitPoints(objectSpaceRay);
      List<Intersection> rightHitPoints = RightShape.GetAllHitPoints(objectSpaceRay);

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
               if (nextIntersection.Hits)
               {
                  worldSpaceRay.MinT = GetWorldSpaceT(worldSpaceRay, objectSpaceRay, nextIntersection.t);
                  return true;
               }
               continue;
            }
            case Difference: {
               // if hp is on left and we're outside right, return it
               if (nextIntersection == leftIntersection && !RightShape.Inside(nextIntersection.Location))
               {
                  worldSpaceRay.MinT = GetWorldSpaceT(worldSpaceRay, objectSpaceRay, nextIntersection.t);
                  return true;
               }
               if (nextIntersection == rightIntersection && LeftShape.Inside(nextIntersection.Location))
               {
                  worldSpaceRay.MinT = GetWorldSpaceT(worldSpaceRay, objectSpaceRay, nextIntersection.t);
                  return true;
               }

               // if hp is on right and we're inside left, fliip normal and return it
               continue;
            }
            case Intersection: {
               // if hp is on left and we're inside right, return it
               if (nextIntersection == leftIntersection && RightShape.Inside(nextIntersection.Location))
               {
                  worldSpaceRay.MinT = GetWorldSpaceT(worldSpaceRay, objectSpaceRay, nextIntersection.t);
                  return true;
               }
               // if hp is on right and we're inside left, return it
               if (nextIntersection == rightIntersection && LeftShape.Inside(nextIntersection.Location))
               {
                  worldSpaceRay.MinT = GetWorldSpaceT(worldSpaceRay, objectSpaceRay, nextIntersection.t);
                  return true;
               }
               continue;
            }
         }
      }
   }

   private float GetWorldSpaceT(Ray worldSpaceRay, Ray objectSpaceRay, float objectSpaceT) {
      float value = objectSpaceT;

      if (ObjectToWorld != null && ObjectToWorld.HasScale()) {
         Point objectSpaceIntersectionPoint = objectSpaceRay.GetPointAtT(objectSpaceT);
         Point worldSpaceIntersectionPoint = ObjectToWorld.Apply(objectSpaceIntersectionPoint);
         value = worldSpaceRay.GetTAtPoint(worldSpaceIntersectionPoint);
      }

      return value;
   }

   @Override
   public Intersection GetHitInfo(Ray worldSpaceRay) {
      Ray objectSpaceRay = worldSpaceRay;

      if (WorldToObject != null) {
         objectSpaceRay = WorldToObject.Apply(worldSpaceRay);
      }

      // get all hitpoints - in order
      List<Intersection> leftHitPoints = LeftShape.GetAllHitPoints(objectSpaceRay);
      List<Intersection> rightHitPoints = RightShape.GetAllHitPoints(objectSpaceRay);

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
               if (nextIntersection.Hits)
                  return nextIntersection;
               continue;
            }
            case Difference: {
               // if hp is on left and we're outside right, return it
               if (nextIntersection == leftIntersection && !RightShape.Inside(nextIntersection.Location))
                  return nextIntersection;
               // if hp is on right and we're inside left, flip normal and return it
               if (nextIntersection == rightIntersection && LeftShape.Inside(nextIntersection.Location)) {
                  //nextIntersection.Normal.Scale(-1);
                  return nextIntersection;
               }
               continue;
            }
            case Intersection: {
               // if hp is on left and we're inside right, return it
               if (nextIntersection == leftIntersection && RightShape.Inside(nextIntersection.Location))
                  return nextIntersection;
               // if hp is on right and we're inside left, return it
               if (nextIntersection == rightIntersection && LeftShape.Inside(nextIntersection.Location))
                  return nextIntersection;
               continue;
            }
         }
      }
   }

   @Override
   public void RecalculateWorldBoundingBox() {

   }

   @Override
   public List<Intersection> GetAllHitPoints(Ray ray) {
      // get all hitpoints - in order
      List<Intersection> leftHitPoints = LeftShape.GetAllHitPoints(ray);
      List<Intersection> rightHitPoints = RightShape.GetAllHitPoints(ray);

      List<Intersection> intersections = new ArrayList<>();

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

         intersections.add(nextIntersection);
      }

      return intersections;
   }
}
