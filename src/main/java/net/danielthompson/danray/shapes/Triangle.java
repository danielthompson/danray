package net.danielthompson.danray.shapes;

import net.danielthompson.danray.acceleration.BoundingEdge;
import net.danielthompson.danray.acceleration.KDAxis;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.Point3;
import net.danielthompson.danray.structures.Ray;

import java.util.List;

/**
 * DanRay
 * User: dthompson
 * Date: 7/12/13
 * Time: 5:23 PM
 */
public class Triangle extends AbstractShape {

   private Point3 _vertex0;
   private Point3 _vertex1;
   private Point3 _vertex2;

   private Point3 _normalDirection;

   public int ID;

   @Override
   public void SetInCurrentKDNode(boolean value) {

   }

   private Material _material;

   public Triangle(Point3 vertex0, Point3 vertex1, Point3 vertex2, Material material) {
      super(material);
      _vertex0 = vertex0;
      _vertex1 = vertex1;
      _vertex2 = vertex2;

      _material = material;

      _normalDirection = _vertex1.cross(_vertex0);
   }

   public Triangle(Point3 vertex0, Point3 vertex1, Point3 vertex2, Point3 normalDirection, Material material) {
      super(material);
      _vertex0 = vertex0;
      _vertex1 = vertex1;
      _vertex2 = vertex2;

      _material = material;

      _normalDirection = normalDirection;
   }

   @Override
   public BoundingEdge[] GetBoundingEdges(KDAxis axis) {
      return new BoundingEdge[0];
   }

   @Override
   public void SetBoundingEdges(BoundingEdge[] edges, KDAxis axis) {

   }

   @Override
   public Intersection intersect(Ray ray) {
      return GetHitInfo(this, ray, _vertex0, _vertex1, _vertex2);
   }

   @Override
   public List<Intersection> intersectAll(Ray ray) {
      return null;
   }

   @Override
   public boolean hits(Ray worldSpaceRay) {
      throw new java.lang.UnsupportedOperationException();
   }

   public static Intersection GetHitInfo(AbstractShape shape, Ray ray, Point3 vertex0, Point3 vertex1, Point3 vertex2) {
      Intersection state = new Intersection();
/*
      Point E1 = Point.minus(vertex1, vertex0);
      Point E2 = Point.minus(vertex2, vertex0);
      Point T = Point.minus(ray.Origin, vertex0);
      Point Q = T.cross(E1);
      Point P = ray.Direction.cross(E2);

      float multiplier = 1.0 / P.dot(E1);

      float t = Q.dot(E2) * multiplier;
      float u = P.dot(T) * multiplier;
      float v = Q.dot(ray.Direction) * multiplier;

      if (t > 0 && u >= 0 && v >= 0 && u + v <= 1.0) {
         state.hits = true;
         state.location = ray.scaleFromOrigin(t);
         state.Drawable = drawable;
         state.T = t;
      }
      else {
         state.hits = false;
      }*/
      return state;
   }

   public static Ray GetNormal(Triangle triangle, Intersection state) {
      // TODO
      return null; //return new Ray(triangle._vertex0, triangle._normalDirection);
   }

   @Override
   public void recalculateWorldBoundingBox() {

   }

   @Override
   public float GetMedian(KDAxis axis) {
      return (_vertex0.getAxis(axis) + _vertex1.getAxis(axis) + _vertex2.getAxis(axis)) / 3.0f;
   }


}
