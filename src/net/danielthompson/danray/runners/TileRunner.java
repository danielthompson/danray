package net.danielthompson.danray.runners;

import net.danielthompson.danray.TraceManager;
import net.danielthompson.danray.presets.RenderQualityPreset;
import net.danielthompson.danray.shading.Blender;
import net.danielthompson.danray.structures.ColorWithStatistics;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Scene;
import net.danielthompson.danray.tracers.Tracer;

import java.awt.*;

/**
 * Created by daniel on 3/4/14.
 */


public class TileRunner implements Runnable {
   volatile int _xTilePointer;
   volatile int _yTilePointer;

   private final int _xTileWidth = 32;
   private final int _yTileWidth = 32;

   private final int _xLastTileWidth;
   private final int _yLastTileWidth;

   private final int _numXTiles;
   private final int _numYTiles;

   private final int _x;
   private final int _y;

   private final TraceManager _manager;
   private final Tracer _tracer;
   private final Scene _scene;
   private int _frame;

   private final RenderQualityPreset qualityPreset;

   public TileRunner(TraceManager manager, Tracer tracer, Scene scene, RenderQualityPreset qualityPreset, int frame) {
      _manager = manager;
      _tracer = tracer;
      _scene = scene;
      _frame = frame;
      _x = qualityPreset.getX();
      _y = qualityPreset.getY();

      this.qualityPreset = qualityPreset;

      _xLastTileWidth = getXLastTileWidth();
      _yLastTileWidth = getYLastTileWidth();

      _numXTiles = getXTiles();
      _numYTiles = getYTiles();
   }

   private int getXLastTileWidth() {
      int n = _x % _xTileWidth;
      if (n == 0)
         n = _xTileWidth;
      return n;
   }

   private int getYLastTileWidth() {
      int n = _y % _yTileWidth;
      if (n == 0)
         n = _yTileWidth;
      return n;
   }

   private int getXTiles() {
      if (_x % _xTileWidth > 0)
         return (_x / _xTileWidth) + 1;

      else
         return (_x / _xTileWidth);
   }

   private int getYTiles() {
      if (_y % _yTileWidth > 0)
         return (_y / _yTileWidth) + 1;

      else
         return (_y / _yTileWidth);
   }

   @Override
   public void run() {
      int[] tile = getNextTile();
      while (tile != null) {

         int xMin = tile[0] * _xTileWidth;
         int yMin = tile[1] * _yTileWidth;

         int xMax = (tile[0] + 1) * _xTileWidth;
         int yMax = (tile[1] + 1) * _yTileWidth;

         if (tile[0] == _numXTiles - 1)
            xMax = tile[0] * _xTileWidth + _xLastTileWidth;

         if (tile[1] == _numYTiles - 1)
            yMax = tile[1] * _yTileWidth + _yLastTileWidth;

         for (int y = yMin; y < yMax; y++) {
            for (int x = xMin; x < xMax; x++) {
               trace(x, y);

            }

         }

         tile = getNextTile();
      }
   }

   private void trace(int x, int y) {
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

   public int[] getNextTile() {
      int[] tile = new int[2];
      tile[0] = -1;
      tile[1] = -1;

      if (_xTilePointer < _numXTiles && _yTilePointer < _numYTiles) {
         tile[0] = _xTilePointer;
         tile[1] = _yTilePointer;

         _xTilePointer++;

         if (_xTilePointer >= _numXTiles) {
            _xTilePointer = 0;
            _yTilePointer++;
         }
      }

      if (tile[0] == -1)
         return null;
      return tile;
   }

}