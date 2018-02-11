package net.danielthompson.danray.ui.gl.gl3;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.GLBuffers;
import net.danielthompson.danray.structures.Transform;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static com.jogamp.opengl.GL.*;

public class GL3InstancedShape {

   public interface Buffer {

      int VERTEX_LOCATION = 0;
      int VERTEX_COLOR = 1;
      int ELEMENT = 2;
      int MAX = 3;
   }

   private IntBuffer _vboNames = GLBuffers.newDirectIntBuffer(Buffer.MAX);
   private IntBuffer _vaoName = GLBuffers.newDirectIntBuffer(1);

   private FloatBuffer _matBuffer = GLBuffers.newDirectFloatBuffer(16);

   public GL3Program Program;

   public float[] VertexPositions;

   public float[] VertexColors;

   public short[] VertexIndices;

   public FloatBuffer VertexPositionBuffer;

   public FloatBuffer VertexColorBuffer;

   public ShortBuffer VertexIndexBuffer;

   long _start = System.currentTimeMillis();

   float Divisor = 1.0f / 5000.0f;

   Transform ObjectToWorld;

   public void initBuffers(GL3 gl) {

      // generate buffer object names
      gl.glGenBuffers(
            Buffer.MAX, // number of names to be generated
            _vboNames // array to store names in
      );

      if (VertexPositions != null) {
         VertexPositionBuffer = GLBuffers.newDirectFloatBuffer(VertexPositions);

         // bind a named buffer object
         gl.glBindBuffer(
               GL_ARRAY_BUFFER, // target - vertex attributes
               _vboNames.get(Buffer.VERTEX_LOCATION) // name of buffer object
         );

         // create and initialize a buffer object's data store
         gl.glBufferData(
               GL_ARRAY_BUFFER, // target - vertex attributes
               VertexPositionBuffer.capacity() * Float.BYTES, // size in bytes
               VertexPositionBuffer, // data to copy in
               GL_STATIC_DRAW // usage hint (STATIC - modify once, used many times; DRAW - modified by app, source for GL commands)
         );
      }

      if (VertexColors != null) {
         VertexColorBuffer = GLBuffers.newDirectFloatBuffer(VertexColors);

         // bind a named buffer object
         gl.glBindBuffer(
               GL_ARRAY_BUFFER, // target - vertex attributes
               _vboNames.get(Buffer.VERTEX_COLOR) // name of buffer object
         );

         // create and initialize a buffer object's data store
         gl.glBufferData(
               GL_ARRAY_BUFFER, // target - vertex attributes
               VertexColorBuffer.capacity() * Float.BYTES, // size in bytes
               VertexColorBuffer, // data to copy in
               GL_STATIC_DRAW // usage hint (STATIC - modify once, used many times; DRAW - modified by app, source for GL commands)
         );

      }

      if (VertexIndices != null) {
         VertexIndexBuffer = GLBuffers.newDirectShortBuffer(VertexIndices);

         // bind a named buffer object
         gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, _vboNames.get(Buffer.ELEMENT));

         // create and initialize a buffer object's data store
         gl.glBufferData(
               GL_ELEMENT_ARRAY_BUFFER, // target -  vertex array indices
               VertexIndexBuffer.capacity() * Short.BYTES, // size in bytes
               VertexIndexBuffer, // data to copy in
               GL_STATIC_DRAW // usage hint (STATIC - modify once, used many times; DRAW - modified by app, source for GL commands)
         );
      }

      // unbind the binding point's previously bound buffer object
      gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
   }

   public void initVertexArray(GL3 gl) {

      // generate vertex array object (VAO) names
      gl.glGenVertexArrays(1, _vaoName);

      // bind a vertex array object (VAO)
      gl.glBindVertexArray(_vaoName.get(0));
      {
         // bind a named buffer object (GL_ARRAY_BUFFER - vertex attributes)
         gl.glBindBuffer(GL_ARRAY_BUFFER, _vboNames.get(Buffer.VERTEX_LOCATION));
         {
            int stride = (3) * Float.BYTES;
            int offset = 0;

            // enable or disable a generic vertex attribute array for currently bound VAO
            gl.glEnableVertexAttribArray(GL3Semantic.Attr.POSITION);

            // define an array of generic vertex attribute data
            gl.glVertexAttribPointer(
                  GL3Semantic.Attr.POSITION, // index of the generic vertex attribute to be modified
                  3, // number of components per generic vertex attribute
                  GL_FLOAT, // data type of each component in the array
                  false, // specifies whether fixed-point data values should be normalized
                  stride, // byte offset between consecutive generic vertex attributes
                  offset // offset of the first generic vertex attribute
            );
         }

         // bind a named buffer object (GL_ARRAY_BUFFER - vertex attributes)
         gl.glBindBuffer(GL_ARRAY_BUFFER, _vboNames.get(Buffer.VERTEX_COLOR));
         {
            int stride = (3) * Float.BYTES;
            int offset = 0;

            // enable or disable a generic vertex attribute array for the currently bound VAO
            gl.glEnableVertexAttribArray(GL3Semantic.Attr.COLOR);

            // define an array of generic vertex attribute data
            gl.glVertexAttribPointer(
                  GL3Semantic.Attr.COLOR, // index of the generic vertex attribute to be modified
                  3, // number of components per generic vertex attribute
                  GL_FLOAT,  // data type of each component in the array
                  false,  // specifies whether fixed-point data values should be normalized
                  stride, // byte offset between consecutive generic vertex attributes
                  offset // offset of the first generic vertex attribute
            );
         }

         // unbind
         gl.glBindBuffer(GL_ARRAY_BUFFER, 0);

         gl.glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, _vboNames.get(Buffer.ELEMENT));
      }
      gl.glBindVertexArray(0);

      //checkError(gl, "initVao");
   }

   public void initProgram(GL3 gl) {
      Program = new GL3Program(gl, "hello-triangle", "hello-triangle");
   }

   public void display(GL3 gl) {

      gl.glUseProgram(Program.name);

      gl.glBindVertexArray(_vaoName.get(0));

      // model matrix
      {
//         long now = System.currentTimeMillis();
//         float diff = (float) (now - _start) * Divisor;
//
//         diff = 1.0f;
//
//         float[] scale = FloatUtil.makeScale(new float[16], true, 500f, 500f, 500f);
//         float[] zRotation = FloatUtil.makeRotationEuler(new float[16], 0, diff, 0, 0);
//         float[] modelToWorldMat = FloatUtil.multMatrix(scale, zRotation);
//
//         for (int i = 0; i < 16; i++) {
//            _matBuffer.put(i, modelToWorldMat[i]);
//         }

         double[] colMajor = ObjectToWorld._matrix.getColMajor();

         for (int i = 0; i < 16; i++) {
            _matBuffer.put(i, (float)colMajor[i]);
         }

//         float[] identity = new float[16];
//         FloatUtil.makeIdentity(identity);
//
//         for (int i = 0; i < 16; i++) {
//            _matBuffer.put(i, identity[i]);
//         }

         // Modifies the value of a uniform variable or a uniform variable array.
         gl.glUniformMatrix4fv(Program.modelToWorldMatUL, 1, false, _matBuffer);
      }

      gl.glDrawElements(GL_TRIANGLES, VertexIndices.length, GL_UNSIGNED_SHORT, 0);

      gl.glBindVertexArray(0);

   }

   public void dispose(GL3 gl) {
      if (Program != null) {
         gl.glDeleteProgram(Program.name);
      }

      gl.glDeleteVertexArrays(1, _vaoName);

      gl.glDeleteBuffers(Buffer.MAX, _vboNames);
   }
}