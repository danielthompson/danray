package net.danielthompson.danray.cameras;

import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Transform;
import net.danielthompson.danray.structures.Vector;

/**
 * Created by dthompson on 03 Apr 17.
 */
public class TransformCamera {
   public CameraSettings Settings;

   public final Vector forward = new Vector(0, 0, -1);
   public final Vector up = new Vector(0, 0, 1);
   public final Vector right = new Vector(0, 0, 1);

   public Point _rearFocalPoint;

   public TransformCamera(CameraSettings settings) {
      
   }
}
