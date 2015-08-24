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

   public void setColor(Color color) {
      Color = color;
   }

   public double getReflectivity() {
      return _reflectivity;
   }

   public void setReflectivity(double reflectivity) {
      _reflectivity = reflectivity;
   }

   public void setSpecular(double specular) {
      _specular = specular;
   }

   public double getDiffuse() {
      return 1.0 - _specular;
   }

   public void setDiffuse(double diffuse) {
      _specular = 1.0 - diffuse;
   }

   public double getTransparency() {
      return _transparency;
   }

   public void setTransparency(double transparency) {
      _transparency = transparency;
   }

   public double getIndexOfRefraction() {
      return _indexOfRefraction;
   }

   public void setIndexOfRefraction(double indexOfRefraction) {
      _indexOfRefraction = indexOfRefraction;
   }

   public Material() {

   }

   public Material (Color color, double reflectivity, double transparency, double indexOfRefraction, double specular) {
      Color = color;
      _reflectivity = reflectivity;
      _transparency = transparency;
      _indexOfRefraction = indexOfRefraction;
      _specular = specular;
   }


}
