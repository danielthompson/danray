package net.danielthompson.danray.runners;

import net.danielthompson.danray.Logger;
import net.danielthompson.danray.TraceManager;
import net.danielthompson.danray.config.RenderQuality;
import net.danielthompson.danray.integrators.AbstractIntegrator;
import net.danielthompson.danray.films.AbstractFilm;
import net.danielthompson.danray.samplers.AbstractSampler;
import net.danielthompson.danray.structures.Point2;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Sample;
import net.danielthompson.danray.scenes.AbstractScene;
import org.apache.commons.lang.exception.ExceptionUtils;

/**
 * Created by daniel on 4/3/16.
 */
public abstract class AbstractRunner implements Runnable {

   final TraceManager Manager;
   private final AbstractIntegrator Integrator;
   protected final AbstractScene Scene;
   private int Frame;
   private final AbstractFilm Film;
   private final AbstractSampler Sampler;

   private final int _samplesPerPixel;
   private final int _superSamplesPerPixel;

   AbstractRunner(TraceManager traceManager,
                  AbstractIntegrator integrator,
                  AbstractScene scene,
                  RenderQuality qualityPreset,
                  AbstractFilm film,
                  AbstractSampler sampler,
                  int frame) {
      Manager = traceManager;
      Integrator = integrator;
      Scene = scene;
      Frame = frame;
      Film = film;
      Sampler = sampler;
      _samplesPerPixel = qualityPreset.samplesPerPixel;
      _superSamplesPerPixel = qualityPreset.superSamplesPerPixel;
   }

   @Override
   public abstract void run();

   /**
    * Traces and outputs the given pixel.
    */
   public void trace(final int x, final int y) {

      try {
         int iterations = 0;
         int reachedSamples = 0;
         int heatCount = 0;

         do {
            final Point2[] sampleLocations = Sampler.GetSamples(x, y, _samplesPerPixel);
            final int numSamples = sampleLocations.length;
            final Ray[] cameraRays = Scene.Camera.getRays(sampleLocations, numSamples);
            TraceManager.InitialRays.addAndGet(cameraRays.length);
            final Sample[] samples = new Sample[cameraRays.length];
            for (int i = 0; i < cameraRays.length; i++) {
               samples[i] = Integrator.getSample(cameraRays[i], 1, x, y);
               heatCount += samples[i].KDHeatCount;
            }
            Film.AddSamples(x, y, samples);
            reachedSamples += numSamples;
            iterations++;
         }
         while (!Film.AboveThreshhold() && iterations < _superSamplesPerPixel);

         Manager.SetKDHeatForPixel(x, y, heatCount);
         Manager.SetRayCountForPixel(x, y, reachedSamples);
      } catch (Exception e) {
         Logger.Log(Logger.Level.Error, "Exception caught at (" + x + ", " + y + "): " + e.toString());
         Logger.Log(Logger.Level.Error, ExceptionUtils.getStackTrace(e));

         throw e;
      }
   }
}
