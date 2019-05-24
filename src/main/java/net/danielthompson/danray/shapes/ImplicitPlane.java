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

   public Point3 Origin;
   public Normal Normal;

   public ImplicitPlane(Point3 origin, Normal normal, Material material) {
      super(material);
      this.Origin = origin;
      this.Normal = normal;
      RecalculateWorldBoundingBox();
   }

   @Override
   public void RecalculateWorldBoundingBox() {
      WorldBoundingBox = new BoundingBox(
            new Point3(-Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE),
            new Point3(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE)
      );
   }

   @Override
   public Intersection GetHitInfo(Ray ray) {
      float numerator = (Point3.minus(Origin, ray.Origin)).dot(Normal);
      float denominator = ray.Direction.dot(Normal);

      Intersection state = new Intersection();

      // if they are orthogonal, then they don't hit.
      if (Constants.WithinEpsilon(denominator, 0.0f)) {
         // no intersection
         state.hits = false;
      }

      // need to check for both normal directions!
      else {
         float T = numerator / denominator;
         if (T > 0.0) {
            state.hits = true;
            state.t = T;
            state.location = ray.getPointAtT(state.t);
            state.normal = Normal;
            state.shape = this;
         }
      }

      return state;
   }

   @Override
   public List<Intersection> GetAllHitPoints(Ray ray) {
      return null;
   }


}
