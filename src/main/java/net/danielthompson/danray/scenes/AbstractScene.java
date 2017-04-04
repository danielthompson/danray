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

   public abstract IntersectionState getNearestShape(Ray ray);

   public String compile(TracerOptions _tracerOptions) {
      for (AbstractShape shape : Shapes) {
         shape.RecalculateWorldBoundingBox();
      }

      for (AbstractLight light : Lights) {
         light.RecalculateWorldBoundingBox();
      }

      if (SkyBoxImage != null) {
         Skybox = new Box(Transform.identity, Transform.identity,null);
      }

      return "Bounding boxes recalculated.\r\n";
   }

   public SpectralPowerDistribution getSkyBoxSPD(Vector v) {

      if (SkyBoxImage == null)
         return backgroundColor;

      Ray r = new Ray(SkyBoxPoint, v);

      IntersectionState state = Skybox.getHitInfo(r);

      Point p = state.IntersectionPoint;

      float x = 0.0f;
      float y = 0.0f;

      float width = SkyBoxImage.getWidth();
      float height = SkyBoxImage.getHeight();

      float tileSize = width * .25f;

      // left wall

      x = Constants.WithinEpsilon(p.X, 0) ? tileSize - tileSize * p.Z : x;
      y = Constants.WithinEpsilon(p.X, 0) ? height - (tileSize * p.Y + tileSize) : y;

      // back wall

      x = Constants.WithinEpsilon(p.Z, 0) ? tileSize * p.X + tileSize: x;
      y = Constants.WithinEpsilon(p.Z, 0) ? height - (tileSize * p.Y + tileSize) : y;
//      y = Constants.WithinEpsilon(p.Z, 0) ? .33f * height * (2 - p.Y) : y;

      // right wall

      x = Constants.WithinEpsilon(p.X, 1) ? tileSize * p.Z + 2 * tileSize : x;
      y = Constants.WithinEpsilon(p.X, 1) ? height - (tileSize * p.Y + tileSize) : y;

      // front wall

      x = Constants.WithinEpsilon(p.Z, 1) ? (tileSize - tileSize * p.X) + 3 * tileSize : x;
      y = Constants.WithinEpsilon(p.Z, 1) ? height - (tileSize * p.Y + tileSize) : y;

      // top wall

      x = Constants.WithinEpsilon(p.Y, 1) ? tileSize * p.X + tileSize : x;
      y = Constants.WithinEpsilon(p.Y, 1) ? tileSize - tileSize * p.Z : y;

      // bottom wall

      x = Constants.WithinEpsilon(p.Y, 0) ? tileSize * p.X + tileSize : x;
      y = Constants.WithinEpsilon(p.Y, 0) ? tileSize * p.Z + 2 * tileSize : y;

      int newX = (int)x - 1;
      int newY = (int)y - 1;

      if (newX >= width || newY >= height) {
         int i = 0;
      }
      if (newX <= 0 || newY <= 0) {
         int j = 0;
         newX = 0;
         newY = 0;
      }


      int rgb = SkyBoxImage.getRGB(newX, newY);

      Color c = new Color(rgb);

      SpectralPowerDistribution spd = new SpectralPowerDistribution(c);

      return spd;
   }

}
