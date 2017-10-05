/**
 * 
 */
package projectManagement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
		Project p = askProjectData();
		c.addRootProject(p);
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
		 */
		public void addRootProject(Project rootProject){
			this.rootProjects.add(rootProject);
		}
		
		public static Project askProjectData(){
			Scanner sc = new Scanner(System.in);
			System.out.println("Introduce project name:");
			String name = sc.nextLine();
			System.out.println("Introduce a description:");
			String description = sc.nextLine();
			
			Component c = new Project(name, description);
			c.setFather(null);
			return (Project) c;
		}

}
