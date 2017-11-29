package timetracker;

import java.util.Observable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used as a Decorator for a Task. A LimitedIntervalTime Task will
 * stop by itself when an interval reaches the established maximum time.
 */
public class LimitedIntervalTime extends TaskDecorator {

	/**
	 * Logger for the class.
	 */
	private static Logger logger = LoggerFactory
			.getLogger(LimitedIntervalTime.class);

	/**
	 * Used for serialization.
	 */
	private static final long serialVersionUID = 7L;

	/**
	 * @uml.property name="maxIntervalTime"
	 */
	private long maxIntervalTime;

	/**
	 * Getter of the property <tt>maxIntervalTime</tt>
	 * 
	 * @return Returns the maxIntervalTime.
	 * @uml.property name="maxIntervalTime"
	 */
	public final long getMaxIntervalTime() {
		logger.debug("getting max interval time: " + maxIntervalTime);
		assert this.invariant();
		return maxIntervalTime;
	}

	/**
	 * Setter of the property <tt>maxIntervalTime</tt>
	 * 
	 * @param maxIntervalTime
	 *            The maxIntervalTime to set.
	 * @uml.property name="maxIntervalTime"
	 */
	public final void setMaxIntervalTime(final long maxIntervalTimeSet) {
		logger.debug("setting max interval time: " + maxIntervalTime);
		this.maxIntervalTime = maxIntervalTimeSet;
		assert this.invariant();
	}

	/**
	 * Constructor of the class.
	 * 
	 * @param description
	 *            : description of the Task.
	 * @param name
	 *            : name of the Task.
	 * @param father
	 *            : Project Father of the task.
	 * @param task
	 *            : Task that is decorating.
	 * @param maxIntervalTime
	 *            : maxim of seconds each Interval of this task can last.
	 */
	public LimitedIntervalTime(final String description, final String name,
			final Project father, final Task task, 
			final long maxIntervalTimeSet) {
		super(description, name, father, task);
		this.maxIntervalTime = maxIntervalTimeSet;
		assert this.invariant();
	}

	/**
	 * Implements the update() method from Task.java. It will automatically stop
	 * the Task if it reaches the maxIntervalTime.
	 */
	@Override
	public final void update(final Observable observable, final Object object) {
		this.getTask().update(observable, object);

		if (!this.getTask().childrenIsEmpty()) {
			if (this.getTask().getLastInterval().isRunning()) {
				if (this.getTask().getLastInterval().getTotalTime() 
						>= this.maxIntervalTime) {
					this.getTask().stop();
				}
			}
		}
		assert this.invariant();
	}

	/**
	 * Implements the start() method from Task.java.
	 */
	public final void start() {
		this.getTask().start();
		assert this.invariant();
	}

}
