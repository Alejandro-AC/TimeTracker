package timetracker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Project extends Activity {

	/**
	 * Logger for the class.
	 */
	private static Logger logger = LoggerFactory.getLogger(Interval.class);

	/**
	 * Getter of the property <tt>logger</tt>
	 * 
	 * @return Returns the getLogger().
	 * @uml.property name="logger"
	 */
	public static final Logger getLogger() {
		return logger;
	}

	/**
	 * Used for serialization.
	 */
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
		getLogger().debug(
				"getting childrens: " + children + "from project "
						+ this.getName());
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
		getLogger().debug("setting childrens to project " + this.getName());
		this.children = childrenToSet;
	}

	/**
	 * Searches for the child with the name given and returns it if it has been
	 * found.
	 * 
	 * @param name
	 *            : name of the component to be found.
	 * @return Returns the child Activity with the same name, or null if it has
	 *         not been found.
	 */
	public final Activity getChild(final String name) {
		getLogger().debug(
				"searching child " + name + " in project " + this.getName());
		for (Activity child : children) {
			if (child.getName().equals(name)) {
				return child;
			}
		}
		return null;
	}

	/**
	 * Constructor of the class.
	 */
	public Project(final String name, final String description,
			final Project father) {
		super(description, name, father);
	}
	
	private boolean invariant(){
		boolean correct = true;
		for (Object obj:this.getChildren()) {
			if (!(obj instanceof Project) && !(obj instanceof Task)) {
				correct = false;
			}
		}
		
		return correct;
	}

	/**
	 * Adds a new project to the children list.
	 */
	public final void addChildProject(final Client client) {

		ArrayList<String> properties = new ArrayList<String>();

		properties = askChildProperties(client);
		Project p = new Project(properties.get(0), properties.get(1), this);
		this.children.add(p);
		getLogger().info("added child project " + p.getName());
	}

	/**
	 * Adds a new task to the children list.
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
				getLogger().debug("Introducing interval time option");
				System.out.print("Limited Interval Time (0/1): ");
				limitedIntervalTime = Integer.parseInt(scanner.nextLine());
				getLogger().debug(
						"interval time option introduced"
								+ " have correct type");
				correctType = true;
			} catch (Exception e) {
				getLogger().warn(
						"introduced value in interval time option"
								+ " is not a number. Must be 0 or 1.");
				System.out.println("Scheduled option must be 0 or 1."
						+ " Introduce option again.");
			}
		}

		while (!correctOption) {
			if (limitedIntervalTime != 0 && limitedIntervalTime != 1) {
				getLogger().debug(
						"introduced value in "
								+ "limitedIntervalTime option is not 0 or 1.");
				System.out.println("Interval time option must be 0 or 1");
				System.out.print("Limited Interval Time (0/1): ");
				limitedIntervalTime = Integer.parseInt(scanner.nextLine());
			} else {
				getLogger().debug("interval time option introduced correctly");
				correctOption = true;
			}
		}

		if (limitedIntervalTime == 1) {
			correctType = false;
			while (!correctType) {
				try {
					getLogger().debug("Introducing interval time");
					System.out.print("Interval time (seconds): ");
					long limitedTime = Long.parseLong(scanner.nextLine());
					t = new LimitedIntervalTime(properties.get(0),
							properties.get(1), this, t, limitedTime);
					getLogger().debug(
							"limited time " + limitedTime
									+ " introduced correctly");
					correctType = true;
				} catch (Exception e) {
					System.out.println("Limited interval time"
							+ " must be a number.");
					getLogger()
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
				getLogger().debug("introducing scheduled option");
				System.out.print("Scheduled (0/1): ");
				scheduled = Integer.parseInt(scanner.nextLine());
				getLogger().debug(
						"scheduled option introduced" + " have correct type");
				correctType = true;
			} catch (Exception e) {
				getLogger().warn(
						"introduced value in scheduled option"
								+ " is not a number. Must be 0 or 1.");
				System.out.println("Scheduled option must be 0 or 1."
						+ " Introduce an option again.");
			}
		}

		correctOption = false;
		while (!correctOption) {
			if (scheduled != 0 && scheduled != 1) {
				getLogger().debug(
						"introduced value in scheduled option"
								+ " is not 0 or 1.");
				System.out.println("Scheduled option must be 0 or 1");
				System.out.print("Scheduled (0/1): ");
				scheduled = Integer.parseInt(scanner.nextLine());
			} else {
				getLogger().debug(
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
					getLogger().debug(
							"introducing starting date" + " for schedule task");
					System.out.print("Starting date (yyyy-MM-dd HH:mm:ss): ");
					String dateString = scanner.nextLine();
					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					scheduledDate = dateFormat.parse(dateString);
					getLogger().debug(
							"introduced date " + scheduledDate + " correctly");
					correctType = true;
				} catch (Exception e) {
					getLogger().debug("Date format incorrect");
					System.out.println("Date format incorrect."
							+ " Introduce date again.");
				}
			}

			t = new Scheduled(properties.get(0), properties.get(1), this, t,
					scheduledDate);
		}
		Clock.getInstance().getNotification().addObserver(t);
		this.children.add(t);
		getLogger().info("added task " + t.getName());
	}

	/**
	 * Asks the user the properties needed to create a new Project.
	 * 
	 * @return ArrayList with two strings: the name and the description for the
	 *         new Activity.
	 */
	public final ArrayList<String> askChildProperties(final Client client) {
		ArrayList<String> properties = new ArrayList<String>();
		Scanner sc = new Scanner(System.in);
		getLogger().debug("introducing activity name");
		System.out.print("Introduce a name: ");
		properties.add(sc.nextLine());
		getLogger().debug("name introduced: " + properties.get(0));
		while (client.getActivity(properties.get(0)) != null) {
			getLogger()
					.warn("activity " + properties.get(0) + " already exist");
			properties.remove(0);
			getLogger().debug("introducing new activity name");
			System.out.print("A component with the same name"
					+ " already exists in the system. Introduce a new name: ");
			properties.add(sc.nextLine());
			getLogger().debug("new name introduced: " + properties.get(0));
		}
		getLogger().debug("introducing activity description");
		System.out.print("Introduce a description: ");
		properties.add(sc.nextLine());
		getLogger().debug("description introduced: " + properties.get(1));
		getLogger().debug("all properties has been introduced correctly");
		return properties;
	}

	/**
	 * Calculates the total time of all the children of the current Project.
	 */
	public final void calculateTotalTime() {
		long sum = 0;
		for (Activity child : children) {
			sum += child.getTotalTime();
		}
		setTotalTime(sum);

		if (this.getFather() != null) {
			this.getFather().calculateTotalTime();
		}
		getLogger().debug(
				"calculating total time project " + this.getName() + " = "
						+ this.getTotalTime());
	}

	/**
	 * Removes the specified child if it is in the children list.
	 * 
	 * @param name
	 *            : name of the child to be removed.
	 * @return True if the child could be removed and false if it couldn't.
	 */
	public final void removeChild(final Activity activity) {
		this.children.remove(activity);
		getLogger().info("Removed the activity " + activity.getName());
	}

	/**
	 * Accepts a Visitor (in this case, the Impresor to print this Acitivity's
	 * information).
	 * 
	 * @param visitor
	 *            : visitor that is being accepted.
	 * @level: current level of the Project in the Activities Tree.
	 */
	@Override
	public final void acceptVisitor(final Visitor visitor, final int level) {
		getLogger()
				.debug("Project " + this.getName() + " is accepting visitor");

		visitor.visitProject(this, level);
	}

	/**
	 * Function used in the tests to add a new child without asking for the
	 * properties.
	 * 
	 * @param child
	 *            : new child to add to the children list.
	 */
	public final void testAddChild(final Activity newChild) {
		this.children.add(newChild);
	}

	/**
	 * @uml.property name="reports"
	 * @uml.associationEnd multiplicity="(0 -1)"
	 *                     inverse="project:timetracker.Report"
	 */
	@SuppressWarnings("unused")
	private Collection<Report> reports = new ArrayList<Report>();

	/**
	 * Adds an already existing project to the current project.
	 */
	public final void addExistingChildProject(final Project project) {
		this.children.add(project);
	}
}
