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


   public float _reflectivity;
   public float _transparency;
   public float _indexOfRefraction;
   public float _specular;

   public Material() {

   }

   public Material (ReflectanceSpectrum spectrum, float reflectivity, float transparency, float indexOfRefraction, float specular) {
      ReflectanceSpectrum = spectrum;
      _reflectivity = reflectivity;
      _transparency = transparency;
      _indexOfRefraction = indexOfRefraction;
      _specular = specular;
   }


}
