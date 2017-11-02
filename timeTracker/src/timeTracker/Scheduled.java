package timeTracker;

import java.util.Date;
import java.util.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used as a Decorator for a Task.
 * A Scheduled Task will start only when the current date matches its scheduledDate.  
 */
public class Scheduled extends TaskDecorator {

	/**
	 * Logger for the class.
	 */
	static Logger logger = LoggerFactory.getLogger(Scheduled.class);
	
	/**
	 * Used for serialization.
	 */
	private static final long serialVersionUID = 6L;
	
	/**
	 * @uml.property  name="scheduledDate"
	 */
	private Date scheduledDate;

		/**
		 * Getter of the property <tt>scheduledDate</tt>
		 * @return  Returns the scheduledDate.
		 * @uml.property  name="scheduledDate"
		 */
		public Date getScheduledDate() {
			//logger.debug("getting scheduled date " + this.getName());
			return scheduledDate;
		}
	
		/**
		 * Setter of the property <tt>scheduledDate</tt>
		 * @param scheduledDate  The scheduledDate to set.
		 * @uml.property  name="scheduledDate"
		 */
		public void setScheduledDate(Date scheduledDate) {
			//logger.debug("setting scheduled date: " + this.scheduledDate);
			this.scheduledDate = scheduledDate;
		}
	
	/**
	 * Constructor of the class.
	 * @param description: description of the Task.
	 * @param name: name of the Task.
	 * @param father: Project Father of the task.
	 * @param task: Task that is decorating.
	 * @param scheduledDate: Date when the Task will start. 
	 */
	public Scheduled(String description, String name, Project father, Task task, Date scheduledDate) {
		super(description, name, father, task);
		this.scheduledDate = scheduledDate;
	}

	/**
	 * Implements the start() method from Task.java.
	 * It will only start if the actual Date is the same as the scheduledDate.
	 */
	@Override
	public void start() {
		//logger.debug("scheduled task start");
		Date nearestSecond = new Date( ((Clock.getInstance().getCurrentDate().getTime() + 500) / 1000) * 1000 );
		if(!nearestSecond.before(this.scheduledDate) && !nearestSecond.after(this.scheduledDate)) {
			this.task.start();
		}
	}
	
	/**
	 * Implements the update() method from Task.java.
	 * It will check in any execution of update if the Task can start.
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		this.start();
		this.task.update(arg0, arg1);			
	}
	

}