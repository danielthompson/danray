/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.danielthompson.danray.utility;

import com.jogamp.nativewindow.util.Dimension;
import com.jogamp.newt.Display;
import com.jogamp.newt.NewtFactory;
import com.jogamp.newt.Screen;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.gl2.GLUT;

import static com.jogamp.opengl.GL.*;

/**
 * @author gbarbieri
 */
public class ModifiedTriangle implements GLEventListener, KeyListener, MouseListener {

   private static int screenIdx = 0;
   private static Dimension windowSize = new Dimension(1024, 768);
   private static boolean undecorated = false;
   private static boolean alwaysOnTop = false;
   private static boolean fullscreen = false;
   private static boolean mouseVisible = true;
   private static boolean mouseConfined = false;
   public static GLWindow glWindow;
   public static Animator animator;

   public static void main(String[] args) {

      Display display = NewtFactory.createDisplay(null);
      Screen screen = NewtFactory.createScreen(display, screenIdx);
      GLProfile glProfile = GLProfile.getDefault();
      GLCapabilities glCapabilities = new GLCapabilities(glProfile);
      glWindow = GLWindow.create(screen, glCapabilities);

      glWindow.setSize(windowSize.getWidth(), windowSize.getHeight());
      glWindow.setPosition(50, 50);
      glWindow.setUndecorated(undecorated);
      glWindow.setAlwaysOnTop(alwaysOnTop);
      glWindow.setFullscreen(fullscreen);
      glWindow.setPointerVisible(mouseVisible);
      glWindow.confinePointer(mouseConfined);
      glWindow.setVisible(true);

      ModifiedTriangle helloTriangle = new ModifiedTriangle();
      glWindow.addGLEventListener(helloTriangle);
      glWindow.addMouseListener(helloTriangle);
      glWindow.addKeyListener(helloTriangle);

      animator = new Animator(glWindow);
      animator.start();
   }

   private long start, now;

   private GLU _glu;
   private GLUT _glut;

   private float _xRotation = 0;
   private float _yRotation = 0;
   private float _zRotation = 0;

   private float[] _position = {0, 0, 20};

   public ModifiedTriangle() {

      _glu = new GLU();
   }

   @Override
   public void init(GLAutoDrawable drawable) {
      System.out.println("init");

      GL2 gl = drawable.getGL().getGL2();
      gl.glEnable(GL.GL_DEPTH_TEST);

      boolean isGL2 = gl.isGL2();
      boolean isGL3 = gl.isGL3();
      boolean isGL4 = gl.isGL4();

      start = System.currentTimeMillis();
   }


   @Override
   public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
      System.out.println("reshape");
      GL2 gl = drawable.getGL().getGL2();
      /**
       Just the glViewport for this sample, normally here you update your
       projection matrix.
       */
      gl.glViewport(x, y, width, height);
      gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
      gl.glLoadIdentity();
      _glu.gluPerspective(80.0, width / height, 1.0, 1000.0);
      gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);

   }

   @Override
   public void display(GLAutoDrawable drawable) {
      // System.out.println("display");

      GL2 gl = drawable.getGL().getGL2();

      gl.glLoadIdentity();
      //gl.glRotatef((float)_yRotation, 0, 1, 0);
      /*GLU.gluLookAt(_position[0], _position[1], _position[2],
            _sceneCenter[0], _sceneCenter[1], _sceneCenter[2],
            _up[0], _up[1], _up[2]);
*/

      gl.glRotatef((float)_xRotation, 1, 0, 0);
      gl.glRotatef((float)_yRotation, 0, 1, 0);
      gl.glTranslatef((float) -_position[0], (float) -_position[1], (float) -_position[2]);

      gl.glClearColor(0f, .33f, 0.66f, 1f);
      gl.glClearDepthf(1f);
      gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

      gl.glBegin(GL.GL_TRIANGLES);
      {
         gl.glColor3f(1, 0, 0);
         gl.glVertex3f(2, 2.5f, -2);
         gl.glColor3f(.5f, 1, .5f);
         gl.glVertex3f(-3.5f, -2.5f, -2);
         gl.glColor3f(0.25f, 0, .5f);
         gl.glVertex3f(2, -4, -2);
      }
      gl.glEnd();

      gl.glBegin(GL.GL_TRIANGLE_STRIP);
      {
         gl.glColor3f(.2f, .2f, .2f);
         gl.glVertex3f(-200, -10, -200);
         gl.glVertex3f(200, -10, -200);
         gl.glVertex3f(-200, -10, 200);
         gl.glVertex3f(200, -10, 200);
      }
      gl.glEnd();

      gl.glTranslatef(0.0f, 10.0f, 0.0f);

      gl.glColor3f(1, 1, 0);

      GLUquadric quadric = _glu.gluNewQuadric();
      _glu.gluSphere(quadric, 1, 100, 100);



      checkError(gl, "display");
   }

   @Override
   public void dispose(GLAutoDrawable glAutoDrawable) {
      System.exit(0);
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

   @Override
   public void keyPressed(KeyEvent e) {

      short keyCode = e.getKeyCode();

      switch (keyCode) {
         case KeyEvent.VK_ESCAPE: {
            ModifiedTriangle.animator.stop();
            ModifiedTriangle.glWindow.destroy();
            break;
         }
         case KeyEvent.VK_W: {
            float rad = (float) Math.toRadians(_yRotation);
            float cos = (float) Math.cos(rad);
            float sin = (float) Math.sin(rad);

            _position[0] += sin;
            _position[2] -= cos;
            break;
         }
         case KeyEvent.VK_S: {
            float rad = (float) Math.toRadians(_yRotation);
            float cos = (float) Math.cos(rad);
            float sin = (float) Math.sin(rad);

            _position[0] -= sin;
            _position[2] += cos;
            break;
         }

         case KeyEvent.VK_A: {
            float rad = (float) Math.toRadians(_yRotation);
            float cos = (float) Math.cos(rad);
            float sin = (float) Math.sin(rad);

            _position[0] -= cos;
            _position[2] -= sin;
            break;
         }
         case KeyEvent.VK_D: {
            float rad = (float) Math.toRadians(_yRotation);
            float cos = (float) Math.cos(rad);
            float sin = (float) Math.sin(rad);

            _position[0] += cos;
            _position[2] += sin;
            break;
         }

         case KeyEvent.VK_E: {
            _yRotation += 10;
            if (_yRotation >= 360)
               _yRotation %= 360;
            break;
         }
         case KeyEvent.VK_Q: {
            _yRotation -= 10;
            while (_yRotation <= 0)
               _yRotation += 360;
            break;
         }
      }

      System.out.println("pos: " + _position[0] + " " + _position[1] + " " +  _position[2]);
      System.out.println("y rot: " + _yRotation);
   }

   @Override
   public void keyReleased(KeyEvent e) {

   }

   @Override
   public void mouseClicked(MouseEvent mouseEvent) {

   }

   @Override
   public void mouseEntered(MouseEvent mouseEvent) {

   }

   @Override
   public void mouseExited(MouseEvent mouseEvent) {

   }

   @Override
   public void mousePressed(MouseEvent mouseEvent) {

   }

   @Override
   public void mouseReleased(MouseEvent mouseEvent) {

   }

   private float _prevX;
   private float _prevY;

   private boolean _first = true;

   @Override
   public void mouseMoved(MouseEvent mouseEvent) {

      if (_first) {
         _first = false;

      }
      else {
         // mouse x - rotate about y axis

         float xDiff = (mouseEvent.getX() - _prevX) / 5.0f;
         _yRotation += xDiff;
         if (_yRotation >= 360)
            _yRotation %= 360;

         while (_yRotation < 0) {
            _yRotation += 360;
         }

         // mouse y - rotate about x axis

         float yDiff = (mouseEvent.getY() - _prevY) / 5.0f;
         _xRotation += yDiff;

         if (_xRotation >= 360)
            _xRotation %= 360;

         while (_xRotation < 0) {
            _xRotation += 360;
         }
      }
      _prevX = mouseEvent.getX();
      _prevY = mouseEvent.getY();

   }

   @Override
   public void mouseDragged(MouseEvent mouseEvent) {

   }

   @Override
   public void mouseWheelMoved(MouseEvent mouseEvent) {

   }
}
