package net.danielthompson.danray.shading.bxdf;

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

   public GlossyBRDF(float gloss) {
      Gloss = gloss;
   }

   @Override
   public float f(float thetaIncoming, float thetaOutgoing) {

      float lambertF = lambertianBRDF.f(thetaIncoming, thetaOutgoing);
      float mirrorF = mirrorBRDF.f(thetaIncoming, thetaOutgoing);

      float f = GeometryCalculations.Lerp(mirrorF, Gloss, lambertF, (1.0f - Gloss));

      return f;
   }

   @Override
   public float f(Vector incoming, Normal normal, Vector outgoing) {

      float lambertF = lambertianBRDF.f(incoming, normal, outgoing);
      float mirrorF = mirrorBRDF.f(incoming, normal, outgoing);

      float f = GeometryCalculations.Lerp(mirrorF, Gloss, lambertF, (1.0f - Gloss));

      return f;
   }

   @Override
   public Vector getVectorInPDF(Normal normal, Vector incoming) {

      Vector lambertVector = lambertianBRDF.getVectorInPDF(normal, incoming);
      Vector mirrorVector = mirrorBRDF.getVectorInPDF(normal, incoming);

      Vector lerp = Vector.Lerp(mirrorVector, Gloss, lambertVector, (1.0f - Gloss));

      return lerp;

   }
}