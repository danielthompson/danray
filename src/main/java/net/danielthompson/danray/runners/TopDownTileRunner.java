package net.danielthompson.danray.runners;

import net.danielthompson.danray.TraceManager;
import net.danielthompson.danray.presets.RenderQualityPreset;
import net.danielthompson.danray.integrators.AbstractIntegrator;
import net.danielthompson.danray.films.AbstractFilm;
import net.danielthompson.danray.samplers.AbstractSampler;
import net.danielthompson.danray.scenes.AbstractScene;

import java.util.Collections;
import java.util.EmptyStackException;

/**
 * Created by daniel on 3/4/14.
 */


public class TopDownTileRunner extends AbstractTileRunner {


   public TopDownTileRunner(TraceManager manager,
                            AbstractIntegrator tracer,
                            AbstractScene scene,
                            RenderQualityPreset qualityPreset,
                            AbstractFilm film,
                            AbstractSampler sampler,
                            int frame) {
      super(manager, tracer, scene, qualityPreset, film, sampler, frame);

      Collections.reverse(Tiles);

   }

   @Override
   public void run() {

      try {

         while (true) {
            Tile tile = Tiles.pop();

            for (int y = tile.minx; y < tile.maxy; y++) {
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