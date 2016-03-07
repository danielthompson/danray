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
   public Point _location;

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

   public double GetVolume() {
      return 0;
   }

   public BoundingBox GetWorldBoundingBox() {
      return null;
   }

   public double getMedian(KDAxis axis) {
      return _location.getAxis(axis);
   }

   public IntersectionState GetHitInfo(Ray ray) {
      return null;
   }

   public boolean Hits(Ray ray) {
      return false;
   }
}