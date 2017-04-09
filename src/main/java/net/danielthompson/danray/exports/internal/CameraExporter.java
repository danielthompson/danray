package net.danielthompson.danray.exports.internal;

import net.danielthompson.danray.cameras.Camera;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by dthompson on 3/2/2016.
 */
public class CameraExporter {
   public static Element Process(Camera object, Document document, Element parent) {

      Element rootElement = document.createElement("Camera");

      rootElement.appendChild(CameraSettingsExporter.Process(object.Settings, document, rootElement));

      rootElement.setAttribute("Type", String.valueOf(object.getClass().getSimpleName()));

      Element orientation = document.createElement("CurrentOrientation");

      rootElement.appendChild(orientation);

      parent.appendChild(rootElement);

      return rootElement;
   }
}
