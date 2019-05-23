package net.danielthompson.danray.shapes;

import net.danielthompson.danray.acceleration.BoundingEdge;
import net.danielthompson.danray.acceleration.KDAxis;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.states.Intersection;
import net.danielthompson.danray.structures.*;


import java.util.List;

/**
 * User: daniel
 * Date: 7/13/13
 * Time: 15:38
 */
public class TriangleMesh extends AbstractShape {

   private Material _material;

   private List<Point3> _vertices;
   private List<List<Point3>> _faces;
   private Point3 _origin;

   public int ID;

   @Override
   public void SetInCurrentKDNode(boolean value) {

   }

   private BoundingBox _boundingBox;

   public TriangleMesh(List<Point3> vertices, List<List<Point3>> faces) {
      this(vertices, faces, null);
   }

   public TriangleMesh(List<Point3> vertices, List<List<Point3>> faces, Point3 origin) {
      super(null);
      _vertices = vertices;
      _faces = faces;
      SetOrigin(origin);
   }

   public void SetOrigin(Point3 origin) {
      _boundingBox.Translate(Point3.minus(origin, _origin));

      _origin = origin;
      if (origin != null && origin.x != 0.0 && origin.y != 0.0 && origin.z != 0.0) {
         for (int i = 0; i < _vertices.size(); i++) {
            Point3 vertex = _vertices.get(i);
            vertex.plus(origin);
            _vertices.set(i, vertex);
         }
      }
   }

   public void SetMaterial(Material material) {
      _material = material;
   }


   @Override
   public BoundingEdge[] GetBoundingEdges(KDAxis axis) {
      return new BoundingEdge[0];
   }

   @Override
   public void SetBoundingEdges(BoundingEdge[] edges, KDAxis axis) {

   }

   @Override
   public Intersection GetHitInfo(Ray ray) {

      Intersection closestStateToRay = new Intersection();
      closestStateToRay.hits = false;
      closestStateToRay.t = Float.MAX_VALUE;

      for (List<Point3> face : _faces) {
         Intersection state = Triangle.GetHitInfo(this, ray, face.get(0), face.get(1), face.get(2));

         if (state.hits && state.t < closestStateToRay.t) {
            closestStateToRay = state;
            //state.Face = face;
         }
      }

      return closestStateToRay;
   }

   @Override
   public List<Intersection> GetAllHitPoints(Ray ray) {
      return null;
   }

   @Override
   public boolean Hits(Ray worldSpaceRay) {
      throw new java.lang.UnsupportedOperationException();
   }

   /*
   public Ray GetNormal(Intersection state) {
      return new Ray(state.Face.get(0), state.Face.get(0).cross(state.Face.get(1)));
   }
   */


   private void recalcBoundingBox() {
      float minX = Float.MAX_VALUE;
      float maxX = Float.MIN_VALUE;

      float minY = Float.MAX_VALUE;
      float maxY = Float.MIN_VALUE;

      float minZ = Float.MAX_VALUE;
      float maxZ = Float.MIN_VALUE;

      for (Point3 vertex : _vertices) {
         float x = vertex.x;
         float y = vertex.y;
         float z = vertex.z;

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

      _boundingBox = new BoundingBox(new Point3(minX, minY, minZ), new Point3(maxX, maxY, maxZ));
   }

   @Override
   public void RecalculateWorldBoundingBox() {

   }


   @Override
   public float GetMedian(KDAxis axis) {
      return _boundingBox.getMedian(axis);
   }
}


















