package zbsmirnova.dirviewer.application;

import static zbsmirnova.dirviewer.application.util.Util.getRenderer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import zbsmirnova.dirviewer.application.renderer.Renderer;
import zbsmirnova.dirviewer.application.util.ErrorComponent;
import zbsmirnova.dirviewer.application.util.TooLargeFileException;

/**
 * Application allows to walk through system directories and files (lazy loading).
 * Application supports file preview for text (files with extension .txt, .iml, .java, .xml,
 * .log) and images (files with extension .jpg and .png).
 * Supported text encoding - UTF8.
 * File size should not be larger then 2 gb.
 * Image and text loading executes out of Event Dispatch Thread.
 */

public class Application{

  private static final int APPLICATION_WIDTH = 1070;
  private static final int APPLICATION_HEIGHT = 600;
  private static final int FILE_VIEW_WIDTH = 700;
  private static final int TREE_WIDTH = 350;

  private JComponent fileView;
  private JPanel filePanel;
  private  JPanel gui;
  private JProgressBar progressBar;

  private JPanel getGui() {
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

      progressBar = new JProgressBar();
      progressBar.setVisible(false);
      progressBar.setMinimum(0);
      progressBar.setMaximum(100);
      filePanel.add(progressBar, BorderLayout.SOUTH);
      progressBar.setVisible(false);

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
    Renderer renderer = getRenderer(file.getName());
    filePanel.remove(fileView);
    try{
      FileLoader loader = new FileLoader(file, progressBar);
      loader.execute();
      byte[] byteArray;
      byteArray = loader.doInBackground();
      fileView = renderer.render(byteArray);
    }
    catch (TooLargeFileException e){
      e.printStackTrace();
      fileView = new ErrorComponent("File size limit is exceeded, " + file.getName() +
          " is over 2 gb");
    }
    catch (IOException e) {
      e.printStackTrace();
      fileView = new ErrorComponent("Error loading file " + file.getName());
    }
    filePanel.add(fileView, BorderLayout.CENTER);
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