package net.danielthompson.danray.textures;

import net.danielthompson.danray.shading.ReflectanceSpectrum;

public abstract class AbstractTexture {

   public abstract ReflectanceSpectrum Evaluate(float u, float v);
}
