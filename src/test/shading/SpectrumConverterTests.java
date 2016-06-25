package test.shading;

import net.danielthompson.danray.exports.internal.IExporter;
import net.danielthompson.danray.exports.internal.SPDExporter;
import net.danielthompson.danray.exports.internal.UnitTestExporter;
import net.danielthompson.danray.shading.fullspectrum.FullSpectralBlender;
import net.danielthompson.danray.shading.fullspectrum.FullSpectralPowerDistribution;
import net.danielthompson.danray.shading.fullspectrum.FullSpectrum;
import net.danielthompson.danray.shading.fullspectrum.RelativeFullSpectralPowerDistributionLibrary;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.*;
import java.io.File;

/**
 * Created by daniel on 4/3/16.
 */
public class SpectrumConverterTests {
   @BeforeMethod
   public void setUp() throws Exception {

   }

   @AfterMethod
   public void tearDown() throws Exception {

   }

   @Test
   public void testConvertRed() throws Exception {

      FullSpectralPowerDistribution spd = FullSpectrum.ConvertFromRGB(1.0f, 0.0f, 0.0f);

      Color output = FullSpectralBlender.ConvertSPDtoRGB(spd);

      System.out.println(output);
      System.out.println("");
   }

   @Test
   public void testConvertGreen() throws Exception {

      FullSpectralPowerDistribution spd = FullSpectrum.ConvertFromRGB(0.0f, 1.0f, 0.0f);

      Color output = FullSpectralBlender.ConvertSPDtoRGB(spd);

      System.out.println(output);
      System.out.println("");
   }

   @Test
   public void testConvertBlue() throws Exception {

      FullSpectralPowerDistribution spd = FullSpectrum.ConvertFromRGB(0.0f, 0.0f, 1.0f);

      Color output = FullSpectralBlender.ConvertSPDtoRGB(spd);

      System.out.println(output);
      System.out.println("");
   }

   public FullSpectralPowerDistribution ResetRed() {
      FullSpectralPowerDistribution currentRed = new FullSpectralPowerDistribution();
      currentRed.add(RelativeFullSpectralPowerDistributionLibrary.Constant.getSPD());
      currentRed.scale(.1);
      currentRed.add(RelativeFullSpectralPowerDistributionLibrary.Red.getSPD());
      return currentRed;
   }

   public FullSpectralPowerDistribution ResetGreen() {
      FullSpectralPowerDistribution currentRed = new FullSpectralPowerDistribution();
      currentRed.add(RelativeFullSpectralPowerDistributionLibrary.Constant.getSPD());
      currentRed.scale(.1);
      currentRed.add(RelativeFullSpectralPowerDistributionLibrary.Green.getSPD());
      return currentRed;
   }

   public FullSpectralPowerDistribution ResetBlue() {
      FullSpectralPowerDistribution currentRed = new FullSpectralPowerDistribution();
      currentRed.add(RelativeFullSpectralPowerDistributionLibrary.Constant.getSPD());
      currentRed.scale(.1);
      currentRed.add(RelativeFullSpectralPowerDistributionLibrary.Blue.getSPD());
      return currentRed;
   }

   @Test
   public void testConvertRandom() throws Exception {

      FullSpectralPowerDistribution currentRed = ResetRed();
      FullSpectralPowerDistribution currentGreen = ResetGreen();
      FullSpectralPowerDistribution currentBlue = ResetBlue();

      FullSpectralPowerDistribution newRed = RelativeFullSpectralPowerDistributionLibrary.Red.getSPD();
      FullSpectralPowerDistribution newGreen = RelativeFullSpectralPowerDistributionLibrary.Green.getSPD();
      FullSpectralPowerDistribution newBlue = RelativeFullSpectralPowerDistributionLibrary.Blue.getSPD();

      float match = 0.0f;

      Color[] inputs = new Color[10];
      Color[] currentOutputs = new Color[inputs.length];
      Color[] newOutputs = new Color[inputs.length];
      float[] currentPercentMatches = new float[inputs.length];
      float[] newPercentMatches = new float[inputs.length];

      float[] redChange = new float[currentRed.Buckets.length];
      float[] greenChange = new float[currentGreen.Buckets.length];
      float[] blueChange = new float[currentBlue.Buckets.length];

      for (int i = 0; i < inputs.length; i++) {
         inputs[i] = new Color((float) Math.random(), (float) Math.random(), (float) Math.random());

      }

      // determine colors

      float currentPercentMatch = 0.0f;
      float newPercentMatch = 0.0f;

      int iterations = 0;
      while (currentPercentMatch / (float) (inputs.length) < .95f) {
         // iterations++;
         // calculate current output with all 10 colors
         for (int n = 0; n < redChange.length; n++) {
            redChange[n] = 1.0f + ((float) (Math.random() - .5f) * .3f);
            greenChange[n] = 1.0f + ((float) (Math.random() - .5f) * .3f);
            blueChange[n] = 1.0f + ((float) (Math.random() - .5f) * .3f);
         }

         for (int j = 0; j < inputs.length; j++) {
            final float r = inputs[j].getRed() / 255.f;
            final float g = inputs[j].getGreen() / 255.f;
            final float b = inputs[j].getBlue() / 255.f;

            FullSpectralPowerDistribution currentSpd = FullSpectrum.ConvertFromRGB(r, currentRed, g, currentGreen, b, currentBlue);
            currentOutputs[j] = FullSpectralBlender.ConvertSPDtoRGB(currentSpd);
            currentPercentMatches[j] = PercentMatch(currentOutputs[j], inputs[j]);

            newRed = FullSpectralPowerDistribution.scale(currentRed, 1);
            newGreen = FullSpectralPowerDistribution.scale(currentGreen, 1);
            newBlue = FullSpectralPowerDistribution.scale(currentBlue, 1);

            for (int n = 0; n < newRed.Buckets.length; n++) {
               newRed.Buckets[n] *= redChange[n];
            }
            for (int n = 0; n < newGreen.Buckets.length; n++) {
               newGreen.Buckets[n] *= greenChange[n];
            }
            for (int n = 0; n < newBlue.Buckets.length; n++) {
               newBlue.Buckets[n] *= blueChange[n];
            }
            FullSpectralPowerDistribution newSpd = FullSpectrum.ConvertFromRGB(r, newRed, g, newGreen, b, newBlue);
            newOutputs[j] = FullSpectralBlender.ConvertSPDtoRGB(newSpd);
            newPercentMatches[j] = PercentMatch(newOutputs[j], inputs[j]);
         }

         float better = 0;
         currentPercentMatch = 0.0f;
         newPercentMatch = 0.0f;

         for (int j = 0; j < inputs.length; j++) {
            currentPercentMatch += currentPercentMatches[j];
            newPercentMatch += newPercentMatches[j];

            if (newPercentMatches[j] >= currentPercentMatches[j]) {
               better++;
            }
         }

         if (newPercentMatch >= currentPercentMatch) {
            currentRed = newRed;
            currentGreen = newGreen;
            currentBlue = newBlue;
            if (newPercentMatch > currentPercentMatch)
               System.out.println(currentPercentMatch + " " + newPercentMatch);
            currentPercentMatch = newPercentMatch;
            iterations = 0 - (int)(newPercentMatch * 100000);

         }
         if (iterations > 100000) {
            iterations = 0;
            currentRed = ResetRed();
            currentGreen = ResetGreen();
            currentBlue = ResetBlue();
            System.out.println("Reached iterations, resetting...");
         }
      }

      System.out.println("Determined SPD match = [" + currentPercentMatch + "]");

      // test colors
      for (int i = 0; i < inputs.length; i++) {
         final float r = inputs[i].getRed();
         final float g = inputs[i].getGreen();
         final float b = inputs[i].getBlue();

         FullSpectralPowerDistribution currentSpd = FullSpectrum.ConvertFromRGB(r, currentRed, g, currentGreen, b, currentBlue);
         Color currentOutput = FullSpectralBlender.ConvertSPDtoRGB(currentSpd);

         currentPercentMatch = PercentMatch(currentOutput, inputs[i]);
         System.out.println("Tested SPD match for " + i + " = [" + currentPercentMatch + "]");
      }

      File _dir = new File("test");
      if (!_dir.exists())
         _dir.mkdir();

      _dir = new File("test/exports");
      if (!_dir.exists())
         _dir.mkdir();


      //red
      File file = new File(_dir, "spd-red.xml");

      final FullSpectralPowerDistribution finalRed = FullSpectralPowerDistribution.scale(currentRed, 1.0);

      UnitTestExporter unitTestExporter = new UnitTestExporter(file);

      IExporter exporter = new IExporter() {
         @Override
         public Element Process(Document document, Element root) {

            return SPDExporter.Process(finalRed, document, root);
         }

      };

      unitTestExporter.Process(exporter);

      // green

      file = new File(_dir, "spd-green.xml");

      final FullSpectralPowerDistribution finalGreen = FullSpectralPowerDistribution.scale(currentGreen, 1.0);

      unitTestExporter = new UnitTestExporter(file);

      exporter = new IExporter() {
         @Override
         public Element Process(Document document, Element root) {

            return SPDExporter.Process(finalGreen, document, root);
         }

      };

      unitTestExporter.Process(exporter);

      // blue

      file = new File(_dir, "spd-blue.xml");

      final FullSpectralPowerDistribution finalBlue = FullSpectralPowerDistribution.scale(currentBlue, 1.0);

      unitTestExporter = new UnitTestExporter(file);

      exporter = new IExporter() {
         @Override
         public Element Process(Document document, Element root) {

            return SPDExporter.Process(finalBlue, document, root);
         }

      };

      unitTestExporter.Process(exporter);
   }

   private float PercentMatch(Color c1, Color c2) {
      int redDiff = Math.abs(c1.getRed() - c2.getRed());
      int greenDiff = Math.abs(c1.getGreen() - c2.getGreen());
      int blueDiff = Math.abs(c1.getBlue() - c2.getBlue());

      int total = 255 + 255 + 255;

      float percentage = 1.0f - (redDiff + greenDiff + blueDiff) / (float)total;

      return percentage;
   }
}
