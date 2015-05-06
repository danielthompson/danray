package net.danielthompson.danray.lights;

import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shapes.Sphere;
import net.danielthompson.danray.structures.Point;
import org.apache.commons.math3.util.FastMath;

/**
 * Created by daniel on 3/8/14.
 */
public class SphereLight extends Sphere implements Radiatable {

   protected double Power;

   public SphereLight(double power) {
      Power = power;
   }

   public SphereLight(double power, Material material) {
      super(material);
      Power = power;

   }

   private static double[] randoms;

   static {
      randoms = new double[65536];

      for (int i = 0; i < 65536; i++) {
         randoms[i] = FastMath.random() * 2.0 - 1.0;
      }
   }

   private static int randomPointer;

   private static Object mutex = new Object();

   @Override
   public Point getRandomPointOnSurface() {

      double x;
      double y;
      double z;



      //synchronized (mutex) {
         x = randoms[randomPointer];
         randomPointer = (randomPointer + 1) & 65535;
         y = randoms[randomPointer];
         randomPointer = (randomPointer + 1) & 65535;
         z = randoms[randomPointer];
         randomPointer = (randomPointer + 1) & 65535;
      //}

      Point point = new Point(x, y, z);
      point.Normalize();
      point.Scale(Radius);
      point.Plus(Origin);
      return point;
   }

   @Override
   public double getPower() {
      return Power;
   }
}
