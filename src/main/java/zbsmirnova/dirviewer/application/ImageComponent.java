package zbsmirnova.dirviewer.application;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JComponent;


public class ImageComponent extends JComponent {

  private BufferedImage img;

  public ImageComponent(byte[] byteArray) throws IOException{
    ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
    img = ImageIO.read(bais);
  }

  @Override
  public void paint(Graphics g){
    int w = getWidth();
    int h = getHeight();
    if (img == null) {
      g.drawString("Error rendering image", 0, h/2);
      return;
    }
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

