package net.danielthompson.danray.textures;

import net.danielthompson.danray.shading.ReflectanceSpectrum;
import net.danielthompson.danray.states.Intersection;

public abstract class AbstractTexture {

   public abstract ReflectanceSpectrum Evaluate(Intersection intersection);
}
