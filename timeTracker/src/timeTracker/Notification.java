package timeTracker;

import java.util.Observable;


public class Notification extends Observable {
		
		/**
		 */
		public void clockNotify(){
			setChanged();
			notifyObservers();
		}

}
