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
import java.awt.image.SampleModel;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class TriangleFilterFilmTests {
   @BeforeMethod
   public void setUp() throws Exception {

   }

   @AfterMethod
   public void tearDown() throws Exception {

   }

   @Test
   public void testTriangleFilm1() {
      BufferedImage image = new BufferedImage(3, 3, TYPE_INT_RGB);
      AbstractFilm film = new TriangleFilterFilm(image);

      Sample sample = new Sample();
      sample.SpectralPowerDistribution = new SpectralPowerDistribution();
      sample.SpectralPowerDistribution.R = 1.0f;
      sample.SpectralPowerDistribution.G = 1.0f;
      sample.SpectralPowerDistribution.B = 1.0f;

      Sample[] samples = new Sample[] {sample};

      film.AddSamples(1.5f, 1.5f, samples);

      Color color = new Color(film.Image.getRGB(1, 1));

      Assert.assertEquals(color, new Color(255, 255, 255));
   }
}
