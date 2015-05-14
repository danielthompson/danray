package net.danielthompson.danray.structures;

import net.danielthompson.danray.acceleration.KDAxis;
import net.danielthompson.danray.states.IntersectionState;

/**
 * DanRay
 * User: dthompson
 * Date: 7/24/13
 * Time: 4:29 PM
 */
public interface Boundable {
   public double GetVolume();
   public BoundingBox GetWorldBoundingBox();

   public int getID();

   public double getMedian(KDAxis axis);

   IntersectionState GetHitInfo(Ray ray);

   boolean Hits(Ray ray);
}
