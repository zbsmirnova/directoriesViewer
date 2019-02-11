package zbsmirnova.dirviewer.application;

import static zbsmirnova.dirviewer.application.Util.getFileType;
import static zbsmirnova.dirviewer.application.Util.getListModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import javax.swing.DefaultListModel;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class UtilTest {

  private static final String TEXT_FILE = "textFile.txt";
  private static final String JPG_FILE = "pictureFile.jpg";
  private static final String PNG_FILE = "pictureFile.png";
  private static final String PDF_FILE = "unknownFile.pdf";

  private static DefaultListModel<String> model;
  private static byte[] byteArray;

  @BeforeClass
  public static void init(){
    model = new DefaultListModel<>();
    File file = new File("C:\\Users\\813705\\IdeaProjects\\directoriesViewer\\src\\test\\java\\zbsmirnova\\dirviewer\\application\\text.txt");
    //File file = new File(".", "text.txt");
    byteArray = new byte[(int)file.length()];
    try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),
        StandardCharsets.UTF_8))) {
      String s;
      int destPos = 0;
      while ((s = br.readLine()) != null) {
        model.addElement(s + System.lineSeparator());
      }
    }
    catch (IOException e){
      e.printStackTrace();
    }
    try(InputStream is = new FileInputStream(file)) {
      is.read(byteArray);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  @Test
  public void getFileTypeTest(){
    Assert.assertEquals(getFileType(TEXT_FILE),FileType.TEXT);
    Assert.assertEquals(getFileType(JPG_FILE),FileType.PICTURE);
    Assert.assertEquals(getFileType(PNG_FILE),FileType.PICTURE);
    Assert.assertEquals(getFileType(PDF_FILE),FileType.UNKNOWN);
  }

  @Test
  public void getListModelTest() {
    DefaultListModel<String> expected = getListModel(byteArray);
    Assert.assertEquals(expected.getSize(), model.getSize());
    for (int i = 0; i < model.getSize() - 1; i++) {
      Assert.assertEquals(expected.get(i), model.get(i));
    }
    Assert.assertTrue(model.getElementAt(model.getSize()-1)
        .startsWith(expected.getElementAt(expected.getSize() - 1)));
}
}