package net.danielthompson.danray;

import net.danielthompson.danray.acceleration.KDScene;
import net.danielthompson.danray.acceleration.compactkd.KDCompactScene;
import net.danielthompson.danray.cameras.*;
import net.danielthompson.danray.cameras.apertures.CircleAperture;
import net.danielthompson.danray.cameras.apertures.SquareAperture;
import net.danielthompson.danray.imports.SPDFileImporter;
import net.danielthompson.danray.lights.AbstractLight;
import net.danielthompson.danray.lights.PointLight;
import net.danielthompson.danray.lights.SphereLight;
import net.danielthompson.danray.scenes.NaiveScene;
import net.danielthompson.danray.scenes.AbstractScene;
import net.danielthompson.danray.shading.*;
import net.danielthompson.danray.shading.bxdf.BRDF;
import net.danielthompson.danray.shading.bxdf.LambertianBRDF;
import net.danielthompson.danray.shading.bxdf.MirrorBRDF;
import net.danielthompson.danray.shading.fullspectrum.*;
import net.danielthompson.danray.shapes.*;
import net.danielthompson.danray.structures.*;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Vector;

import java.awt.*;
import java.io.File;
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


   public static AbstractScene SpectralLemon(int x, int y) {

      CameraSettings settings = new CameraSettings();
      settings.X = x;
      settings.Y = y;
      settings.FocalLength = 1200;
      settings.Rotation = 0;
      settings.ZoomFactor =  1.5;
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

      AbstractLight light = new SphereLight(lightSPD, sphere);

      //scene.Shapes.add(light);
      scene.addLight(light);

      // left light

      lightSPD = new SpectralPowerDistribution(1.0f, 0.8f, 0.8f);

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(red);

      sphere = new Sphere();
      sphere.Origin = new Point(-800, 0, 500);
      sphere.Radius = 100;

      light = new SphereLight(lightSPD, sphere);

      //scene.Shapes.add(light);
      scene.addLight(light);


      // left wall

      Material boxMaterial = new Material();
      boxMaterial.ReflectanceSpectrum = new ReflectanceSpectrum(Color.yellow);

      ArrayList<Transform> list = new ArrayList<>();
      list.add(Transform.Translate(new Vector(-1500, 0, 0)));
      list.add(Transform.Scale(1.0, 1000.0, 10000.0));

      Transform[] transforms = GetCompositeTransforms(list);

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
      list.add(Transform.Scale(1.0, 1000.0, 10000.0));

      transforms = GetCompositeTransforms(list);

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
      list.add(Transform.Scale(1500.0, 1000.0, 1));

      transforms = GetCompositeTransforms(list);

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
      list.add(Transform.Scale(1500.0, 1000.0, 1));

      transforms = GetCompositeTransforms(list);

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
      list.add(Transform.Scale(1500.0, 1, 10000));

      transforms = GetCompositeTransforms(list);

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
      list.add(Transform.Scale(1500.0, 1, 10000));

      transforms = GetCompositeTransforms(list);

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
            transforms = GetCompositeTransforms(list);

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

      list.add(Transform.Scale(50.0, 1000.0, 50.0));

      transforms = GetCompositeTransforms(list);

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
      settings.FocalLength = 2000;
      settings.Rotation = 0;
      settings.ZoomFactor =  1;
      settings.FocusDistance = 500;
      settings.Aperture = new CircleAperture(20);

      Point origin = new Point(0, 0, 500);
      Vector direction = new Vector(0, 0, -1);
      settings.Orientation = new Ray(origin, direction);

      Camera camera = new SimplePointableCamera(settings);

      AbstractScene scene = new NaiveScene(camera);

      BRDF brdf = new LambertianBRDF();
      Material material = new Material();

      material.BRDF = brdf;
      material.ReflectanceSpectrum = new ReflectanceSpectrum(Color.blue);

      Sphere sphere1 = new Sphere(material);
      sphere1.Origin = new Point(50.0, 50.0, 40.0);
      sphere1.Radius = 10;

      scene.addShape(sphere1);

      material = new Material();
      material.BRDF = brdf;
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 255, 128));

      Sphere sphere2 = new Sphere(material);
      sphere2.Origin = new Point(0.0, 0.0, 20.0);
      sphere2.Radius = 55;

      scene.addShape(sphere2);

      material = new Material();
      material.BRDF = brdf;
      material.ReflectanceSpectrum = new ReflectanceSpectrum(Color.green);

      Sphere sphere3 = new Sphere(material);
      sphere3.Origin = new Point(200.0, 200.0, 25.0);
      sphere3.Radius = 100;

      scene.addShape(sphere3);

      SpectralPowerDistribution lightSPD = new SpectralPowerDistribution(100000.0f, 100000.0f, 100000.0f);

      Sphere sphere = new Sphere();
      sphere.Origin = new Point(300, 3300, -1500);
      sphere.Radius = 1000;

      AbstractLight light = new SphereLight(lightSPD, sphere);

      scene.Shapes.add(light);
      scene.addLight(light);

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
      Vector direction = new Vector(0, -.35, -1);
      settings.Orientation = new Ray(origin, direction);

      Camera camera = new DepthOfFieldCamera(settings);

      AbstractScene scene = new NaiveScene(camera);

      // floor orange plane
      Point planeOrigin = new Point(0, -250, 0);
      Normal planeNormal = new Normal(0, 1, 0);

      Material material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 240, 185));
      material._specular = 1 - .5;
      material._reflectivity = .5;

      ImplicitPlane plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addShape(plane);

      // spheres

      for (int i = 0; i < 9; i++) {
         material = new Material();
         material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(0, 131, 255));
         material._specular = 1 - .5;
         material._reflectivity = .5;

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
      settings.ZoomFactor = 1.0/8.0;
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
      material._specular = 1 - .75;
      material._reflectivity = .25;
      material._intrinsic = 1 - (material._reflectivity + material._specular + material._transparency);

      material.BRDF = brdf;

      double frontZ = -150;

      Point p0 = new Point(-100, 0, frontZ - 1);
      Point p1 = new Point(500, 600, frontZ);

      Box box = new Box(p0, p1, material);
      scene.addShape(box);

      int total = 320;

      int sphereXInterval = 10;
      int maxSmallSpheresX = total / sphereXInterval;

      int sphereYInterval = 10;
      int maxSmallSpheresY = total / sphereYInterval;

      double radius = sphereXInterval / 3;

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
            material._reflectivity = .3;
            material._transparency = 0;
            material._specular = .1;
            material._intrinsic = 1 - (material._reflectivity + material._specular + material._transparency);
            material.BRDF = brdf;

            Sphere sphere = new Sphere(material);

            double originX = sphereXInterval * i + (j % 5) * 3 + 50;
            double originY = sphereYInterval * j + (yOffset[i * maxSmallSpheresY + j]) + 150;
            double originZ = frontZ + radius;

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



      AbstractLight light = new SphereLight(lightSPD, sphere);

      scene.Shapes.add(light);
      scene.addLight(light);

      //scene.addLight(new PointLight(new Point(300, 300, 800), 30));

      //scene.addLight(new PointLight(new Point(x / 20, y / 2, -100), 5.7));
      //scene.addLight(new PointLight(new Point(19 * x / 20, y / 2, -100), 5.7));
      //scene.addLight(new PointLight(new Point(x / 2, 3 * y / 4, -100), 5.7));

      //scene.addLight(new PointLight(new Point(x / 2, y / 2, 300), 5.9));
      //scene.addLight(new PointLight(new Point(x / 2, y / 2, 600), 10.0));



      return scene;
   }


   public static AbstractScene CornellBox(int x, int y) {
      CameraSettings settings = new CameraSettings();
      settings.X = x;
      settings.Y = y;
      settings.FocalLength = 150;
      settings.Rotation = 0;
      settings.ZoomFactor = 1.0;
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
      material._specular = 1 - .75;
      material._reflectivity = .25;
      material._intrinsic = 1 - (material._reflectivity + material._specular + material._transparency);

      material.BRDF = brdf;

      Point p0 = new Point(-1000, -1000, -1000);
      Point p1 = new Point(1000, 1000, 1000);

      Box box = new Box(p0, p1, material);
      scene.addShape(box);

      // right light

      SpectralPowerDistribution lightSPD = new SpectralPowerDistribution(2000.0f, 2000.0f, 2000.0f);

      Sphere sphere = new Sphere();
      sphere.Origin = new Point(0, 250, -250);
      sphere.Radius = 50;



      AbstractLight light = new SphereLight(lightSPD, sphere);

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

         double originX = Math.random() * 600;
         double originY = Math.random() * 600;
         double originZ = Math.random() * 10 + 20;

         material = new Material();


         material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(0, 131, 255));
         material._reflectivity = .2;
         material._transparency = 0;
         material._specular = 1 - .8;
         material.BRDF = brdf;
         //material.setIndexOfRefraction(1.1);

         Sphere sphere = new Sphere(material);
         sphere.Origin = new Point(originX, originY, originZ);
         sphere.Radius = Math.random() * 50;

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

               int red = (int) (((double)i / (double)maxSmallSpheresX) * (255 - constant) + constant);
               int green = (int) (((double)j / (double)maxSmallSpheresY) * (255 - constant) + constant);
               int blue = (int) (Math.random() * (255 - constant) + constant);

               Color color = new Color(red, green, blue);
               /*
               Color color = null;
               double chance = Math.random();

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
            material._reflectivity = .3;
            material._transparency = 0;
            material._specular = 1 - .7;
            material.BRDF = brdf;

               Sphere sphere = new Sphere(material);

               double originX = sphereXInterval * i + Math.random() * 5 - 10;
               double originY = sphereYInterval * j + Math.random() * 5 - 10;
               double originZ = /*sphereZInterval * k + */ Math.random() * 100 + 60;

               sphere.Origin = new Point(originX, originY, originZ);
               sphere.Radius = Math.random() * 5 + 5;

               scene.addShape(sphere);
            //}
         }
      }

      Sphere sphere = new Sphere();
      sphere.Origin = new Point(300, 300, 3000);
      sphere.Radius = 70;



      SpectralPowerDistribution lightSPD = new SpectralPowerDistribution(100000.0f, 100000.0f, 100000.0f);

      AbstractLight light = new SphereLight(lightSPD, sphere);

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
      settings.ZoomFactor = 1 / 2.0;
      settings.FocusDistance = 500;
      settings.Aperture = new SquareAperture(5);

      Point origin = new Point(100, 200, 600);
      Vector direction = new Vector(-.1, -.3, -1);
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
      material._specular = 1 - .75;
      material._reflectivity = .25;

      Point b0 = new Point(-10000, -10000, -1);
      Point b1 = new Point(10000, 10000, 0);

      Box box = new Box(b0, b1, material);
      scene.addShape(box);

      //BoundingBox box = new BoundingBox(planeOrigin, planeNormal, material);
      //scene.addShape(plane);

      double offset = 1;

      double increment = 2;

      for (int i = 10; i <= 110; i += 50) {
         //for (int j = 10; j <= 110; j+= 50) {
            for (int k = 10; k <= 100; k+= 50) {
               double originX = i + offset;
               double originY = i - offset;
               double originZ = k + 15;

               material = new Material();

               material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(0, 131, 255));
               material._reflectivity = .2;
               material._transparency = 0;
               material._specular = 1 - .8;
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
      sphere.Origin = new Point(2 * x / 5, y / 3, 400.0);
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

     // scene.addLight(new PointLight(new Point(x / 3, y / 2, 900), 20.0));
     // scene.addLight(new PointLight(new Point(2 * x / 3, 2 * y / 3, 900), 20.0));
   //   return scene;
 //  }
//*/
   public static AbstractScene FourReflectiveSphereWithLights(int x, int y) {

      CameraSettings settings = new CameraSettings();
      settings.X = x;
      settings.Y = y;
      settings.FocalLength = 1200;
      settings.Rotation = 0;
      settings.ZoomFactor = 1 / 1.5;
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
      sphere.Origin = new Point(-900, 1150, 600.0);
      sphere.Radius = 150;

      scene.addShape(sphere);*/



      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(70, 137, 102));
      material._specular = 1 - .2;
      material._reflectivity = 0;
      material._transparency = .8;
      material._indexOfRefraction = 1.2;

      sphere = new Sphere(material);
      sphere.Origin = new Point(-200, 700, 500.0);
      sphere.Radius = 150;

      //scene.addShape(sphere);

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 240, 185));
      material._specular = 1 - .5;
      material._reflectivity = .5;
      material._transparency = 0;
      material._indexOfRefraction = 1.0;

      sphere = new Sphere(material);
      sphere.Origin = new Point(500, 100, 600.0);
      sphere.Radius = 150;

      //scene.addShape(sphere);

      p0 = new Point(-100, -100, -100);
      p1 = new Point(100, 100, 100);
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(30, 120, 120));
      material._reflectivity = .4;
      material._specular = .3;
      material._specular = 1 - .4;

      ArrayList<Transform> list = new ArrayList<>();
      list.add(Transform.Translate(new Vector(-200, 100, 1000)));
      //list.add(Transform.RotateZ(45));
      //list.add(Transform.RotateY(90));
      //list.add(Transform.RotateX(60));
      list.add(Transform.Scale(1.0, 5.0, 1.0));

      Transform[] transforms = GetCompositeTransforms(list);

      Transform objectToWorld = transforms[0];
      Transform worldToObject = transforms[1];

      box = new Box(p0, p1, material, objectToWorld, worldToObject);
      scene.addShape(box);



      for (int i = 0; i < 1; i++) {
         p0 = new Point(-100, -100, -100);
         p1 = new Point(100, 100, 100);

         material = new Material();
         material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(0, i * 10, 220));
         material._reflectivity = .4;
         material._specular = .3;
         material._specular = 1 - .4;

         list = new ArrayList<>();

         //

         list.add(Transform.Translate(new Vector(Math.sin(i) * i * 120 - 400, -(0 - 20 * i), 700)));
         //list.add(Transform.RotateZ(45));
         list.add(Transform.RotateY(i * 5));
         //list.add(Transform.RotateX(60));
         list.add(Transform.Scale(0.3, (i * .2) + .1, 0.3));

         transforms = GetCompositeTransforms(list);

         objectToWorld = transforms[0];
         worldToObject = transforms[1];

         box = new Box(p0, p1, material, objectToWorld, worldToObject);
         scene.addShape(box);
      }



      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 176, 59));
      material._reflectivity = .01;
      material._specular = 1 - .99;

      sphere = new Sphere(material);
      sphere.Origin = new Point(-200, 100, 500);
      sphere.Radius = 150;

      scene.addShape(sphere);

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(182, 73, 38));
      material._reflectivity = .2;
      //material.setSpecular(.3);
      material._specular = 1 - .8;

      list = new ArrayList<>();
      list.add(Transform.Translate(new Vector(500, 200, 900)));
      list.add(Transform.RotateZ(45));
      list.add(Transform.RotateY(45));
      list.add(Transform.RotateX(60));
      list.add(Transform.Scale(2.0, 1.0, 1.0));

      transforms = GetCompositeTransforms(list);

      objectToWorld = transforms[0];
      worldToObject = transforms[1];

      sphere = new Sphere(150, worldToObject, objectToWorld, material);
      sphere.Origin = new Point(0, 0, 0);

      scene.addShape(sphere);

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(128, 38, 163));
      material._reflectivity = .2;
      //material.setSpecular(.3);
      material._specular = 1 - .8;

      list = new ArrayList<>();
      list.add(Transform.Translate(new Vector(400, 50, 400)));
      list.add(Transform.RotateY(70));
      list.add(Transform.RotateZ(90));

      list.add(Transform.Scale(50.0, 200.0, 50.0));

      transforms = GetCompositeTransforms(list);

      objectToWorld = transforms[0];
      worldToObject = transforms[1];

      Cylinder cylinder = new Cylinder(1, 1, worldToObject, objectToWorld, material);

      scene.addShape(cylinder);

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(142, 40, 0));
      material._reflectivity = .95;
      material._specular = 1 - .05;

      sphere = new Sphere(material);
      sphere.Origin = new Point(160, 300, 300.0);
      sphere.Radius = 25;

      //scene.addShape(sphere);

      // bottom horizontal orange plane


      p0 = new Point(-10000, -1, -10000);
      p1 = new Point(10000, 0, 10000);

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 240, 185));
      material._specular = 1 - .95;
      material._reflectivity = .05;

      box = new Box(p0, p1, material);

      scene.addShape(box);

      // top horizontal orange plane


      p0 = new Point(-10000, 1001, -10000);
      p1 = new Point(10000, 1000, 10000);

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 131, 0));
      material._specular = 1 - .5;
      material._reflectivity = .5;

      box = new Box(p0, p1, material);

      //scene.addShape(box);

      // forward white vertical z plane

      p0 = new Point(-10000, -10000, -400);
      p1 = new Point(10000, 10000, -401);

      Point planeOrigin = new Point(0, 0, -400);
      Normal planeNormal = new Normal(0, 0, 1);

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 240, 185));
      material._specular = 1 - .75;
      material._reflectivity = .25;

      box = new Box(p0, p1, material);
      scene.addShape(box);

      // back white vertical z plane

      p0 = new Point(-10000, -10000, 5000);
      p1 = new Point(10000, 10000, 5001);

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 240, 185));
      material._specular = 1 - .75;
      material._reflectivity = .25;

      box = new Box(p0, p1, material);
      //scene.addShape(box);

      // left blue vertical x plane

      p0 = new Point(-1000, -10000, -10000);
      p1 = new Point(-1001, 11000, 10000);

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(0, 131, 255));
      material._specular = 1 - .75;
      material._reflectivity = .25;

      box = new Box(p0, p1, material);
      //scene.addShape(box);

      // right blue vertical x plane

      p0 = new Point(1000, -10000, -10000);
      p1 = new Point(1001, 11000, 10000);


      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(0, 131, 255));
      material._specular = 1 - .75;
      material._reflectivity = .25;

      box = new Box(p0, p1, material);
      //scene.addShape(box);

      SpectralPowerDistribution spd = new SpectralPowerDistribution(1.0f, 1.0f, 1.0f);

      sphere = new Sphere();
      sphere.Origin = new Point(1000, 1000, 500);
      sphere.Radius = 50;

      SphereLight sphereLight = new SphereLight(spd, sphere);

      PointLight pointLight = new PointLight(spd, new Point(1000, 1000, 500));
      scene.addLight(pointLight);


      //scene.addLight(new PointLight(new Point(300, 500, 700), 15.0));
      //scene.addLight(new PointLight(new Point(400, 1000, 1300), 40.0));
      //scene.addLight(new PointLight(new Point(300, 300, 300), 5.0));
      //scene.addLight(new PointLight(new Point(300, 300, 1500), 10.0));
      //scene.addLight(new PointLight(new Point(685, 360, -350), 5.0));
      //scene.addLight(new PointLight(new Point(575, 180, -200), 5.0));
      return scene;
   }

   /**
    * Returns the composite Object-To-World and World-To-Object transforms of the list of transforms.
    * @param list
    * @return
    */
   private static Transform[] GetCompositeTransforms(java.util.List<Transform> list) {
      Transform objectToWorld = new Transform();

      for (int i = 0; i < list.size(); i++) {
         objectToWorld = objectToWorld.Apply(list.get(i));
      }

      Transform worldToObject = new Transform();

      for (int i = list.size() - 1; i >= 0; i--) {
         worldToObject = worldToObject.Apply(list.get(i));
      }

      worldToObject = worldToObject.Invert();

      return new Transform[] {objectToWorld, worldToObject};

   }


   public static AbstractScene FourReflectiveSphereWithLightsPointable(int x, int y) {

      CameraSettings settings = new CameraSettings();
      settings.X = x;
      settings.Y = y;
      settings.FocalLength = 1200;
      settings.Rotation = 0;
      settings.ZoomFactor = 1 / 2.0;
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
      scene.addLight(new PointLight(new Point(160, 360, 600), 5.0));
      scene.addLight(new PointLight(new Point(480, 360, 600), 5.0));
      scene.addLight(new PointLight(new Point(300, 300, 300), 5.0));
      scene.addLight(new PointLight(new Point(300, 300, 1500), 10.0));
      //scene.addLight(new PointLight(new Point(685, 360, -350), 5.0));
      scene.addLight(new PointLight(new Point(575, 180, -200), 5.0));
*/


      // horizontal orange plane

      Point planeOrigin = new Point(0, 0, 0);
      Normal planeNormal = new Normal(0, 1, 0);

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 131, 0));
      material._specular = 1 - .5;
      material._reflectivity = .5;

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
      material._specular = 1 - .9;
      material._reflectivity = .1;

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
      material._specular = 1 - .5;
      material._reflectivity = .5;
      material._transparency = 0;
      material._indexOfRefraction = 1.2;

      Sphere sphere = new Sphere(material);
      sphere.Origin = new Point(-200, 350, -100.0);
      sphere.Radius = 150;

      scene.addShape(sphere);

      // green sphere

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(70, 137, 102));
      material._specular = 1 - .5;
      material._reflectivity = .5;
      material._transparency = 0;
      material._indexOfRefraction = 1.2;

      sphere = new Sphere(material);
      sphere.Origin = new Point(500, 350, 500.0);
      sphere.Radius = 150;

      scene.addShape(sphere);

      // white sphere

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 240, 185));
      material._specular = 1 - .1;
      material._reflectivity = .9;
      material._transparency = 0;
      material._indexOfRefraction = 1.3;

      sphere = new Sphere(material);
      sphere.Origin = new Point(500, 100, 600.0);
      sphere.Radius = 150;

      scene.addShape(sphere);

      // big dark orange sphere

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(182, 73, 38));
      material._reflectivity = .5;
      material._specular = 1 - .5;

      sphere = new Sphere(material);
      sphere.Origin = new Point(800, 350, 1000.0);
      sphere.Radius = 150;

      scene.addShape(sphere);

      //

      // orange sphere

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 176, 59));
      material._reflectivity = .5;
      material._specular = 1 - .5;

      sphere = new Sphere(material);
      sphere.Origin = new Point(-200, 100, 1000);
      sphere.Radius = 150;

      scene.addShape(sphere);

      // tiny red sphere

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(142, 40, 0));
      material._reflectivity = .5;
      material._specular = 1 - .5;

      sphere = new Sphere(material);
      sphere.Origin = new Point(160, 300, 300.0);
      sphere.Radius = 25;

      scene.addShape(sphere);

      // tiny blue sphere

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(62, 96, 111));
      material._reflectivity = .6;
      material._specular = 1 - .4;

      sphere = new Sphere(material);
      sphere.Origin = new Point(250, 300, 300.0);
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
      settings.ZoomFactor = 0.5;

      double zoomFactor = 1.0/2.0;
      double focusDistance = 1487.0;



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

      //scene.addLight(new PointLight(new Point(160, 360, 600), 5.0));
      //scene.addLight(new PointLight(new Point(480, 360, 600), 5.0));
      //scene.addLight(new PointLight(new Point(300, 300, 300), 5.0));
      //scene.addLight(new PointLight(new Point(300, 300, 1500), 10.0));
      //scene.addLight(new PointLight(new Point(685, 360, -350), 5.0));
      //scene.addLight(new PointLight(new Point(575, 180, -200), 5.0));

      Material material = new Material();

      // horizontal orange plane

      Point planeOrigin = new Point(0, 0, 0);
      Normal planeNormal = new Normal(0, 1, 0);

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 131, 0));
      material._specular = 1;
      material._reflectivity = .1;

      ImplicitPlane plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addShape(plane);

      // white vertical z plane

      planeOrigin = new Point(0, 0, -400);
      planeNormal = new Normal(0, 0, 1);

      

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 240, 185));
      material._specular = 1;
      material._reflectivity = .25;

      plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addShape(plane);

      // white vertical x plane

      planeOrigin = new Point(800, 0, 0);
      planeNormal = new Normal(-1, 0, 0);

      

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(255, 240, 185));
      material._specular = 1;
      material._reflectivity = .25;

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
      material._specular = 1 - .5;
      material._reflectivity = .5;
      material._transparency = 0;
      material._indexOfRefraction = 1.2;

      Sphere sphere = new Sphere(material);
      sphere.Origin = new Point(100, 350, -100.0);
      sphere.Radius = 150;

      scene.addShape(sphere);

      // right green sphere

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(70, 137, 102));
      material._specular = 1 - .5;
      material._reflectivity = .5;
      material._transparency = 0;
      material._indexOfRefraction = 1.2;

      sphere = new Sphere(material);
      sphere.Origin = new Point(500, 350, -100.0);
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
      material._specular = 1 - .5;
      material._reflectivity = .5;
      material._transparency = 0;
      material._indexOfRefraction = 1.2;

      Sphere sphere = new Sphere(material);
      sphere.Origin = new Point(-500, 300, 0.0);
      sphere.Radius = 150;

      scene.addShape(sphere);

      // right green sphere

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(70, 137, 102));
      material._specular = 1 - .5;
      material._reflectivity = .5;
      material._transparency = 0;
      material._indexOfRefraction = 1.2;

      sphere = new Sphere(material);
      sphere.Origin = new Point(500, 500, 0.0);
      sphere.Radius = 150;

      scene.addShape(sphere);

      // middle

      material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(new Color(70, 137, 102));
      material._specular = 1 - .5;
      material._reflectivity = .5;
      material._transparency = 0;
      material._indexOfRefraction = 1.2;

      sphere = new Sphere(material);
      sphere.Origin = new Point(0, 150, 0.0);
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
      settings.ZoomFactor =  1.5;
      settings.FocusDistance = 500;
      settings.Aperture = new CircleAperture(20);

      Point origin = new Point(0, 0, 4000);
      Vector direction = new Vector(0, 0, -1);
      settings.Orientation = new Ray(origin, direction);

      Camera camera = new SimplePointableCamera(settings);

      AbstractScene scene = new NaiveScene(camera);

      Material material = new Material();
      material.ReflectanceSpectrum = new ReflectanceSpectrum(Color.blue);
      material._reflectivity = .33;
      //material.setTransparency(.33);
      //material.setIndexOfRefraction(1.333);

      Sphere sphere = new Sphere(material);
      sphere.Origin = new Point(2 * x / 5, y / 3, 500.0);
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

      //scene.addLight(new PointLight(new Point(x / 2, y / 2, 500), 25.0));

      return scene;
   }
}