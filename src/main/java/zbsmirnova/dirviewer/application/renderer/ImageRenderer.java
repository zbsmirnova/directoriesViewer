package zbsmirnova.dirviewer.application.renderer;

import java.io.IOException;
import javax.swing.JComponent;
import zbsmirnova.dirviewer.application.ImageComponent;
import zbsmirnova.dirviewer.application.util.ErrorComponent;


public class ImageRenderer implements Renderer {

  @Override
  public JComponent render(byte [] byteArray) throws IOException{
    if(byteArray == null) return new ErrorComponent("Image rendering error");;
      return new ImageComponent(byteArray);
//    } catch (IOException | NullPointerException e) {
//      e.printStackTrace();
//      return new JLabel("Image rendering error");
//    }
  }
}
