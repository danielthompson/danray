package net.danielthompson.danray.exports.internal;

import net.danielthompson.danray.shading.bxdf.BRDF;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.*;

/**
 * Created by dthompson on 3/2/2016.
 */
public class BRDFExporter {
   public static Element Process(BRDF object, Document document, Element parent) {

      Element rootElement = document.createElement("BRDF");
      rootElement.setAttribute("Type", String.valueOf(object.getClass().getSimpleName()));
      parent.appendChild(rootElement);

      return rootElement;
   }
}
