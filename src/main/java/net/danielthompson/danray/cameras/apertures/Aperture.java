package net.danielthompson.danray.cameras.apertures;

import net.danielthompson.danray.structures.Point3;

/**
 * Created by daniel on 3/2/14.
 */
public abstract class Aperture {
   public float Size;
   public abstract Point3 GetOriginPoint();
}
