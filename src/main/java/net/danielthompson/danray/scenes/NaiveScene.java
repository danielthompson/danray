package net.danielthompson.danray.scenes;

import net.danielthompson.danray.cameras.Camera;
import net.danielthompson.danray.presets.TracerOptions;
import net.danielthompson.danray.shapes.AbstractShape;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.Ray;

/**
 * DanRay
 * User: dthompson
 * Date: 6/28/13
 * Time: 5:24 PM
 */
public class NaiveScene extends AbstractScene {

   public NaiveScene(Camera camera) {
      super(camera);
      ImplementationType = "Naive Scene";
   }

   @Override
   public IntersectionState getNearestShape(Ray ray) {
      return getNearestShapeBetween(ray, 0, Double.MAX_VALUE);
   }

   @Override
   public IntersectionState getNearestShapeBeyond(Ray ray, double t) {
      return getNearestShapeBetween(ray, t, Double.MAX_VALUE);
   }

   @Override
   public IntersectionState getNearestShapeBetween(Ray ray, double t0, double t1) {
      IntersectionState closestStateToRay = null;

      for (AbstractShape shape : Shapes) {
         IntersectionState state = shape.getHitInfo(ray);

         //boolean replace = (state.Hits && state.TMin >= t0 && state.TMin <= t1) && (closestStateToRay == null || state.TMin < closestStateToRay.TMin);

         //closestStateToRay = (state.Hits && state.TMin >= t0 && state.TMin <= t1) && (closestStateToRay == null || state.TMin < closestStateToRay.TMin) ? state : closestStateToRay;


         if (state.Hits && state.TMin >= (t0 /*+ Constants.Epsilon*/) && (state.TMin/* + Constants.Epsilon*/) <= t1) {
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
   public String compile(TracerOptions _tracerOptions) {
      return super.compile(_tracerOptions);

   }

}