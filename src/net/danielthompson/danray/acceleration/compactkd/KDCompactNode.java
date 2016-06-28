package net.danielthompson.danray.acceleration.compactkd;

import net.danielthompson.danray.acceleration.KDAxis;
import net.danielthompson.danray.shapes.Shape;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.BoundingBox;
import net.danielthompson.danray.structures.Ray;

import java.util.List;

/**
 * Created by daniel on 11/30/14.
 */
public class KDCompactNode {

   public List<Shape> Shapes;
   public BoundingBox BoundingBox;

   public double Split;
   public KDAxis Axis;

   public KDCompactNode(List<Shape> objects) {
      Shapes = objects;
   }

   public KDCompactNode(List<Shape> shapes, KDAxis axis) {
      Shapes = shapes;
      Axis = axis;
   }

   public IntersectionState getHitInfo(Ray ray) {
      return BoundingBox.GetHitInfoNew(BoundingBox.point1, BoundingBox.point2, ray);
   }

   @Override
   public String toString() {

      String size = "0";

      if (Shapes != null) {
         size = "" + Shapes.size();
      }

      String axis = "";

      if (Axis != null) {
         axis = " in " + Axis.toString();
      }

      return "Node [" + size + "]" + axis;
   }
}