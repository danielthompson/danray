package test.imports;

import net.danielthompson.danray.imports.SPDFileImporter;
import net.danielthompson.danray.imports.WavefrontObjectImporter;
import net.danielthompson.danray.shading.SpectralPowerDistribution;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;

/**
 * User: daniel
 * Date: 7/13/13
 * Time: 17:57
 */
public class SPDFileImportTests {
   @BeforeMethod
   public void setUp() throws Exception {

   }

   @AfterMethod
   public void tearDown() throws Exception {

   }

   @Test
   public void testImport1() throws Exception {
      SPDFileImporter importer = new SPDFileImporter(new File("spds/d65.spd"));

      SpectralPowerDistribution spd = importer.Process();

      System.err.println(spd.Power);

      for (int i = 0; i < spd.Buckets.length; i++) {
         System.err.println(spd.Buckets[i]);
      }

   }
}
