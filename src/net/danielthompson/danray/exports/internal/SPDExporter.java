package net.danielthompson.danray.exports.internal;

import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.structures.Ray;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

/**
 * Created by dthompson on 14 Aug 15.
 */
public class SPDExporter {

   public static Element Process(SpectralPowerDistribution object, Document document, Element parent) {

      Element rootElement = document.createElement("SpectralPowerDistribution");

      rootElement.setAttribute("Power", String.valueOf(object.Power));

      rootElement.appendChild(SpectrumExporter.Process(object, document, rootElement));

      parent.appendChild(rootElement);

      return rootElement;
   }
}