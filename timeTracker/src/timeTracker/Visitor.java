package timeTracker;

/**
 * This class is used as an Interface for the Impresor class. 
 */
public interface Visitor {
	// Declatarion of the specific visiting methods that are needed to print
	// the Activities tree.
	
	public void visitInterval(Interval interval, int level);
	
	public void visitTask(Task task, int level);
	
	public void visitProject(Project project, int level);
	
}
