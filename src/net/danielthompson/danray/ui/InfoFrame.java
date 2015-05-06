package net.danielthompson.danray.ui;

import java.awt.*;

/**
 * Created by daniel on 4/28/15.
 */
public class InfoFrame extends Frame {

   private Label _mouseLocationLabel;
   private Label _sceneLocationLabel;

   public InfoFrame(String title) {
      super(title);

      this.setLayout(new GridBagLayout());

      GridBagConstraints c = new GridBagConstraints();

      _mouseLocationLabel = new Label();
      _mouseLocationLabel.setBounds(10, 50, 300, 50);
      _mouseLocationLabel.setVisible(true);
      _mouseLocationLabel.setForeground(Color.BLACK);
      _mouseLocationLabel.setBackground(Color.WHITE);
      _mouseLocationLabel.setText("Startup text");

      this.add(_mouseLocationLabel);

      _sceneLocationLabel = new Label();
      _sceneLocationLabel.setBounds(10, 100, 300, 50);
      _sceneLocationLabel.setVisible(true);
      _sceneLocationLabel.setForeground(Color.BLACK);
      _sceneLocationLabel.setBackground(Color.WHITE);
      _sceneLocationLabel.setText("Startup text");

      this.add(_sceneLocationLabel);

   }

   public void setMouseLocation(int x, int y) {
      _mouseLocationLabel.setText("Mouse location: (" + x + ", " + y + ")");
      _mouseLocationLabel.paint(this.getGraphics());
      this.paint(this.getGraphics());
   }

   public void setSceneLocation(double x, double y, double z) {
      _sceneLocationLabel.setText("Scene location: (" + x + ", " + y + ", " + z + ")");
      _sceneLocationLabel.paint(this.getGraphics());
      this.paint(this.getGraphics());
   }

   public void setNoSceneLocation() {
      _sceneLocationLabel.setText("Scene location: no hit");
      _sceneLocationLabel.paint(this.getGraphics());
      this.paint(this.getGraphics());
   }

}
