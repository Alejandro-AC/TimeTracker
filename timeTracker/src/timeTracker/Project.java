/**
 * 
 */
package timeTracker;

import java.text.SimpleDateFormat;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/** 
 * @author marcm
 */
public class Project extends Activity {
	
	static Logger logger = LoggerFactory.getLogger(Project.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	
	/**
	 * @uml.property   name="children"
	 * @uml.associationEnd   multiplicity="(0 -1)" aggregation="shared" inverse="father:timeTracker.Activity"
	 */
	private Collection<Activity> children = new ArrayList<Activity>();

	/** 
	 * Getter of the property <tt>children</tt>
	 * @return  Returns the children.
	 * @uml.property  name="children"
	 */
	@SuppressWarnings("unchecked") 
	public Collection<Activity> getChildren() {
		if (children.isEmpty()) {
			return null;
		} else {
			return children;
		}
	}

	/**
	 * Constructor of the class.
	 */
	public Project(String name, String description, Project father) {
		super(description, name, father);
	}

	/**
	 * Adds a new project to the children list.
	 */
	public void addChildProject(Client client) {
		
		ArrayList<String> properties = new ArrayList<String>();
		
		properties = askChildProperties(client);
		Project p = new Project(properties.get(0), properties.get(1), this);
		this.children.add(p);
		logger.info("added child project "+p.getName());
	}

	/**
	 * Adds a new task to the children list.
	 */
	public void addChildTask(Client client) {
		ArrayList<String> properties = new ArrayList<String>();
		Scanner scanner = new Scanner(System.in);
		
		properties = askChildProperties(client);	
		
		Task t = new SimpleTask(properties.get(0), properties.get(1), this);
		
		
		////
		
		System.out.print("Limited Interval Time (0/1): ");
		int limitedIntervalTime = Integer.parseInt(scanner.nextLine());
		
		if (limitedIntervalTime == 1) {
			System.out.print("Interval time (seconds): ");
			long limitedTime = Long.parseLong(scanner.nextLine());
			t = new LimitedIntervalTime(properties.get(0), properties.get(1), this, t, limitedTime);
		}
		
		System.out.print("Scheduled (0/1): ");
		int scheduled = Integer.parseInt(scanner.nextLine());
		
		if (scheduled == 1) {
			System.out.print("Starting date (yyyy-MM-dd HH:mm:ss): ");
			String dateString = scanner.nextLine();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date scheduledDate = null;
			try{
				scheduledDate = dateFormat.parse(dateString);
			}catch(Exception e){
				System.out.println("Date format incorrect.");
			}
			
			t = new Scheduled(properties.get(0), properties.get(1), this, t, scheduledDate);
		}
		Clock.getInstance().getNotification().addObserver(t);
		this.children.add(t);
		logger.info("added task " + t.getName());
	}

	/**
	 * Asks the user the properties needed to create a new Project.
	 * @return ArrayList with two strings: the name and the description for the new Activity.
	 */
	public ArrayList<String> askChildProperties(Client client) {
		ArrayList<String> properties = new ArrayList<String>();
		Scanner sc = new Scanner(System.in);
		logger.debug("introducing activity name");
		System.out.print("Introduce a name: ");
		properties.add(sc.nextLine());
		logger.debug("name introduced: " + properties.get(0));
		while(client.getActivity(properties.get(0)) != null) {
			logger.warn("activity "+properties.get(0)+" already exist");
			properties.remove(0);
			logger.debug("introducing new activity name");
			System.out.print("A component with the same name already exists in the system. Introduce a new name: ");
			properties.add(sc.nextLine());
			logger.debug("new name introduced: " + properties.get(0));
		}
		logger.debug("introducing activity description");
		System.out.print("Introduce a description: ");
		properties.add(sc.nextLine());
		logger.debug("description introduced: " + properties.get(1));
		logger.debug("all properties has been introduced correctly");
		return properties;
	}

	/**
	 * Calculates the total time of all the children of the current Project.
	 */
	public void calculateTotalTime() {
		long sum = 0;
		for (Activity child : children) {
			sum += child.getTotalTime();
		}
		this.totalTime = sum;
		
		if(this.father != null){
			father.calculateTotalTime();
		}
	}

	/**
	 * Searches for the child with the name given and returns it if it has been found.
	 * @param name: name of the component to be found.
	 * @return Returns the child Activity with the same name, or null if it has not been found.
	 */
	public Activity getChild(String name) {
		for (Activity child : children) {
			if (child.getName().equals(name)) {
				return child;
			}
		}
		return null;
	}

	/**
	 * Removes the specified child if it is in the children list.
	 * @param name: name of the child to be removed.
	 * @return True if the child could be removed and false if it couldn't.
	 */
	public boolean removeChild(String name){
		Activity child = getChild(name);
		if (child != null) {
			int index = ((AbstractList<Activity>) children).indexOf(child);
			children.remove(index);
			return true;			
		} else {
			return false;
		}
	}

	/** 
	 * Setter of the property <tt>children</tt>
	 * @param children  The children to set.
	 * @uml.property  name="children"
	 */
	public void setChildren(Collection<Activity> children) {
		this.children = children;
	}

	@Override
	public void acceptVisitor(Visitor visitor, int level) {
		visitor.visitProject(this, level);
		for (Activity child : children) {
			child.acceptVisitor(visitor, level+1);
		}
	}
}
