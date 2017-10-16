/**
 * 
 */
package timeTracker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

/** 
 * @author marcm
 */
public class Client {
	
	/**
	 * Constructor of the class.
	 */
	public Client() {
	}

	/**
	 * Main function of the program.
	 * @param args
	 */
	public static void main(String[] args) {
		Client c = new Client();
		clock.schedule(1);
		
		c.printMenu();
		c.getMenuOption();
	}
	
	/** 
	 * @uml.property name="clock"
	 */
	public static timeTracker.Clock clock = new Clock();

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
	public void addRootProject() {
		ArrayList<String> properties = new ArrayList<String>();
		
		properties = askRootProjectProperties();
		
		Project p = new Project(properties.get(0), properties.get(1));
		this.rootProjects.add(p);		
	}

	/**
	 * Searches for the rootProject with the name given and returns it if it has been found.
	 * @param name: name of the rootProject to be found.
	 * @return Return the 1 with the same name, or null if it has not been found.
	 */
	public Project getRootProject(String name) {
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
	public ArrayList<String> askRootProjectProperties() {
		ArrayList<String> properties = new ArrayList<String>();
		Scanner sc = new Scanner(System.in);
		
		System.out.print("Introduce a name for the RootProject: ");
		properties.add(sc.nextLine());
		while (getRootProject(properties.get(0)) != null) {
			properties.remove(0);
			System.out.print("A Root Project with the same name already exists in the system. Introduce a new name: ");
			properties.add(sc.nextLine());
		}
		System.out.print("Introduce a description: ");
		properties.add(sc.nextLine());
		
		return properties;
	}
	
	/** 
	 * Generates the menu.
	 */
	public void printMenu() {
		System.out.println("1. Add Root Project");
		System.out.println("2. Add Child Project");
		System.out.println("3. Add Child Task");
		System.out.println("4. Add Interval");
		System.out.println("0. Exit");
	}
	
	/** 
	 * Gets the test option that the user decides to run and executes it.
	 */
	public void getMenuOption() {
		Scanner scanner = new Scanner(System.in);
		int option = -1;
		
		while(option != 0) {
			Project fatherProject;
			Task fatherTask;
			String fatherName;
			
			Stack<Component> nonVisited = new Stack<Component>();
			nonVisited.addAll(rootProjects);
			System.out.println("");
			System.out.println("  - TREE -");
			System.out.println("");
			for (int i = 0; i < rootProjects.size(); i++) {
				printTree(nonVisited.pop(), 0, nonVisited);
				System.out.println("");
			}
			
			System.out.println("");
			System.out.print("Introduce nueva opcion: ");
			option = Integer.parseInt(scanner.nextLine());
			
			switch(option) {
			case 1:		// Add Root Project
				addRootProject();
				break;
			case 2:		// Add Child Project
				System.out.print("Enter the name of the Father Project: ");
				fatherName = scanner.nextLine();
				
				fatherProject = (Project) getComponent(fatherName);
				
				if (fatherProject != null) {
					addChildProject(fatherProject, this);
				} else {
					System.out.println("Error. The specified Father Project does not exist.");
				}
				break;
			case 3:		// Add Child Task
				System.out.print("Enter the name of the Father Project: ");
				fatherName = scanner.nextLine();
				
				fatherProject = (Project) getComponent(fatherName);
				
				if (fatherProject != null) {
					addChildTask(fatherProject, this);
				} else {
					System.out.println("Error. The specified Father Project does not exist.");
				}
				break;	
			case 4: 	// Add Interval
				System.out.print("Enter the name of the Task: ");
				fatherName = scanner.nextLine();
				
				fatherTask = (Task) getComponent(fatherName);
				
				if (fatherTask != null) {
					addInterval(fatherTask);
				} else {
					System.out.println("Error. The specified Task does not exist.");
				}
				break;
			case 0:
				break;
			default:
				System.out.println("Error. Invalid option");
				break;
			}
		}
		scanner.close();
	}
		
	/** 
	 * Adds a child Project to an existing Project.
	 * @param father: father of the new child Project.
	 */
	public void addChildProject(Project father, Client client) {
		father.addChildProject(client);
	}
	
	/** 
	 * Adds a child Task to an existing Project.
	 * @param father: father of the new child Task.
	 */
	public void addChildTask(Project father, Client client) {
		father.addChildTask(client);
	}
	
	/**
	 * Prints all the Projects and Tasks of the system.
	 * @param component: component to be printed.
	 * @param level: level in the hierarchy of the "tree".
	 * @param nonVisited: stack with the components that haven't been visited yet.
	 */
	public void printTree(Component component, int level, Stack<Component> nonVisited) {		
		char[] spaces = new char[level*2];
		Arrays.fill(spaces, ' ');
		
		String type = "";
		if (component instanceof Project) {
			type = "P. ";
		} else if (component instanceof Task) {
			type = "T. ";
		}
		
		System.out.println(new String(spaces) + type + component.getName());
		
		if (component instanceof Project) {
			Collection<Component> children = component.getChildren();
			if (children != null) {
				nonVisited.addAll(children);
				for (int i = 0; i < children.size(); i++) {
					printTree(nonVisited.pop(), level + 1, nonVisited);
				}
			}
		} else if (component instanceof Task) {
			Collection<Interval> intervals = component.getChildren();
			if (intervals != null) {
				spaces = new char[level*2 + 2];
				Arrays.fill(spaces, ' ');
				for (Interval interval : intervals) {
					System.out.println(new String(spaces) + "I. " + interval.getId() + "..TotalTime.." + interval.getTotalTime() + "s " + " ..StartTime.." + interval.getStartDate());
				}
			}
		}
	}
	
	/**
	 * Searches for a specific Component in the tree, starting from the rootProjects, using a BFS based algorithm.
	 * @param name: name of the Component to be found.
	 * @return component: the Component that has been searched. It's value is null if it couldn't be found.
	 */
	public Component getComponent(String name) {
		boolean found = false;
		Queue<Component> nonVisited = new LinkedList<Component>();
		nonVisited.addAll(rootProjects);
		Iterator<Project> iter = rootProjects.iterator();
		Component component = null;
		
		while(!found && iter.hasNext()) {
			component = searchComponent(name, iter.next(), nonVisited);
			if (component != null) {
				found = true;
			}
		}		
		return component;
	}

	/**
	 * Searches for a specific Component in the tree.
	 * @param name: name of the Component to be found.
	 * @param component: actual Component that is being checked.
	 * @param nonVisited: list of Components that haven't been visited.
	 * @return component: the Component that has been searched. It's value is null if it couldn't be found.
	 */
	private Component searchComponent(String name, Component component, Queue<Component> nonVisited) {
		if (!nonVisited.isEmpty()) {		// There's at least one element to visit (the current one)
			nonVisited.remove();
			if (name.equals(component.getName())) {
				return component;
				
			} else if (component instanceof Project) {		// keep searching
				Collection<Component> children = component.getChildren();
				if (children != null) {
					nonVisited.addAll(children);
				}
				return searchComponent(name, nonVisited.peek(), nonVisited);
				
			} 
			return null;
			
		} else {
			return null;
		}
	}
	
	/**
	 * Adds a new Interval to the specified Task. 
	 * @param father: Task where the new Interval must be added. 
	 */
	public void addInterval(Task father) {
		father.addInterval();
	}


}
