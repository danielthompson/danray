package net.danielthompson.danray.lights;

import net.danielthompson.danray.acceleration.KDAxis;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.BoundingBox;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Ray;

/**
 * DanRay
 * User: dthompson
 * Date: 6/30/13
 * Time: 9:30
 */
public class PointLight implements Radiatable {
   private double _lumens;
   private Point _location;

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
   public double getPower() {
      return _lumens;
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