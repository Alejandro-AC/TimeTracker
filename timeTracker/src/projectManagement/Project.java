/**
 * 
 */
package projectManagement;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;

/** 
 * @author marcm
 */
public class Project extends Component {

	/** 
	 * @uml.property name="components"
	 * @uml.associationEnd multiplicity="(0 -1)" aggregation="shared" inverse="project:projectManagement.Component"
	 */
	private Collection<Component> components = new ArrayList<Component>();

	/** 
	 * Getter of the property <tt>components</tt>
	 * @return  Returns the components.
	 * @uml.property  name="components"
	 */
	public Collection<Component> getComponents() {
		return components;
	}

	/** 
	 * Setter of the property <tt>components</tt>
	 * @param components  The components to set.
	 * @uml.property  name="components"
	 */
	public void setComponents(Collection<Component> components) {
		this.components = components;
	}
	
	/**
	 * 
	 */
	public void calculateTotalTime() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Adds a child to the components list.
	 * @param child: child Component to be added.
	 */
	public void addChild(Component child){
		this.components.add(child);
	}
	
	/**
	 * Removes the specified child if it is in the children list.
	 * @param name: name of the child to be removed.
	 * @return True if the child could be removed and false if it couldn't.
	 */
	public boolean removeChild(String name){
		Component child = getChild(name);
		if (child != null) {
			int index = ((AbstractList<Component>) components).indexOf(child);
			components.remove(index);
			return true;			
		} else {
			return false;
		}
	}			
	
	/**
	 * Searches for the child with the name given and returns it if it has been found.
	 * @param name: name of the component to be found.
	 * @return Returns the child Component with the same name, or null if it has not been found.
	 */
	public Component getChild(String name){
		for (Component child : components) {
			if (child.getName() == name) {
				return child;
			}
		}
		return null;
	}

	/**
	 * Constructor of the class.
	 */	
	public Project(String name, String description) {
		super(name, description);
	}
	
}
