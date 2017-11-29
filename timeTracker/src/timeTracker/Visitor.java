package timetracker;

/**
 * This class is used as an Interface for the Impresser class.
 */
public interface Visitor {
	// Declaration of the specific visiting methods that are needed to print
	// the Activities tree.	
	/**
	 * @param level: level of the current Interval in the tree.
	 */
	void visitInterval(Interval interval, int level);
	
	/**
	 * @param level: level of the current Task in the tree.
	 */
	void visitTask(Task task, int level);
	
	/**
	 * @param level: level of the current Project in the tree.
	 */
	void visitProject(Project project, int level);

}
