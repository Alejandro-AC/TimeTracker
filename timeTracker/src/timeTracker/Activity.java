package timetracker;

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
	 * @uml.property   name="father"
	 * @uml.associationEnd   multiplicity="(1 1)"
	 * inverse="children:time.Tracker.Project"
	 */
	private Project father = null;

		/** 
		 * Getter of the property <tt>father</tt>
		 * @return  Returns the father.
		 * @uml.property  name="father"
		 */
		protected final Project getFather() {
			return father;
		}
	
		/** 
		 * Setter of the property <tt>father</tt>
		 * @param father  The father to set.
		 * @uml.property  name="father"
		 */
		protected final void setFather(final Project fatherSet) {
			this.father = fatherSet;
		}
		
	/**
	 * @uml.property   name="name"
	 */
	private String name;

		/** 
		 * Getter of the property <tt>name</tt>
		 * @return  Returns the name.
		 * @uml.property  name="name"
		 */
		protected String getName() {
			return this.name;
		}

		/** 
		 * Setter of the property <tt>name</tt>
		 * @param name  The name to set.
		 * @uml.property  name="name"
		 */
		protected final void setName(final String nameSet) {
			this.name = nameSet;
		}
		
	
	/**
	 * @uml.property   name="description"
	 */
	private String description;

		/** 
		 * Getter of the property <tt>description</tt>
		 * @return  Returns the description.
		 * @uml.property  name="description"
		 */
		protected final String getDescription() {
			return this.description;
		}
	
		/** 
		 * Setter of the property <tt>description</tt>
		 * @param description  The description to set.
		 * @uml.property  name="description"
		 */
		protected final void setDescription(final String descriptionSet) {
			this.description = descriptionSet;
		}

	/**
	 * @uml.property   name="endDate"
	 */
	private Date endDate;

		/** 
		 * Getter of the property <tt>endDate</tt>
		 * @return  Returns the endDate.
		 * @uml.property  name="endDate"
		 */
		protected final Date getEndDate() {
			return this.endDate;
		}
	
		/** 
		 * Setter of the property <tt>endDate</tt>
		 * @param endDate  The endDate to set.
		 * @uml.property  name="endDate"
		 */
		protected final void setEndDate(final Date endDateSet) {
			this.endDate = endDateSet;
			if (this.getFather() != null) {
				this.getFather().setEndDate(endDateSet);
			}
		}


	/**
	 * @uml.property   name="startDate"
	 */
	private Date startDate;

		/** 
		 * Getter of the property <tt>startDate</tt>
		 * @return  Returns the startDate.
		 * @uml.property  name="startDate"
		 */
		protected final Date getStartDate() {
			return this.startDate;
		}
	
		/** 
		 * Setter of the property <tt>startDate</tt>
		 * @param startDate  The startDate to set.
		 * @uml.property  name="startDate"
		 */
		protected final void setStartDate(final Date startDateSet) {
			if (this.startDate == null) {
				if (this.getFather() != null) {
					this.getFather().setStartDate(startDateSet);
				}
				this.startDate = startDateSet;
			}
		}

	/**
	 * @uml.property   name="totalTime"
	 */
	private long totalTime;

		/** 
		 * Getter of the property <tt>totalTime</tt>
		 * @return  Returns the totalTime.
		 * @uml.property  name="totalTime"
		 */
		protected long getTotalTime() {
			return this.totalTime;
		}
	
		/** 
		 * Setter of the property <tt>totalTime</tt>
		 * @param totalTime  The totalTime to set.
		 * @uml.property  name="totalTime"
		 */
		protected final void setTotalTime(final long totalTimeSet) {
			this.totalTime = totalTimeSet;
		}

	/**
	 * Constructor of the class.
	 */
	public Activity(final String descriptionConstr, 
			final String nameConstr, final Project fatherConstr) {
		this.description = descriptionConstr;
		this.name = nameConstr;
		this.father = fatherConstr;
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
	 * Accepts a Visitor 
	 * (in this case, the Impresor to print this Acitivity's information).
	 * @param visitor: visitor that is being accepted.
	 * @level: current level of the Activity in the Activities Tree.
	 */
	public abstract void acceptVisitor(Visitor visitor, int level);
			

}
