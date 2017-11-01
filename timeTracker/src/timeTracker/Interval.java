package timeTracker;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to instance every period of time that a Task has been running.
 */
public class Interval implements Serializable {
	
	/**
	 * Logger for the class.
	 */
	static Logger logger = LoggerFactory.getLogger(Interval.class);

	/**
	 * Used for serialization.
	 */
	private static final long serialVersionUID = 4L;
	
	/**
	 * @uml.property   name="simpleTask"
	 * @uml.associationEnd   multiplicity="(1 1)" inverse="children:timeTracker.SimpleTask"
	 */
	private SimpleTask simpleTask = null;

		/** 
		 * Getter of the property <tt>simpleTask</tt>
		 * @return  Returns the simpleTask.
		 * @uml.property  name="simpleTask"
		 */
		public SimpleTask getTask() {
			return simpleTask;
		}
	
		/** 
		 * Setter of the property <tt>simpleTask</tt>
		 * @param simpleTask  The simpleTask to set.
		 * @uml.property  name="simpleTask"
		 */
		public void setTask(SimpleTask simpleTask) {
			this.simpleTask = simpleTask;
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
		public Date getStartDate() {
			return startDate;
		}
		
		/**
		 * Setter of the property <tt>startDate</tt>
		 * @param startDate  The startDate to set.
		 * @uml.property  name="startDate"
		 */
		public void setStartDate(Date startDate) {
			logger.debug("set start date: " + startDate);
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
		public boolean isRunning() {
			return running;
		}

		/**
		 * Setter of the property <tt>running</tt>
		 * @param running  The running to set.
		 * @uml.property  name="running"
		 */
		public void setRunning(boolean running) {
			this.running = running;
		}
	
	/**
	 * Constructor of the class.
	 */
	public Interval(int id, SimpleTask father){
		this.id = id;
		this.simpleTask = father;
		this.start();
	}

	/**
	 * Calculates the time between the startDate and the endDate.
	 */
	public void calculateTime(){
		long difference = endDate.getTime() - startDate.getTime();
		long seconds = TimeUnit.MILLISECONDS.toSeconds(difference);
		this.totalTime = seconds;
	}

	/**
	 * Starts the execution of the current Interval.
	 */
	public void start(){
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
	public void stop(){
		logger.debug("interval stopped");
		Date endD = Clock.getInstance().getCurrentDate();		
		this.setEndDate(endD);
		this.setRunning(false);
	}
	
	/**
	 * Accepts a Visitor (in this case, the Impresor to print this Acitivity's information).
	 * @param visitor: visitor that is being accepted.
	 * @level: current level of the Interval in the Activities Tree.
	 */
	public void acceptVisitor(Visitor visitor, int level) {
		visitor.visitInterval(this, level+1);
	}


}
