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

   private float[] _vertexLocations = {
         0, 0, 0,
         100, 0, 0,
         0, 100, 0,
   };

   private float[] _vertexColors = {
         1, 0, 0,
         0, 0, 1,
         0, 1, 0
   };

   private float[] _ballVerts;

   private short[] _elementData = {0, 1, 2};

   private interface Buffer {

      int VERTEX_LOCATION = 0;
      int VERTEX_COLOR = 1;
      int ELEMENT = 2;
      int GLOBAL_MATRICES = 3;
      int MAX = 4;
   }

   private IntBuffer _bufferName = GLBuffers.newDirectIntBuffer(Buffer.MAX);
   private IntBuffer _vertexArrayName = GLBuffers.newDirectIntBuffer(1);

   private FloatBuffer _clearColor;
   private FloatBuffer _clearDepth;

   private FloatBuffer _matBuffer = GLBuffers.newDirectFloatBuffer(16);

   private Program _program;

   private long _start;

   public OpenGL3Canvas(GLCapabilities caps, AbstractScene scene) {
      super(caps, scene);

      CameraState.Camera = Scene.Camera;

      addMouseListener(MouseListener);
      addMouseMotionListener(MouseListener);
      addKeyListener(KeyListener);

      Animator = new Animator(this);

      Animator.start();

      _ballVerts = getBallVertsOnly();
   }

   @Override
   public void dispose(GLAutoDrawable drawable) {
      GL3 gl = drawable.getGL().getGL3();

      gl.glDeleteProgram(_program.name);
      gl.glDeleteVertexArrays(1, _vertexArrayName);
      gl.glDeleteBuffers(Buffer.MAX, _bufferName);

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

      _start = System.currentTimeMillis();
   }

   private void initBuffers(GL3 gl) {

      FloatBuffer vertexLocationBuffer = GLBuffers.newDirectFloatBuffer(_vertexLocations);
      FloatBuffer vertexColorBuffer = GLBuffers.newDirectFloatBuffer(_vertexColors);

      ShortBuffer elementBuffer = GLBuffers.newDirectShortBuffer(_elementData);

      // generate buffer object names
      gl.glGenBuffers(
            Buffer.MAX, // number of names to be generated
            _bufferName // array to store names in
      );

      // vertex locations

      // bind a named buffer object
      gl.glBindBuffer(
            GL_ARRAY_BUFFER, // target - vertex attributes
            _bufferName.get(Buffer.VERTEX_LOCATION) // name of buffer object
      );

      // create and initialize a buffer object's data store
      gl.glBufferData(
            GL_ARRAY_BUFFER, // target - vertex attributes
            vertexLocationBuffer.capacity() * Float.BYTES, // size in bytes
            vertexLocationBuffer, // data to copy in
            GL_STATIC_DRAW // usage hint (STATIC - modify once, used many times; DRAW - modified by app, source for GL commands)
      );

      // vertex colors

      // bind a named buffer object
      gl.glBindBuffer(
            GL_ARRAY_BUFFER, // target - vertex attributes
            _bufferName.get(Buffer.VERTEX_COLOR) // name of buffer object
      );

      // create and initialize a buffer object's data store
      gl.glBufferData(
            GL_ARRAY_BUFFER, // target - vertex attributes
            vertexColorBuffer.capacity() * Float.BYTES, // size in bytes
            vertexColorBuffer, // data to copy in
            GL_STATIC_DRAW // usage hint (STATIC - modify once, used many times; DRAW - modified by app, source for GL commands)
      );

      // unbind the binding point's previously bound buffer object
      gl.glBindBuffer(GL_ARRAY_BUFFER, 0);

      // vertex indices

      // bind a named buffer object
      gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, _bufferName.get(Buffer.ELEMENT));

      // create and initialize a buffer object's data store
      gl.glBufferData(
            GL_ELEMENT_ARRAY_BUFFER, // target -  vertex array indices
            elementBuffer.capacity() * Short.BYTES, // size in bytes
            elementBuffer, // data to copy in
            GL_STATIC_DRAW // usage hint (STATIC - modify once, used many times; DRAW - modified by app, source for GL commands)
      );

      // unbind the binding point's previously bound buffer object
      gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

      // uniforms

      // bind a named buffer object
      gl.glBindBuffer(gl.GL_UNIFORM_BUFFER, _bufferName.get(Buffer.GLOBAL_MATRICES));

      // create and initialize a buffer object's data store
      gl.glBufferData(
            gl.GL_UNIFORM_BUFFER, // target - uniform block storage
            16 * Float.BYTES * 2, // size in bytes
            null, // data to copy
            gl.GL_STREAM_DRAW // usage hint (STREAM = modify once, use a few times; DRAW - modified by app, source for GL commands
      );

      // unbind the binding point's previously bound buffer object
      gl.glBindBuffer(gl.GL_UNIFORM_BUFFER, 0);

      // bind a buffer object to an indexed buffer target
      gl.glBindBufferBase(
            gl.GL_UNIFORM_BUFFER, // target - uniform block storage
            OpenGL3Semantic.Uniform.GLOBAL_MATRICES, // index of the binding point within target
            _bufferName.get(Buffer.GLOBAL_MATRICES) // name of the buffer object to bind to the target
      );

      checkError(gl, "initBuffers");
   }

   private void initVertexArray(GL3 gl) {

      // generate vertex array object (VAO) names
      gl.glGenVertexArrays(1, _vertexArrayName);

      // bind a vertex array object (VAO)
      gl.glBindVertexArray(_vertexArrayName.get(0));
      {
         // bind a named buffer object (GL_ARRAY_BUFFER - vertex attributes)
         gl.glBindBuffer(GL_ARRAY_BUFFER, _bufferName.get(Buffer.VERTEX_LOCATION));
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
         gl.glBindBuffer(GL_ARRAY_BUFFER, _bufferName.get(Buffer.VERTEX_COLOR));
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

         gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, _bufferName.get(Buffer.ELEMENT));
      }
      gl.glBindVertexArray(0);

      checkError(gl, "initVao");
   }

   private void initProgram(GL3 gl) {

      _program = new Program(gl, getClass(), "shaders/gl3", "hello-triangle", "hello-triangle");

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

         gl.glBindBuffer(gl.GL_UNIFORM_BUFFER, _bufferName.get(Buffer.GLOBAL_MATRICES));

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

      gl.glUseProgram(_program.name);
      gl.glBindVertexArray(_vertexArrayName.get(0));

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
         gl.glUniformMatrix4fv(_program.modelToWorldMatUL, 1, false, _matBuffer);
      }

      gl.glDrawElements(GL_TRIANGLES, _elementData.length, GL_UNSIGNED_SHORT, 0);

      gl.glUseProgram(0);
      gl.glBindVertexArray(0);

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
      gl.glBindBuffer(gl.GL_UNIFORM_BUFFER, _bufferName.get(Buffer.GLOBAL_MATRICES));

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

      Program(GL3 gl, Class context, String root, String vertex, String fragment) {

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