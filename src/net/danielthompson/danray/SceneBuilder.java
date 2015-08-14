package net.danielthompson.danray;

import net.danielthompson.danray.acceleration.KDScene;
import net.danielthompson.danray.animation.CameraOrientationMovement;
import net.danielthompson.danray.cameras.*;
import net.danielthompson.danray.cameras.apertures.Aperture;
import net.danielthompson.danray.cameras.apertures.CircleAperture;
import net.danielthompson.danray.cameras.apertures.SquareAperture;
import net.danielthompson.danray.imports.WavefrontObjectImporter;
import net.danielthompson.danray.lights.PointLight;
import net.danielthompson.danray.lights.Radiatable;
import net.danielthompson.danray.lights.SpectralSphereLight;
import net.danielthompson.danray.lights.SphereLight;
import net.danielthompson.danray.shading.*;
import net.danielthompson.danray.shapes.*;
import net.danielthompson.danray.structures.*;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Vector;

import java.awt.*;
import java.io.File;
import java.util.*;

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

   public static KDScene OneSphere(int x, int y) {

      KDScene scene = new KDScene(null);

      Material material = new Material();

      material.setColor(Color.blue);

      Sphere sphere = new Sphere(material);
      sphere.Origin = new Point(x / 2, y / 2, -300.0);
      sphere.Radius = 100;

      scene.addDrawableObject(sphere);

      Point lightOrigin = new Point(x / 2, y / 2, 100);
      double lumens = 1.0;

      Radiatable light = new PointLight(lightOrigin, lumens);
      scene.addRadiatableObject(light);

      return scene;
   }

   public static Scene SpectralLemon(int x, int y) {

      CameraSettings settings = new CameraSettings();
      settings.X = x;
      settings.Y = y;
      settings.FocalLength = 1200;
      settings.Rotation = 0;
      settings.ZoomFactor =  1.5;
      settings.FocusDistance = 500;
      settings.Aperture = new CircleAperture(20);

      Point origin = new Point(0, 0, 4000);
      Vector direction = new Vector(.25, -.25, -1);
      settings.Orientation = new Ray(origin, direction);

      Camera camera = new SimplePointableCamera(settings);

      Scene scene = new NaiveScene(camera);

      SpectralBlender.setFilmSpeed(.1f);

      // right light

      RelativeSpectralPowerDistribution lightRSPD = null;

      lightRSPD = RelativeSpectralPowerDistributionLibrary.D65;
      /*
      new RelativeSpectralPowerDistribution(RelativeSpectralPowerDistributionLibrary.Blue);
      lightRSPD.Merge(RelativeSpectralPowerDistributionLibrary.Red);
      lightRSPD.Merge(RelativeSpectralPowerDistributionLibrary.Yellow);
      */
      SpectralPowerDistribution lightSPD = new SpectralPowerDistribution(lightRSPD, 50f);

      Material material = new Material();


      SpectralSphereLight light = new SpectralSphereLight(10, null, lightSPD);
      light.Origin = new Point(0, 15000, 15000);
      light.Radius = 1000;

      light.ID = getNextID();

      scene._drawables.add(light);
      scene.SpectralRadiatables.add(light);

      // left light

      lightSPD = new SpectralPowerDistribution(RelativeSpectralPowerDistributionLibrary.Sunset, 5f);

      light = new SpectralSphereLight(10, null, lightSPD);
      light.Origin = new Point(-800, 0, 500);
      light.Radius = 100;

      scene.SpectralRadiatables.add(light);
      scene._drawables.add(light);

      // left wall

      Material boxMaterial = new Material();
      boxMaterial.SpectralReflectanceCurve = SpectralReflectanceCurveLibrary.LemonSkin;

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
      scene._drawables.add(box);

      // right wall

      boxMaterial = new Material();
      boxMaterial.setReflectivity(.5);
      boxMaterial.SpectralReflectanceCurve = SpectralReflectanceCurveLibrary.LemonSkin;

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
      scene._drawables.add(box);

      // front wall

      boxMaterial = new Material();
      boxMaterial.SpectralReflectanceCurve = SpectralReflectanceCurveLibrary.Grass;

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
      scene._drawables.add(box);

      // back wall

      boxMaterial = new Material();
      boxMaterial.SpectralReflectanceCurve = SpectralReflectanceCurveLibrary.Grass;

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
      //scene._drawables.add(box);

      // floor

      boxMaterial = new Material();
      boxMaterial.SpectralReflectanceCurve = SpectralReflectanceCurveLibrary.Grass;

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

      scene._drawables.add(box);

      // ceiling

      boxMaterial = new Material();
      boxMaterial.SpectralReflectanceCurve = SpectralReflectanceCurveLibrary.Grass;

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
      //scene._drawables.add(box);

      // top left little box

      boxMaterial = new Material();
      boxMaterial.SpectralReflectanceCurve = SpectralReflectanceCurveLibrary.Orange;

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
            //scene._drawables.add(box);

         //}
      }


      // bottom right little box

      boxMaterial = new Material();
      boxMaterial.SpectralReflectanceCurve = SpectralReflectanceCurveLibrary.LightBlue;

      p0 = new Point(800, -1000, 0);
      p1 = new Point(1300, -500, 500);
      box = new Box(p0, p1, boxMaterial);
      box.ID = getNextID();
      scene._drawables.add(box);


      material = new Material();

      material.SpectralReflectanceCurve = SpectralReflectanceCurveLibrary.Grass;

      list = new ArrayList<>();
      list.add(Transform.Translate(new Vector(-400, 50, 1000)));
      list.add(Transform.RotateY(90));
      list.add(Transform.RotateZ(90));

      list.add(Transform.Scale(50.0, 1000.0, 50.0));

      transforms = GetCompositeTransforms(list);

      objectToWorld = transforms[0];
      worldToObject = transforms[1];

      Cylinder cylinder = new Cylinder(1, 1, worldToObject, objectToWorld, material);

      //scene.addDrawableObject(cylinder);





      return scene;
   }

   public static KDScene Default() {

      KDScene scene = new KDScene(null);

      Material material = new Material();

      material.setColor(Color.blue);

      Sphere sphere1 = new Sphere(material);
      sphere1.Origin = new Point(50.0, 50.0, 40.0);
      sphere1.Radius = 10;

      scene.addDrawableObject(sphere1);

      material = new Material();
      material.setColor(new Color(255, 255, 128));

      Sphere sphere2 = new Sphere(material);
      sphere2.Origin = new Point(0.0, 0.0, 20.0);
      sphere2.Radius = 55;

      scene.addDrawableObject(sphere2);

      material = new Material();
      material.setColor(Color.green);

      Sphere sphere3 = new Sphere(material);
      sphere3.Origin = new Point(200.0, 200.0, 25.0);
      sphere3.Radius = 40;

      scene.addDrawableObject(sphere3);

      return scene;
   }

   public static Scene DepthOfFieldTest(int x, int y) {

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

      Scene scene = new NaiveScene(camera);

      // floor orange plane
      Point planeOrigin = new Point(0, -250, 0);
      Normal planeNormal = new Normal(0, 1, 0);

      Material material = new Material();
      material.setColor(new Color(255, 240, 185));
      material.setDiffuse(.5);
      material.setReflectivity(.5);

      ImplicitPlane plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addDrawableObject(plane);

      // spheres

      for (int i = 0; i < 9; i++) {
         material = new Material();
         material.setColor(new Color(0, 131, 255));
         material.setDiffuse(.5);
         material.setReflectivity(.5);

         Sphere sphere = new Sphere(material);
         int coord = -850 + (175 * i);
         sphere.Origin = new Point(coord, 0, coord);
         sphere.Radius = 100;
         scene.addDrawableObject(sphere);

         //scene.addRadiatableObject(new PointLight(new Point(-450 + (300 * i), 2000, -450 + (300 * i)), 25));
      }

      scene.addRadiatableObject(new PointLight(new Point(-650, 600, 925), 40));

      for (int i = 0; i < 1; i++) {
         scene.addRadiatableObject(new PointLight(new Point(0, 3000, -175 + (i * 300)), 200));
      }
      return scene;
   }

   public static Scene PlaneAndBox(int x, int y) {

      CameraSettings settings = new CameraSettings();
      settings.X = x;
      settings.Y = y;
      settings.FocalLength = 600;
      settings.ZoomFactor = 3 / 4.0;
      settings.FocusDistance = 100;

      Point origin = new Point(200, 500, 400);
      Vector direction = new Vector(.2, .2, -1);
      settings.Orientation = new Ray(origin, direction);

      settings.Aperture = new SquareAperture(20);

      Camera camera = new DepthOfFieldCamera(settings);

      Scene scene = new KDScene(camera);

      scene.addRadiatableObject(new PointLight(new Point(400, 400, 600), 15.7));
      scene.addRadiatableObject(new PointLight(new Point(300, 300, 600), 15.7));

      Material material = new Material();


      // horizontal orange plane
      Point planeOrigin = new Point(0, 1000, 0);
      Normal planeNormal = new Normal(0, 1, 0);

      material = new Material();
      material.setColor(new Color(182, 73, 38));
      material.setDiffuse(.8);
      material.setReflectivity(.2);

      ImplicitPlane plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addDrawableObject(plane);

      // horizontal orange plane
      planeOrigin = new Point(0, 0, 0);
      planeNormal = new Normal(0, 1, 0);


      material = new Material();
      material.setColor(new Color(255, 131, 0));
      material.setDiffuse(.8);
      material.setReflectivity(.2);

      plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addDrawableObject(plane);

      // white vertical z plane

      planeOrigin = new Point(0, 0, -400);
      planeNormal = new Normal(0, 0, 1);
      
      material = new Material();
      material.setColor(new Color(255, 240, 185));
      material.setDiffuse(.5);
      material.setReflectivity(.5);

      plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addDrawableObject(plane);

      // white vertical z plane

      planeOrigin = new Point(0, 0, 1000);
      planeNormal = new Normal(0, 0, 1);

      

      material = new Material();
      material.setColor(new Color(255, 240, 185));
      material.setDiffuse(.5);
      material.setReflectivity(.5);

      plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      //scene.addDrawableObject(plane);

      // white vertical x plane

      planeOrigin = new Point(800, 0, 0);
      planeNormal = new Normal(-1, 0, 0);

      

      material = new Material();
      material.setColor(new Color(255, 240, 185));
      material.setDiffuse(.5);
      material.setReflectivity(.5);

      plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addDrawableObject(plane);

      // blue vertical x plane

      planeOrigin = new Point(-200, 0, 0);
      planeNormal = new Normal(1, 0, 0);

      

      material = new Material();
      material.setColor(new Color(0, 131, 255));
      material.setDiffuse(.8);
      material.setReflectivity(.2);

      plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addDrawableObject(plane);

      // box

      material = new Material();
      material.setColor(new Color(131, 131, 255));
      material.setDiffuse(.8);
      material.setReflectivity(.2);

      Box box = new Box(new Point(250, 550, 250), new Point(350, 650, 350), material);
      scene.addDrawableObject(box);

      return scene;
   }

   public static Scene SomeRegularSpheres(int x, int y) {

      CameraSettings settings = new CameraSettings();
      settings.X = x;
      settings.Y = y;
      settings.FocalLength = 200;
      settings.Rotation = 0;
      settings.ZoomFactor = 1 / 30.0;
      settings.FocusDistance = 250;
      settings.Aperture = new SquareAperture(5);

      Point origin = new Point(5, 5, 60);
      Vector direction = new Vector(0, 0, -1);
      settings.Orientation = new Ray(origin, direction);

      Camera camera = null;

      if (Main.UseDepthOfField) {
         camera = new DepthOfFieldCamera(settings);
      }

      else {
         camera = new IsometricCamera(settings);
         //camera = new SimplePointableCamera(settings);
      }

      Scene scene = new KDScene(camera);

      scene.numFrames = 1;

      // white vertical z plane

      Point planeOrigin = new Point(0, 0, -10);
      Normal planeNormal = new Normal(0, 0, 1);

      Material material = new Material();
      material.setColor(new Color(255, 240, 185));
      material.setDiffuse(.75);
      material.setReflectivity(.25);

      ImplicitPlane plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addDrawableObject(plane);


      java.util.List<Point> origins = new ArrayList<Point>();
      origins.add(new Point(1, 1, 0));
      origins.add(new Point(3, 5, 0));
      origins.add(new Point(5, 3, 0));
      origins.add(new Point(7, 7, 0));
      origins.add(new Point(9, 9, 0));
      origins.add(new Point(11, 11, 0));
      origins.add(new Point(13, 13, 0));
      origins.add(new Point(15, 15, 0));
      origins.add(new Point(17, 17, 0));
      origins.add(new Point(19, 19, 0));

      for (int i = 0; i < origins.size(); i++) {
         int red = (i % 3) * 64 + 48;
         int green = 232;
         int blue = (i % 3) * 64 + 48;

         Color color = new Color(red, green, blue);

         material = new Material();
         material.setColor(color);
         material.setReflectivity(.3);
         material.setTransparency(0);
         material.setDiffuse(.7);

         Sphere sphere = new Sphere(material);

         sphere.Origin = origins.get(i);
         sphere.Radius = 1;
         scene.addDrawableObject(sphere);
      }

      SphereLight sphereLight = new SphereLight(25, material);

      //PointLight pointLight = new PointLight(location, 50);

      sphereLight.Origin = new Point(300, 300, 300);
      sphereLight.Radius = 20;

      scene.addRadiatableObject(sphereLight);
      //scene.addDrawableObject(sphereLight);

      //scene.addRadiatableObject(new PointLight(new Point(20, 20, 200), 1.7));

      return scene;
   }

   public static Scene ManyRegularSpheres(int x, int y) {
      CameraSettings settings = new CameraSettings();
      settings.X = x;
      settings.Y = y;
      settings.FocalLength = 200;
      settings.Rotation = 0;
      settings.ZoomFactor = 1 / 2.;
      settings.FocusDistance = 250;
      settings.Aperture = new SquareAperture(5);

      Point origin = new Point(200, 300, 200);
      Vector direction = new Vector(0, 0, -1);
      settings.Orientation = new Ray(origin, direction);


      Camera camera = null;

      if (Main.UseDepthOfField) {
         camera = new DepthOfFieldCamera(settings);
      }

      else {
         camera = new IsometricCamera(settings);
         //camera = new SimplePointableCamera(settings);
      }

      Scene scene = new KDScene(camera);

      scene.numFrames = 1;

      // white vertical z plane

      Point planeOrigin = new Point(0, 0, 0);
      Normal planeNormal = new Normal(0, 0, 1);

      Material material = new Material();
      material.setColor(new Color(255, 240, 185));
      material.setDiffuse(.75);
      material.setReflectivity(.25);

      ImplicitPlane plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addDrawableObject(plane);

      /*
      for (int i = 0; i < 600; i += 3) {

         double originX = i;
         double originY = i;
         double originZ = 20;

         material = new Material();


         material.setColor(new Color(0, 131, 255));
         material.setReflectivity(.2);
         material.setTransparency(0);
         material.setDiffuse(.8);
         //material.setIndexOfRefraction(1.1);

         Sphere sphere = new Sphere(material);
         sphere.Origin = new Point(originX, originY, originZ);
         sphere.Radius = originZ;
         scene.addDrawableObject(sphere);
      }*/

      int maxSmallSpheresX = 8;
      int sphereXInterval = 40;

      int maxSmallSpheresY = 8;
      int sphereYInterval = 40;

      int[] yOffset = new int[maxSmallSpheresX * maxSmallSpheresY];
      for (int i = 0; i < yOffset.length; i++) {
         yOffset[i] = i * 0;
      }

      for (int i = 0; i < maxSmallSpheresX; i++) {
         for (int j = 0; j < maxSmallSpheresY; j++) {

            if (i == 0 || i == 1) {
               if (j == 0 || j == 1 || j == 2) {
                  continue;
               }
            }

            int red = (i % 3) * 64 + 48;
            int green = ((i + j) % 4) * 48 + 48;
            int blue = (j % 3) * 64 + 48;

            Color color = new Color(red, green, blue);

            material = new Material();
            material.setColor(color);
            material.setReflectivity(.3);
            material.setTransparency(0);
            material.setDiffuse(.7);

            Sphere sphere = new Sphere(material);

            double originX = sphereXInterval * i + (j % 5) * 3 + 50;
            double originY = sphereYInterval * j + (yOffset[i * maxSmallSpheresY + j]) + 150;
            double originZ = 100;

            sphere.Origin = new Point(originX, originY, originZ);
            sphere.Radius = 10;
            scene.addDrawableObject(sphere);
         }
      }

      SphereLight sphereLight = new SphereLight(5, material);

      //PointLight pointLight = new PointLight(location, 50);


      sphereLight.Origin = new Point(300, 300, 300);
      sphereLight.Radius = 20;

      scene.addRadiatableObject(sphereLight);
      //scene.addDrawableObject(sphereLight);

      //scene.addRadiatableObject(new PointLight(new Point(300, 300, 300), 5.7));

      //scene.addRadiatableObject(new PointLight(new Point(x / 20, y / 2, -100), 5.7));
      //scene.addRadiatableObject(new PointLight(new Point(19 * x / 20, y / 2, -100), 5.7));
      //scene.addRadiatableObject(new PointLight(new Point(x / 2, 3 * y / 4, -100), 5.7));

      //scene.addRadiatableObject(new PointLight(new Point(x / 2, y / 2, 300), 5.9));
      //scene.addRadiatableObject(new PointLight(new Point(x / 2, y / 2, 600), 10.0));



      return scene;
   }


   public static Scene ManyRandomSpheres(int x, int y) {
      CameraSettings settings = new CameraSettings();
      settings.X = x;
      settings.Y = y;
      settings.FocalLength = 200;
      settings.Rotation = 0;
      settings.ZoomFactor = 1 / 5.0;
      settings.FocusDistance = 450;
      settings.Aperture = new SquareAperture(2);

      Point origin = new Point(300, 300, 200);
      Vector direction = new Vector(.45, 0, -1);
      settings.Orientation = new Ray(origin, direction);

      Camera camera = null;

      CameraOrientationMovement movement = new CameraOrientationMovement();
      movement.frame = 240;

      Point finalOrigin = new Point(800, 400, 700);
      Vector finalDirection = new Vector(-.50, 0, -1);
      Ray finalOrientation = new Ray(finalOrigin, finalDirection);

      movement.orientation = finalOrientation;
      settings.Movement = movement;

      if (Main.UseDepthOfField) {
         camera = new DepthOfFieldCamera(settings);

      }

      else {
         //camera = new IsometricCamera(x, y, rotation, zoomFactor, orientation);

         camera = new SimplePointableCamera(settings);
      }

      Scene scene = new KDScene(camera);

      scene.numFrames = 1;

      // white vertical z plane

      Point planeOrigin = new Point(0, 0, 0);
      Normal planeNormal = new Normal(0, 0, 1);

      Material material = new Material();
      material.setColor(new Color(255, 240, 185));
      material.setDiffuse(.75);
      material.setReflectivity(.25);

      ImplicitPlane plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addDrawableObject(plane);

      for (int i = 0; i < 40; i++) {

         double originX = Math.random() * 600;
         double originY = Math.random() * 600;
         double originZ = Math.random() * 10 + 20;

         material = new Material();


         material.setColor(new Color(0, 131, 255));
         material.setReflectivity(.2);
         material.setTransparency(0);
         material.setDiffuse(.8);
         //material.setIndexOfRefraction(1.1);

         Sphere sphere = new Sphere(material);
         sphere.Origin = new Point(originX, originY, originZ);
         sphere.Radius = Math.random() * 50;
         scene.addDrawableObject(sphere);
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
               material.setColor(color);
               material.setReflectivity(.3);
               material.setTransparency(0);
               material.setDiffuse(.7);

               Sphere sphere = new Sphere(material);

               double originX = sphereXInterval * i + Math.random() * 5 - 10;
               double originY = sphereYInterval * j + Math.random() * 5 - 10;
               double originZ = /*sphereZInterval * k + */ Math.random() * 100 + 60;

               sphere.Origin = new Point(originX, originY, originZ);
               sphere.Radius = Math.random() * 5 + 5;
               scene.addDrawableObject(sphere);
            //}
         }
      }

      SphereLight sphereLight = new SphereLight(50, material);

      //PointLight pointLight = new PointLight(location, 50);


      sphereLight.Origin = new Point(300, 300, 1000);
      sphereLight.Radius = 75;

      scene.addRadiatableObject(sphereLight);
      //scene.addRadiatableObject(new PointLight(new Point(300, 300, 1000), 50));

      scene.addRadiatableObject(new PointLight(new Point(300, 300, 10000), 250));

      //scene.addRadiatableObject(new PointLight(new Point(x / 20, y / 2, -100), 5.7));
      //scene.addRadiatableObject(new PointLight(new Point(19 * x / 20, y / 2, -100), 5.7));
      //scene.addRadiatableObject(new PointLight(new Point(x / 2, 3 * y / 4, -100), 5.7));

      //scene.addRadiatableObject(new PointLight(new Point(x / 2, y / 2, 300), 5.9));
      //scene.addRadiatableObject(new PointLight(new Point(x / 2, y / 2, 600), 10.0));

      //scene.BuildKDTree();

      return scene;
   }

   public static Scene SpheresInAnXPattern(int x, int y) {
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

      Scene scene = new KDScene(camera);

      // white vertical z plane

      Point planeOrigin = new Point(0, 0, 0);
      Normal planeNormal = new Normal(0, 0, 1);

      Material material = new Material();
      material.setColor(new Color(255, 240, 185));
      material.setDiffuse(.75);
      material.setReflectivity(.25);

      Point b0 = new Point(-10000, -10000, -1);
      Point b1 = new Point(10000, 10000, 0);

      Box box = new Box(b0, b1, material);
      scene.addDrawableObject(box);

      //Box box = new Box(planeOrigin, planeNormal, material);
      //scene.addDrawableObject(plane);

      double offset = 1;

      double increment = 2;

      for (int i = 10; i <= 110; i += 50) {
         //for (int j = 10; j <= 110; j+= 50) {
            for (int k = 10; k <= 100; k+= 50) {
               double originX = i + offset;
               double originY = i - offset;
               double originZ = k + 15;

               material = new Material();

               material.setColor(new Color(0, 131, 255));
               material.setReflectivity(.2);
               material.setTransparency(0);
               material.setDiffuse(.8);
               //material.setIndexOfRefraction(1.1);

               Sphere sphere = new Sphere(material);
               sphere.Origin = new Point(originX, originY, originZ);
               sphere.Radius = 20;
               scene.addDrawableObject(sphere);
               offset += increment;
            }

         //}
         offset += increment;
      }
/*
      int maxSmallSpheresX = 15;
      int sphereXInterval = 40;

      int maxSmallSpheresY = 15;
      int sphereYInterval = 40;

      for (int i = 0; i < maxSmallSpheresX; i++) {
         for (int j = 0; j < maxSmallSpheresY; j++) {


            int constant = 128;

            int red = (int)(Math.random() * (255 - constant) + constant);
            int green = (int)(Math.random() * (255 - constant) + constant);
            int blue = (int)(Math.random() * (255 - constant) + constant);

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
            /*material = new Material();
            material.setColor(color);
            material.setReflectivity(.3);
            material.setTransparency(0);
            material.setDiffuse(.7);

            Sphere sphere = new Sphere(material);

            double originX = sphereXInterval * i + Math.random() * 5 - 10;
            double originY = sphereYInterval * j + Math.random() * 5 - 10;
            double originZ = Math.random() * 5 + 60;

            sphere.Origin = new Point(originX, originY, originZ);
            sphere.Radius = Math.random() * 5 + 5;
            scene.addDrawableObject(sphere);
         }
      }*/

      scene.addRadiatableObject(new PointLight(new Point(300, 300, 900), 15));
      scene.addRadiatableObject(new PointLight(new Point(300, 900, 300), 15));
      scene.addRadiatableObject(new PointLight(new Point(900, 300, 300), 15));

      return scene;
   }

   public static KDScene TwoSpheresWithLights(int x, int y) {
      KDScene scene = new KDScene(null);

      Material material = new Material();
      material.setColor(Color.blue);
      material.setDiffuse(0);
      material.setReflectivity(0);

      Sphere sphere = new Sphere(material);
      sphere.Origin = new Point(x / 3, y / 2, 0.0);
      sphere.Radius = y / 4;

      scene.addDrawableObject(sphere);

      material = new Material();
      material.setColor(Color.red);
      material.setDiffuse(0);
      material.setReflectivity(0);

      sphere = new Sphere(material);
      sphere.Origin = new Point(2 * x / 3, y / 2, 0.0);
      sphere.Radius = y / 4;

      scene.addDrawableObject(sphere);

      //Point lightOrigin = new Point(x / 2, y / 2, 400);
      //double lumens = 1.0;

      //Radiatable light = new PointLight(lightOrigin, lumens);
      //scene.addRadiatableObject(light);

      //scene.addRadiatableObject(new PointLight(new Point(x / 3, y / 3, 300), 0.5));
      //scene.addRadiatableObject(new PointLight(new Point(x / 2, y / 4, -100), 0.7));
      //scene.addRadiatableObject(new PointLight(new Point(x / 20, y / 2, -100), 0.8));
      //scene.addRadiatableObject(new PointLight(new Point(19 * x / 20, y / 2, -100), 0.9));
      //scene.addRadiatableObject(new PointLight(new Point(x / 2, 3 * y / 4, -100), 0.8));

      scene.addRadiatableObject(new PointLight(new Point(x / 2, y / 2, 300), 1.0));
      return scene;


   }


   public static KDScene ReflectiveTriangleMeshWithLight(int x, int y) {
      KDScene scene = new KDScene(null);

      // teapot

      WavefrontObjectImporter importer = new WavefrontObjectImporter(new File("models/teapot.obj"));
      Material material = new Material();
      material.setColor(new Color(182, 73, 38));
      material.setDiffuse(.5);
      material.setReflectivity(.5);
      material.setTransparency(0);

      TriangleMesh mesh = importer.Process();
      mesh.SetMaterial(material);
      mesh.SetRotation(new Tuple(160, 20, 20));
      mesh.SetOrigin(new Point(2 * x / 3, y / 2, 400));

      scene.addDrawableObject(mesh);
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

      scene.addDrawableObject(sphere);

      // plane

      Point planeOrigin = new Point(0, 0, 0);
      Point planeNormal = new Point(0, 0, -1);

      Vector planeNormal1 = new Vector(planeOrigin, planeNormal);

      material = new Material();
      material.setColor(new Color(182, 73, 38));
      material.setDiffuse(.5);
      material.setReflectivity(.5);

      ImplicitPlane plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addDrawableObject(plane);
*/
      // lights

      scene.addRadiatableObject(new PointLight(new Point(x / 3, y / 2, 900), 20.0));
      scene.addRadiatableObject(new PointLight(new Point(2 * x / 3, 2 * y / 3, 900), 20.0));
      return scene;
   }

   public static Scene FourReflectiveSphereWithLights(int x, int y) {

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

      CameraOrientationMovement movement = new CameraOrientationMovement();
      movement.frame = 240;
      movement.orientation = new Ray(new Point(100, 300, 1500), new Vector(0, 0, -1));

      settings.Movement = movement;

      if (Main.UseDepthOfField) {
         camera = new DepthOfFieldCamera(settings);
      }

      else {
         camera = new SimplePointableCamera(settings);
      }
      //Vector orientation = new Vector(new Point(x/2, y/2, 1600), new Point(0, 0, -1));

      Scene scene = new KDScene(camera);

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

      scene.addDrawableObject(sphere);*/



      material = new Material();
      material.setColor(new Color(70, 137, 102));
      material.setDiffuse(.2);
      material.setReflectivity(0);
      material.setTransparency(.8);
      material.setIndexOfRefraction(1.2);

      sphere = new Sphere(material);
      sphere.Origin = new Point(-200, 700, 500.0);
      sphere.Radius = 150;

      //scene.addDrawableObject(sphere);

      material = new Material();
      material.setColor(new Color(255, 240, 185));
      material.setDiffuse(.5);
      material.setReflectivity(.5);
      material.setTransparency(0);
      material.setIndexOfRefraction(1.0);

      sphere = new Sphere(material);
      sphere.Origin = new Point(500, 100, 600.0);
      sphere.Radius = 150;

      //scene.addDrawableObject(sphere);

      p0 = new Point(-100, -100, -100);
      p1 = new Point(100, 100, 100);
      material.setColor(new Color(30, 120, 120));
      material.setReflectivity(.4);
      material.setSpecular(.3);
      material.setDiffuse(.4);

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
      scene.addDrawableObject(box);



      for (int i = 0; i < 20; i++) {
         p0 = new Point(-100, -100, -100);
         p1 = new Point(100, 100, 100);

         material = new Material();
         material.setColor(new Color(0, i * 10, 220));
         material.setReflectivity(.4);
         material.setSpecular(.3);
         material.setDiffuse(.4);

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
         scene.addDrawableObject(box);
      }



      material = new Material();
      material.setColor(new Color(255, 176, 59));
      material.setReflectivity(.01);
      material.setDiffuse(.99);

      sphere = new Sphere(material);
      sphere.Origin = new Point(-200, 100, 500);
      sphere.Radius = 150;

      scene.addDrawableObject(sphere);

      material = new Material();
      material.setColor(new Color(182, 73, 38));
      material.setReflectivity(.2);
      //material.setSpecular(.3);
      material.setDiffuse(.8);

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

      scene.addDrawableObject(sphere);

      material = new Material();
      material.setColor(new Color(128, 38, 163));
      material.setReflectivity(.2);
      //material.setSpecular(.3);
      material.setDiffuse(.8);

      list = new ArrayList<>();
      list.add(Transform.Translate(new Vector(400, 50, 400)));
      list.add(Transform.RotateY(70));
      list.add(Transform.RotateZ(90));

      list.add(Transform.Scale(50.0, 200.0, 50.0));

      transforms = GetCompositeTransforms(list);

      objectToWorld = transforms[0];
      worldToObject = transforms[1];

      Cylinder cylinder = new Cylinder(1, 1, worldToObject, objectToWorld, material);

      scene.addDrawableObject(cylinder);

      material = new Material();
      material.setColor(new Color(142, 40, 0));
      material.setReflectivity(.95);
      material.setDiffuse(.05);

      sphere = new Sphere(material);
      sphere.Origin = new Point(160, 300, 300.0);
      sphere.Radius = 25;

      //scene.addDrawableObject(sphere);

      // bottom horizontal orange plane


      p0 = new Point(-10000, -1, -10000);
      p1 = new Point(10000, 0, 10000);

      material = new Material();
      material.setColor(new Color(255, 240, 185));
      material.setDiffuse(.95);
      material.setReflectivity(.05);

      box = new Box(p0, p1, material);

      scene.addDrawableObject(box);

      // top horizontal orange plane


      p0 = new Point(-10000, 1001, -10000);
      p1 = new Point(10000, 1000, 10000);

      material = new Material();
      material.setColor(new Color(255, 131, 0));
      material.setDiffuse(.5);
      material.setReflectivity(.5);

      box = new Box(p0, p1, material);

      //scene.addDrawableObject(box);

      // forward white vertical z plane

      p0 = new Point(-10000, -10000, -400);
      p1 = new Point(10000, 10000, -401);

      Point planeOrigin = new Point(0, 0, -400);
      Normal planeNormal = new Normal(0, 0, 1);

      material = new Material();
      material.setColor(new Color(255, 240, 185));
      material.setDiffuse(.75);
      material.setReflectivity(.25);

      box = new Box(p0, p1, material);
      scene.addDrawableObject(box);

      // back white vertical z plane

      p0 = new Point(-10000, -10000, 5000);
      p1 = new Point(10000, 10000, 5001);

      material = new Material();
      material.setColor(new Color(255, 240, 185));
      material.setDiffuse(.75);
      material.setReflectivity(.25);

      box = new Box(p0, p1, material);
      //scene.addDrawableObject(box);

      // left blue vertical x plane

      p0 = new Point(-1000, -10000, -10000);
      p1 = new Point(-1001, 11000, 10000);

      material = new Material();
      material.setColor(new Color(0, 131, 255));
      material.setDiffuse(.75);
      material.setReflectivity(.25);

      box = new Box(p0, p1, material);
      //scene.addDrawableObject(box);

      // right blue vertical x plane

      p0 = new Point(1000, -10000, -10000);
      p1 = new Point(1001, 11000, 10000);


      material = new Material();
      material.setColor(new Color(0, 131, 255));
      material.setDiffuse(.75);
      material.setReflectivity(.25);

      box = new Box(p0, p1, material);
      //scene.addDrawableObject(box);

      SphereLight sphereLight = new SphereLight(100, material);

      PointLight pointLight = new PointLight(new Point(1000, 1000, 500), 100);
      scene.addRadiatableObject(pointLight);

      sphereLight.Origin = new Point(1000, 1000, 500);
      sphereLight.Radius = 50;

      //scene.addRadiatableObject(sphereLight);

      sphereLight = new SphereLight(100, material);

      pointLight = new PointLight(new Point(-1000, 2000, 500), 100);
      scene.addRadiatableObject(pointLight);

      sphereLight.Origin = new Point(-1000, 2000, 500);
      sphereLight.Radius = 50;

      //scene.addRadiatableObject(sphereLight);

      //scene.addRadiatableObject(new PointLight(new Point(300, 500, 700), 15.0));
      //scene.addRadiatableObject(new PointLight(new Point(400, 1000, 1300), 40.0));
      //scene.addRadiatableObject(new PointLight(new Point(300, 300, 300), 5.0));
      //scene.addRadiatableObject(new PointLight(new Point(300, 300, 1500), 10.0));
      //scene.addRadiatableObject(new PointLight(new Point(685, 360, -350), 5.0));
      //scene.addRadiatableObject(new PointLight(new Point(575, 180, -200), 5.0));
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


   public static KDScene FourReflectiveSphereWithLightsPointable(int x, int y) {

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

      KDScene scene = new KDScene(camera);

      scene.numFrames = 1;

      // lights

      Material material = new Material();
      material.setColor(new Color(64, 192, 255));

      Point location = new Point(250, 360, 600);

      SphereLight sphereLight = new SphereLight(25, material);

      PointLight pointLight = new PointLight(location, 50);

      sphereLight.Origin = location;
      sphereLight.Radius = 50;

      scene.addRadiatableObject(sphereLight);
      //scene.addDrawableObject(sphereLight);
      //scene.addRadiatableObject(pointLight);

      location = new Point(300, 300, 300);

      pointLight = new PointLight(location, 15);


      material = new Material();
      material.setColor(new Color(255, 255, 64));

      sphereLight = new SphereLight(15, material);
      sphereLight.Origin = location;
      sphereLight.Radius = 50;

      scene.addRadiatableObject(sphereLight);
      //scene.addDrawableObject(sphereLight);
      //scene.addRadiatableObject(pointLight);
/*
      sphereLight = new SphereLight(5);
      sphereLight.Origin = new Point(300, 300, 300);
      sphereLight.Radius = 50;

      scene.addRadiatableObject(sphereLight);

      sphereLight = new SphereLight(10);
      sphereLight.Origin = new Point(300, 300, 1500);
      sphereLight.Radius = 50;

      scene.addRadiatableObject(sphereLight);

      sphereLight = new SphereLight(5);
      sphereLight.Origin = new Point(575, 180, -200);
      sphereLight.Radius = 50;

      scene.addRadiatableObject(sphereLight);
      */

/*
      scene.addRadiatableObject(new PointLight(new Point(160, 360, 600), 5.0));
      scene.addRadiatableObject(new PointLight(new Point(480, 360, 600), 5.0));
      scene.addRadiatableObject(new PointLight(new Point(300, 300, 300), 5.0));
      scene.addRadiatableObject(new PointLight(new Point(300, 300, 1500), 10.0));
      //scene.addRadiatableObject(new PointLight(new Point(685, 360, -350), 5.0));
      scene.addRadiatableObject(new PointLight(new Point(575, 180, -200), 5.0));
*/


      // horizontal orange plane

      Point planeOrigin = new Point(0, 0, 0);
      Normal planeNormal = new Normal(0, 1, 0);

      material = new Material();
      material.setColor(new Color(255, 131, 0));
      material.setDiffuse(.5);
      material.setReflectivity(.5);

      ImplicitPlane plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addDrawableObject(plane);

      // white vertical z plane

      planeOrigin = new Point(0, 0, -1000);
      planeNormal = new Normal(0, 0, 1);

      

      material = new Material();
      material.setColor(new Color(255, 240, 185));
      material.setDiffuse(1);
      material.setReflectivity(0);

      plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addDrawableObject(plane);

      // white vertical x plane

      planeOrigin = new Point(800, 0, 0);
      planeNormal = new Normal(-1, 0, 0);

      

      material = new Material();
      material.setColor(new Color(255, 240, 185));
      material.setDiffuse(.9);
      material.setReflectivity(.1);

      plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addDrawableObject(plane);

      // blue vertical x plane

      planeOrigin = new Point(-200, 0, 0);
      planeNormal = new Normal(1, 0, 0);

      

      material = new Material();
      material.setColor(new Color(0, 131, 255));
      material.setDiffuse(1);
      material.setReflectivity(0);

      plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addDrawableObject(plane);


      // green sphere

      material = new Material();
      material.setColor(new Color(70, 137, 102));
      material.setDiffuse(.5);
      material.setReflectivity(.5);
      material.setTransparency(0);
      material.setIndexOfRefraction(1.2);

      Sphere sphere = new Sphere(material);
      sphere.Origin = new Point(-200, 350, -100.0);
      sphere.Radius = 150;

      scene.addDrawableObject(sphere);

      // green sphere

      material = new Material();
      material.setColor(new Color(70, 137, 102));
      material.setDiffuse(.5);
      material.setReflectivity(.5);
      material.setTransparency(0);
      material.setIndexOfRefraction(1.2);

      sphere = new Sphere(material);
      sphere.Origin = new Point(500, 350, 500.0);
      sphere.Radius = 150;

      scene.addDrawableObject(sphere);

      // white sphere

      material = new Material();
      material.setColor(new Color(255, 240, 185));
      material.setDiffuse(.1);
      material.setReflectivity(.9);
      material.setTransparency(0);
      material.setIndexOfRefraction(1.3);

      sphere = new Sphere(material);
      sphere.Origin = new Point(500, 100, 600.0);
      sphere.Radius = 150;

      scene.addDrawableObject(sphere);

      // big dark orange sphere

      material = new Material();
      material.setColor(new Color(182, 73, 38));
      material.setReflectivity(.5);
      material.setDiffuse(.5);

      sphere = new Sphere(material);
      sphere.Origin = new Point(800, 350, 1000.0);
      sphere.Radius = 150;

      scene.addDrawableObject(sphere);

      //

      // orange sphere

      material = new Material();
      material.setColor(new Color(255, 176, 59));
      material.setReflectivity(.5);
      material.setDiffuse(.5);

      sphere = new Sphere(material);
      sphere.Origin = new Point(-200, 100, 1000);
      sphere.Radius = 150;

      scene.addDrawableObject(sphere);

      // tiny red sphere

      material = new Material();
      material.setColor(new Color(142, 40, 0));
      material.setReflectivity(.5);
      material.setDiffuse(.5);

      sphere = new Sphere(material);
      sphere.Origin = new Point(160, 300, 300.0);
      sphere.Radius = 25;

      scene.addDrawableObject(sphere);

      // tiny blue sphere

      material = new Material();
      material.setColor(new Color(62, 96, 111));
      material.setReflectivity(.6);
      material.setDiffuse(.4);

      sphere = new Sphere(material);
      sphere.Origin = new Point(250, 300, 300.0);
      sphere.Radius = 25;

      scene.addDrawableObject(sphere);



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

      SphereLight sphereLight = new SphereLight(5);
      sphereLight.Origin = new Point(160, 360, 600);
      sphereLight.Radius = 50;

      scene.addRadiatableObject(sphereLight);

      sphereLight = new SphereLight(5);
      sphereLight.Origin = new Point(480, 360, 600);
      sphereLight.Radius = 50;

      scene.addRadiatableObject(sphereLight);

      sphereLight = new SphereLight(5);
      sphereLight.Origin = new Point(300, 300, 300);
      sphereLight.Radius = 50;

      scene.addRadiatableObject(sphereLight);

      sphereLight = new SphereLight(10);
      sphereLight.Origin = new Point(300, 300, 1500);
      sphereLight.Radius = 50;

      scene.addRadiatableObject(sphereLight);

      sphereLight = new SphereLight(5);
      sphereLight.Origin = new Point(575, 180, -200);
      sphereLight.Radius = 50;

      scene.addRadiatableObject(sphereLight);

      //scene.addRadiatableObject(new PointLight(new Point(160, 360, 600), 5.0));
      //scene.addRadiatableObject(new PointLight(new Point(480, 360, 600), 5.0));
      //scene.addRadiatableObject(new PointLight(new Point(300, 300, 300), 5.0));
      //scene.addRadiatableObject(new PointLight(new Point(300, 300, 1500), 10.0));
      //scene.addRadiatableObject(new PointLight(new Point(685, 360, -350), 5.0));
      //scene.addRadiatableObject(new PointLight(new Point(575, 180, -200), 5.0));

      Material material = new Material();

      // horizontal orange plane

      Point planeOrigin = new Point(0, 0, 0);
      Normal planeNormal = new Normal(0, 1, 0);

      material = new Material();
      material.setColor(new Color(255, 131, 0));
      material.setDiffuse(0);
      material.setReflectivity(.1);

      ImplicitPlane plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addDrawableObject(plane);

      // white vertical z plane

      planeOrigin = new Point(0, 0, -400);
      planeNormal = new Normal(0, 0, 1);

      

      material = new Material();
      material.setColor(new Color(255, 240, 185));
      material.setDiffuse(0);
      material.setReflectivity(.25);

      plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addDrawableObject(plane);

      // white vertical x plane

      planeOrigin = new Point(800, 0, 0);
      planeNormal = new Normal(-1, 0, 0);

      

      material = new Material();
      material.setColor(new Color(255, 240, 185));
      material.setDiffuse(0);
      material.setReflectivity(.25);

      plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addDrawableObject(plane);

      // blue vertical x plane

      planeOrigin = new Point(-200, 0, 0);
      planeNormal = new Normal(1, 0, 0);

      

      material = new Material();
      material.setColor(new Color(0, 131, 255));
      material.setDiffuse(1);

      plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addDrawableObject(plane);


      // left green sphere

      material = new Material();
      material.setColor(new Color(70, 137, 102));
      material.setDiffuse(.5);
      material.setReflectivity(.5);
      material.setTransparency(0);
      material.setIndexOfRefraction(1.2);

      Sphere sphere = new Sphere(material);
      sphere.Origin = new Point(100, 350, -100.0);
      sphere.Radius = 150;

      scene.addDrawableObject(sphere);

      // right green sphere

      material = new Material();
      material.setColor(new Color(70, 137, 102));
      material.setDiffuse(.5);
      material.setReflectivity(.5);
      material.setTransparency(0);
      material.setIndexOfRefraction(1.2);

      sphere = new Sphere(material);
      sphere.Origin = new Point(500, 350, -100.0);
      sphere.Radius = 150;

      scene.addDrawableObject(sphere);

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

      SphereLight sphereLight = new SphereLight(400);
      sphereLight.Origin = new Point(0, 2000, 0);
      sphereLight.Radius = 1000;

      PointLight pointLight = new PointLight(new Point(0, 2000, 0), 1000);

      scene.addRadiatableObject(sphereLight);
      //scene.addRadiatableObject(pointLight);

      Material material = new Material();

      // horizontal white plane

      Point planeOrigin = new Point(0, 0, 0);
      Normal planeNormal = new Normal(0, 1, 0);

      material = new Material();
      material.setColor(new Color(255, 240, 185));
      material.setDiffuse(1);

      ImplicitPlane plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addDrawableObject(plane);

      // blue vertical z plane
/*
      planeOrigin = new Point(-200, 0, 0);
      planeNormal = new Point(0, 0, 1);

      planeNormal1 = new Vector(planeOrigin, planeNormal);

      material = new Material();
      material.setColor(new Color(0, 131, 255));
      material.setDiffuse(1);

      plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addDrawableObject(plane);
*/

      // left green sphere

      material = new Material();
      material.setColor(new Color(70, 137, 102));
      material.setDiffuse(.5);
      material.setReflectivity(.5);
      material.setTransparency(0);
      material.setIndexOfRefraction(1.2);

      Sphere sphere = new Sphere(material);
      sphere.Origin = new Point(-500, 300, 0.0);
      sphere.Radius = 150;

      scene.addDrawableObject(sphere);

      // right green sphere

      material = new Material();
      material.setColor(new Color(70, 137, 102));
      material.setDiffuse(.5);
      material.setReflectivity(.5);
      material.setTransparency(0);
      material.setIndexOfRefraction(1.2);

      sphere = new Sphere(material);
      sphere.Origin = new Point(500, 500, 0.0);
      sphere.Radius = 150;

      scene.addDrawableObject(sphere);

      // middle

      material = new Material();
      material.setColor(new Color(70, 137, 102));
      material.setDiffuse(.5);
      material.setReflectivity(.5);
      material.setTransparency(0);
      material.setIndexOfRefraction(1.2);

      sphere = new Sphere(material);
      sphere.Origin = new Point(0, 150, 0.0);
      sphere.Radius = 150;

      scene.addDrawableObject(sphere);

      return scene;
   }

   public static KDScene TwoTransparentReflectiveSpheresWithLights(int x, int y) {
      KDScene scene = new KDScene(null);

      Material material = new Material();
      material.setColor(Color.blue);
      material.setReflectivity(.33);
      //material.setTransparency(.33);
      //material.setIndexOfRefraction(1.333);

      Sphere sphere = new Sphere(material);
      sphere.Origin = new Point(2 * x / 5, y / 3, 500.0);
      sphere.Radius = y / 5;

      scene.addDrawableObject(sphere);

      Point planeOrigin = new Point(0, 0, -400);
      Normal planeNormal = new Normal(0, 0, 1);

      material = new Material();
      material.setColor(new Color(255, 131, 0));
      //material.setDiffuse(.5);
      //material.setReflectivity(0);

      ImplicitPlane plane = new ImplicitPlane(planeOrigin, planeNormal, material);
      scene.addDrawableObject(plane);

      scene.addRadiatableObject(new PointLight(new Point(x / 2, y / 2, 500), 25.0));

      return scene;
   }
}