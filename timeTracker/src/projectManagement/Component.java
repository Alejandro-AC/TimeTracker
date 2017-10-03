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
	 * @uml.property  name="name"
	 */
	private String name;

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
	 * @uml.property  name="description"
	 */
	private String description;

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
	 * @uml.property  name="totalTime"
	 */
	private Date totalTime;

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
	 * @uml.property  name="father"
	 */
	private Component father = null;

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
		 */
		public void calculateTotalTime(){
		}

}
