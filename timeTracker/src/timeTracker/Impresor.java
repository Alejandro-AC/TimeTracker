package timeTracker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Stack;

public class Impresor implements Visitor, Runnable{

	private static Impresor uniqueInstance = new Impresor();
	
	public Impresor(){
		
	}
	
	public static Impresor getInstance() {
		return uniqueInstance;
	}
	
	private Collection<Project> rootProjects = new ArrayList<Project>();
	
	public void visitInterval(Interval interval, int level){		
		char[] spaces = new char[level*2];
		Arrays.fill(spaces, ' ');
		String type = "I.";
		String format = "  %" + ((level*2)+2) + "s%-" + (25-(level*2)) + "s%" + (30) + "s%" + (30) + "s%" + (15) + "ss%n";	
		System.out.printf(format, type, interval.getId(), interval.getStartDate(), interval.getEndDate(), interval.getTotalTime());	
	}
	
	public void visitTask(Task task, int level){
		char[] spaces = new char[level*2];
		Arrays.fill(spaces, ' ');
		String type = "T.";
		String format = "  %" + ((level*2)+2) + "s%-" + (25-(level*2)) + "s%" + (30) + "s%" + (30) + "s%" + (15) + "ss%n";	
		System.out.printf(format, type, task.getName(), task.getStartDate(), task.getEndDate(), task.getTotalTime());
		//System.out.println(new String(spaces) + type + task.getName() +				
		//		"  ..StartTime.." + task.getStartDate() +
		//		"  ..EndTime.." + task.getEndDate() +
		//		" ..TotalTime.." + task.getTotalTime() + "s " );
	}
	
	public void visitProject(Project project, int level){		
		char[] spaces = new char[level*2];
		Arrays.fill(spaces, ' ');
		String type = "P.";
		String format = "  %" + ((level*2)+2) + "s%-" + (25-(level*2)) + "s%" + (30) + "s%" + (30) + "s%" + (15) + "ss%n";	
		System.out.printf(format, type, project.getName(), project.getStartDate(), project.getEndDate(), project.getTotalTime());
	}	
	
	public void imprimeix(){
		Stack<Activity> nonVisited = new Stack<Activity>();
		nonVisited.addAll(rootProjects);
		String title = "- ACTIVITIES TREE -";
		String activityName = "Activity Name";
		String startDate = "Start Date";
		String endTime = "End Time";
		String totalTime = "Total Time";
		System.out.println("");
		System.out.println(title);
		System.out.println("");
		String format = "%10s%" + (5) + "s%" + (30) + "s%" + (30) + "s%" + (20) + "s%n";		
		System.out.printf(format, " ", activityName, startDate, endTime, totalTime);	
		for (Project root : rootProjects) {
			root.acceptVisitor(uniqueInstance, 0);
			System.out.println("");
		}
	}
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
	  while(true) {			   
		   try {
			Thread.sleep(this.reprintTime*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   this.imprimeix();
	  }
	}


	/**
	 * @uml.property  name="reprintTime"
	 */
	private long reprintTime = 1;


	/**
	 * Setter of the property <tt>reprintTime</tt>
	 * @param reprintTime  The reprintTime to set.
	 * @uml.property  name="reprintTime"
	 */
	public void setReprintTime(long reprintTime) {
		this.reprintTime = reprintTime;
	}


	public void setRootProjects(Collection<Project> rootProjects) {
		this.rootProjects = rootProjects;
	}



}
