package net.danielthompson.danray.shapes;

import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.BoundingPlane;
import net.danielthompson.danray.structures.Normal;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Ray;

import static net.danielthompson.danray.structures.Constants.WithinDelta;

/**
 * DanRay
 * User: dthompson
 * Date: 7/8/13
 * Time: 1:10 PM
 */
public class ImplicitPlane extends BoundingPlane implements Drawable {

   private Material _material;

   public ImplicitPlane(Point origin, Normal normal, Material material) {
      super(origin, normal);
      _material = material;
   }

   @Override
   public Material GetMaterial() {
      return _material;
   }

   @Override
   public double getSurfaceArea() {
      return Double.POSITIVE_INFINITY;
   }

   @Override
   public IntersectionState GetHitInfo(Ray ray) {
      IntersectionState state = super.GetHitInfo(ray);
      state.Drawable = this;
      return state;
   }


}
