package net.danielthompson.danray.shapes;

import net.danielthompson.danray.acceleration.KDAxis;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.states.IntersectionState;
import net.danielthompson.danray.structures.*;

import java.util.List;

/**
 * User: daniel
 * Date: 7/13/13
 * Time: 15:38
 */
public class TriangleMesh implements Shape {

   private Material _material;

   private List<Point> _vertices;
   private List<List<Point>> _faces;
   private Point _origin;

   private Tuple _rotation;

   public int ID;
   public int getID() {
      return ID;
   }

   @Override
   public boolean InCurrentKDNode() {
      return false;
   }

   @Override
   public void SetInCurrentKDNode(boolean value) {

   }

   private BoundingBox _boundingBox;

   public TriangleMesh(List<Point> vertices, List<List<Point>> faces) {
      this(vertices, faces, null, null);
   }

   public TriangleMesh(List<Point> vertices, List<List<Point>> faces, Point origin) {
      this (vertices, faces, origin, null);
   }

   public TriangleMesh(List<Point> vertices, List<List<Point>> faces, Point origin, Tuple rotation) {
      _vertices = vertices;
      _faces = faces;
      SetRotation(rotation);
      SetOrigin(origin);
   }

   public void SetOrigin(Point origin) {
      _boundingBox.Translate(Point.Minus(origin, _origin));

      _origin = origin;
      if (origin != null && origin.X != 0.0 && origin.Y != 0.0 && origin.Z != 0.0) {
         for (int i = 0; i < _vertices.size(); i++) {
            Point vertex = _vertices.get(i);
            vertex.Plus(origin);
            _vertices.set(i, vertex);
         }
      }
   }

   public void SetRotation(Tuple rotation) {
      Point originalOrigin = _origin;

      if (originalOrigin != null && !originalOrigin.equals(new Point(0, 0, 0)))
         SetOrigin(Point.Scale(originalOrigin, -1));

      _rotation = rotation;

      if (_rotation != null) {
         for (Point point : _vertices) {
            point.Rotate(new Ray(new Point(0, 0, 0), new Vector(1, 0, 0)), rotation.X);
            point.Rotate(new Ray(new Point(0, 0, 0), new Vector(0, 1, 0)), rotation.Y);
            point.Rotate(new Ray(new Point(0, 0, 0), new Vector(0, 0, 1)), rotation.Z);
         }
      }

      SetOrigin(originalOrigin);
      recalcBoundingBox();
   }

   public void SetMaterial(Material material) {
      _material = material;
   }

   @Override
   public Material GetMaterial() {
      return _material;
   }

   @Override
   public double getSurfaceArea() {
      return 0.0;
   }

   @Override
   public IntersectionState GetHitInfo(Ray ray) {

      IntersectionState closestStateToRay = new IntersectionState();
      closestStateToRay.Hits = false;
      closestStateToRay.TMin = Double.MAX_VALUE;

      for (List<Point> face : _faces) {
         IntersectionState state = Triangle.GetHitInfo(this, ray, face.get(0), face.get(1), face.get(2));

         if (state.Hits && state.TMin < closestStateToRay.TMin) {
            closestStateToRay = state;
            state.Face = face;
         }
      }

      return closestStateToRay;
   }

   @Override
   public boolean Hits(Ray ray) {
      return GetHitInfo(ray).Hits;
   }

   /*
   public Ray GetNormal(IntersectionState state) {
      return new Ray(state.Face.get(0), state.Face.get(0).Cross(state.Face.get(1)));
   }
   */

   @Override
   public double GetVolume() {
      return 0;
   }

   private void recalcBoundingBox() {
      double minX = Double.MAX_VALUE;
      double maxX = Double.MIN_VALUE;

      double minY = Double.MAX_VALUE;
      double maxY = Double.MIN_VALUE;

      double minZ = Double.MAX_VALUE;
      double maxZ = Double.MIN_VALUE;

      for (Point vertex : _vertices) {
         double x = vertex.X;
         double y = vertex.Y;
         double z = vertex.Z;

         if (x < minX)
            minX = x;
         if (x > maxX)
            maxX = x;

         if (y < minY)
            minY = y;
         if (y > maxY)
            maxY = y;

         if (z < minZ)
            minZ = z;
         if (z > maxZ)
            maxZ = z;
      }

      _boundingBox = new BoundingBox(new Point(minX, minY, minZ), new Point(maxX, maxY, maxZ));
   }

   @Override
   public BoundingBox GetWorldBoundingBox() {
      return _boundingBox;
   }

   @Override
   public double getMedian(KDAxis axis) {
      return _boundingBox.getMedian(axis);
   }
}


















