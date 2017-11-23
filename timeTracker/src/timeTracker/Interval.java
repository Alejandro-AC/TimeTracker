package timetracker;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to instance every period of time
 * that a Task has been running.
 */
public class Interval implements Serializable {
	
	/**
	 * Logger for the class.
	 */
	private static  Logger logger = LoggerFactory.getLogger(Interval.class);
	
	/**
	 * Getter of the property <tt>logger</tt>
	 * @return  Returns the Logger.
	 * @uml.property  name="logger"
	 */
	public static final Logger getLogger() {
		return logger;
	}

	/**
	 * Used for serialization.
	 */
	private static final long serialVersionUID = 8L;
	
	/**
	 * @uml.property   name="simpleTask"
	 * @uml.associationEnd   multiplicity="(1 1)" 
	 * inverse="children:time.Tracker.SimpleTask"
	 */
	private SimpleTask simpleTask = null;

	/** 
	 * Getter of the property <tt>simpleTask</tt>
	 * @return  Returns the simpleTask.
	 * @uml.property  name="simpleTask"
	 */
	public final SimpleTask getTask() {
		logger.debug("getting Task: " + simpleTask.getName());
		return simpleTask;
	}

	/** 
	 * Setter of the property <tt>simpleTask</tt>
	 * @param simpleTask  The simpleTask to set.
	 * @uml.property  name="simpleTask"
	 */
	public final void setTask(final SimpleTask simpleTaskToSet) {
		logger.debug("setting task: " + simpleTask.getName());
		this.simpleTask = simpleTaskToSet;
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
	public final Date getEndDate() {
		logger.debug("getting end date: " + endDate);
		return endDate;
	}

	/**
	 * Setter of the property <tt>endDate</tt>
	 * @param endDate  The endDate to set.
	 * @uml.property  name="endDate"
	 */
	public final void setEndDate(final Date endDateToSet) {
		getLogger().debug("setting end date: " + endDate);
		this.endDate = endDateToSet;
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
	public final int getId() {
		getLogger().debug("getting id" + id);
		return id;
	}

	/**
	 * Setter of the property <tt>id</tt>
	 * @param id  The id to set.
	 * @uml.property  name="id"
	 */
	public final void setId(final int idToSet) {
		this.id = idToSet;
		logger.debug("set id: " + id);
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
	public final Date getStartDate() {
		logger.debug("getting start date: " + startDate);
		return startDate;
	}
	
	/**
	 * Setter of the property <tt>startDate</tt>
	 * @param startDate  The startDate to set.
	 * @uml.property  name="startDate"
	 */
	public final void setStartDate(final Date startDateToSet) {
		logger.debug("set start date: " + startDateToSet);
		this.startDate = startDateToSet;
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
	public final long getTotalTime() {
		getLogger().debug("getting total time: " + totalTime);
		return totalTime;
	}

	/**
	 * Setter of the property <tt>totalTime</tt>
	 * @param seconds  The totalTime to set.
	 * @uml.property  name="totalTime"
	 */
	public final void setTotalTime(final long seconds) {
		logger.debug("set total time: " + seconds);
		this.totalTime = seconds;
	}
	
	/**
	 * @uml.property  name="running"
	 */
	private boolean running = false;

	/**
	 * Getter of the property <tt>running</tt>
	 * @return  Returns the running.
	 * @uml.property  name="running"
	 */
	public final boolean isRunning() {
		getLogger().debug("interval running: " + running);
		return running;
	}

	/**
	 * Setter of the property <tt>running</tt>
	 * @param running  The running to set.
	 * @uml.property  name="running"
	 */
	public final void setRunning(final boolean runningToSet) {
		logger.debug("setting interval running: " + runningToSet);
		this.running = runningToSet;
	}
	
	/**
	 * Constructor of the class.
	 */
	public Interval(final int intervalId, final SimpleTask father) {
		this.id = intervalId;
		this.simpleTask = father;
		this.start();
	}

	/**
	 * Calculates the time between the startDate and the endDate.
	 */
	public final void calculateTime() {
		long difference = endDate.getTime() - startDate.getTime();
		long seconds = TimeUnit.MILLISECONDS.toSeconds(difference);
		this.totalTime = seconds;
		getLogger().debug("calculated time: " + this.totalTime);
	}

	/**
	 * Starts the execution of the current Interval.
	 */
	public final void start() {
		logger.debug("interval started");
		Date startD = Clock.getInstance().getCurrentDate();		
		this.setStartDate(startD);
		if (this.getId() == 1) {
			this.getTask().setStartDate(startD);
		}
		this.setRunning(true);
	}

	/**
	 * Stops the execution of the current Interval.
	 */
	public final void stop() {
		logger.debug("interval stopped");
		Date endD = Clock.getInstance().getCurrentDate();
		
		this.setEndDate(endD);
		
		this.setRunning(false);
	}
	
	/**
	 * Accepts a Visitor (in this case,
	 * the Impresor to print this Acitivity's information).
	 * @param visitor: visitor that is being accepted.
	 * @level: current level of the Interval in the Activities Tree.
	 */
	public final void acceptVisitor(final Visitor visitor, final int level) {
		logger.debug("Interval with ID " + this.getId() + "accepting visitor");
		visitor.visitInterval(this, level + 1);
	}


}
