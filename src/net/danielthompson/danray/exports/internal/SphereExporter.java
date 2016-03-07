package net.danielthompson.danray.exports.internal;

import net.danielthompson.danray.shapes.Box;
import net.danielthompson.danray.shapes.Sphere;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by dthompson on 3/2/2016.
 */
public class SphereExporter {
   public static Element Process(Sphere object, Document document, Element parent) {

      Element rootElement = document.createElement("Sphere");

      rootElement.appendChild(PointExporter.Process(object.Origin, document, rootElement));
      rootElement.setAttribute("Radius", String.valueOf(object.Radius));
      rootElement.appendChild(AbstractShapeExporter.Process(object, document, rootElement));
      parent.appendChild(rootElement);

      return rootElement;
   }
}
