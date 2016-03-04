package net.danielthompson.danray.structures;

/**
 * Created by daniel on 1/1/15.
 */
public class Statistics {
   public int DrawableIntersections;
   public int BoundingIntersections;

   public int BoundsHitLeft;
   public int BoundsHitRight;

   public int NoBoundsHit;
   public int OneBoundHit;
   public int BothBoundHit;

   public int LeftNodesTraversed;
   public int RightNodesTraversed;

   public Statistics() {
      ;
   }

   public Statistics(int drawableIntersections, int boundingIntersections) {
      this.DrawableIntersections = drawableIntersections;
      this.BoundingIntersections = boundingIntersections;
   }

   public void Add(Statistics that) {
      if (that != null) {
         this.DrawableIntersections += that.DrawableIntersections;
         this.BoundingIntersections += that.BoundingIntersections;
         this.NoBoundsHit += that.NoBoundsHit;
         this.OneBoundHit += that.OneBoundHit;
         this.BothBoundHit += that.BothBoundHit;
         this.BoundsHitLeft += that.BoundsHitLeft;
         this.BoundsHitRight += that.BoundsHitRight;
         this.LeftNodesTraversed += that.LeftNodesTraversed;
         this.RightNodesTraversed += that.RightNodesTraversed;
      }
   }
}
