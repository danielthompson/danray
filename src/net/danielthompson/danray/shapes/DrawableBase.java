package net.danielthompson.danray.shapes;

import net.danielthompson.danray.acceleration.KDAxis;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.BoundingBox;
import net.danielthompson.danray.structures.Ray;

/**
 * Created by daniel on 2/16/15.
 */
public abstract class DrawableBase implements Drawable {

   public int ID;

   @Override
   public double GetVolume() {
      return 0;
   }

   @Override
   public BoundingBox GetWorldBoundingBox() {
      return null;
   }

   public int getID() {
      return ID;
   }

   public BoundingBox GetObjectBoundingBox() {
      return null;
   }

   @Override
   public double getMedian(KDAxis axis) {
      return 0;
   }

   @Override
   public IntersectionState GetHitInfo(Ray ray) {
      return null;
   }

   @Override
   public boolean Hits(Ray ray) {
      return false;
   }

   @Override
   public double getSurfaceArea() {return 0;}
}
