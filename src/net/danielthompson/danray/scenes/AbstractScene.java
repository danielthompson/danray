package net.danielthompson.danray.scenes;

import net.danielthompson.danray.cameras.Camera;
import net.danielthompson.danray.lights.AbstractLight;
import net.danielthompson.danray.presets.TracerOptions;
import net.danielthompson.danray.shapes.Shape;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Statistics;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 1/4/15.
 */
public abstract class AbstractScene {
   public int numFrames = 1;
   public Camera Camera;

   public Statistics statistics;

   public String ImplementationType = "Base Scene";

   public List<AbstractLight> Lights;
   public List<Shape> Shapes;

   public AbstractScene(Camera camera) {
      Camera = camera;
      Shapes = new ArrayList<>();
      Lights = new ArrayList<>();
   }

   public void addShape(Shape shape) {
      Shapes.add(shape);
   }

   public void addLight(AbstractLight light) {
      Lights.add(light);
   }

   public abstract IntersectionState getNearestShape(Ray ray);

   public abstract IntersectionState getNearestShapeBeyond(Ray ray, double t);

   public IntersectionState getNearestShapeBetween(Ray ray, double t0, double t1) {
      return null;
   }

   public abstract String compile(TracerOptions _tracerOptions);
}