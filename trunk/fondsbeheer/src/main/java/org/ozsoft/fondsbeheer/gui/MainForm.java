package org.ozsoft.fondsbeheer.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

public class MainForm {
	
	private final JFrame frame;
	
	public MainForm() {
		frame = new JFrame("Fondsbeheer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode();
        TreeModel treeModel = new DefaultTreeModel(rootNode);
        JTree tree = new JTree(treeModel);
        tree.setAutoscrolls(true);
        tree.setShowsRootHandles(true);
        JScrollPane treePanel = new JScrollPane(tree);
        treePanel.setPreferredSize(new Dimension(200, 200));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.VERTICAL;
		gbc.weightx = 0.0;
		gbc.weighty = 1.0;
		gbc.insets = new Insets(10, 10, 10, 5);
		frame.getContentPane().add(treePanel, gbc);
		
		DefaultListModel listModel = new DefaultListModel();
		JList list = new JList(listModel);
		JScrollPane listPanel = new JScrollPane(list);
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.insets = new Insets(10, 5, 10, 10);
		frame.getContentPane().add(listPanel, gbc);
		
		frame.setSize(800, 600);
		frame.setLocationRelativeTo(null);
//		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
	}

}
