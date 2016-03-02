package net.danielthompson.danray.exports.internal;

import net.danielthompson.danray.structures.Matrix4x4;
import net.danielthompson.danray.structures.Transform;
import org.w3c.dom.Element;
import org.w3c.dom.Document;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by dthompson on 3/1/2016.
 */
public class TransformExporter {

   public static Element Process(Transform object, Document document, Element parent) {

      Element rootElement = document.createElement("Transform");
      parent.appendChild(rootElement);

      Element matrix = document.createElement("Matrix");
      processMatrix(object._matrix, document, matrix);
      rootElement.appendChild(matrix);

      Element inverse = document.createElement("Inverse");
      processMatrix(object._inverse, document, inverse);
      rootElement.appendChild(inverse);

      return rootElement;
   }

   private static void processMatrix(Matrix4x4 matrix, Document document, Element element) {
      // child elements
      for (int row = 0; row < 4; row++) {
         for (int col = 0; col < 4; col++) {
            Element value = document.createElement("Value");
            value.setAttribute("row", String.valueOf(row));
            value.setAttribute("col", String.valueOf(col));
            value.setTextContent(String.valueOf(matrix.matrix[row][col]));
            element.appendChild(value);
         }
      }

   }
}
