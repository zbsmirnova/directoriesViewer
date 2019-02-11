package zbsmirnova.dirviewer.application;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.SwingWorker;

//takes File, returns byte[]

public class AsynchronousLoader extends SwingWorker<byte[], Void> {
  private final File file;
  private final byte[] byteArray;

  public AsynchronousLoader(File file) {
    this.file = file;
    //handle situation when file.length > Integer.MAX_VALUE
    this.byteArray = new byte[(int)file.length()];
  }

  @Override
  protected byte[] doInBackground() {
    try(InputStream is = new FileInputStream(file)) {
      is.read(byteArray);//правильно ли?
    }
    catch (IOException e){
      return new byte[0]; //empty array
    }
    return byteArray;
  }

//  @Override
//  protected void process(List<byte[]> chunks) {
//    for (byte[] b : chunks) {
//      data.addAll(b);
//    }
//  }
}
