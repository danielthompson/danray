package test.benchmark;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Created by dthompson on 3/11/2016.
 */
public class IntersectionBenchmarks {

   @BeforeMethod
   public void setUp() throws Exception {

   }

   @AfterMethod
   public void tearDown() throws Exception {

   }
//
//   @Test
//   public void test() throws Exception {
//      for (int i = 0; i < 1; i++) {
//         test1MIntersections();
//      }
//   }
//
//   @Test
//   public void test1MIntersections() throws Exception {
//
//      final int dimension = 20;
//      final int dimensionOver2 = dimension / 2;
//      final int d3 = dimension * dimension * dimension;
//      final long intersections = d3 * d3;
//
//      Ray[] rays = new Ray[d3];
//      BoundingBox[] boundingBoxes = new BoundingBox[d3];
//      Sphere[] spheres = new Sphere[d3];
//
//      int n = 0;
//
//      for (int x = 0 - dimensionOver2; x < dimensionOver2; x++) {
//         for (int y = 0 - dimensionOver2; y < dimensionOver2; y++) {
//            for (int z = 0 - dimensionOver2; z < dimensionOver2; z++) {
//               //points[i] = new Point(x, y, z);
//               //vectors[i] = new Vector(x, y, z);
//               rays[n] = new Ray(new Point(x, y, z), new Vector(x, y, z));
//               boundingBoxes[n] = new BoundingBox(new Point(x, y, z), new Point(x + 1, y + 1, z + 1));
//               spheres[n] = new Sphere(null);
//               spheres[n].Radius = 3;
//               spheres[n].Origin = new Point(x, y, z);
//               n++;
//            }
//         }
//      }
//
//      final long BBStartTime = System.currentTimeMillis();
//
//      for (int i = 0; i < d3; i++) {
//         for (int j = 0; j < d3; j++) {
//            boundingBoxes[i].getHitInfo(rays[j]);
//         }
//      }
//
//      final long BBDuration = System.currentTimeMillis() - BBStartTime;
//
//      long BBrate = intersections * 1000 / BBDuration;
//
//      System.out.println(BBrate + " ray/boundingbox int / sec, duration = [" + BBDuration + "] ms");
//
//      final long SphereStartTime = System.currentTimeMillis();
//
//      for (int i = 0; i < d3; i++) {
//         for (int j = 0; j < d3; j++) {
//            spheres[i].getHitInfo(rays[j]);
//         }
//      }
//
//      final long SphereDuration = System.currentTimeMillis() - SphereStartTime;
//
//      long SphereRate = intersections * 1000 / SphereDuration;
//
//      System.out.println(SphereRate + " ray/sphere int/sec, duration = [" + SphereDuration + "] ms");
//      System.out.println("");
//
//   }

}
