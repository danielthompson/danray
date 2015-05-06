package net.danielthompson.danray;

import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.*;
import org.apache.commons.math3.util.FastMath;

/**
 * Created by daniel on 3/17/15.
 */
public class GeometryCalculations {

   private static double angleOfIncidencePercentageFactor = 10. / 9.;

   public static double GetAngleOfIncidence(Ray incomingRay, IntersectionState state) {
      Normal normal = state.Normal;
      Vector incomingDirection = incomingRay.Direction;

      double angleRadians = FastMath.acos(normal.Dot(incomingDirection));

      double angleDegress = FastMath.toDegrees(angleRadians);

      return 180 - angleDegress;
   }

   public static double GetAngleOfIncidencePercentage(Ray incomingRay, IntersectionState state) {
      double AoI = GeometryCalculations.GetAngleOfIncidence(incomingRay, state);

      double AoIp = FastMath.abs(100 - (AoI * angleOfIncidencePercentageFactor));

      return AoIp;
   }

   public static Ray GetRefractedRay(IntersectionState state, Normal normal, Ray incomingRay, double oldIndexOfRefraction) {

      normal.Normalize();

      double n1 = oldIndexOfRefraction;
      double n2 = state.Drawable.GetMaterial().getIndexOfRefraction();
      double nRatio = n1 / n2;

      double cosTheta1 = -normal.Dot(incomingRay.Direction);
      double cosTheta2 = Math.sqrt(1 - nRatio * nRatio * (1 - (cosTheta1 * cosTheta1)));

      Vector refractedDirection;

      if (cosTheta1 > 0) {
         refractedDirection = Vector.Plus(incomingRay.Scale(nRatio), new Vector(Normal.Scale(normal, nRatio * cosTheta1 - cosTheta2)));
      }
      else {
         refractedDirection = Vector.Plus(incomingRay.Scale(nRatio), new Vector(Normal.Scale(normal, nRatio * cosTheta1 + cosTheta2)));
      }

      Point offsetIntersection = Point.Plus(state.IntersectionPoint, Vector.Scale(refractedDirection, .0000001));

      return new Ray(offsetIntersection, refractedDirection);
   }

   public static Ray GetReflectedRay(Point intersectionPoint, Normal normal, Ray incomingRay) {
      normal.Normalize();
      double factor = incomingRay.Direction.Dot(normal) * 2;
      Vector scaled = new Vector(Normal.Scale(normal, factor));
      Vector direction = Vector.Minus(new Vector(0, 0, 0), Vector.Minus(scaled, incomingRay.Direction));

      Point offsetIntersection = Point.Plus(intersectionPoint, Vector.Scale(direction, Constants.NumericalDelta * 1000));

      //Point direction = normal.ScaleFromOrigin(incomingRay.Direction.Dot(normal.Direction) * 2).Minus(incomingRay.Direction);
      Ray reflectedRay = new Ray(offsetIntersection, direction);
      return reflectedRay;
   }

   public static Ray GetReflectedRayPerturbed(Point intersectionPoint, Ray normal, Ray incomingRay, double perturbation) {
      double factor = incomingRay.Direction.Dot(normal.Direction) * 2;
      Vector scaled = normal.Scale(factor);
      Vector direction = Vector.Minus(new Vector(0, 0, 0), Vector.Minus(scaled, incomingRay.Direction));

      Vector perturbationDirection = new Vector(Math.random()*perturbation, Math.random()*perturbation, Math.random()*perturbation);

      Point offsetIntersection = Point.Plus(intersectionPoint, Vector.Scale(direction, .0000001));

      direction.Plus(perturbationDirection);

      //Point direction = normal.ScaleFromOrigin(incomingRay.Direction.Dot(normal.Direction) * 2).Minus(incomingRay.Direction);
      Ray reflectedRay = new Ray(offsetIntersection, direction);
      return reflectedRay;
   }
}
