package net.danielthompson.danray.shading.bxdf.reflect;

import net.danielthompson.danray.shading.bxdf.BRDF;
import net.danielthompson.danray.structures.Normal;
import net.danielthompson.danray.structures.Vector3;
import net.danielthompson.danray.utility.GeometryCalculations;

/**
 * Created by daniel on 8/16/15.
 */
public class GlossyBRDF extends BRDF {

   private LambertianBRDF lambertianBRDF = new LambertianBRDF();
   private SpecularBRDF mirrorBRDF = new SpecularBRDF();

   public float Gloss = 0.5f;

   private float _diffuse;

   public GlossyBRDF(float gloss) {
      Gloss = gloss;

      if (gloss == 1.0f) {
         Delta = true;
         Glossy = false;
      }
      else {
         Delta = false;
         Glossy = true;
      }

      _diffuse = 1.0f - gloss;
   }

   @Override
   public float f(float thetaIncoming, float thetaOutgoing) {

      float lambertF = lambertianBRDF.f(thetaIncoming, thetaOutgoing);
      float mirrorF = 1;
      float f = GeometryCalculations.Lerp(mirrorF, Gloss, lambertF, _diffuse);

      return f;
   }

   @Override
   public float f(Vector3 incoming, Normal normal, Vector3 outgoing) {

      float lambertF = lambertianBRDF.f(incoming, normal, outgoing);
//      float mirrorF = mirrorBRDF.f(incoming, normal, outgoing);
      float mirrorF = 1;

      float f = GeometryCalculations.Lerp(mirrorF, Gloss, lambertF, _diffuse);

      return f;
   }

   @Override
   public Vector3 getVectorInPDF(Normal normal, Vector3 incoming, float leavingIndexOfRefraction, float enteringIndexOfRefraction) {

      Vector3 mirrorVector = mirrorBRDF.getVectorInPDF(normal, incoming, 0, 0);

      Normal mirrorAsNormal = new Normal(mirrorVector);

      Vector3 lerp;

      Vector3 lambertVector = lambertianBRDF.getVectorInPDF(mirrorAsNormal, incoming, 0, 0);
      lerp = Vector3.slerp(mirrorVector, Gloss, lambertVector, _diffuse);

      return lerp;

   }
}