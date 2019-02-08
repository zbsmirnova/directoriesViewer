package zbsmirnova.dirviewer.application;

import static zbsmirnova.dirviewer.application.Application.APPLICATION_HEIGHT;
import static zbsmirnova.dirviewer.application.Application.FILE_VIEW_WIDTH;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
//implements ChangeListener
public class ImageLoader extends JComponent {
  private BufferedImage img;
  boolean isScaled = false;

  public void paint(Graphics g) {
    if(isScaled){
      g.drawImage(img, 0, 0, null);
    }
    else{
      paintScaled(g);
    }
  }

  private void paintScaled(Graphics g){
    int height = img.getHeight();
    int width = img.getWidth();
    double k = 1.0;

    if(height > width && height > APPLICATION_HEIGHT){
      k = (double) height/(APPLICATION_HEIGHT-20);
    }
    else if(width > height && width > FILE_VIEW_WIDTH){
      k = (double) width/(FILE_VIEW_WIDTH-20);
    }

    height = (int) (height / k);
    width = (int)(width/k);

    g.drawImage(img.getScaledInstance(width, height,2), 0, 0, null);
  }

  ImageLoader(File file) {
    try {
      img = ImageIO.read(file);
    }
    catch (IOException e) {
      System.out.println(e.getMessage());//??????
    }
  }
}

