/**
 * 
 */
package projectManagement;

import java.util.Collection;

/** 
 * @author marcm
 */
public class Client {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	/**
	 * @uml.property  name="rootProjects"
	 * @uml.associationEnd  multiplicity="(0 -1)" aggregation="shared" inverse="client:projectManagement.Project"
	 */
	private Collection rootProjects;

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
		}

}
