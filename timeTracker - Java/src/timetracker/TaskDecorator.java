package timetracker;

import java.util.Collection;
import java.util.Observable;

/**
 * Abstract class used to implement a Decorator for SimpleTask.
 */
public abstract class TaskDecorator extends Task {

	private static final long serialVersionUID = 5L;

	/**
	 * @uml.property name="task"
	 * @uml.associationEnd multiplicity="(1 1)" aggregation="shared"
	 *                     inverse="taskDecorator:timetracker.Task"
	 */
	private Task task = null;

	/**
	 * Getter of the property <tt>task</tt>
	 * 
	 * @return Returns the task.
	 * @uml.property name="task"
	 */
	protected final Task getTask() {
		return task;
	}

	public TaskDecorator(final String description, final String name,
			final Project father, final Task decoratedTask) {
		super(description, name, father);
		this.task = decoratedTask;
	}

	// The following methods are the implementation of the
	// Task.java abstract methods. Every methods calls its
	// father's method, so when it reaches a SimpleTask this is executed.
	// In the case of the getters, we want to reach the SimpleTask so
	// it will return it's variable.

	public final void acceptVisitor(final Visitor visitor, final int level) {
		this.task.acceptVisitor(visitor, level);
	}

	public final Collection<Interval> getChildren() {
		return this.task.getChildren();
	}

	public final void calculateTotalTime() {
		this.task.calculateTotalTime();
	}

	public void start() {
	}

	public final Interval getIntervalById(final int id) {
		return this.task.getIntervalById(id);
	}

	public final Interval getLastInterval() {
		return this.task.getLastInterval();
	}

	public final boolean removeInterval(final int id) {
		return this.task.removeInterval(id);
	}

	public final void stop() {
		this.task.stop();
	}

	public final boolean childrenIsEmpty() {
		return this.task.childrenIsEmpty();
	}

	public void update(final Observable arg0, final Object arg1) {
	}

	public final String getName() {
		return this.task.getName();
	}

	public final long getTotalTime() {
		return this.task.getTotalTime();
	}
}
