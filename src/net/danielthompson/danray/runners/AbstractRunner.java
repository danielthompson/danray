package net.danielthompson.danray.runners;

import net.danielthompson.danray.TraceManager;
import net.danielthompson.danray.presets.RenderQualityPreset;
import net.danielthompson.danray.integrators.AbstractIntegrator;
import net.danielthompson.danray.films.AbstractFilm;
import net.danielthompson.danray.samplers.AbstractSampler;
import net.danielthompson.danray.samplers.RandomSampler;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Sample;
import net.danielthompson.danray.structures.Scene;

/**
 * Created by daniel on 4/3/16.
 */
public abstract class AbstractRunner implements Runnable {

   final TraceManager Manager;
   private final AbstractIntegrator Integrator;
   protected final Scene Scene;
   private int Frame;
   private final AbstractFilm Film;

   private final int _samplesPerPixel;
   private final int _superSamplesPerPixel;

   AbstractRunner(TraceManager traceManager, AbstractIntegrator integrator, Scene scene, RenderQualityPreset qualityPreset, AbstractFilm film, int frame) {
      Manager = traceManager;
      Integrator = integrator;
      Scene = scene;
      Frame = frame;
      Film = film;
      _samplesPerPixel = qualityPreset.getSamplesPerPixel();
      _superSamplesPerPixel = qualityPreset.getSuperSamplesPerPixel();
   }

   @Override
   public abstract void run();

   /**
    * Traces and outputs the given pixel.
    */
   public void trace(int x, int y) {

      int iterations = 0;
      int reachedSamples = 0;
      int heatCount = 0;

      AbstractSampler sampler = new RandomSampler();;

      do {
         float[][] pixels = sampler.GetSamples(x, y, _samplesPerPixel);

         for (int j = 0; j < _samplesPerPixel; j++) {

            Ray[] cameraRays = Scene.Camera.getInitialStochasticRaysForPixel(pixels[j][0], pixels[j][1], 1);
            Manager.InitialRays += cameraRays.length;

            Sample[] colorsWithStatistics = new Sample[cameraRays.length];
            for (int i = 0; i < cameraRays.length; i++) {
               colorsWithStatistics[i] = Integrator.GetSample(cameraRays[i], 1);
               heatCount += colorsWithStatistics[i].KDHeatCount;
               Manager.Statistics[x][y].Add(colorsWithStatistics[i].Statistics);
            }

            Film.AddSamples(x, y, colorsWithStatistics);
            reachedSamples += _samplesPerPixel;
         }
         iterations++;
      }
      while (!Film.AboveThreshhold() && iterations < _superSamplesPerPixel);

      Manager.SetKDHeatForPixel(x, y, heatCount);
      Manager.SetRayCountForPixel(x, y, reachedSamples);
   }
}