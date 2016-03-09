package net.danielthompson.danray.ui.opengl;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;


import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.gl2.GLUT;
import net.danielthompson.danray.acceleration.KDNode;
import net.danielthompson.danray.acceleration.KDScene;
import net.danielthompson.danray.lights.PointLight;
import net.danielthompson.danray.lights.Radiatable;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shapes.Box;
import net.danielthompson.danray.shapes.Shape;
import net.danielthompson.danray.shapes.Sphere;
import net.danielthompson.danray.structures.BoundingBox;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Scene;


import static com.jogamp.opengl.GL.*;

/**
 * Created by daniel on 3/4/14.
 */
public class OpenGLCanvas extends GLCanvas implements GLEventListener{

   private GLU _glu;
   private GLUT _glut;
   public static Animator animator;
   public Scene _scene;

   private OpenGLKeyListener _keyListener;
   private OpenGLMouseListener _mouseListener;
   private OpenGLCameraState _cameraState;

   public KDNode SelectedNode;

   private static float OneOver255 = 1 / 255.0f;

   public OpenGLCanvas(GLCapabilities caps, Scene scene) {
      super(caps);
      _scene = scene;
      addGLEventListener(this);

      _cameraState = new OpenGLCameraState();

      _keyListener = new OpenGLKeyListener(_cameraState);
      _mouseListener = new OpenGLMouseListener(_cameraState);

      addMouseListener(_mouseListener);
      addMouseMotionListener(_mouseListener);
      addKeyListener(_keyListener);

      _glu = new GLU();
      _glut = new GLUT();
      animator = new Animator(this);

      animator.start();

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

      _cameraState._position[0] = _scene.Camera._currentOrientation.Origin.X;
      _cameraState._position[1] = _scene.Camera._currentOrientation.Origin.Y;
      _cameraState._position[2] = _scene.Camera._currentOrientation.Origin.Z;

      _cameraState._direction[0] = _scene.Camera._currentOrientation.Direction.X;
      _cameraState._direction[1] = _scene.Camera._currentOrientation.Direction.Y;
      _cameraState._direction[2] = _scene.Camera._currentOrientation.Direction.Z;

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
      gl.glRasterPos2i(10, 90);
      _glut.glutBitmapString(font, String.format("%.2f", _cameraState._position[0]));
      gl.glRasterPos2i(10, 50);
      _glut.glutBitmapString(font, String.format("%.2f", _cameraState._position[1]));
      gl.glRasterPos2i(10, 10);
      _glut.glutBitmapString(font, String.format("%.2f", _cameraState._position[2]));

      gl.glEnable(GL_DEPTH_TEST);
      gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
      gl.glPopMatrix();
      gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
      gl.glPopMatrix();
      gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
   }

   @Override
   public void display(GLAutoDrawable drawable) {
       //System.out.println("display");

      GL2 gl = drawable.getGL().getGL2();

      gl.glEnable(gl.GL_LIGHTING);
      gl.glEnable(gl.GL_LIGHT0);
      gl.glEnable(gl.GL_COLOR_MATERIAL);

      // camera
      gl.glLoadIdentity();
      gl.glRotatef((float)_cameraState._xRotation, 1, 0, 0);
      gl.glRotatef((float)_cameraState._yRotation, 0, 1, 0);
      gl.glTranslatef((float) -_cameraState._position[0], (float) -_cameraState._position[1], (float) -_cameraState._position[2]);

      // background
      gl.glClearColor(0f, .11f, 0.22f, 1f);
      gl.glClearDepthf(1f);
      gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

      // scene shapes
      GLUquadric quadric = _glu.gluNewQuadric();

      for (Shape shape : _scene.shapes) {
         if (shape instanceof Sphere) {
            Sphere sphere = (Sphere)shape;
            Point origin = sphere.Origin;
            gl.glTranslatef(((float)origin.X), ((float)origin.Y), ((float)origin.Z));

            if (sphere.InCurrentKDNode)
               gl.glColor3f(0.2f, 0.2f, 0.2f);
            else
               gl.glColor3f(0.2f, 1.0f, 1.0f);
               //setColor(gl, sphere.Material);
            _glu.gluSphere(quadric, sphere.Radius, 10, 10);
            gl.glTranslatef(-((float)origin.X), -((float)origin.Y), -((float)origin.Z));
         }
         else if (shape instanceof Box) {
            drawBox(gl, (Box)shape);
         }
      }

      // lights

      for (Radiatable radiatable : _scene.Radiatables) {
         if (radiatable instanceof PointLight) {
            PointLight light = (PointLight)radiatable;
            float[] lightpos = {(float)(light._location.X), (float)(light._location.Y), (float)(light._location.Z)};
            gl.glLightfv(gl.GL_LIGHT0, gl.GL_POSITION, lightpos, 0);

         }
      }

      // kd nodes

      if (_scene instanceof KDScene) {
         KDScene scene = (KDScene)_scene;
         gl.glEnable(GL.GL_BLEND); //Enable blending.
         gl.glDepthMask (false);
         gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA); //Set blending function.

         DrawNodes(gl, scene.rootNode);
         gl.glDepthMask (true);
         gl.glDisable(GL.GL_BLEND);
      }

      drawOverlay(gl);
      checkError(gl, "display");
      //drawable.swapBuffers();
   }

   private void DrawNodes(GL2 gl, KDNode node) {
      if (node != null) {
         if (node.equals(SelectedNode)) {
            gl.glColor4f(.25f, 0.25f, 1.0f, .5f);
            drawBoundingBox(gl, node._box);
         }
         else {
            //gl.glColor4f(0.2f, 0.2f, 0.2f, .2f);
            if (node._leftChild != null)
               DrawNodes(gl, node._leftChild);
            if (node._rightChild != null) {
               DrawNodes(gl, node._rightChild);
            }
         }
      }
   }

   public void SetNode(KDNode node) {
      SelectedNode = node;

      for (Shape shape : _scene.shapes)
         shape.SetInCurrentKDNode(false);

      for (Shape shape : node._objects)
         shape.SetInCurrentKDNode(true);
   }

   private void setColor(GL2 gl, Material material) {

      float red = material.Color.getRed() * OneOver255;
      float green = material.Color.getGreen() * OneOver255;
      float blue = material.Color.getBlue() * OneOver255;

      gl.glColor3f(red, green, blue);
   }

   private void drawBoundingBox(GL2 gl, BoundingBox box) {

      float p0x = (float)box.point1.X;
      float p0y = (float)box.point1.Y;
      float p0z = (float)box.point1.Z;
      float p1x = (float)box.point2.X;
      float p1y = (float)box.point2.Y;
      float p1z = (float)box.point2.Z;
      drawBox(gl, p0x, p0y, p0z, p1x, p1y, p1z);
   }

   private void drawBox(GL2 gl, Box box) {

      float p0x = (float)box.point1.X;
      float p0y = (float)box.point1.Y;
      float p0z = (float)box.point1.Z;
      float p1x = (float)box.point2.X;
      float p1y = (float)box.point2.Y;
      float p1z = (float)box.point2.Z;

      setColor(gl, box.Material);

      drawBox(gl, p0x, p0y, p0z, p1x, p1y, p1z);
   }

   private void drawBox(GL2 gl, float p0x, float p0y, float p0z, float p1x, float p1y, float p1z) {
      gl.glBegin(GL.GL_TRIANGLE_STRIP);

      // front face
      gl.glNormal3f(0, 0, 1);
      gl.glVertex3f(p0x, p0y, p1z);
      gl.glVertex3f(p0x, p1y, p1z);
      gl.glVertex3f(p1x, p0y, p1z);
      gl.glVertex3f(p1x, p1y, p1z);

      // right face
      gl.glNormal3f(1, 0, 0);
      gl.glVertex3f(p1x, p0y, p0z);
      gl.glVertex3f(p1x, p1y, p0z);

      // back face
      gl.glNormal3f(0, 0, -1);
      gl.glVertex3f(p0x, p0y, p0z);
      gl.glVertex3f(p0x, p1y, p0z);

      // left face
      gl.glNormal3f(-1, 0, 0);
      gl.glVertex3f(p0x, p0y, p1z);
      gl.glVertex3f(p0x, p1y, p1z);

      gl.glEnd();

      // top face
      gl.glBegin(GL.GL_TRIANGLE_STRIP);
      gl.glNormal3f(0, 1, 0);
      gl.glVertex3f(p0x, p1y, p1z);
      gl.glVertex3f(p0x, p1y, p0z);
      gl.glVertex3f(p1x, p1y, p1z);
      gl.glVertex3f(p1x, p1y, p0z);
      gl.glEnd();

      // bottom face
      gl.glBegin(GL.GL_TRIANGLE_STRIP);
      gl.glNormal3f(0, -1, 0);
      gl.glVertex3f(p0x, p0y, p0z);
      gl.glVertex3f(p0x, p0y, p1z);
      gl.glVertex3f(p1x, p0y, p0z);
      gl.glVertex3f(p1x, p0y, p1z);
      gl.glEnd();
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
      _glu.gluPerspective(80.0, (float)width / (float)height, 1.0, 1000.0);
      gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);

   }



}
