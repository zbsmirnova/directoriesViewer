package zbsmirnova.dirviewer.application;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

public class FileLoader extends SwingWorker<byte[], Void> {
  private final File file;
  private byte[] byteArray;
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
//      for(byte b : byteArray) {
//        publish(b);
//      }
    }
//    catch(FileNotFoundException e){
//      e.printStackTrace();
//      return null;
//    }
    return byteArray;
    //return null;
  }

//  @Override
//  protected void process(List<Byte> chunks) {
//   // byteArray = chunks.get(0);
//    for (int i = 0; i < chunks.size(); i++) {
//      byteArray[i] = chunks.get(i);
//    }
//

  @Override
  protected void done() {
    progressBar.setIndeterminate(false);
    progressBar.setVisible(false);

  }

}
