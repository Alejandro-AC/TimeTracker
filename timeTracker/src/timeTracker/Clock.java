package timeTracker;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Clock implements Runnable {	
	
	private static Clock uniqueInstance = new Clock();
	static Logger logger = LoggerFactory.getLogger(Client.class);
	/**
	 */
	private Clock(){	
		this.notification = new Notification();
		this.currentDate = new Date();
	}

	public static Clock getInstance() {
		return uniqueInstance;
	}
	

	/** 
	 * @uml.property name="Notification"
	 */
	private Notification notification;

	/** 
	 * @uml.property name="currentDate"
	 */
	private Date currentDate;

	
	@Override
	public void run() {
		logger.debug("Clock started running");
		// TODO Auto-generated method stub
		  while(running) {			   
			   try {
				Thread.sleep(this.refreshTime*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			   this.currentDate = new Date();
			   notification.clockNotify();
		  }
	}

	/**
	 * @uml.property  name="refreshTime"
	 */
	private long refreshTime = 1; //Default refresh 1s


	/**
	 * Setter of the property <tt>refreshTime</tt>
	 * @param refreshTime  The refreshTime to set.
	 * @uml.property  name="refreshTime"
	 */
	public void setRefreshTime(long refreshTime) {
		logger.debug("set refresh time: "+refreshTime);
		this.refreshTime = refreshTime;
	}


	/**
	 * Getter of the property <tt>currentDate</tt>
	 * @return  Returns the currentDate.
	 * @uml.property  name="currentDate"
	 */
	public Date getCurrentDate() {
		return currentDate;
	}

	/**
	 * Getter of the property <tt>Notification</tt>
	 * @return  Returns the notification.
	 * @uml.property  name="Notification"
	 */
	public Notification getNotification() {
		return notification;
	}


	/**
	 * @uml.property  name="running"
	 */
	private boolean running = true;

	/**
	 */
	public void terminate(){
		logger.debug("clock running off");
		this.running = false;
	}

		




}
		

