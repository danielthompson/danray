package net.danielthompson.danray.shading.bxdf.subsurface;

import net.danielthompson.danray.structures.Point3;
import net.danielthompson.danray.structures.Vector;

import static net.danielthompson.danray.utility.GeometryCalculations.splitRandom;

public class SubsurfaceScatteringFunction {
   private final float _minDistance;
   private final float _maxDistance;
   public final float Transmittance;

   public SubsurfaceScatteringFunction(float minimumDistance, float maximumDistance, float transmittance) {
      _minDistance = minimumDistance;
      _maxDistance = maximumDistance;
      Transmittance = transmittance;
   }

   /**
    * Gets the next point in direction v from point p.
    * @param p
    * @param v
    * @return
    */
   public Point3 GetNextPoint(Point3 p, Vector v) {
      float distance = (float)splitRandom.get().nextDouble(_minDistance, _maxDistance);
      return Point3.plus(p, Vector.Scale(v, distance));
   }

   public Vector GetVector() {
      float x = (float)(splitRandom.get().nextDouble(2.0) - 1.0);
      float y = (float)(splitRandom.get().nextDouble(2.0) - 1.0);
      float z = (float)(splitRandom.get().nextDouble(2.0) - 1.0);

      Vector v = new Vector(x, y, z);
      v.Normalize();
      return v;
   }
}
