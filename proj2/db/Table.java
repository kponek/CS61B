package db;

public class Table {
    private Column[] cols;
    private String name;
    /*Table(int numRows, int numCols, String[] colNames, String[] colTypes,  Column[] col) {
        cols = new Column[numRows];
        for (int i = 0; i < numCols; i++) {
            cols[i] = new Column(colNames[i],colTypes[i],)
        }
    }*/
    Table(String tableName, String[] colNames, String[] colTypes) {
        cols = new Column[colNames.length];
        name = tableName;
        for (int i = 0; i < colNames.length; i++) {
            cols[i] = new Column(colNames[i], colTypes[i]);
        }
    }
    public void insertRow(String[] literals) {
        for (int i = 0; i < cols.length; i++) {
            if (cols[i].getDataType().equals("int")) {
                cols[i].addRow(Integer.parseInt(literals[i]));
            }
            else if (cols[i].getDataType().equals("float")) {
                cols[i].addRow(Float.parseFloat(literals[i]));
            }
            else {
                cols[i].addRow(literals[i]);
            }
        }
    }
    public String toString() {
        String tableString = "";
        if (cols.length == 0) {
            return tableString;
        }
        //first line has the column names and types
        for (int k = 0; k < cols.length; k++) {
            if (k == 0) {
                tableString = tableString + cols[k].getColumnName() + " " + cols[k].getDataType();
            }
            else {
                tableString = tableString + "," + cols[k].getColumnName() + " " + cols[k].getDataType();
            }
        }
        tableString += "\n";
        //remaining lines are the data
        for (int i = 0; i < cols[0].getSize(); i++) {
            for (int j = 0; j < cols.length; j++) {
                if (j == 0) {
                    tableString += cols[j].getItem(i);
                }
                else {
                    tableString = tableString + "," + cols[j].getItem(i);
                }
            }
            tableString += "\n";
        }
        return tableString;
    }
    public String getName() {
        return name;
    }
    public void setName(String tableName) {
        name = tableName;
    }
    public int getRowSize() {
        return cols.length;
    }
    public Column[] getCols() {
        return cols;
    }
}
