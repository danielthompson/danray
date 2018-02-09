package net.danielthompson.danray.ui.opengl;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.GLBuffers;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_ELEMENT_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_STATIC_DRAW;

public class GLShape {

   public interface Buffer {

      int VERTEX_LOCATION = 0;
      int VERTEX_COLOR = 1;
      int ELEMENT = 2;
      int GLOBAL_MATRICES = 3;
      int MAX = 4;
   }

   private IntBuffer _vboNames = GLBuffers.newDirectIntBuffer(Buffer.MAX);

   public float[] VertexPositions;

   public float[] VertexColors;

   public short[] VertexIndices;

   public FloatBuffer VertexPositionBuffer;

   public FloatBuffer VertexColorBuffer;

   public ShortBuffer VertexIndexBuffer;

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

      // uniforms

      // bind a named buffer object
      gl.glBindBuffer(gl.GL_UNIFORM_BUFFER, _vboNames.get(Buffer.GLOBAL_MATRICES));

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
            _vboNames.get(Buffer.GLOBAL_MATRICES) // name of the buffer object to bind to the target
      );
   }
}