/**
 * 
 */
package timeTracker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

/** 
 * @author marcm
 */
public class Client {
	
	/**
	 * Constructor of the class.
	 */
	public Client(){
	}

	/**
	 * Main function of the program.
	 * @param args
	 */
	public static void main(String[] args) {
		Client c = new Client();
		c.testMenu();
		c.testGetMenuOption();
	}

	/**
	 * @uml.property  name="rootProjects"
	 * @uml.associationEnd  multiplicity="(0 -1)" aggregation="shared" inverse="client:timeTracker.Project"
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
	 * Adds a rootProject to the rootProjects list.
	 */
	public void addRootProject(){
		ArrayList<String> properties = new ArrayList<String>();
		
		properties = askRootProjectProperties();		
		
		Project p = new Project(properties.get(0), properties.get(1));
		this.rootProjects.add(p);		
	}

	/**
	 * Searches for the rootProject with the name given and returns it if it has been found.
	 * @param name: name of the rootProject to be found.
	 * @return Return the interval with the same name, or null if it has not been found.
	 */
	public Project getRootProject(String name){
		for (Project rootProject : rootProjects) {
			if (rootProject.getName().equals(name)) {
				return rootProject;
			}
		}
		return null;
	}

	/** 
	 * Asks the user the properties needed to create a new Project.
	 * @return ArrayList with two strings: the name and the description for the new Component.
	 */
	public ArrayList<String> askRootProjectProperties(){
		ArrayList<String> properties = new ArrayList<String>();
		Scanner sc = new Scanner(System.in);
		
		System.out.print("Introduce a name: ");
		properties.add(sc.nextLine());
		System.out.print("Introduce a description: ");
		properties.add(sc.nextLine());
		
		return properties;
	}
	
	/**
	 * Generates the menu for the test options.
	 */
	public void testMenu() {
		System.out.println("1. Add Root Project");
		System.out.println("2. Add Child Project to Root Project");
		System.out.println("3. Add Child Task to Root Project");
	}
	
	/**
	 * Gets the test option that the user decides to run and executes it.
	 */
	public void testGetMenuOption() {
		Scanner scanner = new Scanner(System.in);
		int option = -1;
		Project p;
		
		while(option != 0) {
			System.out.print("Introduce nueva opcion: ");
			option = scanner.nextInt();
			
			switch(option) {
			case 1:
				testAddRootProject();
				break;
			case 2:
				p = ((ArrayList<Project>) rootProjects).get(0);
				testAddChildProjectToRootProject(p);
				break;
			case 3:
				p = ((ArrayList<Project>) rootProjects).get(0);
				testAddTaskChildToRootProject(p);
				break;				
			case 0:
				break;
			}
		}
	}
	
	/**
	 * Test 1.
	*/
	public void testAddRootProject(){
		addRootProject();
		Scanner sc = new Scanner(System.in);
		System.out.print("Introduce project's name to be found: ");
		String name = sc.nextLine();
		Project p = getRootProject(name);
		if (p != null) {
			System.out.println(p.getName());
			System.out.println(p.getDescription());
		} else {
			System.out.println("This project is not in the root list.");
		}
	}
	
	/** 
	 * Test 2.
	 * @param rootProject
	 */
	public void testAddChildProjectToRootProject(Project rootProject){
		rootProject.addChildProject();
		Scanner sc = new Scanner(System.in);
		System.out.print("Introduce child project's name to be found: ");
		String name = sc.nextLine();
		Component child = rootProject.getChild(name);
		if (child != null) {
			System.out.println(child.getName());
			System.out.println(child.getDescription());
		} else {
			System.out.println("This project is not in the " + rootProject.getName() + " children list.");
		}
	}
	
	/**
	 * Test 3.
	 * @param rootProject
	 */
	public void testAddTaskChildToRootProject(Project rootProject) {
		rootProject.addChildTask();
		Scanner sc = new Scanner(System.in);
		System.out.print("Introduce child-task's name to be found: ");
		String name = sc.nextLine();
		Component child = rootProject.getChild(name);
		if (child != null) {
			System.out.println(child.getName());
			System.out.println(child.getDescription());
		} else {
			System.out.println("This task is not in the " + rootProject.getName() + " children list.");
		}
	}

}
