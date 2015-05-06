package net.danielthompson.danray.states;

import net.danielthompson.danray.shapes.Drawable;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Statistics;
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
   public Drawable Drawable;
   public List<Point> Face;
   public Normal Normal;
   public Statistics Statistics;

   public IntersectionState() {
      TMin = -Double.MAX_VALUE;
   }
}
