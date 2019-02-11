package zbsmirnova.dirviewer.application;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.swing.DefaultListModel;
import zbsmirnova.dirviewer.application.renderer.ImageRenderer;
import zbsmirnova.dirviewer.application.renderer.Renderer;
import zbsmirnova.dirviewer.application.renderer.TextRenderer;
import zbsmirnova.dirviewer.application.renderer.UnknownFileRenderer;

public class Util {

  private static Set<String> textFileExtensions;

  static {
    textFileExtensions = new HashSet<>();
    String[] extensions = {"txt", "iml", "java", "xml", "TXT", "log"};
    textFileExtensions.addAll(Arrays.asList(extensions));
  }

  static FileType getFileType(String fileName){
    String fileExtension3 = fileName.substring(fileName.length() - 3);
    String fileExtension4 = fileName.substring(fileName.length() - 4);
    if(textFileExtensions.contains(fileExtension3) || textFileExtensions.contains(fileExtension4))
      return FileType.TEXT;
    else if(fileExtension3.equals("jpg") || fileExtension3.equals("png"))
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