package net.danielthompson.danray.lights;

import net.danielthompson.danray.structures.Boundable;
import net.danielthompson.danray.structures.Point;

/**
 * Created with IntelliJ IDEA.
 * User: daniel
 * Date: 6/30/13
 * Time: 9:48
 */
public interface Radiatable extends Boundable {
   Point getRandomPointOnSurface();
   double getPower();
}
