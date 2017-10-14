/**
 * 
 */
package timeTracker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;
import java.util.Stack;

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
		
		System.out.print("Introduce a name for the RootProject: ");
		properties.add(sc.nextLine());
		System.out.print("Introduce a description: ");
		properties.add(sc.nextLine());
		
		return properties;
	}
	
	/**
	 * Generates the menu for the test options.
	 */
	public void testMenu() {
		System.out.println("1. Print Root Projects");
		System.out.println("2. Open Project");
		System.out.println("3. Add Root Project");
		System.out.println("4. Add Child Project to Root Project");
		System.out.println("5. Add Child Task to Root Project");
		System.out.println("0. Exit");
	}
	
	/**
	 * Gets the test option that the user decides to run and executes it.
	 */
	public void testGetMenuOption() {
		Scanner scanner = new Scanner(System.in);
		int option = -1;
		Project father;
		String fatherName;
		
		while(option != 0) {
				
			Stack<Component> nonVisited = new Stack<Component>();
			nonVisited.addAll(rootProjects);
			System.out.println("");
			for (int i = 0; i < rootProjects.size(); i++) {
				printTree(nonVisited.pop(), 0, nonVisited);
			}
			
			System.out.println("");
			System.out.print("Introduce nueva opcion: ");
			option = Integer.parseInt(scanner.nextLine());
			
			switch(option) {
			case 1:
				testPrintRootProjects();
				break;
			case 2:
				System.out.print("Project name: ");
				fatherName = scanner.nextLine();
				father = getRootProject(fatherName);
				while (father == null) {
					System.out.print("That project doesn't exist. Project name: ");
					fatherName = scanner.nextLine();
					father = getRootProject(fatherName);
				}
				testPrintChildren(father);
				break;
			case 3:
				addRootProject();
				break;
			case 4:
				System.out.print("Father's name: ");
				fatherName = scanner.nextLine();
				father = getRootProject(fatherName);
				while (father == null) {
					System.out.print("That rootProject doesn't exist. Father's name: ");
					fatherName = scanner.nextLine();
					father = getRootProject(fatherName);
				}
				addChildProjectToRootProject(father);
				break;
			case 5:
				System.out.print("Father's name: ");
				fatherName = scanner.nextLine();
				father = getRootProject(fatherName);
				while (father == null) {
					System.out.print("That rootProject doesn't exist. Father's name: ");
					fatherName = scanner.nextLine();
					father = getRootProject(fatherName);
				}
				addChildTaskToRootProject(father);
				break;				
			case 0:
				break;
			}
		}
		scanner.close();
	}
		
	/** 
	 * *******************
	 * @param rootProject
	 */
	public void addChildProjectToRootProject(Project rootProject){
		rootProject.addChildProject();
	}
	
	/** 
	 * @param rootProject
	 */
	public void addChildTaskToRootProject(Project rootProject){
		rootProject.addChildTask();
	}
	
	/**
	 * ****************
	 */
	public void testPrintRootProjects() {
		for (Project rootProject: rootProjects) {
			System.out.println(rootProject.getName());
		}
	}
	
	/**
	 * **************************
	 * @param father
	 */
	public void testPrintChildren(Project father) {
		for (Component child : father.getChildren()) {
			System.out.println(child.getName());
		}
	}
	
	/**
	 * 
	 * @param component
	 * @param visitedList
	 */
	public void printTree(Component component, int level, Stack<Component> nonVisited){		
		char[] spaces = new char[level];
		Arrays.fill(spaces, ' ');
		System.out.println(new String(spaces) + component.getName());
		
		Collection<Component> children = component.getChildren();
		if (children != null) {
			nonVisited.addAll(children);
			for (int i = 0; i < children.size(); i++) {
				printTree(nonVisited.pop(), level + 1, nonVisited);
			}
		}
	}


}
