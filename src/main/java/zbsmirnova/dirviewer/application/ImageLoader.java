package zbsmirnova.dirviewer.application;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class ImageLoader extends JComponent {

  private BufferedImage img;

  ImageLoader(File file) {
    try {
      img = ImageIO.read(file);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void paint(Graphics g){
    int w = getWidth();
    int h = getHeight();
    int imageWidth = img.getWidth();
    int imageHeight = img.getHeight();
    double scale = getScale(w, h, imageWidth, imageHeight);
    int scaledWidth = (int)(imageWidth * scale);
    int scaledHeight = (int)(imageHeight * scale);
    double x = (w - scale * imageWidth) / 2;
    double y = (h - scale * imageHeight) / 2;
    g.drawImage(img.getScaledInstance(scaledWidth, scaledHeight, 2), (int)x, (int)y, null);
  }

  private double getScale(int w, int h, int imgWidth, int imgHeight){
    double wScale = (double) w/imgWidth;
    double hScale = (double) h/imgHeight;
    return Double.min(wScale, hScale);
  }
}

