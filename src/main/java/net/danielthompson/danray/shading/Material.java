package net.danielthompson.danray.shading;

import net.danielthompson.danray.shading.bxdf.BRDF;

/**
 * DanRay
 * User: dthompson
 * Date: 7/12/13
 * Time: 5:42 PM
 */
public class Material {
   public ReflectanceSpectrum ReflectanceSpectrum;


   public BRDF BRDF;

   public double _reflectivity;
   public double _transparency;
   public double _indexOfRefraction;
   public double _specular;
   public double _intrinsic;

   public Material() {

   }

   public Material (ReflectanceSpectrum spectrum, double reflectivity, double transparency, double indexOfRefraction, double specular) {
      ReflectanceSpectrum = spectrum;
      _reflectivity = reflectivity;
      _transparency = transparency;
      _indexOfRefraction = indexOfRefraction;
      _specular = specular;
      _intrinsic = 1 - (_reflectivity + _specular + _transparency);
   }


}
