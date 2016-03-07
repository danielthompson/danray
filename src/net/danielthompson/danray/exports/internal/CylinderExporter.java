package net.danielthompson.danray.exports.internal;

import net.danielthompson.danray.cameras.apertures.Aperture;
import net.danielthompson.danray.shapes.AbstractShape;
import net.danielthompson.danray.shapes.Cylinder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by dthompson on 3/2/2016.
 */
public class CylinderExporter {
   public static Element Process(Cylinder object, Document document, Element parent) {

      Element rootElement = document.createElement("Cylinder");
      rootElement.setAttribute("Radius", String.valueOf(object.Height));
      rootElement.setAttribute("Height", String.valueOf(object.Radius));

      rootElement.appendChild(AbstractShapeExporter.Process(object, document, rootElement));
      parent.appendChild(rootElement);

      return rootElement;
   }
}
