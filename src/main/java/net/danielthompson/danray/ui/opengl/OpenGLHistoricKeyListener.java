package net.danielthompson.danray.ui.opengl;

import net.danielthompson.danray.TraceManager;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by daniel on 3/8/16.
 */
public class OpenGLHistoricKeyListener extends TimerTask implements KeyListener {

   private final OpenGLCameraState _cameraState;

   private int _keyPressHistoryIndex = 0;

   private final int _keyPressHistorySize = 16;

   private final float _oneOverKeyPressHistorySize = 1.0f / (float)_keyPressHistorySize;

   private static Timer _timer = new Timer();

   private HashMap<Integer, KeyHistory> _keyPressHistories;

   private final float _sensitivityFactor = 10.0f;

   private TraceManager _traceManager;

   public OpenGLHistoricKeyListener(OpenGLCameraState cameraState, TraceManager traceManager) {

      _cameraState = cameraState;
      _traceManager = traceManager;
      _timer.schedule(this, 0, 50);

      _keyPressHistories = new HashMap<>();
      _keyPressHistories.put(KeyEvent.VK_W, new KeyHistory());
      _keyPressHistories.put(KeyEvent.VK_S, new KeyHistory());
      _keyPressHistories.put(KeyEvent.VK_A, new KeyHistory());
      _keyPressHistories.put(KeyEvent.VK_D, new KeyHistory());
      _keyPressHistories.put(KeyEvent.VK_Q, new KeyHistory());
      _keyPressHistories.put(KeyEvent.VK_E, new KeyHistory());
   }

   private class KeyHistory {
      private boolean[] _presses = new boolean[_keyPressHistorySize];

      private int _startIndex;

      public void StartPress() {
         _presses[_keyPressHistoryIndex] = true;
         _startIndex = _keyPressHistoryIndex;
      }

      public void EndPress() {

         // we haven't run off the end of the array
         if (_startIndex < _keyPressHistoryIndex) {
            for (int i = _startIndex; i < _keyPressHistoryIndex; i++)
               _presses[i] = true;
         }

         // if we have
         else {
            for (int i = _startIndex; i < _keyPressHistorySize; i++)
               _presses[i] = true;

            for (int i = 0; i < _startIndex; i++) {
               _presses[i] = true;
            }
         }
      }

      public float GetPercentage() {
         float accumulator = 0.0f;


         /*
         for (int i = _keyPressHistoryIndex; i < _keyPressHistorySize; i++) {

         }
         */
         for (int i = 0; i < _keyPressHistorySize; i++) {
            if (_presses[i])
               accumulator++;
         }

         return (accumulator * _oneOverKeyPressHistorySize);
      }

      public void NoPress() {
         _presses[_keyPressHistoryIndex] = false;
      }
   }

   @Override
   public void run() {
      if (_keyPressHistoryIndex == _keyPressHistorySize - 1)
         _keyPressHistoryIndex = 0;
      else
         _keyPressHistoryIndex++;

      for (int keyCode : _keyPressHistories.keySet()) {
         _keyPressHistories.get(keyCode).NoPress();
      }

      processKeys();
   }

   public void processKeys() {

//      for (int keyCode : _keyPressHistories.keySet()) {
//         KeyHistory history = _keyPressHistories.get(keyCode);
//         float percentage = history.GetPercentage() * _sensitivityFactor;
//
//         float rad = (float) Math.toRadians(CameraState._yRotation);
//         float cos = (float) (Math.cos(rad) * percentage);
//         float sin = (float) (Math.sin(rad) * percentage);
//
//         switch (keyCode) {
//            case KeyEvent.VK_W: {
//               CameraState._position[0] += sin;
//               CameraState._position[2] -= cos;
//               break;
//            }
//            case KeyEvent.VK_S: {
//               CameraState._position[0] -= sin;
//               CameraState._position[2] += cos;
//               break;
//            }
//            case KeyEvent.VK_A: {
//               CameraState._position[0] -= cos;
//               CameraState._position[2] -= sin;
//               break;
//            }
//            case KeyEvent.VK_D: {
//               CameraState._position[0] += cos;
//               CameraState._position[2] += sin;
//               break;
//            }
//            case KeyEvent.VK_E: {
//               CameraState._yRotation += 10;
//               if (CameraState._yRotation >= 360)
//                  CameraState._yRotation %= 360;
//               break;
//            }
//            case KeyEvent.VK_Q: {
//               CameraState._yRotation -= 10;
//               while (CameraState._yRotation <= 0)
//                  CameraState._yRotation += 360;
//               break;
//            }
//         }
//      }
   }

   @Override
   public void keyTyped(KeyEvent e) {

   }



   @Override
   public void keyPressed(KeyEvent e) {

      int keyCode = e.getKeyCode();

      if (keyCode == KeyEvent.VK_ESCAPE) {
         _cameraState.hasFocus = false;
         // OpenGLCanvas.animator.stop();
         //_canvas.destroy();
      }

      else if (keyCode == KeyEvent.VK_W
            || keyCode == KeyEvent.VK_S
            || keyCode == KeyEvent.VK_A
            || keyCode == KeyEvent.VK_D
            || keyCode == KeyEvent.VK_Q
            || keyCode == KeyEvent.VK_E)
      {
         KeyHistory history = _keyPressHistories.get(keyCode);
         history.StartPress();
      }
//
//      System.out.println("pos: " + CameraState._position[0] + " " + CameraState._position[1] + " " +  CameraState._position[2]);
//      System.out.println("y rot: " + CameraState._yRotation);
   }

   @Override
   public void keyReleased(KeyEvent e) {
      int keyCode = e.getKeyCode();

      if (keyCode == KeyEvent.VK_W
         || keyCode == KeyEvent.VK_S
         || keyCode == KeyEvent.VK_A
         || keyCode == KeyEvent.VK_D
         || keyCode == KeyEvent.VK_Q
         || keyCode == KeyEvent.VK_E)
      {
         KeyHistory history = _keyPressHistories.get(keyCode);
         history.EndPress();
      }
   }
}