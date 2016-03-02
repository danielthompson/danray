package net.danielthompson.danray.exports;

import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.structures.Scene;
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
public class SceneExporter {

   private Scene _scene;
   private File _file;

   public SceneExporter(Scene scene, File file) {

      _scene = scene;
      _file = file;
   }

   public StreamResult Process() {

      try {
         DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

         // root element
         Document doc = docBuilder.newDocument();
         Element rootElement = doc.createElement("DanRayScene");
         doc.appendChild(rootElement);

         rootElement.setAttribute("Version", "1");

         // child elements
         for (int i = 0; i < _scene.Drawables.size(); i++) {
            Element bucket = doc.createElement("Drawable");

            bucket.setAttribute("wavelength", String.valueOf(((i + 38) * 10)));

            rootElement.appendChild(bucket);
         }

         // write the content into xml file
         TransformerFactory transformerFactory = TransformerFactory.newInstance();
         Transformer transformer = transformerFactory.newTransformer();
         transformer.setOutputProperty(OutputKeys.INDENT, "yes");
         DOMSource source = new DOMSource(doc);
         StreamResult result = new StreamResult(_file);

         transformer.transform(source, result);

         // Output to console for testing
         StreamResult console = new StreamResult(System.err);
         transformer.transform(source, console);

         return result;


      }
      catch (ParserConfigurationException pce) {
         System.out.println(pce.getMessage());
      } catch (TransformerConfigurationException e) {
         e.printStackTrace();
      } catch (TransformerException e) {
         e.printStackTrace();
      }

      return null;

   }
}