package timetracker;

import java.util.ArrayList;

import java.util.Collection;
import java.util.Date;
import java.util.Observable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class defines the implementation of a Task without any Decorator.
 * 
 * @uml.dependency supplier="timetracker.Interval"
 * @uml.dependency supplier="java.util.Observer"
 */
public class SimpleTask extends Task {

	private static Logger logger = LoggerFactory.getLogger(SimpleTask.class);
	private static final long serialVersionUID = 4L;

	/**
	 * @uml.property name="children"
	 * @uml.associationEnd multiplicity="(0 -1)" aggregation="shared"
	 *                     inverse="task:timetracker.Interval"
	 */
	private Collection<Interval> children = new ArrayList<Interval>();

	/**
	 * Getter of the property <tt>children</tt>
	 * 
	 * @return Returns the intervals.
	 * @uml.property name="children"
	 */
	public final Collection<Interval> getChildren() {
		logger.debug("getting childrens from simple task: " + this.getName());
		assert this.invariant();
		return this.children;
	}

	public final boolean childrenIsEmpty() {
		logger.debug("checking if children is empty");
		assert this.invariant();
		return this.children.isEmpty();
	}

	/**
	 * @uml.property name="minIntervalTime"
	 */
	private static long minIntervalTime;

	/**
	 * Setter of the property <tt>minIntervalTime</tt>
	 * 
	 * @param intervalTime
	 *            The minIntervalTime to set.
	 * @uml.property name="minIntervalTime"
	 */
	public static void setMinIntervalTime(final long intervalTime) {
		logger.debug("setting min interval time: " + intervalTime);
		minIntervalTime = intervalTime;
	}

	public SimpleTask(final String name, final String description,
			final Project father) {
		super(description, name, father);
		this.children = new ArrayList<Interval>();
		assert this.invariant();

	}

	/**
	 * Searches for the interval with the id given and returns it if it has been
	 * found.
	 * @param id: id of the interval to be found.
	 */
	public final Interval getIntervalById(final int id) {
		logger.debug("searching interval with id: " + id + "in task "
				+ this.getName());
		for (Interval interval : children) {
			if (interval.getId() == id) {
				assert this.invariant();
				return interval;
			}
		}
		assert this.invariant();
		return null;
	}

	public final Interval getLastInterval() {
		assert this.invariant();
		return getIntervalById(children.size());
	}

	/**
	 * @param id: id of the interval to be removed.
	 */
	public final boolean removeInterval(final int id) {
		Interval interval = getIntervalById(id);
		if (interval != null) {
			logger.debug("interval exists");
			children.remove(interval);
			logger.info("interval with id " + id
					+ " has been removed from simple task " + this.getName());
			assert this.invariant();
			return true;
		} else {
			logger.debug("interval doesnt exist");
			assert this.invariant();
			return false;
		}
	}

	@Override
	public final void acceptVisitor(final Visitor visitor, final int level) {
		logger.debug("simple task " + this.getName() + "is accepting visitor");

		visitor.visitTask(this, level);
	}

	/**
	 * Adds a new Interval to the children list.
	 */
	public final void start() {
		if (!this.children.isEmpty() && this.getLastInterval().isRunning()) {
			logger.warn("Can't start the task, it is already running");
			System.out.println("Can't start the task, it is already running");
		} else {
			Interval interval = new Interval(children.size() + 1, this);
			children.add(interval);
			logger.info("interval for task " + this.getName() + " started");
		}
		assert this.invariant();
	}

	/**
	 * Implements the update() method of Task.java. If there is an active
	 * Interval, it will update it's properties.
	 */
	@Override
	public final void update(final Observable arg0, final Object arg1) {
		Date currentD = Clock.getInstance().getCurrentDate();

		if (!this.children.isEmpty()) {
			if (this.getLastInterval().isRunning()) {
				this.getLastInterval().setEndDate(currentD);
				this.getLastInterval().calculateTime();
				this.setEndDate(currentD);
			}
		}
		this.calculateTotalTime();
		assert this.invariant();
	}

	public final void stop() {
		if (!this.children.isEmpty()) {
			if (this.getLastInterval().isRunning()) {

				this.getLastInterval().stop();

				if (this.getLastInterval().getTotalTime() < minIntervalTime) {
					logger.debug("interval too short");
					this.removeInterval(this.getLastInterval().getId());
				}
			} else {
				logger.warn("trying to stop an interval already stopped");
				System.out.println("You can not stop an interval that "
						+ "has already been stopped.");
			}
		}
		assert this.invariant();
	}

	public final void calculateTotalTime() {
		long sum = 0;

		for (Interval child : children) {
			sum += child.getTotalTime();
		}

		setTotalTime(sum);

		logger.debug("calculating total time from simple task "
				+ this.getName() + " = " + sum);

		if (this.getFather() != null) {
			this.getFather().calculateTotalTime();
		}
		assert this.invariant();
	}

}
