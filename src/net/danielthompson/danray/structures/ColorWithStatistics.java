package net.danielthompson.danray.structures;

import java.awt.Color;

/**
 * Created by daniel on 1/1/15.
 */
public class ColorWithStatistics {
   public Color Color;
   public Statistics Statistics;

   public ColorWithStatistics() {

   }

   public ColorWithStatistics(Color color, Statistics statistics) {
      this.Color = color;
      this.Statistics = statistics;
   }
}
