package net.danielthompson.danray.exports.internal;

import net.danielthompson.danray.shapes.Cylinder;
import net.danielthompson.danray.shapes.ImplicitPlane;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by dthompson on 3/2/2016.
 */
public class ImplicitPlaneExporter {
   public static Element Process(ImplicitPlane object, Document document, Element parent) {

      Element rootElement = document.createElement("ImplicitPlane");
      rootElement.appendChild(PointExporter.Process(object.Origin, document, rootElement));
      rootElement.appendChild(NormalExporter.Process(object.Normal, document, rootElement));
      rootElement.appendChild(AbstractShapeExporter.Process(object, document, rootElement));

      parent.appendChild(rootElement);
      return rootElement;
   }
}
