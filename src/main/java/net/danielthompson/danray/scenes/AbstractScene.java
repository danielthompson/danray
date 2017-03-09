package net.danielthompson.danray.scenes;

import net.danielthompson.danray.cameras.Camera;
import net.danielthompson.danray.lights.AbstractLight;
import net.danielthompson.danray.presets.TracerOptions;

import net.danielthompson.danray.shapes.AbstractShape;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.Ray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 1/4/15.
 */
public abstract class AbstractScene {
   public int numFrames = 1;
   public Camera Camera;

   public String ImplementationType = "Base Scene";

   public List<AbstractLight> Lights;
   public List<AbstractShape> Shapes;

   public AbstractScene(Camera camera) {
      Camera = camera;
      Shapes = new ArrayList<>();
      Lights = new ArrayList<>();
   }

   public void addShape(AbstractShape shape) {
      Shapes.add(shape);
   }

   public void addLight(AbstractLight light) {
      Lights.add(light);
   }

   public abstract IntersectionState getNearestShape(Ray ray);

   public abstract IntersectionState getNearestShapeBeyond(Ray ray, float t);

   public IntersectionState getNearestShapeBetween(Ray ray, float t0, float t1) {
      return null;
   }

   public String compile(TracerOptions _tracerOptions) {
      for (AbstractShape shape : Shapes) {
         shape.RecalculateWorldBoundingBox();
      }

      for (AbstractLight light : Lights) {
         light.RecalculateWorldBoundingBox();
      }

      return "Bounding boxes recalculated.\r\n";
   }
}
