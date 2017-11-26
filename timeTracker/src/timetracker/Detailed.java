package timetracker;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Detailed extends Report {

	/**
	 * @uml.property  name="logger"
	 */
	private static Logger logger = LoggerFactory.getLogger(Detailed.class);
	
	public Detailed(final Project projectSet, final Format formatSet,
			final Date startDateSet, final Date endDateSet) {
		super(projectSet, formatSet, startDateSet, endDateSet);
	}

	@Override
	public final void visitInterval(final Interval interval, final int level) {
		
	}

	@Override
	public final void visitTask(final Task task, final int level) {

	}

	@Override
	public final void visitProject(final Project project, final int level) {
		
	}

	@Override
	public void generateReport() {
		// TODO Auto-generated method stub
		
	}


}
