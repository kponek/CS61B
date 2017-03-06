package db;

import edu.princeton.cs.introcs.In;
import edu.princeton.cs.introcs.Out;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Handler {
    public static String createTable(String tableName, String[] columnTypes, String[] columnNames, Database db) {
        Table table = new Table(tableName, columnTypes, columnNames);
        ArrayList<Table> data = db.getTables();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getName().equals(tableName)) {
                return "ERROR: table already exists";
            }
        }
        data.add(table);
        return "";
    }

    public static String createTable(String tableName, String clause, Database db) {
        ArrayList<Table> data = db.getTables();
        Matcher selClause;
        Pattern ERROR = Pattern.compile("ERROR: " + "\\s*(.*)\\s*");
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getName().equals(tableName)) {
                return "ERROR: table already exists";
            }
        }
        if ((selClause = ERROR.matcher(clause)).matches()) {
            return clause;
        }
        String[] colTypes;
        String[] colNames;
        String[] firstline;
        String[] lines;
        Matcher m;
        Pattern linesplit = Pattern.compile("\\s*\n\\s*");
        Pattern line1split = Pattern.compile("\\s*,\\s*");
        Pattern line1Data = Pattern.compile("(\\w+)\\s+(\\w+)");
        Pattern rowData = Pattern.compile("\\s*,\\s*");
        lines = linesplit.split(clause);
        firstline = line1split.split(lines[0]);
        colNames = new String[firstline.length];
        colTypes = new String[firstline.length];
        for (int i = 0; i < firstline.length; i++) {
            if ((m = line1Data.matcher(firstline[i])).matches()) {
                colNames[i] = m.group(1);
                colTypes[i] = m.group(2);
            }
        }
        Table dataTable = new Table(tableName, colTypes, colNames);
        data.add(dataTable);
        for (int i = 1; i < lines.length; i++) {
            String[] inRow = rowData.split(lines[i]);
            dataTable.insertRow(inRow);
        }
        return "";
    }

    public static String load(String tableName, Database db) {
        ArrayList<Table> data = db.getTables();
        Table dataTable;
        Pattern space = Pattern.compile("(\\w+)\\s+(\\w+)");
        Pattern p = Pattern.compile("\\s*,\\s*");
        Matcher m;
        String holder;
        In inputFile;
        try {
            inputFile = new In(tableName + ".tbl");
        } catch (Exception e) {
            return "ERROR: TBL file not found: " + tableName + ".tbl";
        }
        String[] line = p.split(inputFile.readLine());
        String[] colNames = new String[line.length];
        String[] colTypes = new String[line.length];
        for (int i = 0; i < line.length; i++) {
            if ((m = space.matcher(line[i])).matches()) {
                colNames[i] = m.group(1);
                colTypes[i] = m.group(2);
            } else {
                return "ERROR: Malformed column and data type format.";
            }
        }
        for (int i = 0; i < colTypes.length; i++) {
            if (!colTypes[i].equals("int") & !colTypes[i].equals("float") &
                    !colTypes[i].equals("string")) {
                return "ERROR: incorrect data type.";
            }
        }
        if (colTypes.length != colNames.length) {
            return "ERROR: Malformed column name and type.";
        }
        dataTable = new Table(tableName, colTypes, colNames);
        while (inputFile.hasNextLine()) {
            String[] inRow = p.split(inputFile.readLine());
            if (inRow.length != colTypes.length) {
                dropTable(tableName, db);
                return "ERROR: Malformed row length.";
            }
            holder = dataTable.insertRow(inRow);
            if (!holder.equals("")) {
                dropTable(tableName, db);
                return holder;
            }
        }
        data.add(dataTable);
        return "";
    }

    public static String store(String tableName, Database db) {
        ArrayList<Table> data = db.getTables();
        Table table;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getName().equals(tableName)) {
                table = data.get(i);
                Out outputFile = new Out(tableName + ".tbl");
                outputFile.println(table.toString());
                return "";
            }
        }
        return "ERROR: No table " + tableName + " to create table file from";
    }

    public static String dropTable(String tableName, Database db) {
        ArrayList<Table> data = db.getTables();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getName().equals(tableName)) {
                data.remove(i);
                return "";
            }
        }
        return "ERROR: No table " + tableName + " to remove from database";
    }

    public static String insertInto(String tableName, String[] literals, Database db) {
        ArrayList<Table> data = db.getTables();
        int i = isData(tableName, db);
        if (i != -1) {
            if (data.get(i).getRowSize() != literals.length) {
                return "ERROR: length of rows unequal to length of literals.";
            }
            return data.get(i).insertRow(literals);
        }

        return "ERROR: No table " + tableName + " to insert literals into";
    }

    public static String printTable(String tableName, Database db) {
        ArrayList<Table> data = db.getTables();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getName().equals(tableName)) {
                return data.get(i).toString();
            }
        }
        return "ERROR: No table " + tableName + " in database";
    }

    public static String selectTable(String[] tableName, String[] expr, String[] cond, Database db) {
        String rtVal = "";
        return rtVal;
    }

    public static String selectTable(String[] tableName, String[] expr, Database db) {
        Table[] tabs = tableNamesToArray(tableName, db);
        Table joined = joinAll(tabs);
        for (String s : expr) {
            if (s.equals("*")) {
                return joined.toString();
            } else if (expr.length == 1) {
                for (int i = 0; i < joined.getRowSize(); i++) {
                    if (s.equals(joined.getCols()[i].getColumnName())) {
                        return joined.getCols()[i].toString();
                    }
                }
            } else {
                Column[] cols = joined.stringToColumnArray(expr);
                return joined.selectString(cols);
            }
            //TODO: add more cases for multiple column select
        }
        return "";
    }

    private static Table[] tableNamesToArray(String[] tableNames, Database db) {
        ArrayList<Table> data = db.getTables();
        Table[] tabArray = new Table[tableNames.length];
        int index = 0;
        for (String table : tableNames) {
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getName().equals(table)) {
                    tabArray[index] = data.get(i);
                    index++;
                }
            }
        }
        return tabArray;
    }

    public static int isData(String tableName, Database db) {
        ArrayList<Table> data = db.getTables();
        int rtv = -1;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getName().equals(tableName)) {
                rtv = i;
                break;
            }
        }
        return rtv;
    }

    public static int isColumn(String colName, int index, Database db) {
        ArrayList<Table> data = db.getTables();
        int rtv = -1;
        for (int i = 0; i < data.get(index).getRowSize(); i++) {
            if (data.get(index).getCols()[i].getColumnName().equals(colName)
                    || colName.equals("*")) {
                rtv = i;
            }
        }
        return rtv;
    }

    private static Table joinAll(Table[] tables) {
        if (tables.length == 1) {
            return tables[0];
        }
        Table joined = join(tables[0], tables[1]);
        Table[] restTables = Arrays.copyOfRange(tables, 1, tables.length);
        restTables[0] = joined;
        return joinAll(restTables);
    }

    public static Table join(Table a, Table b) {
        //self join
        if (a.equals(b)) {
            return a;
        }
        Column[] aCols = a.getCols();
        Column[] bCols = b.getCols();
        //tree map keeps keys in order --> can iterate through it
        TreeMap<Integer, Integer> matchingColIndices = new TreeMap<>();
        TreeMap<Integer, List<Integer>> matchingRowIndices = new TreeMap<>();
        ArrayList<Integer> valueList = new ArrayList<>();
        //find indices of matching columns and store in matchingColIndices
        //keys, value pairs are matching Table a and b indices --> matchingAIndex,matchingBIndex
        for (int i = 0; i < aCols.length; i++) {
            for (int j = 0; j < bCols.length; j++) {
                if (aCols[i].getColumnName().equals(bCols[j].getColumnName())) {
                    matchingColIndices.put(i, j);
                }
            }
        }
        //cartesian product if there are no matching columns
        if (matchingColIndices.size() == 0) {
            return cartesian(a, b);
        }
        //find rows that match and store in matchingRowIndices
        for (int k = 0; k < matchingColIndices.size(); k++) {
            //iterate through each pair in map
            for (Map.Entry<Integer, Integer> entry : matchingColIndices.entrySet()) {
                //this loop is for a
                for (int l = 0; l < aCols[0].getSize(); l++) {
                    if (bCols[entry.getValue()].getData().contains(aCols[entry.getKey()].getItem(l))) {
                        for (int m = 0; m < bCols[0].getSize(); m++) {
                            if (aCols[entry.getKey()].getItem(l).equals(bCols[entry.getValue()].getItem(m))) {
                                valueList.add(m);
                                matchingRowIndices.put(l, valueList);
                            }
                        }
                    }
                    valueList = new ArrayList<Integer>();
                }
            }
        }

        //add matched columns to table
        Column[] totCols = new Column[aCols.length + bCols.length - matchingColIndices.size()];
        for (int i = 0; i < matchingColIndices.size(); i++) {
            for (Map.Entry<Integer, Integer> entry : matchingColIndices.entrySet()) {
                totCols[i] = new Column(aCols[entry.getKey()].getColumnName(), aCols[entry.getKey()].getDataType(), aCols[entry.getKey()].getData());
                //TODO: make sure to only add the keys in rows, right now this adds the whole column
                for (int j = totCols[i].getSize() - 1; j >= 0; j--) {
                    if (!matchingRowIndices.containsKey(j))
                        totCols[i].removeRow(j);
                    else {
                        for (int r = 1; r < matchingRowIndices.get(j).size(); r++) {
                            totCols[i].addRow(totCols[i].getItem(j));
                        }
                    }
                }
            }
        }
        //add rest of columns of Table a to table
        //for (int j = matchingColIndices.size(); j < aCols.length; j++) {
        int index = matchingColIndices.size();
        for (int i = 0; i < aCols.length; i++) {
            if (!matchingColIndices.containsKey(i)) {
                totCols[index] = new Column(aCols[i].getColumnName(), aCols[i].getDataType(), aCols[i].getData());
                //TODO: make sure to only add the keys in rows, right now this adds the whole column
                for (int j = totCols[i].getSize() - 1; j >= 0; j--) {
                    if (!matchingRowIndices.containsKey(j))
                        totCols[i].removeRow(j);
                    else {
                        for (int r = 1; r < matchingRowIndices.get(j).size(); r++) {
                            totCols[i].addRow(totCols[i].getItem(j));
                        }
                    }
                }
                index++;
            }
        }
        //combine map ArrayList values into one combined ArrayList for next combining the remaining b columns
        ArrayList<Integer> totalRows = new ArrayList<>();
        for (int i = 0; i < Math.max(aCols[0].getSize(), bCols[0].getSize()); i++) {
            for (Map.Entry<Integer, List<Integer>> entry : matchingRowIndices.entrySet()) {
                if (entry.getValue().contains(i) && !totalRows.contains(i)) {
                    totalRows.add(i);
                }
            }
        }
        // add remaing columns of Table b to table
        //for (int k = aCols.length; k < totCols.length; k++) {
        for (int j = 0; j < bCols.length; j++) {
            if (!matchingColIndices.containsValue(j)) {
                totCols[index] = new Column(bCols[j].getColumnName(), bCols[j].getDataType(), bCols[j].getData());
                //TODO: make sure to only add the keys in rows, right now this adds the whole column
                //for (Map.Entry<Integer,List<Integer>> entry: matchingRowIndices.descendingMap().entrySet()) {
                for (int i = totCols[index].getSize() - 1; i >= 0; i--) {
                    //if (!entry.getValue().contains(i))
                    if (!totalRows.contains(i))//new line
                        totCols[index].removeRow(i);
                }
                //}
                index++;
            }

        }
        return new Table("C", totCols);
        //TODO: change the name to real name from select input
    }

    private static Table cartesian(Table a, Table b) {
        Column[] totCols = new Column[a.getCols().length + b.getCols().length];
        for (int m = 0; m < a.getRowSize(); m++) {
            totCols[m] = new Column(a.getCols()[m].getColumnName(), a.getCols()[m].getDataType());
            for (int i = 0; i < a.getCols()[0].getSize(); i++) {
                for (int j = 0; j < b.getCols()[0].getSize(); j++) {
                    totCols[m].addRow(a.getCols()[m].getItem(i));
                }
            }
        }
        for (int j = a.getRowSize(); j < a.getRowSize() + b.getRowSize(); j++) {
            totCols[j] = new Column(b.getCols()[j - a.getRowSize()].getColumnName(), b.getCols()[j - a.getRowSize()].getDataType());
            for (int i = 0; i < a.getCols()[0].getSize(); i++) {
                for (int k = 0; k < b.getCols()[0].getSize(); k++) {
                    totCols[j].addRow(b.getCols()[j - a.getRowSize()].getItem(k));
                }
            }
        }
        return new Table("C", totCols);
    }
}
