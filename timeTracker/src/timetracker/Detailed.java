package timetracker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Type of Report. It focuses on detailed information of the Root
 * Projects of the Activities Tree and the Activities that it contains
 * between a given range of time.
 */
public class Detailed extends Report {	
	
	/**
	 * @uml.property  name="logger"
	 */
	private static Logger logger = LoggerFactory.getLogger(Detailed.class);
	
	private Table projectsTable;
	private Table tasksTable;
	private Table intervalsTable;
	
	private static final long SECONDS_IN_HOUR = 3600;
	private static final long MINUTES_IN_HOUR = 60;
	
	public Detailed(final Project projectSet, final Format formatSet,
			final Date startDateSet, final Date endDateSet, 
			final String reportNameSet) {
		super(projectSet, formatSet, startDateSet, endDateSet, reportNameSet);
	}

	public final void generateReport() {
		logger.debug("generating detailed report");
		getElements().add(new Line());
		getElements().add(new Title("Detailed report"));
		getElements().add(new Line());
		
		getElements().add(new SubTitle("Period"));
			// Period Table
		logger.debug("creating period table");
		ArrayList<String> periodFields = new ArrayList<String>();
		periodFields.add("");
		periodFields.add("Date");
		
		Table periodTable = new Table(periodFields);
		
		ArrayList<String> row = new ArrayList<String>();
		row.add("From");
		row.add(getDateFormat().format(getStartDate()));
		periodTable.addRow(row);
		
		row = new ArrayList<String>();
		row.add("Until");
		row.add(getDateFormat().format(getEndDate()));
		periodTable.addRow(row);
		
		row = new ArrayList<String>();
		row.add("Report generation date");
		Date currentDate = new Date();
		row.add(getDateFormat().format(currentDate));
		periodTable.addRow(row);
		
		getElements().add(periodTable);
	
		
		getDateFormat().format(getStartDate());
	
		getElements().add(new Line());
		
		getElements().add(new SubTitle("Root Projects"));
		
			// Creating table to save rootProjects' data
		logger.debug("creating root projects table");
		ArrayList<String> rootProjectsFields = new ArrayList<String>();
		rootProjectsFields.add("Father Project");
		rootProjectsFields.add("Project");
		rootProjectsFields.add("Start date");
		rootProjectsFields.add("End date");
		rootProjectsFields.add("Total time");
		this.projectsTable = new Table(rootProjectsFields);	
		
		for (Activity rootProject : getProject().getChildren()) {
			logger.debug("visiting a new root project");
			rootProject.acceptVisitor(this, 0);
		}
		
		getElements().add(this.projectsTable);
		
		
		getElements().add(new Line());
		
		getElements().add(new SubTitle("subProjects"));
		
		getElements().add(new TextElement("In the next table there are"
				 + " only included the subprojects that have some task"
				 + " with some interval within the period:"));
		getElements().add(new TextElement(""));
		
			// Creating table to save subProjects' data
		logger.debug("creating subprojects table");
		ArrayList<String> subProjectsFields = new ArrayList<String>();
		subProjectsFields.add("Father Project");
		subProjectsFields.add("Project");
		subProjectsFields.add("Start date");
		subProjectsFields.add("End date");
		subProjectsFields.add("Total time");
		this.projectsTable = new Table(subProjectsFields);			
		
		// Recursion to create subProjects table
		generateSubProjectsTable(getProject().getChildren());		
		getElements().add(this.projectsTable);
		
		
		getElements().add(new Line());
		
		getElements().add(new SubTitle("Tasks"));
		
		getElements().add(new TextElement("In the next table there are"
				 + " included the duration of every task and the"
				 + " project they belong to:"));
		getElements().add(new TextElement(""));
		
			// Creating table to save tasks data
		logger.debug("creating root projects table");
		ArrayList<String> tasksFields = new ArrayList<String>();
		tasksFields.add("Father Project");
		tasksFields.add("Task");
		tasksFields.add("Start date");
		tasksFields.add("End date");
		tasksFields.add("Total time");
		this.tasksTable = new Table(tasksFields);			
		
		// Recursion to create tasks table
		generateTasksTable(getProject().getChildren());	
		getElements().add(this.tasksTable);
				
		getElements().add(new Line());		
		getElements().add(new SubTitle("Intervals"));		
		getElements().add(new TextElement("In the next table there are"
				+ " included the start Date, end Date and duration of"
				+ " every interval within the specified time. Also the"
				+ " task and project they belong to:"));
		getElements().add(new TextElement(""));
		
			// Creating table to save intervals data
		logger.debug("creating root projects table");
		ArrayList<String> intervalsFields = new ArrayList<String>();
		intervalsFields.add("Task's Father Project");
		intervalsFields.add("Task");
		intervalsFields.add("Interval");
		intervalsFields.add("Start date");
		intervalsFields.add("End date");
		intervalsFields.add("Duration");
		this.intervalsTable = new Table(intervalsFields);			
		
		// Recursion to create tasks table
		generateIntervalsTable(getProject().getChildren());	
		getElements().add(this.intervalsTable);
		
		getElements().add(new Line());
		getElements().add(new TextElement("Time Tracker v1.0"));	
		
		// Apply format
		getFormat().setReport(this);
		getFormat().applyFormat();
	}

	@Override
	public final void visitInterval(final Interval interval, final int level) {
		long time;
		// Interval doesn't start after period or ends before period then valid
		if (interval.getStartDate().before(getEndDate())
				&& interval.getEndDate().after(getStartDate())) {
			logger.debug("visiting a valid interval");
			// It's a valid interval
			
			Date intervalStart = new Date(
					((interval.getStartDate().getTime() 
							+ (MILISECONDS_IN_SECOND / 2)) 
							/ MILISECONDS_IN_SECOND)
							* MILISECONDS_IN_SECOND);
			
			Date intervalEnd = new Date(
					((interval.getEndDate().getTime() 
							+ (MILISECONDS_IN_SECOND / 2)) 
							/ MILISECONDS_IN_SECOND)
							* MILISECONDS_IN_SECOND);			

			// Interval starts before period and ends after period
			if (interval.getStartDate().before(getStartDate())
					&& interval.getEndDate().after(getEndDate())) {
				
				time = getEndDate().getTime()
						- getStartDate().getTime();
				
				// Interval starts before period and ends before period
			} else if (interval.getStartDate().before(getStartDate())
					&& interval.getEndDate().before(getEndDate())) {
				
				time = intervalEnd.getTime()
						- getStartDate().getTime();
				
				// Interval starts after period and ends after period 
			} else if (interval.getStartDate().after(getStartDate())
					&& interval.getEndDate().after(getEndDate())) {	
				
				time = getEndDate().getTime()
						- intervalStart.getTime();
				
				// Interval starts after period and ends before period 
			} else {
				
				time = intervalEnd.getTime()
						- intervalStart.getTime();
			}
			
			logger.debug("getting intersection time of the interval");
			
			if (level != 0) {
				logger.debug("sum until now:" + getIntersectionTime());
				logger.debug("adding:" + time);
				setIntersectionTime(getIntersectionTime() 
						+ TimeUnit.MILLISECONDS.toSeconds(time));
			}			
			
			if (level == 0 && time > 0) {
				logger.debug("visiting a interval as root");
				
				ArrayList<String> row = new ArrayList<String>();
				row.add(interval.getTask().getFather().getName());
				row.add(interval.getTask().getName());
				row.add(Integer.toString(interval.getId()));
				row.add(getDateFormat().format(interval.getStartDate()));
				row.add(getDateFormat().format(interval.getEndDate()));
				
				long duration = TimeUnit.MILLISECONDS.toSeconds(time);
				
				long hours = duration / SECONDS_IN_HOUR;
				long minutes = (duration % SECONDS_IN_HOUR) / MINUTES_IN_HOUR;
				long seconds = duration % MINUTES_IN_HOUR;
				String timeString = String.format(" %02d" + "h" + " %02d" 
						+ "m" + " %02d" + "s", hours, minutes, seconds);
				
				row.add(timeString);
	
				this.intervalsTable.addRow(row);
			}
			
		}
	}

	@Override
	public final void visitTask(final Task task, final int level) {
		if (level == 0) {
			logger.debug("visiting a task as root" 
					+ " restarting the intersection time");
			setIntersectionTime(0);
		}					
			
		if (!task.getStartDate().after(getEndDate())
				&& !task.getEndDate().before(getStartDate())) {
			// It's a valid task, we can visit its intervals
			logger.debug("visiting a valid task");
			for (Interval child : task.getChildren()) {
				child.acceptVisitor(this, level + 1);
			}
			
			
			if (level  == 0 && getIntersectionTime() > 0) {
					// Adding task data
				logger.debug("generating data table " 
						+ "for the current root task");
				ArrayList<String> row = new ArrayList<String>();
				row.add(task.getFather().getName());
				row.add(task.getName());
				row.add(getDateFormat().format(task.getStartDate()));
				row.add(getDateFormat().format(task.getEndDate()));
				
				long hours = getIntersectionTime() / SECONDS_IN_HOUR;
				long minutes = (getIntersectionTime() % SECONDS_IN_HOUR) 
						/ MINUTES_IN_HOUR;
				long seconds = getIntersectionTime() % MINUTES_IN_HOUR;
				String timeString = String.format(" %02d" + "h" + " %02d" 
						+ "m" + " %02d" + "s", hours, minutes, seconds);
				
				row.add(timeString);
	
				this.tasksTable.addRow(row);
			}			
		}		
	}

	@Override
	public final void visitProject(final Project project, final int level) {
		if (level == 0) {
			logger.debug("visiting a root project, " 
					+ "restarting the intersection time");
			setIntersectionTime(0);
		}		

		if (project.getStartDate().before(getEndDate())
				&& project.getEndDate().after(getStartDate())) {
			// It's a valid project, we can visit its activities
			logger.debug("visiting a valid project");
			for (Activity child : project.getChildren()) {
				child.acceptVisitor(this, level + 1);
			}			
			
			if (level == 0 && getIntersectionTime() > 0) {
					// Adding project data
				logger.debug("generating data table " 
						+ "for the current root project");
				ArrayList<String> row = new ArrayList<String>();
				row.add(project.getFather().getName()); 
				row.add(project.getName());
				row.add(getDateFormat().format(project.getStartDate()));
				row.add(getDateFormat().format(project.getEndDate()));
				
				long hours = getIntersectionTime() / SECONDS_IN_HOUR;
				long minutes = (getIntersectionTime() % SECONDS_IN_HOUR) 
						/ MINUTES_IN_HOUR;
				long seconds = getIntersectionTime() % MINUTES_IN_HOUR;
				String timeString = String.format(" %02d" + "h" + " %02d" 
						+ "m" + " %02d" + "s", hours, minutes, seconds);
				
				row.add(timeString);
	
				this.projectsTable.addRow(row);
			}			
		}
	}

	public final void generateSubProjectsTable(
			final Collection<Activity> activities) {
		for (Activity activity : activities) {
			if (activity instanceof Project) {
			Collection<Activity> subActivities = activity.getChildren();	
			generateSubProjectsTable(subActivities);
				for (Activity subActivity : subActivities) {			
					if (subActivity instanceof Project) {					
						logger.debug("visiting a new sub project");
						subActivity.acceptVisitor(this, 0);						
					}			
				}		
			}
		}
	}
	
	public final void generateTasksTable(
			final Collection<Activity> activities) {
		for (Activity activity : activities) {
			if (activity instanceof Project) {
				Collection<Activity> subActivities = activity.getChildren();	
				generateTasksTable(subActivities);
				for (Activity subActivity 
						: subActivities) {							
					if (subActivity instanceof Task) {
						logger.debug("visiting a new task");
						subActivity.acceptVisitor(this, 0);
					}
				}		
			}
		}
	}
	
	public final void generateIntervalsTable(
			final Collection<Activity> activities) {
		for (Activity activity : activities) {
			if (activity instanceof Project) {
				Collection<Activity> subActivities = activity.getChildren();	
				generateIntervalsTable(subActivities);
			} else {
				if (activity instanceof Task) {	
					Collection<Interval> intervals = activity.getChildren();
					for (Interval interval : intervals) {				
						logger.debug("visiting a new interval");
						interval.acceptVisitor(this, -1);		
					}
				}		
			}
		}
	}


}
