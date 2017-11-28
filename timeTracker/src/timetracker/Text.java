package timetracker;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Text extends Format {
	
	/**
	 * @uml.property  name="logger"
	 */
	private static Logger logger = LoggerFactory.getLogger(Text.class);

	@Override
	public final void applyFormat() {
		try {
			logger.error("creating an output file");
			PrintWriter out = new PrintWriter(getReport().getReportName() 
					+ ".txt");
			
			for (Element element : getReport().getElements()) {			
				
				if (element instanceof TextElement) {
					out.println(element.getData());
					
				} else if (element instanceof Title) {
					out.println(element.getData());
					
				} else if (element instanceof SubTitle) {
					out.println(element.getData());
					
				} else if (element instanceof Line) {
					out.println("------------------------------" 
							+ "---------------------------------"
							+ "---------------------------------------");
					
				} else if (element instanceof Table) {
					@SuppressWarnings("unchecked")
					ArrayList<ArrayList<String>> rows 
						= (ArrayList<ArrayList<String>>) element.getData();

					for (ArrayList<String> row:rows) {

			            ArrayList<String> temp = row;

			            for (String tableElement : temp) {
			            	out.printf("%-24s", tableElement);
			            }
			            out.println("\n");			      
			        }				
				}
			}			
			out.close();
			
		} catch (FileNotFoundException e) {
			logger.error("Error trying to create the output file");
			e.printStackTrace();
		}
		
	}		

}
