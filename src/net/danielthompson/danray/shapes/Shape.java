package net.danielthompson.danray.shapes;

import net.danielthompson.danray.acceleration.BoundingEdge;
import net.danielthompson.danray.acceleration.KDAxis;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.BoundingBox;
import net.danielthompson.danray.structures.Ray;

/**
 * DanRay
 * User: dthompson
 * Date: 6/28/13
 * Time: 5:45 PM
 */
public interface Shape {
   public double GetVolume();
   public BoundingBox GetWorldBoundingBox();

   public int getID();

   public boolean InCurrentKDNode();

   public void SetInCurrentKDNode(boolean value);

   public double getMedian(KDAxis axis);

   IntersectionState GetHitInfo(Ray ray);

   boolean Hits(Ray ray);

   Material GetMaterial();

   double getSurfaceArea();

   BoundingEdge[] GetBoundingEdges(KDAxis axis);

   void SetBoundingEdges(BoundingEdge[] edges, KDAxis axis);
}
