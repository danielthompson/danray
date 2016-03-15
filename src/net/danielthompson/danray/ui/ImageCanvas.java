package net.danielthompson.danray.ui;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by daniel on 3/4/14.
 */
public class ImageCanvas extends Canvas {
   private BufferedImage _image;

   public ImageCanvas() {

   }

   public ImageCanvas(BufferedImage image) {
      this();
      _image = image;
   }

   public void setImage(BufferedImage image) {
      _image = image;
   }

   public void paint(Graphics g) {
      g.drawImage(_image, 0, 0, Color.magenta, null);
   }

   public void update(Graphics g) {
      paint(g);
   }

}
