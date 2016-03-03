package net.danielthompson.danray.exports.internal;

import net.danielthompson.danray.structures.Ray;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.*;

/**
 * Created by dthompson on 3/2/2016.
 */
public class ColorExporter {
   public static Element Process(Color object, Document document, Element parent) {

      Element rootElement = document.createElement("Color");
      rootElement.setAttribute("R", String.valueOf(object.getRed()));
      rootElement.setAttribute("G", String.valueOf(object.getGreen()));
      rootElement.setAttribute("B", String.valueOf(object.getBlue()));
      parent.appendChild(rootElement);

      return rootElement;
   }
}
