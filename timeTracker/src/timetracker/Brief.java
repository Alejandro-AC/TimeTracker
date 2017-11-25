package timetracker;

import java.util.concurrent.TimeUnit;


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
		long time;
		
		if (interval.getStartDate().before(getEndDate()) 
				&& interval.getEndDate().after(getStartDate())) {			
			// Valid interval
			
			if (interval.getStartDate().before(getStartDate()) 
					&& interval.getEndDate().after(getEndDate())) {
				// I4
				time = interval.getEndDate().getTime() 
						- interval.getStartDate().getTime();				
			} else if (interval.getStartDate().before(getStartDate())) {
				// I1
				time = interval.getEndDate().getTime() 
						- getStartDate().getTime();			
			} else if (interval.getEndDate().after(getEndDate())) {
				// I3
				time = getEndDate().getTime() 
						- interval.getStartDate().getTime();
			} else {
				// I2
				time = interval.getEndDate().getTime() 
						- interval.getStartDate().getTime();
			}			
			this.intersectionTime += TimeUnit.MILLISECONDS.toSeconds(time);		
		}
	}

	@Override
	public final void visitTask(final Task task, final int level) {
		if (task.getStartDate().before(getEndDate()) 
				&& task.getEndDate().after(getStartDate())) {
			
			// Valid task
			for (Interval child : task.getChildren()) {
				child.acceptVisitor(this, level + 1);
			}
		}
	}

	@Override
	public final void visitProject(final Project project, final int level) {
		this.intersectionTime = 0;
		
		if (project.getStartDate().before(getEndDate()) 
				&& project.getEndDate().after(getStartDate())) {
			// Valid project
			for (Activity child : project.getChildren()) {
				child.acceptVisitor(this, level + 1);
			}
			
			// Get data : TODO
		}

	}

}
