package zbsmirnova.dirviewer.application.renderer;

import javax.swing.JComponent;
import zbsmirnova.dirviewer.application.util.ErrorComponent;

public class UnknownFileRenderer implements Renderer {

  @Override
  public JComponent render(byte[] byteArray) {
    return new ErrorComponent("File format not supported.");
  }
}
