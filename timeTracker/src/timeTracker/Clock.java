package timeTracker;

import java.util.Date;


public class Clock implements Runnable {	
	
	private static Clock uniqueInstance = new Clock();
	
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
		// TODO Auto-generated method stub
		  while(true) {			   
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
	private long refreshTime = 10;


	/**
	 * Setter of the property <tt>refreshTime</tt>
	 * @param refreshTime  The refreshTime to set.
	 * @uml.property  name="refreshTime"
	 */
	public void setRefreshTime(long refreshTime) {
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

		




}
		

