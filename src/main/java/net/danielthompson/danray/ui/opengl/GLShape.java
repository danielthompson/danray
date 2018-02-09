package net.danielthompson.danray.ui.opengl;

import com.jogamp.opengl.util.GLBuffers;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class GLShape {

   public float[] VertexPositions;

   public float[] VertexColors;

   public short[] VertexIndices;

   public FloatBuffer VertexPositionBuffer;

   public FloatBuffer VertexColorBuffer;

   public ShortBuffer VertexIndexBuffer;

   public void initBuffers() {
      if (VertexPositions != null) {
         VertexPositionBuffer = GLBuffers.newDirectFloatBuffer(VertexPositions);
      }

      if (VertexColors != null) {
         VertexColorBuffer = GLBuffers.newDirectFloatBuffer(VertexColors);
      }

      if (VertexIndices != null) {
         VertexIndexBuffer = GLBuffers.newDirectShortBuffer(VertexIndices);
      }
   }
}