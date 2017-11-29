package timetracker;

/**
 * Type of Element. It contains a String with a simple line of text
 * that will be displayed in a Report.
 */
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
