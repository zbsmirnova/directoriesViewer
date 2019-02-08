package zbsmirnova.dirviewer.application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.SwingWorker;


class TextLoader {
  private final File file;

  TextLoader(File file){
    this.file = file;
  }

  private static class TextWorker extends SwingWorker<ListModel, String> {
    private final File file;
    private final DefaultListModel<String> model;

    private TextWorker(File file, DefaultListModel<String> model) {
      this.file = file;
      this.model = model;
      //model.setColumnIdentifiers(new Object[]{file.getAbsolutePath()});
    }

    @Override
    protected ListModel<String> doInBackground() throws Exception {
      try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),
          StandardCharsets.UTF_8))) {
        String s;
        while ((s = br.readLine()) != null) {
          publish(s);
        }
      }
      return model;
    }

    @Override
    protected void process(List<String> chunks) {
      for (String s : chunks) {
//        if(s.length() < FILE_VIEW_WIDTH){
          model.add(0, s);
//        }
//        else{
//          String s1 = s.substring(0, FILE_VIEW_WIDTH);
//          String s2 = s.substring(FILE_VIEW_WIDTH);
//          model.addRow(new Object[]{s1});
//          model.addRow(new Object[]{s2});
//        }
      }
    }
  }

  JComponent display() {
    DefaultListModel<String> model = new DefaultListModel<>();
    JList<String> list = new JList<>(model);
    list.setModel(model);
    list.setEnabled(true);
    //list.setShowGrid(false);
    TextWorker worker = new TextWorker(file, model);
    worker.execute();
    return list;
  }
}