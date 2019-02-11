package zbsmirnova.dirviewer.application.renderer;

import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.JLabel;
import zbsmirnova.dirviewer.application.ImageComponent;


public class ImageRenderer implements Renderer {

  @Override
  public JComponent render(byte [] byteArray) {
    try {
      return new ImageComponent(byteArray);
    } catch (IOException | NullPointerException e) {
      e.printStackTrace();
      return new JLabel("Image rendering error");
    }
  }
}
