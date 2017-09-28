/**
 * 
 */
package projectManagement;

import java.util.Collection;

/** 
 * @author marcm
 */
public class Task extends Component {

	/**
	 * @uml.property  name="intervals"
	 * @uml.associationEnd  multiplicity="(0 -1)" aggregation="shared" inverse="task:projectManagement.Interval"
	 */
	private Collection intervals;

	/**
	 * Getter of the property <tt>intervals</tt>
	 * @return  Returns the intervals.
	 * @uml.property  name="intervals"
	 */
	public Collection getIntervals() {
		return intervals;
	}

	/**
	 * Setter of the property <tt>intervals</tt>
	 * @param intervals  The intervals to set.
	 * @uml.property  name="intervals"
	 */
	public void setIntervals(Collection intervals) {
		this.intervals = intervals;
	}

}
