package timetracker;

import java.io.IOException;
import java.util.Collection;
import java.util.Observable;
import java.util.Observer;

/**
 * This abstract class is used for the Decorator of any SimpleTask.
 * 
 * @uml.dependency supplier="java.util.Observer"
 */
public abstract class Task extends Activity implements Observer {

	private static final long serialVersionUID = 3L;

	/**
	 * While doing the deserialization, this function adds every Task to the
	 * Observer list.
	 */
	private void readObject(final java.io.ObjectInputStream stream)
			throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
		Clock.getInstance().getNotification().addObserver(this);
	}
	
	public Task(final String description, final String name,
			final Project father) {
		super(description, name, father);
	}

	@SuppressWarnings("unchecked")
	@Override
	public abstract Collection<Interval> getChildren();
	
	protected final boolean invariant() {
		boolean correct = true;
		if (this.getFather() == null) {
			correct = false;
		}
		if (!(this.getFather() instanceof Project)) {
			correct = false;
		}
		for (Object obj:this.getChildren()) {
			if (!(obj instanceof Interval) && !childrenIsEmpty()) {
				correct = false;
			}
		}
		return correct;
	}

	// Abstract methods that will be implemented in the
	// sub-classes that need them.

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
