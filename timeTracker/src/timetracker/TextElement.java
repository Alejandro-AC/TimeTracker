package timetracker;

public class TextElement extends Element {	
	
	/**
	 * @uml.property  name="text"
	 */
	private String text;
	
	@Override
	public final Object getData() {
		return this.text;
	}
	
	public TextElement(final String textSet) {
		this.text = textSet;
		
	}

	

	

}
