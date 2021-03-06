package timetracker;

/**
 * The Format in which the Report is generated. Every Format
 * has it's own properties and ways of being generated.
 */
public abstract class Format {

	/**
	 * @uml.property name="report"
	 * @uml.associationEnd multiplicity="(1 1)"
	 *                     inverse="format:timetracker.Report"
	 */
	private Report report = null;

	/**
	 * Getter of the property <tt>report</tt>
	 * @return Returns the report.
	 * @uml.property name="report"
	 */
	public final Report getReport() {
		return this.report;
	}
		
	/**
	 * Setter of the property <tt>report</tt>
	 * @param report The report to set.
	 * @uml.property name="report"
	 */
	public final void setReport(final Report reportSet) {
		this.report = reportSet;
	}
	
	public abstract void applyFormat();

}
