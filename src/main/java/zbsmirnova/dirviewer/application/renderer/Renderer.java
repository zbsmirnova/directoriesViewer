package zbsmirnova.dirviewer.application.renderer;

import java.io.IOException;
import javax.swing.JComponent;

/**
 * Renderer interface provides a single method for creating JComponent object of an array of bytes.
 */

public interface Renderer {

  JComponent render(byte[] byteArray) throws IOException;

}
