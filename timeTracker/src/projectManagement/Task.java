/**
 * 
 */
package projectManagement;

import java.util.AbstractList;
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
	public Collection<Interval> getIntervals() {
		return intervals;
	}

	/**
	 * Setter of the property <tt>intervals</tt>
	 * @param intervals  The intervals to set.
	 * @uml.property  name="intervals"
	 */
	public void setIntervals(Collection<Interval> intervals) {
		this.intervals = intervals;
	}

	public void calculateTotalTime() {
		// TODO Auto-generated method stub
		
	}

	/** 
	 * Adds an interval to the intervals list.
	 */
	public void addInterval(){
		Interval interval = new Interval();
		intervals.add(interval);
	}	
	
	/**
	 * Removes the specified interval if it is in the intervals list.
	 * @param id: id of the interval to be removed.
	 * @return True if the interval could be removed and false if it couldn't.
	 */
	public boolean removeInterval(int id){
		Interval interval = getInterval(id);
		if (interval != null) {
			int index = (((AbstractList<Interval>) intervals).indexOf(interval));
			intervals.remove(index);
			return true;
		} else {
			return false;
		}
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
