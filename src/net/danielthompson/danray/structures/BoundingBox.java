package net.danielthompson.danray.structures;

import net.danielthompson.danray.acceleration.KDAxis;
import net.danielthompson.danray.states.IntersectionState;

/**
 * User: daniel
 * Date: 7/21/13
 * Time: 15:44
 */
public class BoundingBox implements Boundable {
   public Point point1;
   public Point point2;

   public int ID;
   public int getID() {
      return ID;
   }

   public BoundingBox (Point point1, Point point2) {
      this.point1 = point1;
      this.point2 = point2;
   }

   public boolean isPointInside(Point p) {
      return (
            (p.X >= point1.X || Constants.WithinDelta(p.X, point1.X))
         && (p.Y >= point1.Y || Constants.WithinDelta(p.Y, point1.Y))
         && (p.Z >= point1.Z || Constants.WithinDelta(p.Z, point1.Z))
         && (p.X <= point2.X || Constants.WithinDelta(p.X, point2.X))
         && (p.Y <= point2.Y || Constants.WithinDelta(p.Y, point2.Y))
         && (p.Z <= point2.Z || Constants.WithinDelta(p.Z, point2.Z))
      );
   }

   public double getUpperBoundInAxis(KDAxis axis) {
      return Math.max(point1.getAxis(axis), point2.getAxis(axis));
   }

   public double getLowerBoundInAxis(KDAxis axis) {
      return Math.min(point1.getAxis(axis), point2.getAxis(axis));
   }

   public double GetVolume() {
      return Math.abs((point2.X - point1.X) * (point2.Y - point1.Y) * (point2.Z - point1.Z));
   }

   public double getSurfaceArea() {
      double xLength = Math.abs(point2.X - point1.X);
      double yLength = Math.abs(point2.Y - point1.Y);
      double zLength = Math.abs(point2.Z - point1.Z);

      double surfaceArea = 2 * (xLength * yLength + xLength * zLength + yLength * zLength);

      return surfaceArea;

   }

   public KDAxis getLargestExtent() {
      double x = point2.X - point1.X;
      double y = point2.Y - point1.Y;
      double z = point2.Z - point1.Z;

      if (x >= y) {
         if (x >= z)
            return KDAxis.X;
         else
            return KDAxis.Z;
      }
      else {
         if (y >= z) {
            return KDAxis.Y;
         }
         else
            return KDAxis.Z;
      }
   }

   @Override
   public BoundingBox GetWorldBoundingBox() {
      return this;
   }

   @Override
   public double getMedian(KDAxis axis) {
      double median = (point1.getAxis(axis) + point2.getAxis(axis)) / 2.0;
      return median;
   }



   @Override
   public IntersectionState GetHitInfo(Ray ray) {
      double maxBoundFarT = Double.MAX_VALUE;
      double minBoundNearT = 0;

      IntersectionState state = new IntersectionState();
      state.Hits = true;

      double[] directions = new double[3];
      directions[0] = ray.Direction.X;
      directions[1] = ray.Direction.Y;
      directions[2] = ray.Direction.Z;

      KDAxis[] axes = new KDAxis[3];
      axes[0] = KDAxis.X;
      axes[1] = KDAxis.Y;
      axes[2] = KDAxis.Z;

      for (int i = 0; i < 3; i++) {
         KDAxis axis = axes[i];
         double rayInverse = 1.0 / directions[i];
         double tNear = (point1.getAxis(axis) - ray.Origin.getAxis(axis)) * rayInverse;
         double tFar = (point2.getAxis(axis) - ray.Origin.getAxis(axis)) * rayInverse;
         if (tNear > tFar) {
            double swap = tNear;
            tNear = tFar;
            tFar = swap;
         }

         minBoundNearT = (tNear > minBoundNearT) ? tNear : minBoundNearT;
         maxBoundFarT = (tFar < maxBoundFarT) ? tFar : maxBoundFarT;
         if (minBoundNearT > maxBoundFarT) {
            state.Hits = false;
            break;
         }
         else {
            state.TMin = minBoundNearT;
            state.TMax = maxBoundFarT;
         }
      }
      /*
      if (state.Hits) {

         ray.MinT = state.TMin;
         ray.MaxT = state.TMax;
      }*/
      return state;
   }

   @Override
   public boolean Hits(Ray ray) {
      return (ray.Origin.X >= point1.X && ray.Origin.X <= point2.X
            && ray.Origin.Y >= point1.Y && ray.Origin.Y <= point2.Y
            && ray.Origin.Z >= point1.Z && ray.Origin.Z <= point2.Z) || GetHitInfo(ray).Hits;
   }

   public void Translate(Vector vector) {
      point1.Plus(vector);
      point2.Plus(vector);
   }

   public boolean equals(Object obj) {
      if (obj == null)
         return false;
      if (obj == this)
         return true;
      if (!(obj instanceof BoundingBox))
         return false;

      BoundingBox rhs = (BoundingBox) obj;
      return (point1.equals(rhs.point1) && point2.equals(rhs.point2));
   }

   public static BoundingBox GetBoundingBox(BoundingBox box1, BoundingBox box2) {
      double p1x = Math.min(box1.point1.X, box2.point1.X);
      double p1y = Math.min(box1.point1.Y, box2.point1.Y);
      double p1z = Math.min(box1.point1.Z, box2.point1.Z);
      Point p1 = new Point(p1x, p1y, p1z);

      double p2x = Math.max(box1.point1.X, box2.point1.X);
      double p2y = Math.max(box1.point1.Y, box2.point1.Y);
      double p2z = Math.max(box1.point1.Z, box2.point1.Z);
      Point p2 = new Point(p2x, p2y, p2z);

      BoundingBox box = new BoundingBox(p1, p2);
      return box;
   }

   public static BoundingBox GetBoundingBox(BoundingBox box, Point point) {
      double p1x = Math.min(box.point1.X, point.X);
      double p1y = Math.min(box.point1.Y, point.Y);
      double p1z = Math.min(box.point1.Z, point.Z);
      Point p1 = new Point(p1x, p1y, p1z);

      double p2x = Math.max(box.point1.X, point.X);
      double p2y = Math.max(box.point1.Y, point.Y);
      double p2z = Math.max(box.point1.Z, point.Z);
      Point p2 = new Point(p2x, p2y, p2z);

      return new BoundingBox(p1, p2);
   }
}