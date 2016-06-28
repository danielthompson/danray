package net.danielthompson.danray.shapes;

import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.*;


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
      WorldBoundingBox = new BoundingBox(
            new Point(-Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE),
            new Point(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE)
            );
   }

   @Override
   public IntersectionState getHitInfo(Ray ray) {
      double numerator = (Point.Minus(Origin, ray.Origin)).Dot(Normal);
      double denominator = ray.Direction.Dot(Normal);

      IntersectionState state = new IntersectionState();

      // if they are orthogonal, then they don't hit.
      if (Constants.WithinEpsilon(denominator, 0.0)) {
         // no intersection
         state.Hits = false;
      }

      // need to check for both normal directions!
      else {
         double T = numerator / denominator;
         if (T > 0.0) {
            state.Hits = true;
            state.TMin = T;
            state.IntersectionPoint = ray.GetPointAtT(state.TMin);
            state.Normal = Normal;
            state.Shape = this;
         }
      }

      return state;
   }




}
