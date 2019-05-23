package net.danielthompson.danray.scenes;

import net.danielthompson.danray.cameras.Camera;
import net.danielthompson.danray.lights.AbstractLight;
import net.danielthompson.danray.presets.TracerOptions;

import net.danielthompson.danray.scenes.skyboxes.AbstractSkybox;
import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.shapes.AbstractShape;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.*;

import java.awt.*;
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

   public SpectralPowerDistribution getEnvironmentColor(Vector3 v) {
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
         boolean hits = shape.Hits(ray);
         test = (hits && ray.MinT >= Constants.Epsilon && ray.MinT < closestT);
         nearestShapeIndex = test ? i : nearestShapeIndex;
         closestT = test ? ray.MinT : closestT;
      }

      Intersection closestIntersection = null;

      if (nearestShapeIndex >= 0) {
         closestIntersection = shapes.get(nearestShapeIndex).GetHitInfo(ray);
         // flip the normal if the ray is approaching from the other side
         if (closestIntersection.Normal.Dot(ray.Direction) > 0)
            closestIntersection.Normal.Scale(-1);
         if (closestIntersection != null && Float.isNaN(closestIntersection.Location.x)) {
            // wtf?
            closestIntersection = shapes.get(nearestShapeIndex).GetHitInfo(ray);
         }
      }




      return closestIntersection;
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
