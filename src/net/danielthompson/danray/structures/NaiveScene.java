package net.danielthompson.danray.structures;

import net.danielthompson.danray.lights.Radiatable;
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
   public void addDrawableObject(Drawable drawable) {
      Drawables.add(drawable);
   }

   @Override
   public void addRadiatableObject(Radiatable radiatable) {
      Radiatables.add(radiatable);
   }

   @Override
   public IntersectionState GetClosestDrawableToRay(Ray ray) {
      return GetClosestDrawableToRayBeyond(ray, 0);
   }

   public IntersectionState GetClosestDrawableToRayBeyond(Ray ray, double t) {
      IntersectionState closestStateToRay = null;
      statistics = new Statistics();
      for (Drawable drawable : Drawables) {
         IntersectionState state = drawable.GetHitInfo(ray);
         statistics.DrawableIntersections++;
         state.Statistics = statistics;

         if (state.Hits && state.TMin > t) {
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
   public String Compile() {
      return "No scene compilation necessary.";
   }

}