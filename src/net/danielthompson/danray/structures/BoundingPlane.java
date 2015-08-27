package net.danielthompson.danray.structures;

import net.danielthompson.danray.acceleration.KDAxis;
import net.danielthompson.danray.states.IntersectionState;



/**
 * Created by daniel on 12/7/14.
 */
public class BoundingPlane implements Boundable {
   protected Point _origin;
   protected Normal _normal;

   public int ID;
   public int getID() {
      return ID;
   }

   public BoundingPlane(Point origin, Normal normal) {
      _origin = origin;
      _normal = normal;
   }

   public Normal GetNormal() {
      return _normal;
   }

   @Override
   public double GetVolume() {
      return 0;
   }

   @Override
   public BoundingBox GetWorldBoundingBox() {
      return null;
   }

   @Override
   public double getMedian(KDAxis axis) {
      return 0;
   }

   public IntersectionState GetHitInfo(Ray ray) {
      double numerator = (Point.Minus(_origin, ray.Origin)).Dot(_normal);
      double denominator = ray.Direction.Dot(_normal);

      IntersectionState state = new IntersectionState();

      // if they are orthogonal, then they don't hit.
      if (Constants.WithinEpsilon(denominator, 0.0)) {
         // no intersection
         state.Hits = false;
      }

      // need to check for both normal directions!
      else {
         double T = numerator / denominator;
         if (T > 0.0) {
            state.Hits = true;
            state.TMin = T;
            state.IntersectionPoint = ray.GetPointAtT(state.TMin);
            state.Normal = _normal;
         }
      }

      return state;
   }

   @Override
   public boolean Hits(Ray ray) {
      return GetHitInfo(ray).Hits;
   }

   public boolean On(Point p) {
      return (_normal.Dot(Point.Minus(_origin, p)) == 0);
   }

}
