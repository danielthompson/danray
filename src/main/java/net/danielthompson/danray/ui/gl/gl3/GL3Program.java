package net.danielthompson.danray.ui.gl.gl3;

import com.jogamp.opengl.GL2ES2;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;

public class GL3Program {

   public int name, modelToWorldMatUL;

   // "shaders/gl3", "hello-triangle", "hello-triangle");

   public GL3Program(GL3 gl, String vertex, String fragment) {

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
      gl.glUniformBlockBinding(name, globalMatricesBI, GL3Semantic.Uniform.GLOBAL_MATRICES);
   }
}
