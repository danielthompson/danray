package net.danielthompson.danray.shading.bxdf;

import net.danielthompson.danray.structures.Normal;
import net.danielthompson.danray.structures.Vector;

public abstract class BTDF extends BxDF {
   public abstract Vector getVectorInPDF(Normal normal, Vector incoming, float leavingIndexOfRefraction, float enteringIndexOfRefraction);
}
