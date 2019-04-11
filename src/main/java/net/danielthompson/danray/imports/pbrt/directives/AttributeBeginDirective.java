package net.danielthompson.danray.imports.pbrt.directives;

import net.danielthompson.danray.imports.pbrt.Constants;
import net.danielthompson.danray.imports.pbrt.PBRTDirective;

public class AttributeBeginDirective extends PBRTDirective {
   public AttributeBeginDirective() {
      super(null);
      this.Identifier = Constants.AttributeBegin;
   }
}
