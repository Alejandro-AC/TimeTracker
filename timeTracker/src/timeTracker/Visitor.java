package timetracker;

/**
 * This class is used as an Interface for the Impresser class. 
 */
public interface Visitor {
	// Declaration of the specific visiting methods that are needed to print
	// the Activities tree.
	
	void visitInterval(Interval interval, int level);
	
	void visitTask(Task task, int level);
	
	void visitProject(Project project, int level);
	
}
