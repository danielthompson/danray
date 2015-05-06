package test.imports;

import net.danielthompson.danray.imports.WavefrontObjectImporter;
import net.danielthompson.danray.shapes.TriangleMesh;
import net.danielthompson.danray.structures.Point;
import org.testng.annotations.*;

import java.io.File;
import java.util.List;

/**
 * User: daniel
 * Date: 7/13/13
 * Time: 17:57
 */
public class WavefrontObjectImporterTests {
   @BeforeMethod
   public void setUp() throws Exception {

   }

   @AfterMethod
   public void tearDown() throws Exception {

   }

   @Test
   public void testImport1() throws Exception {
      WavefrontObjectImporter importer = new WavefrontObjectImporter(new File("models/diamond.obj"));

   }
}
