package timetracker;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Unique class is used to get the current time.
 */
public final class Clock implements Runnable {

	private static final int MAX_REFRESH_RATE = 1000;
	
	private static Logger logger = LoggerFactory.getLogger(Clock.class);

	private static Clock uniqueInstance = new Clock();

	public static Clock getInstance() {
		logger.debug("getting clock instance");
		return uniqueInstance;
	}

	/**
	 * @uml.property name="Notification"
	 */
	private Notification notification;

	/**
	 * Getter of the property <tt>Notification</tt>
	 * 
	 * @return Returns the notification.
	 * @uml.property name="Notification"
	 */
	public Notification getNotification() {
		logger.debug("getting clock notification");
		invariant();
		return notification;
	}

	/**
	 * @uml.property name="running"
	 */
	private boolean running = true;

	/**
	 * @uml.property name="currentDate"
	 */
	private Date currentDate;

	/**
	 * Getter of the property <tt>currentDate</tt>
	 * 
	 * @return Returns the currentDate.
	 * @uml.property name="currentDate"
	 */
	public Date getCurrentDate() {
		logger.debug("getting current date: " + currentDate);
		invariant();
		return currentDate;
	}

	/**
	 * The default refresh time is 1 second.
	 * @uml.property name="refreshTime"
	 */
	private long refreshTime = 1;

	/**
	 * Setter of the property <tt>refreshTime</tt>
	 * 
	 * @param refreshTime
	 *            The refreshTime to set.
	 * @uml.property name="refreshTime"
	 */
	public void setRefreshTime(final long refreshTimeSet) {
		logger.debug("set refresh time: " + refreshTimeSet);
		this.refreshTime = refreshTimeSet;
		invariant();
	}

	private Clock() {
		this.notification = new Notification();
		this.currentDate = new Date();
		invariant();
	}

	private boolean invariant() {
		boolean correct = true;

		if (this.refreshTime <= 0 || this.refreshTime > MAX_REFRESH_RATE) {
			correct = false;
		}
		if (!(this.currentDate instanceof Date)) {
			correct = false;
		}
		
		return correct;
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
		invariant();
	}

	public void terminate() {
		logger.debug("clock running off");
		this.running = false;
		invariant();
	}

}
