package net.danielthompson.danray.shapes;

import net.danielthompson.danray.acceleration.BoundingEdge;
import net.danielthompson.danray.acceleration.KDAxis;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.Constants;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Ray;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * DanRay
 * User: dthompson
 * Date: 7/12/13
 * Time: 5:23 PM
 */
public class Triangle extends AbstractShape {

   private Point _vertex0;
   private Point _vertex1;
   private Point _vertex2;

   private Point _normalDirection;

   public int ID;

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
   public boolean hits(Ray worldSpaceRay) {
      throw new NotImplementedException();
   }

   public static IntersectionState GetHitInfo(AbstractShape shape, Ray ray, Point vertex0, Point vertex1, Point vertex2) {
      IntersectionState state = new IntersectionState();
/*
      Point E1 = Point.Minus(vertex1, vertex0);
      Point E2 = Point.Minus(vertex2, vertex0);
      Point T = Point.Minus(ray.Origin, vertex0);
      Point Q = T.Cross(E1);
      Point P = ray.Direction.Cross(E2);

      float multiplier = 1.0 / P.Dot(E1);

      float t = Q.Dot(E2) * multiplier;
      float u = P.Dot(T) * multiplier;
      float v = Q.Dot(ray.Direction) * multiplier;

      if (t > 0 && u >= 0 && v >= 0 && u + v <= 1.0) {
         state.hits = true;
         state.IntersectionPoint = ray.ScaleFromOrigin(t);
         state.Drawable = drawable;
         state.T = t;
      }
      else {
         state.hits = false;
      }*/
      return state;
   }

   public static Ray GetNormal(Triangle triangle, IntersectionState state) {
      // TODO
      return null; //return new Ray(triangle._vertex0, triangle._normalDirection);
   }

   @Override
   public void RecalculateWorldBoundingBox() {

   }

   @Override
   public float getMedian(KDAxis axis) {
      return (_vertex0.getAxis(axis) + _vertex1.getAxis(axis) + _vertex2.getAxis(axis)) / 3.0f;
   }


}
