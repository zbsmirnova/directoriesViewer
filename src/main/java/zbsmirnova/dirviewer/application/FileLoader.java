package zbsmirnova.dirviewer.application;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import zbsmirnova.dirviewer.application.util.TooLargeFileException;

public class FileLoader extends SwingWorker<byte[], Integer> {
  private final File file;
  private final byte[] byteArray;
  private JProgressBar progressBar;


  public FileLoader(File file, JProgressBar progressBar) {
    this.file = file;
    if(file.length() > Integer.MAX_VALUE) throw new TooLargeFileException();
    this.byteArray = new byte[(int)file.length()];
    this.progressBar = progressBar;
  }

  @Override
  protected byte[] doInBackground() throws IOException {
    progressBar.setVisible(true);
    progressBar.setIndeterminate(true);
    try(InputStream is = new FileInputStream(file)) {
      int n = is.read(byteArray);
    }
    return byteArray;
  }

  @Override
  protected void done() {
    progressBar.setIndeterminate(false);
    progressBar.setVisible(false);
  }

}
