package test.imports.pbrt;

import net.danielthompson.danray.imports.PBRTImporter;
import net.danielthompson.danray.imports.pbrt.directives.CameraDirective;
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
   public void testImport1() {
      String s = "Camera \"perspective\" \"float fov\" 45";

      PBRTImporter importer = new PBRTImporter(null);
      List<List<String>> listOfWords = importer.Lex(s);

      Assert.assertNotNull(listOfWords);
      Assert.assertEquals(1, listOfWords.size());

      List<String> words = listOfWords.get(0);

      Assert.assertNotNull(words);
      Assert.assertEquals(10, words.size());

      Assert.assertEquals("Camera", words.get(0));
      Assert.assertEquals("\"", words.get(1));
      Assert.assertEquals("perspective", words.get(2));
      Assert.assertEquals("\"", words.get(3));
      Assert.assertEquals("\"", words.get(4));
      Assert.assertEquals("float", words.get(5));
      Assert.assertEquals("fov", words.get(6));
      Assert.assertEquals("\"", words.get(7));
      Assert.assertEquals("45", words.get(8));
   }

   @Test
   public void testImportSingleArgument() {
      String s = "Camera \"perspective\" \"float fov\" 45";

      PBRTImporter importer = new PBRTImporter(null);
      List<List<String>> listOfWords = importer.Lex(s);

      List<String> words = listOfWords.get(0);
      CameraDirective directive = new CameraDirective(words);
   }

   @Test
   public void testImportDuplicateArgument() {
      String s = "Camera \"perspective\" \"float fov\" 45 \"float fov\" 45";

      PBRTImporter importer = new PBRTImporter(null);
      List<List<String>> listOfWords = importer.Lex(s);

      List<String> words = listOfWords.get(0);
      CameraDirective directive = new CameraDirective(words);
   }

   @Test
   public void testImportMultiplesArgument() {
      String s = "Camera \"perspective\" \"float fov\" 45 \"float fov\" 45";

      PBRTImporter importer = new PBRTImporter(null);
      List<List<String>> listOfWords = importer.Lex(s);

      List<String> words = listOfWords.get(0);
      CameraDirective directive = new CameraDirective(words);
   }
}
