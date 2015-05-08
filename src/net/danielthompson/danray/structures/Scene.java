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

   public List<Radiatable> _radiatables;
   public List<Drawable> _drawables;
   public List<SpectralRadiatable> SpectralRadiatables;

   public Scene(Camera camera) {
      Camera = camera;
      _drawables = new ArrayList<>();
      _radiatables = new ArrayList<>();
      //_planes = new ArrayList<>();
      SpectralRadiatables = new ArrayList<>();
   }

   public abstract void addDrawableObject(Drawable drawable);

   public abstract void addRadiatableObject(Radiatable radiatable);

   public abstract IntersectionState GetClosestDrawableToRay(Ray ray);

   public abstract String getImplementationType();

   public abstract String Compile();
}
