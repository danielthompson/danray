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

   public final static Point point1 = new Point(0, 0, 0);
   public final static Point point2 = new Point(1, 1, 1);

   public Box(Transform objectToWorld, Transform worldToObject, Material material) {
      super(material);
      ObjectToWorld = objectToWorld;
      WorldToObject = worldToObject;

      RecalculateWorldBoundingBox();
   }

   public Box(Transform[] transforms, Material material) {
      this(transforms[0], transforms[1], material);
   }

   public Box(Point p1, Point p2, Material material)
   {
      super(material);
      throw new UnsupportedOperationException();
   }

   public Box(Point p1, Point p2, Material material, Transform objectToWorld, Transform worldToObject) {
      super(material);
      throw new UnsupportedOperationException();
   }

   @Override
   public void RecalculateWorldBoundingBox() {
      WorldBoundingBox = new BoundingBox(point1.clone(), point2.clone());

      if (ObjectToWorld != null) {
         WorldBoundingBox = ObjectToWorld.Apply(WorldBoundingBox);
      }
   }

   @Override
   public boolean Inside(Point worldSpacePoint) {
      Point objectSpacePoint = worldSpacePoint;

      if (WorldToObject != null) {
         objectSpacePoint = WorldToObject.Apply(worldSpacePoint);
      }

      return (
            point1.X <= objectSpacePoint.X
         && point1.Y <= objectSpacePoint.Y
         && point1.Z <= objectSpacePoint.Z
         && point2.X >= objectSpacePoint.X
         && point2.Y >= objectSpacePoint.Y
         && point2.Z >= objectSpacePoint.Z
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

      if (intersection.Hits) {
         if (ObjectToWorld != null && ObjectToWorld.HasScale()) {
            Point objectSpaceFirstIntersectionPoint = objectSpaceRay.GetPointAtT(intersection.t);
            Point worldSpaceFirstIntersectionPoint = ObjectToWorld.Apply(objectSpaceFirstIntersectionPoint);
            minT = worldSpaceRay.GetTAtPoint(worldSpaceFirstIntersectionPoint);

            //Point objectSpaceSecondIntersectionPoint = objectSpaceRay.GetPointAtT(intersection.TMax);
            //Point worldSpaceSecondIntersectionPoint = ObjectToWorld.Apply(objectSpaceSecondIntersectionPoint);
            //maxT = worldSpaceRay.GetTAtPoint(worldSpaceSecondIntersectionPoint);
         }

         worldSpaceRay.MinT = intersection.Hits && minT < worldSpaceRay.MinT ? minT : worldSpaceRay.MinT;
         //worldSpaceRay.MaxT = intersection.Hits && maxT < worldSpaceRay.MaxT? maxT : worldSpaceRay.MaxT;
      }

      return intersection.Hits;
   }

   private void FillIntersectionData(Intersection intersection, Ray objectSpaceRay, Ray worldSpaceRay) {
      if (intersection.Hits) {
         intersection.Shape = this;
         intersection.Location = objectSpaceRay.GetPointAtT(intersection.t);

         if (Constants.WithinEpsilon(intersection.Location.X, 0) || Constants.WithinEpsilon(intersection.Location.X, 1)) {
            intersection.u = intersection.Location.Y;
            intersection.v = intersection.Location.Z;
         }
         else if (Constants.WithinEpsilon(intersection.Location.Y, 0) || Constants.WithinEpsilon(intersection.Location.Y, 1)) {
            intersection.u = intersection.Location.Z;
            intersection.v = intersection.Location.X;
         }
         else if (Constants.WithinEpsilon(intersection.Location.Z, 0) || Constants.WithinEpsilon(intersection.Location.Z, 1)) {
            intersection.u = intersection.Location.X;
            intersection.v = intersection.Location.Y;
         }

         intersection.Normal = Constants.WithinEpsilon(point1.X, intersection.Location.X) ? new Normal(Constants.NegativeX) : intersection.Normal;
         intersection.Normal = Constants.WithinEpsilon(point1.Y, intersection.Location.Y) ? new Normal(Constants.NegativeY) : intersection.Normal;
         intersection.Normal = Constants.WithinEpsilon(point1.Z, intersection.Location.Z) ? new Normal(Constants.NegativeZ) : intersection.Normal;
         intersection.Normal = Constants.WithinEpsilon(point2.X, intersection.Location.X) ? new Normal(Constants.PositiveX) : intersection.Normal;
         intersection.Normal = Constants.WithinEpsilon(point2.Y, intersection.Location.Y) ? new Normal(Constants.PositiveY) : intersection.Normal;
         intersection.Normal = Constants.WithinEpsilon(point2.Z, intersection.Location.Z) ? new Normal(Constants.PositiveZ) : intersection.Normal;

         if (intersection.Normal == null) {
//            intersection.Location = objectSpaceRay.GetPointAtT(intersection.TMax);
//
//            intersection.Normal = Constants.WithinEpsilon(point1.X, intersection.Location.X) ? new Normal(Constants.NegativeX) : intersection.Normal;
//            intersection.Normal = Constants.WithinEpsilon(point1.Y, intersection.Location.Y) ? new Normal(Constants.NegativeY) : intersection.Normal;
//            intersection.Normal = Constants.WithinEpsilon(point1.Z, intersection.Location.Z) ? new Normal(Constants.NegativeZ) : intersection.Normal;
//            intersection.Normal = Constants.WithinEpsilon(point2.X, intersection.Location.X) ? new Normal(Constants.PositiveX) : intersection.Normal;
//            intersection.Normal = Constants.WithinEpsilon(point2.Y, intersection.Location.Y) ? new Normal(Constants.PositiveY) : intersection.Normal;
//            intersection.Normal = Constants.WithinEpsilon(point2.Z, intersection.Location.Z) ? new Normal(Constants.PositiveZ) : intersection.Normal;
//
//            if (intersection.Normal == null) {
            float smallest = Float.MAX_VALUE;

            float p1xDiff = point1.X - intersection.Location.X;
            smallest = p1xDiff <= smallest ? p1xDiff : smallest;
            intersection.Normal = p1xDiff <= smallest ? new Normal(Constants.NegativeX) : intersection.Normal;

            float p1yDiff = point1.Y - intersection.Location.Y;
            smallest = p1yDiff <= smallest ? p1yDiff : smallest;
            intersection.Normal = p1yDiff <= smallest ? new Normal(Constants.NegativeY) : intersection.Normal;

            float p1zDiff = point1.Z - intersection.Location.Z;
            smallest = p1zDiff <= smallest ? p1zDiff : smallest;
            intersection.Normal = p1zDiff <= smallest ? new Normal(Constants.NegativeZ) : intersection.Normal;

            float p2xDiff = point2.X - intersection.Location.X;
            smallest = p2xDiff <= smallest ? p2xDiff : smallest;
            intersection.Normal = p2xDiff <= smallest ? new Normal(Constants.PositiveX) : intersection.Normal;

            float p2yDiff = point2.Y - intersection.Location.Y;
            smallest = p2yDiff <= smallest ? p2yDiff : smallest;
            intersection.Normal = p2yDiff <= smallest ? new Normal(Constants.PositiveY) : intersection.Normal;

            float p2zDiff = point2.Z - intersection.Location.Z;
            smallest = p2zDiff <= smallest ? p2zDiff : smallest;
            intersection.Normal = p2zDiff <= smallest ? new Normal(Constants.PositiveZ) : intersection.Normal;

            if (intersection.Normal == null) {
               System.out.println("Bounding box intersection couldn't find normal...");

               if (Float.isNaN(p1xDiff)) {
                  System.out.println("nan");
               }
               System.out.println("point1: " + point1);
               System.out.println("point2: " + point2);
               System.out.println("hitpoint: " + intersection.Location);

               boolean isNan = Float.isNaN(objectSpaceRay.Direction.X);

               throw new NullPointerException();
               //}
            }
         }

         if (intersection.Normal.Dot(objectSpaceRay.Direction) > 0)
            intersection.Normal.Scale(-1);

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

   public Point[] getWorldPoints() {

      Point[] points = new Point[8];

      points[0] = new Point(0, 0, 0);
      points[1] = new Point(0, 0, 1);
      points[2] = new Point(0, 1, 0);
      points[3] = new Point(0, 1, 1);
      points[4] = new Point(1, 0, 0);
      points[5] = new Point(1, 0, 1);
      points[6] = new Point(1, 1, 0);
      points[7] = new Point(1, 1, 1);

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
