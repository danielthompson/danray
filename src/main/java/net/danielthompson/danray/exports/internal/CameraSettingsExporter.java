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
      rootElement.setAttribute("FocalLength", String.valueOf(object.FocalLength));
      rootElement.setAttribute("ZoomFactor", String.valueOf(object.ZoomFactor));
      rootElement.setAttribute("FocusDistance", String.valueOf(object.FocusDistance));

//      Element orientation = document.createElement("Orientation");
//      orientation.appendChild(RayExporter.Process(object.Orientation, document, rootElement));
//      rootElement.appendChild(orientation);
//      rootElement.appendChild(ApertureExporter.Process(object.Aperture, document, rootElement));

      parent.appendChild(rootElement);

      return rootElement;
   }
}
