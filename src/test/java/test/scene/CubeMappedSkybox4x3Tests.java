package test.scene;

import net.danielthompson.danray.SceneBuilder;
import net.danielthompson.danray.scenes.skyboxes.CubeMappedSkybox;
import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.structures.Bitmap;
import net.danielthompson.danray.structures.Vector3;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.awt.image.BufferedImage;

public class CubeMappedSkybox4x3Tests {

   private void assertEquals(Bitmap actual, Bitmap expected) {
      Assert.assertNotNull(actual);
      Assert.assertNotNull(expected);

      Assert.assertEquals(actual.width, expected.width);
      Assert.assertEquals(actual.height, actual.width);

      for (int x = 0; x <= 2; x++) {
         for (int y = 0; y <= 2; y++) {
            int actualR = actual.getRed(x, y);
            int expectedR = expected.getRed(x, y);
            Assert.assertEquals(actualR, expectedR);

            int actualG = actual.getGreen(x, y);
            int expectedG = expected.getGreen(x, y);
            Assert.assertEquals(actualG, expectedG);

            int actualB = actual.getBlue(x, y);
            int expectedB = expected.getBlue(x, y);
            Assert.assertEquals(actualB, expectedB);
         }
      }
   }

   @Test
   public void testNegX() {
      BufferedImage image = SceneBuilder.Skyboxes.Load(SceneBuilder.Skyboxes.PrimaryColors4x3);
      CubeMappedSkybox skybox = new CubeMappedSkybox(image, 1);
      Assert.assertNotNull(skybox);

      Bitmap actual = skybox.negX;
      Assert.assertNotNull(actual);

      Bitmap expected = new Bitmap(3, 3);
      expected.set(0, 0, 0, 170, 170);
      expected.set(0, 1, 0, 255, 0);
      expected.set(0, 2, 85, 170, 170);

      expected.set(1, 0, 0, 0, 255);
      expected.set(1, 1, 0, 255, 255);
      expected.set(1, 2, 255, 0, 255);

      expected.set(2, 0, 85, 85, 170);
      expected.set(2, 1, 255, 0, 0);
      expected.set(2, 2, 170, 85, 170);

      assertEquals(actual, expected);
   }

   @Test
   public void testNegZ() {
      BufferedImage image = SceneBuilder.Skyboxes.Load(SceneBuilder.Skyboxes.PrimaryColors4x3);
      CubeMappedSkybox skybox = new CubeMappedSkybox(image, 1);
      Assert.assertNotNull(skybox);

      Bitmap actual = skybox.negZ;
      Assert.assertNotNull(actual);

      Bitmap expected = new Bitmap(3, 3);
      expected.set(0, 0, 85, 85, 170);
      expected.set(0, 1, 0, 255, 255);
      expected.set(0, 2, 170, 85, 170);

      expected.set(1, 0, 0, 0, 255);
      expected.set(1, 1, 255, 0, 0);
      expected.set(1, 2, 255, 0, 255);

      expected.set(2, 0, 170, 85, 85);
      expected.set(2, 1, 255, 255, 0);
      expected.set(2, 2, 255, 85, 85);

      assertEquals(actual, expected);
   }

   @Test
   public void testPosX() {
      BufferedImage image = SceneBuilder.Skyboxes.Load(SceneBuilder.Skyboxes.PrimaryColors4x3);
      CubeMappedSkybox skybox = new CubeMappedSkybox(image, 1);
      Assert.assertNotNull(skybox);

      Bitmap actual = skybox.posX;
      Assert.assertNotNull(actual);

      Bitmap expected = new Bitmap(3, 3);
      expected.set(0, 0, 170, 85, 85);
      expected.set(0, 1, 255, 0, 0);
      expected.set(0, 2, 255, 85, 85);

      expected.set(1, 0, 0, 0, 255);
      expected.set(1, 1, 255, 255, 0);
      expected.set(1, 2, 255, 0, 255);

      expected.set(2, 0, 85, 170, 85);
      expected.set(2, 1, 0, 255, 0);
      expected.set(2, 2, 170, 170, 85);

      assertEquals(actual, expected);
   }

   @Test
   public void testPosZ() {
      BufferedImage image = SceneBuilder.Skyboxes.Load(SceneBuilder.Skyboxes.PrimaryColors4x3);
      CubeMappedSkybox skybox = new CubeMappedSkybox(image, 1);
      Assert.assertNotNull(skybox);

      Bitmap actual = skybox.posZ;
      Assert.assertNotNull(actual);

      Bitmap expected = new Bitmap(3, 3);
      expected.set(0, 0, 85, 170, 85);
      expected.set(0, 1, 255, 255, 0);
      expected.set(0, 2, 170, 170, 85);

      expected.set(1, 0, 0, 0, 255);
      expected.set(1, 1, 0, 255, 0);
      expected.set(1, 2, 255, 0, 255);

      expected.set(2, 0, 0, 170, 170);
      expected.set(2, 1, 0, 255, 255);
      expected.set(2, 2, 85, 170, 170);

      assertEquals(actual, expected);
   }

   @Test
   public void testPosY() {
      BufferedImage image = SceneBuilder.Skyboxes.Load(SceneBuilder.Skyboxes.PrimaryColors4x3);
      CubeMappedSkybox skybox = new CubeMappedSkybox(image, 1);
      Assert.assertNotNull(skybox);

      Bitmap actual = skybox.posY;
      Assert.assertNotNull(actual);

      Bitmap expected = new Bitmap(3, 3);
      expected.set(0, 0, 0, 170, 170);
      expected.set(0, 1, 0, 255, 255);
      expected.set(0, 2, 85, 85, 170);

      expected.set(1, 0, 0, 255, 0);
      expected.set(1, 1, 0, 0, 255);
      expected.set(1, 2, 255, 0, 0);

      expected.set(2, 0, 85, 170, 85);
      expected.set(2, 1, 255, 255, 0);
      expected.set(2, 2, 170, 85, 85);

      assertEquals(actual, expected);
   }

   @Test
   public void testNegY() {
      BufferedImage image = SceneBuilder.Skyboxes.Load(SceneBuilder.Skyboxes.PrimaryColors4x3);
      CubeMappedSkybox skybox = new CubeMappedSkybox(image, 1);
      Assert.assertNotNull(skybox);

      Bitmap actual = skybox.negY;
      Assert.assertNotNull(actual);

      Bitmap expected = new Bitmap(3, 3);
      expected.set(0, 0, 170, 85, 170);
      expected.set(0, 1, 0, 255, 255);
      expected.set(0, 2, 85, 170, 170);

      expected.set(1, 0, 255, 0, 0);
      expected.set(1, 1, 255, 0, 255);
      expected.set(1, 2, 0, 255, 0);

      expected.set(2, 0, 255, 85, 85);
      expected.set(2, 1, 255, 255, 0);
      expected.set(2, 2, 170, 170, 85);

      assertEquals(actual, expected);
   }

   @Test
   public void testRayNegZBlur0() {
      BufferedImage image = SceneBuilder.Skyboxes.Load(SceneBuilder.Skyboxes.PrimaryColors4x3);
      CubeMappedSkybox skybox = new CubeMappedSkybox(image);
      Assert.assertNotNull(skybox);

      SpectralPowerDistribution expected = new SpectralPowerDistribution(1.0f, 0.0f, 0.0f);
      SpectralPowerDistribution actual = skybox.getSPD(new Vector3(0, 0, -1));
      Assert.assertNotNull(actual);

      Assert.assertEquals(actual.R, expected.R);
      Assert.assertEquals(actual.G, expected.G);
      Assert.assertEquals(actual.B, expected.B);
   }

   @Test
   public void testRayNegZBlur1() {
      BufferedImage image = SceneBuilder.Skyboxes.Load(SceneBuilder.Skyboxes.PrimaryColors4x3);
      CubeMappedSkybox skybox = new CubeMappedSkybox(image, 1);
      Assert.assertNotNull(skybox);

      SpectralPowerDistribution expected = new SpectralPowerDistribution(1.0f, 0.0f, 0.0f);
      SpectralPowerDistribution actual = skybox.getSPD(new Vector3(0, 0, -1));
      Assert.assertNotNull(actual);

      Assert.assertEquals(actual.R, expected.R);
      Assert.assertEquals(actual.G, expected.G);
      Assert.assertEquals(actual.B, expected.B);
   }
}
