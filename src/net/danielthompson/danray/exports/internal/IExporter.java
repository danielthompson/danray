package net.danielthompson.danray.exports.internal;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Created by dthompson on 3/1/2016.
 */
public interface IExporter {
   Element Process(Document document, Element rootElement);
}
