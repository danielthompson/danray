package net.danielthompson.danray.exports.internal;

import net.danielthompson.danray.structures.BoundingBox;
import net.danielthompson.danray.structures.Point;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by dthompson on 3/2/2016.
 */
public class BoundingBoxExporter {
   public static Element Process(BoundingBox object, Document document, Element parent) {

      Element rootElement = document.createElement("BoundingBox");
      rootElement.appendChild(PointExporter.Process(object.point1, document, rootElement));
      rootElement.appendChild(PointExporter.Process(object.point2, document, rootElement));
      parent.appendChild(rootElement);

      return rootElement;
   }
}
