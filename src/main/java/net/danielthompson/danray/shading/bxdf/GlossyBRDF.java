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


      if (gloss == 1.0f) {
         Delta = true;
         Glossy = false;
      }
      else {
         Delta = false;
         Glossy = true;
      }
   }

   @Override
   public float f(float thetaIncoming, float thetaOutgoing) {

      float lambertF = lambertianBRDF.f(thetaIncoming, thetaOutgoing);
//      float mirrorF = mirrorBRDF.f(thetaIncoming, thetaOutgoing);
      float mirrorF = 1;

      float f = GeometryCalculations.Lerp(mirrorF, Gloss, lambertF, (1.0f - Gloss));
//      float f = GeometryCalculations.Lerp(mirrorF, (1.0f - Gloss), lambertF, Gloss);

      return f;
   }

   @Override
   public float f(Vector incoming, Normal normal, Vector outgoing) {

      float lambertF = lambertianBRDF.f(incoming, normal, outgoing);
//      float mirrorF = mirrorBRDF.f(incoming, normal, outgoing);
      float mirrorF = 1;

      float f = GeometryCalculations.Lerp(mirrorF, Gloss, lambertF, (1.0f - Gloss));

      return f;
   }

   @Override
   public Vector getVectorInPDF(Normal normal, Vector incoming) {

      Vector mirrorVector = mirrorBRDF.getVectorInPDF(normal, incoming);

      Normal mirrorAsNormal = new Normal(mirrorVector);

      Vector lerp;

      float dot = 0.0f;

//      do {
         Vector lambertVector = lambertianBRDF.getVectorInPDF(mirrorAsNormal, incoming);
         lerp = Vector.Slerp(mirrorVector, Gloss, lambertVector, (1.0f - Gloss));

//         dot = lerp.Dot(normal);
//
////         if (lerp.X == Float.NaN) {
////            // wtf?
////            lerp = Vector.Slerp(mirrorVector, Gloss, lambertVector, (1.0f - Gloss));
////
////            dot = lerp.Dot(normal);
////
////         }
//
//      } while (dot < 0);




//      Vector lerp = Vector.Lerp(mirrorVector, Gloss, lambertVector, (1.0f - Gloss));
      //Vector lerp = Vector.Slerp(mirrorVector, Gloss, lambertVector, (1.0f - Gloss));
//      Vector lerp = Vector.Lerp(mirrorVector, (1.0f - Gloss), lambertVector, Gloss);

      return lerp;

   }
}