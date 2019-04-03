package net.danielthompson.danray.imports;

import java.io.File;

public abstract class AbstractFileImporter<T> {
   protected File file;

   AbstractFileImporter(File file) {
      this.file = file;
   }

   public abstract T Process();
}
