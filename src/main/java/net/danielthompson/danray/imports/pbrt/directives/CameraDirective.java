package net.danielthompson.danray.imports.pbrt.directives;

import net.danielthompson.danray.cameras.Camera;
import net.danielthompson.danray.cameras.PerspectiveCamera;
import net.danielthompson.danray.imports.pbrt.Constants;
import net.danielthompson.danray.imports.pbrt.PBRTArgument;
import net.danielthompson.danray.imports.pbrt.PBRTDirective;

import java.util.ArrayList;
import java.util.List;

public class CameraDirective extends PBRTDirective {

   List<PBRTArgument> Arguments;

   public CameraDirective(List<String> words) {
      super(words);
      this.Identifier = Constants.Camera;
      this.Type = getType(words);

      Arguments = new ArrayList<>();

      for (int i = 4; i < words.size(); i += 8) {
         PBRTArgument<Float> arg = getFloatArgument(words, 4);
         Arguments.add(arg);
      }
   }

   public Camera Parse() {
      float fov;

      for (PBRTArgument arg : Arguments) {
         if (arg.Name.equals("fov"))
            fov = (Float)arg.Values.get(0);
      }

      return new PerspectiveCamera(null, null);
   }
}
