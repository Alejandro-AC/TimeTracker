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
			PrintWriter out = new PrintWriter("report.txt");
			
			for (Element element : getReport().getElements()) {
				if (element instanceof TextElement 
						|| element instanceof Line) {
					out.println(element.getData());
				} else {
					final int space = 25;
					@SuppressWarnings("unchecked")
					ArrayList<ArrayList<String>> rows 
						= (ArrayList<ArrayList<String>>) element.getData();
					
					if (rows.get(0).size() == 2) {
						// Period table
						for (int i = 0; i < 4; i++) {
							ArrayList<String> periodRow = rows.get(0);
							/*String format = "s%" 
									+ (space - periodRow.get(0).length()) 
									+ "s%";*/
							out.println(periodRow.get(0));
							/*out.format(format, periodRow.get(0), 
									periodRow.get(1));*/
							rows.remove(0);						
						}
					} else {
						// Project table
						while (!rows.isEmpty()) {
							ArrayList<String> projectRow = rows.get(0);
							/*String format = "s%-" 
									+ (space - projectRow.get(0).length()) 
									+ "s%" 
									+ (space - projectRow.get(1).length()) 
									+ "s%" 
									+ (space - projectRow.get(2).length()) 
									+ "s%" 
									+ (space - projectRow.get(3).length());
							out.format(format, projectRow.get(0), 
									projectRow.get(1), projectRow.get(2), 
									projectRow.get(3));*/
							out.println(projectRow.get(0));
							rows.remove(0);
						}
					}
					
				}
			}
			
			out.close();
			
		} catch (FileNotFoundException e) {
			logger.error("Erro trying to create the output file");
			e.printStackTrace();
		}
		
	}	

}
