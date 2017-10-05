/**
 * 
 */
package projectManagement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

/** 
 * @author marcm
 */
public class Client {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Client c = new Client();
		c.addRootProject();
	}

	/**
	 * @uml.property  name="rootProjects"
	 * @uml.associationEnd  multiplicity="(0 -1)" aggregation="shared" inverse="client:projectManagement.Project"
	 */
	private Collection<Component> rootProjects = new ArrayList<Component>();

	/**
	 * Getter of the property <tt>rootProjects</tt>
	 * @return  Returns the rootProjects.
	 * @uml.property  name="rootProjects"
	 */
	public Collection getRootProjects() {
		return rootProjects;
	}

	/**
	 * Setter of the property <tt>rootProjects</tt>
	 * @param rootProjects  The rootProjects to set.
	 * @uml.property  name="rootProjects"
	 */
	public void setRootProjects(Collection rootProjects) {
		this.rootProjects = rootProjects;
	}
	
	/** 
	 * Function that adds a root-project to the root-project's list.
	 */
	public void addRootProject(){
		Scanner sc = new Scanner(System.in);
		System.out.print("Introduce project name: ");
		String name = sc.nextLine();
		System.out.print("Introduce a description: ");
		String description = sc.nextLine();
		
		Project p = new Project(name, description);
		this.rootProjects.add(p);
	}			 

}
