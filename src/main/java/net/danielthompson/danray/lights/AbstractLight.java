package net.danielthompson.danray.lights;

import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.shapes.AbstractShape;
import net.danielthompson.danray.structures.*;

/**
 * Created by daniel on 6/25/16.
 */
public abstract class AbstractLight extends AbstractShape {

   public int ID;

   public SpectralPowerDistribution SpectralPowerDistribution;

   public AbstractLight(SpectralPowerDistribution spd) {
      super(null);
      SpectralPowerDistribution = spd;
   }

   /**
    * Returns a random point on the surface of the light, in world space.
    * @return A random point on the surface of the light, in world space.
    */
   public abstract Point getRandomPointOnSurface();

   /**
    * Returns a random point on the surface of the light, in world space, that is within the hemisphere
    * centered about the provided vector from the origin.
    * @param side The direction from the origin for which to provide a point.
    * @return A random point on the surface of the light, in world space.
    */
   public abstract Point getRandomPointOnSideOf(Vector side);

   /**
    * Returns a random point on the surface of the light, in world space, such that that the point is
    * within the hemisphere centered about a vector from the origin to the provided point.
    * @param point The point that defines the direction from the origin for which to provide a point.
    * @return A random point on th esurface of the light, in world space.
    */
   public abstract Point getRandomPointOnSideOf(Point point);

   public abstract Ray getRandomRayInPDF();

   /**
    * Returns the probability [0, 1] that the given direction to the given point
    * will be sampled by this light's SampleL method.
    * @param point The point (in world space) which the light might illuminate.
    * @param directionFromLightToPoint The vector from a previously determined point on the light to the given point.
    * @return The probability [0, 1] that the direction to the point will be sampled.
    */
   public abstract float getPDF(Point point, Vector directionFromLightToPoint);
}
