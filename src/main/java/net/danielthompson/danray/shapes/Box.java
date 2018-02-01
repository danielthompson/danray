package net.danielthompson.danray.shapes;

import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.*;


/**
 * Created by daniel on 2/17/14.
 */
public class Box extends AbstractShape {

   public final static Point point1 = new Point(0, 0, 0);
   public final static Point point2 = new Point(1, 1, 1);

   private final static Normal _negativeX = new Normal(-1, 0, 0);
   private final static Normal _negativeY = new Normal(0, -1, 0);
   private final static Normal _negativeZ = new Normal(0, 0, -1);
   private final static Normal _positiveX = new Normal(1, 0, 0);
   private final static Normal _positiveY = new Normal(0, 1, 0);
   private final static Normal _positiveZ = new Normal(0, 0, 1);

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
   public boolean hits(Ray worldSpaceRay) {
      Ray objectSpaceRay = worldSpaceRay;

      if (WorldToObject != null) {
         objectSpaceRay = WorldToObject.Apply(worldSpaceRay);
      }

      IntersectionState state = BoundingBox.GetHitInfoNew(point1, point2, objectSpaceRay);

      float minT = state.TMin;
      float maxT = state.TMax;

      if (state.Hits) {
         if (ObjectToWorld != null && ObjectToWorld.HasScale()) {
            Point objectSpaceFirstIntersectionPoint = objectSpaceRay.GetPointAtT(state.TMin);
            Point worldSpaceFirstIntersectionPoint = ObjectToWorld.Apply(objectSpaceFirstIntersectionPoint);
            minT = worldSpaceRay.GetTAtPoint(worldSpaceFirstIntersectionPoint);

            Point objectSpaceSecondIntersectionPoint = objectSpaceRay.GetPointAtT(state.TMax);
            Point worldSpaceSecondIntersectionPoint = ObjectToWorld.Apply(objectSpaceSecondIntersectionPoint);
            maxT = worldSpaceRay.GetTAtPoint(worldSpaceSecondIntersectionPoint);
         }

         worldSpaceRay.MinT = state.Hits && minT < worldSpaceRay.MinT ? minT : worldSpaceRay.MinT;
         worldSpaceRay.MaxT = state.Hits && maxT < worldSpaceRay.MaxT? maxT : worldSpaceRay.MaxT;
      }

      return state.Hits;
   }

   @Override
   public IntersectionState getHitInfo(Ray worldSpaceRay) {

      Ray objectSpaceRay = worldSpaceRay;

      if (WorldToObject != null) {
         objectSpaceRay = WorldToObject.Apply(worldSpaceRay);
      }

      IntersectionState state = BoundingBox.GetHitInfoNew(point1, point2, objectSpaceRay);

      if (state.Hits) {
         state.Shape = this;
         state.IntersectionPoint = objectSpaceRay.GetPointAtT(state.TMin);

         state.Normal = Constants.WithinEpsilon(point1.X, state.IntersectionPoint.X) ? _negativeX : state.Normal;
         state.Normal = Constants.WithinEpsilon(point1.Y, state.IntersectionPoint.Y) ? _negativeY : state.Normal;
         state.Normal = Constants.WithinEpsilon(point1.Z, state.IntersectionPoint.Z) ? _negativeZ : state.Normal;
         state.Normal = Constants.WithinEpsilon(point2.X, state.IntersectionPoint.X) ? _positiveX : state.Normal;
         state.Normal = Constants.WithinEpsilon(point2.Y, state.IntersectionPoint.Y) ? _positiveY : state.Normal;
         state.Normal = Constants.WithinEpsilon(point2.Z, state.IntersectionPoint.Z) ? _positiveZ : state.Normal;
         /*
         if (Constants.WithinEpsilon(point1.X, state.IntersectionPoint.X))
            state.Normal = _negativeX;
         else if (Constants.WithinEpsilon(point1.Y, state.IntersectionPoint.Y))
            state.Normal = _negativeY;
         else if (Constants.WithinEpsilon(point1.Z, state.IntersectionPoint.Z))
            state.Normal = _negativeZ;
         else if (Constants.WithinEpsilon(point2.X, state.IntersectionPoint.X))
            state.Normal = _positiveX;
         else if (Constants.WithinEpsilon(point2.Y, state.IntersectionPoint.Y))
            state.Normal = _positiveY;
         else if (Constants.WithinEpsilon(point2.Z, state.IntersectionPoint.Z))
            state.Normal = _positiveZ;
*/
         if (state.Normal == null) {
            state.IntersectionPoint = objectSpaceRay.GetPointAtT(state.TMax);

            state.Normal = Constants.WithinEpsilon(point1.X, state.IntersectionPoint.X) ? _negativeX : state.Normal;
            state.Normal = Constants.WithinEpsilon(point1.Y, state.IntersectionPoint.Y) ? _negativeY : state.Normal;
            state.Normal = Constants.WithinEpsilon(point1.Z, state.IntersectionPoint.Z) ? _negativeZ : state.Normal;
            state.Normal = Constants.WithinEpsilon(point2.X, state.IntersectionPoint.X) ? _positiveX : state.Normal;
            state.Normal = Constants.WithinEpsilon(point2.Y, state.IntersectionPoint.Y) ? _positiveY : state.Normal;
            state.Normal = Constants.WithinEpsilon(point2.Z, state.IntersectionPoint.Z) ? _positiveZ : state.Normal;

            /*

            if (Constants.WithinEpsilon(point1.X, state.IntersectionPoint.X))
               state.Normal = _negativeX;
            else if (Constants.WithinEpsilon(point1.Y, state.IntersectionPoint.Y))
               state.Normal = _negativeY;
            else if (Constants.WithinEpsilon(point1.Z, state.IntersectionPoint.Z))
               state.Normal = _negativeZ;
            else if (Constants.WithinEpsilon(point2.X, state.IntersectionPoint.X))
               state.Normal = _positiveX;
            else if (Constants.WithinEpsilon(point2.Y, state.IntersectionPoint.Y))
               state.Normal = _positiveY;
            else if (Constants.WithinEpsilon(point2.Z, state.IntersectionPoint.Z))
               state.Normal = _positiveZ;
            */

            if (state.Normal == null) {
               float smallest = Float.MAX_VALUE;

               float p1xDiff = point1.X - state.IntersectionPoint.X;
               smallest = p1xDiff <= smallest ? p1xDiff : smallest;
               state.Normal = p1xDiff <= smallest ? _negativeX : state.Normal;

               float p1yDiff = point1.Y - state.IntersectionPoint.Y;
               smallest = p1yDiff <= smallest ? p1yDiff : smallest;
               state.Normal = p1yDiff <= smallest ? _negativeY : state.Normal;

               float p1zDiff = point1.Z - state.IntersectionPoint.Z;
               smallest = p1zDiff <= smallest ? p1zDiff : smallest;
               state.Normal = p1zDiff <= smallest ? _negativeZ : state.Normal;

               float p2xDiff = point2.X - state.IntersectionPoint.X;
               smallest = p2xDiff <= smallest ? p2xDiff : smallest;
               state.Normal = p2xDiff <= smallest ? _positiveX : state.Normal;

               float p2yDiff = point2.Y - state.IntersectionPoint.Y;
               smallest = p2yDiff <= smallest ? p2yDiff : smallest;
               state.Normal = p2yDiff <= smallest ? _positiveY : state.Normal;

               float p2zDiff = point2.Z - state.IntersectionPoint.Z;
               smallest = p2zDiff <= smallest ? p2zDiff : smallest;
               state.Normal = p2zDiff <= smallest ? _positiveZ : state.Normal;

               if (state.Normal == null) {
                  System.out.println("Bounding box intersection couldn't find normal...");

                  if (Float.isNaN(p1xDiff)) {
                     System.out.println("nan");
                  }
                  System.out.println("point1: " + point1);
                  System.out.println("point2: " + point2);
                  System.out.println("hitpoint: " + state.IntersectionPoint);

                  boolean isNan = Float.isNaN(objectSpaceRay.Direction.X);

                  throw new NullPointerException();
               }
            }
         }

         if (state.Normal.Dot(objectSpaceRay.Direction) > 0)
            state.Normal.Scale(-1);

         if (ObjectToWorld != null) {
            state.IntersectionPoint = ObjectToWorld.Apply(state.IntersectionPoint);
            state.Normal = ObjectToWorld.Apply(state.Normal);
            if (ObjectToWorld.HasScale()) {
               state.Normal.Normalize();
               state.TMin = worldSpaceRay.GetTAtPoint(state.IntersectionPoint);
            }
         }

      }
      return state;
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
