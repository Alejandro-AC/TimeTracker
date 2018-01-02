package timetracker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Abstract class that generates a report of a Project. It displays some
 * details about all the Activities that are part of the selected Project.
 */
public abstract class Report implements Visitor {

	public static final int MILISECONDS_IN_SECOND = 1000;
	
	/**
	 * @uml.property name="project"
	 * @uml.associationEnd multiplicity="(1 1)"
	 *                     inverse="reports:timetracker.Project"
	 */
	private Project project = null;
	
	/**
	 * Getter of the property <tt>project</tt>
	 * 
	 * @return Returns the projects.
	 * @uml.property name="project"
	 */
	public final Project getProject() {
		return this.project;
	}

	/**
	 * @uml.property name="format"
	 * @uml.associationEnd multiplicity="(1 1)"
	 *                     inverse="report:timetracker.Format"
	 */
	private Format format = null;

	/**
	 * Getter of the property <tt>format</tt>
	 * 
	 * @return Returns the format.
	 * @uml.property name="format"
	 */
	public final Format getFormat() {
		return this.format;
	}

	/**
	 * @uml.property name="elements"
	 * @uml.associationEnd multiplicity="(1 -1)" aggregation="shared"
	 *                     inverse="report:timetracker.Element"
	 */
	private Collection<Element> elements = new ArrayList<Element>();

	/**
	 * Getter of the property <tt>elements</tt>
	 * 
	 * @return Returns the elements.
	 * @uml.property name="elements"
	 */
	public final Collection<Element> getElements() {
		return this.elements;
	}
	
	/**
	 * @uml.property name="startDate"
	 */
	private Date startDate;

	/**
	 * Getter of the property <tt>startDate</tt>
	 * 
	 * @return Returns the startDate.
	 * @uml.property name="startDate"
	 */
	public final Date getStartDate() {
		
		Date dateStart = new Date(
			((this.startDate.getTime() 
					+ (MILISECONDS_IN_SECOND / 2)) / MILISECONDS_IN_SECOND)
					* MILISECONDS_IN_SECOND);			
		return dateStart;
	}

	/**
	 * @uml.property name="endDate"
	 */
	private Date endDate;

	/**
	 * Getter of the property <tt>endDate</tt>
	 * 
	 * @return Returns the endDate.
	 * @uml.property name="endDate"
	 */
	public final Date getEndDate() {
		Date dateEnd = new Date(
				((this.endDate.getTime() 
						+ (MILISECONDS_IN_SECOND / 2)) 
						/ MILISECONDS_IN_SECOND)
						* MILISECONDS_IN_SECOND);			
		return dateEnd;
	}
	
	/**
	 * @uml.property  name="intersectionTime"
	 */
	private long intersectionTime = 0;

	/**
	 * Getter of the property <tt>intersectionTime</tt>
	 * @return  Returns the intersectionTime.
	 * @uml.property  name="intersectionTime"
	 */
	public final long getIntersectionTime() {
		return this.intersectionTime;
	}

	/**
	 * Setter of the property <tt>intersectionTime</tt>
	 * @param intersectionTime  The intersectionTime to set.
	 * @uml.property  name="intersectionTime"
	 */
	public final void setIntersectionTime(final long intersectionTimeSet) {
		this.intersectionTime = intersectionTimeSet;
	}
		
	/**
	 * @uml.property  name="durationFormat"
	 */
	private DateFormat durationFormat = 
			new SimpleDateFormat("HH'h' mm'm' ss's'");
		
	/**
	 * Getter of the property <tt>durationFormat</tt>
	 * @return  Returns the durationFormat.
	 * @uml.property  name="durationFormat"
	 */
	public final DateFormat getDurationFormat() {
		return this.durationFormat;
	}

	/**
	 * @uml.property  name="dateFormat"
	 */
	private DateFormat dateFormat =
			new SimpleDateFormat("yyyy/MM/dd, HH:mm'h'");

	/**
	 * Getter of the property <tt>dateFormat</tt>
	 * @return  Returns the dateFormat.
	 * @uml.property  name="dateFormat"
	 */
	public final DateFormat getDateFormat() {
		return this.dateFormat;
	}
	
	/**
	 * @uml.property  name="reportName"
	 */
	private String reportName;

	/**
	 * Getter of the property <tt>reportName</tt>
	 * @return  Returns the reportName.
	 * @uml.property  name="reportName"
	 */
	public final String getReportName() {
		return reportName;
	}
	
	public Report(final Project projectSet, final Format formatSet, 
			final Date startDateSet, final Date endDateSet, 
			final String reportNameSet) {
		this.project = projectSet;
		this.format = formatSet;
		this.startDate = startDateSet;
		this.endDate = endDateSet;
		this.reportName = reportNameSet;
	}
	
	/**
	 * @param level: level in the tree of the current Interval.
	 */
	public abstract void visitInterval(Interval interval, int level);
	
	/**
	 * @param level: level in the tree of the current Task.
	 */
	public abstract void visitTask(Task task, int level);
	
	/**
	 * @param level: level in the tree of the current Project.
	 */
	public abstract void visitProject(Project projectVisited, int level);

	public abstract void generateReport();	

}
