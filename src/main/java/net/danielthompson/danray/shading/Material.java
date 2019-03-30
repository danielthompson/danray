package net.danielthompson.danray.shading;

import net.danielthompson.danray.shading.bxdf.BRDF;
import net.danielthompson.danray.shading.bxdf.BTDF;
import net.danielthompson.danray.shading.bxdf.BxDF;
import net.danielthompson.danray.shading.bxdf.subsurface.SubsurfaceScatteringFunction;

import java.util.ArrayList;
import java.util.List;

public class Material {

   public final List<BxDF> BxDFs = new ArrayList<>();
   public final List<Float> Weights = new ArrayList<>();

   public ReflectanceSpectrum ReflectanceSpectrum;
   public BRDF BRDF;
   public float BRDFweight = 1.0f;
   public BTDF BTDF;
   public float BTDFweight = 1.0f;

   public SubsurfaceScatteringFunction BSSRDF;
   public float BSSRDFweight = 1.0f;

   public float IndexOfRefraction;

   public Material() {
      // intentionally empty
   }

   public Material(ReflectanceSpectrum spectrum, float indexOfRefraction) {
      ReflectanceSpectrum = spectrum;
      IndexOfRefraction = indexOfRefraction;
   }
}
