package net.danielthompson.danray.exports.internal;

import net.danielthompson.danray.structures.Normal;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by dthompson on 3/2/2016.
 */
public class NormalExporter {
   public static Element Process(Normal object, Document document, Element parent) {

      Element rootElement = document.createElement("normal");
      rootElement.setAttribute("x", String.valueOf(object.x));
      rootElement.setAttribute("y", String.valueOf(object.y));
      rootElement.setAttribute("z", String.valueOf(object.z));
      parent.appendChild(rootElement);

      return rootElement;
   }
}
