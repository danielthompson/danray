package net.danielthompson.danray.shapes;

import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shapes.csg.CSGShape;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.*;

import java.util.List;


/**
 * Created by daniel on 2/17/14.
 */
public class Box extends CSGShape {

   public final static Point3 point1 = new Point3(0, 0, 0);
   public final static Point3 point2 = new Point3(1, 1, 1);

   public Box(Transform objectToWorld, Transform worldToObject, Material material) {
      super(material);
      ObjectToWorld = objectToWorld;
      WorldToObject = worldToObject;

      RecalculateWorldBoundingBox();
   }

   public Box(Transform[] transforms, Material material) {
      this(transforms[0], transforms[1], material);
   }

   public Box(Point3 p1, Point3 p2, Material material)
   {
      super(material);
      throw new UnsupportedOperationException();
   }

   public Box(Point3 p1, Point3 p2, Material material, Transform objectToWorld, Transform worldToObject) {
      super(material);
      throw new UnsupportedOperationException();
   }

   @Override
   public void RecalculateWorldBoundingBox() {
      WorldBoundingBox = new BoundingBox(point1, point2);

      if (ObjectToWorld != null) {
         WorldBoundingBox = ObjectToWorld.Apply(WorldBoundingBox);
      }
   }

   @Override
   public boolean Inside(Point3 worldSpacePoint) {
      Point3 objectSpacePoint = worldSpacePoint;

      if (WorldToObject != null) {
         objectSpacePoint = WorldToObject.Apply(worldSpacePoint);
      }

      return (
            point1.x <= objectSpacePoint.x
         && point1.y <= objectSpacePoint.y
         && point1.z <= objectSpacePoint.z
         && point2.x >= objectSpacePoint.x
         && point2.y >= objectSpacePoint.y
         && point2.z >= objectSpacePoint.z
      );
   }

   @Override
   public boolean Hits(Ray worldSpaceRay) {
      Ray objectSpaceRay = worldSpaceRay;

      if (WorldToObject != null) {
         objectSpaceRay = WorldToObject.Apply(worldSpaceRay);
      }

      Intersection intersection = BoundingBox.GetHitInfoNew(point1, point2, objectSpaceRay);

      float minT = intersection.t;
      //float maxT = intersection.TMax;

      if (intersection.hits) {
         if (ObjectToWorld != null && ObjectToWorld.HasScale()) {
            Point3 objectSpaceFirstIntersectionPoint = objectSpaceRay.GetPointAtT(intersection.t);
            Point3 worldSpaceFirstIntersectionPoint = ObjectToWorld.Apply(objectSpaceFirstIntersectionPoint);
            minT = worldSpaceRay.GetTAtPoint(worldSpaceFirstIntersectionPoint);

            //Point objectSpaceSecondIntersectionPoint = objectSpaceRay.GetPointAtT(intersection.TMax);
            //Point worldSpaceSecondIntersectionPoint = ObjectToWorld.Apply(objectSpaceSecondIntersectionPoint);
            //maxT = worldSpaceRay.GetTAtPoint(worldSpaceSecondIntersectionPoint);
         }

         worldSpaceRay.MinT = intersection.hits && minT < worldSpaceRay.MinT ? minT : worldSpaceRay.MinT;
         //worldSpaceRay.MaxT = intersection.hits && maxT < worldSpaceRay.MaxT? maxT : worldSpaceRay.MaxT;
      }

      return intersection.hits;
   }

   private void FillIntersectionData(Intersection intersection, Ray objectSpaceRay, Ray worldSpaceRay) {
      if (intersection.hits) {
         intersection.shape = this;
         intersection.location = objectSpaceRay.GetPointAtT(intersection.t);

         if (Constants.WithinEpsilon(intersection.location.x, 0) || Constants.WithinEpsilon(intersection.location.x, 1)) {
            intersection.u = intersection.location.y;
            intersection.v = intersection.location.z;
         }
         else if (Constants.WithinEpsilon(intersection.location.y, 0) || Constants.WithinEpsilon(intersection.location.y, 1)) {
            intersection.u = intersection.location.z;
            intersection.v = intersection.location.x;
         }
         else if (Constants.WithinEpsilon(intersection.location.z, 0) || Constants.WithinEpsilon(intersection.location.z, 1)) {
            intersection.u = intersection.location.x;
            intersection.v = intersection.location.y;
         }

         intersection.normal = Constants.WithinEpsilon(point1.x, intersection.location.x) ? new Normal(Constants.NegativeX) : intersection.normal;
         intersection.normal = Constants.WithinEpsilon(point1.y, intersection.location.y) ? new Normal(Constants.NegativeY) : intersection.normal;
         intersection.normal = Constants.WithinEpsilon(point1.z, intersection.location.z) ? new Normal(Constants.NegativeZ) : intersection.normal;
         intersection.normal = Constants.WithinEpsilon(point2.x, intersection.location.x) ? new Normal(Constants.PositiveX) : intersection.normal;
         intersection.normal = Constants.WithinEpsilon(point2.y, intersection.location.y) ? new Normal(Constants.PositiveY) : intersection.normal;
         intersection.normal = Constants.WithinEpsilon(point2.z, intersection.location.z) ? new Normal(Constants.PositiveZ) : intersection.normal;

         if (intersection.normal == null) {
//            intersection.location = objectSpaceRay.GetPointAtT(intersection.TMax);
//
//            intersection.normal = Constants.WithinEpsilon(point1.x, intersection.location.x) ? new normal(Constants.NegativeX) : intersection.normal;
//            intersection.normal = Constants.WithinEpsilon(point1.y, intersection.location.y) ? new normal(Constants.NegativeY) : intersection.normal;
//            intersection.normal = Constants.WithinEpsilon(point1.z, intersection.location.z) ? new normal(Constants.NegativeZ) : intersection.normal;
//            intersection.normal = Constants.WithinEpsilon(point2.x, intersection.location.x) ? new normal(Constants.PositiveX) : intersection.normal;
//            intersection.normal = Constants.WithinEpsilon(point2.y, intersection.location.y) ? new normal(Constants.PositiveY) : intersection.normal;
//            intersection.normal = Constants.WithinEpsilon(point2.z, intersection.location.z) ? new normal(Constants.PositiveZ) : intersection.normal;
//
//            if (intersection.normal == null) {
            float smallest = Float.MAX_VALUE;

            float p1xDiff = point1.x - intersection.location.x;
            smallest = p1xDiff <= smallest ? p1xDiff : smallest;
            intersection.normal = p1xDiff <= smallest ? new Normal(Constants.NegativeX) : intersection.normal;

            float p1yDiff = point1.y - intersection.location.y;
            smallest = p1yDiff <= smallest ? p1yDiff : smallest;
            intersection.normal = p1yDiff <= smallest ? new Normal(Constants.NegativeY) : intersection.normal;

            float p1zDiff = point1.z - intersection.location.z;
            smallest = p1zDiff <= smallest ? p1zDiff : smallest;
            intersection.normal = p1zDiff <= smallest ? new Normal(Constants.NegativeZ) : intersection.normal;

            float p2xDiff = point2.x - intersection.location.x;
            smallest = p2xDiff <= smallest ? p2xDiff : smallest;
            intersection.normal = p2xDiff <= smallest ? new Normal(Constants.PositiveX) : intersection.normal;

            float p2yDiff = point2.y - intersection.location.y;
            smallest = p2yDiff <= smallest ? p2yDiff : smallest;
            intersection.normal = p2yDiff <= smallest ? new Normal(Constants.PositiveY) : intersection.normal;

            float p2zDiff = point2.z - intersection.location.z;
            smallest = p2zDiff <= smallest ? p2zDiff : smallest;
            intersection.normal = p2zDiff <= smallest ? new Normal(Constants.PositiveZ) : intersection.normal;

            if (intersection.normal == null) {
               System.out.println("Bounding box intersection couldn't find normal...");

               if (Float.isNaN(p1xDiff)) {
                  System.out.println("nan");
               }
               System.out.println("point1: " + point1);
               System.out.println("point2: " + point2);
               System.out.println("hitpoint: " + intersection.location);

               boolean isNan = Float.isNaN(objectSpaceRay.Direction.x);

               intersection.normal = new Normal(Constants.PositiveZ);
               //}
            }
         }

         if (intersection.normal.Dot(objectSpaceRay.Direction) > 0)
            intersection.normal.Scale(-1);

         CalculateTangents(intersection);

         ToWorldSpace(intersection, worldSpaceRay);
      }
   }

   @Override
   public Intersection GetHitInfo(Ray worldSpaceRay) {

      Ray objectSpaceRay = worldSpaceRay;

      if (WorldToObject != null) {
         objectSpaceRay = WorldToObject.Apply(worldSpaceRay);
      }

      Intersection intersection = BoundingBox.GetHitInfoNew(point1, point2, objectSpaceRay);


      // TODO fix this garbage with this some boundary testing

      FillIntersectionData(intersection, objectSpaceRay, worldSpaceRay);



      return intersection;
   }

   @Override
   public List<Intersection> GetAllHitPoints(Ray worldSpaceRay) {

      Ray objectSpaceRay = worldSpaceRay;

      if (WorldToObject != null) {
         objectSpaceRay = WorldToObject.Apply(worldSpaceRay);
      }

      List<Intersection> intersections = BoundingBox.GetBothHitInfo(point1, point2, objectSpaceRay);
      for (Intersection intersection : intersections) {
         FillIntersectionData(intersection, objectSpaceRay, worldSpaceRay);
      }

      return intersections;
   }

   public String toString() {
      return ID + "";
   }

   public Point3[] getWorldPoints() {

      Point3[] points = new Point3[8];

      points[0] = new Point3(0, 0, 0);
      points[1] = new Point3(0, 0, 1);
      points[2] = new Point3(0, 1, 0);
      points[3] = new Point3(0, 1, 1);
      points[4] = new Point3(1, 0, 0);
      points[5] = new Point3(1, 0, 1);
      points[6] = new Point3(1, 1, 0);
      points[7] = new Point3(1, 1, 1);

      ObjectToWorld.ApplyInPlace(points[0]);
      ObjectToWorld.ApplyInPlace(points[1]);
      ObjectToWorld.ApplyInPlace(points[2]);
      ObjectToWorld.ApplyInPlace(points[3]);
      ObjectToWorld.ApplyInPlace(points[4]);
      ObjectToWorld.ApplyInPlace(points[5]);
      ObjectToWorld.ApplyInPlace(points[6]);
      ObjectToWorld.ApplyInPlace(points[7]);

      return points;
   }

}
