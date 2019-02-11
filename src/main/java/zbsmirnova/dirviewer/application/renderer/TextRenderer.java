package zbsmirnova.dirviewer.application.renderer;

import static zbsmirnova.dirviewer.application.util.Util.getListModel;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import zbsmirnova.dirviewer.application.util.ErrorComponent;

public class TextRenderer implements Renderer {

  @Override
  public JComponent render(byte[] byteArray) {
    if(byteArray == null){
      return new ErrorComponent("Text rendering error");
    }
    DefaultListModel<String> model = getListModel(byteArray);
    JList<String> list = new JList<>();
    list.setModel(model);
    list.setEnabled(true);
    return new JScrollPane(list);
  }


}
