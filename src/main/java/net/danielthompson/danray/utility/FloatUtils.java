package net.danielthompson.danray.utility;

import net.danielthompson.danray.structures.Constants;

public class FloatUtils {

   /**
    * Cribbed from PBRT - thanks Matt!
    * http://www.pbr-book.org/3ed-2018/Shapes/Managing_Rounding_Error.html#x1-ErrorPropagation
    * @param n
    * @return
    */
   public static float gamma(int n) {
      return (n * Constants.MachineEpsilon) / (1 - n * Constants.MachineEpsilon);
   }

}
