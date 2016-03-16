package net.danielthompson.danray.structures;

import net.danielthompson.danray.acceleration.KDAxis;
import net.danielthompson.danray.states.IntersectionState;

/**
 * User: daniel
 * Date: 7/21/13
 * Time: 15:44
 */
public class BoundingBox {
   public Point point1;
   public Point point2;

   public BoundingBox (Point point1, Point point2) {
      this.point1 = point1;
      this.point2 = point2;
   }

   public boolean isPointInside(Point p) {
      return (
            (p.X >= point1.X || Constants.WithinEpsilon(p.X, point1.X))
         && (p.Y >= point1.Y || Constants.WithinEpsilon(p.Y, point1.Y))
         && (p.Z >= point1.Z || Constants.WithinEpsilon(p.Z, point1.Z))
         && (p.X <= point2.X || Constants.WithinEpsilon(p.X, point2.X))
         && (p.Y <= point2.Y || Constants.WithinEpsilon(p.Y, point2.Y))
         && (p.Z <= point2.Z || Constants.WithinEpsilon(p.Z, point2.Z))
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


   public BoundingBox GetWorldBoundingBox() {
      return this;
   }

   public double getMedian(KDAxis axis) {
      double median = (point1.getAxis(axis) + point2.getAxis(axis)) / 2.0;
      return median;
   }

   public IntersectionState GetHitInfo(Ray ray) {
      return BoundingBox.GetHitInfo(point1, point2, ray);
   }

   // orig
   public static IntersectionState GetHitInfo(Point p1, Point p2, Ray ray) {
      double maxBoundFarT = Double.MAX_VALUE;
      double minBoundNearT = 0;

      IntersectionState state = new IntersectionState();
      state.Hits = true;

      double rayInverse = 1.0 / ray.Direction.X;
      double tNear = (p1.X - ray.Origin.X) * rayInverse;
      double tFar = (p2.X - ray.Origin.X) * rayInverse;
      if (tNear > tFar) {
         double swap = tNear;
         tNear = tFar;
         tFar = swap;
      }

      minBoundNearT = (tNear > minBoundNearT) ? tNear : minBoundNearT;
      maxBoundFarT = (tFar < maxBoundFarT) ? tFar : maxBoundFarT;
      if (minBoundNearT > maxBoundFarT) {
         state.Hits = false;
         return state;
      }
      //else {
      state.TMin = minBoundNearT;
      state.TMax = maxBoundFarT;
      //}

      // y

      rayInverse = 1.0 / ray.Direction.Y;
      tNear = (p1.Y - ray.Origin.Y) * rayInverse;
      tFar = (p2.Y - ray.Origin.Y) * rayInverse;
      if (tNear > tFar) {
         double swap = tNear;
         tNear = tFar;
         tFar = swap;
      }

      minBoundNearT = (tNear > minBoundNearT) ? tNear : minBoundNearT;
      maxBoundFarT = (tFar < maxBoundFarT) ? tFar : maxBoundFarT;
      if (minBoundNearT > maxBoundFarT) {
         state.Hits = false;
         return state;
      }
      //else {
      state.TMin = minBoundNearT;
      state.TMax = maxBoundFarT;
      //}

      // z

      rayInverse = 1.0 / ray.Direction.Z;
      tNear = (p1.Z - ray.Origin.Z) * rayInverse;
      tFar = (p2.Z - ray.Origin.Z) * rayInverse;
      if (tNear > tFar) {
         double swap = tNear;
         tNear = tFar;
         tFar = swap;
      }

      minBoundNearT = (tNear > minBoundNearT) ? tNear : minBoundNearT;
      maxBoundFarT = (tFar < maxBoundFarT) ? tFar : maxBoundFarT;
      if (minBoundNearT > maxBoundFarT) {
         state.Hits = false;
         return state;
      }

      state.TMin = minBoundNearT;
      state.TMax = maxBoundFarT;

      return state;

   }

   public static IntersectionState GetHitInfoModified(Point p1, Point p2, Ray ray) {
      double maxBoundFarT = Double.MAX_VALUE;
      double minBoundNearT = 0;

      IntersectionState state = new IntersectionState();
      state.Hits = true;

      // X
      double rayInverse = 1.0 / ray.Direction.X;
      double tNear = (p1.X - ray.Origin.X) * rayInverse;
      double tFar = (p2.X - ray.Origin.X) * rayInverse;

      double swap = tNear;
      tNear = tNear > tFar ? tFar : tNear;
      tFar = tNear > tFar ? swap : tFar;
      /*if (tNear > tFar) {
         double swap = tNear;
         tNear = tFar;
         tFar = swap;
      }*/

      minBoundNearT = (tNear > minBoundNearT) ? tNear : minBoundNearT;
      maxBoundFarT = (tFar < maxBoundFarT) ? tFar : maxBoundFarT;

      if (minBoundNearT > maxBoundFarT) {
         state.Hits = false;
         return state;
      }
      state.TMin = minBoundNearT;
      state.TMax = maxBoundFarT;

      // Y
      rayInverse = 1.0 / ray.Direction.Y;
      tNear = (p1.Y - ray.Origin.Y) * rayInverse;
      tFar = (p2.Y - ray.Origin.Y) * rayInverse;


      /*f (tNear > tFar) {
         double swap = tNear;
         tNear = tFar;
         tFar = swap;
      }*/
      swap = tNear;
      tNear = tNear > tFar ? tFar : tNear;
      tFar = tNear > tFar ? swap : tFar;

      minBoundNearT = (tNear > minBoundNearT) ? tNear : minBoundNearT;
      maxBoundFarT = (tFar < maxBoundFarT) ? tFar : maxBoundFarT;

      if (minBoundNearT > maxBoundFarT) {
         state.Hits = false;
         return state;
      }

      state.TMin = minBoundNearT;
      state.TMax = maxBoundFarT;

      // Z
      rayInverse = 1.0 / ray.Direction.Z;
      tNear = (p1.Z - ray.Origin.Z) * rayInverse;
      tFar = (p2.Z - ray.Origin.Z) * rayInverse;
      /*f (tNear > tFar) {
         double swap = tNear;
         tNear = tFar;
         tFar = swap;
      }*/
      swap = tNear;
      tNear = tNear > tFar ? tFar : tNear;
      tFar = tNear > tFar ? swap : tFar;

      minBoundNearT = (tNear > minBoundNearT) ? tNear : minBoundNearT;
      maxBoundFarT = (tFar < maxBoundFarT) ? tFar : maxBoundFarT;

      if (minBoundNearT > maxBoundFarT) {
         state.Hits = false;
         return state;
      }

      state.TMin = minBoundNearT;
      state.TMax = maxBoundFarT;


      //state.TMin = (tNear > minBoundNearT) ? tNear : minBoundNearT;
      //state.TMax = (tFar < maxBoundFarT) ? tFar : maxBoundFarT;

      //state.Hits = (state.TMin <= state.TMax);
      //state.Hits = (minBoundNearT <= maxBoundFarT);

      if (ray.Direction.Y < 0.673 && ray.Direction.Y > 0.671)
         System.out.println("");

      return state;

   }

   public static IntersectionState GetHitInfoNew(Point p1, Point p2, Ray ray) {
      double maxBoundFarT = Double.MAX_VALUE;
      double minBoundNearT = 0;

      IntersectionState state = new IntersectionState();
      state.Hits = true;

      // X
      double tNear = (p1.X - ray.Origin.X) * ray.DirectionInverse.X;
      double tFar = (p2.X - ray.Origin.X) * ray.DirectionInverse.X;

      double swap = tNear;
      tNear = tNear > tFar ? tFar : tNear;
      tFar = tNear > tFar ? swap : tFar;
      /*if (tNear > tFar) {
         double swap = tNear;
         tNear = tFar;
         tFar = swap;
      }*/

      minBoundNearT = (tNear > minBoundNearT) ? tNear : minBoundNearT;
      maxBoundFarT = (tFar < maxBoundFarT) ? tFar : maxBoundFarT;

      if (minBoundNearT > maxBoundFarT) {
         state.Hits = false;
         return state;
      }
      state.TMin = minBoundNearT;
      state.TMax = maxBoundFarT;

      // Y
      tNear = (p1.Y - ray.Origin.Y) * ray.DirectionInverse.Y;
      tFar = (p2.Y - ray.Origin.Y) * ray.DirectionInverse.Y;


      /*f (tNear > tFar) {
         double swap = tNear;
         tNear = tFar;
         tFar = swap;
      }*/
      swap = tNear;
      tNear = tNear > tFar ? tFar : tNear;
      tFar = tNear > tFar ? swap : tFar;

      minBoundNearT = (tNear > minBoundNearT) ? tNear : minBoundNearT;
      maxBoundFarT = (tFar < maxBoundFarT) ? tFar : maxBoundFarT;

      if (minBoundNearT > maxBoundFarT) {
         state.Hits = false;
         return state;
      }

      state.TMin = minBoundNearT;
      state.TMax = maxBoundFarT;

      // Z
      tNear = (p1.Z - ray.Origin.Z) * ray.DirectionInverse.Z;
      tFar = (p2.Z - ray.Origin.Z) * ray.DirectionInverse.Z;
      /*f (tNear > tFar) {
         double swap = tNear;
         tNear = tFar;
         tFar = swap;
      }*/
      swap = tNear;
      tNear = tNear > tFar ? tFar : tNear;
      tFar = tNear > tFar ? swap : tFar;

      minBoundNearT = (tNear > minBoundNearT) ? tNear : minBoundNearT;
      maxBoundFarT = (tFar < maxBoundFarT) ? tFar : maxBoundFarT;

      if (minBoundNearT > maxBoundFarT) {
         state.Hits = false;
         return state;
      }

      state.TMin = minBoundNearT;
      state.TMax = maxBoundFarT;


      //state.TMin = (tNear > minBoundNearT) ? tNear : minBoundNearT;
      //state.TMax = (tFar < maxBoundFarT) ? tFar : maxBoundFarT;

      //state.Hits = (state.TMin <= state.TMax);
      //state.Hits = (minBoundNearT <= maxBoundFarT);

      if (ray.Direction.Y < 0.673 && ray.Direction.Y > 0.671)
         System.out.println("");

      return state;

   }

   public boolean Hits(Ray ray) {
      return (ray.Origin.X >= point1.X && ray.Origin.X <= point2.X
            && ray.Origin.Y >= point1.Y && ray.Origin.Y <= point2.Y
            && ray.Origin.Z >= point1.Z && ray.Origin.Z <= point2.Z) || BoundingBox.GetHitInfo(point1, point2, ray).Hits;
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

      Point p0 = new Point(-100, 0, -1500);
      Point p1 = new Point(500, 600, -1499);

      if (box1.point2.X == 500 && box1.point2.Y == 600 && box1.point2.Z == -1499)
         System.out.flush();
      box1.point2.Z = Math.max(box1.point2.Z, box2.point2.Z);
   }

   public static BoundingBox GetBoundingBox(BoundingBox box1, BoundingBox box2) {
      double p1x = Math.min(box1.point1.X, box2.point1.X);
      double p1y = Math.min(box1.point1.Y, box2.point1.Y);
      double p1z = Math.min(box1.point1.Z, box2.point1.Z);
      Point p1 = new Point(p1x, p1y, p1z);

      double p2x = Math.max(box1.point2.X, box2.point2.X);
      double p2y = Math.max(box1.point2.Y, box2.point2.Y);
      double p2z = Math.max(box1.point2.Z, box2.point2.Z);
      Point p2 = new Point(p2x, p2y, p2z);

      BoundingBox box = new BoundingBox(p1, p2);
      return box;
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

   public static boolean IsOutsideOf(BoundingBox b1, BoundingBox b2) {
      return
            ((b1.point2.X > b2.point1.X
            && b1.point2.Y > b2.point1.Y
            && b1.point2.Z > b2.point1.Z)
            || (b2.point2.X > b1.point1.X
                && b2.point2.Y > b1.point1.Y
                && b2.point2.Z > b1.point1.Z))
            ;
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

   public static BoundingBox Intersection(BoundingBox b1, BoundingBox b2) {

      // b1 is inside of b2
      if (IsInsideOf(b1, b2))
         return b1;

      // b2 is inside of b1
      if (IsInsideOf(b2, b1))
         return b2;

      if (IsOutsideOf(b1, b2))
         return null;

      BoundingBox xMin = b1;
      BoundingBox xMax = b1;
      
      if (b1.point1.X > b2.point1.X) {
         xMin = b2;
      }
      if (b1.point2.X < b2.point2.X) {
         xMax = b2;
      }

      BoundingBox yMin = b1;
      BoundingBox yMax = b2;

      if (b1.point1.Y > b2.point1.Y) {
         yMin = b2;
      }
      if (b1.point2.Y < b2.point2.Y) {
         yMax = b2;
      }

      BoundingBox zMin = b1;
      BoundingBox zMax = b2;

      if (b1.point1.Z > b2.point1.Z) {
         zMin = b2;
      }
      if (b1.point2.Z < b2.point2.Z) {
         zMax = b2;
      }

      double x1 = xMin.point1.X;
      double x2 = xMin.point2.X < xMax.point1.X ? xMin.point2.X : xMax.point1.X;
      double x3 = xMin.point2.X > xMax.point1.X ? xMin.point2.X : xMax.point1.X;
      double x4 = xMax.point2.X;

      double y1 = yMin.point1.Y;
      double y2 = yMin.point2.Y < yMax.point1.Y ? yMin.point2.Y : yMax.point1.Y;
      double y3 = yMin.point2.Y > yMax.point1.Y ? yMin.point2.Y : yMax.point1.Y;
      double y4 = yMax.point2.Y;

      double z1 = zMin.point1.Z;
      double z2 = zMin.point2.Z < zMax.point1.Z ? zMin.point2.Z : zMax.point1.Z;
      double z3 = zMin.point2.Z > zMax.point1.Z ? zMin.point2.Z : zMax.point1.Z;
      double z4 = zMax.point2.Z;
      
      Point p1 = new Point(x2, y2, z2);
      Point p2 = new Point(x3, y3, z3);

      return new BoundingBox(p1, p2);





   }
}