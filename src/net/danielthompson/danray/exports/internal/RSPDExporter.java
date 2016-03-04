package net.danielthompson.danray.exports.internal;

import net.danielthompson.danray.shading.RelativeSpectralPowerDistribution;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by dthompson on 14 Aug 15.
 */
public class RSPDExporter {

   public static Element Process(RelativeSpectralPowerDistribution object, Document document, Element parent) {

      Element rootElement = document.createElement("RelativeSpectralPowerDistribution");

      rootElement.appendChild(SpectrumExporter.Process(object, document, rootElement));

      parent.appendChild(rootElement);

      return rootElement;
   }
}