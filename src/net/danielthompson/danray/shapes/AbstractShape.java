package net.danielthompson.danray.shapes;

import net.danielthompson.danray.acceleration.KDAxis;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.BoundingBox;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Transform;

/**
 * Created by daniel on 2/16/15.
 */
public abstract class AbstractShape implements Shape {

   public int ID;

   public BoundingBox WorldBoundingBox;

   public Material Material;
   public Transform ObjectToWorld;
   public Transform WorldToObject;

   public AbstractShape(Material material) {
      this.Material = material;
   }

   @Override
   public double GetVolume() {
      return 0;
   }

   @Override
   public BoundingBox GetWorldBoundingBox() {
      return WorldBoundingBox;
   }

   public int getID() {
      return ID;
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

   @Override
   public Material GetMaterial() {
      return Material;
   }
}
