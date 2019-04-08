package net.danielthompson.danray.imports.pbrt.directives;

import net.danielthompson.danray.imports.pbrt.Constants;
import net.danielthompson.danray.imports.pbrt.PBRTDirective;

import java.util.List;

public class SamplerDirective extends PBRTDirective {
   public SamplerDirective(List<String> words) {
      super(words);
      this.Identifier = Constants.Sampler;
   }
}
