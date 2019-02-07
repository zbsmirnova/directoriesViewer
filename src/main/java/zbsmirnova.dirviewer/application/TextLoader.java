package zbsmirnova.dirviewer.application;

import static zbsmirnova.dirviewer.application.Application.APPLICATION_HEIGHT;
import static zbsmirnova.dirviewer.application.Application.FILE_VIEW_WIDTH;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;


class TextLoader {
  private final File file;

  TextLoader(File file){
    this.file = file;
  }

  private static class TextWorker extends SwingWorker<List<String>, String> {
    private final File file;
    private JTextArea textArea;
    //private final DefaultTableModel model;

    private TextWorker(File file, JTextArea textArea) {
        this.file = file;
        this.textArea = textArea;
//        this.model = model;
//        model.setColumnIdentifiers(new Object[]{file.getAbsolutePath()});
    }



    @Override
    protected List<String> doInBackground() throws Exception {
      List<String> contents = new ArrayList<>(256);
      try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),
          StandardCharsets.UTF_8))) {
        String s;
        while ((s = br.readLine()) != null) {
          publish(s);
          contents.add(s);
        }
      }
      return contents;
    }

    @Override
    protected void process(List<String> chunks) {
      for (String text : chunks) {
        textArea.append(text);
      }
    }
  }

  JComponent display() {
    JTextArea textArea = new JTextArea();
    textArea.setPreferredSize(new Dimension(FILE_VIEW_WIDTH-10, APPLICATION_HEIGHT-10));
    textArea.setLineWrap(true);
    TextWorker worker = new TextWorker(file, textArea);
    worker.execute();
    JScrollPane textAreaScroll =  new JScrollPane(textArea);
    return textAreaScroll;
  }
}