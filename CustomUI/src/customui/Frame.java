package customui;


import javax.swing.JFrame;


public class Frame extends JFrame {
	
	
	private static final long serialVersionUID = 1L;
	
	private Container container;
	

	public Frame(String title) {
		super(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		container = new Container(null);

		setSize(800, 600);
		setLocationRelativeTo(null);
		setVisible(true);
	}


}
