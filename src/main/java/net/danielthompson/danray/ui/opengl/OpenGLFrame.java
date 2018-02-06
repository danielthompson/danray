package net.danielthompson.danray.ui.opengl;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import net.danielthompson.danray.scenes.AbstractScene;

import javax.swing.*;


/**
 * Created by daniel on 3/8/16.
 */
public class OpenGLFrame extends JFrame {
   public AbstractOpenGLCanvas Canvas;

   public OpenGLFrame(AbstractScene scene) {
      super("OpenGL View");
      GLProfile.initSingleton();

      String profileToUse = GLProfile.GL3;

      GLProfile profile = GLProfile.get(profileToUse);
      GLCapabilities caps = new GLCapabilities(profile);

      caps.setHardwareAccelerated(true);
      //caps.setDoubleBuffered(true);s

      switch (profileToUse) {
         case GLProfile.GL3: {
            Canvas = new OpenGL3Canvas(caps, scene);
            break;
         }
         default: {
            Canvas = new OpenGLCanvas(caps, scene);
            break;
         }
      }

      //Add the split pane to this panel.
      add(Canvas);
   }
}
