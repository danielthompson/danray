package net.danielthompson.danray.ui.opengl;

import com.jogamp.opengl.*;
import com.jogamp.opengl.math.FloatUtil;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.GLBuffers;
import net.danielthompson.danray.scenes.AbstractScene;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static com.jogamp.opengl.GL.*;

/**
 * Created by daniel on 3/4/14.
 */
public class OpenGL3Canvas extends AbstractOpenGLCanvas {

   private GL3Shape _triangle = new GL3Shape();
   private GL3Shape _sphere = new GL3Shape();

   private interface Buffer {
      int GLOBAL_MATRICES = 0;
      int MAX = 1;
   }

   private IntBuffer _vboNames = GLBuffers.newDirectIntBuffer(Buffer.MAX);

   private FloatBuffer _clearColor;
   private FloatBuffer _clearDepth;

   private FloatBuffer _matBuffer = GLBuffers.newDirectFloatBuffer(16);

   public OpenGL3Canvas(GLCapabilities caps, AbstractScene scene) {
      super(caps, scene);

      CameraState.Camera = Scene.Camera;

      addMouseListener(MouseListener);
      addMouseMotionListener(MouseListener);
      addKeyListener(KeyListener);

      Animator = new Animator(this);

      Animator.start();

      _triangle.VertexPositions = new float[] {
            0, 0, 0,
            100, 0, 0,
            0, 100, 0,
      };

      _triangle.VertexColors = new float[] {
            1, 0, 0,
            0, 0, 1,
            0, 1, 0
      };

      _triangle.VertexIndices = new short[] {0, 1, 2};

      _sphere.VertexPositions = getBallVertsOnly();

   }

   @Override
   public void dispose(GLAutoDrawable drawable) {
      GL3 gl = drawable.getGL().getGL3();

      _triangle.dispose(gl);

      System.exit(0);
   }

   @Override
   public void init(GLAutoDrawable drawable) {
      System.out.println("init");

      GL3 gl = drawable.getGL().getGL3();

      initBuffers(gl);

      initVertexArray(gl);

      initProgram(gl);

      gl.glEnable(GL_DEPTH_TEST);

      float[] clear = { 0.0f, 0.33f, 0.66f, 1.0f};
      _clearColor = GLBuffers.newDirectFloatBuffer(clear);

      float[] depth = { 1.0f };
      _clearDepth = GLBuffers.newDirectFloatBuffer(depth);
   }

   private void initBuffers(GL3 gl) {

      _triangle.initBuffers(gl);

      // generate buffer object names
      gl.glGenBuffers(
            Buffer.MAX, // number of names to be generated
            _vboNames // array to store names in
      );

      // uniforms

      // bind a named buffer object
      gl.glBindBuffer(gl.GL_UNIFORM_BUFFER, _vboNames.get(Buffer.GLOBAL_MATRICES));
      {
         // create and initialize a buffer object's data store
         gl.glBufferData(
               gl.GL_UNIFORM_BUFFER, // target - uniform block storage
               16 * Float.BYTES * 2, // size in bytes
               null, // data to copy
               gl.GL_STREAM_DRAW // usage hint (STREAM = modify once, use a few times; DRAW - modified by app, source for GL commands
         );
      }
      // unbind the binding point's previously bound buffer object
      gl.glBindBuffer(gl.GL_UNIFORM_BUFFER, 0);

      // bind a buffer object to an indexed buffer target
      gl.glBindBufferBase(
            gl.GL_UNIFORM_BUFFER, // target - uniform block storage
            OpenGL3Semantic.Uniform.GLOBAL_MATRICES, // index of the binding point within target
            _vboNames.get(Buffer.GLOBAL_MATRICES) // name of the buffer object to bind to the target
      );

      checkError(gl, "initBuffers");
   }

   private void initVertexArray(GL3 gl) {

      _triangle.initVertexArray(gl);

   }

   private void initProgram(GL3 gl) {

      _triangle.initProgram(gl);

      checkError(gl, "initProgram");
   }

   private float[] getBallVertsOnly() {

      int levels = 40;
      int stride = 6;

      float[] ballVerts = new float[(levels + 1) * (levels + 1) * stride];

      for(int i = 0; i <= levels; i++)
      {
         float lat0 = (float) (Math.PI * (-0.5 + (float) (i - 1) / levels));
         float z0  = (float) Math.sin(lat0);
         float zr0 = (float) Math.cos(lat0);

         float lat1 = (float) (Math.PI * (-0.5 + (float) i / levels));
         float z1 = (float) Math.sin(lat1);
         float zr1 = (float) Math.cos(lat1);

         for(int j = 0; j <= levels; j++)
         {
            int index = (i * levels + j) * stride;

            float lng = (float) (2 * Math.PI * (float) (j - 1) / levels);
            float x = (float) Math.cos(lng);
            float y = (float) Math.sin(lng);

            ballVerts[index] = x * zr0;
            ballVerts[index + 1] = y * zr0;
            ballVerts[index + 2] = z0;

            //ballVerts.add(x * zr0); //X
            //ballVerts.add(y * zr0); //Y
            //ballVerts.add(z0);      //Z

            ballVerts[index + 3] = x * zr1;
            ballVerts[index + 4] = y * zr1;
            ballVerts[index + 5] = z1;

//            ballVerts.add(x * zr1); //X
//            ballVerts.add(y * zr1); //Y
//            ballVerts.add(z1);      //Z

         }
      }

      return ballVerts;
   }

   private float[] getBallVertsWithColor() {

      int levels = 40;

      float[] ballVerts = new float[(levels + 1) * (levels + 1) * 14];

      for(int i = 0; i <= levels; i++)
      {
         float lat0 = (float) (Math.PI * (-0.5 + (float) (i - 1) / levels));
         float z0  = (float) Math.sin(lat0);
         float zr0 = (float) Math.cos(lat0);

         float lat1 = (float) (Math.PI * (-0.5 + (float) i / levels));
         float z1 = (float) Math.sin(lat1);
         float zr1 = (float) Math.cos(lat1);

         for(int j = 0; j <= levels; j++)
         {
            int index = (i * levels + j) * 14;

            float lng = (float) (2 * Math.PI * (float) (j - 1) / levels);
            float x = (float) Math.cos(lng);
            float y = (float) Math.sin(lng);

            ballVerts[index] = x * zr0;
            ballVerts[index + 1] = y * zr0;
            ballVerts[index + 2] = z0;

            //ballVerts.add(x * zr0); //X
            //ballVerts.add(y * zr0); //Y
            //ballVerts.add(z0);      //Z

            ballVerts[index + 3] = 0.0f;
            ballVerts[index + 4] = 1.0f;
            ballVerts[index + 5] = 0.0f;
            ballVerts[index + 6] = 1.0f;

//            ballVerts.add(0.0f);
//            ballVerts.add(1.0f);
//            ballVerts.add(0.0f);
//            ballVerts.add(1.0f); //R,G,B,A

            ballVerts[index + 7] = x * zr1;
            ballVerts[index + 8] = y * zr1;
            ballVerts[index + 9] = z1;

//            ballVerts.add(x * zr1); //X
//            ballVerts.add(y * zr1); //Y
//            ballVerts.add(z1);      //Z

            ballVerts[index + 10] = 0.0f;
            ballVerts[index + 11] = 1.0f;
            ballVerts[index + 12] = 0.0f;
            ballVerts[index + 13] = 1.0f;

//            ballVerts.add(0.0f);
//            ballVerts.add(1.0f);
//            ballVerts.add(0.0f);
//            ballVerts.add(1.0f); //R,G,B,A
         }
      }

      return ballVerts;
   }

   @Override
   public void display(GLAutoDrawable drawable) {

      GL3 gl = drawable.getGL().getGL3();

      // view matrix (camera)
      {

         double[] colMajor = Scene.Camera.cameraToWorld._inverse.getColMajor();

         for (int i = 0; i < 16; i++) {
            _matBuffer.put(i, (float)colMajor[i]);
         }

         gl.glBindBuffer(gl.GL_UNIFORM_BUFFER, _vboNames.get(Buffer.GLOBAL_MATRICES));

         // update a subset of a buffer object's data store
         gl.glBufferSubData(
               gl.GL_UNIFORM_BUFFER, // target
               0, // offset
               16 * Float.BYTES,  // size
               _matBuffer // data
         );

         gl.glBindBuffer(gl.GL_UNIFORM_BUFFER, 0);
      }

      gl.glClearBufferfv(gl.GL_COLOR, 0, _clearColor);
      gl.glClearBufferfv(gl.GL_DEPTH, 0, _clearDepth);

      _triangle.display(gl);

      checkError(gl, "display");
   }


   @Override
   public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
      GL3 gl = drawable.getGL().getGL3();

      float aspect = (float)width / (float)height;

      float[] perspective = new float[16];
      FloatUtil.makePerspective(
            perspective, // matrix
            0, // offset into matrix
            true, // initialize to identity first
            1f, // angle in radians
            aspect, // aspect ratio
            0.1f, // znear
            10000f // zfar
      );

      for (int i = 0; i < 16; i++) {
         _matBuffer.put(i, perspective[i]);
      }

      // bind named buffer to binding point
      gl.glBindBuffer(gl.GL_UNIFORM_BUFFER, _vboNames.get(Buffer.GLOBAL_MATRICES));
      {
         // updates a subset of a buffer object's data store
         gl.glBufferSubData(
               gl.GL_UNIFORM_BUFFER, // uniform block storage
               16 * Float.BYTES, // offset
               16 * Float.BYTES, // size
               _matBuffer // data
         );
      }
      // unbind current binding point
      gl.glBindBuffer(gl.GL_UNIFORM_BUFFER, 0);

      // set the viewport
      gl.glViewport(x, y, width, height);

   }

}