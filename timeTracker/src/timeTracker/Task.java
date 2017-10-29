/**
 * 
 */
package timeTracker;


import java.util.ArrayList;
import java.util.Collection;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/** 
 * @author marcm
 */
public class Task extends Activity {
	
	static Logger logger = LoggerFactory.getLogger(Task.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 3L;
	
	/** 
	 * @uml.property name="children"
	 * @uml.associationEnd multiplicity="(0 -1)" aggregation="shared" inverse="task:timeTracker.Interval"
	 */
	private Collection<Interval> children = new ArrayList<Interval>();
	
	/**
	 * Getter of the property <tt>children</tt>
	 * @return  Returns the intervals.
	 * @uml.property  name="children"
	 */
	@SuppressWarnings("unchecked")
	public Collection<Interval> getChildren() {
		if (children.isEmpty()) {
			return null;
		} else {
			return children;
		}
	}

	/**
	 * Constructor of the class.
	 */
	public Task(String name, String description, Project father) {
		super(description, name, father);
		
	}

	/**
	 * Adds a new interval to the intervals list.
	 */
	public void addInterval() {	
		if (!this.children.isEmpty() && this.getLastInterval().isRunning() == true){
			logger.warn("Can't start the task, it is already running");
			System.out.println("Can't start the task, it is already running");
		}else{
			Interval interval = new Interval(children.size() + 1, this);
			children.add(interval);		
			Clock.getInstance().getNotification().addObserver(interval);
			logger.info("interval for task "+this.getName()+" started");
		}
		
	}

	/**
	 * Calculates the total time of all the children of the current Task.
	 */
	public void calculateTotalTime() {
		long sum = 0;
		
		for (Interval child : children) {
			sum += child.getTotalTime();
		}
		
		this.totalTime = sum;
		
		if(this.father != null){
			father.calculateTotalTime();
		}
	}

	/** 
	 * Searches for the interval with the id given and returns it if it has been found.
	 * @param id: id of the interval to be found.
	 * @return Return the interval with the same id, or null if it has not been found.
	 */
	public Interval getIntervalById(int id) {
		for (Interval interval : children) {
			if (interval.getId() == id) {
				return interval;
			}			
		}
		return null;
	}
	

	/**
	 * Removes the specified interval if it is in the intervals list.
	 * @param id: id of the interval to be removed.
	 * @return True if the interval could be removed and false if it couldn't.
	 */
	public boolean removeInterval(int id) {
		Interval interval = getIntervalById(id);
		if (interval != null) {
			logger.debug("interval exists");
			children.remove(interval);
			return true;
		} else {
			logger.debug("interval doesnt exist");
			return false;
		}
	}

	/**
	 */
	public Interval getLastInterval(){
		return getIntervalById(children.size());			
	}
			
	/**
	 */
	public void stop() {
		if (!this.children.isEmpty()) {
			if(this.getLastInterval().getStartDate() != null && this.getLastInterval().getEndDate() == Clock.getInstance().getCurrentDate()) {
				
				this.getLastInterval().stop();
				
				if (this.getLastInterval().getTotalTime() < minIntervalTime) {
					logger.debug("interval too short");
					this.removeInterval(this.getLastInterval().getId());					
				}
			} else {
				System.out.println("No se puede parar un intervalo que ya se ha parado.");
			}
		}
	}


	@Override
	public void acceptVisitor(Impresor imp, int level) {		
		imp.visitTask(this, level);
		for (Interval child : children) {
			child.acceptVisitor(imp, level+1);
		}	
	}


	/** 
	 * @uml.property name="minIntervalTime"
	 */
	public static long minIntervalTime;


	/**
	 */
	public static void setMinIntervalTime(long intervalTime){
		minIntervalTime = intervalTime;
	}
}


