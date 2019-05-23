package net.danielthompson.danray.ui;

import net.danielthompson.danray.Main;
import net.danielthompson.danray.TraceManager;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class MainCanvas extends Canvas implements MouseListener, MouseMotionListener, KeyListener {
   private BufferedImage _image;
   private TraceManager _manager;

   public MainCanvas(TraceManager manager, BufferedImage image) {
      _manager = manager;
      _image = image;

      this.addMouseListener(this);
      this.addMouseMotionListener(this);
   }

   public MainCanvas(BufferedImage image) {
      this(null, image);

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
      if (Main.Finished) {
         int[] pixel = new int[2];
         pixel[0] = mouseEvent.getX();
         pixel[1] = mouseEvent.getY();

         Main.Retrace(pixel);
      }
   }

   @Override
   public void mousePressed(MouseEvent mouseEvent) {
      int x = mouseEvent.getX();
      int y = mouseEvent.getY();

      if (_manager != null)
         _manager.setMouseClickXY(x, y);
      System.out.println("Click at x: " + x + " y: " + y);


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

   @Override
   public void mouseDragged(MouseEvent mouseEvent) {

   }

   @Override
   public void mouseMoved(MouseEvent mouseEvent) {
      /*int[] pixel = new int[2];
      pixel[0] = mouseEvent.getX();
      pixel[1] = mouseEvent.getY();
*/
      int x = mouseEvent.getX();
      int y = mouseEvent.getY();

      if (_manager != null)
         _manager.setMouseXY(x, y);
      System.out.println("Hover at x: " + x + " y: " + y);
   }

   @Override
   public void keyPressed(KeyEvent event) {

   }

   @Override
   public void keyTyped(KeyEvent event) {
      char key = event.getKeyChar();
      /*
      switch (key) {
         case 'a':
            // move camera left 10

      }*/
   }

   @Override
   public void keyReleased(KeyEvent event) {

   }
}