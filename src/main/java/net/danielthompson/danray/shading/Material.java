package net.danielthompson.danray.shading;

import net.danielthompson.danray.shading.bxdf.BRDF;

public class Material {

   public ReflectanceSpectrum ReflectanceSpectrum;
   public BRDF BRDF;

   public float _indexOfRefraction;

   public Material() {
      // intentionally empty
   }

   public Material(ReflectanceSpectrum spectrum, float indexOfRefraction) {
      ReflectanceSpectrum = spectrum;
      _indexOfRefraction = indexOfRefraction;
   }


}
