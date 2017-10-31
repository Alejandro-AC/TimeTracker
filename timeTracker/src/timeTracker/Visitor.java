package timeTracker;

public interface Visitor {
	
	public void visitInterval(Interval interval, int level);
	
	public void visitTask(Task task, int level);
	
	public void visitProject(Project project, int level);
	
}
