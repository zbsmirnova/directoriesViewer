package zbsmirnova.dirviewer.application;

public class Util {

  static FileType getFileType(String fileName){
    if(fileName.endsWith(".txt") | fileName.endsWith(".TXT")) return FileType.TEXT;
    else if(fileName.endsWith(".jpg") | fileName.endsWith(".png"))
      return FileType.PICTURE;
    else return FileType.UNKNOWN;
  }
}
