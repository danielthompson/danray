package net.danielthompson.danray.ui;

import net.danielthompson.danray.exports.SPDFileExporter;
import net.danielthompson.danray.imports.SPDFileImporter;
import net.danielthompson.danray.shading.SpectralBlender;
import net.danielthompson.danray.shading.SpectralPowerDistribution;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by daniel on 8/15/15.
 */
public class SPDEditorView {

   public JPanel mainPanel;
   private JButton loadButton;
   private JButton revertButton;
   private JButton saveButton;
   private JButton newButton;
   private JTextField powerTextField;
   private JSlider spdSlider0;

   private JPanel spdSliderPanel;
   private JLabel colorLabel;
   private JPanel spdMainPanel;

   private JSlider[] _spdSliders;
   private JLabel[] _spdWavelengthLabels;
   private JLabel[] _spdPowerLabels;
   private JLabel[] _spdColorLabels;

   private File _spdFile;
   private SpectralPowerDistribution _spd;

   private boolean _enableSliderListeners = false;

   private double _spdSliderAdjustmentFactor;
   private int _spdAdjustmentResultPosition = 80;

   private ChangeListener _spdSliderChangeListener = new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
         JSlider source = (JSlider)e.getSource();
         int sliderValue = source.getValue();
         int spdPointer = Integer.parseInt(source.getName());
         System.out.print("slider [" + spdPointer + "] state changed to [" + sliderValue + "], ");

         if (!source.getValueIsAdjusting() && _enableSliderListeners) {
            System.out.println("actioning...");
            float oldSpdValue = _spd.Buckets[spdPointer];
            float newSpdValue = (float)(sliderValue / _spdSliderAdjustmentFactor);

            _spd.Buckets[spdPointer] = newSpdValue;

            setSliders(_spd);

         }
         else {
            System.out.println("not actioning.");
         }

      }
   };

   public SPDEditorView() {

      populateSpdSliderList();

      for (int i = 0; i < _spdSliders.length; i++) {
         JSlider slider = _spdSliders[i];
         slider.setName(String.valueOf(i));
         slider.addChangeListener(_spdSliderChangeListener);

         JLabel wavelengthLabel = _spdWavelengthLabels[i];
         wavelengthLabel.setText(String.valueOf((i + 38) * 10));

         JLabel colorLabel = _spdColorLabels[i];

         SpectralPowerDistribution spd = new SpectralPowerDistribution();
         spd.Buckets[i] = .1f;

         Color c = SpectralBlender.ConvertSPDtoRGB(spd);

         c = new Color(c.getRed(), c.getGreen(), c.getBlue());

         colorLabel.setOpaque(true);
         colorLabel.setBackground(c);
         colorLabel.setText(String.valueOf((i + 38) * 10));
         colorLabel.setForeground(Color.white);

         slider.setOpaque(true);
         slider.setBackground(c);


      }



      loadButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            if (_spdFile != null) {
               chooser.setCurrentDirectory(_spdFile);
            }
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                  "Spectral Power Distributions", "spd", "rspd");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showOpenDialog(mainPanel);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
               _spdFile = chooser.getSelectedFile();
               System.out.println("You chose to open this file: " + _spdFile.getName());
               loadSPD(_spdFile);
            }
         }
      });

      saveButton.addActionListener(new ActionListener() {
         @Override
         public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            if (_spdFile != null) {
               chooser.setCurrentDirectory(_spdFile);
            }
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                  "Spectral Power Distributions", "spd", "rspd");
            chooser.setFileFilter(filter);
            int returnVal = chooser.showSaveDialog(mainPanel);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
               File file = chooser.getSelectedFile();
               saveSPD(file);
            }
         }
      });

   }

   private void saveSPD(File file) {
      SPDFileExporter exporter = new SPDFileExporter(_spd, file);
      exporter.Process();
      _spdFile = file;
   }

   private void populateSpdSliderList() {
      ArrayList<JSlider> spdSliderList = new ArrayList<>();
      ArrayList<JLabel> wavelengthList = new ArrayList<>();
      ArrayList<JLabel> powerList = new ArrayList<>();
      ArrayList<JLabel> colorList = new ArrayList<>();

      for (Component mainPanelComponent : spdSliderPanel.getComponents()) {
         if (mainPanelComponent instanceof JPanel) {
            boolean wavelengthFound = false;
            boolean powerFound = false;
            boolean colorFound = false;
            for (Component innerPanelComponent : ((JPanel) mainPanelComponent).getComponents()) {
               if (innerPanelComponent instanceof JSlider)
                  spdSliderList.add((JSlider) innerPanelComponent);
               if (innerPanelComponent instanceof JLabel) {
                  if (!wavelengthFound) {
                     wavelengthList.add((JLabel) innerPanelComponent);
                     wavelengthFound = true;
                  }
                  else if (!powerFound) {
                     powerList.add((JLabel) innerPanelComponent);
                     powerFound = true;
                  }
                  else {
                     colorList.add((JLabel) innerPanelComponent);
                     colorFound = true;
                  }
               }
            }
         }
      }

      _spdSliders = spdSliderList.toArray(new JSlider[spdSliderList.size()]);
      _spdWavelengthLabels = wavelengthList.toArray(new JLabel[spdSliderList.size()]);
      _spdPowerLabels = powerList.toArray(new JLabel[spdSliderList.size()]);
      _spdColorLabels = colorList.toArray(new JLabel[spdSliderList.size()]);
   }

   private void loadSPD(File spdFile) {
      SPDFileImporter importer = new SPDFileImporter(spdFile);
      SpectralPowerDistribution spd = importer.Process();
      _spd = spd;

      powerTextField.setText(String.valueOf(spd.Power));

      setSliders(spd);

   }

   private void setSliders(SpectralPowerDistribution spd) {
      float max = spd.max();
      _spdSliderAdjustmentFactor = _spdAdjustmentResultPosition / max;

      System.out.println("slider adjustment factor = [" + _spdSliderAdjustmentFactor + "]");

      _enableSliderListeners = false;

      for (int i = 0; i < _spdSliders.length; i++) {
         int newValue = (int)(spd.Buckets[i] * _spdSliderAdjustmentFactor);

         _spdSliders[i].setValue(newValue);

         DecimalFormat df = new DecimalFormat("##");

         String value = df.format(spd.Buckets[i]);

         if (value.length() < 2) {
            value = new DecimalFormat("#.#").format(spd.Buckets[i]);
            if (value.length() < 3) {
               value = new DecimalFormat(".##").format(spd.Buckets[i]);
            }
         }


         _spdPowerLabels[i].setText(value);
      }

      _enableSliderListeners = true;



      Color c = SpectralBlender.ConvertSPDtoRGB(spd);

      colorLabel.setBackground(c);


   }


   private void createUIComponents() {
      // TODO: place custom component creation code here
   }
}
