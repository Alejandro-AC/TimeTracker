package timetracker;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HTML is a class that applies the HTML format to any Report that
 * is being generated. 
 */
public class Html extends Format {
	
	/**
	 * @uml.property  name="logger"
	 */
	private static Logger logger = LoggerFactory.getLogger(Text.class);
	
	@Override
	public final void applyFormat() {
		try {
			logger.error("creating an output file");
			PrintWriter out = new PrintWriter(getReport().getReportName() 
					+ ".html");
			
			for (Element element : getReport().getElements()) {			
				
				if (element instanceof TextElement) {
					out.println("<p>" + element.getData() + "</p>");
					
				} else if (element instanceof Title) {
					out.println("<h1>" + element.getData() + "</h1>");
					
				} else if (element instanceof SubTitle) {
					out.println("<h3>" + element.getData() + "</h3>");
					
				} else if (element instanceof Line) {
					out.println("<hr/>");
					
				} else if (element instanceof Table) {
					@SuppressWarnings("unchecked")
					ArrayList<ArrayList<String>> rows 
						= (ArrayList<ArrayList<String>>) element.getData();
					out.print("<table border='1'>");

					for (ArrayList<String> row:rows) {

			            ArrayList<String> temp = row;
			            out.println("<tr>");

			            for (String tableElement : temp) {
			                out.print("<td>" + tableElement + "</td>");
			            }
			            out.println("</tr>");			      
			        }
			        out.print("</table>");					
				}
			}			
			out.close();
			
		} catch (FileNotFoundException e) {
			logger.error("Error trying to create the output file");
			e.printStackTrace();
		}
		
	}	

}
