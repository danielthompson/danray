package test.tracers.spectraltracer;

import net.danielthompson.danray.lights.SpectralSphereLight;
import net.danielthompson.danray.shading.Material;
import net.danielthompson.danray.shading.SpectralBlender;
import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.shading.SpectralReflectanceCurve;
import net.danielthompson.danray.shapes.Box;
import net.danielthompson.danray.structures.*;
import net.danielthompson.danray.structures.Point;
import net.danielthompson.danray.tracers.SpectralTracer;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.awt.*;

/**
 * Created by daniel on 5/6/15.
 */
public class SimpleTests {
   public double delta = .0000000000001;

   @BeforeMethod
   public void setUp() throws Exception {

   }

   @AfterMethod
   public void tearDown() throws Exception {

   }

   @Test
   public void testGetSPDForRay1() throws Exception {
      Scene scene = new NaiveScene(null);

      SpectralPowerDistribution lightSPD = new SpectralPowerDistribution();
      for (int i = 0; i < lightSPD.Buckets.length; i++) {
         lightSPD.Buckets[i] = 500000.0f;
      }

      SpectralSphereLight light = new SpectralSphereLight(10, null, lightSPD);
      light.Origin = new Point(4, 2, 0);
      light.Radius = 1;

      scene.SpectralRadiatables.add(light);

      Material boxMaterial = new Material();
      boxMaterial.SpectralReflectanceCurve = new SpectralReflectanceCurve();
      for (int i = 0; i < boxMaterial.SpectralReflectanceCurve.Buckets.length; i++) {
         boxMaterial.SpectralReflectanceCurve.Buckets[i] = 1.0f;
      }

      Point p0 = new Point(1, 4, 0);
      Point p1 = new Point(3, 6, 0);
      Box box = new Box(p0, p1, boxMaterial);

      scene.shapes.add(box);

      Point rayOrigin = new Point(0, 2, 0);
      Vector rayDirection = new Vector(1, 1, 0);

      Ray ray = new Ray(rayOrigin, rayDirection);

      SpectralTracer tracer = new SpectralTracer(scene, 1);

      SpectralPowerDistribution spectralPowerDistribution = tracer.GetSPDForRay(ray, 0);

      Color c = SpectralBlender.ConvertSPDtoRGB(spectralPowerDistribution);

      System.out.println("");
   }
}
