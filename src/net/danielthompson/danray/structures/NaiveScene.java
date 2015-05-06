package net.danielthompson.danray.structures;

import net.danielthompson.danray.acceleration.KDNode;
import net.danielthompson.danray.acceleration.KDTree;
import net.danielthompson.danray.lights.Radiatable;
import net.danielthompson.danray.shapes.*;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.cameras.Camera;


import java.util.ArrayList;
import java.util.List;

/**
 * DanRay
 * User: dthompson
 * Date: 6/28/13
 * Time: 5:24 PM
 */
public class NaiveScene extends Scene {


   private List<Drawable> _planes;

   private List<Radiatable> _radiatables;

   public long drawableIntersections;

   public Statistics statistics;

   public NaiveScene(Camera camera) {
      super(camera);
      _drawables = new ArrayList<>();
      _radiatables = new ArrayList<>();
      _planes = new ArrayList<>();
   }

   @Override
   public void addDrawableObject(Drawable drawable) {
      if (drawable instanceof ImplicitPlane) {
         _planes.add(drawable);
      }
      else {
         _drawables.add(drawable);
      }
   }

   @Override
   public void addRadiatableObject(Radiatable radiatable) {
      _radiatables.add(radiatable);
   }

   @Override
   public IntersectionState GetClosestDrawableToRay(Ray ray) {
      IntersectionState closestStateToRay = null;
      statistics = new Statistics();
      for (Drawable drawable : _drawables) {
         IntersectionState state = drawable.GetHitInfo(ray);
         statistics.DrawableIntersections++;
         state.Statistics = statistics;

         if (state.Hits) {
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
   public String getImplementationType() {
      return " naive object array.";
   }

   @Override
   public String Compile() {
      return "No scene compilation necessary.";
   }

}