package timetracker;

/**
 * Type of Element. A Line doesn't contain any specific information, 
 * but is used to display a line in a Report.
 */
public class Line extends Element {

	@Override
	public final Object getData() {
		return "------------------------------" 
				+ "---------------------------------"
				+ "---------------------------------------";
	}

}
