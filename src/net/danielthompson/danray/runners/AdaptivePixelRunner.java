package net.danielthompson.danray.runners;

import net.danielthompson.danray.TraceManager;
import net.danielthompson.danray.tracers.Tracer;
import net.danielthompson.danray.shading.Blender;
import net.danielthompson.danray.acceleration.KDScene;
import net.danielthompson.danray.structures.Ray;

import java.awt.*;

/**
 * Created by daniel on 3/4/14.
 */


public class AdaptivePixelRunner implements Runnable {


   private final Object _lock = new Object();

   volatile int _xPointer;
   volatile int _yPointer;

   private final int _x;
   private final int _y;

   private final TraceManager _manager;
   private final Tracer _tracer;
   private final KDScene _scene;

   private final int _samples;
   private final int _superSamples;

   private final float _insideQuality = .4f;

   private final int _maxRecursion = 20;

   public AdaptivePixelRunner(TraceManager manager, Tracer tracer, KDScene scene, int x, int y, int samples, int superSamples) {
      _manager = manager;
      _tracer = tracer;
      _scene = scene;
      _x = x;
      _y = y;
      _samples = samples;
      _superSamples = superSamples;

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
      /*
      Color color;

      double leftX = pixel[0];
      double rightX = leftX + 1;

      double topY = pixel[1];
      double bottomY = topY + 1;

      Vector topLeftVector = _scene.Camera.getStochasticRayForPixel(leftX, topY);
      Vector bottomRightVector = _scene.Camera.getStochasticRayForPixel(rightX, bottomY);

      Color topLeftColor = _tracer.GetColorForRay(topLeftVector, 1);
      Color bottomRightColor = _tracer.GetColorForRay(bottomRightVector, 1);

      if (Blender.CloseEnough(topLeftColor, bottomRightColor, .9999f)) {
         color = Blender.BlendColors(new Color[] {topLeftColor, bottomRightColor});
      }
      else {
         color = GetColorInsideLeftBox(leftX, topY, topLeftColor, rightX, bottomY, bottomRightColor, 1);
      }
      /*

      int reachedSamples = 0;

      Vector[] cameraRays = _scene.Camera.getInitialStochasticRaysForPixel(pixel[0], pixel[1], _samples);
      Color[] colors = new Color[cameraRays.length];
      for (int i = 0; i < cameraRays.length; i++) {
         colors[i] = _tracer.GetColorForRay(cameraRays[i], 1);
      }

      Color blendSoFar = Blender.BlendColors(colors);
      Color marginalBlend = blendSoFar;

      for (int j = 0; j < _superSamples; j++) {
         reachedSamples += _samples;
         cameraRays = _scene.Camera.getInitialStochasticRaysForPixel(pixel[0], pixel[1], _samples);
         colors = new Color[cameraRays.length];
         for (int i = 0; i < cameraRays.length; i++) {
            colors[i] = _tracer.GetColorForRay(cameraRays[i], 1);
         }

         Color marginalColor = Blender.BlendColors(colors);

         marginalBlend = Blender.BlendWeighted(marginalColor, 1, blendSoFar, 1 + j);

         if (Blender.CloseEnough(blendSoFar, marginalBlend, .9999f)) {
            blendSoFar = marginalBlend;
            break;
         }
         else {
            blendSoFar = marginalBlend;
         }

      }*/

      //_manager.SetRayCountForPixel(pixel, reachedSamples);
      //_manager.SetPixelColor(pixel, color);

   }

   private Color GetColorInsideLeftBox(double leftX, double topY, Color topLeftColor, double rightX, double bottomY, Color bottomRightColor, int recursionDepth) {
      double centerX = (leftX + rightX) * .5;
      double centerY = (topY + bottomY) * .5;

      Ray topRightRay = _scene.Camera.getStochasticRayForPixel(rightX, topY);
      Ray centerRay = _scene.Camera.getStochasticRayForPixel(centerX, centerY);
      Ray bottomLeftRay = _scene.Camera.getStochasticRayForPixel(leftX, bottomY);

      Color topRightColor = null; //_tracer.GetColorForRay(topRightVector, 1);
      Color centerColor = null; //_tracer.GetColorForRay(centerVector, 1);
      Color bottomLeftColor = null; //_tracer.GetColorForRay(bottomLeftVector, 1);

      Color firstColor;
      Color secondColor;
      Color thirdColor;
      Color fourthColor;

      // top left

      if (_maxRecursion == recursionDepth || Blender.CloseEnough(topLeftColor, centerColor, _insideQuality)) {
         firstColor = Blender.BlendColors(new Color[] {topLeftColor, centerColor});
      }
      else {
         Color boxColor = GetColorInsideLeftBox(leftX, topY, topLeftColor, centerX, centerY, centerColor, recursionDepth + 1);

         firstColor = Blender.BlendRGB(new Color[] {topLeftColor, centerColor, boxColor}, new float[] {.2f, .2f, .6f});
      }

      // top right

      if (_maxRecursion == recursionDepth || Blender.CloseEnough(topRightColor, centerColor, _insideQuality)) {
         secondColor = Blender.BlendColors(new Color[] {topRightColor, centerColor});
      }
      else {
         Color boxColor = GetColorInsideRightBox(rightX, topY, topRightColor, centerX, centerY, centerColor, recursionDepth + 1);

         secondColor = Blender.BlendRGB(new Color[] {topRightColor, centerColor, boxColor}, new float[] {.2f, .2f, .6f});
      }

      // bottom right

      if (_maxRecursion == recursionDepth || Blender.CloseEnough(centerColor, bottomRightColor, _insideQuality)) {
         thirdColor = Blender.BlendColors(new Color[] {centerColor, bottomRightColor});
      }
      else {
         Color boxColor = GetColorInsideLeftBox(centerX, centerY, centerColor, rightX, bottomY, bottomRightColor, recursionDepth + 1);

         thirdColor = Blender.BlendRGB(new Color[] {centerColor, bottomRightColor, boxColor}, new float[] {.2f, .2f, .6f});
      }

      // bottom left

      if (_maxRecursion == recursionDepth || Blender.CloseEnough(centerColor, bottomLeftColor, _insideQuality)) {
         fourthColor = Blender.BlendColors(new Color[] {centerColor, bottomLeftColor});
      }
      else {
         Color boxColor = GetColorInsideRightBox(centerX, centerY, centerColor, leftX, bottomY, bottomLeftColor, recursionDepth + 1);

         fourthColor = Blender.BlendRGB(new Color[] {centerColor, bottomLeftColor, boxColor}, new float[] {.2f, .2f, .6f});

      }

      Color blended = Blender.BlendColors(new Color[] {firstColor, secondColor, thirdColor, fourthColor});
      return blended;

   }

   private Color GetColorInsideRightBox(double rightX, double topY, Color topRightColor, double leftX, double bottomY, Color bottomLeftColor, int recursionDepth) {
      double centerX = (leftX + rightX) * .5;
      double centerY = (topY + bottomY) * .5;

      Ray topLeftRay = _scene.Camera.getStochasticRayForPixel(leftX, topY);
      Ray centerRay = _scene.Camera.getStochasticRayForPixel(centerX, centerY);
      Ray bottomRightRay = _scene.Camera.getStochasticRayForPixel(rightX, bottomY);

      Color topLeftColor = null; //_tracer.GetColorForRay(topLeftVector, 1);
      Color centerColor = null; //_tracer.GetColorForRay(centerVector, 1);
      Color bottomRightColor = null; //_tracer.GetColorForRay(bottomRightVector, 1);

      Color firstColor;
      Color secondColor;
      Color thirdColor;
      Color fourthColor;

      // top left

      if (_maxRecursion == recursionDepth || Blender.CloseEnough(topLeftColor, centerColor, _insideQuality)) {
         firstColor = Blender.BlendColors(new Color[] {topLeftColor, centerColor});
      }
      else {
         Color boxColor = GetColorInsideLeftBox(leftX, topY, topLeftColor, centerX, centerY, centerColor, recursionDepth + 1);

         firstColor = Blender.BlendRGB(new Color[]{topLeftColor, centerColor, boxColor}, new float[]{.2f, .2f, .6f});
      }

      // top right

      if (_maxRecursion == recursionDepth || Blender.CloseEnough(topRightColor, centerColor, _insideQuality)) {
         secondColor = Blender.BlendColors(new Color[] {topRightColor, centerColor});
      }
      else {
         Color boxColor = GetColorInsideRightBox(rightX, topY, topRightColor, centerX, centerY, centerColor, recursionDepth + 1);

         secondColor = Blender.BlendRGB(new Color[]{topRightColor, centerColor, boxColor}, new float[]{.2f, .2f, .6f});
      }

      // bottom right

      if (_maxRecursion == recursionDepth || Blender.CloseEnough(centerColor, bottomRightColor, _insideQuality)) {
         thirdColor = Blender.BlendColors(new Color[] {centerColor, bottomRightColor});
      }
      else {
         Color boxColor = GetColorInsideLeftBox(centerX, centerY, centerColor, rightX, bottomY, bottomRightColor, recursionDepth + 1);

         thirdColor = Blender.BlendRGB(new Color[]{centerColor, bottomRightColor, boxColor}, new float[]{.2f, .2f, .6f});
      }

      // bottom left

      if (_maxRecursion == recursionDepth || Blender.CloseEnough(centerColor, bottomLeftColor, _insideQuality)) {
         fourthColor = Blender.BlendColors(new Color[] {centerColor, bottomLeftColor});
      }
      else {
         Color boxColor = GetColorInsideRightBox(centerX, centerY, centerColor, leftX, bottomY, bottomLeftColor, recursionDepth + 1);

         fourthColor = Blender.BlendRGB(new Color[] {centerColor, bottomLeftColor, boxColor}, new float[] {.2f, .2f, .6f});

      }

      Color blended = Blender.BlendColors(new Color[] {firstColor, secondColor, thirdColor, fourthColor});
      return blended;
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