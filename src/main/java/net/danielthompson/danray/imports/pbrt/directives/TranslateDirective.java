package net.danielthompson.danray.imports.pbrt.directives;

import net.danielthompson.danray.imports.pbrt.Constants;
import net.danielthompson.danray.imports.pbrt.PBRTDirective;

import java.util.List;

public class TranslateDirective extends PBRTDirective {

   public TranslateDirective(List<String> words) {
      super(words);
      this.Identifier = Constants.Translate;
   }
}
