package test.exports;

import net.danielthompson.danray.exports.SPDFileExporter;
import net.danielthompson.danray.exports.internal.*;
import net.danielthompson.danray.shading.RelativeSpectralPowerDistributionLibrary;
import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.structures.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.transform.stream.StreamResult;
import java.io.File;

/**
 * Created by dthompson on 3/1/2016.
 */
public class XMLExportTests {

   private File _dir;

   @BeforeMethod
   public void setUp() throws Exception {
      _dir = new File("test");
      if (!_dir.exists())
         _dir.mkdir();

      _dir = new File("test/exports");
      if (!_dir.exists())
         _dir.mkdir();


   }

   @AfterMethod
   public void tearDown() throws Exception {

   }

   @Test
   public void testTransformExport() throws Exception {

      final Transform object = Transform.Translate(new Vector(500, 200, 900));

      File file = new File(_dir, "transform.xml");

      final UnitTestExporter unitTestExporter = new UnitTestExporter(file);

      IExporter exporter = new IExporter() {
         @Override
         public Element Process(Document document, Element root) {

            return TransformExporter.Process(object, document, root);
         }

      };

      unitTestExporter.Process(exporter);
   }

   @Test
   public void testVectorExport() throws Exception {

      final Vector object = new Vector(500.23, 200, -219.13857);

      File file = new File(_dir, "vector.xml");

      final UnitTestExporter unitTestExporter = new UnitTestExporter(file);

      IExporter exporter = new IExporter() {
         @Override
         public Element Process(Document document, Element root) {

            return VectorExporter.Process(object, document, root);
         }

      };

      unitTestExporter.Process(exporter);
   }

   @Test
   public void testPointExport() throws Exception {

      final Point object = new Point(500.23, 200, -219.13857);

      File file = new File(_dir, "point.xml");

      final UnitTestExporter unitTestExporter = new UnitTestExporter(file);

      IExporter exporter = new IExporter() {
         @Override
         public Element Process(Document document, Element root) {

            return PointExporter.Process(object, document, root);
         }

      };

      unitTestExporter.Process(exporter);
   }


   @Test
   public void testNormalExport() throws Exception {

      final Normal object = new Normal(500.23, 200, -219.13857);

      File file = new File(_dir, "normal.xml");

      final UnitTestExporter unitTestExporter = new UnitTestExporter(file);

      IExporter exporter = new IExporter() {
         @Override
         public Element Process(Document document, Element root) {

            return NormalExporter.Process(object, document, root);
         }

      };

      unitTestExporter.Process(exporter);
   }

   @Test
   public void testRayExport() throws Exception {

      final Ray object = new Ray(new Point(123, -456, -78.932), new Vector(55.23, 200, -219.13857));

      File file = new File(_dir, "ray.xml");

      final UnitTestExporter unitTestExporter = new UnitTestExporter(file);

      IExporter exporter = new IExporter() {
         @Override
         public Element Process(Document document, Element root) {

            return RayExporter.Process(object, document, root);
         }

      };

      unitTestExporter.Process(exporter);
   }
}
