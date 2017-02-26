package test.exports;

import net.danielthompson.danray.exports.SPDFileExporter;
import net.danielthompson.danray.shading.fullspectrum.RelativeFullSpectralPowerDistributionLibrary;
import net.danielthompson.danray.shading.fullspectrum.FullSpectralPowerDistribution;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.xml.transform.stream.StreamResult;
import java.io.File;

/**
 * User: daniel
 * Date: 7/13/13
 * Time: 17:57
 */
public class SPDFileExportTests {
   @BeforeMethod
   public void setUp() throws Exception {

   }

   @AfterMethod
   public void tearDown() throws Exception {

   }

   @Test
   public void testExport() throws Exception {

      FullSpectralPowerDistribution spd = RelativeFullSpectralPowerDistributionLibrary.D65.getSPD();
      File file = new File("src/test/resources/spds/test.xml");

      SPDFileExporter exporter = new SPDFileExporter(spd, file);

      StreamResult result = exporter.Process();
   }
}
