package zbsmirnova.dirviewer.application;

import static zbsmirnova.dirviewer.application.Util.getFileType;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.io.File;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class Application extends JPanel {

  private static final int APPLICATION_WIDTH = 1070;
  private static final int APPLICATION_HEIGHT = 600;
  private static final int FILE_VIEW_WIDTH = 700;
  private static final int TREE_WIDTH = 350;

  private JComponent fileView;
  private JPanel filePanel;
  private JPanel gui;

  private Container getGui() {
    if (gui == null) {

      TreeLoader treeLoader = new TreeLoader(this);

      gui = new JPanel(new BorderLayout(3, 3));
      gui.setBorder(new EmptyBorder(5, 5, 5, 5));

      JScrollPane treeScroll = new JScrollPane(treeLoader.getTree());
      Dimension treePreferredSize = treeScroll.getPreferredSize();
      Dimension treePreferredWide = new Dimension(
          TREE_WIDTH,
          (int) treePreferredSize.getHeight());
      treeScroll.setPreferredSize(treePreferredWide);

      fileView = new JLabel();
      filePanel = new JPanel(new BorderLayout(3, 3));
      filePanel.setBorder(new EmptyBorder(0, 11, 0, 3));
      filePanel.add(fileView);

      Dimension fileMinWide = new Dimension(
          FILE_VIEW_WIDTH,
          APPLICATION_HEIGHT);
      filePanel.setMinimumSize(fileMinWide);

      gui.add(treeScroll, BorderLayout.WEST);
      gui.add(filePanel, BorderLayout.CENTER);
    }

    return gui;
  }

  void previewFile(File file) {
    if (getFileType(file.getName()) == FileType.PICTURE) {
      fileView = new ImageLoader(file);
    } else if (getFileType(file.getName()) == FileType.TEXT) {
      TextLoader loader = new TextLoader(file);
      fileView = loader.display();
    }
    filePanel.remove(0);
    filePanel.add(fileView);
    gui.updateUI();
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      try {
        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
      }
      catch (Exception e) {
        e.printStackTrace();
      }
      Application app = new Application();
      JFrame f = new JFrame("Application");
      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      f.setContentPane(app.getGui());
      f.pack();
      f.setLocationByPlatform(true);
      f.setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);
      f.setMinimumSize(f.getSize());
      f.setVisible(true);
    });
  }
}