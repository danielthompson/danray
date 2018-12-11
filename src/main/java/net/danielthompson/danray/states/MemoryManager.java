package net.danielthompson.danray.states;

/**
 * Created by dthompson on 3/11/2016.
 */
public class MemoryManager {
   public static final int NumInstances = 1024;

   public static final Intersection[] Stack = new Intersection[NumInstances];

   public static int AvailablePointer;

   public MemoryManager() {
      for (int i = 0; i < NumInstances; i++) {
         Stack[i] = new Intersection();
      }
   }

   public static Intersection Get() {
      return Stack[AvailablePointer++];
   }

   public static void Clean(Intersection state) {
      state.Face = null;
      state.Hits = false;

   }


}
