package net.danielthompson.danray.ui.gl.gl3;

import com.jogamp.opengl.*;
import com.jogamp.opengl.math.FloatUtil;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.GLBuffers;
import net.danielthompson.danray.scenes.AbstractScene;
import net.danielthompson.danray.shapes.AbstractShape;
import net.danielthompson.danray.ui.gl.common.AbstractGLCanvas;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static com.jogamp.opengl.GL.*;

/**
 * Created by daniel on 3/4/14.
 */
public class GL3Canvas extends AbstractGLCanvas {

   private GL3Shape _triangle = new GL3Shape();

   private List<GL3Shape> _shapes = new ArrayList<>();

   private interface Buffer {
      int GLOBAL_MATRICES = 0;
      int MAX = 1;
   }

   private IntBuffer _vboNames = GLBuffers.newDirectIntBuffer(Buffer.MAX);

   private FloatBuffer _clearColor;
   private FloatBuffer _clearDepth;

   private FloatBuffer _matBuffer = GLBuffers.newDirectFloatBuffer(16);

   public GL3Canvas(GLCapabilities caps, AbstractScene scene) {
      super(caps, scene);

      CameraState.Camera = Scene.Camera;

      Animator = new Animator(this);

      Animator.start();

      _triangle.VertexPositions = new float[] {
            0, 0, 0,
            1, 0, 0,
            0, 1, 0,
            1, 1, 0/*,
            200, 100, 0,
            100, 200, 0,*/
      };

      _triangle.VertexColors = new float[] {
            1, 0, 0,
            0, 0, 1,
            0, 1, 0,
            1, 0, 0/*,
            0, 0, 1,
            0, 1, 0*/
      };

      _triangle.VertexIndices = new short[] {0, 1, 2, 2, 1, 3};

      for (AbstractShape shape : scene.Shapes) {
         GL3Shape sphere = new GL3Shape();

         sphere.ObjectToWorld = shape.ObjectToWorld;

         _shapes.add(sphere);

         sphere.VertexPositions = getBallVertsOnly();

         sphere.VertexIndices = new short[sphere.VertexPositions.length];

         for (short i = 0; i < sphere.VertexPositions.length; i += 3) {

            short triangleIndex = (short)(i / 3);

            if (triangleIndex % 2 == 0) {
               sphere.VertexIndices[i] =       triangleIndex;
               sphere.VertexIndices[i + 1] =   (short)(triangleIndex + 1);
               sphere.VertexIndices[i + 2] =   (short)(triangleIndex + 2);
            }
            else {
               sphere.VertexIndices[i] =       (short)(triangleIndex + 1);
               sphere.VertexIndices[i + 1] =   (short)(triangleIndex);
               sphere.VertexIndices[i + 2] =   (short)(triangleIndex + 2);
            }
         }

         sphere.VertexColors = new float[sphere.VertexPositions.length];

         for (short i = 0; i < sphere.VertexPositions.length; i += 3) {
//         _sphere.VertexIndices[i] = i;
//         _sphere.VertexIndices[i + 1] = (short)(i + 1);
//         _sphere.VertexIndices[i + 2] = (short)(i + 2);

            if (i % 9 == 0) {
               sphere.VertexColors[i] = 1.0f;
               sphere.VertexColors[i + 1] = 0.0f;
               sphere.VertexColors[i + 2] = 0.0f;
            }
            else if (i % 9 == 3) {
               sphere.VertexColors[i] = 0.0f;
               sphere.VertexColors[i + 1] = 1.0f;
               sphere.VertexColors[i + 2] = 0.0f;
            }
            else if (i % 9 == 6) {
               sphere.VertexColors[i] = 0.0f;
               sphere.VertexColors[i + 1] = 0.0f;
               sphere.VertexColors[i + 2] = 1.0f;
            }


         }
      }



   }

   @Override
   public void dispose(GLAutoDrawable drawable) {
      GL3 gl = drawable.getGL().getGL3();

      _triangle.dispose(gl);

      for (GL3Shape shape : _shapes) {
         shape.dispose(gl);
      }

      System.exit(0);
   }

   @Override
   public void init(GLAutoDrawable drawable) {
      System.out.println("init");

      GL3 gl = drawable.getGL().getGL3();

      String vendor = gl.glGetString(GL.GL_VENDOR);
      System.out.println("Vendor: [" + vendor + "]");

      String renderer = gl.glGetString(GL.GL_RENDERER);
      System.out.println("Renderer: [" + renderer + "]");

      String version = gl.glGetString(GL.GL_VERSION);
      System.out.println("Version: [" + version + "]");

      String glslVersion = gl.glGetString(GL3.GL_SHADING_LANGUAGE_VERSION);
      System.out.println("GLSL Version: [" + glslVersion + "]");

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

      for (GL3Shape shape : _shapes) {
         shape.initBuffers(gl);
      }

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
            GL3Semantic.Uniform.GLOBAL_MATRICES, // index of the binding point within target
            _vboNames.get(Buffer.GLOBAL_MATRICES) // name of the buffer object to bind to the target
      );

      checkError(gl, "initBuffers");
   }

   private void initVertexArray(GL3 gl) {

      _triangle.initVertexArray(gl);

      for (GL3Shape shape : _shapes) {
         shape.initVertexArray(gl);
      }

   }

   private void initProgram(GL3 gl) {

      _triangle.initProgram(gl);

      for (GL3Shape shape : _shapes) {
         shape.initProgram(gl);
      }

      checkError(gl, "initProgram");
   }

   private float[] getBallVertsOnly() {

      int levels = 20;
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

            //ballVerts.add(x * zr0); //x
            //ballVerts.add(y * zr0); //y
            //ballVerts.add(z0);      //z

            ballVerts[index + 3] = x * zr1;
            ballVerts[index + 4] = y * zr1;
            ballVerts[index + 5] = z1;

//            ballVerts.add(x * zr1); //x
//            ballVerts.add(y * zr1); //y
//            ballVerts.add(z1);      //z

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

            //ballVerts.add(x * zr0); //x
            //ballVerts.add(y * zr0); //y
            //ballVerts.add(z0);      //z

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

//            ballVerts.add(x * zr1); //x
//            ballVerts.add(y * zr1); //y
//            ballVerts.add(z1);      //z

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

      System.out.println("display");

      GL3 gl = drawable.getGL().getGL3();

      // view matrix (camera)
      {
         // move camera

         Scene.Camera.moveOriginAlongOrientation(CameraState.ActiveOriginMovement);
         Scene.Camera.moveDirectionAlongOrientation(CameraState.ActiveDirectionMovement);

         float originDecay = 0.9f;

         // decay movement rates

         CameraState.ActiveOriginMovement.X *= originDecay;

         CameraState.ActiveOriginMovement.Z *= originDecay;

         // calculate next camera movement position

         if (CameraState.MoveForward) {
            CameraState.ActiveOriginMovement.Z--;
            if (CameraState.ActiveOriginMovement.Z < -10) {
               CameraState.ActiveOriginMovement.Z = -10;
            }

         }
         if (CameraState.MoveBackward) {
            CameraState.ActiveOriginMovement.Z++;
            if (CameraState.ActiveOriginMovement.Z > 10) {
               CameraState.ActiveOriginMovement.Z = 10;
            }
         }

         if (CameraState.MoveLeft) {
            CameraState.ActiveOriginMovement.X--;
            if (CameraState.ActiveOriginMovement.X < -10) {
               CameraState.ActiveOriginMovement.X = -10;
            }
         }
         if (CameraState.MoveRight) {
            CameraState.ActiveOriginMovement.X++;
            if (CameraState.ActiveOriginMovement.X > 10) {
               CameraState.ActiveOriginMovement.X = 10;
            }
         }

         float directionDecay = 0.8f;

         CameraState.ActiveDirectionMovement.X *= directionDecay;
         CameraState.ActiveDirectionMovement.Y *= directionDecay;
         CameraState.ActiveDirectionMovement.Z *= directionDecay;

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

      //_triangle.display(gl);

      for (GL3Shape shape : _shapes) {
         shape.display(gl);
      }

      checkError(gl, "display");
   }

   @Override
   public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
      GL3 gl = drawable.getGL().getGL3();

      float aspect = (float)width / (float)height;

      float fieldOfViewRad = (float)Math.toRadians(Scene.Camera.Settings.fov);

      float[] perspective = new float[16];
      FloatUtil.makePerspective(
            perspective, // matrix
            0, // offset into matrix
            true, // initialize to identity first
            fieldOfViewRad, // angle in radians
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