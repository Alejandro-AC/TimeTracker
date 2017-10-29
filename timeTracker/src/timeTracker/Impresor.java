package timeTracker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;

public class Impresor implements Visitor, Runnable{
	
	private static Impresor uniqueInstance = new Impresor();
	private volatile boolean running = true;
	
	public Impresor(){
		
	}
	
	public static Impresor getInstance() {
		return uniqueInstance;
	}
	
    public void terminate() {
        running = false;
    }
    
    public void reanudate() {
        running = true;
    }
	
	private Collection<Project> rootProjects = new ArrayList<Project>();
	
	public void visitInterval(Interval interval, int level){		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String type = "I.";
		String format = "  %" + ((level*2)+2) + "s%-" + (25-(level*2)) + "s%" + (30) + "s%" + (30) + "s%" + (15) + "ss%n";	
		
		if (interval.getStartDate() == null || interval.getEndDate() == null)
		{
			System.out.printf(format, type, interval.getId(), " ", " ", interval.getTotalTime());
		}else{
			System.out.printf(format, type, interval.getId(), dateFormat.format(interval.getStartDate()), dateFormat.format(interval.getEndDate()), interval.getTotalTime());
		}
			
	}
	
	public void visitTask(Task task, int level){
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String type = "T.";
		String format = "  %" + ((level*2)+2) + "s%-" + (25-(level*2)) + "s%" + (30) + "s%" + (30) + "s%" + (15) + "ss%n";
		
		if (task.getStartDate() == null || task.getEndDate() == null)
		{
			System.out.printf(format, type, task.getName(), " ", " ", task.getTotalTime());
		}else{
			System.out.printf(format, type, task.getName(), dateFormat.format(task.getStartDate()), dateFormat.format(task.getEndDate()), task.getTotalTime());
		}
			
		
	}
	
	public void visitProject(Project project, int level){	
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String type = "P.";
		String format = "  %" + ((level*2)+2) + "s%-" + (25-(level*2)) + "s%" + (30) + "s%" + (30) + "s%" + (15) + "ss%n";	
		
		if (project.getStartDate() == null || project.getEndDate() == null)
		{
			System.out.printf(format, type, project.getName(), " ", " ", project.getTotalTime());
		}else{
			System.out.printf(format, type, project.getName(), dateFormat.format(project.getStartDate()), dateFormat.format(project.getEndDate()), project.getTotalTime());
		}
		
		
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
		
		System.out.println("");
		System.out.print("Enter 1 to see the menu or 0 to Exit: ");		
		System.out.println("");
		System.out.println("");
	}
	

	@Override
	public void run() {		
	  while(running) {			   
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
