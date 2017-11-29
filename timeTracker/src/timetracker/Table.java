package timetracker;

import java.util.ArrayList;

/**
 * Type of Element. It is used to store data from the Activities
 * and the Intervals of the Tree Activities, and make it easier to
 * be presented for the Reports.
 */
public class Table extends Element {
	
	/**
	 * @uml.property  name="tableData"
	 */
	private ArrayList<ArrayList<String>> table
		= new ArrayList<ArrayList<String>>();

	@Override
	public final Object getData() {
		return table;
	}

	public Table(final ArrayList<String> tableFieldsSet) {
		this.table.add(tableFieldsSet);
	}

	/**
	 * Adds a new row to the table.
	 */
	public final void addRow(final ArrayList<String> row) {
		this.table.add(row);
	}
	
	

}
