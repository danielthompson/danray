package net.danielthompson.danray.runners;

import net.danielthompson.danray.Logger;
import net.danielthompson.danray.TraceManager;
import net.danielthompson.danray.presets.RenderQualityPreset;
import net.danielthompson.danray.integrators.AbstractIntegrator;
import net.danielthompson.danray.films.AbstractFilm;
import net.danielthompson.danray.samplers.AbstractSampler;
import net.danielthompson.danray.scenes.AbstractScene;
import org.apache.commons.lang.exception.ExceptionUtils;

/**
 * Created by daniel on 3/4/14.
 */


public class TileRunner extends AbstractRunner {
   private volatile int _xTilePointer;
   private volatile int _yTilePointer;

   private final int _xTileWidth = 32;
   private final int _yTileWidth = 32;

   private final int _xLastTileWidth;
   private final int _yLastTileWidth;

   private final int _numXTiles;
   private final int _numYTiles;

   private final int _x;
   private final int _y;


   public TileRunner(TraceManager manager,
                     AbstractIntegrator tracer,
                     AbstractScene scene,
                     RenderQualityPreset qualityPreset,
                     AbstractFilm film,
                     AbstractSampler sampler,
                     int frame) {
      super(manager, tracer, scene, qualityPreset, film, sampler, frame);

      _x = qualityPreset.getX();
      _y = qualityPreset.getY();

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
               if (x == 460 && y == 1) {
                  int i = 0;
               }

               try {
                  trace(x, y);
               }
               catch (Exception e) {
                  Logger.Log("Exception caught at (" + x + ", " + y + "): " + e.toString());
                  Logger.Log(ExceptionUtils.getStackTrace(e));

                  throw e;
               }
            }
         }
         tile = getNextTile();
      }
   }

   private int[] getNextTile() {
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