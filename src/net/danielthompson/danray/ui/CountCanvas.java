package net.danielthompson.danray.ui;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

/**
 * Created by daniel on 3/4/14.
 */
public class CountCanvas extends Canvas implements MouseListener {
   private BufferedImage _image;

   public CountCanvas() {
      this.addMouseListener(this);
   }

   public CountCanvas(BufferedImage image) {
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

   @Override
   public void mouseClicked(MouseEvent mouseEvent) {
      /*if (Main.Finished) {
         int[] pixel = new int[2];
         pixel[0] = mouseEvent.getX();
         pixel[1] = mouseEvent.getY();

         Main.Retrace(pixel);
      }*/
   }

   @Override
   public void mousePressed(MouseEvent mouseEvent) {

   }

   @Override
   public void mouseReleased(MouseEvent mouseEvent) {

   }

   @Override
   public void mouseEntered(MouseEvent mouseEvent) {

   }

   @Override
   public void mouseExited(MouseEvent mouseEvent) {

   }
}
