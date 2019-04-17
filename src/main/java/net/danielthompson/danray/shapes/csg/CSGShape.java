package net.danielthompson.danray.shapes.csg;

import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shapes.AbstractShape;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Ray;
import org.apache.commons.lang.NotImplementedException;

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

   public boolean Hits(Ray ray) {

      // get all hitpoints - in order
      List<Intersection> leftHitPoints = LeftShape.GetAllHitPoints(ray);
      List<Intersection> rightHitPoints = RightShape.GetAllHitPoints(ray);

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
                  return true;
               continue;
            }
            case Difference: {
               // if hp is on left and we're outside right, return it
               if (nextIntersection == leftIntersection && !RightShape.Inside(nextIntersection.Location))
                  return true;
               if (nextIntersection == rightIntersection && LeftShape.Inside(nextIntersection.Location))
                  return true;
               // if hp is on right and we're inside left, fliip normal and return it
               continue;
            }
            case Intersection: {
               // if hp is on left and we're inside right, return it
               if (nextIntersection == leftIntersection && RightShape.Inside(nextIntersection.Location))
                  return true;
               // if hp is on right and we're inside left, return it
               if (nextIntersection == rightIntersection && LeftShape.Inside(nextIntersection.Location))
                  return true;
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
      return null;
   }
}
