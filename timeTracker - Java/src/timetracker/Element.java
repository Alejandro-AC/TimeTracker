package timetracker;

/**
 * Element is an abstract class used to encapsulate all the elements
 * that a Report can contain in a unique list.
 */
public abstract class Element {
	
	/**
	 * Getter of the property <tt>data</tt>
	 * @return  Returns the data.
	 * @uml.property  name="data"
	 */
	public abstract Object getData();
}
