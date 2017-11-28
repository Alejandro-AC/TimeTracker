package timetracker;


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
