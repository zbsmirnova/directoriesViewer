package zbsmirnova.dirviewer.application.renderer;

import java.io.IOException;
import javax.swing.JComponent;
import zbsmirnova.dirviewer.application.ImageComponent;


public class ImageRenderer implements Renderer {

  @Override
  public JComponent render(byte [] byteArray) {
    try {
      return new ImageComponent(byteArray);
    } catch (IOException e) {
      e.printStackTrace();
      return new UnknownFileRenderer().render(byteArray);
    }
  }
}
