package timetracker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class encapsulates all the information related to a Project.
 * A Project can contain more Activities.
 */
public class Project extends Activity {

	private static Logger logger = LoggerFactory.getLogger(Interval.class);
	private static final long serialVersionUID = 2L;

	/**
	 * @uml.property name="children"
	 * @uml.associationEnd multiplicity="(0 -1)" aggregation="shared"
	 *                     inverse="father:time.Tracker.Activity"
	 */
	private Collection<Activity> children = new ArrayList<Activity>();

	/**
	 * Getter of the property <tt>children</tt>
	 * 
	 * @return Returns the children.
	 * @uml.property name="children"
	 */
	@SuppressWarnings("unchecked")
	public final Collection<Activity> getChildren() {
		logger.debug(
				"getting childrens: " + children + "from project "
						+ this.getName());
		assert this.invariant();
		return this.children;
	}

	/**
	 * Setter of the property <tt>children</tt>
	 * 
	 * @param children
	 *            The children to set.
	 * @uml.property name="children"
	 */
	public final void setChildren(final Collection<Activity> childrenToSet) {
		logger.debug("setting childrens to project " + this.getName());
		this.children = childrenToSet;
		assert this.invariant();
	}

	/**
	 * Searches for the child with the name given and returns it if it has been
	 * found.
	 * @param name: name of the component to be found.
	 */
	public final Activity getChild(final String name) {
		logger.debug(
				"searching child " + name + " in project " + this.getName());
		for (Activity child : children) {
			if (child.getName().equals(name)) {
				assert this.invariant();
				return child;
			}
		}
		assert this.invariant();
		return null;
	}
	
	/**
	 * @uml.property name="reports"
	 * @uml.associationEnd multiplicity="(0 -1)"
	 *                     inverse="project:timetracker.Report"
	 */
	@SuppressWarnings("unused")
	private Collection<Report> reports = new ArrayList<Report>();

	public Project(final String name, final String description,
			final Project father) {
		super(description, name, father);
		assert this.invariant();
	}
	
	private boolean invariant() {
		boolean correct = true;
		for (Object obj:this.getChildren()) {
			if (!(obj instanceof Project) && !(obj instanceof Task)) {
				correct = false;
			}
		}
		
		if (this.getName() == null) {
			correct = false;
		}
		
		return correct;
	}

	/**
	 * Adds a new project to the children list of an existing Project.
	 */
	public final void addChildProject(final Client client) {

		ArrayList<String> properties = new ArrayList<String>();

		properties = askChildProperties(client);
		Project p = new Project(properties.get(0), properties.get(1), this);
		this.children.add(p);
		logger.info("added child project " + p.getName());
		assert this.invariant();
	}

	/**
	 * Adds a new task to the children list of an existing Project.
	 */
	public final void addChildTask(final Client client) {
		ArrayList<String> properties = new ArrayList<String>();
		Scanner scanner = new Scanner(System.in);
		boolean correctType = false;
		boolean correctOption = false;

		properties = askChildProperties(client);

		Task t = new SimpleTask(properties.get(0), properties.get(1), this);

		int limitedIntervalTime = 0;

		while (!correctType) {
			try {
				logger.debug("Introducing interval time option");
				System.out.print("Limited Interval Time (0/1): ");
				limitedIntervalTime = Integer.parseInt(scanner.nextLine());
				logger.debug(
						"interval time option introduced"
								+ " have correct type");
				correctType = true;
			} catch (Exception e) {
				logger.warn(
						"introduced value in interval time option"
								+ " is not a number. Must be 0 or 1.");
				System.out.println("Scheduled option must be 0 or 1."
						+ " Introduce option again.");
			}
		}

		while (!correctOption) {
			if (limitedIntervalTime != 0 && limitedIntervalTime != 1) {
				logger.debug(
						"introduced value in "
								+ "limitedIntervalTime option is not 0 or 1.");
				System.out.println("Interval time option must be 0 or 1");
				System.out.print("Limited Interval Time (0/1): ");
				limitedIntervalTime = Integer.parseInt(scanner.nextLine());
			} else {
				logger.debug("interval time option introduced correctly");
				correctOption = true;
			}
		}

		if (limitedIntervalTime == 1) {
			correctType = false;
			while (!correctType) {
				try {
					logger.debug("Introducing interval time");
					System.out.print("Interval time (seconds): ");
					long limitedTime = Long.parseLong(scanner.nextLine());
					t = new LimitedIntervalTime(properties.get(0),
							properties.get(1), this, t, limitedTime);
					logger.debug(
							"limited time " + limitedTime
									+ " introduced correctly");
					correctType = true;
				} catch (Exception e) {
					System.out.println("Limited interval time"
							+ " must be a number.");
					logger
							.warn("Introduced value in"
									+ " limited interval time is not " 
									+ "a number.");
				}
			}
		}

		int scheduled = 0;
		correctType = false;
		while (!correctType) {
			try {
				logger.debug("introducing scheduled option");
				System.out.print("Scheduled (0/1): ");
				scheduled = Integer.parseInt(scanner.nextLine());
				logger.debug(
						"scheduled option introduced" + " have correct type");
				correctType = true;
			} catch (Exception e) {
				logger.warn(
						"introduced value in scheduled option"
								+ " is not a number. Must be 0 or 1.");
				System.out.println("Scheduled option must be 0 or 1."
						+ " Introduce an option again.");
			}
		}

		correctOption = false;
		while (!correctOption) {
			if (scheduled != 0 && scheduled != 1) {
				logger.debug(
						"introduced value in scheduled option"
								+ " is not 0 or 1.");
				System.out.println("Scheduled option must be 0 or 1");
				System.out.print("Scheduled (0/1): ");
				scheduled = Integer.parseInt(scanner.nextLine());
			} else {
				logger.debug(
						"introducing scheduled option"
								+ " introduced correctly");
				correctOption = true;
			}
		}

		if (scheduled == 1) {
			Date scheduledDate = null;
			correctType = false;
			while (!correctType) {
				try {
					logger.debug(
							"introducing starting date" + " for schedule task");
					System.out.print("Starting date (yyyy-MM-dd HH:mm:ss): ");
					String dateString = scanner.nextLine();
					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					scheduledDate = dateFormat.parse(dateString);
					logger.debug(
							"introduced date " + scheduledDate + " correctly");
					correctType = true;
				} catch (Exception e) {
					logger.debug("Date format incorrect");
					System.out.println("Date format incorrect."
							+ " Introduce date again.");
				}
			}

			t = new Scheduled(properties.get(0), properties.get(1), this, t,
					scheduledDate);
		}
		Clock.getInstance().getNotification().addObserver(t);
		this.children.add(t);
		logger.info("added task " + t.getName());
		assert this.invariant();
	}

	/**
	 * Asks the user the properties needed to create a new Project.
	 */
	public final ArrayList<String> askChildProperties(final Client client) {
		ArrayList<String> properties = new ArrayList<String>();
		Scanner sc = new Scanner(System.in);
		logger.debug("introducing activity name");
		System.out.print("Introduce a name: ");
		properties.add(sc.nextLine());
		logger.debug("name introduced: " + properties.get(0));
		while (client.getActivity(properties.get(0)) != null) {
			logger
					.warn("activity " + properties.get(0) + " already exist");
			properties.remove(0);
			logger.debug("introducing new activity name");
			System.out.print("A component with the same name"
					+ " already exists in the system. Introduce a new name: ");
			properties.add(sc.nextLine());
			logger.debug("new name introduced: " + properties.get(0));
		}
		logger.debug("introducing activity description");
		System.out.print("Introduce a description: ");
		properties.add(sc.nextLine());
		logger.debug("description introduced: " + properties.get(1));
		logger.debug("all properties has been introduced correctly");
		// properties: ArrayList with two strings: the name and the description 
		// for the new Activity
		assert this.invariant();
		return properties;
	}

	public final void calculateTotalTime() {
		long sum = 0;
		for (Activity child : children) {
			sum += child.getTotalTime();
		}
		setTotalTime(sum);

		if (this.getFather() != null) {
			this.getFather().calculateTotalTime();
		}
		logger.debug(
				"calculating total time project " + this.getName() + " = "
						+ this.getTotalTime());
		assert this.invariant();
	}

	public final void removeChild(final Activity activity) {
		this.children.remove(activity);
		logger.info("Removed the activity " + activity.getName());
		assert this.invariant();
	}

	@Override
	public final void acceptVisitor(final Visitor visitor, final int level) {
		logger.debug("Project " + this.getName() + " is accepting visitor");

		visitor.visitProject(this, level);
		assert this.invariant();
	}

	/**
	 * Function used in the tests to add a new child without asking for the
	 * properties.
	 */
	public final void testAddChild(final Activity newChild) {
		this.children.add(newChild);
		assert this.invariant();
	}

	/**
	 * Adds an already existing project to the current project,  
	 * asking for its propeties. Used for the tests.
	 */
	public final void addExistingChildProject(final Project project) {
		this.children.add(project);
		assert this.invariant();
	}
}
