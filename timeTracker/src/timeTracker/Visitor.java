package timeTracker;

public interface Visitor {
	
	public void visitInterval(Interval interval, int level);
	
	public void visitTask(SimpleTask simpleTask, int level);
	
	public void visitProject(Project project, int level);
	
}
