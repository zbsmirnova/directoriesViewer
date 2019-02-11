package zbsmirnova.dirviewer.application.renderer;

import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;

public class UnknownFileRenderer implements Renderer {

  @Override
  public JComponent render(byte[] byteArray) {
    JLabel label = new JLabel();
    label.setLayout(new BorderLayout());
    label.setText("File format not supported.");
    return label;
  }
}
