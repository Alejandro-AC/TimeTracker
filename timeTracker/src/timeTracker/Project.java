/**
 * 
 */
package timeTracker;

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
public class Project extends Activitat {
	
	static Logger logger = LoggerFactory.getLogger(Project.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	
	/**
	 * @uml.property   name="children"
	 * @uml.associationEnd   multiplicity="(0 -1)" aggregation="shared" inverse="father:timeTracker.Activitat"
	 */
	private Collection<Activitat> children = new ArrayList<Activitat>();

	/** 
	 * Getter of the property <tt>children</tt>
	 * @return  Returns the children.
	 * @uml.property  name="children"
	 */
	@SuppressWarnings("unchecked") 
	public Collection<Activitat> getChildren() {
		if (children.isEmpty()) {
			return null;
		} else {
			return children;
		}
	}

	/**
	 * Constructor of the class.
	 */
	public Project(String name, String description) {
		super(description, name);
	}

	/**
	 * Adds a new project to the children list.
	 */
	public void addChildProject(Client client) {
		
		ArrayList<String> properties = new ArrayList<String>();
		
		properties = askChildProperties(client);
		Project p = new Project(properties.get(0), properties.get(1));
		this.children.add(p);
		logger.info("added child project "+p.getName());
	}

	/**
	 * Adds a new task to the children list.
	 */
	public void addChildTask(Client client) {
		ArrayList<String> properties = new ArrayList<String>();
		
		properties = askChildProperties(client);
		Task t = new Task(properties.get(0), properties.get(1));
		this.children.add(t);
		logger.info("added child task "+t.getName());
	}

	/**
	 * Asks the user the properties needed to create a new Project.
	 * @return ArrayList with two strings: the name and the description for the new Activitat.
	 */
	public ArrayList<String> askChildProperties(Client client) {
		ArrayList<String> properties = new ArrayList<String>();
		Scanner sc = new Scanner(System.in);
		logger.debug("introducing activity name");
		System.out.print("Introduce a name: ");
		properties.add(sc.nextLine());
		while(client.getActivitat(properties.get(0)) != null) {
			logger.warn("activity "+properties.get(0)+" already exist");
			properties.remove(0);
			logger.debug("introducing new activity name");
			System.out.print("A component with the same name already exists in the system. Introduce a new name: ");
			properties.add(sc.nextLine());
		}
		logger.debug("introducing activity description");
		System.out.print("Introduce a description: ");
		properties.add(sc.nextLine());
		logger.debug("all properties has been introduced correctly");
		return properties;
	}

	/**
	 * Calculates the total time of all the children of the current Project.
	 */
	public void calculateTotalTime() {
		long sum = 0;
		for (Activitat child : children) {
			sum += child.getTotalTime().getTime();
		}
		sum += this.totalTime.getTime();
		this.totalTime = new Date(sum);	
	}

	/**
	 * Searches for the child with the name given and returns it if it has been found.
	 * @param name: name of the component to be found.
	 * @return Returns the child Activitat with the same name, or null if it has not been found.
	 */
	public Activitat getChild(String name) {
		for (Activitat child : children) {
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
		Activitat child = getChild(name);
		if (child != null) {
			int index = ((AbstractList<Activitat>) children).indexOf(child);
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
	public void setChildren(Collection<Activitat> children) {
		this.children = children;
	}
}
