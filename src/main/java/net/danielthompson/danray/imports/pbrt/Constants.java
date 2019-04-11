package net.danielthompson.danray.imports.pbrt;

import java.util.Arrays;
import java.util.List;

public class Constants {
   public static final String Accelerator = "Accelerator";
   public static final String AreaLightSource = "AreaLightSource";
   public static final String AttributeBegin = "AttributeBegin";
   public static final String AttributeEnd = "AttributeEnd";
   public static final String Camera = "Camera";
   public static final String Film = "Film";
   public static final String Integrator = "Integrator";
   public static final String LightSource = "LightSource";
   public static final String LookAt = "LookAt"; // done
   public static final String MakeNamedMaterial = "MakeNamedMaterial";
   public static final String Material = "Material";
   public static final String NamedMaterial = "NamedMaterial";
   public static final String PixelFilter = "PixelFilter";
   public static final String Sampler = "Sampler";
   public static final String Shape = "Shape";
   public static final String Texture = "Texture";
   public static final String TransformBegin = "TransformBegin";
   public static final String TransformEnd = "TransformEnd";
   public static final String Translate = "Translate";
   public static final String WorldBegin = "WorldBegin"; // done
   public static final String WorldEnd = "WorldEnd";

   public static final List<String> AllDirectives = Arrays.asList(
         Constants.Accelerator,
         Constants.AreaLightSource,
         Constants.AttributeBegin,
         Constants.AttributeEnd,
         Constants.Camera,
         Constants.Film,
         Constants.Integrator,
         Constants.LightSource,
         Constants.LookAt,
         Constants.MakeNamedMaterial,
         Constants.Material,
         Constants.NamedMaterial,
         Constants.PixelFilter,
         Constants.Sampler,
         Constants.Shape,
         Constants.Texture,
         Constants.TransformBegin,
         Constants.TransformEnd,
         Constants.Translate,
         Constants.WorldBegin,
         Constants.WorldEnd
   );

   public static final List<String> SceneWideDirectives = Arrays.asList(
         Constants.Accelerator,
         //Constants.AreaLightSource,
         //Constants.AttributeBegin,
         //Constants.AttributeEnd,
         Constants.Camera,
         Constants.Film,
         Constants.Integrator,
         Constants.LookAt,
         Constants.MakeNamedMaterial,
         //Constants.NamedMaterial,
         Constants.PixelFilter,
         Constants.Sampler,
         //Constants.Shape,
         Constants.Texture,
         Constants.TransformBegin,
         Constants.TransformEnd,
         Constants.Translate
         //Constants.WorldBegin,
         //Constants.WorldEnd
   );

   public static final List<String> WorldDirectives = Arrays.asList(
         //Constants.Accelerator,
         Constants.AreaLightSource,
         Constants.AttributeBegin,
         Constants.AttributeEnd,
         //Constants.Camera,
         //Constants.Film,
         //Constants.Integrator,
         Constants.LightSource,
         Constants.LookAt,
         Constants.MakeNamedMaterial,
         Constants.Material,
         Constants.NamedMaterial,
         //Constants.PixelFilter,
         //Constants.Sampler,
         Constants.Shape,
         Constants.Texture,
         Constants.TransformBegin,
         Constants.TransformEnd,
         Constants.Translate
         //Constants.WorldBegin,
         //Constants.WorldEnd
   );
}
