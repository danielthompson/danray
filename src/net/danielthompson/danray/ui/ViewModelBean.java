package net.danielthompson.danray.ui;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

public class ViewModelBean implements Serializable {

   private String cameraOrigin;

   private String cameraDirection;

   private String mouse;

   private String scene;


   public ViewModelBean() {
   }

   public String getCameraOrigin() {
      return cameraOrigin;
   }

   public void setCameraOrigin(String cameraOrigin) {
      this.cameraOrigin = cameraOrigin;
   }

   public String getCameraDirection() {
      return cameraDirection;
   }

   public void setCameraDirection(String cameraDirection) {
      this.cameraDirection = cameraDirection;
   }

   public String getMouse() {
      return mouse;
   }

   public void setMouse(String mouse) {
      this.mouse = mouse;
   }

   public String getScene() {
      return scene;
   }

   public void setScene(String scene) {
      this.scene = scene;
   }

}