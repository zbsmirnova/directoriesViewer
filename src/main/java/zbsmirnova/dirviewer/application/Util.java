package zbsmirnova.dirviewer.application;

import javax.swing.DefaultListModel;
import zbsmirnova.dirviewer.application.renderer.ImageRenderer;
import zbsmirnova.dirviewer.application.renderer.Renderer;
import zbsmirnova.dirviewer.application.renderer.TextRenderer;
import zbsmirnova.dirviewer.application.renderer.UnknownFileRenderer;

public class Util {

  static FileType getFileType(String fileName){
    if(fileName.endsWith(".txt") | fileName.endsWith(".TXT")) return FileType.TEXT;
    else if(fileName.endsWith(".jpg") | fileName.endsWith(".png"))
      return FileType.PICTURE;
    else return FileType.UNKNOWN;
  }

  static Renderer getRenderer(String fileName){
    Renderer renderer;
    switch(getFileType(fileName)){
      case TEXT:
        renderer = new TextRenderer();
        break;
      case PICTURE:
        renderer = new ImageRenderer();
        break;
      default:
        renderer = new UnknownFileRenderer();
    }
    return renderer;
  }

  private static void addLine(byte[] byteArray, DefaultListModel<String> model, int start, int end) {
    byte[] nextStringBytes = new byte[end - start];
    System.arraycopy(byteArray, start, nextStringBytes, 0, end - start);
    model.addElement(new String(nextStringBytes));
  }

  public static DefaultListModel<String> getListModel(byte[] byteArray){
    DefaultListModel<String> model = new DefaultListModel<>();
    String lineSeparator = System.lineSeparator();
    char[] separatorChars = lineSeparator.toCharArray();
    int start = 0;
    int end = 0;

    while(end <= byteArray.length - 1) {
      char current = (char) byteArray[end];
      if (current == separatorChars[0] & byteArray.length - end > 0) {
        byte[] currentArray = new byte[separatorChars.length];
        System.arraycopy(byteArray, end, currentArray, 0, separatorChars.length);
        if (new String(currentArray).equals(lineSeparator)) {
          end += separatorChars.length;
          addLine(byteArray, model, start, end);
          start = end;
        }
      }
      else if(byteArray.length - end - 1 == 0){
        end++;
        addLine(byteArray, model, start, end);
      }
      else {
        end++;
      }
    }
    return model;
  }
}