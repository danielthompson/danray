package test.shapes.csg;

import net.danielthompson.danray.shapes.Box;
import net.danielthompson.danray.shapes.csg.CSGOperation;
import net.danielthompson.danray.shapes.csg.CSGShape;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Transform;
import net.danielthompson.danray.structures.Vector;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class DifferenceTests {

   private CSGShape shape;

   @BeforeMethod
   public void setUp() throws Exception {
      shape = new CSGShape(null);
      shape.Operation = CSGOperation.Difference;

      Transform[] inputTransforms = new Transform[]{
            Transform.Translate(new Vector(0, 0, 1)),
            Transform.Scale(2),

      };
      Transform[] compositeTransforms = Transform.composite(inputTransforms);

      shape.LeftShape = new Box(compositeTransforms, null);

      inputTransforms = new Transform[]{
            Transform.Translate(new Vector(1, 0, 0)),
            Transform.Scale(2),

      };
      compositeTransforms = Transform.composite(inputTransforms);

      shape.RightShape = new Box(compositeTransforms, null);
   }

   @AfterMethod
   public void tearDown() throws Exception {
      shape = null;
   }

   @Test
   public void shouldNotHit1() {
      Point origin = new Point(1, 0, -1);
      Vector direction = new Vector(1, 0, 1);
      Ray ray = new Ray(origin, direction);

      boolean hits = shape.Hits(ray);

      Assert.assertFalse(hits);
   }

   @Test
   public void shouldNotHit2() {
      Point origin = new Point(1, 0, -0.5f);
      Vector direction = new Vector(1, 0, 1);
      Ray ray = new Ray(origin, direction);

      boolean hits = shape.Hits(ray);

      Assert.assertFalse(hits);
   }

   @Test
   public void shouldHit1() {
      Point origin = new Point(0, 0, -2);
      Vector direction = new Vector(2, 0, 5);
      Ray ray = new Ray(origin, direction);

      boolean hits = shape.Hits(ray);
      Assert.assertTrue(hits);
   }
}
