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
		String type = "I. ";
		System.out.println(new String(spaces) + type + interval.getId() +
				"  ..StartTime.." + interval.getStartDate() +
				"  ..EndTime.." + interval.getEndDate() +
				" ..TotalTime.." + interval.getTotalTime() + "s " );		
	}
	
	public void visitTask(Task task, int level){
		char[] spaces = new char[level*2];
		Arrays.fill(spaces, ' ');
		String type = "T. ";
		System.out.println(new String(spaces) + type + task.getName() +				
				"  ..StartTime.." + task.getStartDate() +
				"  ..EndTime.." + task.getEndDate() +
				" ..TotalTime.." + task.getTotalTime() + "s " );
	}
	
	public void visitProject(Project project, int level){		
		char[] spaces = new char[level*2];
		Arrays.fill(spaces, ' ');
		String type = "P. ";
		System.out.println(new String(spaces) + type + project.getName() +
				"  ..StartTime.." + project.getStartDate() +
				"  ..EndTime.." + project.getEndDate() +
				" ..TotalTime.." + project.getTotalTime() + "s " );		
	}	
	
	public void imprimeix(){
		Stack<Activity> nonVisited = new Stack<Activity>();
		nonVisited.addAll(rootProjects);
		System.out.println("");
		System.out.println("  - TREE -");
		System.out.println("");
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
