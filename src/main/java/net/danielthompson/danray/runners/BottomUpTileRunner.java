package net.danielthompson.danray.runners;

import net.danielthompson.danray.Logger;
import net.danielthompson.danray.TraceManager;
import net.danielthompson.danray.films.AbstractFilm;
import net.danielthompson.danray.integrators.AbstractIntegrator;
import net.danielthompson.danray.presets.RenderQualityPreset;
import net.danielthompson.danray.samplers.AbstractSampler;
import net.danielthompson.danray.scenes.AbstractScene;

import java.util.EmptyStackException;

/**
 * Created by daniel on 3/4/14.
 */


public class BottomUpTileRunner extends AbstractTileRunner {

   public BottomUpTileRunner(TraceManager manager,
                             AbstractIntegrator tracer,
                             AbstractScene scene,
                             RenderQualityPreset qualityPreset,
                             AbstractFilm film,
                             AbstractSampler sampler,
                             int frame) {
      super(manager, tracer, scene, qualityPreset, film, sampler, frame);
   }

   @Override
   public void run() {

      boolean light = true;
      boolean dark = true;

      try {

         while (true) {
            Tile tile = Tiles.pop();

            //Logger.Log("Popped tile at " + tile.minx + " / " + tile.miny );

            for (int y = tile.maxy - 1; y >= tile.miny; y--) {
               for (int x = tile.minx; x < tile.maxx; x++) {
//                  if (x == 119) {
////                     if (y == 315 && light) {
////                        Logger.Log("bright");
////                        light = false;
////                        trace(x, y);
////                     }
//                     if (y == 311 && dark) {
//                        Logger.Log("dark");
//                        dark = false;
//                        trace(x, y);
//                     }
//
//                  }
                  trace(x, y);
               }
            }
         }
      }
      catch (EmptyStackException e) {

      }
   }
}