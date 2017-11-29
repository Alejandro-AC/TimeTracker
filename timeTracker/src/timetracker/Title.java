package timetracker;

/**
 * Type of Element. It contains an String with a title.
 * This class is used to differentiate between a SubTitle and
 * a Title (which is bigger in size for the HTML format).
 */
public class Title extends Element {

	/**
	 * @uml.property  name="text"
	 */
	private String text;
	
	@Override
	public final Object getData() {
		return this.text;
	}

	public Title(final String textSet) {
		this.text = textSet;
		
	}

}
