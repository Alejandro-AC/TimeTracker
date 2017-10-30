package timeTracker;

import java.util.Observer;



public abstract class Task extends Activity implements Observer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 10L;

	public Task(String description, String name, Project father) {
		super(description, name, father);
	}

	@Override
	public void acceptVisitor(Impresor imp, int level) {
	}

	@Override
	public void calculateTotalTime() {
	}

	@Override
	public <T> T getChildren() {
		return null;
	}

}
