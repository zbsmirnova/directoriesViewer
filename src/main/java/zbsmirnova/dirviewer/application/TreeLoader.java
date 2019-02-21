package zbsmirnova.dirviewer.application;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import zbsmirnova.dirviewer.application.renderer.FileTreeCellRenderer;


class TreeLoader {

  private final Application application;
  private FileSystemView fileSystemView;
  private JTree tree;
  private File selectedFile;


  TreeLoader(Application application){
    this.application = application;
  }

  File getSelectedFile() {
    return selectedFile;
  }

  JTree getTree() {
    fileSystemView = FileSystemView.getFileSystemView();

    DefaultMutableTreeNode root = new DefaultMutableTreeNode();
    DefaultTreeModel treeModel = new DefaultTreeModel(root);

    TreeSelectionListener treeSelectionListener = tse -> {
      DefaultMutableTreeNode node =
          (DefaultMutableTreeNode) tse.getPath().getLastPathComponent();
      selectedFile = (File) node.getUserObject();
      if (selectedFile != null && selectedFile.isDirectory())
        showChildren(node);
      else if(selectedFile != null){
        if(application.getLoader() != null && !application.getLoader().isDone()){
          application.getLoader().cancel(true);}
        application.previewFile(selectedFile);
        }
    };


    File[] roots = fileSystemView.getRoots();
    for (File fileSystemRoot : roots) {
      DefaultMutableTreeNode node = new DefaultMutableTreeNode(fileSystemRoot);
      root.add(node);
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
          Arrays.sort(files, Comparator.comparing(File::getName));
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
        chunks.sort(Comparator.comparing(File::getName));
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
