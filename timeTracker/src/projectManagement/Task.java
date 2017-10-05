/**
 * 
 */
package projectManagement;

import java.util.Collection;
import java.util.ArrayList;

/** 
 * @author marcm
 */
public class Task extends Component {

	/**
	 * @uml.property  name="intervals"
	 * @uml.associationEnd  multiplicity="(0 -1)" aggregation="shared" inverse="task:projectManagement.Interval"
	 */
	private Collection<Interval> intervals = new ArrayList<Interval>();

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

	public void calculateTotalTime() {
		// TODO Auto-generated method stub
		
	}

	/**
	 */
	public void addInterval(Interval interval){
	}

	/**
	 */
	public void removeInterval(int id){
	}

	/** 
	 * Searches for the interval with the id given and returns it if it has been found.
	 * @param id: id of the interval to be found.
	 * @return Return the interval with the same id, or null if it has not been found.
	 */
	public Interval getInterval(int id){
		for (Interval interval : intervals) {
			if (interval.getId() == id) {
				return interval;
			}			
		}
		return null;
	}		 

	/**
	 */
	public Task(String name, String description){
		super(name, description); 
	}				

}
