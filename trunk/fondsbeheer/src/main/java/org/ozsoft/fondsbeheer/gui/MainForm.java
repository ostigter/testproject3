package org.ozsoft.fondsbeheer.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import org.ozsoft.fondsbeheer.services.FundService;
import org.ozsoft.fondsbeheer.services.FundServiceImpl;

public class MainForm {
	
	private final FundService fundService;
	
	private JFrame frame;
	
	public MainForm() {
		fundService = new FundServiceImpl();
		fundService.start();
		createUI();
	}
	
	private void createUI() {
		frame = new JFrame("Fondsbeheer");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});
		frame.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		// Left panel with tree with categories and funds.
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
		
		// Right panel with selected fund.
		DefaultListModel listModel = new DefaultListModel();
		JList list = new JList(listModel);
		JScrollPane fundPanel = new JScrollPane(list);
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		gbc.insets = new Insets(10, 5, 10, 10);
		frame.getContentPane().add(fundPanel, gbc);
		
		frame.setSize(800, 600);
		frame.setLocationRelativeTo(null);
//		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
	}
	
	private void exit() {
		fundService.stop();
	}

}
