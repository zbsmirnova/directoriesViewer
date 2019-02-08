package zbsmirnova.dirviewer.application;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

public class Application extends JPanel {
  private static final int APPLICATION_WIDTH = 1070;
  static final int APPLICATION_HEIGHT = 600;
  static final int FILE_VIEW_WIDTH = 700;

  JComponent fileView;
  JPanel filePanel;
  JPanel gui;

  private Container getGui(){
    if (gui==null) {
      TreeLoader treeLoader = new TreeLoader(this);

      gui = new JPanel(new BorderLayout(3, 3));
      gui.setBorder(new EmptyBorder(5, 5, 5, 5));

      JScrollPane treeScroll = new JScrollPane(treeLoader.getTree());
      Dimension treePreferredSize = treeScroll.getPreferredSize();
      Dimension treePreferredWide = new Dimension(
          350,
          (int) treePreferredSize.getHeight());
      treeScroll.setPreferredSize(treePreferredWide);

      fileView = new JLabel();
      filePanel =  new JPanel(new BorderLayout(3, 3));
      filePanel.setBorder(new EmptyBorder(0,11,0,3));
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


  public static void main(String[] args) {
    Application app = new Application();
    JFrame f = new JFrame("Application");
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    f.setContentPane(app.getGui());
    f.pack();
    f.setLocationByPlatform(true);
    f.setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT);
    f.setMinimumSize(f.getSize());
    f.setVisible(true);
  }
}