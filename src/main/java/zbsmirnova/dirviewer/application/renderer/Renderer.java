package zbsmirnova.dirviewer.application.renderer;


import javax.swing.JComponent;

public interface Renderer {

  JComponent render(byte[] byteArray);

}
