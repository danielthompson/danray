package net.danielthompson.danray.states;

import net.danielthompson.danray.shapes.AbstractShape;
import net.danielthompson.danray.structures.Point3;
import net.danielthompson.danray.structures.Normal;
import net.danielthompson.danray.structures.Vector3;

import java.util.List;

/**
 * DanRay
 * User: dthompson
 * Date: 6/28/13
 * Time: 5:59 PM
 */
public class Intersection {

   /**
    * World-space location where the intersection occurs.
    */
   public Point3 location;

   /**
    * Accumulated error in the location
    */
   public Vector3 error = new Vector3(0, 0, 0);

   /**
    * World-space t-value where the ray first intersects the boundary of the shape.
    * If t = 0, the ray's origin is on the boundary of the shape.
    * If t > 0, the origin could be inside or outside the shape.
    * Defaults to Float.MAX_VALUE if there is no intersection.
    */
   public float t;

   /**
    * If true, the ray hits the shape.
    * If false, the ray doesn't hit the shape.
    */
   public boolean hits;

   /**
    * The shape that the ray hits. Null if the ray doesn't hit any shape.
    */
   public AbstractShape shape;

   /**
    * The world-space normal vector at the intersection point.
    */
   public Normal normal;

   /**
    * The world-space vector that is tangent to the normal in the u-texture direction.
    */
   public Vector3 tangentU;

   /**
    * The world-space vector that is tangent to the normal in the v-texture direction.
    */
   public Vector3 tangentV;

   /**
    * The x coordinate of the image pixel that the ray contributes to.
    */
   public float x;

   /**
    * The y coordinate of the image pixel that the ray contributes to.
    */
   public float y;

   /**
    * The u texture coordinate.
    */
   public float u;

   /**
    * The v texture coordinate.
    */
   public float v;

   public int KDHeatCount;

   public Intersection() {
      t = Float.MAX_VALUE;
   }
}
