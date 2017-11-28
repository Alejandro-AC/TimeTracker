package timetracker;


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
