package jtreetest;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

public class Main {

    public static void main(String[] args) {
        JFrame frame = new JFrame("JTree Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
        TreeModel treeModel = new DefaultTreeModel(rootNode);
        JTree tree = new JTree(treeModel);
        tree.setAutoscrolls(true);
        tree.setShowsRootHandles(true);
        JScrollPane treePane = new JScrollPane(tree);
        treePane.setMinimumSize(new Dimension(100, 100));

        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.anchor = GridBagConstraints.CENTER;
        gc.fill = GridBagConstraints.BOTH;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        frame.getContentPane().add(treePane, gc);
        
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        // Populate tree.
        rootNode.setUserObject("ROOT");
        DefaultMutableTreeNode dataNode = new DefaultMutableTreeNode("data");
        rootNode.add(dataNode);
        DefaultMutableTreeNode fooNode = new DefaultMutableTreeNode("foo");
        dataNode.add(fooNode);
        DefaultMutableTreeNode docNode = new DefaultMutableTreeNode("Foo-0001.xml");
        fooNode.add(docNode);
        docNode = new DefaultMutableTreeNode("Foo-0002.xml");
        fooNode.add(docNode);
        DefaultMutableTreeNode modulesNode = new DefaultMutableTreeNode("modules");
        rootNode.add(modulesNode);
        tree.updateUI();
    }

}
