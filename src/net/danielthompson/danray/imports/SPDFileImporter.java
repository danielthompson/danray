package net.danielthompson.danray.imports;

import net.danielthompson.danray.shading.SpectralPowerDistribution;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.xml.sax.*;
import org.w3c.dom.*;

/**
 * Created by dthompson on 14 Aug 15.
 */
public class SPDFileImporter {

   private File _file;

   public SPDFileImporter(File file) {

      _file = file;
   }

   public SpectralPowerDistribution Process() {

      SpectralPowerDistribution spd = new SpectralPowerDistribution();

      Document dom;
      // Make an  instance of the DocumentBuilderFactory
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      try {
         // use the factory to take an instance of the document builder
         DocumentBuilder db = dbf.newDocumentBuilder();
         // parse using the builder to get the DOM mapping of the
         // XML file
         dom = db.parse(_file);

         Element doc = dom.getDocumentElement();

         int version = getIntValue(doc, "version");

         switch (version) {
            case 1:
               System.err.println("Importing version [" + version + "] file.");
               spd = ImportV1(doc);

               break;
            default:
               throw new RuntimeException("Can't import [" + _file.getName() + "], unsupported file version (" + version + ").");
         }

      }
      catch (ParserConfigurationException pce) {
         System.out.println(pce.getMessage());
      }
      catch (SAXException se) {
         System.out.println(se.getMessage());
      }
      catch (IOException ioe) {
         System.err.println(ioe.getMessage());
      }

      return spd;
   }

   public SpectralPowerDistribution ImportV1(Element spdNode) {

      SpectralPowerDistribution spd = new SpectralPowerDistribution();

      spd.Power = getDoubleValue(spdNode, "power");

      System.err.println("SPD has power [" + spd.Power + "]");

      NodeList buckets = spdNode.getElementsByTagName("bucket");

      for (int i = 0; i < buckets.getLength(); i++) {
         Node bucket = buckets.item(i);
         spd.Buckets[i] = getFloatValue(bucket, "power");
      }

      return spd;
   }

   private float getFloatValue(Node node, String attribute) {
      Element e = (Element)node;
      return getFloatValue(e, attribute);
   }

   private float getFloatValue(Element element, String attribute) {
      String value = element.getAttribute(attribute);
      float floatValue = Float.parseFloat(value);

      return floatValue;
   }

   private double getDoubleValue(Node node, String attribute) {
      Element e = (Element)node;
      return getDoubleValue(e, attribute);
   }

   private double getDoubleValue(Element element, String attribute) {
      String value = element.getAttribute(attribute);
      double doubleValue = Double.parseDouble(value);

      return doubleValue;
   }

   private int getIntValue(Element element, String attribute) {
      String value = element.getAttribute(attribute);
      int intValue = Integer.parseInt(value);

      return intValue;
   }

   private int getIntValue(Node node, String attribute) {

      Element e = (Element)node;
      return getIntValue(e, attribute);

   }
}