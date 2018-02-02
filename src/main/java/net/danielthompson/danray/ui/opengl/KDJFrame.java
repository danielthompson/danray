package net.danielthompson.danray.ui.opengl;

import net.danielthompson.danray.acceleration.KDNode;
import net.danielthompson.danray.acceleration.KDScene;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Created by daniel on 3/8/16.
 */
public class KDJFrame extends JFrame implements TreeSelectionListener {
   private final KDScene _scene;
   private AbstractOpenGLCanvas _canvas;

   private final JTree _tree;

   public KDJFrame(KDScene scene, AbstractOpenGLCanvas canvas) {
      super("kd-Tree Nodes");
      _scene = scene;
      _canvas = canvas;


      DefaultMutableTreeNode treeNodeRoot = CreateNodes(_scene.rootNode);
      _tree = new JTree(treeNodeRoot);
      _tree.addTreeSelectionListener(this);
      this.add(_tree);
   }

   private DefaultMutableTreeNode CreateNodes(KDNode node) {
      DefaultMutableTreeNode treeNode = null;

      if (node != null) {
         treeNode = CreateNode(node);

         if (node.LeftChild != null) {
            DefaultMutableTreeNode childNode = CreateNodes(node.LeftChild);
            if (childNode != null) {
               //childNode.setParent(treeNode);
               treeNode.add(childNode);
            }

         }
         if (node.RightChild != null) {
            DefaultMutableTreeNode childNode = CreateNodes(node.RightChild);
            if (childNode != null) {
               //childNode.setParent(treeNode);
               treeNode.add(childNode);
            }

         }
      }

      return treeNode;
   }

   private DefaultMutableTreeNode CreateNode(KDNode node)
   {
      DefaultMutableTreeNode treeNode = null;

      if (node != null) {
         treeNode = new DefaultMutableTreeNode(node, true);

      }

      return treeNode;
   }

   @Override
   public void valueChanged(TreeSelectionEvent e) {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode) _tree.getLastSelectedPathComponent();

      /* if nothing is selected */
      if (node == null) return;

      /* retrieve the node that was selected */
      KDNode nodeInfo = (KDNode)(node.getUserObject());

       /* React to the node selection. */
      _canvas.SetNode(nodeInfo);
   }
}
