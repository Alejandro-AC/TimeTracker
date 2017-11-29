package timetracker;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to instance every period of time that a Task has been
 * running.
 */
public class Interval implements Serializable {

	private static Logger logger = LoggerFactory.getLogger(Interval.class);
	private static final long serialVersionUID = 8L;

	/**
	 * @uml.property name="simpleTask"
	 * @uml.associationEnd multiplicity="(1 1)"
	 *                     inverse="children:time.Tracker.SimpleTask"
	 */
	private SimpleTask simpleTask = null;

	/**
	 * Getter of the property <tt>simpleTask</tt>
	 * 
	 * @return Returns the simpleTask.
	 * @uml.property name="simpleTask"
	 */

	private boolean invariant() {
		boolean correct = true;
		
		if (this.simpleTask == null) {
			correct = false;
		}
		if (!(this.simpleTask instanceof SimpleTask) 
				|| !(this.simpleTask instanceof Task)) {
			correct = false;
		}
		if (this.getStartDate().after(this.getEndDate())) {
			correct = false;
		}
		return correct;
	}

	public final SimpleTask getTask() {
		logger.debug("getting Task: " + simpleTask.getName());
		assert this.invariant();
		return simpleTask;
	}

	/**
	 * @uml.property name="endDate"
	 */
	private Date endDate;

	/**
	 * Getter of the property <tt>endDate</tt>
	 * 
	 * @return Returns the endDate.
	 * @uml.property name="endDate"
	 */
	public final Date getEndDate() {
		logger.debug("getting end date: " + endDate);
		assert this.invariant();
		return endDate;
	}

	/**
	 * Setter of the property <tt>endDate</tt>
	 * 
	 * @param endDate
	 *            The endDate to set.
	 * @uml.property name="endDate"
	 */
	public final void setEndDate(final Date endDateToSet) {
		logger.debug("setting end date: " + endDate);
		this.endDate = endDateToSet;
		assert this.invariant();
	}

	/**
	 * @uml.property name="id"
	 */
	private int id;

	/**
	 * Getter of the property <tt>id</tt>
	 * 
	 * @return Returns the id.
	 * @uml.property name="id"
	 */
	public final int getId() {
		logger.debug("getting id" + id);
		assert this.invariant();
		return id;
	}

	/**
	 * @uml.property name="startDate"
	 */
	private Date startDate;

	/**
	 * Getter of the property <tt>startDate</tt>
	 * 
	 * @return Returns the startDate.
	 * @uml.property name="startDate"
	 */
	public final Date getStartDate() {
		logger.debug("getting start date: " + startDate);
		assert this.invariant();
		return startDate;
	}

	/**
	 * Setter of the property <tt>startDate</tt>
	 * 
	 * @param startDate
	 *            The startDate to set.
	 * @uml.property name="startDate"
	 */
	public final void setStartDate(final Date startDateToSet) {
		logger.debug("set start date: " + startDateToSet);
		this.startDate = startDateToSet;
		assert this.invariant();
	}

	/**
	 * @uml.property name="totalTime"
	 */
	private long totalTime;

	/**
	 * Getter of the property <tt>totalTime</tt>
	 * 
	 * @return Returns the totalTime.
	 * @uml.property name="totalTime"
	 */
	public final long getTotalTime() {
		logger.debug("getting total time: " + totalTime);
		assert this.invariant();
		return totalTime;
	}

	/**
	 * @uml.property name="running"
	 */
	private boolean running = false;

	/**
	 * Getter of the property <tt>running</tt>
	 * 
	 * @return Returns the running.
	 * @uml.property name="running"
	 */
	public final boolean isRunning() {
		logger.debug("interval running: " + running);
		assert this.invariant();
		return running;
	}

	/**
	 * Setter of the property <tt>running</tt>
	 * 
	 * @param running
	 *            The running to set.
	 * @uml.property name="running"
	 */
	public final void setRunning(final boolean runningToSet) {
		logger.debug("setting interval running: " + runningToSet);
		this.running = runningToSet;
		assert this.invariant();
	}

	
	public Interval(final int intervalId, final SimpleTask father) {
		this.id = intervalId;
		this.simpleTask = father;
		this.start();
		assert this.invariant();
	}

	public final void calculateTime() {
		long difference = endDate.getTime() - startDate.getTime();
		long seconds = TimeUnit.MILLISECONDS.toSeconds(difference);
		this.totalTime = seconds;
		logger.debug("calculated time: " + this.totalTime);
		assert this.invariant();
	}

	public final void start() {
		logger.debug("interval started");
		Date startD = Clock.getInstance().getCurrentDate();
		this.setStartDate(startD);
		if (this.getId() == 1) {
			this.getTask().setStartDate(startD);
		}
		this.setRunning(true);
		assert this.invariant();
	}

	public final void stop() {
		logger.debug("interval stopped");
		Date endD = Clock.getInstance().getCurrentDate();

		this.setEndDate(endD);

		this.setRunning(false);
		assert this.invariant();
	}

	/**
	 * @level: current level of the Interval in the Activities Tree.
	 */
	public final void acceptVisitor(final Visitor visitor, final int level) {
		logger.debug("Interval with ID " + this.getId() + "accepting visitor");

		visitor.visitInterval(this, level + 1);
		assert this.invariant();

	}

}
