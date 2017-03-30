package net.danielthompson.danray.scenes;

import net.danielthompson.danray.cameras.Camera;
import net.danielthompson.danray.presets.TracerOptions;
import net.danielthompson.danray.shapes.AbstractShape;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.Constants;
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

      int nearestShapeIndex = -1;

      float closestT = ray.MinT;

      for (int i = 0; i < Shapes.size(); i++) {

         Shapes.get(i).hits(ray);

         float newT = ray.MinT;

         if (newT >= Constants.Epsilon) {
            int j = 0;
         }

         nearestShapeIndex = (newT >= Constants.Epsilon && newT < closestT) ? i : nearestShapeIndex;
      }

      IntersectionState closestStateToRay = null;

      if (nearestShapeIndex >= 0)

         closestStateToRay = Shapes.get(nearestShapeIndex).getHitInfo(ray);

      return closestStateToRay;
   }


   @Override
   public String compile(TracerOptions _tracerOptions) {
      return super.compile(_tracerOptions);

   }

}