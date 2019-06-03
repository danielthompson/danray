package net.danielthompson.danray.scenes.skyboxes;

import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.shapes.Box;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.*;
import net.danielthompson.danray.structures.Point3;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CubeMappedSkybox extends AbstractSkybox {

   private final SpectralPowerDistribution nanColor = new SpectralPowerDistribution(Color.magenta);

   private final BufferedImage negX;
   private final BufferedImage negY;
   private final BufferedImage negZ;
   private final BufferedImage posX;
   private final BufferedImage posY;
   private final BufferedImage posZ;

   private final Point3 center = new Point3(0.5f, 0.5f, 0.5f);

   private final Box box;

   private final int tileHeight;
   private final int tileWidth;

   public CubeMappedSkybox(final BufferedImage image, Transform[] transforms) {
      box = new Box(transforms,null);

      tileWidth = image.getWidth() / 4;
      tileHeight = image.getHeight() / 3;

      // assumes cubemap is tiled like
      //    +Y
      // -X -Z +X +Z
      //    -Y
      negX = image.getSubimage(0, tileWidth, tileWidth, tileWidth);
      negY = image.getSubimage(tileWidth, tileWidth * 2, tileWidth, tileWidth);
      negZ = image.getSubimage(tileWidth, tileWidth, tileWidth, tileWidth);
      posX = image.getSubimage(tileWidth * 2, tileWidth, tileWidth, tileWidth);
      posY = image.getSubimage(tileWidth, 0, tileWidth, tileWidth);
      posZ = image.getSubimage(tileWidth * 3, tileWidth, tileWidth, tileWidth);
   }

   public CubeMappedSkybox(final BufferedImage image) {
      this(image, new Transform[] { Transform.identity, Transform.identity});
   }

   @Override
   public SpectralPowerDistribution getSPD(final Vector3 direction) {
      if (Float.isNaN(direction.x)) {
         return nanColor;
      }

      final Ray r = new Ray(center, direction);
      // no need to check result, we know it hits
      box.hits(r);
      final Intersection state = box.intersect(r);

      final Point3 p = box.WorldToObject.apply(state.location);

      float u = 0.0f;
      float v = 0.0f;

      BufferedImage texture = null;

      // left wall

      texture = Constants.WithinEpsilon(p.x, 0) ? negX : texture;
      u = Constants.WithinEpsilon(p.x, 0) ? tileWidth - tileWidth * p.z : u;
      v = Constants.WithinEpsilon(p.x, 0) ? tileHeight - (tileHeight * p.y) : v;

      // back wall

      texture = Constants.WithinEpsilon(p.z, 0) ? negZ : texture;
      u = Constants.WithinEpsilon(p.z, 0) ? tileWidth * p.x : u;
      v = Constants.WithinEpsilon(p.z, 0) ? tileHeight - (tileHeight * p.y) : v;

      // right wall

      texture = Constants.WithinEpsilon(p.x, 1) ? posX : texture;
      u = Constants.WithinEpsilon(p.x, 1) ? tileWidth * p.z : u;
      v = Constants.WithinEpsilon(p.x, 1) ? tileHeight - (tileHeight * p.y) : v;

      // front wall

      texture = Constants.WithinEpsilon(p.z, 1) ? posZ : texture;
      u = Constants.WithinEpsilon(p.z, 1) ? (tileWidth - tileWidth * p.x) : u;
      v = Constants.WithinEpsilon(p.z, 1) ? tileHeight - (tileHeight * p.y) : v;

      // top wall

      texture = Constants.WithinEpsilon(p.y, 1) ? posY : texture;
      u = Constants.WithinEpsilon(p.y, 1) ? tileWidth * p.x : u;
      v = Constants.WithinEpsilon(p.y, 1) ? tileHeight - tileHeight * p.z : v;

      // bottom wall

      texture = Constants.WithinEpsilon(p.y, 0) ? negY : texture;
      u = Constants.WithinEpsilon(p.y, 0) ? tileWidth * p.x : u;
      v = Constants.WithinEpsilon(p.y, 0) ? tileHeight * p.z : v;

      u = (u == tileWidth) ? u - 1 : u;
      v = (v == tileHeight) ? v - 1 : v;

      final Color c = lerp2(texture, u, v);

      final SpectralPowerDistribution spd = new SpectralPowerDistribution(c);

      return spd;
   }

   /**
    * Returns the color of the top-left texel for (u, v).
    * @param texture
    * @param u
    * @param v
    * @return
    */
   private Color nearestNeighbor(final BufferedImage texture, final float u, final float v) {
      final int rgb = texture.getRGB((int)u, (int)v);
      final Color c = new Color(rgb);
      return c;
   }

   /**
    * Returns the average of the 4 texel values nearest to (u, v), weighted by distance
    * @param texture
    * @param u
    * @param v
    * @return
    */
   private Color lerp2(final BufferedImage texture, final float u, final float v) {
      // TODO 1. needs to properly handle u/v values on texture boundaries
      // TODO 2. needs to properly handle u/v exactly on a texel (i.e. (1.0f, 1.0f) should not lerp)
      float r = 0, g = 0, b = 0;

      int uFloor = (int)u;
      int vFloor = (int)v;

      final float uLeftDistance = u - uFloor;
      final float uRightDistance = 1.0f - uLeftDistance;
      final float vTopDistance = v - vFloor;
      final float vBottomDistance = 1.0f - vTopDistance;

      final Color leftTop = new Color(texture.getRGB(uFloor, vFloor));
      final Color leftBottom = new Color(texture.getRGB(uFloor, vFloor + 1));
      final Color rightTop = new Color(texture.getRGB(uFloor + 1, vFloor));
      final Color rightBottom = new Color(texture.getRGB(uFloor + 1, vFloor + 1));

      float factor = Constants.OneOver255f * uRightDistance * vBottomDistance;

      r += leftTop.getRed() * factor;
      g += leftTop.getGreen() * factor;
      b += leftTop.getBlue() * factor;

      factor = Constants.OneOver255f * uRightDistance * vTopDistance;

      r += leftBottom.getRed() * factor;
      g += leftBottom.getGreen() * factor;
      b += leftBottom.getBlue() * factor;

      factor = Constants.OneOver255f * uLeftDistance * vBottomDistance;

      r += rightTop.getRed() * factor;
      g += rightTop.getGreen() * factor;
      b += rightTop.getBlue() * factor;

      factor = Constants.OneOver255f * uLeftDistance * vTopDistance;

      r += rightBottom.getRed() * factor;
      g += rightBottom.getGreen() * factor;
      b += rightBottom.getBlue() * factor;

      if (r > 1.0f) {
         //Logger.Log(Logger.Level.Warning, "Clamping skybox r from " + r);
         r = 1.0f;
      }

      if (g > 1.0f) {
         //Logger.Log(Logger.Level.Warning, "Clamping skybox g from " + g);
         g = 1.0f;
      }
      if (b > 1.0f) {
         //Logger.Log(Logger.Level.Warning, "Clamping skybox b from " + b);
         b = 1.0f;
      }

      final Color c = new Color(r, g, b);

      return c;
   }
}
