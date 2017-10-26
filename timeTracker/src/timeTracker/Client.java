/**
 * 
 */
package timeTracker;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 * @author marcm
 */
public class Client {
	
	static Logger logger = LoggerFactory.getLogger(Client.class);
	/**
	 * Constructor of the class.
	 */
	public Client() {
	}

	/**
	 * Main function of the program.
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		Client c = new Client();
		Thread clockThread = new Thread(Clock.getInstance());
		clockThread.start();
		
		Thread impresorThread = new Thread(Impresor.getInstance());
		impresorThread.start();				

		try {		// Deserialization
			FileInputStream fileIn = new FileInputStream("data.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			c.rootProjects = (Collection<Project>) in.readObject();
			System.out.println("Desarialized data from data.ser");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("Activity class not found");
			e.printStackTrace();
			return;
		}
		
		Impresor.getInstance().setRootProjects(c.rootProjects);		
		
		c.printMenu();
		
		
		try {		// Serialization
			FileOutputStream fileOut = new FileOutputStream("data.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(c.rootProjects);
			out.close();
			fileOut.close();
			
			System.out.println("Serialized data is saved in data.ser");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	public void addRootProject() {
		ArrayList<String> properties = new ArrayList<String>();
		logger.debug("adding root project");
		logger.debug("asking project properties");
		properties = askRootProjectProperties();		
		
		Project p = new Project(properties.get(0), properties.get(1));
		this.rootProjects.add(p);
		logger.info("root project "+p.getName()+" added");
		Impresor.getInstance().setRootProjects(this.rootProjects);
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
	 * @return ArrayList with two strings: the name and the description for the new Activity.
	 */
	public ArrayList<String> askRootProjectProperties() {
		ArrayList<String> properties = new ArrayList<String>();
		Scanner sc = new Scanner(System.in);
		
		logger.debug("introducing root project name");
		System.out.print("Introduce a name for the RootProject: ");		
		properties.add(sc.nextLine());
		logger.debug("name introduced: "+properties.get(0));
		while (getRootProject(properties.get(0)) != null) {
			logger.warn("project name "+properties.get(0)+" already exist");
			properties.remove(0);
			logger.debug("Introducing new name for the project");
			System.out.print("A Root Project with the same name already exists in the system. Introduce a new name: ");
			properties.add(sc.nextLine());
			logger.debug("new name introduced: " + properties.get(0));
		}
		logger.debug("introducing description");
		System.out.print("Introduce a description: ");		
		properties.add(sc.nextLine());
		logger.debug("description introduced:"+properties.get(1));
		logger.debug("all properties has been introduced correctly");
		return properties;
	}
	
	/** 
	 * Generates the menu.
	 */
	public void printMenu() {
		
		Scanner scanner = new Scanner(System.in);
		int option = -1;
	
		while(option != 0) {			
			
			logger.debug("choosing menu option: ");
			System.out.println("");
			System.out.print("Enter 1 to see the menu or 0 to Exit: ");				
						
			boolean correctType = false;
			while(!correctType){
				try{			
					option = Integer.parseInt(scanner.nextLine());
					logger.debug("chosen option: " + option);
					correctType = true;
				}catch(Exception e){
					logger.debug("chosen option: " + option);
					logger.warn("Introduced value is not a valid value. Introducing new value");
					System.out.print("Introduced value is not a number. Please enter a number:");
				}
			}
			
			switch(option) {
			case 1:		
				printSubMenu();
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
	 * @param client 
	 * @param father: father of the new child Task.
	 */
	public void addChildTask(Project father, Client client) {
		father.addChildTask(client);
	}
	
	/**
	 * Searches for a specific Activity in the tree, starting from the rootProjects, using a BFS based algorithm.
	 * @param name: name of the Activity to be found.
	 * @return activity: the Activity that has been searched. It's value is null if it couldn't be found.
	 */
	public Activity getActivity(String name) {
		boolean found = false;
		Queue<Activity> nonVisited = new LinkedList<Activity>();
		nonVisited.addAll(rootProjects);
		Iterator<Project> iter = rootProjects.iterator();
		Activity activity = null;
		
		while(!found && iter.hasNext()) {
			activity = searchActivity(name, iter.next(), nonVisited);
			if (activity != null) {
				found = true;
			}
		}		
		return activity;
	}

	/**
	 * Searches for a specific Activity in the tree.
	 * @param name: name of the Activity to be found.
	 * @param activity: actual Activity that is being checked.
	 * @param nonVisited: list of Activities that haven't been visited.
	 * @return activity: the Activity that has been searched. It's value is null if it couldn't be found.
	 */
	private Activity searchActivity(String name, Activity activity, Queue<Activity> nonVisited) {
		if (!nonVisited.isEmpty()) {		// There's at least one element to visit (the current one)
			nonVisited.remove();
			if (name.equals(activity.getName())) {
				return activity;
				
			} else if (activity instanceof Project) {		// keep searching
				Collection<Activity> children = activity.getChildren();
				if (children != null) {
					nonVisited.addAll(children);
				}
				return searchActivity(name, nonVisited.peek(), nonVisited);
				
			} else {
				return searchActivity(name, nonVisited.peek(), nonVisited);
			}
			
		} else {
			logger.debug("nonVisited queue is empty");
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

	/**
	 */
	public void printSubMenu(){
		Scanner scanner = new Scanner(System.in);
		int option = -1;
		
		while(option != 0) {
			Project fatherProject;
			Task fatherTask = null;
			String fatherName = "";			
			
			System.out.println("1. Add Root Project");
			System.out.println("2. Add Child Project");
			System.out.println("3. Add Child Task");
			System.out.println("4. Start Interval");
			System.out.println("5. Stop Interval");
			System.out.println("6. Change Reprint Rate");
			System.out.println("7. Change minimum Interval Time");
			System.out.println("0. Return");
						
			logger.debug("choosing submenu option");
			
			System.out.println("");
			System.out.print("Enter an option: ");			
						
			boolean correctType = false;
			while(!correctType){
				try{
					option = Integer.parseInt(scanner.nextLine());
					logger.debug("chosen option: " + option);
					correctType = true;					
				}catch(Exception e){
					logger.debug("chosen option: " + option);
					logger.warn("Introduced value is not a number. Introducing new value");
					System.out.print("Introduced value is not a number. Please enter a number:");
				}
			}
			
			switch(option) {
			case 1:		// Add Root Project
				addRootProject();
				
				break;
			case 2:		// Add Child Project
				logger.debug("adding child project");
				logger.debug("introducing name of father project");
				System.out.print("Enter the name of the Father Project: ");
				fatherName = scanner.nextLine();
				logger.debug("searching father project "+fatherName);
				fatherProject = (Project) getActivity(fatherName);
				
				if (fatherProject != null) {
					logger.debug("father "+fatherName+" has been found");
					addChildProject(fatherProject, this);
				} else {
					logger.info("The specified Father Project does not exist.");
					System.out.println("Error. The specified Father Project does not exist.");
				}
				
				break;
			case 3:		// Add Child Task
				logger.debug("adding task to project");
				logger.debug("introducing name of father project");
				System.out.print("Enter the name of the Father Project: ");
				fatherName = scanner.nextLine();
				logger.debug("searching father project "+fatherName);
				fatherProject = (Project) getActivity(fatherName);
				
				if (fatherProject != null) {
					logger.debug("father "+fatherName+" has been found");
					addChildTask(fatherProject, this);
				} else {
					logger.info("The specified Father Project does not exist.");
					System.out.println("Error. The specified Father Project does not exist.");
				}
				
				break;	
			case 4: 	// Start Interval
				logger.debug("starting interval");
				logger.debug("introducing name of the task");
				System.out.print("Enter the name of the Task: ");
				
				correctType = false;
				while(!correctType){
					try{
						fatherName = scanner.nextLine();
						logger.debug("task name introduced: " + fatherName);
						logger.debug("searching task " + fatherName);				
						fatherTask = (Task) getActivity(fatherName);
						correctType = true;
					}catch(Exception e){
						logger.warn(fatherName + " is a project, not a task.");
						logger.warn("introducing a new task to find.");
						System.out.print("You need to choose a task, not a project. Please introduce task name: ");
					}
				}

				
				if (fatherTask != null) {
					logger.debug("task " + fatherName+ "has been found");
					addInterval(fatherTask);
				} else {
					logger.debug("The specified task does not exist.");
					System.out.println("Error. The specified Task does not exist.");
				}
				
				break;
			case 5: 	// Stop Interval
				logger.debug("stoping interval");
				logger.debug("introducing name of the task");
				System.out.print("Enter the name of the Task: ");
				
				correctType = false;
				while(!correctType){
					try{
						fatherName = scanner.nextLine();
						logger.debug("task name introduced: " + fatherName);
						logger.debug("searching task " + fatherName);				
						fatherTask = (Task) getActivity(fatherName);
						correctType = true;
					}catch(Exception e){
						logger.warn(fatherName + " is a project, not a task.");
						logger.warn("introducing a new task to find.");
						System.out.print("You need to choose a task, not a project. Please introduce task name: ");
					}
				}
				
				fatherTask = (Task) getActivity(fatherName);
				
				if (fatherTask != null) {
					logger.debug("task " + fatherName+ "has been found");
					stopInterval(fatherTask);
				} else {
					logger.debug("The specified task does not exist.");
					System.out.println("Error. The specified Task does not exist.");
				}
				
				break;
			case 6:
				System.out.print("Enter the new refresh rate in seconds: ");
				
				
				Impresor.getInstance().setReprintTime(Long.parseLong(scanner.nextLine()));
								
				
				break;
			case 7:
				System.out.print("Enter the new minimum Interval time in seconds: ");				
				
				Task.setMinIntervalTime(Long.parseLong(scanner.nextLine()));								
				
				break;
			case 0:
				break;
			default:
				System.out.println("Error. Invalid option");
				break;
			}
		}
				
	}

		
	/**
	 * Stops last Interval of the specified Task. 
	 * @param father: Task where the last Interval will be stopped. 
	 */
	public void stopInterval(Task father) {
		father.stop();
		logger.info("interval for task " + father.getName() + " stopped");
	}

}
