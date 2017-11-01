package timeTracker;

import java.util.Observable;

/**
 * This class is used to notify the Observers of a change in the Observable objects. 
 */
public class Notification extends Observable {
		
		/**
		 * Notifies each Observer about the new Date.
		 */
		public void clockNotify(){
			setChanged();
			notifyObservers();
		}

}
