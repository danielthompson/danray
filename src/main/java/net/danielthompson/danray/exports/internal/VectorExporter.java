package net.danielthompson.danray.exports.internal;

import net.danielthompson.danray.structures.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by dthompson on 3/2/2016.
 */
public class VectorExporter {
   public static Element Process(Vector object, Document document, Element parent) {

      Element rootElement = document.createElement("Vector");
      rootElement.setAttribute("X", String.valueOf(object.X));
      rootElement.setAttribute("Y", String.valueOf(object.Y));
      rootElement.setAttribute("Z", String.valueOf(object.Z));
      parent.appendChild(rootElement);

      return rootElement;
   }
}
