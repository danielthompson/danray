package net.danielthompson.danray.shading.bxdf.reflect;

import net.danielthompson.danray.shading.bxdf.BRDF;
import net.danielthompson.danray.structures.Normal;
import net.danielthompson.danray.structures.Vector;
import net.danielthompson.danray.utility.GeometryCalculations;

/**
 * Created by daniel on 8/16/15.
 */
public class GlossyBRDF extends BRDF {

   private LambertianBRDF lambertianBRDF = new LambertianBRDF();
   private MirrorBRDF mirrorBRDF = new MirrorBRDF();

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
   public float f(Vector incoming, Normal normal, Vector outgoing) {

      float lambertF = lambertianBRDF.f(incoming, normal, outgoing);
//      float mirrorF = mirrorBRDF.f(incoming, normal, outgoing);
      float mirrorF = 1;

      float f = GeometryCalculations.Lerp(mirrorF, Gloss, lambertF, _diffuse);

      return f;
   }

   @Override
   public Vector getVectorInPDF(Normal normal, Vector incoming) {

      Vector mirrorVector = mirrorBRDF.getVectorInPDF(normal, incoming);

      Normal mirrorAsNormal = new Normal(mirrorVector);

      Vector lerp;

      Vector lambertVector = lambertianBRDF.getVectorInPDF(mirrorAsNormal, incoming);
      lerp = Vector.Slerp(mirrorVector, Gloss, lambertVector, _diffuse);

      return lerp;

   }
}