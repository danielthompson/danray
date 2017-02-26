package net.danielthompson.danray.exports.internal;

import net.danielthompson.danray.structures.Ray;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by dthompson on 3/2/2016.
 */
public class RayExporter {
   public static Element Process(Ray object, Document document, Element parent) {

      Element rootElement = document.createElement("Ray");

      rootElement.appendChild(PointExporter.Process(object.Origin, document, rootElement));
      rootElement.appendChild(VectorExporter.Process(object.Direction, document, rootElement));

      parent.appendChild(rootElement);

      return rootElement;
   }
}
