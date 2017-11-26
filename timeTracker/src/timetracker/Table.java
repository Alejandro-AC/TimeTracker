package timetracker;

import java.util.ArrayList;

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
	
	/**
	 * Constructor of the class.
	 */
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
