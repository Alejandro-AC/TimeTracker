package timeTracker;

import java.util.Collection;
import java.util.Observable;




public abstract class TaskDecorator extends Task {

	/**
	 * 
	 */
	private static final long serialVersionUID = 11L;

	public TaskDecorator(String description, String name, Project father, Task task) {
		super(description, name, father);
		this.task = task;
	}    

	/** 
	 * @uml.property name="task"
	 * @uml.associationEnd multiplicity="(1 1)" aggregation="shared" inverse="taskDecorator:timeTracker.Task"
	 */
	protected Task task = null;

	/** 
	 * Getter of the property <tt>task</tt>
	 * @return  Returns the task.
	 * @uml.property  name="task"
	 */
	public Task getTask() {
		return task;
	}

	/** 
	 * Setter of the property <tt>task</tt>
	 * @param task  The task to set.
	 * @uml.property  name="task"
	 */
	public void setTask(Task task) {
		this.task = task;
	}
	
	public void acceptVisitor(Visitor visitor, int level) {
		this.task.acceptVisitor(visitor, level);
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Interval> getChildren() {
		return this.task.getChildren();
	}
	
	public void calculateTotalTime() {
		this.task.calculateTotalTime();
	}

	public void start() {
		this.task.start();
	}

	public Interval getIntervalById(int id) {
		return this.task.getIntervalById(id);
	}
	
	public Interval getLastInterval() {
		return this.task.getLastInterval();
	}
	
	public boolean removeInterval(int id) {
		return this.task.removeInterval(id);
	}

	public void stop() {
		this.task.stop();
	}
	
	public boolean childrenIsEmpty() {
		return this.task.childrenIsEmpty();
	}
	
	public void update(Observable arg0, Object arg1) {
		this.task.update(arg0, arg1);
	}
}
