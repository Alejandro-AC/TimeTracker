/**
 * 
 */
package projectManagement;

import java.util.Observable;
import java.util.Observer;
import java.util.Date;

/** 
 * @author marcm
 */
public class Interval implements Observer {

	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

		
		/**
		 */
		public void start(){
		}


			
			/**
			 */
			public void stop(){
			}


				
				/**
				 */
				public void calculateTime(){
				}



				/**
				 * @uml.property  name="startDate"
				 */
				private Date startDate;



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
				 * @uml.property  name="endDate"
				 */
				private Date endDate;



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

}
