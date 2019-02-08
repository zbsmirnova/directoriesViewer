package zbsmirnova.dirviewer.application;

import static zbsmirnova.dirviewer.application.Util.getFileType;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class Application extends JPanel {
  private static final int APPLICATION_WIDTH = 1070;
  static final int APPLICATION_HEIGHT = 600;
  static final int FILE_VIEW_WIDTH = 700;

  private JTree tree;
  private JComponent fileView;
  private FileSystemView fileSystemView;
  private JPanel filePanel;
  private JPanel gui;

  private Container getGui(){
    if (gui==null) {
      gui = new JPanel(new BorderLayout(3, 3));
      gui.setBorder(new EmptyBorder(5, 5, 5, 5));
      //gui.setLayout(new BorderLayout());

      fileSystemView = FileSystemView.getFileSystemView();

      DefaultMutableTreeNode root = new DefaultMutableTreeNode();
      DefaultTreeModel treeModel = new DefaultTreeModel(root);

      TreeSelectionListener treeSelectionListener = tse -> {
        DefaultMutableTreeNode node =
            (DefaultMutableTreeNode) tse.getPath().getLastPathComponent();
        File file = (File)node.getUserObject();
        if(file.isDirectory()) showChildren(node);
        else previewFile(file);
      };

      File[] roots = fileSystemView.getRoots();
      for (File fileSystemRoot : roots) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(fileSystemRoot);
        root.add(node);
        File[] files = fileSystemView.getFiles(fileSystemRoot, true);
        for (File file : files) {
          if (file.isDirectory() | file.isFile()) {
            node.add(new DefaultMutableTreeNode(file));
          }
        }
      }

      tree = new JTree(treeModel);
      tree.setRootVisible(true);
      tree.addTreeSelectionListener(treeSelectionListener);
      tree.setCellRenderer(new FileTreeCellRenderer());
      tree.expandRow(0);
      tree.setVisibleRowCount(15);

      JScrollPane treeScroll = new JScrollPane(tree);
      Dimension treePreferredSize = treeScroll.getPreferredSize();
      Dimension treePreferredWide = new Dimension(
          350,
          (int) treePreferredSize.getHeight());
      treeScroll.setPreferredSize(treePreferredWide);


      fileView = new JLabel();
      //filePanel =  new JPanel(new BorderLayout(3, 3));
      filePanel =  new JPanel(new BorderLayout(3, 3));
      filePanel.setBorder(new EmptyBorder(0,11,0,3));
      filePanel.add(fileView);

//      Dimension filePreferredSize = filePanel.getPreferredSize();
//      Dimension filePreferredWide = new Dimension(
//          700,
//          (int) filePreferredSize.getHeight());
//      filePanel.setPreferredSize(filePreferredWide);

      //Dimension fileMinSize = filePanel.getMinimumSize();
      Dimension fileMinWide = new Dimension(
          FILE_VIEW_WIDTH,
          APPLICATION_HEIGHT);
      filePanel.setMinimumSize(fileMinWide);
      filePanel.setLayout(new BorderLayout());


      gui.add(treeScroll, BorderLayout.WEST);
      gui.add(filePanel, BorderLayout.CENTER);

    }
    return gui;
  }

  private void showChildren(final DefaultMutableTreeNode node) {
    tree.setEnabled(false);

    SwingWorker<Void, File> worker = new SwingWorker<Void, File>() {

      @Override
      public Void doInBackground() {
        File file = (File) node.getUserObject();
        if (file.isDirectory()) {
          File[] files = fileSystemView.getFiles(file, false);
          if (node.isLeaf()) {
            for (File child : files) {
              publish(child);
            }
          }
        }
        return null;
      }

      @Override
      protected void process(List<File> chunks) {
        for (File file : chunks) {
          node.add(new DefaultMutableTreeNode(file));
        }
      }

      @Override
      protected void done() {
        tree.setEnabled(true);
      }
    };
    worker.execute();
  }

  private void previewFile(File file){
    if (getFileType(file.getName()) == FileType.PICTURE){
      //fileView.remove(0);
      ImageLoader loader = new ImageLoader(file);
      //loader.repaint();
      JCheckBox resizeImageCheckbox = new JCheckBox("resize the picture");
        resizeImageCheckbox.addActionListener(event -> {
          JCheckBox cb = (JCheckBox) event.getSource();
          loader.isScaled = cb.isSelected();
          loader.repaint(); });
      filePanel.add(resizeImageCheckbox, BorderLayout.SOUTH);
      fileView = loader;
    }
    else if (getFileType(file.getName()) == FileType.TEXT){
      TextLoader loader = new TextLoader(file);
      fileView = loader.display();
    }
    filePanel.remove(0);
    filePanel.add(fileView);
    gui.updateUI();
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