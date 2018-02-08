package net.danielthompson.danray.ui.opengl;

import com.jogamp.opengl.*;
import com.jogamp.opengl.math.FloatUtil;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.GLBuffers;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;
import net.danielthompson.danray.scenes.AbstractScene;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static com.jogamp.opengl.GL.*;

/**
 * Created by daniel on 3/4/14.
 */
public class OpenGL3Canvas extends AbstractOpenGLCanvas {

   private interface TriangleBuffer {
      int VERTEX_LOCATION = 0;
      int VERTEX_COLOR = 1;
      int ELEMENT = 2;
      int GLOBAL_MATRICES = 3;
      int MAX = 4;
   }

   private float[] _triangleVertexLocations = {
         0, 0, 0,
         100, 0, 0,
         0, 100, 0,
   };

   private float[] _triangleVertexColors = {
         1, 0, 0,
         0, 0, 1,
         0, 1, 0
   };

   private short[] _triangleVertexIndices = {0, 1, 2};

   private interface SphereBuffer {
      int VERTEX_LOCATION = 0;
      int GLOBAL_MATRICES = 1;
      int MAX = 2;
   }

   private float[] _sphereVertexLocations;

   private IntBuffer _triangleBuffers = GLBuffers.newDirectIntBuffer(TriangleBuffer.MAX);
   private IntBuffer _triangleVAO = GLBuffers.newDirectIntBuffer(1);

   private IntBuffer _sphereBuffers = GLBuffers.newDirectIntBuffer(SphereBuffer.MAX);
   private IntBuffer _sphereVAO = GLBuffers.newDirectIntBuffer(1);

   private FloatBuffer _clearColor;
   private FloatBuffer _clearDepth;

   private FloatBuffer _matBuffer = GLBuffers.newDirectFloatBuffer(16);

   private Program _triangleProgram;

   private Program _sphereProgram;

   private long _start;

   public OpenGL3Canvas(GLCapabilities caps, AbstractScene scene) {
      super(caps, scene);

      CameraState.Camera = Scene.Camera;

      addMouseListener(MouseListener);
      addMouseMotionListener(MouseListener);
      addKeyListener(KeyListener);

      Animator = new Animator(this);

      Animator.start();

      _sphereVertexLocations = getBallVertsOnly();
   }

   @Override
   public void dispose(GLAutoDrawable drawable) {
      GL3 gl = drawable.getGL().getGL3();

      gl.glDeleteProgram(_triangleProgram.name);
      gl.glDeleteVertexArrays(1, _triangleVAO);
      gl.glDeleteBuffers(TriangleBuffer.MAX, _triangleBuffers);

      System.exit(0);
   }

   @Override
   public void init(GLAutoDrawable drawable) {
      System.out.println("init: enter");

      GL3 gl = drawable.getGL().getGL3();

      System.out.println("initTriangleBuffers: enter");
      initTriangleBuffers(gl);
      System.out.println("initTriangleBuffers: leave");

      System.out.println("initSphereBuffers: enter");
      initSphereBuffers(gl);
      System.out.println("initSphereBuffers: leave");

      System.out.println("initTriangleVertexArray: enter");
      initTriangleVertexArray(gl);
      System.out.println("initTriangleVertexArray: leave");

      System.out.println("initSphereVertexArray: enter");
      initSphereVertexArray(gl);
      System.out.println("initSphereVertexArray: leave");

      System.out.println("initProgram: enter");
      initProgram(gl);
      System.out.println("initProgram: leave");

      gl.glEnable(GL_DEPTH_TEST);

      float[] clear = { 0.0f, 0.33f, 0.66f, 1.0f};
      _clearColor = GLBuffers.newDirectFloatBuffer(clear);

      float[] depth = { 1.0f };
      _clearDepth = GLBuffers.newDirectFloatBuffer(depth);

      _start = System.currentTimeMillis();
      System.out.println("init: leave");
   }

   private void initTriangleBuffers(GL3 gl) {
      FloatBuffer triangleVertexLocationBuffer = GLBuffers.newDirectFloatBuffer(_triangleVertexLocations);
      FloatBuffer triangleVertexColorBuffer = GLBuffers.newDirectFloatBuffer(_triangleVertexColors);
      ShortBuffer triangleVertexIndexBuffer = GLBuffers.newDirectShortBuffer(_triangleVertexIndices);


      // generate buffer object names
      gl.glGenBuffers(
            TriangleBuffer.MAX, // number of names to be generated
            _triangleBuffers // array to store names in
      );

      // triangle vertex locations

      // bind a named buffer object
      gl.glBindBuffer(
            GL_ARRAY_BUFFER, // target - vertex attributes
            _triangleBuffers.get(TriangleBuffer.VERTEX_LOCATION) // name of buffer object
      );
      {

         // create and initialize a buffer object's data store
         gl.glBufferData(
               GL_ARRAY_BUFFER, // target - vertex attributes
               triangleVertexLocationBuffer.capacity() * Float.BYTES, // size in bytes
               triangleVertexLocationBuffer, // data to copy in
               GL_STATIC_DRAW // usage hint (STATIC - modify once, used many times; DRAW - modified by app, source for GL commands)
         );
      }

      // triangle vertex colors

      // bind a named buffer object
      gl.glBindBuffer(
            GL_ARRAY_BUFFER, // target - vertex attributes
            _triangleBuffers.get(TriangleBuffer.VERTEX_COLOR) // name of buffer object
      );

      {
         // create and initialize a buffer object's data store
         gl.glBufferData(
               GL_ARRAY_BUFFER, // target - vertex attributes
               triangleVertexColorBuffer.capacity() * Float.BYTES, // size in bytes
               triangleVertexColorBuffer, // data to copy in
               GL_STATIC_DRAW // usage hint (STATIC - modify once, used many times; DRAW - modified by app, source for GL commands)
         );
      }

      // unbind the binding point's previously bound buffer object
      gl.glBindBuffer(GL_ARRAY_BUFFER, 0);

      // vertex indices

      // bind a named buffer object
      gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, _triangleBuffers.get(TriangleBuffer.ELEMENT));
      {
         // create and initialize a buffer object's data store
         gl.glBufferData(
               GL_ELEMENT_ARRAY_BUFFER, // target -  vertex array indices
               triangleVertexIndexBuffer.capacity() * Short.BYTES, // size in bytes
               triangleVertexIndexBuffer, // data to copy in
               GL_STATIC_DRAW // usage hint (STATIC - modify once, used many times; DRAW - modified by app, source for GL commands)
         );
      }

      // unbind the binding point's previously bound buffer object
      gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

      // triangle uniforms

      // bind a named buffer object
      gl.glBindBuffer(gl.GL_UNIFORM_BUFFER, _triangleBuffers.get(TriangleBuffer.GLOBAL_MATRICES));
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
            _triangleBuffers.get(TriangleBuffer.GLOBAL_MATRICES) // name of the buffer object to bind to the target
      );

      checkError(gl, "initTriangleBuffers");
   }


   private void initSphereBuffers(GL3 gl) {
      FloatBuffer sphereVertexLocationBuffer = GLBuffers.newDirectFloatBuffer(_sphereVertexLocations);

      // generate buffer object names
      gl.glGenBuffers(
            SphereBuffer.MAX, // number of names to be generated
            _sphereBuffers // array to store names in
      );

      // sphere vertex locations

      // bind a named buffer object
      gl.glBindBuffer(
            GL_ARRAY_BUFFER,
            _sphereBuffers.get(SphereBuffer.VERTEX_LOCATION)
      );

      {
         gl.glBufferData(
               GL_ARRAY_BUFFER,
               sphereVertexLocationBuffer.capacity() * Float.BYTES,
               sphereVertexLocationBuffer,
               GL_STATIC_DRAW
         );
      }

      // unbind the binding point's previously bound buffer object
      gl.glBindBuffer(GL_ARRAY_BUFFER, 0);


      // generate buffer object names
      gl.glGenBuffers(
            SphereBuffer.MAX, // number of names to be generated
            _sphereBuffers // array to store names in
      );

      // sphere uniforms

      // bind a named buffer object
      gl.glBindBuffer(gl.GL_UNIFORM_BUFFER, _sphereBuffers.get(SphereBuffer.GLOBAL_MATRICES));
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
            _sphereBuffers.get(SphereBuffer.GLOBAL_MATRICES) // name of the buffer object to bind to the target
      );

      checkError(gl, "initSphereBuffers");
   }

   private void initTriangleVertexArray(GL3 gl) {
      // generate vertex array object (VAO) names
      gl.glGenVertexArrays(1, _triangleVAO);

      // bind a vertex array object (VAO)
      gl.glBindVertexArray(_triangleVAO.get(0));
      {
         // bind a named buffer object (GL_ARRAY_BUFFER - vertex attributes)
         gl.glBindBuffer(GL_ARRAY_BUFFER, _triangleBuffers.get(TriangleBuffer.VERTEX_LOCATION));
         {
            int stride = (3) * Float.BYTES;
            int offset = 0;

            // enable or disable a generic vertex attribute array for currently bound VAO
            gl.glEnableVertexAttribArray(OpenGL3Semantic.Attr.POSITION);

            // define an array of generic vertex attribute data
            gl.glVertexAttribPointer(
                  OpenGL3Semantic.Attr.POSITION, // index of the generic vertex attribute to be modified
                  3, // number of components per generic vertex attribute
                  GL_FLOAT, // data type of each component in the array
                  false, // specifies whether fixed-point data values should be normalized
                  stride, // byte offset between consecutive generic vertex attributes
                  offset // offset of the first generic vertex attribute
            );
         }

         // bind a named buffer object (GL_ARRAY_BUFFER - vertex attributes)
         gl.glBindBuffer(GL_ARRAY_BUFFER, _triangleBuffers.get(TriangleBuffer.VERTEX_COLOR));
         {
            int stride = (3) * Float.BYTES;
            int offset = 0;

            // enable or disable a generic vertex attribute array for the currently bound VAO
            gl.glEnableVertexAttribArray(OpenGL3Semantic.Attr.COLOR);

            // define an array of generic vertex attribute data
            gl.glVertexAttribPointer(
                  OpenGL3Semantic.Attr.COLOR, // index of the generic vertex attribute to be modified
                  3, // number of components per generic vertex attribute
                  GL_FLOAT,  // data type of each component in the array
                  false,  // specifies whether fixed-point data values should be normalized
                  stride, // byte offset between consecutive generic vertex attributes
                  offset // offset of the first generic vertex attribute
            );
         }

         // unbind  
         gl.glBindBuffer(GL_ARRAY_BUFFER, 0);

         // originally comented in - Q: what's the point? A: crashes if commented out Q: why? ??
         gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, _triangleBuffers.get(TriangleBuffer.ELEMENT));
      }
      gl.glBindVertexArray(0);

      checkError(gl, "initTriangleVertexArray");
   }

   private void initSphereVertexArray(GL3 gl) {
      // generate vertex array object (VAO) names
      gl.glGenVertexArrays(1, _sphereVAO);

      // bind a vertex array object (VAO)
      gl.glBindVertexArray(_sphereVAO.get(0));
      {
         // bind a named buffer object (GL_ARRAY_BUFFER - vertex attributes)
         gl.glBindBuffer(GL_ARRAY_BUFFER, _sphereBuffers.get(SphereBuffer.VERTEX_LOCATION));
         {
            int stride = (3) * Float.BYTES;
            int offset = 0;

            // enable or disable a generic vertex attribute array for currently bound VAO
            gl.glEnableVertexAttribArray(OpenGL3Semantic.Attr.POSITION);

            // define an array of generic vertex attribute data
            gl.glVertexAttribPointer(
                  OpenGL3Semantic.Attr.POSITION, // index of the generic vertex attribute to be modified
                  3, // number of components per generic vertex attribute
                  GL_FLOAT, // data type of each component in the array
                  false, // specifies whether fixed-point data values should be normalized
                  stride, // byte offset between consecutive generic vertex attributes
                  offset // offset of the first generic vertex attribute
            );
         }

         // unbind
         gl.glBindBuffer(GL_ARRAY_BUFFER, 0);

      }
      gl.glBindVertexArray(0);

      checkError(gl, "initSphereVertexArray");
   }


   private void initProgram(GL3 gl) {

      _triangleProgram = new Program(gl, "hello-triangle", "hello-triangle");

      _sphereProgram = new Program(gl, "sphere", "hello-triangle");

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

   @Override
   public void display(GLAutoDrawable drawable) {

      GL3 gl = drawable.getGL().getGL3();

      // view matrix (camera)
      {

         double[] colMajor = Scene.Camera.cameraToWorld._inverse.getColMajor();

         for (int i = 0; i < 16; i++) {
            _matBuffer.put(i, (float)colMajor[i]);
         }

         gl.glBindBuffer(gl.GL_UNIFORM_BUFFER, _triangleBuffers.get(TriangleBuffer.GLOBAL_MATRICES));
         {
            // update a subset of a buffer object's data store
            gl.glBufferSubData(
                  gl.GL_UNIFORM_BUFFER, // target
                  0, // offset
                  16 * Float.BYTES,  // size
                  _matBuffer // data
            );
         }
         gl.glBindBuffer(gl.GL_UNIFORM_BUFFER, 0);
      }


      gl.glClearBufferfv(gl.GL_COLOR, 0, _clearColor);
      gl.glClearBufferfv(gl.GL_DEPTH, 0, _clearDepth);

      // triangle

      gl.glUseProgram(_triangleProgram.name);
      {
         gl.glBindVertexArray(_triangleVAO.get(0));
         {
            // model matrix
            {
//         long now = System.currentTimeMillis();
//         float diff = (float) (now - _start) / 1_000f;
//
//         float[] scale = FloatUtil.makeScale(new float[16], true, 0.5f, 0.5f, 0.5f);
//         float[] zRotation = FloatUtil.makeRotationEuler(new float[16], 0, diff, 0, 0);
//         float[] modelToWorldMat = FloatUtil.multMatrix(scale, zRotation);
//
//         for (int i = 0; i < 16; i++) {
//            _matBuffer.put(i, modelToWorldMat[i]);
//         }

               float[] identity = new float[16];
               FloatUtil.makeIdentity(identity);

               for (int i = 0; i < 16; i++) {
                  _matBuffer.put(i, identity[i]);
               }

               // Modifies the value of a uniform variable or a uniform variable array.
               gl.glUniformMatrix4fv(_triangleProgram.modelToWorldMatUL, 1, false, _matBuffer);
            }

            gl.glDrawElements(GL_TRIANGLES, _triangleVertexIndices.length, GL_UNSIGNED_SHORT, 0);
         }
         gl.glBindVertexArray(0);
      }
      gl.glUseProgram(0);

      // sphere

//      gl.glUseProgram(_sphereProgram.name);
//      {
//         gl.glBindVertexArray(_sphereVAO.get(0));
//         {
//            // model matrix
//            {
//               float[] identity = new float[16];
//               FloatUtil.makeIdentity(identity);
//
//               for (int i = 0; i < 16; i++) {
//                  _matBuffer.put(i, identity[i]);
//               }
//
//               // Modifies the value of a uniform variable or a uniform variable array.
//               gl.glUniformMatrix4fv(_sphereProgram.modelToWorldMatUL, 1, false, _matBuffer);
//            }
//
//             render primitives from array data
//            gl.glDrawArrays(
//                  GL_TRIANGLES, // mode
//                  0, // first
//                  _sphereVertexLocations.length // count
//            );
//         }
//         gl.glBindVertexArray(0);
//      }
//      gl.glUseProgram(0);


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
      gl.glBindBuffer(gl.GL_UNIFORM_BUFFER, _triangleBuffers.get(TriangleBuffer.GLOBAL_MATRICES));

      // updates a subset of a buffer object's data store
      gl.glBufferSubData(
            gl.GL_UNIFORM_BUFFER, // uniform block storage
            16 * Float.BYTES, // offset
            16 * Float.BYTES, // size
            _matBuffer // data
      );

      // unbind current binding point
      gl.glBindBuffer(gl.GL_UNIFORM_BUFFER, 0);

      // set the viewport
      gl.glViewport(x, y, width, height);

   }

   private class Program {

      int name, modelToWorldMatUL;

      // "shaders/gl3", "hello-triangle", "hello-triangle");

      Program(GL3 gl, String vertex, String fragment) {

         ShaderCode vertShader = ShaderCode.create(
            gl, // GL2ES2
            GL2ES2.GL_VERTEX_SHADER, // int type
            this.getClass(),  // Class<?> context
               "/shaders/gl3", //root, // string srcRoot
            null, // string binRoot
            vertex, // string basename
            true
         );
         ShaderCode fragShader = ShaderCode.create(
            gl, // GL2ES2
            GL2ES2.GL_FRAGMENT_SHADER,  // int type
            this.getClass(), // Class<?> context
            "/shaders/gl3", //root, // string srcRoot
            null, // string binRoot
            fragment, // string basename
            true
         );

         ShaderProgram shaderProgram = new ShaderProgram();

         shaderProgram.add(vertShader);
         shaderProgram.add(fragShader);

         shaderProgram.init(gl);

         name = shaderProgram.program();

         shaderProgram.link(gl, System.err);

         modelToWorldMatUL = gl.glGetUniformLocation(name, "model");

         if (modelToWorldMatUL == -1) {
            System.err.println("uniform 'model' not found!");
         }

         int globalMatricesBI = gl.glGetUniformBlockIndex(name, "GlobalMatrices");

         if (globalMatricesBI == -1) {
            System.err.println("block index 'GlobalMatrices' not found!");
         }
         gl.glUniformBlockBinding(name, globalMatricesBI, OpenGL3Semantic.Uniform.GLOBAL_MATRICES);
      }
   }
}