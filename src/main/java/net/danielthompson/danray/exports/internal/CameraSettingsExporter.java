package net.danielthompson.danray.exports.internal;

import net.danielthompson.danray.cameras.CameraSettings;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by dthompson on 3/2/2016.
 */
public class CameraSettingsExporter {
   public static Element Process(CameraSettings object, Document document, Element parent) {

      Element rootElement = document.createElement("CameraSettings");
      rootElement.setAttribute("X", String.valueOf(object.X));
      rootElement.setAttribute("Y", String.valueOf(object.Y));
      parent.appendChild(rootElement);

      return rootElement;
   }
}
