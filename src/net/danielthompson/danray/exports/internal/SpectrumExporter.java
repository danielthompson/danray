package net.danielthompson.danray.exports.internal;

import net.danielthompson.danray.shading.Spectrum;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by dthompson on 14 Aug 15.
 */
public class SpectrumExporter {

   public static Element Process(Spectrum object, Document document, Element parent) {

      Element rootElement = document.createElement("Spectrum");
      rootElement.setAttribute("NumBuckets", String.valueOf(object.numBuckets));
      rootElement.setAttribute("StartLambda", String.valueOf(object.startLambda));
      rootElement.setAttribute("EndLambda", String.valueOf(object.endLambda));
      rootElement.setAttribute("BucketWidth", String.valueOf(object.bucketWidth));

      // child elements
      for (int i = 0; i < object.Buckets.length; i++) {
         Element bucket = document.createElement("Bucket");
         bucket.setAttribute("Power", String.valueOf(object.Buckets[i]));
         bucket.setAttribute("Wavelength", String.valueOf(((i + 38) * 10)));
         rootElement.appendChild(bucket);
      }

      parent.appendChild(rootElement);

      return rootElement;
   }
}