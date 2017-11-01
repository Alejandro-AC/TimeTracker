package timeTracker;

import java.util.Observable;

/**
 * This class is used as a Decorator for a Task.
 * A LimitedIntervalTime Task will stop by itself when an interval reaches the established maximum time. 
 */
public class LimitedIntervalTime extends TaskDecorator {

	/**
	 * Used for serialization.
	 */
	private static final long serialVersionUID = 12L;
	
	/**
	 * @uml.property  name="maxIntervalTime"
	 */
	private long maxIntervalTime;

		/**
		 * Getter of the property <tt>maxIntervalTime</tt>
		 * @return  Returns the maxIntervalTime.
		 * @uml.property  name="maxIntervalTime"
		 */
		public long getMaxIntervalTime() {
			return maxIntervalTime;
		}
		
		/**
		 * Setter of the property <tt>maxIntervalTime</tt>
		 * @param maxIntervalTime  The maxIntervalTime to set.
		 * @uml.property  name="maxIntervalTime"
		 */
		public void setMaxIntervalTime(long maxIntervalTime) {
			this.maxIntervalTime = maxIntervalTime;
		}
	
	/**
	 * Constructor of the class.
	 * @param description: description of the Task.
	 * @param name: name of the Task.
	 * @param father: Project Father of the task.
	 * @param task: Task that is decorating.
	 * @param maxIntervalTime: maxim of seconds each Interval of this task can last.
	 */
	public LimitedIntervalTime(String description, String name, Project father, Task task, long maxIntervalTime) {
		super(description, name, father, task);
		this.maxIntervalTime = maxIntervalTime;
	}
 
	/**
	 * Implements the update() method from Task.java. 
	 * It will automatically stop the Task if it reaches the maxIntervalTime.
	 */
	@Override
	public void update(Observable observable, Object object) {
		this.task.update(observable, object);	

		if (!this.task.childrenIsEmpty()) {
			if (this.task.getLastInterval().isRunning()) {
				if (this.task.getLastInterval().getTotalTime() >= this.maxIntervalTime) {
					this.task.stop();
				}
			}
		}		
	}
	

}
