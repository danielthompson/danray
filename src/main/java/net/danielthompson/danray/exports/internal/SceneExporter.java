package net.danielthompson.danray.exports.internal;

import net.danielthompson.danray.acceleration.KDScene;
import net.danielthompson.danray.shapes.*;
import net.danielthompson.danray.scenes.AbstractScene;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by dthompson on 14 Aug 15.
 */
public class SceneExporter {

   public static Element Process(AbstractScene object, Document document, Element parent) {

      Element rootElement = document.createElement("Scene");
      rootElement.setAttribute("Type", String.valueOf(object.getClass().getSimpleName()));
      rootElement.setAttribute("version", String.valueOf(1));

      rootElement.appendChild(CameraExporter.Process(object.Camera, document, rootElement));

      if (object.Shapes != null) {
         Element shapeList = document.createElement("Shapes");
         rootElement.appendChild(shapeList);
         for (AbstractShape shape : object.Shapes) {
            if (shape instanceof Box) {
               shapeList.appendChild(BoxExporter.Process((Box)shape, document, shapeList));
            }
            else if (shape instanceof Cylinder) {
               shapeList.appendChild(CylinderExporter.Process((Cylinder)shape, document, shapeList));
            }
            else if (shape instanceof ImplicitPlane) {
               shapeList.appendChild(ImplicitPlaneExporter.Process((ImplicitPlane)shape, document, shapeList));
            }
            else if (shape instanceof Sphere) {
               shapeList.appendChild(SphereExporter.Process((Sphere)shape, document, shapeList));
            }
         }
      }

      if (object instanceof KDScene) {
         KDScene kdScene = (KDScene)object;
         if (kdScene.rootNode != null) {

            Element kdTreeElement = KDNodeExporter.Process(kdScene.rootNode, document, rootElement);
            parent.appendChild(kdTreeElement);
         }
      }

      parent.appendChild(rootElement);

      return rootElement;
   }
}