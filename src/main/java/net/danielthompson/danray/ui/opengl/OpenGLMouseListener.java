package net.danielthompson.danray.ui.opengl;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Created by daniel on 3/8/16.
 */
public class OpenGLMouseListener implements MouseListener, MouseMotionListener {

   private final OpenGLCameraState _cameraState;
   private boolean _first = true;

   public OpenGLMouseListener(OpenGLCameraState cameraState) {

      _cameraState = cameraState;
   }

   @Override
   public void mouseClicked(MouseEvent mouseEvent) {

      resetMousePosition(mouseEvent);
      _cameraState.hasFocus = true;


   }

   @Override
   public void mousePressed(MouseEvent mouseEvent) {

   }

   @Override
   public void mouseReleased(MouseEvent mouseEvent) {

   }

   @Override
   public void mouseDragged(MouseEvent e) {

   }

   @Override
   public void mouseMoved(MouseEvent mouseEvent) {

//      if (CameraState.hasFocus) {
//         if (_first) {
//            _first = false;
//
//         } else {
//            // mouse x - rotate about y axis
//
//            float xDiff = (mouseEvent.getX() - CameraState.PrevMouseX) / 5.0f;
//            CameraState._yRotation += xDiff;
//            if (CameraState._yRotation >= 360)
//               CameraState._yRotation %= 360;
//
//            while (CameraState._yRotation < 0) {
//               CameraState._yRotation += 360;
//            }
//
//            // mouse y - rotate about x axis
//
//            float yDiff = (mouseEvent.getY() - CameraState.PrevMouseY) / 5.0f;
//            CameraState._xRotation += yDiff;
//
//            if (CameraState._xRotation >= 360)
//               CameraState._xRotation %= 360;
//
//            while (CameraState._xRotation < 0) {
//               CameraState._xRotation += 360;
//            }
//         }
//         resetMousePosition(mouseEvent);
         /*CameraState.PrevMouseX = mouseEvent.getX();
         CameraState.PrevMouseY = mouseEvent.getY();*/
//      }

   }

   private void resetMousePosition(MouseEvent event) {
      _cameraState.PrevMouseX = event.getX();
      _cameraState.PrevMouseY = event.getY();
   }
//
//   @Override
//   public void mouseDragged(MouseEvent mouseEvent) {
//
//   }
//
//   @Override
//   public void mouseWheelMoved(MouseEvent mouseEvent) {
//
//   }

   @Override
   public void mouseEntered(MouseEvent mouseEvent) {
      resetMousePosition(mouseEvent);
   }

   @Override
   public void mouseExited(MouseEvent mouseEvent) {

   }

}
