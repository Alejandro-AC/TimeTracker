package timetracker;

import java.util.ArrayList;
import java.util.Collection;

public class Table extends Element {
	
	/**
	 * @uml.property  name="tableData"
	 */
	private Collection<Collection<String>> tableData 
		= new ArrayList<Collection<String>>();

	/** 
	 * @uml.property  name="tableData"
	 */
	public final Collection<Collection<String>> getTableData() {
		return this.tableData;
	}

	/** 
	 * Setter of the property <tt>data</tt>
	 * @param data  The data to set.
	 * @uml.property  name="tableData"
	 */
	public final void setTableData(
			final Collection<Collection<String>> tableDataSet) {
		this.tableData = tableDataSet;
	}

	/**
	 * @uml.property  name="tableFields"
	 */
	private Collection<String> tableFields = new ArrayList<String>();

	/** 
	 * Getter of the property <tt>fields</tt>
	 * @return  Returns the fields.
	 * @uml.property  name="tableFields"
	 */
	public final Collection<String> getTableFields() {
		return this.tableFields;
	}

	/** 
	 * Setter of the property <tt>fields</tt>
	 * @param fields  The fields to set.
	 * @uml.property  name="tableFields"
	 */
	public final void setTableFields(final Collection<String> tableFieldsSet) {
		this.tableFields = tableFieldsSet;
	}

	@Override
	public final Object getData() {
		return null;
	}
	
	/**
	 * Constructor of the class.
	 */
	public Table(final Collection<String> tableFieldsSet) {
		setTableFields(tableFieldsSet);
	}

		
	/**
	 * Adds a new row to the table.
	 */
	public final void addRow(final Collection<String> row) {
		this.tableData.add(row);
	}
	
	

}
