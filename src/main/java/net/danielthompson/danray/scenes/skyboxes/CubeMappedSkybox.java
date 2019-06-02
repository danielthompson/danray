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

   public CubeMappedSkybox(final BufferedImage image) {
      box = new Box(Transform.identity, Transform.identity,null);

      final int width = image.getWidth();
      int height = image.getHeight();

      int tileSize = width / 4;

      // assumes cubemap is tiled like
      //    +Y
      // -X -Z +X +Z
      //    -Y
      negX = image.getSubimage(0, tileSize, tileSize, tileSize);
      negY = image.getSubimage(tileSize, tileSize * 2, tileSize, tileSize);
      negZ = image.getSubimage(tileSize, tileSize, tileSize, tileSize);
      posX = image.getSubimage(tileSize * 2, tileSize, tileSize, tileSize);
      posY = image.getSubimage(tileSize, 0, tileSize, tileSize);
      posZ = image.getSubimage(tileSize * 3, tileSize, tileSize, tileSize);
   }

   @Override
   public SpectralPowerDistribution getSkyBoxSPD(Vector3 direction) {

      if (Float.isNaN(direction.x)) {
         return nanColor;
      }

      final Ray r = new Ray(center, direction);

      final Intersection state = box.intersect(r);

      Point3 p = state.location;

      float u = 0.0f;
      float v = 0.0f;

      float width = negX.getWidth();
      float height = negX.getHeight();

      BufferedImage texture = null;

      //float tileSize = width * .25f;

      // left wall

      texture = Constants.WithinEpsilon(p.x, 0) ? negX : texture;
      u = Constants.WithinEpsilon(p.x, 0) ? width - width * p.z : u;
      v = Constants.WithinEpsilon(p.x, 0) ? height - (height * p.y) : v;

      // back wall

      texture = Constants.WithinEpsilon(p.z, 0) ? negZ : texture;
      u = Constants.WithinEpsilon(p.z, 0) ? width * p.x : u;
      v = Constants.WithinEpsilon(p.z, 0) ? height - (height * p.y) : v;
//      y = Constants.WithinEpsilon(p.z, 0) ? .33f * height * (2 - p.y) : y;

      // right wall

      texture = Constants.WithinEpsilon(p.x, 1) ? posX : texture;
      u = Constants.WithinEpsilon(p.x, 1) ? width * p.z : u;
      v = Constants.WithinEpsilon(p.x, 1) ? height - (height * p.y) : v;

      // front wall

      texture = Constants.WithinEpsilon(p.z, 1) ? posZ : texture;
      u = Constants.WithinEpsilon(p.z, 1) ? (width - width * p.x) : u;
      v = Constants.WithinEpsilon(p.z, 1) ? height - (height * p.y) : v;

      // top wall

      texture = Constants.WithinEpsilon(p.y, 1) ? posY : texture;
      u = Constants.WithinEpsilon(p.y, 1) ? width * p.x : u;
      v = Constants.WithinEpsilon(p.y, 1) ? height - height * p.z : v;

      // bottom wall

      texture = Constants.WithinEpsilon(p.y, 0) ? negY : texture;
      u = Constants.WithinEpsilon(p.y, 0) ? width * p.x : u;
      v = Constants.WithinEpsilon(p.y, 0) ? height * p.z : v;

      u = (u == width) ? u - 1 : u;
      v = (v == height) ? v - 1 : v;

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
   private Color nearestNeighbor(BufferedImage texture, float u, float v) {
      int rgb = texture.getRGB((int)u, (int)v);
      Color c = new Color(rgb);
      return c;
   }

   /**
    * Returns the average of the 4 texel values nearest to (u, v), weighted by distance
    * @param texture
    * @param u
    * @param v
    * @return
    */
   private Color lerp2(final BufferedImage texture, float u, float v) {
      // TODO 1. needs to properly handle u/v values on texture boundaries
      // TODO 2. needs to properly handle u/v exactly on a texel (i.e. (1.0f, 1.0f) should not lerp)
      float r = 0, g = 0, b = 0;

      final float uLeftDistance = u - (int)u;
      final float uRightDistance = 1.0f - uLeftDistance;
      final float vTopDistance = v - (int)v;
      final float vBottomDistance = 1.0f - vTopDistance;

      Color leftTop = new Color(texture.getRGB((int)u, (int)v));
      Color leftBottom = new Color(texture.getRGB((int)u, (int)v + 1));
      Color rightTop = new Color(texture.getRGB((int)u + 1, (int)v));
      Color rightBottom = new Color(texture.getRGB((int)u + 1, (int)v + 1));

      r += leftTop.getRed() * Constants.OneOver255f * uRightDistance * vBottomDistance;
      g += leftTop.getGreen() * Constants.OneOver255f * uRightDistance * vBottomDistance;
      b += leftTop.getBlue() * Constants.OneOver255f * uRightDistance * vBottomDistance;

      r += leftBottom.getRed() * Constants.OneOver255f * uRightDistance * vTopDistance;
      g += leftBottom.getGreen() * Constants.OneOver255f * uRightDistance * vTopDistance;
      b += leftBottom.getBlue() * Constants.OneOver255f * uRightDistance * vTopDistance;

      r += rightTop.getRed() * Constants.OneOver255f * uLeftDistance * vBottomDistance;
      g += rightTop.getGreen() * Constants.OneOver255f * uLeftDistance * vBottomDistance;
      b += rightTop.getBlue() * Constants.OneOver255f * uLeftDistance * vBottomDistance;

      r += rightBottom.getRed() * Constants.OneOver255f * uLeftDistance * vTopDistance;
      g += rightBottom.getGreen() * Constants.OneOver255f * uLeftDistance * vTopDistance;
      b += rightBottom.getBlue() * Constants.OneOver255f * uLeftDistance * vTopDistance;

      Color c = new Color(r, g, b);

      return c;
   }
}
