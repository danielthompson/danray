package net.danielthompson.danray.imports.pbrt.directives;

import net.danielthompson.danray.imports.pbrt.Constants;
import net.danielthompson.danray.imports.pbrt.PBRTDirective;

public class AttributeEndDirective extends PBRTDirective {
   public AttributeEndDirective() {
      super(null);
      this.Identifier = Constants.AttributeEnd;
   }
}
