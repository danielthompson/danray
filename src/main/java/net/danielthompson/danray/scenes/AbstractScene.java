package net.danielthompson.danray.scenes;

import net.danielthompson.danray.cameras.Camera;
import net.danielthompson.danray.lights.AbstractLight;
import net.danielthompson.danray.presets.TracerOptions;

import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.shapes.AbstractShape;
import net.danielthompson.danray.shapes.Box;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.*;
import net.danielthompson.danray.structures.Point;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 1/4/15.
 */
public abstract class AbstractScene {
   public int numFrames = 1;
   public Camera Camera;

   public String ImplementationType = "Base Scene";

   public List<AbstractLight> Lights;
   public List<AbstractShape> Shapes;

   public BufferedImage SkyBoxImage;

   public BufferedImage SkyBoxNegX;
   public BufferedImage SkyBoxNegY;
   public BufferedImage SkyBoxNegZ;
   public BufferedImage SkyBoxPosX;
   public BufferedImage SkyBoxPosY;
   public BufferedImage SkyBoxPosZ;

   public Box Skybox;

   public Point SkyBoxPoint = new Point(0.5f, 0.5f, 0.5f);

   private final SpectralPowerDistribution backgroundColor = new SpectralPowerDistribution(Color.BLACK);

   public AbstractScene(Camera camera) {
      Camera = camera;
      Shapes = new ArrayList<>();
      Lights = new ArrayList<>();
   }

   public void addShape(AbstractShape shape) {
      Shapes.add(shape);
   }

   public void addLight(AbstractLight light) {
      Lights.add(light);
   }

   public abstract IntersectionState getNearestShape(Ray ray, int x, int y);

   public String compile(TracerOptions _tracerOptions) {
      for (AbstractShape shape : Shapes) {
         if (shape.ObjectToWorld == null) {
            shape.ObjectToWorld = Transform.identity;
            shape.WorldToObject = Transform.identity;
         }

         shape.RecalculateWorldBoundingBox();
      }


      if (SkyBoxImage != null) {
         Skybox = new Box(Transform.identity, Transform.identity,null);

         int width = SkyBoxImage.getWidth();
         int height = SkyBoxImage.getHeight();

         int tileSize = width / 4;

         SkyBoxNegX = SkyBoxImage.getSubimage(0, tileSize, tileSize, tileSize);
         SkyBoxNegY = SkyBoxImage.getSubimage(tileSize, tileSize * 2, tileSize, tileSize);
         SkyBoxNegZ = SkyBoxImage.getSubimage(tileSize, tileSize, tileSize, tileSize);
         SkyBoxPosX = SkyBoxImage.getSubimage(tileSize * 2, tileSize, tileSize, tileSize);
         SkyBoxPosY = SkyBoxImage.getSubimage(tileSize, 0, tileSize, tileSize);
         SkyBoxPosZ = SkyBoxImage.getSubimage(tileSize * 3, tileSize, tileSize, tileSize);
      }



      return "Bounding boxes recalculated.\r\n";
   }

   public SpectralPowerDistribution getSkyBoxSPD(Vector direction) {

      if (SkyBoxNegX == null)
         return backgroundColor;

      Ray r = new Ray(SkyBoxPoint, direction);

      IntersectionState state = Skybox.getHitInfo(r);

      Point p = state.IntersectionPoint;

      float u = 0.0f;
      float v = 0.0f;

      float width = SkyBoxNegX.getWidth();
      float height = SkyBoxNegX.getHeight();

      BufferedImage texture = null;

      //float tileSize = width * .25f;

      // left wall

      texture = Constants.WithinEpsilon(p.X, 0) ? SkyBoxNegX : texture;
      u = Constants.WithinEpsilon(p.X, 0) ? width - width * p.Z : u;
      v = Constants.WithinEpsilon(p.X, 0) ? height - (height * p.Y) : v;

      // back wall

      texture = Constants.WithinEpsilon(p.Z, 0) ? SkyBoxNegZ : texture;
      u = Constants.WithinEpsilon(p.Z, 0) ? width * p.X: u;
      v = Constants.WithinEpsilon(p.Z, 0) ? height - (height * p.Y) : v;
//      y = Constants.WithinEpsilon(p.Z, 0) ? .33f * height * (2 - p.Y) : y;

      // right wall

      texture = Constants.WithinEpsilon(p.X, 1) ? SkyBoxPosX : texture;
      u = Constants.WithinEpsilon(p.X, 1) ? width * p.Z: u;
      v = Constants.WithinEpsilon(p.X, 1) ? height - (height * p.Y) : v;

      // front wall

      texture = Constants.WithinEpsilon(p.Z, 1) ? SkyBoxPosZ : texture;
      u = Constants.WithinEpsilon(p.Z, 1) ? (width - width * p.X) : u;
      v = Constants.WithinEpsilon(p.Z, 1) ? height - (height * p.Y) : v;

      // top wall

      texture = Constants.WithinEpsilon(p.Y, 1) ? SkyBoxPosY : texture;
      u = Constants.WithinEpsilon(p.Y, 1) ? width * p.X : u;
      v = Constants.WithinEpsilon(p.Y, 1) ? height - height * p.Z : v;

      // bottom wall

      texture = Constants.WithinEpsilon(p.Y, 0) ? SkyBoxNegY : texture;
      u = Constants.WithinEpsilon(p.Y, 0) ? width * p.X : u;
      v = Constants.WithinEpsilon(p.Y, 0) ? height * p.Z : v;

      u = (u == width) ? u - 1 : u;
      v = (v == height) ? v - 1 : v;

      int rgb = texture.getRGB((int)u, (int)v);

      Color c = new Color(rgb);

      SpectralPowerDistribution spd = new SpectralPowerDistribution(c);

      return spd;
   }

}
