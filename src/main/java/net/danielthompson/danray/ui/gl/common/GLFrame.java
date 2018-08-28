package net.danielthompson.danray.ui.gl.common;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import net.danielthompson.danray.scenes.AbstractScene;
import net.danielthompson.danray.ui.gl.gl2.GL2Canvas;
import net.danielthompson.danray.ui.gl.gl3.GL3Canvas;

import java.awt.*;


/**
 * Created by daniel on 3/8/16.
 */
public class GLFrame extends Frame {
   public AbstractGLCanvas Canvas;

   public GLFrame(AbstractScene scene) {
      super("OpenGL View");
      GLProfile.initSingleton();

      String profileToUse = GLProfile.GL3;

      GLProfile profile = GLProfile.get(profileToUse);
      GLCapabilities caps = new GLCapabilities(profile);

      caps.setHardwareAccelerated(true);
      //caps.setDoubleBuffered(true);s

      switch (profileToUse) {
         case GLProfile.GL3: {
            Canvas = new GL3Canvas(caps, scene);
            break;
         }
         default: {
            Canvas = new GL2Canvas(caps, scene);
            break;
         }
      }

      Canvas.setFocusable(true);

      add(Canvas);

      Canvas.addListeners(this);
   }
}