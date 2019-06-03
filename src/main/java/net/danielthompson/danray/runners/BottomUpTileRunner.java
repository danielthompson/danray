package net.danielthompson.danray.runners;

import net.danielthompson.danray.TraceManager;
import net.danielthompson.danray.films.AbstractFilm;
import net.danielthompson.danray.integrators.AbstractIntegrator;
import net.danielthompson.danray.config.RenderQuality;
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
                             RenderQuality qualityPreset,
                             AbstractFilm film,
                             AbstractSampler sampler,
                             int frame) {
      super(manager, tracer, scene, qualityPreset, film, sampler, frame);
   }

   @Override
   public void run() {
      try {

         while (true) {
            Tile tile = Tiles.pop();
            for (int y = tile.maxy - 1; y >= tile.miny; y--) {
               for (int x = tile.minx; x < tile.maxx; x++) {
                  trace(x, y);
               }
            }
         }
      }
      catch (EmptyStackException e) {

      }
   }
}