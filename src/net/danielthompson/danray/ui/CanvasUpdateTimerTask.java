package net.danielthompson.danray.ui;

import java.awt.*;
import java.util.TimerTask;

/**
 * Created by daniel on 5/10/15.
 */
public class CanvasUpdateTimerTask extends TimerTask {
   private final MainCanvas _traceCanvas;
   private final Graphics _traceGraphics;
   private final CountCanvas _countCanvas;
   private final Graphics _countGraphics;

   public CanvasUpdateTimerTask(MainCanvas traceCanvas, Graphics traceGraphics, CountCanvas countCanvas, Graphics countGraphics) {

      _traceCanvas = traceCanvas;
      _traceGraphics = traceGraphics;
      _countCanvas = countCanvas;
      _countGraphics = countGraphics;
   }

   @Override
   public void run() {
      if (_traceCanvas != null)
         _traceCanvas.update(_traceGraphics);
      if (_countCanvas != null)
         _countCanvas.update(_countGraphics);
   }
}
