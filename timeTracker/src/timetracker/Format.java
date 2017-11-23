package timetracker;


public abstract class Format {

	/** 
	 * @uml.property name="report"
	 * @uml.associationEnd multiplicity="(1 1)" inverse="format:timetracker.Report"
	 */
	private Report report = null;

	/** 
	 * Getter of the property <tt>report</tt>
	 * @return  Returns the report.
	 * @uml.property  name="report"
	 */
	public Report getReport() {
		return report;
	}

	/** 
	 * Setter of the property <tt>report</tt>
	 * @param report  The report to set.
	 * @uml.property  name="report"
	 */
	public void setReport(Report report) {
		this.report = report;
	}

}
