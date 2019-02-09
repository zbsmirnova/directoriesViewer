package zbsmirnova.dirviewer.application;

import java.io.File;
import java.util.List;
import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

class TreeLoader {

  private final Application application;
  private FileSystemView fileSystemView;
  private JTree tree;

  TreeLoader(Application application){
    this.application = application;
  }

  JTree getTree() {
    fileSystemView = FileSystemView.getFileSystemView();

    DefaultMutableTreeNode root = new DefaultMutableTreeNode();
    DefaultTreeModel treeModel = new DefaultTreeModel(root);

    TreeSelectionListener treeSelectionListener = tse -> {
      DefaultMutableTreeNode node =
          (DefaultMutableTreeNode) tse.getPath().getLastPathComponent();
      File file = (File) node.getUserObject();
      if (file.isDirectory())
        showChildren(node);
      else
        application.previewFile(file);
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
    return tree;
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
}
