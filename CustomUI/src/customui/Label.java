package customui;


public class Label extends AbstractComponent {
	
	
	private String text;
	
	
	public Label(Container parent) {
		super(parent);
	}
	
	
	public String getText() {
		return text;
	}
	
	
	public void setText(String text) {
		this.text = text;
	}


}
