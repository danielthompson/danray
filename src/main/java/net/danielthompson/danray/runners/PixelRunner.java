package net.danielthompson.danray.runners;

import net.danielthompson.danray.TraceManager;
import net.danielthompson.danray.integrators.AbstractIntegrator;
import net.danielthompson.danray.config.RenderQuality;
import net.danielthompson.danray.films.AbstractFilm;
import net.danielthompson.danray.samplers.AbstractSampler;
import net.danielthompson.danray.scenes.AbstractScene;


/**
 * Created by daniel on 3/4/14.
 */

public class PixelRunner extends AbstractRunner {
   private final Object _lock = new Object();

   private volatile int _xPointer;
   private volatile int _yPointer;

   private final int _x;
   private final int _y;

   public PixelRunner(TraceManager manager,
                      AbstractIntegrator tracer,
                      AbstractScene scene,
                      RenderQuality qualityPreset,
                      AbstractFilm film,
                      AbstractSampler sampler,
                      int frame) {
      super(manager, tracer, scene, qualityPreset, film, sampler, frame);
      _x = qualityPreset.x;
      _y = qualityPreset.y;
   }

   @Override
   public void run() {
      int[] pixel = GetNextPixel();
      while (pixel != null) {
         trace(pixel[0], pixel[1]);
         if (pixel[0] == _x - 1)
            Manager.UpdateCanvas();
         pixel = GetNextPixel();
      }
   }

   private int[] GetNextPixel() {

      int[] pixel = new int[2];
      pixel[0] = -1;
      pixel[1] = -1;

      synchronized (_lock) {

         if (_xPointer < _x && _yPointer < _y) {

            pixel[0] = _xPointer;
            pixel[1] = _yPointer;

            _yPointer++;

            if (_yPointer >= _y) {
               _xPointer++;
               _yPointer = 0;

            }
         }
      }

      if (pixel[0] == -1)
         return null;
      return pixel;
   }

}