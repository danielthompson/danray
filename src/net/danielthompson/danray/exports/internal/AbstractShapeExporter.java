package net.danielthompson.danray.exports.internal;

import net.danielthompson.danray.cameras.apertures.Aperture;
import net.danielthompson.danray.shapes.AbstractShape;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by dthompson on 3/2/2016.
 */
public class AbstractShapeExporter {
   public static Element Process(AbstractShape object, Document document, Element parent) {

      Element rootElement = document.createElement("AbstractShape");
      rootElement.setAttribute("ID", String.valueOf(object.ID));

      rootElement.appendChild(BoundingBoxExporter.Process(object.WorldBoundingBox, document, rootElement));
      rootElement.appendChild(MaterialExporter.Process(object.Material, document, rootElement));

      if (object.ObjectToWorld != null) {
         Element objectToWorld = TransformExporter.Process(object.ObjectToWorld, document, rootElement);
         objectToWorld.setAttribute("Type", String.valueOf("ObjectToWorld"));
         rootElement.appendChild(objectToWorld);
      }

      if (object.WorldToObject != null) {
         Element worldToObject = TransformExporter.Process(object.WorldToObject, document, rootElement);
         worldToObject.setAttribute("Type", String.valueOf("WorldToObject"));
         rootElement.appendChild(worldToObject);
      }

      parent.appendChild(rootElement);

      return rootElement;
   }
}
