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

//      if (object.Color != null) {
//         rootElement.appendChild(ColorExporter.Process(object.Color, document, rootElement));
//      }
//      if (object.FullSpectralReflectanceCurve != null) {
//         rootElement.appendChild(SRCExporter.Process(object.FullSpectralReflectanceCurve, document, rootElement));
//      }
//      if (object.reflect != null) {
//         rootElement.appendChild(BRDFExporter.Process(object.reflect, document, rootElement));
//      }

      rootElement.setAttribute("IndexOfRefraction", String.valueOf(object.IndexOfRefraction));
      parent.appendChild(rootElement);


      parent.appendChild(rootElement);

      return rootElement;
   }
}
