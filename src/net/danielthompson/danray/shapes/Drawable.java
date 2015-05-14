package net.danielthompson.danray.shapes;

import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.Boundable;
import net.danielthompson.danray.structures.Ray;

/**
 * DanRay
 * User: dthompson
 * Date: 6/28/13
 * Time: 5:45 PM
 */
public interface Drawable extends Boundable {
   Material GetMaterial();
   double BRDF(IntersectionState state, Ray cameraRay, Ray lightRay);
}
