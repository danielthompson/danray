package net.danielthompson.danray.structures;

import net.danielthompson.danray.lights.Radiatable;
import net.danielthompson.danray.presets.TracerOptions;
import net.danielthompson.danray.shapes.*;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.cameras.Camera;

/**
 * DanRay
 * User: dthompson
 * Date: 6/28/13
 * Time: 5:24 PM
 */
public class NaiveScene extends Scene {

   public Statistics statistics;

   public NaiveScene(Camera camera) {
      super(camera);
      ImplementationType = "Naive Scene";
   }

   @Override
   public void addDrawableObject(Shape shape) {
      shapes.add(shape);
   }

   @Override
   public void addRadiatableObject(Radiatable radiatable) {
      Radiatables.add(radiatable);
   }

   @Override
   public IntersectionState GetClosestDrawableToRay(Ray ray) {
      return GetClosestDrawableHitBetween(ray, 0, Double.MAX_VALUE);
   }

   @Override
   public IntersectionState GetClosestDrawableToRayBeyond(Ray ray, double t) {
      return GetClosestDrawableHitBetween(ray, t, Double.MAX_VALUE);
   }

   @Override
   public IntersectionState GetClosestDrawableHitBetween(Ray ray, double t0, double t1) {
      IntersectionState closestStateToRay = null;
      statistics = new Statistics();
      for (Shape shape : shapes) {
         IntersectionState state = shape.GetHitInfo(ray);
         statistics.DrawableIntersections++;
         state.Statistics = statistics;

         if (state.Hits && state.TMin > (t0 + Constants.Epsilon) && (state.TMin + Constants.Epsilon) < t1) {
            if (closestStateToRay == null) {
               closestStateToRay = state;
            }
            if (state.TMin < closestStateToRay.TMin) {
               closestStateToRay = state;
            }
         }
      }

      return closestStateToRay;
   }






   @Override
   public String Compile(TracerOptions _tracerOptions) {
      return "No scene compilation necessary.";
   }

}