package timetracker;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Brief extends Report {
	
	public final void generateReport() {
		getElements().add(new Line());
		getElements().add(new TextElement("Informe breu"));
		getElements().add(new Line());
		
		getElements().add(new TextElement("Període"));
			// Table periode
		/* Table info
		getStartDate();
		getEndDate();
		Date currentDate = new Date();
		*/
			// getElements().add(new Table(/data/));
	
		getElements().add(new Line());
		
		getElements().add(new TextElement("Projectes arrel"));
			// Table projectes arrel
		/*
		for (Activity rootProject : getProject().getChildren()) {
			rootProject.acceptVisitor(this, 0);
		}
		*/
		
		getElements().add(new Line());
		getElements().add(new TextElement("Time Tracker v1.0"));
	}

	@Override
	public final void visitInterval(final Interval interval, final int level) {
		long time;
		if (interval.getStartDate().before(getEndDate())
				&& interval.getEndDate().after(getStartDate())) {
			// It's a valid interval

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
			setIntersectionTime(getIntersectionTime() 
					+ TimeUnit.MILLISECONDS.toSeconds(time));
		}
	}

	@Override
	public final void visitTask(final Task task, final int level) {
		if (task.getStartDate().before(getEndDate())
				&& task.getEndDate().after(getStartDate())) {

			// It's a valid task, we can visit its intervals
			for (Interval child : task.getChildren()) {
				child.acceptVisitor(this, level + 1);
			}
		}
	}

	@Override
	public final void visitProject(final Project project, final int level) {
		// Is this correct?
		if (level == 0) {
			setIntersectionTime(0);
		}		

		if (project.getStartDate().before(getEndDate())
				&& project.getEndDate().after(getStartDate())) {
			// It's a valid project, we can visit its activities
			for (Activity child : project.getChildren()) {
				child.acceptVisitor(this, level + 1);
			}			

			// Get data : TODO
			/*
			project.getName();
			project.getStartDate();
			project.getEndDate();
			this.getIntersectionTime();
			getElements().add(new Table(/data/));
			*/
		}

	}

}
