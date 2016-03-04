package net.danielthompson.danray.exports.internal;

import net.danielthompson.danray.shading.Material;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by dthompson on 3/2/2016.
 */
public class MaterialExporter {
   public static Element Process(Material object, Document document, Element parent) {

      Element rootElement = document.createElement("Material");

      if (object.Color != null) {
         rootElement.appendChild(ColorExporter.Process(object.Color, document, rootElement));
      }
      if (object.SpectralReflectanceCurve != null) {
         rootElement.appendChild(SRCExporter.Process(object.SpectralReflectanceCurve, document, rootElement));
      }
      if (object.BRDF != null) {
         rootElement.appendChild(BRDFExporter.Process(object.BRDF, document, rootElement));
      }

      rootElement.setAttribute("Reflectivity", String.valueOf(object._reflectivity));
      rootElement.setAttribute("Transparency", String.valueOf(object._transparency));
      rootElement.setAttribute("IndexOfRefraction", String.valueOf(object._indexOfRefraction));
      rootElement.setAttribute("Specular", String.valueOf(object._specular));
      parent.appendChild(rootElement);


      parent.appendChild(rootElement);

      return rootElement;
   }
}