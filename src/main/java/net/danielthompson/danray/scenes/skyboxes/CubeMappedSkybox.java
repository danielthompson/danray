package net.danielthompson.danray.scenes.skyboxes;

import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.shapes.Box;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.*;
import net.danielthompson.danray.structures.Point3;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CubeMappedSkybox extends AbstractSkybox {

   public SpectralPowerDistribution NaNColor = new SpectralPowerDistribution(Color.magenta);

   public BufferedImage SkyBoxImage;

   public BufferedImage SkyBoxNegX;
   public BufferedImage SkyBoxNegY;
   public BufferedImage SkyBoxNegZ;
   public BufferedImage SkyBoxPosX;
   public BufferedImage SkyBoxPosY;
   public BufferedImage SkyBoxPosZ;

   public Point3 SkyBoxPoint = new Point3(0.5f, 0.5f, 0.5f);

   public Box Skybox;

   public CubeMappedSkybox(BufferedImage image) {
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
   }

   @Override
   public SpectralPowerDistribution getSkyBoxSPD(Vector3 direction) {

      if (Float.isNaN(direction.x)) {
         return NaNColor;
      }

      Ray r = new Ray(SkyBoxPoint, direction);

      Intersection state = Skybox.GetHitInfo(r);

      Point3 p = state.Location;

      float u = 0.0f;
      float v = 0.0f;

      float width = SkyBoxNegX.getWidth();
      float height = SkyBoxNegX.getHeight();

      BufferedImage texture = null;

      //float tileSize = width * .25f;

      // left wall

      texture = Constants.WithinEpsilon(p.x, 0) ? SkyBoxNegX : texture;
      u = Constants.WithinEpsilon(p.x, 0) ? width - width * p.z : u;
      v = Constants.WithinEpsilon(p.x, 0) ? height - (height * p.y) : v;

      // back wall

      texture = Constants.WithinEpsilon(p.z, 0) ? SkyBoxNegZ : texture;
      u = Constants.WithinEpsilon(p.z, 0) ? width * p.x : u;
      v = Constants.WithinEpsilon(p.z, 0) ? height - (height * p.y) : v;
//      y = Constants.WithinEpsilon(p.z, 0) ? .33f * height * (2 - p.y) : y;

      // right wall

      texture = Constants.WithinEpsilon(p.x, 1) ? SkyBoxPosX : texture;
      u = Constants.WithinEpsilon(p.x, 1) ? width * p.z : u;
      v = Constants.WithinEpsilon(p.x, 1) ? height - (height * p.y) : v;

      // front wall

      texture = Constants.WithinEpsilon(p.z, 1) ? SkyBoxPosZ : texture;
      u = Constants.WithinEpsilon(p.z, 1) ? (width - width * p.x) : u;
      v = Constants.WithinEpsilon(p.z, 1) ? height - (height * p.y) : v;

      // top wall

      texture = Constants.WithinEpsilon(p.y, 1) ? SkyBoxPosY : texture;
      u = Constants.WithinEpsilon(p.y, 1) ? width * p.x : u;
      v = Constants.WithinEpsilon(p.y, 1) ? height - height * p.z : v;

      // bottom wall

      texture = Constants.WithinEpsilon(p.y, 0) ? SkyBoxNegY : texture;
      u = Constants.WithinEpsilon(p.y, 0) ? width * p.x : u;
      v = Constants.WithinEpsilon(p.y, 0) ? height * p.z : v;

      u = (u == width) ? u - 1 : u;
      v = (v == height) ? v - 1 : v;

      Color c = BoxFilterSkybox(texture, u, v);

      SpectralPowerDistribution spd = new SpectralPowerDistribution(c);

      return spd;
   }

   private Color BoxFilterSkybox(BufferedImage texture, float u, float v) {
      int rgb = texture.getRGB((int)u, (int)v);
      Color c = new Color(rgb);
      return c;
   }

   private Color TriangleFilterSkybox(BufferedImage texture, float u, float v) {
      int x, y;
      float weight;

      // top left

      float squaredDistanceTopLeft =
            x = (int)Math.floor(u + 0.5);
      y = (int)Math.floor(v + 0.5);


      return null;
   }
}
