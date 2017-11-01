package timeTracker;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;


/**
 * This abstract class is used for the Decorator of any SimpleTask.
 * @uml.dependency   supplier="java.util.Observer"
 */
public abstract class Task extends Activity implements Observer {
	
	/**
	 * Used for serialization.
	 */
	private static final long serialVersionUID = 3L;
	
	/**
	 * While doing the deserialization, this function adds every Task to
	 * the Observer list.
	 */
	private void readObject(java.io.ObjectInputStream stream)
         throws IOException, ClassNotFoundException {
         stream.defaultReadObject();
         Clock.getInstance().getNotification().addObserver(this);
	}
	
	/**
	 * Constructor of the class.
	 */
	public Task(String description, String name, Project father) {
		super(description, name, father);
	}
	
	// Abstract methods that will be implemented in the sub-classes that need them.
	
	public abstract void acceptVisitor(Visitor visitor, int level);	
	
	public abstract void calculateTotalTime();	

	public abstract void start();	
	
	public abstract void update(Observable arg0, Object arg1);
	
	public abstract void stop();

	public abstract Interval getIntervalById(int id);
	
	public abstract Interval getLastInterval();
	
	public abstract boolean removeInterval(int id);

	public abstract boolean childrenIsEmpty();
		


}
