package timetracker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public abstract class Report implements Visitor {

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
		 * Setter of the property <tt>elements</tt>
		 * 
		 * @param elements
		 *            The elements to set.
		 * @uml.property name="elements"
		 */
		public final void setElements(final Collection<Element> elementsSet) {
			this.elements = elementsSet;
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
			return startDate;
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
			return endDate;
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
	 * Constructor of the class.
	 */
	public Report(final Project projectSet, final Format formatSet, 
			final Date startDateSet, final Date endDateSet, final String reportNameSet) {
		this.project = projectSet;
		this.format = formatSet;
		this.startDate = startDateSet;
		this.endDate = endDateSet;
		this.reportName = reportNameSet;
	}
		
	public abstract void visitInterval(Interval interval, int level);

	public abstract void visitTask(Task task, int level);

	public abstract void visitProject(Project projectVisited, int level);

	public abstract void generateReport();

	/**
	 * @uml.property  name="reportName"
	 */
	private String reportName;

	/**
	 * Getter of the property <tt>reportName</tt>
	 * @return  Returns the reportName.
	 * @uml.property  name="reportName"
	 */
	public String getReportName() {
		return reportName;
	}

	/**
	 * Setter of the property <tt>reportName</tt>
	 * @param reportName  The reportName to set.
	 * @uml.property  name="reportName"
	 */
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
		
	

}
