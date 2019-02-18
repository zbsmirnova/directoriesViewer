package zbsmirnova.dirviewer.application.util;

import com.sun.istack.internal.NotNull;
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
  private static Set<String> imageFileExtensions;

  static {
    textFileExtensions = new HashSet<>();
    String[] textExtensions = {"txt", "iml", "java", "xml", "log"};
    textFileExtensions.addAll(Arrays.asList(textExtensions));

    imageFileExtensions = new HashSet<>();
    String[] imageExtensions = {"png", "jpg"};
    imageFileExtensions.addAll(Arrays.asList(imageExtensions));

  }

  public static FileType getFileType(@NotNull String fileName){
    String fileExtension = "";
    String[] splited = fileName.split("\\.");
    if(splited.length > 0)
      fileExtension = splited[splited.length - 1];

    if(textFileExtensions.contains(fileExtension))
      return FileType.TEXT;
    else if(imageFileExtensions.contains(fileExtension))
      return FileType.PICTURE;
    else return FileType.UNKNOWN;
  }

  public static Renderer getRenderer(@NotNull String fileName){
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

  private static void addLine(@NotNull byte[] byteArray, @NotNull DefaultListModel<String> model,
      int start, int end) {
    if(end <= start) return;
    byte[] nextStringBytes = new byte[end - start];
    System.arraycopy(byteArray, start, nextStringBytes, 0, end - start);
    model.addElement(new String(nextStringBytes));
  }

  public static DefaultListModel<String> getListModel(@NotNull byte[] byteArray){
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