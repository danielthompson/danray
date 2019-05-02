package net.danielthompson.danray.shapes;

import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.*;

import java.util.List;


/**
 * DanRay
 * User: dthompson
 * Date: 7/8/13
 * Time: 1:10 PM
 */
public class ImplicitPlane extends AbstractShape {

   public Point Origin;
   public Normal Normal;

   public ImplicitPlane(Point origin, Normal normal, Material material) {
      super(material);
      this.Origin = origin;
      this.Normal = normal;
      RecalculateWorldBoundingBox();
   }

   @Override
   public void RecalculateWorldBoundingBox() {
      WorldBoundingBox = new BoundingBox(
            new Point(-Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE),
            new Point(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE)
      );
   }

   @Override
   public Intersection GetHitInfo(Ray ray) {
      float numerator = (Point.Minus(Origin, ray.Origin)).Dot(Normal);
      float denominator = ray.Direction.Dot(Normal);

      Intersection state = new Intersection();

      // if they are orthogonal, then they don't hit.
      if (Constants.WithinEpsilon(denominator, 0.0f)) {
         // no intersection
         state.Hits = false;
      }

      // need to check for both normal directions!
      else {
         float T = numerator / denominator;
         if (T > 0.0) {
            state.Hits = true;
            state.t = T;
            state.Location = ray.GetPointAtT(state.t);
            state.Normal = Normal;
            state.Shape = this;
         }
      }

      return state;
   }

   @Override
   public List<Intersection> GetAllHitPoints(Ray ray) {
      return null;
   }


}
