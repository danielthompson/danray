package net.danielthompson.danray.ui;

import java.awt.*;
import java.util.TimerTask;

/**
 * Created by daniel on 5/10/15.
 */
public class CanvasUpdateTimerTask extends TimerTask {
   private final Canvas _canvas;
   private final Graphics _graphics;


   public CanvasUpdateTimerTask(Canvas canvas, Graphics graphics) {

      _canvas = canvas;
      _graphics = graphics;
   }

   @Override
   public void run() {
      if (_canvas != null)
         _canvas.update(_graphics);
   }
}
