/**
 * 
 */
package timeTracker;

import java.util.Observable;
import java.util.Observer;
import java.util.Date;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * @author marcm
 */
public class Interval implements Observer, Serializable {
	
	static Logger logger = LoggerFactory.getLogger(Interval.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 4L;
	
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
	
	/**
	 * ******************************************************
	 */
	@Override
	public void update(Observable o, Object arg) {
		Date currentD = new Date();		
		long seconds = (currentD.getTime()-this.getStartDate().getTime())/1000;
		this.setTotalTime(seconds);
	}

	/**
	 * Constructor of the class.
	 * @param id: id of the new Interval.
	 */
	public Interval(int id){
		this.id = id;
		this.start();
	}

	/**
	 * Calculates the time between the startDate and the endDate.
	 */
	public void calculateTime(){
		long difference = endDate.getTime() - startDate.getTime();
		this.totalTime = difference;
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
	private long totalTime;

	/**
	 * Getter of the property <tt>totalTime</tt>
	 * @return  Returns the totalTime.
	 * @uml.property  name="totalTime"
	 */
	public long getTotalTime() {
		return totalTime;
	}

	/**
	 * Setter of the property <tt>totalTime</tt>
	 * @param seconds  The totalTime to set.
	 * @uml.property  name="totalTime"
	 */
	public void setTotalTime(long seconds) {
		this.totalTime = seconds;
	}

	/**
	 * Starts the execution of the current Interval.
	 */
	public void start(){
		Date startD = new Date();		
		this.setStartDate(startD);		
	}

	/**
	 * Stops the execution of the current Interval.
	 */
	public void stop(){
		Date endD = new Date();		
		this.setEndDate(endD);
		
	}

}
