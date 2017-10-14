/**
 * 
 */
package timeTracker;

import java.util.Observable;
import java.util.Observer;
import java.util.Date;

/** 
 * @author marcm
 */
public class Interval implements Observer {

	/** 
	 * @uml.property name="task"
	 * @uml.associationEnd multiplicity="(1 1)" inverse="children:timeTracker.Task"
	 */
	private Task task = null;

	/** 
	 * Getter of the property <tt>task</tt>
	 * @return  Returns the task.
	 * @uml.property  name="task"
	 */
	public Task getTask() {
		return task;
	}

	/** 
	 * Setter of the property <tt>task</tt>
	 * @param task  The task to set.
	 * @uml.property  name="task"
	 */
	public void setTask(Task task) {
		this.task = task;
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

	/**
	 */
	public Interval(int id){
	}

	/**
	 */
	public void calculateTime(){
	}
	
	/**
	 * @uml.property  name="endDate"
	 */
	private Date endDate;

	/**
	 * Getter of the property <tt>endDate</tt>
	 * @return  Returns the endDate.
	 * @uml.property  name="endDate"
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * Setter of the property <tt>endDate</tt>
	 * @param endDate  The endDate to set.
	 * @uml.property  name="endDate"
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @uml.property  name="id"
	 */
	private int id;

	/**
	 * Getter of the property <tt>id</tt>
	 * @return  Returns the id.
	 * @uml.property  name="id"
	 */
	public int getId() {
		return id;
	}

	/**
	 * Setter of the property <tt>id</tt>
	 * @param id  The id to set.
	 * @uml.property  name="id"
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @uml.property  name="startDate"
	 */
	private Date startDate;

	/**
	 * Getter of the property <tt>startDate</tt>
	 * @return  Returns the startDate.
	 * @uml.property  name="startDate"
	 */
	public Date getStartDate() {
		return startDate;
	}
	
	/**
	 * Setter of the property <tt>startDate</tt>
	 * @param startDate  The startDate to set.
	 * @uml.property  name="startDate"
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @uml.property  name="totalTime"
	 */
	private Date totalTime;

	/**
	 * Getter of the property <tt>totalTime</tt>
	 * @return  Returns the totalTime.
	 * @uml.property  name="totalTime"
	 */
	public Date getTotalTime() {
		return totalTime;
	}

	/**
	 * Setter of the property <tt>totalTime</tt>
	 * @param totalTime  The totalTime to set.
	 * @uml.property  name="totalTime"
	 */
	public void setTotalTime(Date totalTime) {
		this.totalTime = totalTime;
	}

	/**
	 */
	public void start(){
	}

	/**
	 */
	public void stop(){
	}

}
