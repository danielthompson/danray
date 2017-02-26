package net.danielthompson.danray.exports.internal;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

/**
 * Created by dthompson on 3/1/2016.
 */
public class UnitTestExporter {

   private File _file;
   public Element RootElement;
   public Document Document;

   public UnitTestExporter(File file) {

      _file = file;

      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      try {
         DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

         // root element
         Document = docBuilder.newDocument();
         RootElement = Document.createElement("UnitTestRoot");
         Document.appendChild(RootElement);
      }
      catch (ParserConfigurationException e) {
         e.printStackTrace();
      }
   }

   public StreamResult Process(IExporter exporter) {

      try {
         RootElement.setAttribute("Version", "1");

         RootElement.appendChild(exporter.Process(Document, RootElement));

         // write the content into xml file
         TransformerFactory transformerFactory = TransformerFactory.newInstance();
         Transformer transformer = transformerFactory.newTransformer();
         transformer.setOutputProperty(OutputKeys.INDENT, "yes");
         transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
         DOMSource source = new DOMSource(Document);
         if (_file != null) {
            if (_file.exists())
               _file.delete();
            try {
               _file.createNewFile();
               StreamResult result = new StreamResult(_file);
               transformer.transform(source, result);
            }
            catch (IOException e) {
               e.printStackTrace();

            }
         }
         // Output to console for testing
         StreamResult console = new StreamResult(System.err);
         //transformer.transform(source, console);

         return console;


      } catch (TransformerException e) {
         e.printStackTrace();
      }

      return null;

   }
}
