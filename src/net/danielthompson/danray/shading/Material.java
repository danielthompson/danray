package net.danielthompson.danray.shading;

import java.awt.Color;

/**
 * DanRay
 * User: dthompson
 * Date: 7/12/13
 * Time: 5:42 PM
 */
public class Material {
   private Color _color;
   private double _reflectivity;
   private double _transparency;
   private double _indexOfRefraction;
   private double _specular;




   public Color getColor() {
      return _color;
   }

   public void setColor(Color color) {
      _color = color;
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
      _color = color;
      _reflectivity = reflectivity;
      _transparency = transparency;
      _indexOfRefraction = indexOfRefraction;
      _specular = specular;
   }


}
