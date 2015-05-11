package net.danielthompson.danray.shading;

/**
 * Created by daniel on 3/17/15.
 */
public class Spectrum {

   public float[] Buckets;

   public int numBuckets;

   public int startLambda = 380;
   public int endLambda = 760;
   public int bucketWidth = 10;

   public Spectrum() {
      Buckets = new float[39];
   }

}
