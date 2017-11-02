package timeTracker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to print the Activities Tree of the system.
 */
public class Impresor implements Visitor, Runnable{
	
	/**
	 * Logger for the class.
	 */
	static Logger logger = LoggerFactory.getLogger(Impresor.class);
	
	/**
	 * Unique instance of the Impresor for all the system.
	 */
	private static Impresor uniqueInstance = new Impresor();
	
		public static Impresor getInstance() {
			return uniqueInstance;
		}
	
	/**
	 * Current state of the Impresor. 
	 */
	private volatile boolean running = true;
	
	/**
	 * Root projects of the system.
	 */
	private Collection<Project> rootProjects = new ArrayList<Project>();
	
		public void setRootProjects(Collection<Project> rootProjects) {
			logger.debug("set root projects in impresor");
			this.rootProjects = rootProjects;
		}
	
	/**
	 * Refresh time for the Impresor. 
	 * @uml.property  name="reprintTime"
	 */
	private long reprintTime = 1;

		/**
		 * Setter of the property <tt>reprintTime</tt>
		 * @param reprintTime  The reprintTime to set.
		 * @uml.property  name="reprintTime"
		 */
		public void setReprintTime(long reprintTime) {
			logger.debug("set reprint time: "+reprintTime);
			this.reprintTime = reprintTime;
		}
	
	
	/**
	 * Constructor of the class.
	 */
	public Impresor(){
		
	}	
	
	/**
	 * Operations that the Impresor must do after every reprintTime.
	 */
	@Override
	public void run() {		
	logger.debug("impresor running");
	  while(running) {			   
		   try {
			   Thread.sleep(this.reprintTime*1000);
		   } catch (InterruptedException e) {
			   e.printStackTrace();
			   logger.error("Error trying to put to sleep the Impresor");
		   }
		   this.imprimeix();
	   }	
	}
	
	/**
	 * Runs off the Impresor.
	 */
    public void terminate() {
		logger.debug("impresor runing off");
        running = false;
    }
    
    /**
     * Reanudates the Impressor. 
     */
    public void reanudate() {
		logger.debug("set impresor running ON");
        running = true;
    }
	
    /**
     * Visits an Interval to print its information.
     * @param interval: Interval to print.
     * @param level: level of the current Interval in the tree (used for printing).
     */
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
	
	/**
     * Visits a Task to print its information.
     * @param task: Task to print.
     * @param level: level of the current Task in the tree (used for printing).
     */
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
	
	/**
     * Visits a Project to print its information.
     * @param project: Project to print.
     * @param level: level of the current Project in the tree (used for printing).
     */
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
	
	/**
	 * Main method for printing.
	 */
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
		System.out.print("Enter 1 to see the menu, 2 to run the test A.1, 3 to run the test A.2, 0 to Exit: ");		
		System.out.println("");
		System.out.println("");
	}
	

}
