package net.danielthompson.danray.exports.internal;

import net.danielthompson.danray.shading.SpectralReflectanceCurve;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by dthompson on 14 Aug 15.
 */
public class SRCExporter {

   public static Element Process(SpectralReflectanceCurve object, Document document, Element parent) {

      Element rootElement = document.createElement("SpectralReflectanceCurve");

      rootElement.appendChild(SpectrumExporter.Process(object, document, rootElement));

      parent.appendChild(rootElement);

      return rootElement;
   }
}