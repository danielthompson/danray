package test.scene;

import net.danielthompson.danray.scenes.skyboxes.GradientSkybox;
import net.danielthompson.danray.shading.SpectralPowerDistribution;
import net.danielthompson.danray.structures.Vector;
import org.testng.annotations.Test;

import java.awt.*;

public class GradientSkyboxTests {
   @Test
   public void TestPhi() {
      GradientSkybox skybox = new GradientSkybox(Color.WHITE, Color.BLACK);

      Vector v = new Vector(0, -1, 0);

      SpectralPowerDistribution spd = skybox.getSkyBoxSPD(v);
   }
}
