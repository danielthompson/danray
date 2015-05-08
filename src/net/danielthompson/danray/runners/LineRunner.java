package net.danielthompson.danray.runners;

import net.danielthompson.danray.TraceManager;
import net.danielthompson.danray.tracers.Tracer;
import net.danielthompson.danray.acceleration.KDScene;
import net.danielthompson.danray.structures.Ray;

import java.util.*;

/**
 * Created by daniel on 3/4/14.
 */
public class LineRunner implements Runnable {

   private final TraceManager _manager;
   private final Tracer _tracer;
   private final KDScene _scene;

   private final int _x;
   private final int _y;

   private final int _samples;

   int _yPointer;

   public LineRunner(TraceManager manager, Tracer tracer, KDScene scene, int x, int y, int samples) {

      _manager = manager;
      _tracer = tracer;
      _scene = scene;

      _x = x;
      _y = y;

      _samples = samples;
   }

   public void run() {
      int y = GetNextLine();
      int x = 0;
      int samp = 0;
/*
      while (y >= 0) {
         Vector[] cameraRays = GetCameraRaysForLine(y);
         Color[] colors = new Color[cameraRays.length];
         for (int i = 0; i < cameraRays.length; i++) {
            colors[i] = _tracer.GetColorForRay(cameraRays[i], 1);
         }

         Color color = Blender.BlendColors(colors);
         try {
            _manager.SetPixelColor(x, y, color);
         } catch (Exception e) {
            e.printStackTrace();
         }

         samp++;
         if (samp == _samples) {
            x++;
            samp = 0;
            _manager.UpdateCanvas();
         }
         if (x == _x) {

            x = 0;
            y = GetNextLine();
         }
      }*/
   }


   public synchronized int GetNextLine() {
      if (_yPointer < _y) {
         return _yPointer++;
      }

      return -1;
   }

   public Ray[] GetCameraRaysForLine(int y) {
      java.util.List<Ray> lineRays = new ArrayList<Ray>(_samples * _x);

      Ray[] pixelRays;
      for (int i = 0; i < _x; i++) {
         //pixelRays = _scene.Camera.getInitialStochasticRaysForPixel(i, y, _samples);
         //lineRays.addAll(Arrays.asList(pixelRays));
      }

      return lineRays.toArray(new Ray[lineRays.size()]);
   }
}
