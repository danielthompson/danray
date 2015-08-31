package net.danielthompson.danray.structures;

import net.danielthompson.danray.cameras.Camera;
import net.danielthompson.danray.lights.Radiatable;
import net.danielthompson.danray.lights.SpectralRadiatable;
import net.danielthompson.danray.shapes.Drawable;
import net.danielthompson.danray.states.IntersectionState;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 1/4/15.
 */
public abstract class Scene {
   public int numFrames = 1;
   public net.danielthompson.danray.cameras.Camera Camera;

   public String ImplementationType = "Base Scene";

   public List<Radiatable> Radiatables;
   public List<Drawable> Drawables;
   public List<SpectralRadiatable> SpectralRadiatables;

   public Scene(Camera camera) {
      Camera = camera;
      Drawables = new ArrayList<>();
      Radiatables = new ArrayList<>();
      SpectralRadiatables = new ArrayList<>();
   }

   public abstract void addDrawableObject(Drawable drawable);

   public abstract void addRadiatableObject(Radiatable radiatable);

   public abstract IntersectionState GetClosestDrawableToRay(Ray ray);

   public abstract IntersectionState GetClosestDrawableToRayBeyond(Ray ray, double t);

   public IntersectionState GetClosestDrawableHitBetween(Ray ray, double t0, double t1) {
      return null;
   }

   public abstract String Compile();
}
