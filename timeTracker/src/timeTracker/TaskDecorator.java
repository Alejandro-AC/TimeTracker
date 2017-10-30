package timeTracker;

import java.util.ArrayList;
import java.util.Collection;



public abstract class TaskDecorator extends Task {

	/**
	 * 
	 */
	private static final long serialVersionUID = 11L;

	public TaskDecorator(String description, String name, Project father) {
		super(description, name, father);
	}

	/** 
	 * @uml.property name="task"
	 * @uml.associationEnd multiplicity="(0 -1)" aggregation="shared" inverse="taskDecorator:timeTracker.Task"
	 */
	private Collection<Task> task = new ArrayList<Task>();

	/** 
	 * Getter of the property <tt>task</tt>
	 * @return  Returns the task.
	 * @uml.property  name="task"
	 */
	public Collection<Task> getTask() {
		return task;
	}

	/** 
	 * Setter of the property <tt>task</tt>
	 * @param task  The task to set.
	 * @uml.property  name="task"
	 */
	public void setTask(Collection<Task> task) {
		this.task = task;
	}

}
