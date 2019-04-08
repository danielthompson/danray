package test.imports.pbrt;

import net.danielthompson.danray.imports.PBRTImporter;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class CameraDirectiveTests {
   @BeforeMethod
   public void setUp() throws Exception {

   }

   @AfterMethod
   public void tearDown() throws Exception {

   }

   @Test
   public void testImportExample() {
      String s = "Camera \"perspective\" \"float fov\" 45";

      PBRTImporter importer = new PBRTImporter(null);
      List<List<String>> listOfWords = importer.Lex(s);

      Assert.assertNotNull(listOfWords);
      Assert.assertEquals(1, listOfWords.size());

      List<String> words = listOfWords.get(0);

      Assert.assertNotNull(words);
      Assert.assertEquals(10, words.size());

      Assert.assertEquals("LookAt", words.get(0));
      Assert.assertEquals("3", words.get(1));
      Assert.assertEquals("4", words.get(2));
      Assert.assertEquals("1.5", words.get(3));
      Assert.assertEquals(".5", words.get(4));
      Assert.assertEquals(".5", words.get(5));
      Assert.assertEquals("0", words.get(6));
      Assert.assertEquals("0", words.get(7));
      Assert.assertEquals("0", words.get(8));
      Assert.assertEquals("1", words.get(9));
   }
}
