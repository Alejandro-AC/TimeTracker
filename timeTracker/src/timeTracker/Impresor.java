package timetracker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to print the Activities Tree of the system.
 */
public class Impresor implements Visitor, Runnable {

	/**
	 * Logger for the class.
	 */
	private static Logger logger = LoggerFactory.getLogger(Impresor.class);

	/**
	 * Getter of the property <tt>logger</tt>
	 * 
	 * @return Returns the Logger.
	 * @uml.property name="logger"
	 */
	public static final Logger getLogger() {
		return logger;
	}

	/**
	 * Unique instance of the Impresor for all the system.
	 */
	private static Impresor uniqueInstance = new Impresor();

	public static Impresor getInstance() {
		return uniqueInstance;
	}

	/**
	 * Current state of the Impresor.
	 */
	private volatile boolean running = true;

	/**
	 * Root projects of the system.
	 */
	private Collection<Activity> rootProjects = new ArrayList<Activity>();

	public final void setRootProjects(final Collection<Activity> collection) {
		logger.debug("set root projects in impresor");
		this.rootProjects = collection;
	}

	/**
	 * Refresh time for the Impresor.
	 * 
	 * @uml.property name="reprintTime"
	 */
	private long reprintTime = 1;

	/**
	 * Setter of the property <tt>reprintTime</tt>
	 * 
	 * @param reprintTime
	 *            The reprintTime to set.
	 * @uml.property name="reprintTime"
	 */
	public final void setReprintTime(final long reprintTimeSet) {
		logger.debug("set reprint time: " + reprintTimeSet);
		this.reprintTime = reprintTimeSet;
	}

	/**
	 * Constructor of the class.
	 */
	public Impresor() {

	}

	/**
	 * Operations that the Impresor must do after every reprintTime.
	 */
	@Override
	public final void run() {
		final int secConvertTime = 1000;
		logger.debug("impresor running");
		while (running) {
			try {
				Thread.sleep(this.reprintTime * secConvertTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
				logger.error("Error trying to put to sleep the Impresor");
			}
			this.imprimeix();
		}
	}

	/**
	 * Runs off the Impresor.
	 */
	public final void terminate() {
		logger.debug("impresor runing off");
		running = false;
	}

	/**
	 * Reanudates the Impressor.
	 */
	public final void reanudate() {
		logger.debug("set impresor running ON");
		running = true;
	}

	/**
	 * Visits an Interval to print its information.
	 * 
	 * @param interval
	 *            : Interval to print.
	 * @param level
	 *            : level of the current Interval in the tree (used for
	 *            printing).
	 */
	public final void visitInterval(final Interval interval, final int level) {
		final int formatNumber1 = 25;
		final int formatNumber2 = 30;
		final int formatNumber3 = 15;
		logger.debug("visiting interval: " + interval.getId());
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String type = "I.";
		String format = "  %" + ((level * 2) + 2) + "s%-"
				+ (formatNumber1 - (level * 2)) + "s%" + (formatNumber2) + "s%"
				+ (formatNumber2) + "s%" + (formatNumber3) + "ss%n";

		if (interval.getStartDate() == null || interval.getEndDate() == null) {
			System.out.printf(format, type, interval.getId(), " ", " ",
					interval.getTotalTime());
		} else {
			System.out.printf(format, type, interval.getId(),
					dateFormat.format(interval.getStartDate()),
					dateFormat.format(interval.getEndDate()),
					interval.getTotalTime());
		}
	}

	/**
	 * Visits a Task to print its information.
	 * 
	 * @param task
	 *            : Task to print.
	 * @param level
	 *            : level of the current Task in the tree (used for printing).
	 */
	public final void visitTask(final Task task, final int level) {
		final int formatNumber1 = 25;
		final int formatNumber2 = 30;
		final int formatNumber3 = 15;
		logger.debug("visiting interval: " + task.getName());
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String type = "T.";
		String format = "  %" + ((level * 2) + 2) + "s%-"
				+ (formatNumber1 - (level * 2)) + "s%" + (formatNumber2) + "s%"
				+ (formatNumber2) + "s%" + (formatNumber3) + "ss%n";

		if (task.getStartDate() == null || task.getEndDate() == null) {
			System.out.printf(format, type, task.getName(), " ", " ",
					task.getTotalTime());
		} else {
			System.out.printf(format, type, task.getName(),
					dateFormat.format(task.getStartDate()),
					dateFormat.format(task.getEndDate()), task.getTotalTime());
		}

		for (Interval child : task.getChildren()) {
			child.acceptVisitor(this, level + 1);
		}
	}

	/**
	 * Visits a Project to print its information.
	 * 
	 * @param project
	 *            : Project to print.
	 * @param level
	 *            : level of the current Project in the tree (used for
	 *            printing).
	 */
	public final void visitProject(final Project project, final int level) {
		final int formatNumber1 = 25;
		final int formatNumber2 = 30;
		final int formatNumber3 = 15;
		logger.debug("visiting interval: " + project.getName());
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String type = "P.";
		String format = "  %" + ((level * 2) + 2) + "s%-"
				+ (formatNumber1 - (level * 2)) + "s%" + (formatNumber2) + "s%"
				+ (formatNumber2) + "s%" + (formatNumber3) + "ss%n";

		if (project.getStartDate() == null || project.getEndDate() == null) {
			System.out.printf(format, type, project.getName(), " ", " ",
					project.getTotalTime());
		} else {
			System.out.printf(format, type, project.getName(),
					dateFormat.format(project.getStartDate()),
					dateFormat.format(project.getEndDate()),
					project.getTotalTime());
		}

		for (Activity child : project.getChildren()) {
			child.acceptVisitor(this, level + 1);
		}
	}

	/**
	 * Main method for printing.
	 */
	public final void imprimeix() {

		final int formatNumber1 = 30;
		final int formatNumber2 = 20;
		final int formatNumber3 = 5;

		logger.debug("printing activities tree");
		Stack<Activity> nonVisited = new Stack<Activity>();

		String title = "- ACTIVITIES TREE -";
		System.out.println("");
		System.out.println(title);
		System.out.println("");

		if (rootProjects != null) {
			nonVisited.addAll(rootProjects);

			String activityName = "Activity Name";
			String startDate = "Start Date";
			String endTime = "End Time";
			String totalTime = "Total Time";

			String format = "%10s%" + (formatNumber3) + "s%" + (formatNumber1)
					+ "s%" + (formatNumber1) + "s%" + (formatNumber2) + "s%n";
			System.out.printf(format, " ", activityName, startDate, endTime,
					totalTime);
			for (Activity root : rootProjects) {
				root.acceptVisitor(uniqueInstance, 0);
				System.out.println("");
			}
		}
		System.out.println("");
		System.out.print("Enter 1 to see the menu, 2 to run the test A.1, "
				+ "3 to run the test A.2, 0 to Exit: ");
		System.out.println("");
		System.out.println("");
	}

}
