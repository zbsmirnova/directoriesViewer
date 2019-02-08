package zbsmirnova.dirviewer.application;

import static zbsmirnova.dirviewer.application.Util.getFileType;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class UtilTest {
  private static final String TEXT_FILE = "textFile.txt";
  private static final String JPG_FILE = "pictureFile.jpg";
  private static final String PNG_FILE = "pictureFile.png";
  private static final String PDF_FILE = "unknownFile.pdf";

  @Test
  public void getFileTypeTest(){
    Assert.assertEquals(getFileType(TEXT_FILE),FileType.TEXT);
    Assert.assertEquals(getFileType(JPG_FILE),FileType.PICTURE);
    Assert.assertEquals(getFileType(PNG_FILE),FileType.PICTURE);
    Assert.assertEquals(getFileType(PDF_FILE),FileType.UNKNOWN);
  }
}