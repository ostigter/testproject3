package org.ozsoft.fondsbeheer.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.ozsoft.fondsbeheer.entities.Category;
import org.ozsoft.fondsbeheer.entities.Fund;
import org.ozsoft.fondsbeheer.services.ChartService;
import org.ozsoft.fondsbeheer.services.ChartServiceImpl;
import org.ozsoft.fondsbeheer.services.FundService;
import org.ozsoft.fondsbeheer.services.FundServiceImpl;

/**
 * The application's main form.
 * 
 * @author Oscar Stigter
 */
public class MainForm {
	
	/** The Fund service. */
	private final FundService fundService;
	
	/** The frame. */
	private JFrame frame;
	
	/** The tree with the categories and funds. */
	private JTree fundTree;
	
	/** The tree's root node. */
	private DefaultMutableTreeNode fundTreeRoot;
	
	/** The panel with the selected fund. */
	private CanvasPanel fundPanel;
	
	/** The service for creating a fund's history chart. */
	private ChartService chartService;
	
	/**
	 * Constructor.
	 */
	public MainForm() {
		fundService = new FundServiceImpl();
		fundService.start();
		
		chartService = new ChartServiceImpl();
		
		createUI();
		
		updateFundTree();
	}
	
	/**
	 * Creates the UI.
	 */
	private void createUI() {
		frame = new JFrame("Fondsbeheer");
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
		frame.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		// Left panel with tree with categories and funds.
		fundTreeRoot = new DefaultMutableTreeNode("Fondsen");
        TreeModel treeModel = new DefaultTreeModel(fundTreeRoot);
        fundTree = new JTree(treeModel);
        fundTree.setAutoscrolls(true);
        fundTree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent e) {
			    TreePath path = fundTree.getSelectionPath();
			    if (path != null) {
                    int depth = fundTree.getSelectionPath().getPathCount();
    			    if (depth == 1) {
    					rootNodeSelected();
    				} else {
    					DefaultMutableTreeNode node = (DefaultMutableTreeNode) fundTree.getLastSelectedPathComponent();
    					Object obj = node.getUserObject();
    					if (depth == 2) {
    						categorySelected((Category) obj);
    					} else {
    						fundSelected((Fund) obj);
    					}
    				}
			    }
			}
        });
        JScrollPane treePanel = new JScrollPane(fundTree);
        treePanel.setPreferredSize(new Dimension(250, 200));
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
//		DefaultListModel listModel = new DefaultListModel();
//		JList list = new JList(listModel);
//		JScrollPane fundPanel = new JScrollPane(list);
		fundPanel = new CanvasPanel(); 
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
	
	/**
	 * Updates the tree with the categories and funds.
	 */
	private void updateFundTree() {
		fundTreeRoot.removeAllChildren();
		for (Category category : fundService.getCategories()) {
			DefaultMutableTreeNode categoryNode = new DefaultMutableTreeNode(category);
			for (Fund fund : category.getFunds()) {
				categoryNode.add(new DefaultMutableTreeNode(fund));
			}
			fundTreeRoot.add(categoryNode);
		}
		fundTree.expandRow(0);
	}
	
	/**
	 * Handles a selection of the tree's root node.
	 */
	private void rootNodeSelected() {
		System.out.println("Root node selected");
	}
	
	/**
	 * Handles a selection of a category.
	 */
	private void categorySelected(Category category) {
		System.out.println("Category selected: " + category);
	}
	
	/**
	 * Handles a selection of a fund.
	 */
	private void fundSelected(Fund fund) {
		System.out.println("Fund selected: " + fund);
		int width = fundPanel.getWidth();
		int height = fundPanel.getHeight();
		fundPanel.setImage(chartService.createChart(fund, null, width, height));
	}
	
	/**
	 * Closes the application.
	 */
	private void close() {
		fundService.stop();
		frame.dispose();
	}

}
