package net.danielthompson.danray.imports;

import net.danielthompson.danray.scenes.AbstractScene;

import java.io.File;

public class PBRTImporter extends AbstractFileImporter<AbstractScene> {

   public PBRTImporter(File file) {
      super(file);
   }

   @Override
   public AbstractScene Process() {
      return null;
   }
}
