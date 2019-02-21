package zbsmirnova.dirviewer.application;

import static zbsmirnova.dirviewer.application.util.Util.getFileType;
import static zbsmirnova.dirviewer.application.util.Util.getListModel;

import javax.swing.DefaultListModel;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import zbsmirnova.dirviewer.application.util.FileType;

@RunWith(JUnit4.class)
public class UtilTest {

  private static final String TXT_FILE = "textFile.txt";
  private static final String XML_FILE = "xmlFile.xml";
  private static final String JAVA_FILE = "javaFile.java";
  private static final String LOG_FILE = "logFile.log";
  private static final String IML_FILE = "imlFile.iml";
  private static final String JPG_FILE = "pictureFile.jpg";
  private static final String PNG_FILE = "pictureFile.png";
  private static final String PDF_FILE = "unknownFile.pdf";

  private static DefaultListModel<String> model;
  private static byte[] byteArray;

  @BeforeClass
  public static void init(){
    model = new DefaultListModel<>();

    model.addElement("first line" + System.lineSeparator());
    model.addElement("sdkjdshbvfh dfvjvfjnvkjvdjhb" + System.lineSeparator());
    model.addElement("sddlmklkdfkjndkbjvhff" + System.lineSeparator());
    model.addElement(System.lineSeparator());
    model.addElement("odsskdkj dkldkjdfh dskldkldksdsf lksihdjyghgfd" + System.lineSeparator());
    model.addElement("last line" + System.lineSeparator());

    int byteArraySize = 0;
    for (int i = 0; i < model.getSize(); i++) {
      byteArraySize += model.elementAt(i).getBytes().length;
    }
    byteArray = new byte[byteArraySize];

    int destPos = 0;
    for (int i = 0; i < model.getSize(); i++) {
      String line = model.elementAt(i);
      System.arraycopy(line.getBytes(), 0, byteArray, destPos, line.getBytes().length);
      destPos += line.getBytes().length;
    }
  }

  @Test
  public void getFileTypeTest(){
    Assert.assertEquals(getFileType(TXT_FILE), FileType.TEXT);
    Assert.assertEquals(getFileType(JAVA_FILE),FileType.TEXT);
    Assert.assertEquals(getFileType(XML_FILE),FileType.TEXT);
    Assert.assertEquals(getFileType(IML_FILE),FileType.TEXT);
    Assert.assertEquals(getFileType(LOG_FILE),FileType.TEXT);
    Assert.assertEquals(getFileType(JPG_FILE),FileType.PICTURE);
    Assert.assertEquals(getFileType(PNG_FILE),FileType.PICTURE);
    Assert.assertEquals(getFileType(PDF_FILE),FileType.UNKNOWN);

    Assert.assertEquals(getFileType(""),FileType.UNKNOWN);
    Assert.assertEquals(getFileType("dxfd"),FileType.UNKNOWN);
    Assert.assertEquals(getFileType("."),FileType.UNKNOWN);
    Assert.assertEquals(getFileType(".vvh.nvj...txt"),FileType.TEXT);
  }

  @Test
  public void getListModelTest() {
    DefaultListModel<String> expected = getListModel(byteArray);
    Assert.assertArrayEquals(expected.toArray(), model.toArray());
}
}