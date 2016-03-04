package test.exports;

import net.danielthompson.danray.cameras.CameraSettings;
import net.danielthompson.danray.cameras.apertures.Aperture;
import net.danielthompson.danray.cameras.apertures.CircleAperture;
import net.danielthompson.danray.cameras.apertures.SquareAperture;
import net.danielthompson.danray.exports.internal.*;
import net.danielthompson.danray.shading.*;
import net.danielthompson.danray.shading.bxdf.BRDF;
import net.danielthompson.danray.shading.bxdf.GaussianBRDF;
import net.danielthompson.danray.shading.bxdf.LambertianBRDF;
import net.danielthompson.danray.structures.*;
import net.danielthompson.danray.structures.Point;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.*;
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


   @Test
   public void testBoundingBoxExport() throws Exception {

      final BoundingBox object = new BoundingBox(new Point(-123, -456, -78.932), new Point(55.23, 200, 219.13857));

      File file = new File(_dir, "boundingbox.xml");

      final UnitTestExporter unitTestExporter = new UnitTestExporter(file);

      IExporter exporter = new IExporter() {
         @Override
         public Element Process(Document document, Element root) {

            return BoundingBoxExporter.Process(object, document, root);
         }

      };

      unitTestExporter.Process(exporter);
   }

   @Test
   public void testSpectrumExport() throws Exception {

      final Spectrum object = SpectralReflectanceCurveLibrary.Blue;

      File file = new File(_dir, "spectrum.xml");

      final UnitTestExporter unitTestExporter = new UnitTestExporter(file);

      IExporter exporter = new IExporter() {
         @Override
         public Element Process(Document document, Element root) {

            return SpectrumExporter.Process(object, document, root);
         }

      };

      unitTestExporter.Process(exporter);
   }

   @Test
   public void testSPDExport() throws Exception {

      final SpectralPowerDistribution object = RelativeSpectralPowerDistributionLibrary.D65.getSPD();

      File file = new File(_dir, "spd.xml");

      final UnitTestExporter unitTestExporter = new UnitTestExporter(file);

      IExporter exporter = new IExporter() {
         @Override
         public Element Process(Document document, Element root) {

            return SPDExporter.Process(object, document, root);
         }

      };

      unitTestExporter.Process(exporter);
   }


   @Test
       public void testRSPDExport() throws Exception {

      final RelativeSpectralPowerDistribution object = RelativeSpectralPowerDistributionLibrary.D65;

      File file = new File(_dir, "rspd.xml");

      final UnitTestExporter unitTestExporter = new UnitTestExporter(file);

      IExporter exporter = new IExporter() {
         @Override
         public Element Process(Document document, Element root) {

            return RSPDExporter.Process(object, document, root);
         }

      };

      unitTestExporter.Process(exporter);
   }

   @Test
    public void testSRCExport() throws Exception {

      final SpectralReflectanceCurve object = SpectralReflectanceCurveLibrary.Blue;

      File file = new File(_dir, "src.xml");

      final UnitTestExporter unitTestExporter = new UnitTestExporter(file);

      IExporter exporter = new IExporter() {
         @Override
         public Element Process(Document document, Element root) {

            return SRCExporter.Process(object, document, root);
         }

      };

      unitTestExporter.Process(exporter);
   }

   @Test
   public void testColorExport() throws Exception {

      final Color object = new Color(190, 21, 255);

      File file = new File(_dir, "color.xml");

      final UnitTestExporter unitTestExporter = new UnitTestExporter(file);

      IExporter exporter = new IExporter() {
         @Override
         public Element Process(Document document, Element root) {

            return ColorExporter.Process(object, document, root);
         }

      };

      unitTestExporter.Process(exporter);
   }

   @Test
   public void testBRDFExport1() throws Exception {

      final BRDF object = new GaussianBRDF(1);

      File file = new File(_dir, "brdf-gaussian.xml");

      final UnitTestExporter unitTestExporter = new UnitTestExporter(file);

      IExporter exporter = new IExporter() {
         @Override
         public Element Process(Document document, Element root) {

            return BRDFExporter.Process(object, document, root);
         }

      };

      unitTestExporter.Process(exporter);
   }

   @Test
   public void testBRDFExport2() throws Exception {

      final BRDF object = new LambertianBRDF();

      File file = new File(_dir, "brdf-lambertian.xml");

      final UnitTestExporter unitTestExporter = new UnitTestExporter(file);

      IExporter exporter = new IExporter() {
         @Override
         public Element Process(Document document, Element root) {

            return BRDFExporter.Process(object, document, root);
         }

      };

      unitTestExporter.Process(exporter);
   }

   @Test
   public void testMaterialExport1() throws Exception {

      final Material object = new Material();
      object.BRDF = new LambertianBRDF();
      object.Color = Color.green;
      object.SpectralReflectanceCurve = SpectralReflectanceCurveLibrary.LemonSkin;


      File file = new File(_dir, "material1.xml");

      final UnitTestExporter unitTestExporter = new UnitTestExporter(file);

      IExporter exporter = new IExporter() {
         @Override
         public Element Process(Document document, Element root) {

            return MaterialExporter.Process(object, document, root);
         }

      };

      unitTestExporter.Process(exporter);
   }

   @Test
   public void testApertureExport() throws Exception {

      final Aperture object = new CircleAperture(5);

      File file = new File(_dir, "aperture.xml");

      final UnitTestExporter unitTestExporter = new UnitTestExporter(file);

      IExporter exporter = new IExporter() {
         @Override
         public Element Process(Document document, Element root) {

            return ApertureExporter.Process(object, document, root);
         }

      };

      unitTestExporter.Process(exporter);
   }

   @Test
   public void testCameraSettingsExport() throws Exception {

      final CameraSettings object = new CameraSettings();
      object.X = 123;
      object.Y = 456;
      object.FocalLength = 1200;
      object.Rotation = 0;
      object.ZoomFactor = 1 / 1.5;
      object.FocusDistance = 250;
      object.Aperture = new SquareAperture(5);

      Point origin = new Point(100, 800, 1500);
      Vector direction = new Vector(0, -1, -1);

      object.Orientation = new Ray(origin, direction);

      File file = new File(_dir, "camerasettings.xml");

      final UnitTestExporter unitTestExporter = new UnitTestExporter(file);

      IExporter exporter = new IExporter() {
         @Override
         public Element Process(Document document, Element root) {

            return CameraSettingsExporter.Process(object, document, root);
         }

      };

      unitTestExporter.Process(exporter);
   }


}
