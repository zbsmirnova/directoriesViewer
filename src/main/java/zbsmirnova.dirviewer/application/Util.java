package zbsmirnova.dirviewer.application;

public class Util {

  static FileType getFileType(String fileName){
    if(fileName.endsWith(".txt")) return FileType.TEXT;
    else if(fileName.endsWith(".jpg") | fileName.endsWith(".png") | fileName.endsWith(".gif"))
      return FileType.PICTURE;
    else return FileType.UNKNOWN;
  }

}
