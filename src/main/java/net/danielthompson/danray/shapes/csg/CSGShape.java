package net.danielthompson.danray.shapes.csg;

import net.danielthompson.danray.shapes.AbstractShape;
import net.danielthompson.danray.structures.Ray;

public class CSGShape {
   public CSGOperation Operation;

   public CSGShape LeftCSGShape;
   public AbstractShape LeftShape;

   public CSGShape RightCSGShape;
   public AbstractShape RightShape;

   public boolean hits(Ray ray) {
      return false;
   }
}
