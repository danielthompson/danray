package net.danielthompson.danray.lights;

import net.danielthompson.danray.acceleration.KDAxis;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.BoundingBox;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Vector;

/**
 * DanRay
 * User: dthompson
 * Date: 6/30/13
 * Time: 9:30
 */
public class PointLight implements Radiatable {
   protected double _lumens;
   protected Point _location;

   public int ID;
   public int getID() {
      return ID;
   }

   public PointLight(Point location, double lumens) {
      _lumens = lumens;
      _location = location;
   }

   @Override
   public Point getRandomPointOnSurface() {
      return _location;
   }

   @Override
   public Point getRandomPointOnSideOf(Vector side) {
      return _location;
   }

   @Override
   public Point getRandomPointOnSideOf(Point point) {
      return _location;
   }

   @Override
   public Ray getRandomRayInPDF() {
      return null;
   }

   @Override
   public double getPower() {
      return _lumens;
   }

   @Override
   public double getPDF(Point point, Vector directionFromLightToPoint) {
      return 0;
   }

   @Override
   public double GetVolume() {
      return 0;
   }

   @Override
   public BoundingBox GetWorldBoundingBox() {
      return null;
   }

   @Override
   public double getMedian(KDAxis axis) {
      return _location.getAxis(axis);
   }

   @Override
   public IntersectionState GetHitInfo(Ray ray) {
      return null;
   }

   @Override
   public boolean Hits(Ray ray) {
      return false;
   }
}