package net.danielthompson.danray.runners;

import net.danielthompson.danray.TraceManager;
import net.danielthompson.danray.films.AbstractFilm;
import net.danielthompson.danray.integrators.AbstractIntegrator;
import net.danielthompson.danray.config.RenderQuality;
import net.danielthompson.danray.samplers.AbstractSampler;
import net.danielthompson.danray.scenes.AbstractScene;

import java.util.Stack;

public abstract class AbstractTileRunner extends AbstractRunner {
   protected final int _xTileWidth = 32;
   protected final int _yTileWidth = 32;
   protected final int _xLastTileWidth;
   protected final int _yLastTileWidth;
   protected final int _numXTiles;
   protected final int _numYTiles;
   protected final int _x;
   protected final int _y;

   protected volatile Stack<Tile> Tiles;

   protected AbstractTileRunner(TraceManager manager,
                             AbstractIntegrator tracer,
                             AbstractScene scene,
                             RenderQuality qualityPreset,
                             AbstractFilm film,
                             AbstractSampler sampler,
                             int frame) {
      super(manager, tracer, scene, qualityPreset, film, sampler, frame);

      _x = qualityPreset.x;
      _y = qualityPreset.y;

      _xLastTileWidth = getXLastTileWidth();
      _yLastTileWidth = getYLastTileWidth();

      _numXTiles = getXTiles();
      _numYTiles = getYTiles();

      Tiles = new Stack<>();

      for (int yTile = 0; yTile < _numYTiles; yTile++) {
         boolean lastYTile = (yTile == _numYTiles - 1);
         final int miny = yTile * _yTileWidth;
         for (int xTile = 0; xTile < _numXTiles; xTile++) {
            final boolean lastXTile = (xTile == _numXTiles - 1);
            final int minx = xTile * _xTileWidth;
            final Tile tile = new Tile(
                  minx,
                  miny,
                  minx + (lastXTile ? _xLastTileWidth : _xTileWidth),
                  miny + (lastYTile ? _yLastTileWidth : _yTileWidth)
            );

            Tiles.push(tile);
         }
      }
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
}
