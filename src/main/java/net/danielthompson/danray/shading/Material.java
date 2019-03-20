package net.danielthompson.danray.shading;

import net.danielthompson.danray.shading.bxdf.BRDF;
import net.danielthompson.danray.shading.bxdf.BTDF;

public class Material {

   public ReflectanceSpectrum ReflectanceSpectrum;
   public BRDF BRDF;
   public float BRDFweight = 1.0f;
   public BTDF BTDF;
   public float BTDFweight = 1.0f;
   public float IndexOfRefraction;

   public Material() {
      // intentionally empty
   }

   public Material(ReflectanceSpectrum spectrum, float indexOfRefraction) {
      ReflectanceSpectrum = spectrum;
      IndexOfRefraction = indexOfRefraction;
   }
}
