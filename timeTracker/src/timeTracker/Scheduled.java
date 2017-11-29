package timetracker;

import java.util.Date;
import java.util.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used as a Decorator for a Task. A Scheduled Task will start
 * only when the current date matches its scheduledDate.
 */
public class Scheduled extends TaskDecorator {

	private static final int SECOND_IN_MILISECONDS = 1000;

	private static Logger logger = LoggerFactory.getLogger(Scheduled.class);
	private static final long serialVersionUID = 6L;

	/**
	 * @uml.property name="scheduledDate"
	 */
	private Date scheduledDate;

	/**
	 * Getter of the property <tt>scheduledDate</tt>
	 * 
	 * @return Returns the scheduledDate.
	 * @uml.property name="scheduledDate"
	 */
	public final Date getScheduledDate() {
		logger.debug("getting scheduled date " + this.getName());
		assert this.invariant();
		return scheduledDate;
	}

	public Scheduled(final String description, final String name,
			final Project father, final Task task, final Date scheduleDate) {
		super(description, name, father, task);
		this.scheduledDate = scheduleDate;
		assert this.invariant();
	}

	/**
	 * Implements the start() method from Task.java. It will only start if the
	 * actual Date is the same as the scheduledDate.
	 */
	@Override
	public final void start() {
		logger.debug("scheduled task start");
		Date nearestSecond = new Date(
				((Clock.getInstance().getCurrentDate().getTime() 
						+ (SECOND_IN_MILISECONDS / 2)) / SECOND_IN_MILISECONDS)
						* SECOND_IN_MILISECONDS);

		if (!nearestSecond.before(this.scheduledDate)
				&& !nearestSecond.after(this.scheduledDate)) {
			this.getTask().start();
		}
		assert this.invariant();
	}

	/**
	 * Implements the update() method from Task.java. It will check in any
	 * execution of update if the Task can start.
	 */
	@Override
	public final void update(final Observable arg0, final Object arg1) {
		this.start();
		this.getTask().update(arg0, arg1);
		assert this.invariant();
	}

}