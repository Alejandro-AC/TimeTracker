package timetracker;

import java.util.Collection;


public abstract class Report {

	/**
	 * @uml.property  name="project"
	 * @uml.associationEnd  multiplicity="(1 1)" inverse="report:timetracker.Project"
	 */
	private Project project = null;

	/**
	 * Getter of the property <tt>project</tt>
	 * @return  Returns the project.
	 * @uml.property  name="project"
	 */
	public Project getProject() {
		return project;
	}

	/**
	 * Setter of the property <tt>project</tt>
	 * @param project  The project to set.
	 * @uml.property  name="project"
	 */
	public void setProject(Project project) {
		this.project = project;
	}

	/**
	 * @uml.property  name="format"
	 * @uml.associationEnd  multiplicity="(1 1)" inverse="report:timetracker.Format"
	 */
	private Format format = null;

	/**
	 * Getter of the property <tt>format</tt>
	 * @return  Returns the format.
	 * @uml.property  name="format"
	 */
	public Format getFormat() {
		return format;
	}

	/**
	 * Setter of the property <tt>format</tt>
	 * @param format  The format to set.
	 * @uml.property  name="format"
	 */
	public void setFormat(Format format) {
		this.format = format;
	}

	/** 
	 * @uml.property name="element"
	 * @uml.associationEnd multiplicity="(1 -1)" aggregation="shared" inverse="report:timetracker.Element"
	 */
	private Collection element;

	/** 
	 * Getter of the property <tt>element</tt>
	 * @return  Returns the element.
	 * @uml.property  name="element"
	 */
	public Collection getElement() {
		return element;
	}

	/** 
	 * Setter of the property <tt>element</tt>
	 * @param element  The element to set.
	 * @uml.property  name="element"
	 */
	public void setElement(Collection element) {
		this.element = element;
	}

}
