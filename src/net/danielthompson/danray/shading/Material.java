package net.danielthompson.danray.shading;

import net.danielthompson.danray.shading.bxdf.BRDF;

import java.awt.Color;

/**
 * DanRay
 * User: dthompson
 * Date: 7/12/13
 * Time: 5:42 PM
 */
public class Material {
   public Color Color;

   public SpectralReflectanceCurve SpectralReflectanceCurve;

   public BRDF BRDF;

   public double _reflectivity;
   public double _transparency;
   public double _indexOfRefraction;
   public double _specular;
   public double _intrinsic;

   public Material() {

   }

   public Material (Color color, double reflectivity, double transparency, double indexOfRefraction, double specular) {
      Color = color;
      _reflectivity = reflectivity;
      _transparency = transparency;
      _indexOfRefraction = indexOfRefraction;
      _specular = specular;
      _intrinsic = 1 - (_reflectivity + _specular + _transparency);
   }


}
