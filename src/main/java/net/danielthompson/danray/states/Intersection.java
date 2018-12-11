package net.danielthompson.danray.states;

import net.danielthompson.danray.shapes.AbstractShape;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Normal;
import net.danielthompson.danray.structures.Vector;

import java.util.List;

/**
 * DanRay
 * User: dthompson
 * Date: 6/28/13
 * Time: 5:59 PM
 */
public class Intersection {
   public Point Location;
   public float TMin;
   public float TMax;
   public boolean Hits;
   public AbstractShape Shape;
   public List<Point> Face;
   public Normal Normal;
   public Vector TangentU;
   public Vector TangentV;

   public float x;
   public float y;

   public int KDHeatCount;

   public int InstanceID;

   public Intersection() {
      TMin = -Float.MAX_VALUE;
   }
}
