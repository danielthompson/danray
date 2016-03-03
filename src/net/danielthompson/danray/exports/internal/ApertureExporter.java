package net.danielthompson.danray.exports.internal;

import net.danielthompson.danray.cameras.apertures.Aperture;
import net.danielthompson.danray.structures.BoundingBox;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by dthompson on 3/2/2016.
 */
public class ApertureExporter {
   public static Element Process(Aperture object, Document document, Element parent) {

      Element rootElement = document.createElement("Aperture");
      rootElement.setAttribute("Type", String.valueOf(object.getClass().getSimpleName()));
      rootElement.setAttribute("Size", String.valueOf(object.Size));
      parent.appendChild(rootElement);

      return rootElement;
   }
}
