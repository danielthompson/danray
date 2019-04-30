package net.danielthompson.danray.structures;

import net.danielthompson.danray.acceleration.KDAxis;
import net.danielthompson.danray.states.Intersection;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.List;

/**
 * User: daniel
 * Date: 7/21/13
 * Time: 15:44
 */
public class BoundingBox {
   /**
    * "Lower" point. Always in world-space.
    */
   public Point point1;

   /**
    * "Upper" point. Always in world space.
    */
   public Point point2;

   public BoundingBox (Point point1, Point point2) {
      this.point1 = point1.clone();
      this.point2 = point2.clone();
   }

   public BoundingBox(BoundingBox box) {
      this.point1 = box.point1.clone();
      this.point2 = box.point2.clone();
   }

   public float getUpperBoundInAxis(KDAxis axis) {
      return Math.max(point1.getAxis(axis), point2.getAxis(axis));
   }

   public float getLowerBoundInAxis(KDAxis axis) {
      return Math.min(point1.getAxis(axis), point2.getAxis(axis));
   }

   public float GetVolume() {
      return Math.abs((point2.X - point1.X) * (point2.Y - point1.Y) * (point2.Z - point1.Z));
   }

   public float getSurfaceArea() {
      float xLength = Math.abs(point2.X - point1.X);
      float yLength = Math.abs(point2.Y - point1.Y);
      float zLength = Math.abs(point2.Z - point1.Z);

      float surfaceArea = 2 * (xLength * yLength + xLength * zLength + yLength * zLength);

      return surfaceArea;

   }

   public KDAxis getLargestExtent() {
      float x = point2.X - point1.X;
      float y = point2.Y - point1.Y;
      float z = point2.Z - point1.Z;

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


   public BoundingBox GetWorldBoundingBox() {
      return this;
   }

   public float getMedian(KDAxis axis) {
      float median = (point1.getAxis(axis) + point2.getAxis(axis)) / 2.0f;
      return median;
   }

   public Intersection GetHitInfo(Ray ray) {
      return BoundingBox.GetHitInfoNew(point1, point2, ray);
   }

   // orig
   public static Intersection GetHitInfoOld(Point p1, Point p2, Ray ray) {
      float maxBoundFarT = Float.MAX_VALUE;
      float minBoundNearT = 0;

      Intersection state = new Intersection();
      state.Hits = true;

      // X
      float tNear = (p1.X - ray.Origin.X) * ray.DirectionInverse.X;
      float tFar = (p2.X - ray.Origin.X) * ray.DirectionInverse.X;

      if (tNear > tFar) {
         float swap = tNear;
         tNear = tFar;
         tFar = swap;
      }

      minBoundNearT = (tNear > minBoundNearT) ? tNear : minBoundNearT;
      maxBoundFarT = (tFar < maxBoundFarT) ? tFar : maxBoundFarT;
      if (minBoundNearT > maxBoundFarT) {
         state.Hits = false;
         return state;
      }
      state.t = minBoundNearT;
      //state.TMax = maxBoundFarT;

      tNear = (p1.Y - ray.Origin.Y) * ray.DirectionInverse.Y;
      tFar = (p2.Y - ray.Origin.Y) * ray.DirectionInverse.Y;
      if (tNear > tFar) {
         float swap = tNear;
         tNear = tFar;
         tFar = swap;
      }

      minBoundNearT = (tNear > minBoundNearT) ? tNear : minBoundNearT;
      maxBoundFarT = (tFar < maxBoundFarT) ? tFar : maxBoundFarT;
      if (minBoundNearT > maxBoundFarT) {
         state.Hits = false;
         return state;
      }

      state.t = minBoundNearT;
      //state.TMax = maxBoundFarT;

      //rayInverse = ray.DirectionInverse.Z;
      tNear = (p1.Z - ray.Origin.Z) * ray.DirectionInverse.Z;
      tFar = (p2.Z - ray.Origin.Z) * ray.DirectionInverse.Z;
      if (tNear > tFar) {
         float swap = tNear;
         tNear = tFar;
         tFar = swap;
      }

      minBoundNearT = (tNear > minBoundNearT) ? tNear : minBoundNearT;
      maxBoundFarT = (tFar < maxBoundFarT) ? tFar : maxBoundFarT;
      if (minBoundNearT > maxBoundFarT) {
         state.Hits = false;
         return state;
      }

      state.t = minBoundNearT;
      //state.TMax = maxBoundFarT;

      return state;

   }

   public static Intersection GetHitInfoNew(Point p1, Point p2, Ray ray) {
      float maxBoundFarT = Float.MAX_VALUE;
      float minBoundNearT = 0;

      Intersection intersection = new Intersection();
      intersection.Hits = true;

      // X
      float tNear = (p1.X - ray.Origin.X) * ray.DirectionInverse.X;
      float tFar = (p2.X - ray.Origin.X) * ray.DirectionInverse.X;

      float swap = tNear;
      tNear = tNear > tFar ? tFar : tNear;
      tFar = swap > tFar ? swap : tFar;

      minBoundNearT = (tNear > minBoundNearT) ? tNear : minBoundNearT;
      maxBoundFarT = (tFar < maxBoundFarT) ? tFar : maxBoundFarT;
      if (minBoundNearT > maxBoundFarT) {
         intersection.Hits = false;
         return intersection;
      }

      intersection.t = minBoundNearT;
      //intersection.TMax = maxBoundFarT;

      // Y
      tNear = (p1.Y - ray.Origin.Y) * ray.DirectionInverse.Y;
      tFar = (p2.Y - ray.Origin.Y) * ray.DirectionInverse.Y;

      swap = tNear;
      tNear = tNear > tFar ? tFar : tNear;
      tFar = swap > tFar ? swap : tFar;

      minBoundNearT = (tNear > minBoundNearT) ? tNear : minBoundNearT;
      maxBoundFarT = (tFar < maxBoundFarT) ? tFar : maxBoundFarT;

      if (minBoundNearT > maxBoundFarT) {
         intersection.Hits = false;
         return intersection;
      }

      intersection.t = minBoundNearT;
      //intersection.TMax = maxBoundFarT;

      // Z
      tNear = (p1.Z - ray.Origin.Z) * ray.DirectionInverse.Z;
      tFar = (p2.Z - ray.Origin.Z) * ray.DirectionInverse.Z;

      swap = tNear;
      tNear = tNear > tFar ? tFar : tNear;
      tFar = swap > tFar ? swap : tFar;

      minBoundNearT = (tNear > minBoundNearT) ? tNear : minBoundNearT;
      maxBoundFarT = (tFar < maxBoundFarT) ? tFar : maxBoundFarT;

      if (minBoundNearT > maxBoundFarT) {
         intersection.Hits = false;
         return intersection;
      }

      intersection.t = minBoundNearT;
      //intersection.TMax = maxBoundFarT;

      return intersection;
   }

   public static List<Intersection> GetBothHitInfo(Point p1, Point p2, Ray ray) {
      float maxBoundFarT = Float.MAX_VALUE;
      float minBoundNearT = 0;

      List<Intersection> intersections = new ArrayList<>();

      // X
      float tNear = (p1.X - ray.Origin.X) * ray.DirectionInverse.X;
      float tFar = (p2.X - ray.Origin.X) * ray.DirectionInverse.X;

      float swap = tNear;
      tNear = tNear > tFar ? tFar : tNear;
      tFar = swap > tFar ? swap : tFar;

      minBoundNearT = (tNear > minBoundNearT) ? tNear : minBoundNearT;
      maxBoundFarT = (tFar < maxBoundFarT) ? tFar : maxBoundFarT;
      if (minBoundNearT > maxBoundFarT) {
         return intersections;
      }

      // Y
      tNear = (p1.Y - ray.Origin.Y) * ray.DirectionInverse.Y;
      tFar = (p2.Y - ray.Origin.Y) * ray.DirectionInverse.Y;

      swap = tNear;
      tNear = tNear > tFar ? tFar : tNear;
      tFar = swap > tFar ? swap : tFar;

      minBoundNearT = (tNear > minBoundNearT) ? tNear : minBoundNearT;
      maxBoundFarT = (tFar < maxBoundFarT) ? tFar : maxBoundFarT;

      if (minBoundNearT > maxBoundFarT) {
         return intersections;
      }

      // Z
      tNear = (p1.Z - ray.Origin.Z) * ray.DirectionInverse.Z;
      tFar = (p2.Z - ray.Origin.Z) * ray.DirectionInverse.Z;

      swap = tNear;
      tNear = tNear > tFar ? tFar : tNear;
      tFar = swap > tFar ? swap : tFar;

      minBoundNearT = (tNear > minBoundNearT) ? tNear : minBoundNearT;
      maxBoundFarT = (tFar < maxBoundFarT) ? tFar : maxBoundFarT;

      if (minBoundNearT > maxBoundFarT) {
         return intersections;
      }

      if (minBoundNearT > 0) {
         Intersection intersection = new Intersection();
         intersection.t = minBoundNearT;
         intersection.Hits = true;
         intersections.add(intersection);
      }

      Intersection intersection = new Intersection();
      intersection.t = maxBoundFarT;
      intersection.Hits = true;
      intersections.add(intersection);

      return intersections;
   }

   public boolean Hits(Ray ray) {
      return (ray.Origin.X >= point1.X && ray.Origin.X <= point2.X
            && ray.Origin.Y >= point1.Y && ray.Origin.Y <= point2.Y
            && ray.Origin.Z >= point1.Z && ray.Origin.Z <= point2.Z) || BoundingBox.GetHitInfoOld(point1, point2, ray).Hits;
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

   public static void ExpandBoundingBox(BoundingBox box1, BoundingBox box2) {
      box1.point1.X = Math.min(box1.point1.X, box2.point1.X);
      box1.point1.Y = Math.min(box1.point1.Y, box2.point1.Y);
      box1.point1.Z = Math.min(box1.point1.Z, box2.point1.Z);

      box1.point2.X = Math.max(box1.point2.X, box2.point2.X);
      box1.point2.Y = Math.max(box1.point2.Y, box2.point2.Y);


      box1.point2.Z = Math.max(box1.point2.Z, box2.point2.Z);
   }

   public static BoundingBox Union(BoundingBox box1, BoundingBox box2) {
      float p1x = Math.min(box1.point1.X, box2.point1.X);
      float p1y = Math.min(box1.point1.Y, box2.point1.Y);
      float p1z = Math.min(box1.point1.Z, box2.point1.Z);
      Point p1 = new Point(p1x, p1y, p1z);

      float p2x = Math.max(box1.point2.X, box2.point2.X);
      float p2y = Math.max(box1.point2.Y, box2.point2.Y);
      float p2z = Math.max(box1.point2.Z, box2.point2.Z);
      Point p2 = new Point(p2x, p2y, p2z);

      BoundingBox box = new BoundingBox(p1, p2);
      return box;
   }

   public static BoundingBox Difference(BoundingBox b1, BoundingBox b2) {
      return new BoundingBox(b1);
   }

   public static BoundingBox Intersection(BoundingBox b1, BoundingBox b2) {
      return Union(b1, b2);
   }

   /**
    * Checks to see if b1 is strictly inside of b2.
    * @param b1
    * @param b2
    * @return
    */
   public static boolean IsInsideOf(BoundingBox b1, BoundingBox b2) {
      return (b1.point1.X > b2.point1.X
            && b1.point1.Y > b2.point1.Y
            && b1.point1.Z > b2.point1.Z
            && b1.point2.X < b2.point2.X
            && b1.point2.Y < b2.point2.Y
            && b1.point2.Z < b2.point2.Z);
   }

   public static BoundingBox GetBoundingBox(BoundingBox box, Point point) {
      float p1x = Math.min(box.point1.X, point.X);
      float p1y = Math.min(box.point1.Y, point.Y);
      float p1z = Math.min(box.point1.Z, point.Z);
      Point p1 = new Point(p1x, p1y, p1z);

      float p2x = Math.max(box.point2.X, point.X);
      float p2y = Math.max(box.point2.Y, point.Y);
      float p2z = Math.max(box.point2.Z, point.Z);
      Point p2 = new Point(p2x, p2y, p2z);

      return new BoundingBox(p1, p2);
   }
}
