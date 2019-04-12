package test.films;

import net.danielthompson.danray.films.AbstractFilm;
import net.danielthompson.danray.films.TriangleFilterFilm;
import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.structures.Sample;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class TriangleFilterFilmTests {

   private BufferedImage _bufferedImage;
   private AbstractFilm _film;
   private Sample _sample;
   private Sample[] _samples;

   @BeforeMethod
   public void setUp() throws Exception {
      _bufferedImage = new BufferedImage(3, 3, TYPE_INT_RGB);
      _film = new TriangleFilterFilm(_bufferedImage);
      _sample = new Sample();
      _sample.SpectralPowerDistribution = new SpectralPowerDistribution();
      _sample.SpectralPowerDistribution.R = 1.0f;
      _sample.SpectralPowerDistribution.G = 1.0f;
      _sample.SpectralPowerDistribution.B = 1.0f;
      _samples = new Sample[] {_sample};
   }

   @AfterMethod
   public void tearDown() throws Exception {

   }

   @Test
   public void testTriangleFilm1() {
      _film.AddSamples(1.5f, 1.5f, _samples);

      Color color = new Color(_film.Image.getRGB(1, 1));

      Assert.assertEquals(color, new Color(255, 255, 255));

   }

   @Test
   public void testTriangleFilm2() {
      _film.AddSamples(2.0f, 2.0f, _samples);

      Color colorLeft = new Color(_film.Image.getRGB(1, 1));
      Color colorRight = new Color(_film.Image.getRGB(2, 1));

      Assert.assertEquals(colorLeft, new Color(128, 128, 128));
      Assert.assertEquals(colorRight, new Color(128, 128, 128));
   }
}
