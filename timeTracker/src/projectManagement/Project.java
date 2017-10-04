/**
 * 
 */
package projectManagement;

import java.util.Collection;

/** 
 * @author marcm
 */
public class Project extends Component {

	/** 
	 * @uml.property name="components"
	 * @uml.associationEnd multiplicity="(0 -1)" aggregation="shared" inverse="project:projectManagement.Component"
	 */
	private Collection components;

	/** 
	 * Getter of the property <tt>components</tt>
	 * @return  Returns the components.
	 * @uml.property  name="components"
	 */
	public Collection getComponents() {
		return components;
	}

	/** 
	 * Setter of the property <tt>components</tt>
	 * @param components  The components to set.
	 * @uml.property  name="components"
	 */
	public void setComponents(Collection components) {
		this.components = components;
	}

	public void calculateTotalTime() {
		// TODO Auto-generated method stub
		
	}

		
		/**
		 */
		public void addChild(Component child){
		}

			
			/**
			 */
			public void removeChild(String name){
			}

				
				/**
				 */
				public Component getChild(String name){
					return null;
				}
}
