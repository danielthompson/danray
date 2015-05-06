package net.danielthompson.danray.cameras;

import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Vector;

/**
 * Created by daniel on 1/12/14.
 */
public interface Camera {

   Point getWorldPointForPixel(int x, int y);

   Point getWorldPointForPixel(double x, double y);

   Ray[] getInitialStochasticRaysForPixel(double x, double y, int samplesPerPixel);

   Ray[] getInitialStochasticRaysForPixel(int x, int y, int samplesPerPixel);

   Ray getStochasticRayForPixel(double x, double y);

   void setFrame(int frame);

   Point getOrigin();

   Vector getDirection();

   void moveOrigin(Vector offset);
}
