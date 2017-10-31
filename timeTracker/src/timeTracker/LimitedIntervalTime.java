package timeTracker;

import java.util.Observable;


public class LimitedIntervalTime extends TaskDecorator {

	/**
	 * 
	 */
	private static final long serialVersionUID = 12L;
    
	public LimitedIntervalTime(String description, String name, Project father, Task task, long maxIntervalTime) {
		super(description, name, father, task);
		this.maxIntervalTime = maxIntervalTime;
	}
    
	/*public LimitedIntervalTime(String description, String name, Project father) {
		super(description, name, father);
		// TODO Auto-generated constructor stub
	}*/

	@Override
	public void update(Observable arg0, Object arg1) {
		this.task.update(arg0, arg1);	

		if (!this.task.childrenIsEmpty()) {
			if (this.task.getLastInterval().isRunning()) {
				if (this.task.getLastInterval().getTotalTime() >= this.maxIntervalTime) {
					this.task.stop();
				}
			}
		}		
	}

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

}
