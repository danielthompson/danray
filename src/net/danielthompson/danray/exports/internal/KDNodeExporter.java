package net.danielthompson.danray.exports.internal;

import net.danielthompson.danray.acceleration.KDNode;
import net.danielthompson.danray.shapes.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by dthompson on 3/2/2016.
 */
public class KDNodeExporter {
   public static Element Process(KDNode object, Document document, Element parent) {

      Element rootElement = document.createElement("KDNode");

      rootElement.setAttribute("Split", String.valueOf(object.Split));
      if (object.Axis != null) {
         rootElement.setAttribute("Axis", String.valueOf(object.Axis.name()));
      }
      rootElement.setAttribute("Leaf", String.valueOf(object.isLeaf()));

      Element shapeListElement = document.createElement("Shapes");
      if (object._objects != null && object._objects.size() > 0) {
         for (Shape shape : object._objects) {
            Element shapeElement = document.createElement("Shape");
            shapeElement.setAttribute("ID", String.valueOf(shape.getID()));
            shapeListElement.appendChild(shapeElement);
         }
      }
      rootElement.appendChild(shapeListElement);

      if (object._leftChild != null) {
         Element leftChild = KDNodeExporter.Process(object._leftChild, document, rootElement);
         leftChild.setAttribute("Side", String.valueOf("Left"));
         rootElement.appendChild(leftChild);

      }
      if (object._rightChild != null) {
         Element rightChild = KDNodeExporter.Process(object._rightChild, document, rootElement);
         rightChild.setAttribute("Side", String.valueOf("Right"));
         rootElement.appendChild(rightChild);
      }

      parent.appendChild(rootElement);

      return rootElement;
   }
}
