package net.danielthompson.danray;

import net.danielthompson.danray.acceleration.KDScene;
import net.danielthompson.danray.cameras.Camera;
import net.danielthompson.danray.cameras.CameraSettings;
import net.danielthompson.danray.cameras.PerspectiveCamera;
import net.danielthompson.danray.cameras.apertures.CircleAperture;
import net.danielthompson.danray.cameras.apertures.SquareAperture;
import net.danielthompson.danray.lights.AbstractLight;
import net.danielthompson.danray.lights.PointLight;
import net.danielthompson.danray.lights.SphereLight;
import net.danielthompson.danray.scenes.AbstractScene;
import net.danielthompson.danray.scenes.NaiveScene;
import net.danielthompson.danray.scenes.skyboxes.ColorSkybox;
import net.danielthompson.danray.scenes.skyboxes.CubeMappedSkybox;
import net.danielthompson.danray.scenes.skyboxes.RGBSkybox;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shading.ReflectanceSpectrum;
import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.shading.bxdf.BRDF;
import net.danielthompson.danray.shading.bxdf.BTDF;
import net.danielthompson.danray.shading.bxdf.reflect.LambertianBRDF;
import net.danielthompson.danray.shading.bxdf.reflect.SpecularBRDF;
import net.danielthompson.danray.shading.bxdf.transmit.LambertianBTDF;
import net.danielthompson.danray.shading.bxdf.transmit.SpecularBTDF;
import net.danielthompson.danray.shapes.Box;
import net.danielthompson.danray.shapes.Cylinder;
import net.danielthompson.danray.shapes.ImplicitPlane;
import net.danielthompson.danray.shapes.Sphere;
import net.danielthompson.danray.shapes.csg.CSGOperation;
import net.danielthompson.danray.shapes.csg.CSGShape;
import net.danielthompson.danray.structures.Point3;
import net.danielthompson.danray.structures.*;
import net.danielthompson.danray.textures.CheckerboardTexture;
import net.danielthompson.danray.textures.ConstantTexture;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.awt.Color.red;

/**
 * User: daniel
 * Date: 6/30/13
 * Time: 9:53
 */
public class SceneBuilder {

   public static int nextID = 0;

   public static int getNextID() {
      return nextID++;
   }

   public static class Colors {
      public static class Firenze {
         public static Color Green = new Color(70, 137, 102);
         public static Color Beige = new Color(255, 240, 165);
         public static Color Yellow = new Color(255, 176, 59);
         public static Color Orange = new Color(182, 73, 38);
         public static Color Red = new Color(142, 40, 0);
      }

      public static class Solarized {
         public static Color Base03 = new Color(0, 43, 54);
         public static Color Base02 = new Color(7, 54, 66);
         public static Color Base01 = new Color(88, 110, 117);
         public static Color Base00 = new Color(101, 123, 131);
         public static Color Base0 = new Color(131, 148, 150);
         public static Color Base1 = new Color(147, 161, 161);
         public static Color Base2 = new Color(238, 232, 213);
         public static Color Base3 = new Color(253, 246, 227);
         public static Color yellow = new Color(181, 137, 0);
         public static Color orange = new Color(203, 75, 22);
         public static Color red = new Color(220, 50, 47);
         public static Color magenta = new Color(211, 54, 130);
         public static Color violet = new Color(108, 113, 196);
         public static Color blue = new Color(38, 139, 210);
         public static Color cyan = new Color(42, 161, 152);
         public static Color green = new Color(133, 153, 0);
      }

      public static class Chessboard {
         public static Color Dark = new Color(181, 136, 99);
         public static Color Light = new Color(240, 216, 181);
      }


      public static class YlGnBu {
         public static Color Color0 = new Color(255, 255, 217);
         public static Color Color1 = new Color(239, 249, 189);
         public static Color Color2 = new Color(213, 239, 179);
         public static Color Color3 = new Color(170, 222, 183);
         public static Color Color4 = new Color(117, 201, 189);
         public static Color Color5 = new Color(71, 181, 194);
         public static Color Color6 = new Color(41, 153, 191);
         public static Color Color7 = new Color(32, 117, 178);
         public static Color Color8 = new Color(35, 80, 161);
         public static Color Color9 = new Color(29, 51, 135);
      }

      public static class Spectral {
         public static Color Color0 = new Color(158, 1, 66);
         public static Color Color1 = new Color(209, 59, 75);
         public static Color Color2 = new Color(240, 111, 74);
         public static Color Color3 = new Color(252, 170, 98);
         public static Color Color4 = new Color(254, 220, 139);
         public static Color Color5 = new Color(251, 248, 176);
         public static Color Color6 = new Color(226, 243, 161);
         public static Color Color7 = new Color(173, 222, 162);
         public static Color Color8 = new Color(109, 191, 168);
         public static Color Color9 = new Color(67, 140, 180);
      }

      public static class Rainbow {
         public static Color Color0 = new Color(110, 64, 170);
         public static Color Color1 = new Color(190, 60, 175);
         public static Color Color2 = new Color(254, 75, 132);
         public static Color Color3 = new Color(255, 118, 72);
         public static Color Color4 = new Color(228, 181, 46);
         public static Color Color5 = new Color(177, 238, 87);
         public static Color Color6 = new Color(87, 246, 101);
         public static Color Color7 = new Color(31, 225, 159);
         public static Color Color8 = new Color(33, 175, 214);
         public static Color Color9 = new Color(73, 115, 221);
      }
   }



   public static class Sinebow {
      public static Color Color0 = new Color(255, 64, 64);
      public static Color Color1 = new Color(231, 141, 11);
      public static Color Color2 = new Color(167, 213, 3);
      public static Color Color3 = new Color(89, 252, 42);
      public static Color Color4 = new Color(25, 244, 114);
      public static Color Color5 = new Color(0, 192, 191);
      public static Color Color6 = new Color(24, 115, 244);
      public static Color Color7 = new Color(87, 43, 252);
      public static Color Color8 = new Color(166, 3, 214);
      public static Color Color9 = new Color(230, 10, 142);

      public static String Sinebow = "images/colors/sinebow.png";
      public static String YlGnBu = "images/colors/YlGnBu.png";
      public static String Rainbow = "images/colors/rainbow.png";
      public static String Spectral = "images/colors/Spectral.png";

      public static BufferedImage Load(String path) {
         try {
            ClassLoader loader = SceneBuilder.class.getClassLoader();
            URL url = loader.getResource(path);
            //         URL url = loader.getResource("images/cubemap/simple.png");
            return ImageIO.read(url);
         } catch (IOException e) {
            System.out.println("Couldn't load sinebow: " + e.getMessage());
            System.out.println("Working Directory = " +
                  System.getProperty("user.dir"));
            System.exit(-1);
         }

         return null;
      }
   }

   public static class Skyboxes {
      public static String Desert2 = "images/cubemap/desert 2.png";
      public static String Desert2Captions = "images/cubemap/desert 2 - captions.png";
      public static String Garden = "images/cubemap/garden.png";
      public static String Desert1 = "images/cubemap/desert1.jpg";

      public static BufferedImage Load(String path) {
         try {
            ClassLoader loader = SceneBuilder.class.getClassLoader();
            URL url = loader.getResource(path);
            //         URL url = loader.getResource("images/cubemap/simple.png");
            return ImageIO.read(url);
         } catch (IOException e) {
            System.out.println("Couldn't load skybox: " + e.getMessage());
            System.out.println("Working Directory = " +
                  System.getProperty("user.dir"));
            System.exit(-1);
         }

         return null;
      }
   }

   public static BRDF LambertianBRDF = new LambertianBRDF();
   public static BTDF LambertianBTDF = new LambertianBTDF();

   public static BRDF SpecularBRDF = new SpecularBRDF();
   public static BTDF SpecularBTDF = new SpecularBTDF();

   public static AbstractScene SpectralLemon(int x, int y) {

      CameraSettings settings = new CameraSettings();
      settings.x = x;
      settings.y = y;
      settings.focusDistance = 500;
      settings.aperture = new CircleAperture(20);

      Point3 origin = new Point3(0, 0, 4000);
      Vector3 direction = new Vector3(0, 0, -1);

      Camera camera = new PerspectiveCamera(settings, null);

      AbstractScene scene = new NaiveScene(camera);

      // right light

      SpectralPowerDistribution lightSPD = new SpectralPowerDistribution(10.0f, 10.0f, 10.0f);

      Material material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(Color.white);

      Sphere sphere = new Sphere();
      sphere.Origin = new Point3(2000, 15000, 15000);
      sphere.Radius = 1000;

      AbstractLight light = new SphereLight(sphere, lightSPD);

      //scene.Shapes.add(light);
      scene.addLight(light);

      // left light

      lightSPD = new SpectralPowerDistribution(1.0f, 0.8f, 0.8f);

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(red);

      sphere = new Sphere();
      sphere.Origin = new Point3(-800, 0, 500);
      sphere.Radius = 100;

      light = new SphereLight(sphere, lightSPD);

      //scene.Shapes.add(light);
      scene.addLight(light);


      // left wall

      Material boxMaterial = new Material();
      boxMaterial.ReflectanceSpectrum = new ReflectanceSpectrum(Color.yellow);

      ArrayList<Transform> list = new ArrayList<>();
      list.add(Transform.translate(new Vector3(-1500, 0, 0)));
      list.add(Transform.scale(1.0f, 1000.0f, 10000.0f));

      Transform[] transforms = Transform.composite(list);

      Transform objectToWorld = transforms[0];
      Transform worldToObject = transforms[1];

      Point3 p0 = new Point3(-1, -1, -1);
      Point3 p1 = new Point3(1, 1, 1);
      Box box = new Box(p0, p1, boxMaterial, objectToWorld, worldToObject);
      box.ID = getNextID();
      //scene.Drawables.add(box);

      // right wall

      boxMaterial = new Material();
      //boxMaterial.BRDF = new SpecularBRDF();
      boxMaterial.ReflectanceSpectrum = new ReflectanceSpectrum(Color.yellow);

      list = new ArrayList<>();
      list.add(Transform.translate(new Vector3(1500, 0, 0)));
      list.add(Transform.scale(1.0f, 1000.0f, 10000.0f));

      transforms = Transform.composite(list);

      objectToWorld = transforms[0];
      worldToObject = transforms[1];

      p0 = new Point3(-1, -1, -1);
      p1 = new Point3(1, 1, 1);
      box = new Box(p0, p1, boxMaterial, objectToWorld, worldToObject);
      box.ID = getNextID();
      scene.Shapes.add(box);

      // front wall

      boxMaterial = new Material();
      boxMaterial.ReflectanceSpectrum = new ReflectanceSpectrum(Color.green);

      list = new ArrayList<>();
      list.add(Transform.translate(new Vector3(0, 0, -1000)));
      list.add(Transform.scale(1500.0f, 1000.0f, 1));

      transforms = Transform.composite(list);

      objectToWorld = transforms[0];
      worldToObject = transforms[1];

      p0 = new Point3(-1, -1, -1);
      p1 = new Point3(1, 1, 1);

      box = new Box(p0, p1, boxMaterial, objectToWorld, worldToObject);
      box.ID = getNextID();
      //scene.Drawables.add(box);

      // back wall

      boxMaterial = new Material();
      //boxMaterial.BRDF = new SpecularBRDF();
      boxMaterial.ReflectanceSpectrum = new ReflectanceSpectrum(Color.green);

      list = new ArrayList<>();
      list.add(Transform.translate(new Vector3(0, 0, 10000)));
      list.add(Transform.scale(1500.0f, 1000.0f, 1));

      transforms = Transform.composite(list);

      objectToWorld = transforms[0];
      worldToObject = transforms[1];

      p0 = new Point3(-1, -1, -1);
      p1 = new Point3(1, 1, 1);

      box = new Box(p0, p1, boxMaterial, objectToWorld, worldToObject);
      box.ID = getNextID();
      //scene.Drawables.add(box);

      // floor

      boxMaterial = new Material();
      boxMaterial.ReflectanceSpectrum = new ReflectanceSpectrum(Color.green);

      list = new ArrayList<>();
      list.add(Transform.translate(new Vector3(0, -1000, 0)));
      list.add(Transform.scale(1500.0f, 1, 10000));

      transforms = Transform.composite(list);

      objectToWorld = transforms[0];
      worldToObject = transforms[1];

      p0 = new Point3(-1, -1, -1);
      p1 = new Point3(1, 1, 1);

      box = new Box(p0, p1, boxMaterial, objectToWorld, worldToObject);
      box.ID = getNextID();

      scene.Shapes.add(box);

      // ceiling

      boxMaterial = new Material();
      boxMaterial.ReflectanceSpectrum = new ReflectanceSpectrum(Color.green);

      list = new ArrayList<>();
      list.add(Transform.translate(new Vector3(0, 1000, 0)));
      list.add(Transform.scale(1500.0f, 1, 10000));

      transforms = Transform.composite(list);

      objectToWorld = transforms[0];
      worldToObject = transforms[1];

      p0 = new Point3(-1, -1, -1);
      p1 = new Point3(1, 1, 1);
      box = new Box(p0, p1, boxMaterial, objectToWorld, worldToObject);
      box.ID = getNextID();
      //scene.Drawables.add(box);

      // top left little box

      boxMaterial = new Material();
      boxMaterial.ReflectanceSpectrum = new ReflectanceSpectrum(Color.orange);

      for (int i = 0; i < 10; i++) {
         //for (int j = 0; j < 10; j++) {
         list = new ArrayList<>();
         list.add(Transform.translate(new Vector3(i * 120 - 800, i * 120 - 800, i * i * 20)));
         list.add(Transform.scale(50, 50, 50));
         transforms = Transform.composite(list);

         objectToWorld = transforms[0];
         worldToObject = transforms[1];

         p0 = new Point3(-1, -1, -1);
         p1 = new Point3(1, 1, 1);
         box = new Box(p0, p1, boxMaterial, objectToWorld, worldToObject);
         box.ID = getNextID();
         //scene.Drawables.add(box);

         //}
      }


      // bottom right little box

      boxMaterial = new Material();
      boxMaterial.ReflectanceSpectrum = new ReflectanceSpectrum(Color.cyan);

      p0 = new Point3(800, -1000, 0);
      p1 = new Point3(1300, -500, 500);
      box = new Box(p0, p1, boxMaterial);
      box.ID = getNextID();
      scene.Shapes.add(box);


      material = new Material();

      material.ReflectanceSpectrum = new ReflectanceSpectrum(Color.green);

      list = new ArrayList<>();
      list.add(Transform.translate(new Vector3(-400, 50, 1000)));
      list.add(Transform.rotateY(90));
      list.add(Transform.rotateZ(90));

      list.add(Transform.scale(50.0f, 1000.0f, 50.0f));

      transforms = Transform.composite(list);

      objectToWorld = transforms[0];
      worldToObject = transforms[1];

      Cylinder cylinder = new Cylinder(1, 1, worldToObject, objectToWorld, material);

      //scene.addShape(cylinder);

      return scene;
   }

   public static AbstractScene NumericalStabilityTest(int x, int y) {
      CameraSettings settings = new CameraSettings();
      settings.x = x;
      settings.y = y;
      settings.fov = 45f;

      long longBigNum = 1;
      //longBigNum <<= 29;
      float bigNum = longBigNum;

      Transform bigTranslate = Transform.translate(bigNum, bigNum, bigNum);

      Transform[] inputTransforms = new Transform[]{
            bigTranslate,
            Transform.translate(0, 50, 250),
            //Transform.rotateX(-45)
      };
      Transform[] compositeTransforms = Transform.composite(inputTransforms);
      Camera camera = new PerspectiveCamera(settings, compositeTransforms[0]);

      AbstractScene scene = new NaiveScene(camera);

      Material material1 = new Material();
      material1.BxDFs.add(LambertianBRDF);
      material1.Weights.add(1.0f);

      Material material2 = new Material();
      material2.BxDFs.add(SpecularBRDF);
      material2.Weights.add(1.0f);

      CheckerboardTexture texture1 = new CheckerboardTexture();
      texture1.UScale = 32;
      texture1.VScale = 32;
      texture1.Odd = new ReflectanceSpectrum(new Color(0.8f, 0.8f, 0.75f));
      texture1.Even = new ReflectanceSpectrum(new Color(0.9f, 0.9f, 0.85f));
      material1.Texture = texture1;

      CheckerboardTexture texture2 = new CheckerboardTexture();
      texture2.UScale = 32;
      texture2.VScale = 32;
      texture2.Odd = new ReflectanceSpectrum(new Color(0.8f, 0.8f, 0.75f));
      texture2.Even = new ReflectanceSpectrum(new Color(0.9f, 0.9f, 0.85f));
      material2.Texture = texture2;

      inputTransforms = new Transform[]{
            bigTranslate,
            Transform.rotateY(45f),
            Transform.scale(1000f, 1f, 1000f),
            Transform.translate(new Vector3(-0.5f, -0.5f, -0.5f))
      };
      compositeTransforms = Transform.composite(inputTransforms);

      Box box = new Box(compositeTransforms, material1);

      inputTransforms = new Transform[]{
            bigTranslate,
            Transform.translate(0, 101, -100),
            //Transform.rotateZ(10f),
            Transform.scale(100f)
      };
      compositeTransforms = Transform.composite(inputTransforms);

      Sphere sphere = new Sphere(compositeTransforms, material2);

      scene.addShape(sphere);
      scene.addShape(box);

      scene.Skybox = new ColorSkybox(Color.WHITE);

      return scene;
   }

   public static AbstractScene NumericalStabilityTest2(int x, int y) {
      CameraSettings settings = new CameraSettings();
      settings.x = x;
      settings.y = y;
      settings.fov = 45f;

      Transform[] inputTransforms = new Transform[]{
            Transform.translate(0, 0, 50),
      };
      Transform[] compositeTransforms = Transform.composite(inputTransforms);
      Camera camera = new PerspectiveCamera(settings, compositeTransforms[0]);

      AbstractScene scene = new NaiveScene(camera);

      Material material1 = new Material();
      material1.BxDFs.add(SpecularBTDF);
      material1.IndexOfRefraction = 1.0f;
      material1.Weights.add(1.0f);

      Material material2 = new Material();
      material2.BxDFs.add(SpecularBTDF);
      material2.IndexOfRefraction = 1.5f;
      material2.Weights.add(1.0f);

      CheckerboardTexture texture1 = new CheckerboardTexture();
      texture1.UScale = 32;
      texture1.VScale = 32;
      texture1.Odd = new ReflectanceSpectrum(new Color(0.8f, 0.8f, 0.75f));
      texture1.Even = new ReflectanceSpectrum(new Color(0.9f, 0.9f, 0.85f));
      material1.Texture = texture1;

      CheckerboardTexture texture2 = new CheckerboardTexture();
      texture2.UScale = 32;
      texture2.VScale = 32;
      texture2.Odd = new ReflectanceSpectrum(new Color(0.8f, 0.8f, 0.75f));
      texture2.Even = new ReflectanceSpectrum(new Color(0.9f, 0.9f, 0.85f));
      material2.Texture = texture2;

      {
         inputTransforms = new Transform[]{
               Transform.translate(-0.5f, 0, 0)
         };
         compositeTransforms = Transform.composite(inputTransforms);

         Sphere leftShape = new Sphere(compositeTransforms, material1);

         inputTransforms = new Transform[]{
               Transform.translate(0.5f, 0, 0)
         };
         compositeTransforms = Transform.composite(inputTransforms);

         Sphere rightShape = new Sphere(compositeTransforms, material1);

         inputTransforms = new Transform[]{
               Transform.translate(0, 0, 0),
               Transform.scale(20f)
         };
         compositeTransforms = Transform.composite(inputTransforms);

         CSGShape csgShape = new CSGShape(compositeTransforms);
         csgShape.Operation = CSGOperation.Union;
         csgShape.LeftShape = leftShape;
         csgShape.RightShape = rightShape;
         //scene.addShape(csgShape);
      }

      {
         inputTransforms = new Transform[]{
               Transform.scale(20)
         };
         compositeTransforms = Transform.composite(inputTransforms);

         //material1.Texture = new ConstantTexture(Color.white);
         material1.IndexOfRefraction = 1.5f;

         Sphere sphere = new Sphere(compositeTransforms, material1);
         scene.addShape(sphere);
      }

      scene.Skybox = new RGBSkybox();

      return scene;
   }

   public static AbstractScene Default(int x, int y) {

      CameraSettings settings = new CameraSettings();
      settings.x = x;
      settings.y = y;
      settings.fov = 25f;

      float bigNum = 1f;

      Transform bigTranslate = Transform.translate(bigNum, bigNum, bigNum);

      Transform[] inputTransforms = new Transform[]{
            bigTranslate,
            Transform.translate(0, 180, 250),
            Transform.rotateY(0),
            Transform.rotateX(-45)
      };

      Transform[] compositeTransforms = Transform.composite(inputTransforms);
      Camera camera = new PerspectiveCamera(settings, compositeTransforms[0]);

      AbstractScene scene = new NaiveScene(camera);
      Material material;

      CheckerboardTexture texture = new CheckerboardTexture();
      texture.UScale = 4;
      texture.VScale = 4;
      texture.Even = new ReflectanceSpectrum(Colors.Firenze.Beige);
      texture.Odd = new ReflectanceSpectrum(Colors.Firenze.Red);

      // front CSG object

      material = new Material();
      material.BxDFs.add(LambertianBRDF);
      material.Weights.add(1.0f);
      material.Texture = texture;

      inputTransforms = new Transform[]{
            Transform.scale(2),
            Transform.translate(new Vector3(-0.5f, -0.5f, -0.5f))
      };
      compositeTransforms = Transform.composite(inputTransforms);
      Box leftShape = new Box(compositeTransforms, material);

//      material = new Material();
//      material.BxDFs.add(brdf);
//      material.Weights.add(1.0f);
//      material.IndexOfRefraction = 1.25f;
////      material.Texture = new ConstantTexture(new ReflectanceSpectrum(Colors.Rainbow.Color2));
//      material.Texture = new ConstantTexture(new ReflectanceSpectrum(Colors.Rainbow.Color8));

      inputTransforms = new Transform[]{
            Transform.scale(1.35f), // TODO wtf?
      };
      compositeTransforms = Transform.composite(inputTransforms);
      Sphere rightShape = new Sphere(compositeTransforms, material);

      inputTransforms = new Transform[]{
//            Transform.translate(new Vector(0, -31.99f, 50f)),
//            Transform.rotateY(-75f),
//            Transform.scale(20f),
            Transform.identity
      };
      compositeTransforms = Transform.composite(inputTransforms);

      CSGShape csgshape = new CSGShape(compositeTransforms);
      csgshape.LeftShape = leftShape;
      csgshape.RightShape = rightShape;
      csgshape.Operation = CSGOperation.Intersection;
      //scene.addShape(csgshape);

      {
//         material = new Material();
//         material.BxDFs.add(brdf);
//         material.Weights.add(1.0f);
//         material.IndexOfRefraction = 1.25f;
////         material.Texture = new ConstantTexture(new ReflectanceSpectrum(Colors.Rainbow.Color4));
//         material.Texture = new ConstantTexture(new ReflectanceSpectrum(Colors.Rainbow.Color8));

         inputTransforms = new Transform[]{
               Transform.scale(6f, 0.75f, 0.75f),
         };
         compositeTransforms = Transform.composite(inputTransforms);
         Sphere rightShape3 = new Sphere(compositeTransforms, material);

//         material = new Material();
//         material.BxDFs.add(brdf);
//         material.Weights.add(1.0f);
//         material.IndexOfRefraction = 1.25f;
//         material.Texture = new ConstantTexture(new ReflectanceSpectrum(Colors.Rainbow.Color8));

         inputTransforms = new Transform[]{
               Transform.scale(0.75f, 6f, 0.75f),
         };
         compositeTransforms = Transform.composite(inputTransforms);
         Sphere rightShape4 = new Sphere(compositeTransforms, material);

//         material = new Material();
//         material.BxDFs.add(brdf);
//         material.Weights.add(1.0f);
//         material.IndexOfRefraction = 1.25f;
////         material.Texture = new ConstantTexture(new ReflectanceSpectrum(Colors.Rainbow.Color7));
//         material.Texture = new ConstantTexture(new ReflectanceSpectrum(Colors.Rainbow.Color8));

         inputTransforms = new Transform[]{
               Transform.scale(0.75f, 0.75f, 6f),
         };
         compositeTransforms = Transform.composite(inputTransforms);
         Sphere rightShape5 = new Sphere(compositeTransforms, material);

         inputTransforms = new Transform[]{
               Transform.identity
         };
         compositeTransforms = Transform.composite(inputTransforms);

         CSGShape csgshape3 = new CSGShape(compositeTransforms);
         csgshape3.LeftShape = rightShape3;
         csgshape3.RightShape = rightShape4;
         csgshape3.Operation = CSGOperation.Union;

         CSGShape csgShape4 = new CSGShape(compositeTransforms);
         csgShape4.LeftShape = csgshape3;
         csgShape4.RightShape = rightShape5;
         csgShape4.Operation = CSGOperation.Union;

         inputTransforms = new Transform[]{
               bigTranslate,
               Transform.translate(new Vector3(0, -16.99f, 50f)),
               Transform.rotateY(-60f),
               Transform.scale(35f),
         };
         compositeTransforms = Transform.composite(inputTransforms);

         CSGShape csgshape2 = new CSGShape(compositeTransforms);
         csgshape2.LeftShape = csgshape;
         csgshape2.RightShape = csgShape4;
         csgshape2.Operation = CSGOperation.Difference;

         scene.addShape(csgshape2);

         inputTransforms = new Transform[]{
               bigTranslate,
               Transform.translate(new Vector3(-50, -31.99f, 50f)),
               Transform.rotateY(-30f),

               Transform.scale(20f),
         };
         compositeTransforms = Transform.composite(inputTransforms);

         CSGShape csgshape5 = new CSGShape(compositeTransforms);
         csgshape5.LeftShape = csgshape;
         csgshape5.RightShape = csgShape4;
         csgshape5.Operation = CSGOperation.Difference;

         //scene.addShape(csgshape5);
      }
      // left CSG object

      material = new Material();
      material.BxDFs.add(LambertianBRDF);
      material.Weights.add(1.0f);
      material.Texture = new ConstantTexture(new ReflectanceSpectrum(Colors.Rainbow.Color7));

      inputTransforms = new Transform[]{
            Transform.scale(2),
            Transform.translate(new Vector3(-0.5f, -0.5f, -0.5f)),
//            Transform.translate(-50.0f, -32.0f, -25f),
//            Transform.rotateY(-45f),
//            Transform.scale(20f),

      };
      compositeTransforms = Transform.composite(inputTransforms);
      leftShape = new Box(compositeTransforms, material);

      //scene.addShape(leftShape);

      material = new Material();
      material.BxDFs.add(LambertianBRDF);
      material.Weights.add(1.0f);
      material.Texture = new ConstantTexture(new ReflectanceSpectrum(Colors.Rainbow.Color4));

      inputTransforms = new Transform[]{
            //Transform.translate(1, 1, 1),
            Transform.scale(1.45f), // TODO wtf?

      };
      compositeTransforms = Transform.composite(inputTransforms);
      rightShape = new Sphere(compositeTransforms, material);

      inputTransforms = new Transform[]{
            Transform.translate(-50.0f, -32.0f, -25f),
            Transform.rotateY(-45f),
            Transform.scale(20f),

      };
      compositeTransforms = Transform.composite(inputTransforms);

      csgshape = new CSGShape(compositeTransforms);
      csgshape.LeftShape = leftShape;
      csgshape.RightShape = rightShape;
      csgshape.Operation = CSGOperation.Difference;
      //scene.addShape(csgshape);

      material = new Material();
      material.BxDFs.add(LambertianBRDF);
      //material.IndexOfRefraction = 1.5f;
      material.Weights.add(1.0f);
      material.Texture = new ConstantTexture(new ReflectanceSpectrum(Colors.Rainbow.Color5));

      inputTransforms = new Transform[]{
            Transform.translate(0.0f, -32.0f, 25f),
            Transform.rotateY(-45f),
            Transform.scale(20f),
      };
      compositeTransforms = Transform.composite(inputTransforms);
      Sphere sphere3 = new Sphere(compositeTransforms, material);
      //scene.addShape(sphere3);

      // right CSG object

      material = new Material();
      material.BxDFs.add(LambertianBRDF);
      material.Weights.add(1.0f);
      material.Texture = new ConstantTexture(new ReflectanceSpectrum(Colors.Rainbow.Color5));

      inputTransforms = new Transform[]{
            //Transform.scale(2),
            //Transform.translate(new Vector(-0.5f, -0.5f, -0.5f))
      };
      compositeTransforms = Transform.composite(inputTransforms);
      Sphere leftShape2 = new Sphere(compositeTransforms, material);

      material = new Material();
      material.BxDFs.add(LambertianBRDF);
      material.Weights.add(1.0f);
      material.Texture = new ConstantTexture(new ReflectanceSpectrum(Colors.Rainbow.Color6));

      inputTransforms = new Transform[]{
            //Transform.translate(0, 1, 1),
            //Transform.scale(2f), // TODO wtf?

      };
      compositeTransforms = Transform.composite(inputTransforms);
      Box rightShape2 = new Box(compositeTransforms, material);

      inputTransforms = new Transform[]{

            Transform.translate(50.0f, -32.0f, -25f),
            Transform.rotateY(-45f),
            Transform.scale(20f),
      };
      compositeTransforms = Transform.composite(inputTransforms);

      csgshape = new CSGShape(compositeTransforms);
      csgshape.LeftShape = leftShape2;
      csgshape.RightShape = rightShape2;
      csgshape.Operation = CSGOperation.Difference;
      //scene.addShape(csgshape);

      // bottom box

      material = new Material();
//      material.reflect = new GlossyBRDF(0.85f);

      material.BxDFs.add(LambertianBRDF);
      material.Weights.add(1.0f);

      texture = new CheckerboardTexture();
      texture.UScale = 32;
      texture.VScale = 32;
      texture.Odd = new ReflectanceSpectrum(new Color(0.8f, 0.8f, 0.75f));
      texture.Even = new ReflectanceSpectrum(new Color(0.9f, 0.9f, 0.85f));
      material.Texture = texture;

      inputTransforms = new Transform[]{
            bigTranslate,
            Transform.translate(new Vector3(0, -52f, 0f)),
            Transform.rotateY(45f),
            Transform.scale(1000f, 1f, 1000f),
            Transform.translate(new Vector3(-0.5f, -0.5f, -0.5f))
      };
      compositeTransforms = Transform.composite(inputTransforms);
      scene.addShape(new Box(compositeTransforms, material));
      // skybox

      //scene.Skybox = new RGBSkybox();

      //scene.Skybox = new HalfGradientSkybox(Solarized.Base02, Color.WHITE);
      List<Color> colors = new ArrayList<>();

//      colors.add(Colors.Firenze.Beige);
      colors.add(new Color(190, 211, 226));
      colors.add(new Color(140, 200, 255));
      //colors.add(new Color(17, 49, 110));

      List<Float> locations = new ArrayList<>();
      locations.add(.5f);
      locations.add(.57f);
      //locations.add(.65f);

      scene.Skybox = new ColorSkybox(Color.WHITE);
//      scene.Skybox = new SteppedGradientSkybox(colors, locations);
//      scene.SkyBoxImage = Skyboxes.Load(Skyboxes.Desert1);

      return scene;
   }

   public static AbstractScene GlossyStrips(int x, int y) {

      CameraSettings settings = new CameraSettings();
      settings.x = x;
      settings.y = y;
      settings.fov = 20.114292f;

      Transform[] inputTransforms = new Transform[2];
      inputTransforms[0] = Transform.translate(new Vector3(28.2792f, 3.5f, 0));
      inputTransforms[1] = Transform.rotateY(90);

      Transform[] compositeTransforms = Transform.composite(inputTransforms);

      Camera camera = new PerspectiveCamera(settings, compositeTransforms[0]);

      AbstractScene scene = new NaiveScene(camera);

      Material material;
      Box box;

      // base

      material = new Material();
      //material.BRDF = LambertianBRDF;
      material.ReflectanceSpectrum = new ReflectanceSpectrum(Color.white);

      inputTransforms = new Transform[5];
      inputTransforms[0] = Transform.translate(new Vector3(0f, 0f, 0));
      inputTransforms[1] = Transform.rotateZ(-10f);
      inputTransforms[2] = Transform.scale(24f, 0.1f, 16f);
//      inputTransforms[3] = Transform.scale(2);
      inputTransforms[3] = Transform.identity;
      inputTransforms[4] = Transform.translate(new Vector3(-.5f, -.5f, -.5f));
//      inputTransforms[4] = Transform.identity;

      compositeTransforms = Transform.composite(inputTransforms);

      box = new Box(compositeTransforms, material);
      scene.Shapes.add(box);

      // strip 1

      material = new Material();
      //material.BRDF = new GlossyBRDF(1.f);
      material.ReflectanceSpectrum = new ReflectanceSpectrum(Color.gray);

      inputTransforms = new Transform[5];
      inputTransforms[0] = Transform.translate(new Vector3(.264069f, 4.09801f, 0));
      inputTransforms[1] = Transform.rotateZ(-39.8801f);
//      inputTransforms[1] = Transform.rotateZ(-65f);
      inputTransforms[2] = Transform.scale(2.1f, 0.3f, 8f);
//      inputTransforms[3] = Transform.scale(2);
      inputTransforms[3] = Transform.identity;
      inputTransforms[4] = Transform.translate(new Vector3(-.5f, -.5f, -.5f));
//      inputTransforms[4] = Transform.identity;

      compositeTransforms = Transform.composite(inputTransforms);

      box = new Box(compositeTransforms, material);
      scene.Shapes.add(box);

      // strip 2

      material = new Material();
      //material.BRDF = new GlossyBRDF(.99f);
      material.ReflectanceSpectrum = new ReflectanceSpectrum(Color.gray);

      inputTransforms = new Transform[5];
      inputTransforms[0] = Transform.translate(new Vector3(3.06163f, 2.71702f, 0));
      inputTransforms[1] = Transform.rotateZ(-24f);
      inputTransforms[2] = Transform.scale(2.1f, 0.3f, 8f);
//      inputTransforms[3] = Transform.scale(2);
      inputTransforms[3] = Transform.identity;
      inputTransforms[4] = Transform.translate(new Vector3(-.5f, -.5f, -.5f));
//      inputTransforms[4] = Transform.identity;

      compositeTransforms = Transform.composite(inputTransforms);

      box = new Box(compositeTransforms, material);
      scene.Shapes.add(box);

      // strip 3

      material = new Material();
      //material.BRDF = new GlossyBRDF(.98f);
      material.ReflectanceSpectrum = new ReflectanceSpectrum(Color.gray);

      inputTransforms = new Transform[5];
      inputTransforms[0] = Transform.translate(new Vector3(7.09981f, 1.81891f, 0f));
      inputTransforms[1] = Transform.rotateZ(-14f);
      inputTransforms[2] = Transform.scale(2.1f, 0.3f, 8f);
//      inputTransforms[3] = Transform.scale(2);
      inputTransforms[3] = Transform.identity;
      inputTransforms[4] = Transform.translate(new Vector3(-.5f, -.5f, -.5f));
//      inputTransforms[4] = Transform.identity;

      compositeTransforms = Transform.composite(inputTransforms);

      box = new Box(compositeTransforms, material);
      scene.Shapes.add(box);

      // strip 4

      material = new Material();
      //material.BRDF = new GlossyBRDF(.97f);
//      material.reflect = new SpecularBRDF();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(Color.gray);

      inputTransforms = new Transform[5];
      inputTransforms[0] = Transform.translate(new Vector3(10.6769f, 1.23376f, 0f));
//      inputTransforms[1] = Transform.rotateZ(-50);
      inputTransforms[1] = Transform.rotateZ(-9.25f);
//      inputTransforms[1] = Transform.identity;
      inputTransforms[2] = Transform.scale(2.1f, 0.3f, 8f);
//      inputTransforms[3] = Transform.scale(2);
      inputTransforms[3] = Transform.identity;
      inputTransforms[4] = Transform.translate(new Vector3(-.5f, -.5f, -.5f));
//      inputTransforms[4] = Transform.identity;

      compositeTransforms = Transform.composite(inputTransforms);

      box = new Box(compositeTransforms, material);
      scene.Shapes.add(box);

      // light 1

      SpectralPowerDistribution lightSPD = new SpectralPowerDistribution(Color.white, 10f);

      inputTransforms = new Transform[2];
      inputTransforms[0] = Transform.translate(new Vector3(0, 6.5f, 2.7f));
      inputTransforms[1] = Transform.scale(0.05f);

      compositeTransforms = Transform.composite(inputTransforms);

      Sphere sphere = new Sphere(compositeTransforms, null);

      AbstractLight light = new SphereLight(sphere, lightSPD);

      scene.Shapes.add(light);
      scene.Lights.add(light);

      // light 2

      lightSPD = new SpectralPowerDistribution(Color.white, 10f);

      inputTransforms = new Transform[2];
      inputTransforms[0] = Transform.translate(new Vector3(0, 6.5f, 0));
      inputTransforms[1] = Transform.scale(0.5f);

      compositeTransforms = Transform.composite(inputTransforms);

      sphere = new Sphere(compositeTransforms, null);

      light = new SphereLight(sphere, lightSPD);

      scene.Shapes.add(light);
      scene.Lights.add(light);

      // light 3

      lightSPD = new SpectralPowerDistribution(Color.white, 10f);

      inputTransforms = new Transform[1];
      inputTransforms[0] = Transform.translate(new Vector3(0, 6.5f, -2.8f));

      compositeTransforms = Transform.composite(inputTransforms);

      sphere = new Sphere(compositeTransforms, null);

      light = new SphereLight(sphere, lightSPD);

      scene.Shapes.add(light);
      scene.Lights.add(light);

      // red light

//      lightSPD = new SpectralPowerDistribution(Firenze.Orange, 100000.0f);
//
//      inputTransforms = new Transform[2];
//      inputTransforms[0] = Transform.translate(new Vector(300, -3300, -1500));
//      inputTransforms[1] = Transform.scale(100f, 100f, 100f);
//
//      compositeTransforms = Transform.composite(inputTransforms);
//
//      sphere = new Sphere(compositeTransforms, null);
//
//      light = new SphereLight(sphere, lightSPD);

      //scene.Shapes.add(light);
      //scene.Lights.add(light);

      // point light

//      lightSPD = new SpectralPowerDistribution(Color.white, 100000.0f);
//
//      inputTransforms = new Transform[2];
//      inputTransforms[0] = Transform.translate(new Vector(300, -3300, -1500));
//      inputTransforms[1] = Transform.scale(100f, 100f, 100f);
//
//      compositeTransforms = Transform.composite(inputTransforms);
//
//      sphere = new Sphere(compositeTransforms, null);
//
//      PointLight pointLight = new PointLight(lightSPD, new Point(300, 3300, -1500));

      //scene.Shapes.add(light);
      //scene.Lights.add(pointLight);

      // skybox

      scene.Skybox = new CubeMappedSkybox(Skyboxes.Load(Skyboxes.Desert2));

      return scene;
   }

   public static AbstractScene DepthOfFieldTest(int x, int y) {

      CameraSettings settings = new CameraSettings();
      settings.x = x;
      settings.y = y;
      settings.focusDistance = 500;
      settings.aperture = new CircleAperture(20);

      Point3 origin = new Point3(100, 75, 700);
      Vector3 direction = new Vector3(0, -.35f, -1);

      Camera camera = new PerspectiveCamera(settings, null);

      AbstractScene scene = new NaiveScene(camera);

      // floor orange plane
      Point3 planeOrigin = new Point3(0, -250, 0);
      Normal planeNormal = new Normal(0, 1, 0);

      Material material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 240, 185));

      ImplicitPlane plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addShape(plane);

      // spheres

      for (int i = 0; i < 9; i++) {
         material = new Material();
         material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(0, 131, 255));

         Sphere sphere = new Sphere(material);
         int coord = -850 + (175 * i);
         sphere.Origin = new Point3(coord, 0, coord);
         sphere.Radius = 100;
         scene.addShape(sphere);

         //scene.addLight(new PointLight(new Point(-450 + (300 * i), 2000, -450 + (300 * i)), 25));
      }

      SpectralPowerDistribution spd = new SpectralPowerDistribution(1.0f, 1.0f, 1.0f);

      scene.addLight(new PointLight(spd, new Point3(-650, 600, 925)));

      for (int i = 0; i < 1; i++) {
         scene.addLight(new PointLight(spd, new Point3(0, 3000, -175 + (i * 300))));
      }
      return scene;
   }

   public static AbstractScene ManyRegularSpheres(int x, int y) {
      CameraSettings settings = new CameraSettings();
      settings.x = x;
      settings.y = y;
      settings.fov = 20;

      Transform[] inputTransforms = new Transform[1];
      inputTransforms[0] = Transform.translate(new Vector3(0, 0, 1200));
      Transform[] compositeTransforms = Transform.composite(inputTransforms);

//      Vector direction = new Vector(0, 1, -1);
//      settings.Orientation = new Ray(origin, direction);

      Camera camera = new PerspectiveCamera(settings, compositeTransforms[0]);

//      AbstractScene scene = new NaiveScene(camera);
      AbstractScene scene = new KDScene(camera);
//      AbstractScene scene = new KDScene(camera);
      scene.numFrames = 1;

      // white vertical z plane
      BRDF brdf = new SpecularBRDF();

      Material material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(240, 120, 120));
      //material.BRDF = brdf;

      float frontZ = -150;

      inputTransforms = new Transform[3];
      inputTransforms[0] = Transform.translate(new Vector3(0, 0, frontZ));
      inputTransforms[1] = Transform.scale(600, 600, 1f);
      inputTransforms[2] = Transform.translate(new Vector3(-0.5f, -0.5f, -0.5f));

      //inputTransforms[1] = Transform.translate(new Vector(-200f, -300f, -150));

      compositeTransforms = Transform.composite(inputTransforms);

      Box box = new Box(compositeTransforms, material);
      //scene.addShape(box);

      int maxShapes = 400;

      int total = 400;

      int sphereXInterval = 20;
      int maxSmallSpheresX = total / sphereXInterval;

      int sphereYInterval = 20;
      int maxSmallSpheresY = total / sphereYInterval;

      float radius = sphereXInterval / 3;

      int[] yOffset = new int[maxSmallSpheresX * maxSmallSpheresY];
      for (int i = 0; i < yOffset.length; i++) {
         yOffset[i] = 0;
      }

      int num = 0;

      for (int i = 0; i < maxSmallSpheresX; i++) {
         for (int j = 0; j < maxSmallSpheresY; j++) {

            if (i == 0 || i == 1) {
               if (j == 0 || j == 1 || j == 2) {
                  continue;
               }
            }

            /*
            int red = (i % 3) * 64 + 48;
            int green = ((i + j) % 4) * 48 + 48;
            int blue = (j % 3) * 64 + 48;
            */
            Color color = new Color(150, 210, 255);

            material = new Material();
            material.ReflectanceSpectrum = new ReflectanceSpectrum(color);
            //material.BRDF = SpecularBRDF;

            float originX = sphereXInterval * i + (j % 5) * 3;
            float originY = sphereYInterval * j + (yOffset[i * maxSmallSpheresY + j]);
            float originZ = frontZ + radius;

            inputTransforms = new Transform[2];
            inputTransforms[0] = Transform.translate(new Vector3(originX - 150, originY - 150, originZ));
            inputTransforms[1] = Transform.scale(radius);

            compositeTransforms = Transform.composite(inputTransforms);

            Sphere sphere = new Sphere(compositeTransforms, material);

            if (num++ < maxShapes)
               scene.addShape(sphere);
         }
      }

      scene.Skybox = new CubeMappedSkybox(Skyboxes.Load(Skyboxes.Desert2));


      return scene;
   }

   public static AbstractScene CornellBox(int x, int y) {
      CameraSettings settings = new CameraSettings();
      settings.x = x;
      settings.y = y;
      settings.focusDistance = 75;
      settings.aperture = new CircleAperture(10);

      Point3 origin = new Point3(0, 0, 250);
      Vector3 direction = new Vector3(0, 0, -1);

      Camera camera = new PerspectiveCamera(settings, null);

      AbstractScene scene = new NaiveScene(camera);
//      AbstractScene scene = new KDCompactScene(camera);
//      AbstractScene scene = new KDScene(camera);
      scene.numFrames = 1;

      BRDF brdf = new LambertianBRDF();

      Material material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(120, 240, 240));

      //material.BRDF = brdf;

      Point3 p0 = new Point3(-1000, -1000, -1000);
      Point3 p1 = new Point3(1000, 1000, 1000);

      Transform[] inputTransforms = new Transform[2];
      inputTransforms[0] = Transform.translate(new Vector3(-0.5f, -0.5f, -0.5f));
      inputTransforms[1] = Transform.scale(2000f);

      Transform[] compositeTransforms = Transform.composite(inputTransforms);

      Box box = new Box(compositeTransforms[0], compositeTransforms[1], material);
      scene.addShape(box);

      // right light

      SpectralPowerDistribution lightSPD = new SpectralPowerDistribution(2000.0f, 2000.0f, 2000.0f);

      Sphere sphere = new Sphere();
      sphere.Origin = new Point3(0, 250, -250);
      sphere.Radius = 50;


      AbstractLight light = new SphereLight(sphere, lightSPD);

      scene.Shapes.add(light);
      scene.addLight(light);

      return scene;
   }

   public static AbstractScene ManyRandomSpheres(int x, int y) {
      CameraSettings settings = new CameraSettings();
      settings.x = x;
      settings.y = y;
      settings.fov = 50f;

      settings.aperture = new SquareAperture(2);

      Transform[] inputTransforms = new Transform[1];
      inputTransforms[0] = Transform.translate(new Vector3(300, 300, 1000));

      Transform[] compositeTransforms = Transform.composite(inputTransforms);

      Camera camera = new PerspectiveCamera(settings, compositeTransforms[0]);

      AbstractScene scene = new NaiveScene(camera);

      scene.Skybox = new CubeMappedSkybox(Skyboxes.Load(Skyboxes.Desert2));
      scene.numFrames = 1;

      // white vertical z plane

//      Point planeOrigin = new Point(0, 0, 0);
//      normal planeNormal = new normal(0, 0, 1);
      BRDF brdf = new LambertianBRDF();
      Material material = new Material();
//      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 240, 185));
//      material._specular = 1 - .75;
//      material._reflectivity = .25;
//      material.reflect = brdf;
//
//      ImplicitPlane plane = new ImplicitPlane(planeOrigin, planeNormal, material);
//      scene.addShape(plane);

      for (int i = 0; i < 40; i++) {

         float originX = (float) (Math.random() * 600);
         float originY = (float) (Math.random() * 600);
         float originZ = (float) (Math.random() * 10 + 20);

         material = new Material();


         material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(0, 131, 255));
         //material.BRDF = brdf;
         //material.setIndexOfRefraction(1.1);

         Sphere sphere = new Sphere(material);
         sphere.Origin = new Point3(originX, originY, originZ);
         sphere.Radius = (float) (Math.random() * 50);

         scene.addShape(sphere);
      }

      int maxSmallSpheresX = 2;
      int sphereXInterval = 40;

      int maxSmallSpheresY = 2;
      int sphereYInterval = 40;

      int maxSmallSpheresZ = 2;
      int sphereZInterval = 40;

      for (int i = 0; i < maxSmallSpheresX; i++) {
         for (int j = 0; j < maxSmallSpheresY; j++) {
            //for (int k = 0; k < maxSmallSpheresZ; k++) {

            int constant = 128;

            int red = (int) (((float) i / (float) maxSmallSpheresX) * (255 - constant) + constant);
            int green = (int) (((float) j / (float) maxSmallSpheresY) * (255 - constant) + constant);
            int blue = (int) (Math.random() * (255 - constant) + constant);

            Color color = new Color(red, green, blue);
               /*
               Color color = null;
               float chance = Math.random();

               material = new Material();

               if (chance < .2) {
                  color = new Color(0, 131, 255);
               }
               else if (chance < .4) {
                  //color = new Color(182, 73, 38);
                  color = new Color(255, 176, 59);
               }

               else if (chance < .6) {
                  color = new Color(70, 137, 102);
               }
               else if (chance < .8) {
                  color = new Color(255, 240, 185);
               }
               else {
                  color = new Color(255, 176, 59);
               }*/
            material = new Material();
            material.ReflectanceSpectrum = new ReflectanceSpectrum(color);
            //material.BRDF = brdf;

            Sphere sphere = new Sphere(material);

            float originX = (float) (sphereXInterval * i + Math.random() * 5 - 10);
            float originY = (float) (sphereYInterval * j + Math.random() * 5 - 10);
            float originZ = /*sphereZInterval * k + */ (float) (Math.random() * 100 + 60);

            sphere.Origin = new Point3(originX, originY, originZ);
            sphere.Radius = (float) (Math.random() * 5 + 5);

            scene.addShape(sphere);
            //}
         }
      }

      Sphere sphere = new Sphere();
      sphere.Origin = new Point3(300, 300, 3000);
      sphere.Radius = 70;


      SpectralPowerDistribution lightSPD = new SpectralPowerDistribution(100000.0f, 100000.0f, 100000.0f);

      AbstractLight light = new SphereLight(sphere, lightSPD);

      scene.Shapes.add(light);
      scene.addLight(light);

      return scene;

//      SphereLight sphereLight = new SphereLight(50, material);
//      sphereLight.Origin = new Point(300, 300, 1000);
//      sphereLight.Radius = 75;
//      scene.addLight(sphereLight);
//      scene.addLight(new PointLight(new Point(300, 300, 10000), 2500));


   }

   public static AbstractScene SpheresInAnXPattern(int x, int y) {
      CameraSettings settings = new CameraSettings();
      settings.x = x;
      settings.y = y;
      settings.focusDistance = 500;
      settings.aperture = new SquareAperture(5);

      Point3 origin = new Point3(100, 200, 600);
      Vector3 direction = new Vector3(-.1f, -.3f, -1);

      Camera camera = new PerspectiveCamera(settings, null);

      AbstractScene scene = new KDScene(camera);

      // white vertical z plane

      Point3 planeOrigin = new Point3(0, 0, 0);
      Normal planeNormal = new Normal(0, 0, 1);

      Material material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 240, 185));

      Point3 b0 = new Point3(-10000, -10000, -1);
      Point3 b1 = new Point3(10000, 10000, 0);

      Box box = new Box(b0, b1, material);
      scene.addShape(box);

      //BoundingBox box = new BoundingBox(planeOrigin, planeNormal, material);
      //scene.addShape(plane);

      float offset = 1;

      float increment = 2;

      for (int i = 10; i <= 110; i += 50) {
         //for (int j = 10; j <= 110; j+= 50) {
         for (int k = 10; k <= 100; k += 50) {
            float originX = i + offset;
            float originY = i - offset;
            float originZ = k + 15;

            material = new Material();

            material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(0, 131, 255));
            //material.setIndexOfRefraction(1.1);

            Sphere sphere = new Sphere(material);
            sphere.Origin = new Point3(originX, originY, originZ);
            sphere.Radius = 20;
            scene.addShape(sphere);
            offset += increment;
         }

         //}
         offset += increment;
      }

      SpectralPowerDistribution spd = new SpectralPowerDistribution(1.0f, 1.0f, 1.0f);

      scene.addLight(new PointLight(spd, new Point3(300, 300, 900)));
      scene.addLight(new PointLight(spd, new Point3(300, 900, 300)));
      scene.addLight(new PointLight(spd, new Point3(900, 300, 300)));

      return scene;
   }

   public static AbstractScene FourReflectiveSphereWithLights(int x, int y) {

      CameraSettings settings = new CameraSettings();
      settings.x = x;
      settings.y = y;
      settings.focusDistance = 250;
      settings.aperture = new SquareAperture(5);

      Point3 origin = new Point3(100, 800, 1500);
      Vector3 direction = new Vector3(0, -1, -1);


      Camera camera = new PerspectiveCamera(settings, null);
      //Vector orientation = new Vector(new Point(x/2, y/2, 1600), new Point(0, 0, -1));

      AbstractScene scene = new KDScene(camera);

      scene.numFrames = 1;

      Point3 p0, p1;
      Box box;

      Material material;
      Sphere sphere;

      /*material = new Material();
      material.setColor(new Color(70, 137, 102));
      material.setDiffuse(.33);
      material.setReflectivity(.33);
      material.setTransparency(.33);
      material.setIndexOfRefraction(1.333);

      sphere = new Sphere(material);
      sphere.Origin = new Point(-900, 1150, 600.0f);
      sphere.Radius = 150;

      scene.addShape(sphere);*/


      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(70, 137, 102));
      material.IndexOfRefraction = 1.2f;

      sphere = new Sphere(material);
      sphere.Origin = new Point3(-200, 700, 500.0f);
      sphere.Radius = 150;

      //scene.addShape(sphere);

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 240, 185));
      material.IndexOfRefraction = 1.0f;

      sphere = new Sphere(material);
      sphere.Origin = new Point3(500, 100, 600.0f);
      sphere.Radius = 150;

      //scene.addShape(sphere);

      p0 = new Point3(-100, -100, -100);
      p1 = new Point3(100, 100, 100);
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(30, 120, 120));

      ArrayList<Transform> list = new ArrayList<>();
      list.add(Transform.translate(new Vector3(-200, 100, 1000)));
      //list.add(Transform.rotateZ(45));
      //list.add(Transform.rotateY(90));
      //list.add(Transform.rotateX(60));
      list.add(Transform.scale(1.0f, 5.0f, 1.0f));

      Transform[] transforms = Transform.composite(list);

      Transform objectToWorld = transforms[0];
      Transform worldToObject = transforms[1];

      box = new Box(p0, p1, material, objectToWorld, worldToObject);
      scene.addShape(box);


      for (int i = 0; i < 1; i++) {
         p0 = new Point3(-100, -100, -100);
         p1 = new Point3(100, 100, 100);

         material = new Material();
         material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(0, i * 10, 220));

         list = new ArrayList<>();

         //

         list.add(Transform.translate(new Vector3((float) (Math.sin(i) * i * 120 - 400), -(0 - 20 * i), 700)));
         //list.add(Transform.rotateZ(45));
         list.add(Transform.rotateY(i * 5));
         //list.add(Transform.rotateX(60));
         list.add(Transform.scale(0.3f, (i * .2f) + .1f, 0.3f));

         transforms = Transform.composite(list);

         objectToWorld = transforms[0];
         worldToObject = transforms[1];

         box = new Box(p0, p1, material, objectToWorld, worldToObject);
         scene.addShape(box);
      }


      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 176, 59));

      sphere = new Sphere(material);
      sphere.Origin = new Point3(-200, 100, 500);
      sphere.Radius = 150;

      scene.addShape(sphere);

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(182, 73, 38));
      //material.setSpecular(.3);

      list = new ArrayList<>();
      list.add(Transform.translate(new Vector3(500, 200, 900)));
      list.add(Transform.rotateZ(45));
      list.add(Transform.rotateY(45));
      list.add(Transform.rotateX(60));
      list.add(Transform.scale(2.0f, 1.0f, 1.0f));

      transforms = Transform.composite(list);

      objectToWorld = transforms[0];
      worldToObject = transforms[1];

      sphere = new Sphere(worldToObject, objectToWorld, material);
      sphere.Origin = new Point3(0, 0, 0);

      scene.addShape(sphere);

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(128, 38, 163));
      //material.setSpecular(.3);

      list = new ArrayList<>();
      list.add(Transform.translate(new Vector3(400, 50, 400)));
      list.add(Transform.rotateY(70));
      list.add(Transform.rotateZ(90));

      list.add(Transform.scale(50.0f, 200.0f, 50.0f));

      transforms = Transform.composite(list);

      objectToWorld = transforms[0];
      worldToObject = transforms[1];

      Cylinder cylinder = new Cylinder(1, 1, worldToObject, objectToWorld, material);

      scene.addShape(cylinder);

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(142, 40, 0));

      sphere = new Sphere(material);
      sphere.Origin = new Point3(160, 300, 300.0f);
      sphere.Radius = 25;

      //scene.addShape(sphere);

      // bottom horizontal orange plane


      p0 = new Point3(-10000, -1, -10000);
      p1 = new Point3(10000, 0, 10000);

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 240, 185));

      box = new Box(p0, p1, material);

      scene.addShape(box);

      // top horizontal orange plane


      p0 = new Point3(-10000, 1001, -10000);
      p1 = new Point3(10000, 1000, 10000);

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 131, 0));

      box = new Box(p0, p1, material);

      //scene.addShape(box);

      // forward white vertical z plane

      p0 = new Point3(-10000, -10000, -400);
      p1 = new Point3(10000, 10000, -401);

      Point3 planeOrigin = new Point3(0, 0, -400);
      Normal planeNormal = new Normal(0, 0, 1);

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 240, 185));

      box = new Box(p0, p1, material);
      scene.addShape(box);

      // back white vertical z plane

      p0 = new Point3(-10000, -10000, 5000);
      p1 = new Point3(10000, 10000, 5001);

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 240, 185));

      box = new Box(p0, p1, material);
      //scene.addShape(box);

      // left blue vertical x plane

      p0 = new Point3(-1000, -10000, -10000);
      p1 = new Point3(-1001, 11000, 10000);

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(0, 131, 255));

      box = new Box(p0, p1, material);
      //scene.addShape(box);

      // right blue vertical x plane

      p0 = new Point3(1000, -10000, -10000);
      p1 = new Point3(1001, 11000, 10000);


      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(0, 131, 255));

      box = new Box(p0, p1, material);
      //scene.addShape(box);

      SpectralPowerDistribution spd = new SpectralPowerDistribution(1.0f, 1.0f, 1.0f);

      sphere = new Sphere();
      sphere.Origin = new Point3(1000, 1000, 500);
      sphere.Radius = 50;

      SphereLight sphereLight = new SphereLight(sphere, spd);

      PointLight pointLight = new PointLight(spd, new Point3(1000, 1000, 500));
      scene.addLight(pointLight);


      //scene.addLight(new PointLight(new Point(300, 500, 700), 15.0f));
      //scene.addLight(new PointLight(new Point(400, 1000, 1300), 40.0f));
      //scene.addLight(new PointLight(new Point(300, 300, 300), 5.0f));
      //scene.addLight(new PointLight(new Point(300, 300, 1500), 10.0f));
      //scene.addLight(new PointLight(new Point(685, 360, -350), 5.0f));
      //scene.addLight(new PointLight(new Point(575, 180, -200), 5.0f));
      return scene;
   }

   public static AbstractScene FourReflectiveSphereWithLightsPointable(int x, int y) {

      CameraSettings settings = new CameraSettings();
      settings.x = x;
      settings.y = y;
      settings.focusDistance = 500;
      settings.aperture = new CircleAperture(50);

      Point3 origin = new Point3(300, 300, 2500);
      Vector3 direction = new Vector3(0, 0, -1);

      Camera camera = new PerspectiveCamera(settings, null);

      AbstractScene scene = new NaiveScene(camera);

      scene.numFrames = 1;

      // lights

      Material material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(64, 192, 255));

      Point3 location = new Point3(250, 360, 600);

//      SphereLight sphereLight = new SphereLight(25, material);
//
//      PointLight pointLight = new PointLight(location, 50);
//
//      sphereLight.Origin = location;
//      sphereLight.Radius = 50;
//
//      scene.addLight(sphereLight);
//      //scene.addShape(sphereLight);
//      //scene.addLight(pointLight);
//
//      location = new Point(300, 300, 300);
//
//      pointLight = new PointLight(location, 15);
//
//
//      material = new Material();
//      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 255, 64));
//
//      sphereLight = new SphereLight(15, material);
//      sphereLight.Origin = location;
//      sphereLight.Radius = 50;
//
//      scene.addLight(sphereLight);
      //scene.addShape(sphereLight);
      //scene.addLight(pointLight);
/*
      sphereLight = new SphereLight(5);
      sphereLight.Origin = new Point(300, 300, 300);
      sphereLight.Radius = 50;

      scene.addLight(sphereLight);

      sphereLight = new SphereLight(10);
      sphereLight.Origin = new Point(300, 300, 1500);
      sphereLight.Radius = 50;

      scene.addLight(sphereLight);

      sphereLight = new SphereLight(5);
      sphereLight.Origin = new Point(575, 180, -200);
      sphereLight.Radius = 50;

      scene.addLight(sphereLight);
      */

/*
      scene.addLight(new PointLight(new Point(160, 360, 600), 5.0f));
      scene.addLight(new PointLight(new Point(480, 360, 600), 5.0f));
      scene.addLight(new PointLight(new Point(300, 300, 300), 5.0f));
      scene.addLight(new PointLight(new Point(300, 300, 1500), 10.0f));
      //scene.addLight(new PointLight(new Point(685, 360, -350), 5.0f));
      scene.addLight(new PointLight(new Point(575, 180, -200), 5.0f));
*/


      // horizontal orange plane

      Point3 planeOrigin = new Point3(0, 0, 0);
      Normal planeNormal = new Normal(0, 1, 0);

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 131, 0));

      ImplicitPlane plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addShape(plane);

      // white vertical z plane

      planeOrigin = new Point3(0, 0, -1000);
      planeNormal = new Normal(0, 0, 1);


      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 240, 185));

      plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addShape(plane);

      // white vertical x plane

      planeOrigin = new Point3(800, 0, 0);
      planeNormal = new Normal(-1, 0, 0);


      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 240, 185));

      plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addShape(plane);

      // blue vertical x plane

      planeOrigin = new Point3(-200, 0, 0);
      planeNormal = new Normal(1, 0, 0);


      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(0, 131, 255));

      plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addShape(plane);


      // green sphere

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(70, 137, 102));
      material.IndexOfRefraction = 1.2f;

      Sphere sphere = new Sphere(material);
      sphere.Origin = new Point3(-200, 350, -100.0f);
      sphere.Radius = 150;

      scene.addShape(sphere);

      // green sphere

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(70, 137, 102));
      material.IndexOfRefraction = 1.2f;

      sphere = new Sphere(material);
      sphere.Origin = new Point3(500, 350, 500.0f);
      sphere.Radius = 150;

      scene.addShape(sphere);

      // white sphere

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 240, 185));
      material.IndexOfRefraction = 1.3f;

      sphere = new Sphere(material);
      sphere.Origin = new Point3(500, 100, 600.0f);
      sphere.Radius = 150;

      scene.addShape(sphere);

      // big dark orange sphere

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(182, 73, 38));

      sphere = new Sphere(material);
      sphere.Origin = new Point3(800, 350, 1000.0f);
      sphere.Radius = 150;

      scene.addShape(sphere);

      //

      // orange sphere

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 176, 59));

      sphere = new Sphere(material);
      sphere.Origin = new Point3(-200, 100, 1000);
      sphere.Radius = 150;

      scene.addShape(sphere);

      // tiny red sphere

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(142, 40, 0));

      sphere = new Sphere(material);
      sphere.Origin = new Point3(160, 300, 300.0f);
      sphere.Radius = 25;

      scene.addShape(sphere);

      // tiny blue sphere

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(62, 96, 111));

      sphere = new Sphere(material);
      sphere.Origin = new Point3(250, 300, 300.0f);
      sphere.Radius = 25;

      scene.addShape(sphere);


      return scene;
   }

   public static KDScene DiffuseAndSpecularSpheres(int x, int y) {

//      Point3 origin = new Point3(300, 300, 2500);
//      Vector3 direction = new Vector3(0, 0, -1);

      CameraSettings settings = new CameraSettings();
      settings.x = x;
      settings.y = y;
      settings.focusDistance = 1487;
      settings.aperture = new CircleAperture(50);

      float zoomFactor = 1.0f / 2.0f;
      float focusDistance = 1487.0f;

      Camera camera = new PerspectiveCamera(settings, null);

      KDScene scene = new KDScene(camera);

      // lights
//
//      SphereLight sphereLight = new SphereLight(5);
//      sphereLight.Origin = new Point(160, 360, 600);
//      sphereLight.Radius = 50;
//
//      scene.addLight(sphereLight);
//
//      sphereLight = new SphereLight(5);
//      sphereLight.Origin = new Point(480, 360, 600);
//      sphereLight.Radius = 50;
//
//      scene.addLight(sphereLight);
//
//      sphereLight = new SphereLight(5);
//      sphereLight.Origin = new Point(300, 300, 300);
//      sphereLight.Radius = 50;
//
//      scene.addLight(sphereLight);
//
//      sphereLight = new SphereLight(10);
//      sphereLight.Origin = new Point(300, 300, 1500);
//      sphereLight.Radius = 50;
//
//      scene.addLight(sphereLight);
//
//      sphereLight = new SphereLight(5);
//      sphereLight.Origin = new Point(575, 180, -200);
//      sphereLight.Radius = 50;
//
//      scene.addLight(sphereLight);

      //scene.addLight(new PointLight(new Point(160, 360, 600), 5.0f));
      //scene.addLight(new PointLight(new Point(480, 360, 600), 5.0f));
      //scene.addLight(new PointLight(new Point(300, 300, 300), 5.0f));
      //scene.addLight(new PointLight(new Point(300, 300, 1500), 10.0f));
      //scene.addLight(new PointLight(new Point(685, 360, -350), 5.0f));
      //scene.addLight(new PointLight(new Point(575, 180, -200), 5.0f));

      Material material = new Material();

      // horizontal orange plane

      Point3 planeOrigin = new Point3(0, 0, 0);
      Normal planeNormal = new Normal(0, 1, 0);

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 131, 0));

      ImplicitPlane plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addShape(plane);

      // white vertical z plane

      planeOrigin = new Point3(0, 0, -400);
      planeNormal = new Normal(0, 0, 1);


      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 240, 185));

      plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addShape(plane);

      // white vertical x plane

      planeOrigin = new Point3(800, 0, 0);
      planeNormal = new Normal(-1, 0, 0);


      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 240, 185));

      plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addShape(plane);

      // blue vertical x plane

      planeOrigin = new Point3(-200, 0, 0);
      planeNormal = new Normal(1, 0, 0);


      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(0, 131, 255));

      plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addShape(plane);


      // left green sphere

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(70, 137, 102));
      material.IndexOfRefraction = 1.2f;

      Sphere sphere = new Sphere(material);
      sphere.Origin = new Point3(100, 350, -100.0f);
      sphere.Radius = 150;

      scene.addShape(sphere);

      // right green sphere

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(70, 137, 102));
      material.IndexOfRefraction = 1.2f;

      sphere = new Sphere(material);
      sphere.Origin = new Point3(500, 350, -100.0f);
      sphere.Radius = 150;

      scene.addShape(sphere);

      return scene;
   }

   public static AbstractScene AreaLightSourceTest(int x, int y) {

      CameraSettings settings = new CameraSettings();
      settings.x = x;
      settings.y = y;
      settings.fov = 25f;

//      settings.aperture = new CircleAperture(50);

      Transform[] inputTransforms = new Transform[1];
      inputTransforms[0] = Transform.translate(new Vector3(0, 500, 2500));

      Transform[] compositeTransforms = Transform.composite(inputTransforms);

      Camera camera = new PerspectiveCamera(settings, compositeTransforms[0]);

      NaiveScene scene = new NaiveScene(camera);

      Material material;
      Box box;
      Sphere sphere;

      // lights

      SpectralPowerDistribution lightSPD = new SpectralPowerDistribution(Color.white, 1000f);

      material = new Material();
      //material.BRDF = LambertianBRDF;
      material.ReflectanceSpectrum = new ReflectanceSpectrum(Colors.Firenze.Green);
      material.IndexOfRefraction = 1.2f;

      inputTransforms = new Transform[2];
      inputTransforms[0] = Transform.translate(new Vector3(0, 2000, 0));
      inputTransforms[1] = Transform.scale(500);
      compositeTransforms = Transform.composite(inputTransforms);

      sphere = new Sphere(compositeTransforms, material);

      SphereLight sphereLight = new SphereLight(sphere, lightSPD);

      scene.addLight(sphereLight);
      scene.addShape(sphereLight);

      // Bottom box

      material = new Material();
      //material.BRDF = SpecularBRDF;
      material.ReflectanceSpectrum = new ReflectanceSpectrum(Colors.Firenze.Beige);

      inputTransforms = new Transform[2];
      inputTransforms[0] = Transform.scale(1000f, 0.1f, 1000f);
      inputTransforms[1] = Transform.translate(new Vector3(-.5f, -1f, -.5f));

      compositeTransforms = Transform.composite(inputTransforms);

      box = new Box(compositeTransforms, material);
      scene.Shapes.add(box);

      // left green sphere

      material = new Material();
      //material.BRDF = SpecularBRDF;
      material.ReflectanceSpectrum = new ReflectanceSpectrum(Colors.Firenze.Green);
      material.IndexOfRefraction = 1.2f;

      inputTransforms = new Transform[2];
      inputTransforms[0] = Transform.translate(new Vector3(-500, 300, 0.0f));
      inputTransforms[1] = Transform.scale(150f);
      compositeTransforms = Transform.composite(inputTransforms);

      sphere = new Sphere(compositeTransforms, material);
      scene.addShape(sphere);

      // right green sphere

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(Colors.Firenze.Green);
      //material.BRDF = SpecularBRDF;
      material.IndexOfRefraction = 1.2f;

      inputTransforms = new Transform[2];
      inputTransforms[0] = Transform.translate(new Vector3(500, 500, 0.0f));
      inputTransforms[1] = Transform.scale(150f);
      compositeTransforms = Transform.composite(inputTransforms);

      sphere = new Sphere(compositeTransforms, material);
      scene.addShape(sphere);

      // middle

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(Colors.Firenze.Green);
      //material.BRDF = SpecularBRDF;
      material.IndexOfRefraction = 1.2f;

      inputTransforms = new Transform[2];
      inputTransforms[0] = Transform.translate(new Vector3(0, 150, 0.0f));
      inputTransforms[1] = Transform.scale(150f);
      compositeTransforms = Transform.composite(inputTransforms);

      sphere = new Sphere(compositeTransforms, material);
      scene.addShape(sphere);

      scene.Skybox = new CubeMappedSkybox(Skyboxes.Load(Skyboxes.Desert2));

      return scene;
   }

   public static AbstractScene TwoTransparentReflectiveSpheresWithLights(int x, int y) {
      CameraSettings settings = new CameraSettings();
      settings.x = x;
      settings.y = y;
      settings.focusDistance = 500;
      settings.aperture = new CircleAperture(20);

      Point3 origin = new Point3(0, 0, 4000);
      Vector3 direction = new Vector3(0, 0, -1);

      Camera camera = new PerspectiveCamera(settings, null);

      AbstractScene scene = new NaiveScene(camera);

      Material material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(Color.blue);
      //material.setTransparency(.33);
      //material.setIndexOfRefraction(1.333);

      Sphere sphere = new Sphere(material);
      sphere.Origin = new Point3(2 * x / 5, y / 3, 500.0f);
      sphere.Radius = y / 5;

      scene.addShape(sphere);

      Point3 planeOrigin = new Point3(0, 0, -400);
      Normal planeNormal = new Normal(0, 0, 1);

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 131, 0));
      //material.setDiffuse(.5);
      //material.setReflectivity(0);

      ImplicitPlane plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addShape(plane);

      //scene.addLight(new PointLight(new Point(x / 2, y / 2, 500), 25.0f));

      return scene;
   }
}