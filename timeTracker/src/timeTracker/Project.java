/**
 * 
 */
package timeTracker;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

/** 
 * @author marcm
 */
public class Project extends Component {

	/**
	 * @uml.property  name="children"
	 * @uml.associationEnd  multiplicity="(0 -1)" aggregation="shared" inverse="father:timeTracker.Component"
	 */
	private Collection<Component> children = new ArrayList<Component>();

	/**
	 * Getter of the property <tt>children</tt>
	 * @return  Returns the children.
	 * @uml.property  name="children"
	 */
	public Collection<Component> getChildren() {
		return children;
	}

	/**
	 * Setter of the property <tt>children</tt>
	 * @param children  The children to set.
	 * @uml.property  name="children"
	 */
	public void setChildren(Collection<Component> children) {
		this.children = children;
	}

	/**
	 * Constructor of the class.
	 */
	public Project(String name, String description){
		super(description, name);
	}

	/**
	 * Adds a new project to the children list.
	 */
	public void addChildProject(){
		ArrayList<String> properties = new ArrayList<String>();
		
		properties = askChildProperties();
		Project p = new Project(properties.get(0), properties.get(1));
		this.children.add(p);
	}

	/**
	 * Adds a new task to the children list.
	 */
	public void addChildTask(){
		ArrayList<String> properties = new ArrayList<String>();
		
		properties = askChildProperties();
		Task t = new Task(properties.get(0), properties.get(1));
		this.children.add(t);
	}

	/**
	 * Asks the user the properties needed to create a new Project.
	 * @return ArrayList with two strings: the name and the description for the new Component.
	 */
	public ArrayList<String> askChildProperties(){
		ArrayList<String> properties = new ArrayList<String>();
		Scanner sc = new Scanner(System.in);
		
		System.out.print("Introduce a name: ");
		properties.add(sc.nextLine());
		System.out.print("Introduce a description: ");
		properties.add(sc.nextLine());
		
		return properties;
	}

	/**
	 */
	public void calculateTotalTime(){
	}

	/**
	 * Searches for the child with the name given and returns it if it has been found.
	 * @param name: name of the component to be found.
	 * @return Returns the child Component with the same name, or null if it has not been found.
	 */
	public Component getChild(String name){
		for (Component child : children) {
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
		Component child = getChild(name);
		if (child != null) {
			int index = ((AbstractList<Component>) children).indexOf(child);
			children.remove(index);
			return true;			
		} else {
			return false;
		}
	}

}
