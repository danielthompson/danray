package net.danielthompson.danray.scenes;

import net.danielthompson.danray.cameras.Camera;
import net.danielthompson.danray.lights.AbstractLight;
import net.danielthompson.danray.presets.TracerOptions;

import net.danielthompson.danray.scenes.skyboxes.AbstractSkybox;
import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.shapes.AbstractShape;
import net.danielthompson.danray.shapes.Box;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.*;
import net.danielthompson.danray.structures.Point;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 1/4/15.
 */
public abstract class AbstractScene {
   public int numFrames = 1;
   public Camera Camera;
   public AbstractSkybox Skybox;

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

   public SpectralPowerDistribution getEnvironmentColor(Vector v) {
      if (Skybox == null) {
         return new SpectralPowerDistribution(Color.BLACK);
      }
      else {
         return Skybox.getSkyBoxSPD(v);
      }
   }

   public abstract Intersection getNearestShape(Ray ray, int x, int y);

   public Intersection getNearestShapeIteratively(List<AbstractShape> shapes, Ray ray) {
      int nearestShapeIndex = -1;
      float closestT = ray.MinT;
      boolean test = false;

      for (int i = 0; i < shapes.size(); i++) {
         AbstractShape shape = shapes.get(i);
         boolean hits = shape.hits(ray);
         test = (hits && ray.MinT >= Constants.Epsilon && ray.MinT < closestT);
         nearestShapeIndex = test ? i : nearestShapeIndex;
         closestT = test ? ray.MinT : closestT;
      }

      Intersection closestStateToRay = null;

      if (nearestShapeIndex >= 0) {
         closestStateToRay = shapes.get(nearestShapeIndex).getHitInfo(ray);
         if (Float.isNaN(closestStateToRay.Location.X)) {
            // wtf?
            closestStateToRay = shapes.get(nearestShapeIndex).getHitInfo(ray);
         }
      }

      return closestStateToRay;
   }

   public String compile(TracerOptions _tracerOptions) {
      for (AbstractShape shape : Shapes) {
         if (shape.ObjectToWorld == null) {
            shape.ObjectToWorld = Transform.identity;
            shape.WorldToObject = Transform.identity;
         }

         shape.RecalculateWorldBoundingBox();
      }
      return "Bounding boxes recalculated.\r\n";
   }
}
