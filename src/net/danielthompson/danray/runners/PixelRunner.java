package net.danielthompson.danray.runners;

import net.danielthompson.danray.TraceManager;
import net.danielthompson.danray.tracers.Tracer;
import net.danielthompson.danray.presets.RenderQualityPreset;
import net.danielthompson.danray.shading.Blender;
import net.danielthompson.danray.structures.ColorWithStatistics;
import net.danielthompson.danray.structures.Scene;
import net.danielthompson.danray.structures.Ray;

import java.awt.*;

/**
 * Created by daniel on 3/4/14.
 */


public class PixelRunner implements Runnable {
   private final Object _lock = new Object();

   volatile int _xPointer;
   volatile int _yPointer;

   private final int _x;
   private final int _y;

   private final TraceManager _manager;
   private final Tracer _tracer;
   private final Scene _scene;
   private int _frame;

   private final RenderQualityPreset qualityPreset;

   public PixelRunner(TraceManager manager, Tracer tracer, Scene scene, RenderQualityPreset qualityPreset, int frame) {
      _manager = manager;
      _tracer = tracer;
      _scene = scene;
      _frame = frame;
      _x = qualityPreset.getX();
      _y = qualityPreset.getY();

      this.qualityPreset = qualityPreset;
   }

   @Override
   public void run() {
      int[] pixel = GetNextPixel();
      while (pixel != null) {
         trace(pixel);
         if (pixel[0] == _x - 1)
            _manager.UpdateCanvas();
         pixel = GetNextPixel();
      }
   }

   /**
    * Traces and outputs the given pixel. Must not be null.
    * @param pixel
    */
   public void trace(int[] pixel) {

      int reachedSamples = 0;

      Ray[] cameraRays = _scene.Camera.getInitialStochasticRaysForPixel(pixel[0], pixel[1], qualityPreset.getSamplesPerPixel());

      _manager.InitialRays += cameraRays.length;

      ColorWithStatistics[] colorsWithStatistics = new ColorWithStatistics[cameraRays.length];
      for (int i = 0; i < cameraRays.length; i++) {
         colorsWithStatistics[i] = _tracer.GetColorForRay(cameraRays[i], 1);
         _manager.Statistics[pixel[0]][pixel[1]].Add(colorsWithStatistics[i].Statistics);
      }

      Color[] colors = new Color[cameraRays.length];

      for (int i = 0; i < colors.length; i++) {
         colors[i] = colorsWithStatistics[i].Color;
      }

      Color blendSoFar = Blender.BlendColors(colors);
      Color marginalBlend;

      for (int j = 0; j < qualityPreset.getSuperSamplesPerPixel(); j++) {
         reachedSamples += qualityPreset.getSamplesPerPixel();
         cameraRays = _scene.Camera.getInitialStochasticRaysForPixel(pixel[0], pixel[1], qualityPreset.getSamplesPerPixel());
         _manager.InitialRays += cameraRays.length;
         colorsWithStatistics = new ColorWithStatistics[cameraRays.length];
         for (int i = 0; i < cameraRays.length; i++) {
            colorsWithStatistics[i] = _tracer.GetColorForRay(cameraRays[i], 1);
            _manager.Statistics[pixel[0]][pixel[1]].Add(colorsWithStatistics[i].Statistics);
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

         if (Blender.CloseEnough(blendSoFar, marginalBlend, qualityPreset.getConvergenceTerminationThreshold())) {
            blendSoFar = marginalBlend;
            break;
         }
         else {
            blendSoFar = marginalBlend;
         }

      }



      _manager.SetRayCountForPixel(pixel, reachedSamples);
      _manager.SetPixelColor(pixel, blendSoFar);

   }

   public int[] GetNextPixel() {

      int[] pixel = new int[2];
      pixel[0] = -1;
      pixel[1] = -1;

      synchronized (_lock) {

         if (_xPointer < _x && _yPointer < _y) {

            pixel[0] = _xPointer;
            pixel[1] = _yPointer;

            _xPointer++;

            if (_xPointer >= _x) {
               _xPointer = 0;
               _yPointer++;
            }
         }
      }

      if (pixel[0] == -1)
         return null;
      return pixel;
   }

}