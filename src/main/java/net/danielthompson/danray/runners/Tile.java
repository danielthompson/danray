package net.danielthompson.danray.runners;

public class Tile {
   public final int minx;
   public final int miny;
   public final int maxx;
   public final int maxy;

   public Tile(final int minx, final int miny, final int maxx, final int maxy) {
      this.minx = minx;
      this.miny = miny;
      this.maxx = maxx;
      this.maxy = maxy;
   }
}
