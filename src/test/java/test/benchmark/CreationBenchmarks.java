package test.benchmark;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Created by dthompson on 3/11/2016.
 */
public class CreationBenchmarks {

   @BeforeMethod
   public void setUp() throws Exception {

   }

   @AfterMethod
   public void tearDown() throws Exception {

   }
//
//   @Test
//   public void test() throws Exception {
//      for (int i = 1; i < 10; i++) {
//         test1MIntersectionStates((int)(1000000*(5 * i)));
//      }
//   }
//
//   @Test
//   public void test1MIntersectionStates(int n) throws Exception {
//
//
//
//      intersection[] array = new intersection[n];
//
//      final long startTime = System.currentTimeMillis();
//
//      for (int i = 0; i < n; i++) {
//         array[i] = new intersection();
//      }
//
//      final long endTime = System.currentTimeMillis();
//
//      final long duration = endTime - startTime;
//
//      long rate = (n / duration) * 1000;
//
//      DecimalFormat formatter = new DecimalFormat("#,###");
//
//      System.out.println(formatter.format(rate) + " intersectionstates / sec, [" + n + "] objects, duration = [" + duration + "] ms");
//
//      System.out.println("");
//
//   }
}
