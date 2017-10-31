package timeTracker;

import java.util.Calendar;
import java.util.Date;
import java.util.Observable;


public class Scheduled extends TaskDecorator {

	/**
	 * 
	 */
	private static final long serialVersionUID = 13L;

	public Scheduled(String description, String name, Project father, Task task, Date scheduledDate) {
		super(description, name, father, task);
		this.scheduledDate = scheduledDate;
	}

	
	@Override
	public void start() {
		Date nearestSecond = new Date( ((Clock.getInstance().getCurrentDate().getTime() + 500) / 1000) * 1000 );
		if(!nearestSecond.before(this.scheduledDate) &&
				!nearestSecond.after(this.scheduledDate)) {
			this.task.start();
		}
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		
		this.start();
		

		this.task.update(arg0, arg1);	

		
	}
	
	

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
		return scheduledDate;
	}

	/**
	 * Setter of the property <tt>scheduledDate</tt>
	 * @param scheduledDate  The scheduledDate to set.
	 * @uml.property  name="scheduledDate"
	 */
	public void setScheduledDate(Date scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

}
