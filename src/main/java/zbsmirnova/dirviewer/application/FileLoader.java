package zbsmirnova.dirviewer.application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import zbsmirnova.dirviewer.application.util.TooLargeFileException;

public class FileLoader extends SwingWorker<byte[], Integer> {
  private final File file;
  private final byte[] byteArray;
  private JProgressBar progressBar;


  FileLoader(File file, JProgressBar progressBar, byte[] byteArray) {
    this.file = file;
    this.progressBar = progressBar;
    this.byteArray = byteArray;
  }

  @Override
  protected byte[] doInBackground() throws IOException {
    progressBar.setVisible(true);
    progressBar.setIndeterminate(true);
    try(InputStream is = new FileInputStream(file)) {
      //if(true) throw new FileNotFoundException();
      int n = is.read(byteArray);
    }
//    catch(FileNotFoundException e){
//      e.printStackTrace();
//      return null;
//    }
    return byteArray;
  }

  @Override
  protected void done() {
    progressBar.setIndeterminate(false);
    progressBar.setVisible(false);
  }

}
