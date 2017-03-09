package net.danielthompson.danray.acceleration.compactkd;

import net.danielthompson.danray.acceleration.KDAxis;
import net.danielthompson.danray.shapes.AbstractShape;

import java.util.List;


/**
 * Created by daniel on 11/30/14.
 */
public class KDCompactNode {

   public List<AbstractShape> Shapes;

   public float Split;
   public KDAxis Axis;

   public float p0x;
   public float p0y;
   public float p0z;
   public float p1x;
   public float p1y;
   public float p1z;

   public KDCompactNode(List<AbstractShape> objects) {
      Shapes = objects;
   }

   public KDCompactNode(List<AbstractShape> shapes, KDAxis axis) {
      Shapes = shapes;
      Axis = axis;
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