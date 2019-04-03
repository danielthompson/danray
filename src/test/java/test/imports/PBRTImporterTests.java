package test.imports;

import net.danielthompson.danray.imports.PBRTImporter;
import net.danielthompson.danray.imports.WavefrontObjectImporter;
import net.danielthompson.danray.scenes.AbstractScene;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;

/**
 * User: daniel
 * Date: 7/13/13
 * Time: 17:57
 */
public class PBRTImporterTests {
   @BeforeMethod
   public void setUp() throws Exception {

   }

   @AfterMethod
   public void tearDown() throws Exception {

   }

   @Test
   public void testImport1() throws Exception {
      File file = new File("src/test/resources/pbrt-scenes/example.pbrt");
      PBRTImporter importer = new PBRTImporter(file);
      AbstractScene scene = importer.Process();
   }
}
