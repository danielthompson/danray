package net.danielthompson.danray.shapes;

import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Transform;

import java.util.List;

public class InterpolatedShape extends AbstractShape {

   public AbstractShape Shape1;
   public AbstractShape Shape2;
   public float t;

   public InterpolatedShape() {
      this(null, null, null);
   }

   public InterpolatedShape(Material material) {
      this(null, null, material);
   }

   public InterpolatedShape(Transform[] transforms, Material material) {
      this(transforms[0], transforms[1], material);
   }

   public InterpolatedShape(Transform objectToWorld, Transform worldToObject, Material material) {
      super(objectToWorld, worldToObject, material);
      recalculateWorldBoundingBox();
   }

   @Override
   public void recalculateWorldBoundingBox() {

   }

   @Override
   public List<Intersection> GetAllHitPoints(Ray ray) {
      return null;
   }
}
