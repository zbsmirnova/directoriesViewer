package zbsmirnova.dirviewer.application;

import static zbsmirnova.dirviewer.application.util.Util.getRenderer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
  private TreeLoader treeLoader;

  private Loader loader;

  Loader getLoader() {
    return loader;
  }

  private JPanel getGui() {
    if (gui == null) {

      treeLoader = new TreeLoader(this);

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
    try {
      loader = new Loader(file, renderer);
      progressBar.setVisible(true);
      progressBar.setIndeterminate(true);
      loader.execute();
    }
    catch (TooLargeFileException e) {
      e.printStackTrace();
      fileView = new ErrorComponent("File size limit is exceeded, " + file.getName() +
          " is over 2 gb");
      filePanel.add(fileView, BorderLayout.CENTER);
      gui.updateUI();
    }
  }

  public class Loader extends SwingWorker<byte[], Void> {
    private final byte[] byteArray;
    private final File file;
    private final Renderer renderer;

    Loader(File file, Renderer renderer){
      this.file = file;
      this.renderer = renderer;

      if (file.length() > Integer.MAX_VALUE)
        throw new TooLargeFileException();
      byteArray = new byte[(int) file.length()];
    }

    @Override
    protected byte[] doInBackground() throws Exception {
      BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
      int result = bis.read();
      int i = 0;

      while(result != -1 && !isCancelled()) {
          byteArray[i] = ((byte) result);
          i++;
          result = bis.read();
      }
      bis.close();
      return byteArray;
    }

    @Override
    protected void done() {
      progressBar.setIndeterminate(false);
      progressBar.setVisible(false);
      if (file == treeLoader.getSelectedFile()) {
        try {
          filePanel.remove(fileView);
          fileView = renderer.render(get());
          filePanel.add(fileView, BorderLayout.CENTER);
          treeLoader.getTree().requestFocusInWindow();
          gui.updateUI();
        } catch (ExecutionException e) {
          if (e.getCause() instanceof FileNotFoundException) {
            fileView = new ErrorComponent("File not found " + file.getName());
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