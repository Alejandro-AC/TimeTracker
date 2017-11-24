package timetracker;


public class Brief extends Report {
	

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


	@Override
	public final void visitInterval(final Interval interval, final int level) {
		//if (interval.)
	}

	@Override
	public final void visitTask(final Task task, final int level) {
		// Nothing to do.		
	}

	@Override
	public final void visitProject(final Project project, final int level) {
		this.intersectionTime = 0;
		
		// Coger datos
	}

}
