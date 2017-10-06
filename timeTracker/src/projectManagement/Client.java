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
		Client c = new Client();
		c.addRootProject();
		Scanner sc = new Scanner(System.in);
		System.out.print("Introduce project name to be found: ");
		String name = sc.nextLine();
		Project p = c.getRootProject(name);
		if (p != null) {
			System.out.println(p.getName());		
		} else {
			System.out.println("This project is not in the root list.");
		}
	}

	/** 
	 * @uml.property name="rootProjects"
	 * @uml.associationEnd multiplicity="(0 -1)" aggregation="shared" inverse="client:projectManagement.Project"
	 */
	private Collection<Project> rootProjects = new ArrayList<Project>();

	/** 
	 * Getter of the property <tt>rootProjects</tt>
	 * @return  Returns the rootProjects.
	 * @uml.property  name="rootProjects"
	 */
	public Collection<Project> getRootProjects() {
		return rootProjects;
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

	/**
	 * Searches for the rootPorject with the name given and returns it if it has been found.
	 * @param name: name of the rootProject to be found.
	 * @return Return the interval with the same name, or null if it has not been found.
	 */
	public Project getRootProject(String name){
		for (Project rootProject : rootProjects) {
			if (rootProject.getName().equals(name)) {
				System.out.println("equals!");
				return rootProject;
			}
		}
		return null;
	 }

	/** 
	 * Setter of the property <tt>rootProjects</tt>
	 * @param rootProjects  The rootProjects to set.
	 * @uml.property  name="rootProjects"
	 */
	public void setRootProjects(Collection<Project> rootProjects) {
		this.rootProjects = rootProjects;
	}

	/**
	 * Constructor of the class.
	 */
	public Client(){
	}			 

}
