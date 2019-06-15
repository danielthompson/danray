package net.danielthompson.danray.scenes.skyboxes;

import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.shapes.Box;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.*;
import net.danielthompson.danray.structures.Point3;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class CubeMappedSkybox extends AbstractSkybox {

   private final SpectralPowerDistribution nanColor = new SpectralPowerDistribution(Color.magenta);

   public Bitmap negX;
   public Bitmap negY;
   public Bitmap negZ;
   public Bitmap posX;
   public Bitmap posY;
   public Bitmap posZ;

   private final Point3 center = new Point3(0.5f, 0.5f, 0.5f);

   private final Box box;

   private final int tileHeight;
   private final int tileWidth;
   private final int blurWidth;

   public CubeMappedSkybox(final BufferedImage image, final int blurWidth) {
      this(image, new Transform[] {Transform.identity, Transform.identity}, blurWidth);
   }

   public CubeMappedSkybox(final BufferedImage image, final Transform[] transforms, final int blurWidth) {
      box = new Box(transforms,null);

      tileWidth = image.getWidth() / 4;
      tileHeight = image.getHeight() / 3;

      this.blurWidth = blurWidth;

      // assumes cubemap is tiled like
      //    +Y
      // -X -Z +X +Z
      //    -Y

      // case 1 - no blur

      if (blurWidth <= 0) {
         negX = new Bitmap(tileWidth, tileHeight);
         BufferedImage temp = image.getSubimage(0, tileHeight, tileWidth, tileHeight);
         negX.set(temp, 0, 0, false);

         negZ = new Bitmap(tileWidth, tileHeight);
         temp = image.getSubimage(tileWidth, tileHeight, tileWidth, tileHeight);
         negZ.set(temp, 0, 0, false);

         posX = new Bitmap(tileWidth, tileHeight);
         temp = image.getSubimage(tileWidth * 2, tileHeight, tileWidth, tileHeight);
         posX.set(temp, 0, 0, false);

         posZ = new Bitmap(tileWidth, tileHeight);
         temp = image.getSubimage(tileWidth * 3, tileHeight, tileWidth, tileHeight);
         posZ.set(temp, 0, 0, false);

         posY = new Bitmap(tileWidth, tileHeight);
         temp = image.getSubimage(tileWidth, 0, tileWidth, tileHeight);
         posY.set(temp, 0, 0, false);

         negY = new Bitmap(tileWidth, tileHeight);
         temp = image.getSubimage(tileWidth, tileHeight * 2, tileWidth, tileHeight);
         negY.set(temp, 0, 0, false);
      }
      else {

         // neg X
         {
            // main image
            BufferedImage temp = image.getSubimage(0, tileHeight, tileWidth, tileHeight);
            negX = new Bitmap(tileWidth + 2, tileHeight + 2);
            negX.set(temp, 1, 1, false);
            //export(negX, "negx1.png");

            // add left col of +Y to the top of this tile
            temp = image.getSubimage(tileWidth, 0, 1, tileHeight);
            negX.set(temp, 1, 0, true);
            //export(negX, "negx2.png");

            // add right col of +Z to the left of this tile
            temp = image.getSubimage(image.getWidth() - 1, tileHeight, 1, tileHeight);
            negX.set(temp, 0, 1, false);
            //export(negX, "negx3.png");

            // add left col of -Z to the right of this tile
            temp = image.getSubimage(tileWidth, tileHeight, 1, tileHeight);
            negX.set(temp, tileWidth + 1, 1, false);
            //export(negX, "negx4.png");

            // add left col of -Y to the bottom of this tile
            temp = image.getSubimage(tileWidth, tileHeight * 2, 1, tileHeight);
            negX.set(temp, 1, tileHeight + 1, true);
            //export(negX, "negx5.png");

            average(negX);
            export(negX, "negXAverage.png");
         }

         // negZ
         {
            // main image
            BufferedImage temp = image.getSubimage(tileWidth - 1, tileHeight - 1, tileWidth + 2, tileHeight + 2);
            negZ = new Bitmap(tileWidth + 2, tileHeight + 2);
            negZ.set(temp, 0, 0, false);
            //export(negZ, "negz1.png");

            average(negZ);
            export(negZ, "negZAverage.png");
         }

         // posX
         {
            // main image
            BufferedImage temp = image.getSubimage(tileWidth * 2 - 1, tileHeight, tileWidth + 2, tileHeight);
            posX = new Bitmap(tileWidth + 2, tileHeight + 2);
            posX.set(temp, 0, 1, false);
            //export(posX, "posX1.png");

            // add right col of +Y to the top of this tile
            temp = image.getSubimage(tileWidth * 2 - 1, 0, 1, tileHeight);
            posX.set(temp, 1, 0, true);
            //export(posX, "posX2.png");

            // add left col of -Y to the bottom of this tile
            temp = image.getSubimage(tileWidth * 2 - 1, tileHeight * 2, 1, tileHeight);
            posX.set(temp, 1, tileHeight + 1, true);
            //export(posX, "posX5.png");

            average(posX);
            export(posX, "posXAverage.png");
         }

         // posZ
         {
            // main image
            BufferedImage temp = image.getSubimage(tileWidth * 3 - 1, tileHeight, tileWidth + 1, tileHeight);
            posZ = new Bitmap(tileWidth + 2, tileHeight + 2);
            posZ.set(temp, 0, 1, false);
            //export(posZ, "posZ1.png");

            // add top row of +Y to the top of this tile
            temp = image.getSubimage(tileWidth, 0, tileWidth, 1);
            posZ.set(temp, 1, 0, false);
            //export(posZ, "posZ2.png");

            // add left col of -X to the right of this tile
            temp = image.getSubimage(0, tileHeight, 1, tileHeight);
            posZ.set(temp, tileWidth + 1, 1, false);
            //export(posZ, "posZ4.png");

            // add bottom row of -Y to the bottom of this tile
            temp = image.getSubimage(tileWidth, tileHeight * 3 - 1, tileWidth, 1);
            posZ.set(temp, 1, tileHeight + 1, false);
            //export(posZ, "posZ5.png");

            average(posZ);
            export(posZ, "posZAverage.png");
         }

         // posY
         {
            // main image
            BufferedImage temp = image.getSubimage(tileWidth, 0, tileWidth, tileHeight + 1);
            posY = new Bitmap(tileWidth + 2, tileHeight + 2);
            posY.set(temp, 1, 1, false);
            //export(posY, "posY1.png");

            // add top row of +Z to the top of this tile
            temp = image.getSubimage(tileWidth * 3, tileHeight, tileWidth, 1);
            posY.set(temp, 1, 0, false);
            //export(posY, "posY2.png");

            // add top row of -X to the left of this tile
            temp = image.getSubimage(0, tileHeight, tileWidth, 1);
            posY.set(temp, 0, 1, true);
            //export(posY, "posY4.png");

            // add top row of +X to the right of this tile
            temp = image.getSubimage(tileWidth * 2, tileHeight, tileWidth, 1);
            posY.set(temp, tileWidth + 1, 1, true);
            //export(posY, "posY5.png");

            average(posY);
            export(posY, "posYAverage.png");
         }

         // negY
         {
            // main image and top row
            BufferedImage temp = image.getSubimage(tileWidth, tileHeight * 2 - 1, tileWidth, tileHeight + 1);
            negY = new Bitmap(tileWidth + 2, tileHeight + 2);
            negY.set(temp, 1, 0, false);
            //export(negY, "negY1.png");

            // add bottom row of +Z to the bottom of this tile
            temp = image.getSubimage(tileWidth * 3, tileHeight * 2 - 1, tileWidth, 1);
            negY.set(temp, 1, tileWidth + 1, false);
            //export(negY, "negY2.png");

            // add bottom row of -X to the left of this tile
            temp = image.getSubimage(0, tileHeight * 2 - 1, tileWidth, 1);
            negY.set(temp, 0, 1, true);
            //export(negY, "negY4.png");

            // add bottom row of +X to the right of this tile
            temp = image.getSubimage(tileWidth * 2, tileHeight * 2 - 1, tileWidth, 1);
            negY.set(temp, tileWidth + 1, 1, true);
            //export(negY, "negY5.png");

            average(negY);
            export(negY, "negYAverage.png");
         }
      }
   }

   private void average(Bitmap bitmap) {
      // top left average
      {
         int average = bitmap.average(0, 0, 2);
         bitmap.set(0, 0, average);
      }

      // top right average
      {
         int average = bitmap.average(tileWidth, 0, 2);
         bitmap.set(tileWidth + 1, 0, average);
      }

      // bottom left average
      {
         int average = bitmap.average(0, tileHeight, 2);
         bitmap.set(0, tileHeight + 1, average);
      }

      // bottom right average
      {
         int average = bitmap.average(tileWidth, tileHeight, 2);
         bitmap.set(tileWidth + 1, tileHeight + 1, average);
      }
   }

   private void export(Bitmap bitmap, String filename) {
      try {
         ImageIO.write(bitmap.export(), "png", new File(filename));
      }
      catch (IOException e) {

      }
   }

   public CubeMappedSkybox(final BufferedImage image) {
      this(image, new Transform[] { Transform.identity, Transform.identity}, 0);
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

      Bitmap texture = null;

      // assumes cubemap is tiled like
      //    +Y
      // -X -Z +X +Z
      //    -Y

      // left wall
      if (Constants.WithinEpsilon(p.x, 0)) {
         texture = negX;
         u = tileWidth * (1.0f - p.z);
         v = tileHeight * (1.0f - p.y);
      }

      // back wall
      else if (Constants.WithinEpsilon(p.z, 0)) {
         texture = negZ;
         u = tileWidth * p.x;
         v = tileHeight * (1.0f - p.y);
      }

      // right wall
      else if (Constants.WithinEpsilon(p.x, 1)) {
         texture = posX;
         u = tileWidth * p.z;
         v = tileHeight * (1.0f - p.y);
      }

      // front wall
      else if (Constants.WithinEpsilon(p.z, 1)) {
         texture = posZ;
         u = tileWidth * (1.0f - p.x);
         v = tileHeight * (1.0f - p.y);
      }

      // top wall
      else if (Constants.WithinEpsilon(p.y, 1)) {
         texture = posY;
         u = tileWidth * p.x;
         v = tileHeight * (1.0f - p.z);
      }

      // bottom wall
      else {
         texture = negY;
         u = tileWidth * p.x;
         v = tileHeight * p.z;
      }

      u = (u >= tileWidth) ? Math.nextDown(tileWidth)  : u;
      v = (v >= tileHeight) ? Math.nextDown(tileHeight) : v;

      // adjust for blurWidth
      u += blurWidth;
      v += blurWidth;

      u = (u >= tileWidth + blurWidth) ? Math.nextDown(tileWidth + blurWidth)  : u;
      v = (v >= tileHeight + blurWidth) ? Math.nextDown(tileHeight + blurWidth) : v;

      final SpectralPowerDistribution spd;

      if (blurWidth <= 0) {
         spd = nearestNeighbor(texture, u, v);
      }
      else {
         spd = lerp2(texture, u - 0.5f, v - 0.5f);
      }

      return spd;
   }

   /**
    * Returns the color of the top-left texel for (u, v).
    * @param texture
    * @param u
    * @param v
    * @return
    */
   private SpectralPowerDistribution nearestNeighbor(final Bitmap texture, final float u, final float v) {
      try {
         final float r = texture.getRed((int)u, (int)v) * Constants.OneOver255f;
         final float g = texture.getGreen((int)u, (int)v) * Constants.OneOver255f;
         final float b = texture.getBlue((int)u, (int)v) * Constants.OneOver255f;

         SpectralPowerDistribution spd = new SpectralPowerDistribution(r, g, b);
         return spd;

      }
      catch (ArrayIndexOutOfBoundsException e) {
         throw e;
      }

   }

   /**
    * Returns the average of the 4 texel values nearest to (u, v), weighted by distance
    * @param texture
    * @param u
    * @param v
    * @return
    */
   private Color lerp2loop(final BufferedImage texture, final float u, final float v) {
      // TODO 1. needs to properly handle u/v values on texture boundaries
      // TODO 2. needs to properly handle u/v exactly on a texel (i.e. (1.0f, 1.0f) should not lerp)
      float r = 0, g = 0, b = 0;

      final int iSamples = 2;
      final int jSamples = 2;

      final float halfISamples = iSamples / 2.0f;
      final float halfJSamples = jSamples / 2.0f;

      final int uFloor = (int)u - ((iSamples - 1) / 2);
      final int vFloor = (int)v - ((jSamples - 1) / 2);

      final int iBase = uFloor + iSamples;
      final int jBase = vFloor + jSamples;

      for (int i = 0; i < iSamples; i++) {
         final int iIndex = iBase - i;
         final float iDist = (halfISamples - Math.abs(i + uFloor - u)) / halfISamples;
         for (int j = 0; j < jSamples; j++) {
            final int jIndex = jBase - j;
            final float jDist = (halfJSamples - Math.abs(j + vFloor - v)) / halfJSamples;
            final Color texel = new Color(texture.getRGB(iIndex, jIndex));
            final float factor = Constants.OneOver255f * iDist * jDist;
            //Logger.Log(Logger.Level.Info, "factor: " + factor);
            r += texel.getRed() * factor;
            g += texel.getGreen() * factor;
            b += texel.getBlue() * factor;
         }
      }

      //Logger.Flush();

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

   /**
    * Returns the average of the 4 texel values nearest to (u, v), weighted by distance
    * @param texture
    * @param u
    * @param v
    * @return
    */
   private SpectralPowerDistribution lerp2(final Bitmap texture, final float u, final float v) {
      // TODO 1. needs to properly handle u/v values on texture boundaries
      // TODO 2. needs to properly handle u/v exactly on a texel (i.e. (1.0f, 1.0f) should not lerp)
      float r = 0, g = 0, b = 0;

      final int uFloor = (int)u;
      final int vFloor = (int)v;

      final float uLeftDistance = u - uFloor;
      final float uRightDistance = 1.0f - uLeftDistance;
      final float vTopDistance = v - vFloor;
      final float vBottomDistance = 1.0f - vTopDistance;

      // top left
      float factor = Constants.OneOver255f * uRightDistance * vBottomDistance;
      //Logger.Log(Logger.Level.Info, "factor: " + factor);
      r += texture.getRed(uFloor, vFloor) * factor;
      g += texture.getGreen(uFloor, vFloor) * factor;
      b += texture.getBlue(uFloor, vFloor) * factor;

      // bottom left
      factor = Constants.OneOver255f * uRightDistance * vTopDistance;
      //Logger.Log(Logger.Level.Info, "factor: " + factor);
      r += texture.getRed(uFloor, vFloor + 1) * factor;
      g += texture.getGreen(uFloor, vFloor + 1) * factor;
      b += texture.getBlue(uFloor, vFloor + 1) * factor;

      factor = Constants.OneOver255f * uLeftDistance * vBottomDistance;
      //Logger.Log(Logger.Level.Info, "factor: " + factor);
      r += texture.getRed(uFloor + 1, vFloor) * factor;
      g += texture.getGreen(uFloor + 1, vFloor) * factor;
      b += texture.getBlue(uFloor + 1, vFloor) * factor;

      factor = Constants.OneOver255f * uLeftDistance * vTopDistance;
      //Logger.Log(Logger.Level.Info, "factor: " + factor);
      r += texture.getRed(uFloor + 1, vFloor + 1) * factor;
      g += texture.getGreen(uFloor + 1, vFloor + 1) * factor;
      b += texture.getBlue(uFloor + 1, vFloor + 1) * factor;

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

      final SpectralPowerDistribution spd = new SpectralPowerDistribution(r, g, b);

      return spd;
   }
}
