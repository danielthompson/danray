package net.danielthompson.danray.runners;

import net.danielthompson.danray.Main;
import net.danielthompson.danray.TraceManager;
import net.danielthompson.danray.presets.RenderQualityPreset;
import net.danielthompson.danray.samplers.BaseSampler;
import net.danielthompson.danray.shading.Blender;
import net.danielthompson.danray.shading.SpectralBlender;
import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Sample;
import net.danielthompson.danray.structures.Scene;

import java.awt.*;

/**
 * Created by daniel on 4/3/16.
 */
public abstract class BaseRunner implements Runnable {

   final TraceManager Manager;
   private final BaseSampler Sampler;
   protected final Scene Scene;
   private final RenderQualityPreset QualityPreset;
   private int Frame;

   BaseRunner(TraceManager traceManager, BaseSampler sampler, Scene scene, RenderQualityPreset qualityPreset, int frame) {
      Manager = traceManager;
      Sampler = sampler;
      Scene = scene;
      QualityPreset = qualityPreset;
      Frame = frame;
   }

   @Override
   public abstract void run();

   public void trace(int x, int y) {
      int[] pixel = new int[2];
      pixel[0] = x;
      pixel[1] = y;
      trace(pixel);
   }

   /**
    * Traces and outputs the given pixel. Must not be null.
    * @param pixel
    */
   public void trace(int[] pixel) {

      if (Main.UseSpectralRendering) {
         int reachedSamples = 0;

         Ray[] cameraRays = Scene.Camera.getInitialStochasticRaysForPixel(pixel[0], pixel[1], QualityPreset.getSamplesPerPixel());

         Manager.InitialRays += cameraRays.length;

         SpectralPowerDistribution[] initialSamples = new SpectralPowerDistribution[cameraRays.length];

         for (int i = 0; i < cameraRays.length; i++) {
            initialSamples[i] = Sampler.GetSPDForRay(cameraRays[i], 1);
         }

         SpectralPowerDistribution blendSoFar = SpectralPowerDistribution.average(initialSamples);

         SpectralPowerDistribution marginalBlend;

         for (int j = 0; j < QualityPreset.getSuperSamplesPerPixel(); j++) {
            reachedSamples += QualityPreset.getSamplesPerPixel();
            cameraRays = Scene.Camera.getInitialStochasticRaysForPixel(pixel[0], pixel[1], QualityPreset.getSamplesPerPixel());
            Manager.InitialRays += cameraRays.length;

            SpectralPowerDistribution[] additionalSamples = new SpectralPowerDistribution[cameraRays.length];

            for (int i = 0; i < cameraRays.length; i++) {
               additionalSamples[i] = Sampler.GetSPDForRay(cameraRays[i], 1);
            }

            SpectralPowerDistribution additionalBlend = SpectralPowerDistribution.average(additionalSamples);
            marginalBlend = SpectralPowerDistribution.lerp(additionalBlend, 1, blendSoFar, j + 1);

            if (SpectralBlender.CloseEnough(blendSoFar, marginalBlend, QualityPreset.getConvergenceTerminationThreshold())) {
               blendSoFar = marginalBlend;
               break;
            }
            else {
               blendSoFar = marginalBlend;
            }
         }

         blendSoFar.scale(SpectralBlender.FilmSpeedMultiplier);
         Manager.SetRayCountForPixel(pixel, reachedSamples);
         //Manager.SetPixelColor(pixel, c);
         //Manager.SetPixelXYZ(pixel, marginalBlend);
         Manager.SetPixelSPD(pixel, blendSoFar);
      }
      else {

         if (pixel[0] == 332 && pixel[1] == 443)
            System.out.flush();

         int reachedSamples = 0;

         int heatCount = 0;

         Ray[] cameraRays = Scene.Camera.getInitialStochasticRaysForPixel(pixel[0], pixel[1], QualityPreset.getSamplesPerPixel());

         Manager.InitialRays += cameraRays.length;

         Sample[] colorsWithStatistics = new Sample[cameraRays.length];
         for (int i = 0; i < cameraRays.length; i++) {
            colorsWithStatistics[i] = Sampler.GetSample(cameraRays[i], 1);
            heatCount += colorsWithStatistics[i].KDHeatCount;
            Manager.Statistics[pixel[0]][pixel[1]].Add(colorsWithStatistics[i].Statistics);
         }

         Color[] colors = new Color[cameraRays.length];

         for (int i = 0; i < colors.length; i++) {
            colors[i] = colorsWithStatistics[i].Color;
         }

         Color blendSoFar = Blender.BlendColors(colors);
         Color marginalBlend;

         for (int j = 0; j < QualityPreset.getSuperSamplesPerPixel(); j++) {
            reachedSamples += QualityPreset.getSamplesPerPixel();
            cameraRays = Scene.Camera.getInitialStochasticRaysForPixel(pixel[0], pixel[1], QualityPreset.getSamplesPerPixel());
            Manager.InitialRays += cameraRays.length;
            colorsWithStatistics = new Sample[cameraRays.length];
            for (int i = 0; i < cameraRays.length; i++) {
               colorsWithStatistics[i] = Sampler.GetSample(cameraRays[i], 1);
               heatCount += colorsWithStatistics[i].KDHeatCount;
               Manager.Statistics[pixel[0]][pixel[1]].Add(colorsWithStatistics[i].Statistics);
            }

            colors = new Color[cameraRays.length];

            for (int i = 0; i < colors.length; i++) {
               colors[i] = colorsWithStatistics[i].Color;
            }

            Color marginalColor = Blender.BlendColors(colors);
/*
         if (j == 0) {
            blendSoFar = marginalColor;
            continue;
         }
  */
            marginalBlend = Blender.BlendWeighted(marginalColor, 1, blendSoFar, 1 + j);

            if (Blender.CloseEnough(blendSoFar, marginalBlend, QualityPreset.getConvergenceTerminationThreshold())) {
               blendSoFar = marginalBlend;
               break;
            } else {
               blendSoFar = marginalBlend;
            }

         }

         Manager.SetKDHeatForPixel(pixel, heatCount);

         Manager.SetRayCountForPixel(pixel, reachedSamples);
         Manager.SetPixelColor(pixel, blendSoFar);
      }
   }
}
