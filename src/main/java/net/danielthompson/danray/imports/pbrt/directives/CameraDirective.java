package net.danielthompson.danray.imports.pbrt.directives;

import net.danielthompson.danray.imports.pbrt.Constants;
import net.danielthompson.danray.imports.pbrt.PBRTArgument;
import net.danielthompson.danray.imports.pbrt.PBRTDirective;

import java.util.ArrayList;
import java.util.List;

public class CameraDirective extends PBRTDirective {

   public CameraDirective(List<String> words) {
      super(words);
      this.Identifier = Constants.Camera;
      this.Type = words.get(2);

      Arguments = new ArrayList<PBRTArgument>();

      for (int i = 3; i < words.size(); i += 8) {
         PBRTArgument argument = new PBRTArgument();
         argument.Name = words.get(i + 1);
         argument.Type = words.get(i + 4);
         argument.Values = new ArrayList<>();
         argument.Values.add(words.get(i + 6));
      }
   }

}
