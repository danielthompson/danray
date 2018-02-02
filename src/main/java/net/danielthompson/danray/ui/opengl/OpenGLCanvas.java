package net.danielthompson.danray.ui.opengl;

import com.jogamp.opengl.*;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;


import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.gl2.GLUT;
import net.danielthompson.danray.SceneBuilder;
import net.danielthompson.danray.acceleration.KDNode;
import net.danielthompson.danray.acceleration.KDScene;
import net.danielthompson.danray.lights.AbstractLight;
import net.danielthompson.danray.lights.PointLight;
import net.danielthompson.danray.lights.SphereLight;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.shading.Spectrum;
import net.danielthompson.danray.shapes.AbstractShape;
import net.danielthompson.danray.shapes.Box;

import net.danielthompson.danray.shapes.Sphere;
import net.danielthompson.danray.structures.BoundingBox;
import net.danielthompson.danray.scenes.AbstractScene;
import net.danielthompson.danray.utility.GeometryCalculations;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.jogamp.opengl.GL.*;

/**
 * Created by daniel on 3/4/14.
 */
public class OpenGLCanvas extends AbstractOpenGLCanvas {


   public OpenGLCanvas(GLCapabilities caps, AbstractScene scene) {
      super(caps, scene);
      Scene = scene;
      addGLEventListener(this);

      CameraState = new OpenGLCameraState();

      CameraState.Camera = Scene.Camera;

      KeyListener = new OpenGLKeyListener(CameraState);
      MouseListener = new OpenGLMouseListener(CameraState);

      addMouseListener(MouseListener);
      addMouseMotionListener(MouseListener);
      addKeyListener(KeyListener);

      GLU = new GLU();
      GLUT = new GLUT();
      animator = new Animator(this);

      animator.start();

   }

   @Override
   public void init(GLAutoDrawable drawable) {
      System.out.println("init");

      GL2 gl = drawable.getGL().getGL2();
      gl.glEnable(GL.GL_DEPTH_TEST);

      gl.glClearColor(.25f, .25f, .25f, 1f);
      gl.glEnable(GL.GL_LINE_SMOOTH);

   }

   @Override
   public void dispose(GLAutoDrawable drawable) {
      System.exit(0);
   }

   void drawOverlay(GL2 gl) {
      gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
      gl.glPushMatrix();
      gl.glLoadIdentity();
      GLU.gluOrtho2D(0, 1024, 0, 720);
      gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
      gl.glPushMatrix();
      gl.glLoadIdentity();
      gl.glColor3f(1, 0, 0);
      gl.glDisable(GL_DEPTH_TEST);

      int font = GLUT.BITMAP_HELVETICA_18;
      gl.glRasterPos2i(10, 90);
      GLUT.glutBitmapString(font, String.format("%.2f", Scene.Camera.getOrigin().X));
      gl.glRasterPos2i(10, 50);
      GLUT.glutBitmapString(font, String.format("%.2f", Scene.Camera.getOrigin().Y));
      gl.glRasterPos2i(10, 10);
      GLUT.glutBitmapString(font, String.format("%.2f", Scene.Camera.getOrigin().Z));

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

//      gl.glEnable(GLLightingFunc.GL_LIGHTING);
//      gl.glEnable(GLLightingFunc.GL_LIGHT0);
//      gl.glEnable(GLLightingFunc.GL_COLOR_MATERIAL);

      // camera
      gl.glLoadIdentity();

      double[] colMajor = Scene.Camera.cameraToWorld._inverse.getColMajor();
      gl.glMultMatrixd(colMajor, 0);

      // background

      gl.glClearDepthf(1f);
      gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

      // scene Shapes
      GLUquadric quadric = GLU.gluNewQuadric();

      for (AbstractLight light : Scene.Lights) {
         if (light instanceof PointLight) {
            PointLight pointLight = (PointLight)light;
            float[] lightpos = {pointLight.Location.X, pointLight.Location.Y, pointLight.Location.Z};
            gl.glLightfv(gl.GL_LIGHT0, gl.GL_POSITION, lightpos, 0);

         }
      }

      if (Scene instanceof KDScene) {
         KDScene scene = (KDScene) Scene;
         gl.glEnable(GL.GL_BLEND); //Enable blending.
         gl.glDepthMask (false);
         gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA); //Set blending function.

         DrawNodes(gl, scene.rootNode);
         gl.glDepthMask (true);
         gl.glDisable(GL.GL_BLEND);
      }

      else {
         for (AbstractShape shape : Scene.Shapes) {
            gl.glPushMatrix();
            gl.glMultMatrixd(shape.ObjectToWorld._matrix.getColMajor(), 0);
            if (shape instanceof AbstractLight) {
               setColor(gl, ((AbstractLight) shape).SpectralPowerDistribution);
            } else {
               setColor(gl, shape.Material);
            }

            if (shape instanceof Sphere || shape instanceof SphereLight) {
            /*if (sphere.InCurrentKDNode)
               gl.glColor3f(0.2f, 0.2f, 0.2f);
            else
               gl.glColor3f(0.2f, 1.0f, 1.0f);*/

               GLU.gluSphere(quadric, 1, 100, 100);
               //gl.glTranslatef(-origin.X, -origin.Y, -origin.Z);
            } else if (shape instanceof Box) {
               //drawBox(gl, (Box) shape);
            }

            gl.glPopMatrix();
         }
      }

      drawOverlay(gl);
      checkError(gl, "display");
      //drawable.swapBuffers();
   }

   private void DrawNodes(GL2 gl, KDNode node) {

      List<KDNode> nodes = new ArrayList<>();
      nodes.add(node);

      List<Integer> depths = new ArrayList<>();
      depths.add(0);

      int maxDepth = node.GetMaxDepth();

      GLUquadric quadric = GLU.gluNewQuadric();

      while (nodes.size() > 0) {

         KDNode currentNode = nodes.remove(0);
         int depth = depths.remove(0);

         boolean leaf = (currentNode.LeftChild == null);

         if (leaf) {
            SpectralPowerDistribution red = new SpectralPowerDistribution(SceneBuilder.Solarized.red);
            SpectralPowerDistribution blue = new SpectralPowerDistribution(SceneBuilder.Solarized.blue);

            for (AbstractShape shape : currentNode.Shapes) {
               gl.glPushMatrix();
               gl.glMultMatrixd(shape.ObjectToWorld._matrix.getColMajor(), 0);

               Spectrum color;

               if (shape.InCurrentKDNode)
                  color = new Spectrum(SceneBuilder.Firenze.Beige);
               else
                  color = new Spectrum(SceneBuilder.Firenze.Yellow);

               SpectralPowerDistribution spd = new SpectralPowerDistribution();
               spd.R = color.R;
               spd.G = color.G;
               spd.B = color.B;

               setColor(gl, spd);

               if (shape instanceof Sphere || shape instanceof SphereLight) {

                  GLU.gluSphere(quadric, 1, 8, 8);
                  //gl.glTranslatef(-origin.X, -origin.Y, -origin.Z);
               }
               else if (shape instanceof Box) {
                  //drawBox(gl, (Box)shape);
               }

               gl.glPopMatrix();
            }
         }

         float depthPercentage = ((float)depth / (float)maxDepth);

         float r = SceneBuilder.Solarized.blue.getRed()  * OneOver255;
         float g = SceneBuilder.Solarized.blue.getGreen() * OneOver255;
         float b = SceneBuilder.Solarized.blue.getBlue() * OneOver255;
         float a = (1.0f - depthPercentage) * 0.5f;

         // draw current node
         if (currentNode.equals(SelectedNode)) {
            gl.glColor4f(r, g, b, a);
         }
         else {
            gl.glColor4f(r, g, b, a);
         }

         drawBoundingBox(gl, currentNode.BoundingBox, 0.5f);

         if (currentNode.LeftChild != null) {
            nodes.add(currentNode.LeftChild);
            depths.add(depth + 1);
         }
         if (currentNode.RightChild != null) {
            nodes.add(currentNode.RightChild);
            depths.add(depth + 1);
         }

      }


      if (SelectedNode != null) {
         gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

         drawBoundingBox(gl, SelectedNode.BoundingBox, 1.0f);
      }


   }



   private void setColor(GL2 gl, Material material) {

      float red = material.ReflectanceSpectrum.R;// * OneOver255;
      float green = material.ReflectanceSpectrum.G;// * OneOver255;
      float blue = material.ReflectanceSpectrum.B;// * OneOver255;

      gl.glColor3f(red, green, blue);
   }

   private void setColor(GL2 gl, SpectralPowerDistribution spd) {
      float red = spd.R;
      float green = spd.G;
      float blue = spd.B;

      GeometryCalculations.clamp(0, red, 1);
      GeometryCalculations.clamp(0, green, 1);
      GeometryCalculations.clamp(0, blue, 1);

      gl.glColor3f(red, green, blue);
   }

   private void setColor(GL2 gl, Color color) {

   }

   private void drawBoundingBox(GL2 gl, BoundingBox box, float width) {

      float p0x = box.point1.X;
      float p0y = box.point1.Y;
      float p0z = box.point1.Z;
      float p1x = box.point2.X;
      float p1y = box.point2.Y;
      float p1z = box.point2.Z;
      drawBoxAsLines(gl, p0x, p0y, p0z, p1x, p1y, p1z, width);
   }

   private void drawBoxAsLines(GL2 gl, float p0x, float p0y, float p0z, float p1x, float p1y, float p1z, float width) {

      //gl.glDisable(GL.GL_BLEND);

      if (width < 1.0f)
         width = 1.0f;

      gl.glLineWidth(width);

      gl.glBegin(GL_LINE_STRIP);

      gl.glVertex3f(p0x, p0y, p0z);
      gl.glVertex3f(p1x, p0y, p0z);

      gl.glVertex3f(p1x, p1y, p0z);

      gl.glVertex3f(p0x, p1y, p0z);

      gl.glVertex3f(p0x, p0y, p0z);

      gl.glEnd();

      gl.glBegin(GL.GL_LINE_STRIP);

      gl.glVertex3f(p0x, p0y, p1z);
      gl.glVertex3f(p1x, p0y, p1z);

      gl.glVertex3f(p1x, p1y, p1z);

      gl.glVertex3f(p0x, p1y, p1z);

      gl.glVertex3f(p0x, p0y, p1z);

      gl.glEnd();

      gl.glBegin(GL.GL_LINES);

      gl.glVertex3f(p0x, p0y, p0z);
      gl.glVertex3f(p0x, p0y, p1z);

      gl.glVertex3f(p1x, p0y, p0z);
      gl.glVertex3f(p1x, p0y, p1z);

      gl.glVertex3f(p0x, p1y, p0z);
      gl.glVertex3f(p0x, p1y, p1z);

      gl.glVertex3f(p1x, p1y, p0z);
      gl.glVertex3f(p1x, p1y, p1z);

      gl.glEnd();

      //gl.glEnable(GL.GL_BLEND);
   }

   private void drawBox(GL2 gl, Box box) {
      drawBox(gl, 0, 0, 0, 1, 1, 1);
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

      float fov = Scene.Camera.Settings.FieldOfView;
      float aspect = (float) Scene.Camera.Settings.X / (float) Scene.Camera.Settings.Y;
      float zNear = 1;
      float zFar = 10000;

      GLU.gluPerspective(fov, aspect, zNear, zFar);

      //GLU.gluPerspective(80.0, (float)width / (float)height, 1.0, 1000.0);
      gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);

   }



}
