package net.danielthompson.danray.scenes;

import net.danielthompson.danray.cameras.Camera;
import net.danielthompson.danray.config.TracerOptions;
import net.danielthompson.danray.states.Intersection;
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
   public Intersection getNearestShape(Ray ray, int x, int y) {
      return getNearestShapeIteratively(Shapes, ray);
   }


   @Override
   public String compile(TracerOptions _tracerOptions) {
      return super.compile(_tracerOptions);
   }
}