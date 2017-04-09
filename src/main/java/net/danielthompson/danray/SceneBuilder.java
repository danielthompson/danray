package net.danielthompson.danray;

import net.danielthompson.danray.acceleration.KDScene;
import net.danielthompson.danray.acceleration.compactkd.KDCompactScene;
import net.danielthompson.danray.cameras.*;
import net.danielthompson.danray.cameras.apertures.CircleAperture;
import net.danielthompson.danray.cameras.apertures.SquareAperture;
import net.danielthompson.danray.lights.AbstractLight;
import net.danielthompson.danray.lights.PointLight;
import net.danielthompson.danray.lights.SphereLight;
import net.danielthompson.danray.scenes.NaiveScene;
import net.danielthompson.danray.scenes.AbstractScene;
import net.danielthompson.danray.shading.*;
import net.danielthompson.danray.shading.bxdf.BRDF;
import net.danielthompson.danray.shading.bxdf.GlossyBRDF;
import net.danielthompson.danray.shading.bxdf.LambertianBRDF;
import net.danielthompson.danray.shading.bxdf.MirrorBRDF;
import net.danielthompson.danray.shapes.*;
import net.danielthompson.danray.structures.*;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Vector;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;

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
      public static Color orange = new Color(203, 75, 22 );
      public static Color red = new Color(220, 50, 47);
      public static Color magenta= new Color(211, 54, 130);
      public static Color violet= new Color(108, 113, 196);
      public static Color blue = new Color(38, 139, 210);
      public static Color cyan = new Color(42, 161, 152);
      public static Color green = new Color(133, 153, 0);
   }

   public static ClassLoader loader = SceneBuilder.class.getClassLoader();

   public static BRDF LambertianBRDF = new LambertianBRDF();

   public static BRDF MirrorBRDF = new MirrorBRDF();

   public static AbstractScene SpectralLemon(int x, int y) {

      CameraSettings settings = new CameraSettings();
      settings.X = x;
      settings.Y = y;
      settings.FocalLength = 1200;
      settings.Rotation = 0;
      settings.ZoomFactor =  1.5f;
      settings.FocusDistance = 500;
      settings.Aperture = new CircleAperture(20);

      Point origin = new Point(0, 0, 4000);
      Vector direction = new Vector(0, 0, -1);
      settings.Orientation = new Ray(origin, direction);

      Camera camera = new SimplePointableCamera(settings);

      AbstractScene scene = new NaiveScene(camera);

      // right light

      SpectralPowerDistribution lightSPD = new SpectralPowerDistribution(10.0f, 10.0f, 10.0f);

      Material material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(Color.white);

      Sphere sphere = new Sphere();
      sphere.Origin = new Point(2000, 15000, 15000);
      sphere.Radius = 1000;

      AbstractLight light = new SphereLight(sphere, lightSPD);

      //scene.Shapes.add(light);
      scene.addLight(light);

      // left light

      lightSPD = new SpectralPowerDistribution(1.0f, 0.8f, 0.8f);

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(red);

      sphere = new Sphere();
      sphere.Origin = new Point(-800, 0, 500);
      sphere.Radius = 100;

      light = new SphereLight(sphere, lightSPD);

      //scene.Shapes.add(light);
      scene.addLight(light);


      // left wall

      Material boxMaterial = new Material();
      boxMaterial.ReflectanceSpectrum = new ReflectanceSpectrum(Color.yellow);

      ArrayList<Transform> list = new ArrayList<>();
      list.add(Transform.Translate(new Vector(-1500, 0, 0)));
      list.add(Transform.Scale(1.0f, 1000.0f, 10000.0f));

      Transform[] transforms = Transform.composite(list);

      Transform objectToWorld = transforms[0];
      Transform worldToObject = transforms[1];

      Point p0 = new Point(-1, -1, -1);
      Point p1 = new Point(1, 1, 1);
      Box box = new Box(p0, p1, boxMaterial, objectToWorld, worldToObject);
      box.ID = getNextID();
      //scene.Drawables.add(box);

      // right wall

      boxMaterial = new Material();
      boxMaterial.BRDF = new MirrorBRDF();
      boxMaterial.ReflectanceSpectrum = new ReflectanceSpectrum(Color.yellow);

      list = new ArrayList<>();
      list.add(Transform.Translate(new Vector(1500, 0, 0)));
      list.add(Transform.Scale(1.0f, 1000.0f, 10000.0f));

      transforms = Transform.composite(list);

      objectToWorld = transforms[0];
      worldToObject = transforms[1];

      p0 = new Point(-1, -1, -1);
      p1 = new Point(1, 1, 1);
      box = new Box(p0, p1, boxMaterial, objectToWorld, worldToObject);
      box.ID = getNextID();
      scene.Shapes.add(box);

      // front wall

      boxMaterial = new Material();
      boxMaterial.ReflectanceSpectrum = new ReflectanceSpectrum(Color.green);

      list = new ArrayList<>();
      list.add(Transform.Translate(new Vector(0, 0, -1000)));
      list.add(Transform.Scale(1500.0f, 1000.0f, 1));

      transforms = Transform.composite(list);

      objectToWorld = transforms[0];
      worldToObject = transforms[1];

      p0 = new Point(-1, -1, -1);
      p1 = new Point(1, 1, 1);

      box = new Box(p0, p1, boxMaterial, objectToWorld, worldToObject);
      box.ID = getNextID();
      //scene.Drawables.add(box);

      // back wall

      boxMaterial = new Material();
      boxMaterial.BRDF = new MirrorBRDF();
      boxMaterial.ReflectanceSpectrum = new ReflectanceSpectrum(Color.green);

      list = new ArrayList<>();
      list.add(Transform.Translate(new Vector(0, 0, 10000)));
      list.add(Transform.Scale(1500.0f, 1000.0f, 1));

      transforms = Transform.composite(list);

      objectToWorld = transforms[0];
      worldToObject = transforms[1];

      p0 = new Point(-1, -1, -1);
      p1 = new Point(1, 1, 1);

      box = new Box(p0, p1, boxMaterial, objectToWorld, worldToObject);
      box.ID = getNextID();
      //scene.Drawables.add(box);

      // floor

      boxMaterial = new Material();
      boxMaterial.ReflectanceSpectrum = new ReflectanceSpectrum(Color.green);

      list = new ArrayList<>();
      list.add(Transform.Translate(new Vector(0, -1000, 0)));
      list.add(Transform.Scale(1500.0f, 1, 10000));

      transforms = Transform.composite(list);

      objectToWorld = transforms[0];
      worldToObject = transforms[1];

      p0 = new Point(-1, -1, -1);
      p1 = new Point(1, 1, 1);

      box = new Box(p0, p1, boxMaterial, objectToWorld, worldToObject);
      box.ID = getNextID();

      scene.Shapes.add(box);

      // ceiling

      boxMaterial = new Material();
      boxMaterial.ReflectanceSpectrum = new ReflectanceSpectrum(Color.green);

      list = new ArrayList<>();
      list.add(Transform.Translate(new Vector(0, 1000, 0)));
      list.add(Transform.Scale(1500.0f, 1, 10000));

      transforms = Transform.composite(list);

      objectToWorld = transforms[0];
      worldToObject = transforms[1];

      p0 = new Point(-1, -1, -1);
      p1 = new Point(1, 1, 1);
      box = new Box(p0, p1, boxMaterial, objectToWorld, worldToObject);
      box.ID = getNextID();
      //scene.Drawables.add(box);

      // top left little box

      boxMaterial = new Material();
      boxMaterial.ReflectanceSpectrum = new ReflectanceSpectrum(Color.orange);

      for (int i = 0; i < 10; i++) {
         //for (int j = 0; j < 10; j++) {
            list = new ArrayList<>();
            list.add(Transform.Translate(new Vector(i * 120 - 800, i * 120 - 800, i * i * 20)));
            list.add(Transform.Scale(50, 50, 50));
            transforms = Transform.composite(list);

            objectToWorld = transforms[0];
            worldToObject = transforms[1];

            p0 = new Point(-1, -1, -1);
            p1 = new Point(1, 1, 1);
            box = new Box(p0, p1, boxMaterial, objectToWorld, worldToObject);
            box.ID = getNextID();
            //scene.Drawables.add(box);

         //}
      }


      // bottom right little box

      boxMaterial = new Material();
      boxMaterial.ReflectanceSpectrum = new ReflectanceSpectrum(Color.cyan);

      p0 = new Point(800, -1000, 0);
      p1 = new Point(1300, -500, 500);
      box = new Box(p0, p1, boxMaterial);
      box.ID = getNextID();
      scene.Shapes.add(box);


      material = new Material();

      material.ReflectanceSpectrum = new ReflectanceSpectrum(Color.green);

      list = new ArrayList<>();
      list.add(Transform.Translate(new Vector(-400, 50, 1000)));
      list.add(Transform.RotateY(90));
      list.add(Transform.RotateZ(90));

      list.add(Transform.Scale(50.0f, 1000.0f, 50.0f));

      transforms = Transform.composite(list);

      objectToWorld = transforms[0];
      worldToObject = transforms[1];

      Cylinder cylinder = new Cylinder(1, 1, worldToObject, objectToWorld, material);

      //scene.addShape(cylinder);

      return scene;
   }

   public static AbstractScene Default(int x, int y) {

      CameraSettings settings = new CameraSettings();
      settings.X = x;
      settings.Y = y;
      settings.FieldOfView = 45f;

      settings.FocusDistance = 500;
      settings.Aperture = new CircleAperture(20);

      Transform[] inputTransforms = new Transform[2];
      inputTransforms[0] = Transform.Translate(new Vector(0, 0, 200));
      //inputTransforms[1] = Transform.RotateX(45);
      inputTransforms[1] = Transform.identity;

      Transform[] compositeTransforms = Transform.composite(inputTransforms);

      Camera camera = new TransformCamera(settings, compositeTransforms[0]);

      AbstractScene scene = new NaiveScene(camera);

      // Orange ball

      Material material = new Material();

//      material.BRDF = LambertianBRDF;
//      material.ReflectanceSpectrum = new ReflectanceSpectrum(Firenze.Orange);
//
//      inputTransforms = new Transform[2];
//      inputTransforms[0] = Transform.Translate(new Vector(50.0f, 0.0f, 40.0f));
//      inputTransforms[1] = Transform.Scale(10f);
//
//      compositeTransforms = Transform.composite(inputTransforms);
//
//      Sphere sphere1 = new Sphere(compositeTransforms, material);

      //scene.addShape(sphere1);



      // green ball

//      material = new Material();
//      material.BRDF = new GlossyBRDF(0.75f);
//      material.ReflectanceSpectrum = new ReflectanceSpectrum(Firenze.Green);
//
//      inputTransforms = new Transform[2];
//      inputTransforms[0] = Transform.Translate(new Vector(200.0f, -50.0f, 25.0f));
//      inputTransforms[1] = Transform.Scale(100f, 100f, 100f);
//
//      compositeTransforms = Transform.composite(inputTransforms);
//
//      Sphere sphere3 = new Sphere(compositeTransforms, material);

      //scene.addShape(sphere3);

      // green box

      material = new Material();
      material.BRDF = MirrorBRDF;
      material.ReflectanceSpectrum = new ReflectanceSpectrum(Solarized.cyan);

      inputTransforms = new Transform[3];
      inputTransforms[0] = Transform.Translate(new Vector(100.0f, 0.0f, -25f));
      //inputTransforms[1] = Transform.RotateX(45);
      //inputTransforms[2] = Transform.RotateY(45);
      inputTransforms[1] = Transform.Scale(100f, 100f, 100f);
      inputTransforms[2] = Transform.Translate(new Vector(-.5f, -.5f, -.5f));

      compositeTransforms = Transform.composite(inputTransforms);

      Box box = new Box(compositeTransforms, material);

      scene.addShape(box);

      // yellow ball

      material = new Material();
      material.BRDF = LambertianBRDF;
      material.ReflectanceSpectrum = new ReflectanceSpectrum(Firenze.Beige);

      inputTransforms = new Transform[2];
      //inputTransforms[0] = Transform.Translate(new Vector(-150.0f, -50.0f, 100.0f));
      //inputTransforms[1] = Transform.Scale(55f, 55f, 55f);
      inputTransforms[0] = Transform.Translate(new Vector(0f, 0f, -200.0f));
      inputTransforms[1] = Transform.Scale(50f);

      compositeTransforms = Transform.composite(inputTransforms);

      Sphere sphere2 = new Sphere(compositeTransforms, material);

      scene.addShape(sphere2);

      // white light

      SpectralPowerDistribution lightSPD = new SpectralPowerDistribution(Color.white, 1000000.0f);

      inputTransforms = new Transform[2];
      inputTransforms[0] = Transform.Translate(new Vector(300, 3300, -1500));
      inputTransforms[1] = Transform.Scale(100f, 100f, 100f);

      compositeTransforms = Transform.composite(inputTransforms);

      Sphere sphere = new Sphere(compositeTransforms, null);

      AbstractLight light = new SphereLight(sphere, lightSPD);

      //scene.Shapes.add(light);
      //scene.Lights.add(light);

      // red light

//      lightSPD = new SpectralPowerDistribution(Firenze.Orange, 100000.0f);
//
//      inputTransforms = new Transform[2];
//      inputTransforms[0] = Transform.Translate(new Vector(300, -3300, -1500));
//      inputTransforms[1] = Transform.Scale(100f, 100f, 100f);
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
//      inputTransforms[0] = Transform.Translate(new Vector(300, -3300, -1500));
//      inputTransforms[1] = Transform.Scale(100f, 100f, 100f);
//
//      compositeTransforms = Transform.composite(inputTransforms);
//
//      sphere = new Sphere(compositeTransforms, null);
//
//      PointLight pointLight = new PointLight(lightSPD, new Point(300, 3300, -1500));

      //scene.Shapes.add(light);
      //scene.Lights.add(pointLight);

      // skybox

      try {
         URL url = loader.getResource("images/cubemap/desert 2 - captions.png");
         scene.SkyBoxImage = ImageIO.read(url);
      }
      catch (IOException e) {
         System.out.println("Couldn't load skybox: " + e.getMessage());
         System.out.println("Working Directory = " +
               System.getProperty("user.dir"));
         System.exit(-1);
      }

      return scene;
   }

   public static AbstractScene GlossyStrips(int x, int y) {

      CameraSettings settings = new CameraSettings();
      settings.X = x;
      settings.Y = y;
      settings.FieldOfView = 50;

      settings.FocusDistance = 500;
      settings.Aperture = new CircleAperture(20);

      Transform[] inputTransforms = new Transform[2];
//      inputTransforms[0] = Transform.Translate(new Vector(-300, 0, 200));
      inputTransforms[1] = Transform.RotateY(90);
      inputTransforms[0] = Transform.Translate(new Vector(28.2792f, 3.5f, 0));
      inputTransforms[1] = Transform.identity;

      Transform[] compositeTransforms = Transform.composite(inputTransforms);

      Camera camera = new TransformCamera(settings, compositeTransforms[0]);

      AbstractScene scene = new NaiveScene(camera);

      Material material;
      Box box;

      // strip 1

      material = new Material();
      material.BRDF = MirrorBRDF;
      material.ReflectanceSpectrum = new ReflectanceSpectrum(Solarized.green);

      inputTransforms = new Transform[3];
      inputTransforms[0] = Transform.Translate(new Vector(.264069f, 4.09801f, 0));
      inputTransforms[1] = Transform.RotateZ(-39.8801f);
      inputTransforms[2] = Transform.Scale(2.1f, 0.3f, 8f);
      //inputTransforms[3] = Transform.Translate(new Vector(-.5f, -.5f, -.5f));

      compositeTransforms = Transform.composite(inputTransforms);

      box = new Box(compositeTransforms, material);
      scene.Shapes.add(box);

      // strip 2

      material = new Material();
      material.BRDF = MirrorBRDF;
      material.ReflectanceSpectrum = new ReflectanceSpectrum(Solarized.cyan);

      inputTransforms = new Transform[4];
      inputTransforms[0] = Transform.Translate(new Vector(3.06163f, 2.71702f, 0));
      inputTransforms[1] = Transform.RotateX(-35);
      inputTransforms[2] = Transform.Scale(2.1f, 0.3f, 8f);
      inputTransforms[3] = Transform.Translate(new Vector(-.5f, -.5f, -.5f));

      compositeTransforms = Transform.composite(inputTransforms);

      box = new Box(compositeTransforms, material);
      scene.Shapes.add(box);

      // strip 3

      material = new Material();
      material.BRDF = MirrorBRDF;
      material.ReflectanceSpectrum = new ReflectanceSpectrum(Solarized.blue);

      inputTransforms = new Transform[4];
      inputTransforms[0] = Transform.Translate(new Vector(0.0f, -87.0f, -250f));
      inputTransforms[1] = Transform.RotateX(-65);
      inputTransforms[2] = Transform.Scale(500f, 50f, 1f);
      inputTransforms[3] = Transform.Translate(new Vector(-.5f, -.5f, -.5f));

      compositeTransforms = Transform.composite(inputTransforms);

      box = new Box(compositeTransforms, material);
      scene.Shapes.add(box);

      // strip 4

      material = new Material();
      material.BRDF = MirrorBRDF;
      material.ReflectanceSpectrum = new ReflectanceSpectrum(Solarized.violet);

      inputTransforms = new Transform[4];
      inputTransforms[0] = Transform.Translate(new Vector(0.0f, -112, -187));
      inputTransforms[1] = Transform.RotateX(-80);
      inputTransforms[2] = Transform.Scale(500f, 50f, 1f);
      inputTransforms[3] = Transform.Translate(new Vector(-.5f, -.5f, -.5f));

      compositeTransforms = Transform.composite(inputTransforms);

      box = new Box(compositeTransforms, material);
      scene.Shapes.add(box);

      for (int i = 0; i < 4; i++) {
         SpectralPowerDistribution lightSPD = new SpectralPowerDistribution(Color.white, 10000.0f);

         inputTransforms = new Transform[2];
         inputTransforms[0] = Transform.Translate(new Vector(((float)i - 1.5f) * 80, 100, -50));
         inputTransforms[1] = Transform.Scale((float)Math.pow(i, 1.5) * 5 + 5);

         compositeTransforms = Transform.composite(inputTransforms);

         Sphere sphere = new Sphere(compositeTransforms, null);

         AbstractLight light = new SphereLight(sphere, lightSPD);

         scene.Shapes.add(light);
         scene.Lights.add(light);
      }

      // white light

      SpectralPowerDistribution lightSPD = new SpectralPowerDistribution(Color.white, 1000000.0f);

      inputTransforms = new Transform[2];
      inputTransforms[0] = Transform.Translate(new Vector(300, 3300, -1500));
      inputTransforms[1] = Transform.Scale(100f, 100f, 100f);

      compositeTransforms = Transform.composite(inputTransforms);

      Sphere sphere = new Sphere(compositeTransforms, null);

      AbstractLight light = new SphereLight(sphere, lightSPD);

      //scene.Shapes.add(light);
      //scene.Lights.add(light);

      // red light

//      lightSPD = new SpectralPowerDistribution(Firenze.Orange, 100000.0f);
//
//      inputTransforms = new Transform[2];
//      inputTransforms[0] = Transform.Translate(new Vector(300, -3300, -1500));
//      inputTransforms[1] = Transform.Scale(100f, 100f, 100f);
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
//      inputTransforms[0] = Transform.Translate(new Vector(300, -3300, -1500));
//      inputTransforms[1] = Transform.Scale(100f, 100f, 100f);
//
//      compositeTransforms = Transform.composite(inputTransforms);
//
//      sphere = new Sphere(compositeTransforms, null);
//
//      PointLight pointLight = new PointLight(lightSPD, new Point(300, 3300, -1500));

      //scene.Shapes.add(light);
      //scene.Lights.add(pointLight);

      // skybox

      try {
         URL url = loader.getResource("images/cubemap/desert 2 - captions.png");
         scene.SkyBoxImage = ImageIO.read(url);
      }
      catch (IOException e) {
         System.out.println("Couldn't load skybox: " + e.getMessage());
         System.out.println("Working Directory = " +
               System.getProperty("user.dir"));
         System.exit(-1);
      }

      return scene;
   }

   public static AbstractScene DepthOfFieldTest(int x, int y) {

      CameraSettings settings = new CameraSettings();
      settings.X = x;
      settings.Y = y;
      settings.FocalLength = 1200;
      settings.Rotation = 0;
      settings.ZoomFactor = 1;
      settings.FocusDistance = 500;
      settings.Aperture = new CircleAperture(20);

      Point origin = new Point(100, 75, 700);
      Vector direction = new Vector(0, -.35f, -1);
      settings.Orientation = new Ray(origin, direction);

      Camera camera = new DepthOfFieldCamera(settings);

      AbstractScene scene = new NaiveScene(camera);

      // floor orange plane
      Point planeOrigin = new Point(0, -250, 0);
      Normal planeNormal = new Normal(0, 1, 0);

      Material material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 240, 185));
      material._specular = 1 - .5f;
      material._reflectivity = .5f;

      ImplicitPlane plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addShape(plane);

      // spheres

      for (int i = 0; i < 9; i++) {
         material = new Material();
         material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(0, 131, 255));
         material._specular = 1 - .5f;
         material._reflectivity = .5f;

         Sphere sphere = new Sphere(material);
         int coord = -850 + (175 * i);
         sphere.Origin = new Point(coord, 0, coord);
         sphere.Radius = 100;
         scene.addShape(sphere);

         //scene.addLight(new PointLight(new Point(-450 + (300 * i), 2000, -450 + (300 * i)), 25));
      }

      SpectralPowerDistribution spd = new SpectralPowerDistribution(1.0f, 1.0f, 1.0f);

      scene.addLight(new PointLight(spd, new Point(-650, 600, 925)));

      for (int i = 0; i < 1; i++) {
         scene.addLight(new PointLight(spd, new Point(0, 3000, -175 + (i * 300))));
      }
      return scene;
   }

   public static AbstractScene ManyRegularSpheres(int x, int y) {
      CameraSettings settings = new CameraSettings();
      settings.X = x;
      settings.Y = y;
      settings.FocalLength = 150;
      settings.Rotation = 0;
      settings.ZoomFactor = 1.0f/8.0f;
      settings.FocusDistance = 75;
      settings.Aperture = new CircleAperture(10);

      Point origin = new Point(80, 120, -100);
      Vector direction = new Vector(0, 1, -1);
      settings.Orientation = new Ray(origin, direction);

      Camera camera = null;

      if (Main.UseDepthOfField) {
         camera = new DepthOfFieldCamera(settings);
      }

      else {
//         camera = new IsometricCamera(settings);
         camera = new SimplePointableCamera(settings);
      }

//      AbstractScene scene = new NaiveScene(camera);
      AbstractScene scene = new KDCompactScene(camera);
//      AbstractScene scene = new KDScene(camera);
      scene.numFrames = 1;

      // white vertical z plane
      BRDF brdf = new LambertianBRDF();

      Material material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(240, 240, 240));
      material._specular = 1 - .75f;
      material._reflectivity = .25f;

      material.BRDF = brdf;

      float frontZ = -150;

      Point p0 = new Point(-100, 0, frontZ - 1);
      Point p1 = new Point(500, 600, frontZ);

      Box box = new Box(p0, p1, material);
      scene.addShape(box);

      int total = 320;

      int sphereXInterval = 10;
      int maxSmallSpheresX = total / sphereXInterval;

      int sphereYInterval = 10;
      int maxSmallSpheresY = total / sphereYInterval;

      float radius = sphereXInterval / 3;

      int[] yOffset = new int[maxSmallSpheresX * maxSmallSpheresY];
      for (int i = 0; i < yOffset.length; i++) {
         yOffset[i] = 0;
      }

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
            material._reflectivity = .3f;
            material._transparency = 0;
            material._specular = .1f;
            material.BRDF = brdf;

            Sphere sphere = new Sphere(material);

            float originX = sphereXInterval * i + (j % 5) * 3 + 50;
            float originY = sphereYInterval * j + (yOffset[i * maxSmallSpheresY + j]) + 150;
            float originZ = frontZ + radius;

            sphere.Origin = new Point(originX, originY, originZ);
            sphere.Radius = radius;


            scene.addShape(sphere);
         }
      }

      // right light

      SpectralPowerDistribution lightSPD = new SpectralPowerDistribution(20.0f, 20.0f, 20.0f);

      Sphere sphere = new Sphere();
      sphere.Origin = new Point(300, 300, 2300);
      sphere.Radius = 1000;



      AbstractLight light = new SphereLight(sphere, lightSPD);

      scene.Shapes.add(light);
      scene.addLight(light);

      //scene.addLight(new PointLight(new Point(300, 300, 800), 30));

      //scene.addLight(new PointLight(new Point(x / 20, y / 2, -100), 5.7));
      //scene.addLight(new PointLight(new Point(19 * x / 20, y / 2, -100), 5.7));
      //scene.addLight(new PointLight(new Point(x / 2, 3 * y / 4, -100), 5.7));

      //scene.addLight(new PointLight(new Point(x / 2, y / 2, 300), 5.9));
      //scene.addLight(new PointLight(new Point(x / 2, y / 2, 600), 10.0f));



      return scene;
   }


   public static AbstractScene CornellBox(int x, int y) {
      CameraSettings settings = new CameraSettings();
      settings.X = x;
      settings.Y = y;
      settings.FocalLength = 150;
      settings.Rotation = 0;
      settings.ZoomFactor = 1.0f;
      settings.FocusDistance = 75;
      settings.Aperture = new CircleAperture(10);

      Point origin = new Point(0, 0, 250);
      Vector direction = new Vector(0, 0, -1);
      settings.Orientation = new Ray(origin, direction);

      Camera camera = null;

      if (Main.UseDepthOfField) {
         camera = new DepthOfFieldCamera(settings);
      }

      else {
//         camera = new IsometricCamera(settings);
         camera = new SimplePointableCamera(settings);
      }

      AbstractScene scene = new NaiveScene(camera);
//      AbstractScene scene = new KDCompactScene(camera);
//      AbstractScene scene = new KDScene(camera);
      scene.numFrames = 1;

      BRDF brdf = new LambertianBRDF();

      Material material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(120, 240, 240));
      material._specular = 1 - .75f;
      material._reflectivity = .25f;

      material.BRDF = brdf;

      Point p0 = new Point(-1000, -1000, -1000);
      Point p1 = new Point(1000, 1000, 1000);

      Transform[] inputTransforms = new Transform[2];
      inputTransforms[0] = Transform.Translate(new Vector(-0.5f, -0.5f, -0.5f));
      inputTransforms[1] = Transform.Scale(2000f);

      Transform[]  compositeTransforms = Transform.composite(inputTransforms);

      Box box = new Box(p0, p1, material);
      scene.addShape(box);

      // right light

      SpectralPowerDistribution lightSPD = new SpectralPowerDistribution(2000.0f, 2000.0f, 2000.0f);

      Sphere sphere = new Sphere();
      sphere.Origin = new Point(0, 250, -250);
      sphere.Radius = 50;



      AbstractLight light = new SphereLight(sphere, lightSPD);

      scene.Shapes.add(light);
      scene.addLight(light);

      return scene;
   }

   public static AbstractScene ManyRandomSpheres(int x, int y) {
      CameraSettings settings = new CameraSettings();
      settings.X = x;
      settings.Y = y;
      settings.FocalLength = 2000;
      settings.Rotation = 0;
      settings.ZoomFactor = 1;
      settings.FocusDistance = 450;
      settings.Aperture = new SquareAperture(2);

      Point origin = new Point(300, 300, 250);
      Vector direction = new Vector(0, 0, -1);
      settings.Orientation = new Ray(origin, direction);

      Camera camera = null;
/*
      CameraOrientationMovement movement = new CameraOrientationMovement();
      movement.frame = 240;

      Point finalOrigin = new Point(800, 400, 700);
      Vector finalDirection = new Vector(-.50, 0, -1);
      Ray finalOrientation = new Ray(finalOrigin, finalDirection);

      movement.orientation = finalOrientation;
      settings.Movement = movement;
*/
      if (Main.UseDepthOfField) {
         camera = new DepthOfFieldCamera(settings);

      }

      else {
         //camera = new IsometricCamera(x, y, rotation, zoomFactor, orientation);

         camera = new SimplePointableCamera(settings);
      }

      AbstractScene scene = new NaiveScene(camera);

      scene.numFrames = 1;

      // white vertical z plane

//      Point planeOrigin = new Point(0, 0, 0);
//      Normal planeNormal = new Normal(0, 0, 1);
      BRDF brdf = new LambertianBRDF();
      Material material = new Material();
//      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 240, 185));
//      material._specular = 1 - .75;
//      material._reflectivity = .25;
//      material.BRDF = brdf;
//
//      ImplicitPlane plane = new ImplicitPlane(planeOrigin, planeNormal, material);
//      scene.addShape(plane);

      for (int i = 0; i < 40; i++) {

         float originX = (float) (Math.random() * 600);
         float originY = (float) (Math.random() * 600);
         float originZ = (float) (Math.random() * 10 + 20);

         material = new Material();


         material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(0, 131, 255));
         material._reflectivity = .2f;
         material._transparency = 0;
         material._specular = 1 - .8f;
         material.BRDF = brdf;
         //material.setIndexOfRefraction(1.1);

         Sphere sphere = new Sphere(material);
         sphere.Origin = new Point(originX, originY, originZ);
         sphere.Radius = (float) (Math.random() * 50);

         scene.addShape(sphere);
      }

      int maxSmallSpheresX = 15;
      int sphereXInterval = 40;

      int maxSmallSpheresY = 15;
      int sphereYInterval = 40;

      int maxSmallSpheresZ = 15;
      int sphereZInterval = 40;

      for (int i = 0; i < maxSmallSpheresX; i++) {
         for (int j = 0; j < maxSmallSpheresY; j++) {
            //for (int k = 0; k < maxSmallSpheresZ; k++) {

               int constant = 128;

               int red = (int) (((float)i / (float)maxSmallSpheresX) * (255 - constant) + constant);
               int green = (int) (((float)j / (float)maxSmallSpheresY) * (255 - constant) + constant);
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
            material._reflectivity = .3f;
            material._transparency = 0;
            material._specular = 1 - .7f;
            material.BRDF = brdf;

               Sphere sphere = new Sphere(material);

               float originX = (float) (sphereXInterval * i + Math.random() * 5 - 10);
               float originY = (float) (sphereYInterval * j + Math.random() * 5 - 10);
               float originZ = /*sphereZInterval * k + */ (float) (Math.random() * 100 + 60);

               sphere.Origin = new Point(originX, originY, originZ);
               sphere.Radius = (float) (Math.random() * 5 + 5);

               scene.addShape(sphere);
            //}
         }
      }

      Sphere sphere = new Sphere();
      sphere.Origin = new Point(300, 300, 3000);
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
      settings.X = x;
      settings.Y = y;
      settings.FocalLength = 100000000;
      settings.Rotation = 0;
      settings.ZoomFactor = 1 / 2.0f;
      settings.FocusDistance = 500;
      settings.Aperture = new SquareAperture(5);

      Point origin = new Point(100, 200, 600);
      Vector direction = new Vector(-.1f, -.3f, -1);
      settings.Orientation = new Ray(origin, direction);

      Camera camera = null;

      if (Main.UseDepthOfField) {
         camera = new DepthOfFieldCamera(settings);
      }

      else {
         camera = new IsometricCamera(settings);
      }

      AbstractScene scene = new KDScene(camera);

      // white vertical z plane

      Point planeOrigin = new Point(0, 0, 0);
      Normal planeNormal = new Normal(0, 0, 1);

      Material material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 240, 185));
      material._specular = 1 - .75f;
      material._reflectivity = .25f;

      Point b0 = new Point(-10000, -10000, -1);
      Point b1 = new Point(10000, 10000, 0);

      Box box = new Box(b0, b1, material);
      scene.addShape(box);

      //BoundingBox box = new BoundingBox(planeOrigin, planeNormal, material);
      //scene.addShape(plane);

      float offset = 1;

      float increment = 2;

      for (int i = 10; i <= 110; i += 50) {
         //for (int j = 10; j <= 110; j+= 50) {
            for (int k = 10; k <= 100; k+= 50) {
               float originX = i + offset;
               float originY = i - offset;
               float originZ = k + 15;

               material = new Material();

               material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(0, 131, 255));
               material._reflectivity = .2f;
               material._transparency = 0;
               material._specular = 1 - .8f;
               //material.setIndexOfRefraction(1.1);

               Sphere sphere = new Sphere(material);
               sphere.Origin = new Point(originX, originY, originZ);
               sphere.Radius = 20;
               scene.addShape(sphere);
               offset += increment;
            }

         //}
         offset += increment;
      }

      SpectralPowerDistribution spd = new SpectralPowerDistribution(1.0f, 1.0f, 1.0f);

      scene.addLight(new PointLight(spd, new Point(300, 300, 900)));
      scene.addLight(new PointLight(spd, new Point(300, 900, 300)));
      scene.addLight(new PointLight(spd, new Point(900, 300, 300)));

      return scene;
   }


/*
   public static KDScene ReflectiveTriangleMeshWithLight(int x, int y) {
      KDScene scene = new KDScene(null);

      // teapot

      WavefrontObjectImporter importer = new WavefrontObjectImporter(new File("models/teapot.obj"));
      Material material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(182, 73, 38);
      material._specular = 1 - .5;
      material._reflectivity = .5;
      material._transparency = 0;

      TriangleMesh mesh = importer.Process();
      mesh.SetMaterial(material);
      mesh.SetRotation(new Tuple(160, 20, 20));
      mesh.SetOrigin(new Point(2 * x / 3, y / 2, 400));

      scene.addShape(mesh);
/*
      // sphere

      material = new Material();
      material.setColor(new Color(70, 137, 102));
      material.setDiffuse(.33);
      material.setReflectivity(.33);
      material.setTransparency(.33);
      material.setIndexOfRefraction(1.333);

      Sphere sphere = new Sphere(material);
      sphere.Origin = new Point(2 * x / 5, y / 3, 400.0f);
      sphere.Radius = y / 6;

      scene.addShape(sphere);

      // plane

      Point planeOrigin = new Point(0, 0, 0);
      Point planeNormal = new Point(0, 0, -1);

      Vector planeNormal1 = new Vector(planeOrigin, planeNormal);

      material = new Material();
      material.setColor(new Color(182, 73, 38));
      material.setDiffuse(.5);
      material.setReflectivity(.5);

      ImplicitPlane plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addShape(plane);
*/
      // lights

     // scene.addLight(new PointLight(new Point(x / 3, y / 2, 900), 20.0f));
     // scene.addLight(new PointLight(new Point(2 * x / 3, 2 * y / 3, 900), 20.0f));
   //   return scene;
 //  }
//*/
   public static AbstractScene FourReflectiveSphereWithLights(int x, int y) {

      CameraSettings settings = new CameraSettings();
      settings.X = x;
      settings.Y = y;
      settings.FocalLength = 1200;
      settings.Rotation = 0;
      settings.ZoomFactor = 1 / 1.5f;
      settings.FocusDistance = 250;
      settings.Aperture = new SquareAperture(5);

      Point origin = new Point(100, 800, 1500);
      Vector direction = new Vector(0, -1, -1);

      settings.Orientation = new Ray(origin, direction);

      Camera camera = null;
/*
      CameraOrientationMovement movement = new CameraOrientationMovement();
      movement.frame = 240;
      movement.orientation = new Ray(new Point(100, 300, 1500), new Vector(0, 0, -1));

      settings.Movement = movement;
*/
      if (Main.UseDepthOfField) {
         camera = new SimplePointableCamera(settings);
      }

      else {
         camera = new SimplePointableCamera(settings);
      }
      //Vector orientation = new Vector(new Point(x/2, y/2, 1600), new Point(0, 0, -1));

      AbstractScene scene = new KDScene(camera);

      scene.numFrames = 1;

      Point p0, p1;
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
      material._specular = 1 - .2f;
      material._reflectivity = 0;
      material._transparency = .8f;
      material._indexOfRefraction = 1.2f;

      sphere = new Sphere(material);
      sphere.Origin = new Point(-200, 700, 500.0f);
      sphere.Radius = 150;

      //scene.addShape(sphere);

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 240, 185));
      material._specular = 1 - .5f;
      material._reflectivity = .5f;
      material._transparency = 0;
      material._indexOfRefraction = 1.0f;

      sphere = new Sphere(material);
      sphere.Origin = new Point(500, 100, 600.0f);
      sphere.Radius = 150;

      //scene.addShape(sphere);

      p0 = new Point(-100, -100, -100);
      p1 = new Point(100, 100, 100);
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(30, 120, 120));
      material._reflectivity = .4f;
      material._specular = .3f;
      material._specular = 1 - .4f;

      ArrayList<Transform> list = new ArrayList<>();
      list.add(Transform.Translate(new Vector(-200, 100, 1000)));
      //list.add(Transform.RotateZ(45));
      //list.add(Transform.RotateY(90));
      //list.add(Transform.RotateX(60));
      list.add(Transform.Scale(1.0f, 5.0f, 1.0f));

      Transform[] transforms = Transform.composite(list);

      Transform objectToWorld = transforms[0];
      Transform worldToObject = transforms[1];

      box = new Box(p0, p1, material, objectToWorld, worldToObject);
      scene.addShape(box);



      for (int i = 0; i < 1; i++) {
         p0 = new Point(-100, -100, -100);
         p1 = new Point(100, 100, 100);

         material = new Material();
         material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(0, i * 10, 220));
         material._reflectivity = .4f;
         material._specular = .3f;
         material._specular = 1 - .4f;

         list = new ArrayList<>();

         //

         list.add(Transform.Translate(new Vector((float) (Math.sin(i) * i * 120 - 400), -(0 - 20 * i), 700)));
         //list.add(Transform.RotateZ(45));
         list.add(Transform.RotateY(i * 5));
         //list.add(Transform.RotateX(60));
         list.add(Transform.Scale(0.3f, (i * .2f) + .1f, 0.3f));

         transforms = Transform.composite(list);

         objectToWorld = transforms[0];
         worldToObject = transforms[1];

         box = new Box(p0, p1, material, objectToWorld, worldToObject);
         scene.addShape(box);
      }



      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 176, 59));
      material._reflectivity = .01f;
      material._specular = 1 - .99f;

      sphere = new Sphere(material);
      sphere.Origin = new Point(-200, 100, 500);
      sphere.Radius = 150;

      scene.addShape(sphere);

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(182, 73, 38));
      material._reflectivity = .2f;
      //material.setSpecular(.3);
      material._specular = 1 - .8f;

      list = new ArrayList<>();
      list.add(Transform.Translate(new Vector(500, 200, 900)));
      list.add(Transform.RotateZ(45));
      list.add(Transform.RotateY(45));
      list.add(Transform.RotateX(60));
      list.add(Transform.Scale(2.0f, 1.0f, 1.0f));

      transforms = Transform.composite(list);

      objectToWorld = transforms[0];
      worldToObject = transforms[1];

      sphere = new Sphere(worldToObject, objectToWorld, material);
      sphere.Origin = new Point(0, 0, 0);

      scene.addShape(sphere);

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(128, 38, 163));
      material._reflectivity = .2f;
      //material.setSpecular(.3);
      material._specular = 1 - .8f;

      list = new ArrayList<>();
      list.add(Transform.Translate(new Vector(400, 50, 400)));
      list.add(Transform.RotateY(70));
      list.add(Transform.RotateZ(90));

      list.add(Transform.Scale(50.0f, 200.0f, 50.0f));

      transforms = Transform.composite(list);

      objectToWorld = transforms[0];
      worldToObject = transforms[1];

      Cylinder cylinder = new Cylinder(1, 1, worldToObject, objectToWorld, material);

      scene.addShape(cylinder);

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(142, 40, 0));
      material._reflectivity = .95f;
      material._specular = 1 - .05f;

      sphere = new Sphere(material);
      sphere.Origin = new Point(160, 300, 300.0f);
      sphere.Radius = 25;

      //scene.addShape(sphere);

      // bottom horizontal orange plane


      p0 = new Point(-10000, -1, -10000);
      p1 = new Point(10000, 0, 10000);

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 240, 185));
      material._specular = 1 - .95f;
      material._reflectivity = .05f;

      box = new Box(p0, p1, material);

      scene.addShape(box);

      // top horizontal orange plane


      p0 = new Point(-10000, 1001, -10000);
      p1 = new Point(10000, 1000, 10000);

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 131, 0));
      material._specular = 1 - .5f;
      material._reflectivity = .5f;

      box = new Box(p0, p1, material);

      //scene.addShape(box);

      // forward white vertical z plane

      p0 = new Point(-10000, -10000, -400);
      p1 = new Point(10000, 10000, -401);

      Point planeOrigin = new Point(0, 0, -400);
      Normal planeNormal = new Normal(0, 0, 1);

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 240, 185));
      material._specular = 1 - .75f;
      material._reflectivity = .25f;

      box = new Box(p0, p1, material);
      scene.addShape(box);

      // back white vertical z plane

      p0 = new Point(-10000, -10000, 5000);
      p1 = new Point(10000, 10000, 5001);

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 240, 185));
      material._specular = 1 - .75f;
      material._reflectivity = .25f;

      box = new Box(p0, p1, material);
      //scene.addShape(box);

      // left blue vertical x plane

      p0 = new Point(-1000, -10000, -10000);
      p1 = new Point(-1001, 11000, 10000);

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(0, 131, 255));
      material._specular = 1 - .75f;
      material._reflectivity = .25f;

      box = new Box(p0, p1, material);
      //scene.addShape(box);

      // right blue vertical x plane

      p0 = new Point(1000, -10000, -10000);
      p1 = new Point(1001, 11000, 10000);


      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(0, 131, 255));
      material._specular = 1 - .75f;
      material._reflectivity = .25f;

      box = new Box(p0, p1, material);
      //scene.addShape(box);

      SpectralPowerDistribution spd = new SpectralPowerDistribution(1.0f, 1.0f, 1.0f);

      sphere = new Sphere();
      sphere.Origin = new Point(1000, 1000, 500);
      sphere.Radius = 50;

      SphereLight sphereLight = new SphereLight(sphere, spd);

      PointLight pointLight = new PointLight(spd, new Point(1000, 1000, 500));
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
      settings.X = x;
      settings.Y = y;
      settings.FocalLength = 1200;
      settings.Rotation = 0;
      settings.ZoomFactor = 1 / 2.0f;
      settings.FocusDistance = 500;
      settings.Aperture = new CircleAperture(50);

      Point origin = new Point(300, 300, 2500);
      Vector direction = new Vector(0, 0, -1);

      settings.Orientation = new Ray(origin, direction);


      Camera camera = null;

      if (Main.UseDepthOfField) {
         camera = new DepthOfFieldCamera(settings);
      }

      else {
         camera = new SimplePointableCamera(settings);
         //camera = new DepthOfFieldCamera(settings);

      }

      AbstractScene scene = new NaiveScene(camera);

      scene.numFrames = 1;

      // lights

      Material material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(64, 192, 255));

      Point location = new Point(250, 360, 600);

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

      Point planeOrigin = new Point(0, 0, 0);
      Normal planeNormal = new Normal(0, 1, 0);

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 131, 0));
      material._specular = 1 - .5f;
      material._reflectivity = .5f;

      ImplicitPlane plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addShape(plane);

      // white vertical z plane

      planeOrigin = new Point(0, 0, -1000);
      planeNormal = new Normal(0, 0, 1);

      

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 240, 185));
      material._specular = 0;
      material._reflectivity = 0;

      plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addShape(plane);

      // white vertical x plane

      planeOrigin = new Point(800, 0, 0);
      planeNormal = new Normal(-1, 0, 0);

      

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 240, 185));
      material._specular = 1 - .9f;
      material._reflectivity = .1f;

      plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addShape(plane);

      // blue vertical x plane

      planeOrigin = new Point(-200, 0, 0);
      planeNormal = new Normal(1, 0, 0);

      

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(0, 131, 255));
      material._specular = 0;
      material._reflectivity = 0;

      plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addShape(plane);


      // green sphere

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(70, 137, 102));
      material._specular = 1 - .5f;
      material._reflectivity = .5f;
      material._transparency = 0;
      material._indexOfRefraction = 1.2f;

      Sphere sphere = new Sphere(material);
      sphere.Origin = new Point(-200, 350, -100.0f);
      sphere.Radius = 150;

      scene.addShape(sphere);

      // green sphere

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(70, 137, 102));
      material._specular = 1 - .5f;
      material._reflectivity = .5f;
      material._transparency = 0;
      material._indexOfRefraction = 1.2f;

      sphere = new Sphere(material);
      sphere.Origin = new Point(500, 350, 500.0f);
      sphere.Radius = 150;

      scene.addShape(sphere);

      // white sphere

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 240, 185));
      material._specular = 1 - .1f;
      material._reflectivity = .9f;
      material._transparency = 0;
      material._indexOfRefraction = 1.3f;

      sphere = new Sphere(material);
      sphere.Origin = new Point(500, 100, 600.0f);
      sphere.Radius = 150;

      scene.addShape(sphere);

      // big dark orange sphere

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(182, 73, 38));
      material._reflectivity = .5f;
      material._specular = 1 - .5f;

      sphere = new Sphere(material);
      sphere.Origin = new Point(800, 350, 1000.0f);
      sphere.Radius = 150;

      scene.addShape(sphere);

      //

      // orange sphere

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 176, 59));
      material._reflectivity = .5f;
      material._specular = 1 - .5f;

      sphere = new Sphere(material);
      sphere.Origin = new Point(-200, 100, 1000);
      sphere.Radius = 150;

      scene.addShape(sphere);

      // tiny red sphere

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(142, 40, 0));
      material._reflectivity = .5f;
      material._specular = 1 - .5f;

      sphere = new Sphere(material);
      sphere.Origin = new Point(160, 300, 300.0f);
      sphere.Radius = 25;

      scene.addShape(sphere);

      // tiny blue sphere

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(62, 96, 111));
      material._reflectivity = .6f;
      material._specular = 1 - .4f;

      sphere = new Sphere(material);
      sphere.Origin = new Point(250, 300, 300.0f);
      sphere.Radius = 25;

      scene.addShape(sphere);



      return scene;
   }

   public static KDScene DiffuseAndSpecularSpheres(int x, int y) {

      Point origin = new Point(300, 300, 2500);
      Vector direction = new Vector(0, 0, -1);
      Ray orientation = new Ray(origin, direction);

      CameraSettings settings = new CameraSettings();
      settings.X = x;
      settings.Y = y;
      settings.FocalLength = 1200;
      settings.Rotation = 100;
      settings.FocusDistance = 1487;
      settings.Orientation = orientation;
      settings.Aperture = new CircleAperture(50);
      settings.ZoomFactor = 0.5f;

      float zoomFactor = 1.0f/2.0f;
      float focusDistance = 1487.0f;



      Camera camera = null;

      if (Main.UseDepthOfField) {
         camera = new DepthOfFieldCamera(settings);
      }

      else {
         camera = new SimplePointableCamera(settings);
      }

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

      Point planeOrigin = new Point(0, 0, 0);
      Normal planeNormal = new Normal(0, 1, 0);

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 131, 0));
      material._specular = 1;
      material._reflectivity = .1f;

      ImplicitPlane plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addShape(plane);

      // white vertical z plane

      planeOrigin = new Point(0, 0, -400);
      planeNormal = new Normal(0, 0, 1);

      

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 240, 185));
      material._specular = 1;
      material._reflectivity = .25f;

      plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addShape(plane);

      // white vertical x plane

      planeOrigin = new Point(800, 0, 0);
      planeNormal = new Normal(-1, 0, 0);

      

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 240, 185));
      material._specular = 1;
      material._reflectivity = .25f;

      plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addShape(plane);

      // blue vertical x plane

      planeOrigin = new Point(-200, 0, 0);
      planeNormal = new Normal(1, 0, 0);

      

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(0, 131, 255));
      material._specular = 0;

      plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addShape(plane);


      // left green sphere

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(70, 137, 102));
      material._specular = 1 - .5f;
      material._reflectivity = .5f;
      material._transparency = 0;
      material._indexOfRefraction = 1.2f;

      Sphere sphere = new Sphere(material);
      sphere.Origin = new Point(100, 350, -100.0f);
      sphere.Radius = 150;

      scene.addShape(sphere);

      // right green sphere

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(70, 137, 102));
      material._specular = 1 - .5f;
      material._reflectivity = .5f;
      material._transparency = 0;
      material._indexOfRefraction = 1.2f;

      sphere = new Sphere(material);
      sphere.Origin = new Point(500, 350, -100.0f);
      sphere.Radius = 150;

      scene.addShape(sphere);

      return scene;
   }

   public static KDScene AreaLightSourceTest(int x, int y) {

      CameraSettings settings = new CameraSettings();
      settings.X = x;
      settings.Y = y;
      settings.FocalLength = 1200;
      settings.ZoomFactor = 1;
      settings.FocusDistance = 1487;

      Point origin = new Point(0, 500, 2500);
      Vector direction = new Vector(0, 0, -1);
      settings.Orientation = new Ray(origin, direction);

      settings.Aperture = new CircleAperture(50);

      Camera camera = null;

      if (Main.UseDepthOfField) {
         camera = new DepthOfFieldCamera(settings);
      }

      else {

         camera = new SimplePointableCamera(settings);

      }

      KDScene scene = new KDScene(camera);

      // lights
//
//      SphereLight sphereLight = new SphereLight(400);
//      sphereLight.Origin = new Point(0, 2000, 0);
//      sphereLight.Radius = 1000;
//
//      PointLight pointLight = new PointLight(new Point(0, 2000, 0), 1000);
//
//      scene.addLight(sphereLight);
//      //scene.addLight(pointLight);

      Material material = new Material();

      // horizontal white plane

      Point planeOrigin = new Point(0, 0, 0);
      Normal planeNormal = new Normal(0, 1, 0);

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 240, 185));
      material._specular = 0;

      ImplicitPlane plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addShape(plane);

      // blue vertical z plane
/*
      planeOrigin = new Point(-200, 0, 0);
      planeNormal = new Point(0, 0, 1);

      planeNormal1 = new Vector(planeOrigin, planeNormal);

      material = new Material();
      material.setColor(new Color(0, 131, 255));
      material.setDiffuse(1);

      plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addShape(plane);
*/

      // left green sphere

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(70, 137, 102));
      material._specular = 1 - .5f;
      material._reflectivity = .5f;
      material._transparency = 0;
      material._indexOfRefraction = 1.2f;

      Sphere sphere = new Sphere(material);
      sphere.Origin = new Point(-500, 300, 0.0f);
      sphere.Radius = 150;

      scene.addShape(sphere);

      // right green sphere

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(70, 137, 102));
      material._specular = 1 - .5f;
      material._reflectivity = .5f;
      material._transparency = 0;
      material._indexOfRefraction = 1.2f;

      sphere = new Sphere(material);
      sphere.Origin = new Point(500, 500, 0.0f);
      sphere.Radius = 150;

      scene.addShape(sphere);

      // middle

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(70, 137, 102));
      material._specular = 1 - .5f;
      material._reflectivity = .5f;
      material._transparency = 0;
      material._indexOfRefraction = 1.2f;

      sphere = new Sphere(material);
      sphere.Origin = new Point(0, 150, 0.0f);
      sphere.Radius = 150;

      scene.addShape(sphere);

      return scene;
   }

   public static AbstractScene TwoTransparentReflectiveSpheresWithLights(int x, int y) {
      CameraSettings settings = new CameraSettings();
      settings.X = x;
      settings.Y = y;
      settings.FocalLength = 1200;
      settings.Rotation = 0;
      settings.ZoomFactor =  1.5f;
      settings.FocusDistance = 500;
      settings.Aperture = new CircleAperture(20);

      Point origin = new Point(0, 0, 4000);
      Vector direction = new Vector(0, 0, -1);
      settings.Orientation = new Ray(origin, direction);

      Camera camera = new SimplePointableCamera(settings);

      AbstractScene scene = new NaiveScene(camera);

      Material material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(Color.blue);
      material._reflectivity = .33f;
      //material.setTransparency(.33);
      //material.setIndexOfRefraction(1.333);

      Sphere sphere = new Sphere(material);
      sphere.Origin = new Point(2 * x / 5, y / 3, 500.0f);
      sphere.Radius = y / 5;

      scene.addShape(sphere);

      Point planeOrigin = new Point(0, 0, -400);
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