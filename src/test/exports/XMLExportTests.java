package test.exports;

import net.danielthompson.danray.SceneBuilder;
import net.danielthompson.danray.cameras.Camera;
import net.danielthompson.danray.cameras.CameraSettings;
import net.danielthompson.danray.cameras.SimplePointableCamera;
import net.danielthompson.danray.cameras.apertures.Aperture;
import net.danielthompson.danray.cameras.apertures.CircleAperture;
import net.danielthompson.danray.cameras.apertures.SquareAperture;
import net.danielthompson.danray.exports.internal.*;
import net.danielthompson.danray.shading.*;
import net.danielthompson.danray.shading.bxdf.BRDF;
import net.danielthompson.danray.shading.bxdf.GaussianBRDF;
import net.danielthompson.danray.shading.bxdf.LambertianBRDF;
import net.danielthompson.danray.shapes.Box;
import net.danielthompson.danray.shapes.Cylinder;
import net.danielthompson.danray.shapes.ImplicitPlane;
import net.danielthompson.danray.shapes.Sphere;
import net.danielthompson.danray.structures.*;
import net.danielthompson.danray.structures.Point;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.*;
import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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

   @Test
   public void testAbstractShapeExport() throws Exception {

      Transform t1 = Transform.Translate(new Vector(500, 200, 900));
      Transform t2 = Transform.Translate(new Vector(-500, -200, -900));

      final Material material = new Material();
      material.BRDF = new LambertianBRDF();
      material.Color = Color.green;
      material.SpectralReflectanceCurve = SpectralReflectanceCurveLibrary.LemonSkin;

      final Cylinder object = new Cylinder(5.0, 10.1, t1, t2, material);

      File file = new File(_dir, "abstractshape.xml");

      final UnitTestExporter unitTestExporter = new UnitTestExporter(file);

      IExporter exporter = new IExporter() {
         @Override
         public Element Process(Document document, Element root) {

            return AbstractShapeExporter.Process(object, document, root);
         }

      };

      unitTestExporter.Process(exporter);
   }

   @Test
   public void testCylinderExport() throws Exception {

      Transform t1 = Transform.Translate(new Vector(500, 200, 900));
      Transform t2 = Transform.Translate(new Vector(-500, -200, -900));

      final Material material = new Material();
      material.BRDF = new LambertianBRDF();
      material.Color = Color.green;
      material.SpectralReflectanceCurve = SpectralReflectanceCurveLibrary.LemonSkin;

      final Cylinder object = new Cylinder(5.0, 10.1, t1, t2, material);

      File file = new File(_dir, "cylinder.xml");

      final UnitTestExporter unitTestExporter = new UnitTestExporter(file);

      IExporter exporter = new IExporter() {
         @Override
         public Element Process(Document document, Element root) {

            return CylinderExporter.Process(object, document, root);
         }

      };

      unitTestExporter.Process(exporter);
   }

   @Test
   public void testBoxExport() throws Exception {

      Transform t1 = Transform.Translate(new Vector(500, 200, 900));
      Transform t2 = Transform.Translate(new Vector(-500, -200, -900));

      final Material material = new Material();
      material.BRDF = new LambertianBRDF();
      material.Color = Color.green;
      material.SpectralReflectanceCurve = SpectralReflectanceCurveLibrary.LemonSkin;

      final Box object = new Box(new Point(-1, -1.5, 0), new Point(5, 5.5, 10.4), material, t1, t2);

      File file = new File(_dir, "box.xml");

      final UnitTestExporter unitTestExporter = new UnitTestExporter(file);

      IExporter exporter = new IExporter() {
         @Override
         public Element Process(Document document, Element root) {

            return BoxExporter.Process(object, document, root);
         }

      };

      unitTestExporter.Process(exporter);
   }

   @Test
   public void testImplicitPlaneExport() throws Exception {

      final Material material = new Material();
      material.BRDF = new LambertianBRDF();
      material.Color = Color.green;
      material.SpectralReflectanceCurve = SpectralReflectanceCurveLibrary.LemonSkin;

      final ImplicitPlane object = new ImplicitPlane(new Point(-1, 2, -3.5), new Normal(5, .1, -7), material);

      File file = new File(_dir, "implicitplane.xml");

      final UnitTestExporter unitTestExporter = new UnitTestExporter(file);

      IExporter exporter = new IExporter() {
         @Override
         public Element Process(Document document, Element root) {

            return ImplicitPlaneExporter.Process(object, document, root);
         }

      };

      unitTestExporter.Process(exporter);
   }

   @Test
   public void testSphereExport() throws Exception {

      final Material material = new Material();
      material.BRDF = new LambertianBRDF();
      material.Color = Color.green;
      material.SpectralReflectanceCurve = SpectralReflectanceCurveLibrary.LemonSkin;

      final Sphere object = new Sphere(material);

      File file = new File(_dir, "sphere.xml");

      final UnitTestExporter unitTestExporter = new UnitTestExporter(file);

      IExporter exporter = new IExporter() {
         @Override
         public Element Process(Document document, Element root) {

            return SphereExporter.Process(object, document, root);
         }

      };

      unitTestExporter.Process(exporter);
   }


   @Test
   public void testAbstractCameraExport() throws Exception {

      CameraSettings settings = new CameraSettings();
      settings.X = 640;
      settings.Y = 480;
      settings.FocalLength = 1200;
      settings.Rotation = 0;
      settings.ZoomFactor = 1 / 1.5;
      settings.FocusDistance = 250;
      settings.Aperture = new SquareAperture(5);

      Point origin = new Point(100, 800, 1500);
      Vector direction = new Vector(0, -1, -1);

      settings.Orientation = new Ray(origin, direction);

      final Camera object = new SimplePointableCamera(settings);

      File file = new File(_dir, "abstractcamera.xml");

      final UnitTestExporter unitTestExporter = new UnitTestExporter(file);

      IExporter exporter = new IExporter() {
         @Override
         public Element Process(Document document, Element root) {

            return CameraExporter.Process(object, document, root);
         }

      };

      unitTestExporter.Process(exporter);
   }

   @Test
   public void testSceneExport() throws Exception {

      Class sceneBuilderClass = SceneBuilder.class;
      Class sceneClass = Scene.class;

      ArrayList<Method> methodList = new ArrayList<Method>();

      for (Method method : sceneBuilderClass.getMethods()) {
         if (Modifier.isStatic(method.getModifiers())) {
            Class returnType = method.getReturnType();
            if (sceneClass.isAssignableFrom(returnType)) {
               methodList.add(method);
            }
         }
      }

      Collections.sort(methodList, new Comparator<Method>() {
         @Override
         public int compare(Method o1, Method o2) {
            return (o1.getName().compareTo(o2.getName()));
         }
      });

      int i = 0;

      for (Method method : methodList) {
         System.err.println("Method: [" + method.getName() + "]");
         final Scene object = (Scene)method.invoke(null, 400, 300);
         object.Compile();
         File file = new File(_dir, "scene-" + i++ + "-" + method.getName() + ".xml");

         final UnitTestExporter unitTestExporter = new UnitTestExporter(file);

         IExporter exporter = new IExporter() {
            @Override
            public Element Process(Document document, Element root) {

               return SceneExporter.Process(object, document, root);
            }
         };

         unitTestExporter.Process(exporter);
      }


   }
}