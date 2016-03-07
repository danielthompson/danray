package net.danielthompson.danray.ui;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
/*import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.MouseListener;
*/


import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.gl2.GLUT;
import net.danielthompson.danray.structures.Scene;

import java.awt.event.*;

import static com.jogamp.opengl.GL.*;

/**
 * Created by daniel on 3/4/14.
 */
public class OpenGLCanvas extends GLCanvas implements MouseListener, MouseMotionListener, GLEventListener, KeyListener {

   private GLU _glu;
   private GLUT _glut;
   public static Animator animator;
   private Scene _scene;

   public OpenGLCanvas(GLCapabilities caps, Scene scene) {
      super(caps);
      this._scene = scene;
      this.addGLEventListener(this);
      this.addMouseListener(this);
      this.addMouseMotionListener(this);
      this.addKeyListener(this);
      _glu = new GLU();
      _glut = new GLUT();
      animator = new Animator(this);

      animator.start();

   }

   @Override
   public void mouseClicked(MouseEvent mouseEvent) {

   }

   @Override
   public void mousePressed(MouseEvent mouseEvent) {

   }

   @Override
   public void mouseReleased(MouseEvent mouseEvent) {

   }

   private double _prevX;
   private double _prevY;

   private boolean _first = true;

   @Override
   public void mouseDragged(MouseEvent e) {

   }

   @Override
   public void mouseMoved(MouseEvent mouseEvent) {
      if (_first) {
         _first = false;

      }

      else  {
         // mouse x - rotate about y axis

         double xDiff = (mouseEvent.getX() - _prevX) / 5.0;
         _yRotation += xDiff;
         if (_yRotation >= 360)
            _yRotation %= 360;

         while (_yRotation < 0) {
            _yRotation += 360;
         }

         // mouse y - rotate about x axis

         double yDiff = (mouseEvent.getY() - _prevY) / 5.0;
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
      _prevX = mouseEvent.getX();
      _prevY = mouseEvent.getY();
   }

   @Override
   public void mouseExited(MouseEvent mouseEvent) {

   }

   private long start, now;

   @Override
   public void init(GLAutoDrawable drawable) {
      System.out.println("init");

      GL2 gl = drawable.getGL().getGL2();
      gl.glEnable(GL.GL_DEPTH_TEST);

      boolean isGL2 = gl.isGL2();
      boolean isGL3 = gl.isGL3();
      boolean isGL4 = gl.isGL4();

      _position[0] = _scene.Camera._currentOrientation.Origin.X;
      _position[1] = _scene.Camera._currentOrientation.Origin.Y;
      _position[2] = _scene.Camera._currentOrientation.Origin.Z;

      _direction[0] = _scene.Camera._currentOrientation.Direction.X;
      _direction[1] = _scene.Camera._currentOrientation.Direction.Y;
      _direction[2] = _scene.Camera._currentOrientation.Direction.Z;

      start = System.currentTimeMillis();
   }

   @Override
   public void dispose(GLAutoDrawable drawable) {
      System.exit(0);
   }


   void drawOverlay(GL2 gl) {
      gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
      gl.glPushMatrix();
      gl.glLoadIdentity();
      _glu.gluOrtho2D(0, 1024, 0, 720);
      gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
      gl.glPushMatrix();
      gl.glLoadIdentity();
      gl.glColor3f(1, 0, 0);
      gl.glDisable(GL_DEPTH_TEST);

      int font = GLUT.BITMAP_HELVETICA_18;
      gl.glRasterPos2i(10, 50);
      _glut.glutBitmapString(font, String.format("%.2f", _position[0]));
      gl.glRasterPos2i(10, 30);
      _glut.glutBitmapString(font, String.format("%.2f", _position[1]));
      gl.glRasterPos2i(10, 10);
      _glut.glutBitmapString(font, String.format("%.2f", _position[2]));



      gl.glEnable(GL_DEPTH_TEST);
      gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
      gl.glPopMatrix();
      gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
      gl.glPopMatrix();

   }

   void drawString(GL2 gl, String string) {
      gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
      gl.glPushMatrix();
      gl.glLoadIdentity();
      _glu.gluOrtho2D(0, 1024, 0, 720);
      gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
      gl.glPushMatrix();
      gl.glLoadIdentity();
      gl.glColor3f(1, 0, 0);
      gl.glDisable(GL_DEPTH_TEST);
      gl.glRasterPos2i(10, 10);
      int font = GLUT.BITMAP_HELVETICA_18;

      _glut.glutBitmapString(font, string);

      gl.glEnable(GL_DEPTH_TEST);
      gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
      gl.glPopMatrix();
      gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
      gl.glPopMatrix();

   }

   @Override
   public void display(GLAutoDrawable drawable) {
       //System.out.println("display");

      GL2 gl = drawable.getGL().getGL2();

      gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);

      gl.glLoadIdentity();

      gl.glRotatef((float)_xRotation, 1, 0, 0);
      gl.glRotatef((float)_yRotation, 0, 1, 0);
      gl.glTranslatef((float) -_position[0], (float) -_position[1], (float) -_position[2]);

      gl.glClearColor(0f, .33f, 0.66f, 1f);
      gl.glClearDepthf(1f);
      gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

      gl.glBegin(GL.GL_TRIANGLES);
      {
         gl.glColor3f(1, 0, 0);
         gl.glVertex3f(300, 2.5f, -2);
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



      drawOverlay(gl);
      checkError(gl, "display");
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


   private double _xRotation = 0;
   private double _yRotation = 0;
   private double _zRotation = 0;

   private double[] _position = {0, 0, 20};
   private double[] _direction = {0, 0, -1};

   @Override
   public void keyTyped(KeyEvent e) {

   }

   @Override
   public void keyPressed(KeyEvent e) {

      int keyCode = e.getKeyCode();

      switch (keyCode) {
         case KeyEvent.VK_ESCAPE: {
            animator.stop();
            destroy();
            break;
         }
         case KeyEvent.VK_W: {
            double rad = Math.toRadians(_yRotation);
            double cos = Math.cos(rad);
            double sin = Math.sin(rad);

            _position[0] += sin;
            _position[2] -= cos;
            break;
         }
         case KeyEvent.VK_S: {
            double rad = Math.toRadians(_yRotation);
            double cos = Math.cos(rad);
            double sin = Math.sin(rad);

            _position[0] -= sin;
            _position[2] += cos;
            break;
         }

         case KeyEvent.VK_A: {
            double rad = Math.toRadians(_yRotation);
            double cos = Math.cos(rad);
            double sin = Math.sin(rad);

            _position[0] -= cos;
            _position[2] -= sin;
            break;
         }
         case KeyEvent.VK_D: {
            double rad = Math.toRadians(_yRotation);
            double cos = Math.cos(rad);
            double sin = Math.sin(rad);

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
}
