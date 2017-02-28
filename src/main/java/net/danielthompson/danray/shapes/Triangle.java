package net.danielthompson.danray.shapes;

import net.danielthompson.danray.acceleration.BoundingEdge;
import net.danielthompson.danray.acceleration.KDAxis;
import net.danielthompson.danray.lights.Radiatable;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.BoundingBox;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Vector;

/**
 * DanRay
 * User: dthompson
 * Date: 7/12/13
 * Time: 5:23 PM
 */
public class Triangle extends AbstractShape implements Radiatable {

   private Point _vertex0;
   private Point _vertex1;
   private Point _vertex2;

   private Point _normalDirection;

   public int ID;
   public int getID() {
      return ID;
   }

   @Override
   public void SetInCurrentKDNode(boolean value) {

   }

   private Material _material;

   public Triangle(Point vertex0, Point vertex1, Point vertex2, Material material) {
      super(material);
      _vertex0 = vertex0;
      _vertex1 = vertex1;
      _vertex2 = vertex2;

      _material = material;

      _normalDirection = _vertex1.Cross(_vertex0);
   }

   public Triangle(Point vertex0, Point vertex1, Point vertex2, Point normalDirection, Material material) {
      super(material);
      _vertex0 = vertex0;
      _vertex1 = vertex1;
      _vertex2 = vertex2;

      _material = material;

      _normalDirection = normalDirection;
   }

   @Override
   public Material GetMaterial() {
      return _material;
   }

   @Override
   public BoundingEdge[] GetBoundingEdges(KDAxis axis) {
      return new BoundingEdge[0];
   }

   @Override
   public void SetBoundingEdges(BoundingEdge[] edges, KDAxis axis) {

   }

   @Override
   public IntersectionState getHitInfo(Ray ray) {
      return GetHitInfo(this, ray, _vertex0, _vertex1, _vertex2);
   }

   @Override
   public boolean Hits(Ray ray) {
      return getHitInfo(ray).Hits;
   }

   public static IntersectionState GetHitInfo(AbstractShape shape, Ray ray, Point vertex0, Point vertex1, Point vertex2) {
      IntersectionState state = new IntersectionState();
/*
      Point E1 = Point.Minus(vertex1, vertex0);
      Point E2 = Point.Minus(vertex2, vertex0);
      Point T = Point.Minus(ray.Origin, vertex0);
      Point Q = T.Cross(E1);
      Point P = ray.Direction.Cross(E2);

      double multiplier = 1.0 / P.Dot(E1);

      double t = Q.Dot(E2) * multiplier;
      double u = P.Dot(T) * multiplier;
      double v = Q.Dot(ray.Direction) * multiplier;

      if (t > 0 && u >= 0 && v >= 0 && u + v <= 1.0) {
         state.Hits = true;
         state.IntersectionPoint = ray.ScaleFromOrigin(t);
         state.Drawable = drawable;
         state.T = t;
      }
      else {
         state.Hits = false;
      }*/
      return state;
   }

   public static Ray GetNormal(Triangle triangle, IntersectionState state) {
      // TODO
      return null; //return new Ray(triangle._vertex0, triangle._normalDirection);
   }

   @Override
   public BoundingBox GetWorldBoundingBox() {
      return null;
   }

   @Override
   public double getMedian(KDAxis axis) {
      return (_vertex0.getAxis(axis) + _vertex1.getAxis(axis) + _vertex2.getAxis(axis)) / 3.0;
   }


   @Override
   public SpectralPowerDistribution getSPD() {
      return null;
   }

   @Override
   public Point getRandomPointOnSurface() {
      return null;
   }

   @Override
   public Point getRandomPointOnSideOf(Vector side) {
      return null;
   }

   @Override
   public Point getRandomPointOnSideOf(Point point) {
      return null;
   }

   @Override
   public Ray getRandomRayInPDF() {
      return null;
   }

   @Override
   public double getPower() {
      return 0;
   }

   @Override
   public double getPDF(Point point, Vector directionFromLightToPoint) {
      return 0; // TODO
   }
}
