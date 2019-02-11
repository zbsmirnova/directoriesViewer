package zbsmirnova.dirviewer.application.renderer;

import static zbsmirnova.dirviewer.application.Util.getListModel;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;

public class TextRenderer implements Renderer {

  @Override
  public JComponent render(byte[] byteArray) {
    if(byteArray == null){
      return new JLabel("Text rendering error");
    }
    DefaultListModel<String> model = getListModel(byteArray);
    JList<String> list = new JList<>();
    list.setModel(model);
    list.setEnabled(true);
    return new JScrollPane(list);
  }


}
