package timeTracker;

import java.util.Date;
import java.io.Serializable;

/**
 * This abstract class is used for the Composite of the activities of our app.
 * It contains the main functionality for both the Projects and Tasks.  
 */
public abstract class Activity implements Serializable {

	/**
	 * Used for serialization.
	 */
	private static final long serialVersionUID = 1L;
	
	/** 
	 * @uml.property name="father"
	 * @uml.associationEnd multiplicity="(1 1)" inverse="children:timeTracker.Project"
	 */
	protected Project father = null;

		/** 
		 * Getter of the property <tt>father</tt>
		 * @return  Returns the father.
		 * @uml.property  name="father"
		 */
		public Project getFather() {
			return father;
		}
	
		/** 
		 * Setter of the property <tt>father</tt>
		 * @param father  The father to set.
		 * @uml.property  name="father"
		 */
		public void setFather(Project father) {
			this.father = father;
		}
		
	/** 
	 * @uml.property name="name"
	 */
	protected String name;

		/** 
		 * Getter of the property <tt>name</tt>
		 * @return  Returns the name.
		 * @uml.property  name="name"
		 */
		public String getName() {
			return name;
		}

		/** 
		 * Setter of the property <tt>name</tt>
		 * @param name  The name to set.
		 * @uml.property  name="name"
		 */
		public void setName(String name) {
			this.name = name;
		}
		
	
	/** 
	 * @uml.property name="description"
	 */
	protected String description;

		/** 
		 * Getter of the property <tt>description</tt>
		 * @return  Returns the description.
		 * @uml.property  name="description"
		 */
		public String getDescription() {
			return description;
		}
	
		/** 
		 * Setter of the property <tt>description</tt>
		 * @param description  The description to set.
		 * @uml.property  name="description"
		 */
		public void setDescription(String description) {
			this.description = description;
		}

	/** 
	 * @uml.property name="endDate"
	 */
	protected Date endDate;

		/** 
		 * Getter of the property <tt>endDate</tt>
		 * @return  Returns the endDate.
		 * @uml.property  name="endDate"
		 */
		public Date getEndDate() {
			return endDate;
		}
	
		/** 
		 * Setter of the property <tt>endDate</tt>
		 * @param endDate  The endDate to set.
		 * @uml.property  name="endDate"
		 */
		public void setEndDate(Date endDate) {
			this.endDate = endDate;
			if (this.getFather() != null) {
				this.getFather().setEndDate(endDate);
			}
		}


	/** 
	 * @uml.property name="startDate"
	 */
	protected Date startDate;

		/** 
		 * Getter of the property <tt>startDate</tt>
		 * @return  Returns the startDate.
		 * @uml.property  name="startDate"
		 */
		public Date getStartDate() {
			return startDate;
		}
	
		/** 
		 * Setter of the property <tt>startDate</tt>
		 * @param startDate  The startDate to set.
		 * @uml.property  name="startDate"
		 */
		public void setStartDate(Date startDate) {
			if (this.startDate == null){
				if (this.getFather() != null) {
					this.getFather().setStartDate(startDate);
				}
				this.startDate = startDate;
			}
	}

	/** 
	 * @uml.property name="totalTime"
	 */
	protected long totalTime;

		/** 
		 * Getter of the property <tt>totalTime</tt>
		 * @return  Returns the totalTime.
		 * @uml.property  name="totalTime"
		 */
		public long getTotalTime() {
			return totalTime;
		}
	
		/** 
		 * Setter of the property <tt>totalTime</tt>
		 * @param totalTime  The totalTime to set.
		 * @uml.property  name="totalTime"
		 */
		public void setTotalTime(long totalTime) {
			this.totalTime = totalTime;
		}

	/**
	 * Constructor of the class.
	 */
	public Activity(String description, String name, Project father) {
		this.description = description;
		this.name = name;
		this.father = father;
	}
	
	/**
	 * Calculates the total time of all its children.
	 */
	public abstract void calculateTotalTime();
	
	/**
	 * Returns all the children of this Activity.
	 * @return Children of this Activity.
	 */
	public abstract <T> T getChildren();

	
	/**
	 * Accepts a Visitor (in this case, the Impresor to print this Acitivity's information).
	 * @param visitor: visitor that is being accepted.
	 * @level: current level of the Activity in the Activities Tree.
	 */
	public abstract void acceptVisitor(Visitor visitor, int level);
			

}
