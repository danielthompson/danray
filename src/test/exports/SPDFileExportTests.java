package test.exports;

import net.danielthompson.danray.exports.SPDFileExporter;
import net.danielthompson.danray.shading.RelativeSpectralPowerDistributionLibrary;
import net.danielthompson.danray.shading.SpectralPowerDistribution;
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

      SpectralPowerDistribution spd = RelativeSpectralPowerDistributionLibrary.D65.getSPD();
      File file = new File("spds/test.xml");

      SPDFileExporter exporter = new SPDFileExporter(spd, file);

      StreamResult result = exporter.Process();
   }
}
