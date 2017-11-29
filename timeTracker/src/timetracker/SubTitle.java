package timetracker;

/**
 * Type of Element. It contains an String with a title.
 * This class is used to differentiate between a Title and
 * a SubTitle (which is smaller in size for the HTML format).
 */
public class SubTitle extends Element {

	/**
	 * @uml.property  name="text"
	 */
	private String text;
	
	@Override
	public final Object getData() {
		return this.text;
	}
	
	public SubTitle(final String textSet) {
		this.text = textSet;
	}

}
