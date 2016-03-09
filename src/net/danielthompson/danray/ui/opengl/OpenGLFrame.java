package net.danielthompson.danray.ui.opengl;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import net.danielthompson.danray.structures.Scene;

import javax.swing.*;


/**
 * Created by daniel on 3/8/16.
 */
public class OpenGLFrame extends JFrame {
   public OpenGLCanvas Canvas;

   public OpenGLFrame(Scene scene) {
      super("OpenGL View");
      GLProfile.initSingleton();
      GLProfile glp = GLProfile.getDefault();
      GLCapabilities caps = new GLCapabilities(glp);
      caps.setHardwareAccelerated(true);
      //caps.setDoubleBuffered(true);
      Canvas = new OpenGLCanvas(caps, scene);

      //Add the split pane to this panel.
      add(Canvas);
   }
}
