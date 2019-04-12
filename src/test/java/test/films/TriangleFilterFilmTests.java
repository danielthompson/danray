package test.films;

import net.danielthompson.danray.films.AbstractFilm;
import net.danielthompson.danray.films.TriangleFilterFilm;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.awt.image.BufferedImage;

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
      BufferedImage image = new BufferedImage(2, 2, TYPE_INT_RGB);
      AbstractFilm film = new TriangleFilterFilm(image);

      film.AddSamples(1, 1, );

   }
}
