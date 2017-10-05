/**
 * 
 */
package projectManagement;

import java.util.Date;

/** 
 * @author marcm
 */
public abstract class Component {

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
	 * @uml.property name="totalTime"
	 */
	protected Date totalTime;

	/** 
	 * Getter of the property <tt>totalTime</tt>
	 * @return  Returns the totalTime.
	 * @uml.property  name="totalTime"
	 */
	public Date getTotalTime() {
		return totalTime;
	}

	/** 
	 * Setter of the property <tt>totalTime</tt>
	 * @param totalTime  The totalTime to set.
	 * @uml.property  name="totalTime"
	 */
	public void setTotalTime(Date totalTime) {
		this.totalTime = totalTime;
	}

	/** 
	 * @uml.property name="father"
	 */
	protected Component father = null;

	/** 
	 * Getter of the property <tt>father</tt>
	 * @return  Returns the father.
	 * @uml.property  name="father"
	 */
	public Component getFather() {
		return father;
	}

	/** 
	 * Setter of the property <tt>father</tt>
	 * @param father  The father to set.
	 * @uml.property  name="father"
	 */
	public void setFather(Component father) {
		this.father = father;
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
		this.startDate = startDate;
	}

		
		/**
		 */
		public abstract void calculateTotalTime();

			
			/**
			 */
			public Component(String name, String description){
			}
		

}
