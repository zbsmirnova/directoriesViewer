package zbsmirnova.dirviewer.application;

import static zbsmirnova.dirviewer.application.util.Util.getRenderer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
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
  private JScrollPane treeScroll;

  private JPanel getGui() {
    if (gui == null) {

      TreeLoader treeLoader = new TreeLoader(this);

      gui = new JPanel(new BorderLayout(3, 3));
      gui.setBorder(new EmptyBorder(5, 5, 5, 5));

      treeScroll = new JScrollPane(treeLoader.getTree());
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
      //filePanel.requestFocusInWindow();
      gui.add(treeScroll, BorderLayout.WEST);
      gui.add(filePanel, BorderLayout.CENTER);
    }

    return gui;
  }

  void previewFile(File file) {
    Renderer renderer = getRenderer(file.getName());
    filePanel.remove(fileView);
    try {
      if (file.length() > Integer.MAX_VALUE)
        throw new TooLargeFileException();
      byte[] byteArray = new byte[(int) file.length()];

      SwingWorker<byte[], Void> loader = new SwingWorker<byte[], Void>() {
        @Override
        protected byte[] doInBackground() throws Exception {
          progressBar.setVisible(true);
          progressBar.setIndeterminate(true);
          InputStream is = new FileInputStream(file);
          int n = is.read(byteArray);
          return byteArray;
        }

        @Override
        protected void done() {
          progressBar.setIndeterminate(false);
          progressBar.setVisible(false);
//          if (file)
          if (!isCancelled()) {
            try {
              filePanel.remove(fileView);
              fileView = renderer.render(get());
              filePanel.add(fileView, BorderLayout.CENTER);
              treeScroll.requestFocusInWindow();
              gui.updateUI();
            } catch (ExecutionException e) {
              if (e.getCause() instanceof FileNotFoundException) {
                fileView = new ErrorComponent("file not found " + file.getName());
              } else {
                fileView = new ErrorComponent("Unexpected problem loading " + file.getName());
              }
              e.printStackTrace();
            } catch (InterruptedException e) {

              e.printStackTrace();
            } catch (IOException e) {
              fileView = new ErrorComponent("Error rendering file " + file.getName());
              e.printStackTrace();
            } finally {
              filePanel.add(fileView, BorderLayout.CENTER);
              gui.updateUI();
            }
          }
        }
      };
      loader.execute();
    } catch (TooLargeFileException e) {
      e.printStackTrace();
      fileView = new ErrorComponent("File size limit is exceeded, " + file.getName() +
          " is over 2 gb");
    }
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