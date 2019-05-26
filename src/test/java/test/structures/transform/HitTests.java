package test.structures.transform;

import junit.framework.Assert;
import net.danielthompson.danray.shapes.Box;
import net.danielthompson.danray.structures.Point3;
import net.danielthompson.danray.structures.Ray;
import net.danielthompson.danray.structures.Transform;
import net.danielthompson.danray.structures.Vector3;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import test.AssertHelper;

/**
 * Created by daniel on 2/16/15.
 */
public class HitTests {

   @BeforeMethod
   public void setUp() throws Exception {

   }

   @AfterMethod
   public void tearDown() throws Exception {

   }

   @DataProvider(name = "ScaleDataProvider")
   public Object[][] ScaleDataProvider() {
      return new Object[][] {
            {
                  new Ray(new Point3(0, 0, 0), new Vector3(1, 1, 1)),
                  Transform.scale(1, 1, 1),
                  new Ray(new Point3(0, 0, 0), new Vector3(1, 1, 1)) },
            {
                  new Ray(new Point3(0, 0, 0), new Vector3(1, 1, 1)),
                  Transform.scale(-1, -1, -1),
                  new Ray(new Point3(0, 0, 0), new Vector3(-1, -1, -1)) },
            {
                  new Ray(new Point3(2, 2, 2), new Vector3(1, 1, 1)),
                  Transform.scale(1, 1, 1),
                  new Ray(new Point3(2, 2, 2), new Vector3(1, 1, 1)) },
            {
                  new Ray(new Point3(2, 2, 2), new Vector3(5, 5, 5)),
                  Transform.scale(-1, 5, 1),
                  new Ray(new Point3(-2, 10, 2), new Vector3(-5, 25, 5)) },
      };
   }

   @Test(dataProvider = "ScaleDataProvider")
   public void TestApplyScale(Ray originalRay, Transform transform, Ray expectedNewRay) {

      Ray actualNewRay = transform.apply(originalRay);

      Assert.assertNotNull(actualNewRay);
      AssertHelper.assertEquals(expectedNewRay, actualNewRay);
   }

   @Test
   public void TestHitBox1() {
      Transform[] transforms = new Transform[2];
      transforms[0] = Transform.translate(new Vector3(-1.0f, -1.0f, -1.0f));
      transforms[1] = Transform.scale(2.0f, 1.0f, 1.0f);
      Transform compositeTransform[] = Transform.composite(transforms);

      Box box = new Box(compositeTransform, null);

      box.RecalculateWorldBoundingBox();

      Point3 origin = new Point3(5, 5, 5);
      Vector3 direction = new Vector3(-1, -1, -1);

      Ray ray = new Ray(origin, direction);

      boolean hits = box.Hits(ray);

      Assert.assertTrue(hits);
   }

   @Test
   public void TestHitBox2() {

      Transform[] transforms = new Transform[2];
      transforms[0] = Transform.translate(new Vector3(-1.0f, -1.0f, -1.0f));
      transforms[1] = Transform.scale(4.0f, 2.0f, 2.0f);
      Transform compositeTransform[] = Transform.composite(transforms);

      Box box = new Box(compositeTransform, null);

      box.RecalculateWorldBoundingBox();

      Point3 origin = new Point3(5, 5, 5);
      Vector3 direction = new Vector3(-1, -1, -1);

      Ray ray = new Ray(origin, direction);

      boolean hits = box.Hits(ray);

      Assert.assertTrue(hits);
   }

}
