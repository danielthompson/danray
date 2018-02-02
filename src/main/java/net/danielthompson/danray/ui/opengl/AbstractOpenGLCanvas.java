package net.danielthompson.danray.ui.opengl;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.gl2.GLUT;
import net.danielthompson.danray.acceleration.KDNode;
import net.danielthompson.danray.scenes.AbstractScene;
import net.danielthompson.danray.shapes.AbstractShape;

import static com.jogamp.opengl.GL.*;

public abstract class AbstractOpenGLCanvas extends GLCanvas  implements GLEventListener {

   public static Animator animator;
   protected static float OneOver255 = 1 / 255.0f;
   public AbstractScene Scene;
   public KDNode SelectedNode;
   protected GLU GLU;
   protected GLUT GLUT;
   protected OpenGLKeyListener KeyListener;
   protected OpenGLMouseListener MouseListener;
   protected OpenGLCameraState CameraState;

   public AbstractOpenGLCanvas(GLCapabilities caps, AbstractScene scene) {
      super(caps);

      addGLEventListener(this);

      MouseListener = new OpenGLMouseListener(CameraState);
      Scene = scene;
      GLU = new GLU();
      GLUT = new GLUT();
      CameraState = new OpenGLCameraState();
      KeyListener = new OpenGLKeyListener(CameraState);
   }

   public void SetNode(KDNode node) {
      SelectedNode = node;

      for (AbstractShape shape : Scene.Shapes)
         shape.SetInCurrentKDNode(false);

      for (AbstractShape shape : node.Shapes)
         shape.SetInCurrentKDNode(true);
   }

   protected boolean checkError(GL gl, String title) {

      int error = gl.glGetError();
      if (error != GL_NO_ERROR) {
         String errorString;
         switch (error) {
            case GL_INVALID_ENUM:
               errorString = "GL_INVALID_ENUM";
               break;
            case GL_INVALID_VALUE:
               errorString = "GL_INVALID_VALUE";
               break;
            case GL_INVALID_OPERATION:
               errorString = "GL_INVALID_OPERATION";
               break;
            case GL_INVALID_FRAMEBUFFER_OPERATION:
               errorString = "GL_INVALID_FRAMEBUFFER_OPERATION";
               break;
            case GL_OUT_OF_MEMORY:
               errorString = "GL_OUT_OF_MEMORY";
               break;
            default:
               errorString = "UNKNOWN";
               break;
         }
         System.out.println("OpenGL Error(" + errorString + "): " + title);
         throw new Error();
      }
      return error == GL_NO_ERROR;
   }
}
