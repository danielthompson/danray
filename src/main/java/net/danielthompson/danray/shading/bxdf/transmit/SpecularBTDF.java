package net.danielthompson.danray.shading.bxdf.transmit;

import net.danielthompson.danray.shading.bxdf.BTDF;
import net.danielthompson.danray.structures.Normal;
import net.danielthompson.danray.structures.Vector3;

public class SpecularBTDF extends BTDF {

   public SpecularBTDF() {
      Delta = true;
   }

   @Override
   public Vector3 getVectorInPDF(Normal normal, Vector3 incoming, float leavingIndexOfRefraction, float enteringIndexOfRefraction) {
      // use snell's law to compute vector
      // sin θ₂ = (n₁ * sin θ₁) / n₂
      // θ₂ = arcsin((n₁ * sin θ₁) / n₂)

      float r = leavingIndexOfRefraction / enteringIndexOfRefraction;

      Normal tempNormal = new Normal(normal.X, normal.Y, normal.Z);
      float c = Normal.Scale(tempNormal, -1).Dot(incoming);

      if (c < 0) {
         tempNormal.Scale(-1);
         c = Normal.Scale(tempNormal, -1).Dot(incoming);
      }

      Vector3 addend1 = Vector3.scale(incoming, r);

      float term1 = r * c;
      float term2 = 1 - c * c;
      float term3 = r * r;
      float term4 = 1 - term3 * term2;
      float term5 = term1 - (float)Math.sqrt(term4);
      Normal addend2 = Normal.Scale(tempNormal, term5);

      Vector3 refracted = new Vector3(addend1.x + addend2.X, addend1.y + addend2.Y, addend1.z + addend2.Z);
      return refracted;
   }

   @Override
   public float f(Vector3 incoming, Normal normal, Vector3 outgoing) {
      return 0;
   }
}
