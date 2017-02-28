package net.danielthompson.danray.states;

import net.danielthompson.danray.shapes.AbstractShape;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Normal;

import java.util.List;

/**
 * DanRay
 * User: dthompson
 * Date: 6/28/13
 * Time: 5:59 PM
 */
public class IntersectionState {
   public Point IntersectionPoint;
   public double TMin;
   public double TMax;
   public boolean Hits;
   public AbstractShape Shape;
   public List<Point> Face;
   public Normal Normal;

   public int KDHeatCount;

   public int InstanceID;

   public IntersectionState() {
      TMin = -Double.MAX_VALUE;
   }
}
