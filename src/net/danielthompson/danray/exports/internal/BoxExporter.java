package net.danielthompson.danray.exports.internal;

import net.danielthompson.danray.shapes.Box;
import net.danielthompson.danray.shapes.Cylinder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by dthompson on 3/2/2016.
 */
public class BoxExporter {
   public static Element Process(Box object, Document document, Element parent) {

      Element rootElement = document.createElement("BoundingBox");
      rootElement.appendChild(PointExporter.Process(object.point1, document, rootElement));
      rootElement.appendChild(PointExporter.Process(object.point2, document, rootElement));
      rootElement.appendChild(AbstractShapeExporter.Process(object, document, rootElement));
      parent.appendChild(rootElement);

      return rootElement;
   }
}
