package timetracker;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Unique class is used to get the current time.
 */
public final class Clock implements Runnable {	
	
	/**
	 * Logger for the class.
	 */
	private static Logger logger = LoggerFactory.getLogger(Clock.class);
	
	/**
	 * Getter of the property <tt>logger</tt>
	 * @return  Returns the Logger.
	 * @uml.property  name="logger"
	 */
	public static Logger getLogger() {
		return logger;
	}
	
	/**
	 * Unique instance of the Clock for all the system.
	 */
	private static Clock uniqueInstance = new Clock();
	
		/**
		 * 
		 * @return uniqueInstance of the Clock.
		 */
		public static Clock getInstance() {
			//logger.debug("getting clock instance");
			return uniqueInstance;
		}
	
	/** 
	 * @uml.property name="Notification"
	 */
	private Notification notification;

		/**
		 * Getter of the property <tt>Notification</tt>
		 * @return  Returns the notification.
		 * @uml.property  name="Notification"
		 */
		public Notification getNotification() {
			logger.debug("getting clock notification");
			return notification;
		}
	
	/**
	 * @uml.property  name="running"
	 */
	private boolean running = true;
	
	/** 
	 * @uml.property name="currentDate"
	 */
	private Date currentDate;

		/**
		 * Getter of the property <tt>currentDate</tt>
		 * @return  Returns the currentDate.
		 * @uml.property  name="currentDate"
		 */
		public Date getCurrentDate() {
			//logger.debug("getting current date: " + currentDate);
			return currentDate;
		}
	
	/**
	 * The default refresh time is 
	 * @uml.property  name="refreshTime"
	 */
	private long refreshTime = 1; //Default refresh 1s
	
		/**
		 * Setter of the property <tt>refreshTime</tt>
		 * @param refreshTime  The refreshTime to set.
		 * @uml.property  name="refreshTime"
		 */
		public void setRefreshTime(final long refreshTimeSet) {
			logger.debug("set refresh time: " + refreshTimeSet);
			this.refreshTime = refreshTimeSet;
		}
	
	/**
	 * Constructor of the class
	 */
	private Clock() {	
		this.notification = new Notification();
		this.currentDate = new Date();
	}
	
	/**
	 * Operations that the Clock must do after every refreshTime. 
	 */
	@Override
	public void run() {
		final int secConvertTime = 1000;
		logger.debug("Clock started running");
		  while (running) {			   
			   try {
				   Thread.sleep(this.refreshTime * secConvertTime);
			   } catch (InterruptedException e) {
				   e.printStackTrace();
				   logger.error("Error trying to put to sleep the Clock");
			   }
			   this.currentDate = new Date();
			   notification.clockNotify();
		  }
	}

	/**
	 * To stop the Clock.
	 */
	public void terminate() {
		logger.debug("clock running off");
		this.running = false;
	}
	

}
		

