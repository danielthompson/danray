package net.danielthompson.danray.shapes;

import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.*;

/**
 * Created by daniel on 2/17/14.
 */
public class Box extends BoundingBox implements Drawable {

   private Material _material;

   public int ID;
   public int getID() {
      return ID;
   }

   public Transform ObjectToWorld;
   private Transform WorldToObject;

   public BoundingBox WorldBoundingBox;

   private Normal _negativeX = new Normal(-1, 0, 0);
   private Normal _negativeY = new Normal(0, -1, 0);
   private Normal _negativeZ = new Normal(0, 0, -1);
   private Normal _positiveX = new Normal(1, 0, 0);
   private Normal _positiveY = new Normal(0, 1, 0);
   private Normal _positiveZ = new Normal(0, 0, 1);

   public Box(Point p1, Point p2, Material material) {
      super(p1, p2);
      _material = material;
   }

   public Box(Point p1, Point p2, Material material, Transform objectToWorld, Transform worldToObject) {
      this(p1, p2, material);
      ObjectToWorld = objectToWorld;
      WorldToObject = worldToObject;
   }

   @Override
   public BoundingBox GetWorldBoundingBox() {
      if (ObjectToWorld == null) {
         return this;
      }
      else {
         if (WorldBoundingBox == null) {
            /*Point p1 = point1;
            Point p2 = point2;

            Point[] points = new Point[8];

            points[0] = ObjectToWorld.Apply(point1);
            points[1] = ObjectToWorld.Apply(point2);
            points[2] = ObjectToWorld.Apply(new Point(p1.X, p1.Y, p2.Z));
            points[3] = ObjectToWorld.Apply(new Point(p1.X, p2.Y, p1.Z));
            points[4] = ObjectToWorld.Apply(new Point(p1.X, p2.Y, p2.Z));
            points[5] = ObjectToWorld.Apply(new Point(p2.X, p2.Y, p1.Z));
            points[6] = ObjectToWorld.Apply(new Point(p2.X, p1.Y, p2.Z));
            points[7] = ObjectToWorld.Apply(new Point(p2.X, p1.Y, p1.Z));

            double xMin, yMin, zMin;
            xMin = yMin = zMin = Double.MAX_VALUE;

            double xMax, yMax, zMax;
            xMax = yMax = zMax = -Double.MAX_VALUE;

            for (int i = 0; i < 8; i++) {
               if (points[i].X < xMin)
                  xMin = points[i].X;
               if (points[i].Y < yMin)
                  yMin = points[i].Y;
               if (points[i].Z < zMin)
                  zMin = points[i].Z;

               if (points[i].X > xMax)
                  xMax = points[i].X;
               if (points[i].Y > yMax)
                  yMax = points[i].Y;
               if (points[i].Z > zMax)
                  zMax = points[i].Z;

            }

            Point min = new Point(xMin, yMin, zMin);
            Point max = new Point(xMax, yMax, zMax);
            WorldBoundingBox = new BoundingBox(min, max);
            */
            WorldBoundingBox = ObjectToWorld.Apply(this);
         }
         return WorldBoundingBox;
      }
   }

   @Override
   public IntersectionState GetHitInfo(Ray worldSpaceRay) {

      Ray objectSpaceRay = worldSpaceRay;

      if (WorldToObject != null) {
         objectSpaceRay = WorldToObject.Apply(worldSpaceRay);
      }

      IntersectionState state = super.GetHitInfo(objectSpaceRay);

      if (state.Hits) {
         state.Drawable = this;
         state.IntersectionPoint = objectSpaceRay.GetPointAtT(state.TMin);
         state.Normal = Constants.WithinDelta(point1.X, state.IntersectionPoint.X) ? _negativeX : state.Normal;
         state.Normal = Constants.WithinDelta(point1.Y, state.IntersectionPoint.Y) ? _negativeY : state.Normal;
         state.Normal = Constants.WithinDelta(point1.Z, state.IntersectionPoint.Z) ? _negativeZ : state.Normal;
         state.Normal = Constants.WithinDelta(point2.X, state.IntersectionPoint.X) ? _positiveX : state.Normal;
         state.Normal = Constants.WithinDelta(point2.Y, state.IntersectionPoint.Y) ? _positiveY : state.Normal;
         state.Normal = Constants.WithinDelta(point2.Z, state.IntersectionPoint.Z) ? _positiveZ : state.Normal;
         /*
         if (Constants.WithinDelta(point1.X, state.IntersectionPoint.X))
            state.Normal = _negativeX;
         else if (Constants.WithinDelta(point1.Y, state.IntersectionPoint.Y))
            state.Normal = _negativeY;
         else if (Constants.WithinDelta(point1.Z, state.IntersectionPoint.Z))
            state.Normal = _negativeZ;
         else if (Constants.WithinDelta(point2.X, state.IntersectionPoint.X))
            state.Normal = _positiveX;
         else if (Constants.WithinDelta(point2.Y, state.IntersectionPoint.Y))
            state.Normal = _positiveY;
         else if (Constants.WithinDelta(point2.Z, state.IntersectionPoint.Z))
            state.Normal = _positiveZ;
*/
         if (state.Normal == null) {
            state.IntersectionPoint = objectSpaceRay.GetPointAtT(state.TMax);

            state.Normal = Constants.WithinDelta(point1.X, state.IntersectionPoint.X) ? _negativeX : state.Normal;
            state.Normal = Constants.WithinDelta(point1.Y, state.IntersectionPoint.Y) ? _negativeY : state.Normal;
            state.Normal = Constants.WithinDelta(point1.Z, state.IntersectionPoint.Z) ? _negativeZ : state.Normal;
            state.Normal = Constants.WithinDelta(point2.X, state.IntersectionPoint.X) ? _positiveX : state.Normal;
            state.Normal = Constants.WithinDelta(point2.Y, state.IntersectionPoint.Y) ? _positiveY : state.Normal;
            state.Normal = Constants.WithinDelta(point2.Z, state.IntersectionPoint.Z) ? _positiveZ : state.Normal;

            /*

            if (Constants.WithinDelta(point1.X, state.IntersectionPoint.X))
               state.Normal = _negativeX;
            else if (Constants.WithinDelta(point1.Y, state.IntersectionPoint.Y))
               state.Normal = _negativeY;
            else if (Constants.WithinDelta(point1.Z, state.IntersectionPoint.Z))
               state.Normal = _negativeZ;
            else if (Constants.WithinDelta(point2.X, state.IntersectionPoint.X))
               state.Normal = _positiveX;
            else if (Constants.WithinDelta(point2.Y, state.IntersectionPoint.Y))
               state.Normal = _positiveY;
            else if (Constants.WithinDelta(point2.Z, state.IntersectionPoint.Z))
               state.Normal = _positiveZ;
            */

            if (state.Normal == null) {
               System.out.println("Bounding box intersection couldn't find normal...");
               System.out.println("point1: " + point1);
               System.out.println("point2: " + point2);
               System.out.println("hitpoint: " + state.IntersectionPoint);
               throw new NullPointerException();
            }
         }
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

   @Override
   public Material GetMaterial() {
      return _material;
   }

   @Override
   public double BRDF(IntersectionState state, Ray cameraRay, Ray lightRay) {
      return 0;
   }

   public String toString() {
      return ID + "";
   }

}
