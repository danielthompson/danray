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
import java.util.ArrayList;
import java.util.List;

import static com.jogamp.opengl.GL.*;

/**
 * Created by daniel on 3/4/14.
 */
public class OpenGL3Canvas extends AbstractOpenGLCanvas {

   private float[] vertexData = {
         -1, -1, 1, 0, 0,
         +0, +2, 0, 0, 1,
         +1, -1, 0, 1, 0};

   private short[] elementData = {0, 2, 1};

   private interface Buffer {

      int VERTEX = 0;
      int ELEMENT = 1;
      int GLOBAL_MATRICES = 2;
      int MAX = 3;
   }

   private IntBuffer bufferName = GLBuffers.newDirectIntBuffer(Buffer.MAX);
   private IntBuffer vertexArrayName = GLBuffers.newDirectIntBuffer(1);

   private FloatBuffer clearColor = GLBuffers.newDirectFloatBuffer(4);
   private FloatBuffer clearDepth = GLBuffers.newDirectFloatBuffer(1);

   private FloatBuffer matBuffer = GLBuffers.newDirectFloatBuffer(16);

   private Program program;

   private long start;

   public OpenGL3Canvas(GLCapabilities caps, AbstractScene scene) {
      super(caps, scene);


      CameraState.Camera = Scene.Camera;

      addMouseListener(MouseListener);
      addMouseMotionListener(MouseListener);
      addKeyListener(KeyListener);

      Animator = new Animator(this);

      Animator.start();

   }

   @Override
   public void dispose(GLAutoDrawable drawable) {
      GL3 gl = drawable.getGL().getGL3();

      gl.glDeleteProgram(program.name);
      gl.glDeleteVertexArrays(1, vertexArrayName);
      gl.glDeleteBuffers(Buffer.MAX, bufferName);

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

      start = System.currentTimeMillis();
   }

   private void initBuffers(GL3 gl) {

      FloatBuffer vertexBuffer = GLBuffers.newDirectFloatBuffer(vertexData);
      ShortBuffer elementBuffer = GLBuffers.newDirectShortBuffer(elementData);

      // generate buffer object names
      gl.glGenBuffers(
            Buffer.MAX, // number of names to be generated
            bufferName // array to store names in
      );

      // bind a named buffer object
      gl.glBindBuffer(
            GL_ARRAY_BUFFER, // target - vertex attributes
            bufferName.get(Buffer.VERTEX) // name of buffer object
      );

      // create and initialize a buffer object's data store
      gl.glBufferData(
            GL_ARRAY_BUFFER, // target - vertex attributes
            vertexBuffer.capacity() * Float.BYTES, // size in bytes
            vertexBuffer, // data to copy in
            GL_STATIC_DRAW // usage hint (STATIC - modify once, used many times; DRAW - modified by app, source for GL commands)
      );

      // unbind the binding point's previously bound buffer object
      gl.glBindBuffer(GL_ARRAY_BUFFER, 0);

      // bind a named buffer objectr
      gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, bufferName.get(Buffer.ELEMENT));

      // create and initialize a buffer object's data store
      gl.glBufferData(
            GL_ELEMENT_ARRAY_BUFFER, // target -  vertex array indices
            elementBuffer.capacity() * Short.BYTES, // size in bytes
            elementBuffer, // data to copy in
            GL_STATIC_DRAW // usage hint (STATIC - modify once, used many times; DRAW - modified by app, source for GL commands)
      );

      // unbind the binding point's previously bound buffer object
      gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

      // bind a named buffer object
      gl.glBindBuffer(gl.GL_UNIFORM_BUFFER, bufferName.get(Buffer.GLOBAL_MATRICES));

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
            bufferName.get(Buffer.GLOBAL_MATRICES) // name of the buffer object to bind to the target
      );

      checkError(gl, "initBuffers");
   }

   private void initVertexArray(GL3 gl) {

      // generate vertex array object (VAO) names
      gl.glGenVertexArrays(1, vertexArrayName);

      // bind a vertex array object (VAO)
      gl.glBindVertexArray(vertexArrayName.get(0));
      {
         // bind a named buffer object (GL_ARRAY_BUFFER - vertex attributes)
         gl.glBindBuffer(GL_ARRAY_BUFFER, bufferName.get(Buffer.VERTEX));
         {
            int stride = (2 + 3) * Float.BYTES;
            int offset = 0;

            // enable or disable a generic vertex attribute array for currently bound VAO
            gl.glEnableVertexAttribArray(OpenGL3Semantic.Attr.POSITION);

            // define an array of generic vertex attribute data
            gl.glVertexAttribPointer(
                  OpenGL3Semantic.Attr.POSITION, // index of the generic vertex attribute to be modified
                  2, // number of components per generic vertex attribute
                  GL_FLOAT, // data type of each component in the array
                  false, // specifies whether fixed-point data values should be normalized
                  stride, // byte offset between consecutive generic vertex attributes
                  offset // offset of the first generic vertex attribute
            );

            offset = 2 * Float.BYTES;

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

         gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, bufferName.get(Buffer.ELEMENT));
      }
      gl.glBindVertexArray(0);

      checkError(gl, "initVao");
   }

   private void initProgram(GL3 gl) {

      program = new Program(gl, getClass(), "shaders/gl3", "hello-triangle", "hello-triangle");

      checkError(gl, "initProgram");
   }

   private List<Float> getBallVerts() {
      List<Float> ballVerts = new ArrayList<>();

      for(int i = 0; i <= 40; i++)
      {
         float lat0 = (float) (Math.PI * (-0.5 + (float) (i - 1) / 40));
         float z0  = (float) Math.sin(lat0);
         float zr0 = (float) Math.cos(lat0);

         float lat1 = (float) (Math.PI * (-0.5 + (float) i / 40));
         float z1 = (float) Math.sin(lat1);
         float zr1 = (float) Math.cos(lat1);

         for(int j = 0; j <= 40; j++)
         {
            float lng = (float) (2 * Math.PI * (float) (j - 1) / 40);
            float x = (float) Math.cos(lng);
            float y = (float) Math.sin(lng);

            ballVerts.add(x * zr0); //X
            ballVerts.add(y * zr0); //Y
            ballVerts.add(z0);      //Z

            ballVerts.add(0.0f);
            ballVerts.add(1.0f);
            ballVerts.add(0.0f);
            ballVerts.add(1.0f); //R,G,B,A

            ballVerts.add(x * zr1); //X
            ballVerts.add(y * zr1); //Y
            ballVerts.add(z1);      //Z

            ballVerts.add(0.0f);
            ballVerts.add(1.0f);
            ballVerts.add(0.0f);
            ballVerts.add(1.0f); //R,G,B,A
         }
      }

      return ballVerts;
   }

   @Override
   public void display(GLAutoDrawable drawable) {

      GL3 gl = drawable.getGL().getGL3();

      // view matrix
      {
         float[] view = new float[16];
         FloatUtil.makeIdentity(view);

         for (int i = 0; i < 16; i++) {
            matBuffer.put(i, view[i]);
         }
         gl.glBindBuffer(gl.GL_UNIFORM_BUFFER, bufferName.get(Buffer.GLOBAL_MATRICES));
         gl.glBufferSubData(gl.GL_UNIFORM_BUFFER, 16 * Float.BYTES, 16 * Float.BYTES, matBuffer);
         gl.glBindBuffer(gl.GL_UNIFORM_BUFFER, 0);
      }

      gl.glClearBufferfv(gl.GL_COLOR, 0, clearColor.put(0, 0f).put(1, .33f).put(2, 0.66f).put(3, 1f));
      gl.glClearBufferfv(gl.GL_DEPTH, 0, clearDepth.put(0, 1f));

      gl.glUseProgram(program.name);
      gl.glBindVertexArray(vertexArrayName.get(0));

      // model matrix
      {
         long now = System.currentTimeMillis();
         float diff = (float) (now - start) / 1_000f;

         float[] scale = FloatUtil.makeScale(new float[16], true, 0.5f, 0.5f, 0.5f);
         float[] zRotation = FloatUtil.makeRotationEuler(new float[16], 0, 0, 0, diff);
         float[] modelToWorldMat = FloatUtil.multMatrix(scale, zRotation);

         for (int i = 0; i < 16; i++) {
            matBuffer.put(i, modelToWorldMat[i]);
         }

         // Modifies the value of a uniform variable or a uniform variable array.
         gl.glUniformMatrix4fv(program.modelToWorldMatUL, 1, false, matBuffer);
      }

      gl.glDrawElements(GL_TRIANGLES, elementData.length, GL_UNSIGNED_SHORT, 0);

      gl.glUseProgram(0);
      gl.glBindVertexArray(0);

      checkError(gl, "display");
   }


   @Override
   public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
      GL3 gl = drawable.getGL().getGL3();

      float[] ortho = new float[16];
      FloatUtil.makeOrtho(ortho, 0, false, -1, 1, -1, 1, 1, -1);
      for (int i = 0; i < 16; i++) {
         matBuffer.put(i, ortho[i]);
      }
      gl.glBindBuffer(gl.GL_UNIFORM_BUFFER, bufferName.get(Buffer.GLOBAL_MATRICES));
      gl.glBufferSubData(gl.GL_UNIFORM_BUFFER, 0, 16 * Float.BYTES, matBuffer);
      gl.glBindBuffer(gl.GL_UNIFORM_BUFFER, 0);

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