package db;

import edu.princeton.cs.algs4.*;

import java.lang.reflect.Array;
import java.util.*;
import java.util.regex.Pattern;

public class Handler {
    public static String createTable(String tableName, String[] columnTypes, String[] columnNames) {
        Table table = new Table(tableName, columnTypes, columnNames);
        ArrayList<Table> data = Database.getTables();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getName().equals(tableName)) {
                return "ERROR: table already exists";
            }
        }
        data.add(table);
        return "";
    }

    public static String createTable(String tableName, String clause) {
        Table table = new Table(tableName, clause);
        return "";
    }

    public static String load(String tableName) {
        ArrayList<Table> data = Database.getTables();
        Table dataTable;
        String regex = "\\s*(,|\\s+)\\s*";
        Pattern p1 = Pattern.compile(regex);
        In inputFile;
        int index = 0;
        try {
            inputFile = new In(tableName + ".tbl");
        } catch (Exception e) {
            return "ERROR: TBL file not found: " + tableName + ".tbl";
        }
        String[] line = p1.split(inputFile.readLine());
        if (line.length % 2 != 0) {
            return "ERROR: Malformed column name and type.\n";
        }
        String[] colNames = new String[line.length / 2];
        String[] colTypes = new String[line.length / 2];
        int colCounter = 0;
        int colTCounter = 0;
        for (int i = 0; i < line.length; i++) {
            if (i % 2 == 0) {
                colNames[colCounter] = line[i];
                colCounter++;
            } else {
                colTypes[colTCounter] = line[i];
                colTCounter++;
            }
        }
        Pattern p2 = Pattern.compile("\\s*,\\s*");
        if (colTypes.length != colNames.length) {
            return "ERROR: Malformed column name and type.\n";
        }
        dataTable = new Table(tableName, colTypes, colNames);
        while (inputFile.hasNextLine()) {
            String[] inRow = p2.split(inputFile.readLine());
            if (inRow.length != colTypes.length) {
                dropTable(tableName);
                return "ERROR: Malformed row length. \n";
            }
            dataTable.insertRow(inRow);
        }
        if (data != null) {
            data.add(dataTable);
        }
        return "";
    }

    public static String store(String tableName) {
        ArrayList<Table> data = Database.getTables();
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

    public static String dropTable(String tableName) {
        ArrayList<Table> data = Database.getTables();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getName().equals(tableName)) {
                data.remove(i);
                return "";
            }
        }
        return "ERROR: No table " + tableName + " to remove from database";
    }

    public static String insertInto(String tableName, String[] literals) {
        ArrayList<Table> data = Database.getTables();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getName().equals(tableName)) {
                if (data.get(i).getRowSize() != literals.length) {
                    return "ERROR: length of rows unequal to length of literals.";
                }
                data.get(i).insertRow(literals);
                return "";
            }
        }
        return "ERROR: No table " + tableName + " to insert literals into";
    }

    public static String printTable(String tableName) {
        ArrayList<Table> data = Database.getTables();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getName().equals(tableName)) {
                return data.get(i).toString();
            }
        }
        return "ERROR: No table " + tableName + " in database";
    }

    public static String selectTable(String[] tableName, String[] expr, String[] cond) {
        return "";
    }

    public static String selectTable(String[] tableName, String[] expr) {
        for (String s: expr) {
            if (s.equals("*")) {
                joinAll(tableNamesToArray(tableName));
            }
        }
    }

    private static Table[] tableNamesToArray(String[] tableNames) {
        ArrayList<Table> data = Database.getTables();
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
    private static Table joinAll(Table[] tables) {
        if (tables.length == 1) {
            return tables[0];
        }
        Table joined = join(tables[0],tables[1]);
        Table[] restTables = Arrays.copyOfRange(tables,1,tables.length);
        restTables[0] = joined;
        return joinAll(restTables);
    }
    public static Table join(Table a, Table b) {
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

        //find rows that match and store in matchingRowIndices
        for (int k = 0; k < matchingColIndices.size(); k++) {
            //iterate through each pair in map
            for (Map.Entry<Integer,Integer> entry: matchingColIndices.entrySet()) {
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
            for (Map.Entry<Integer,Integer> entry: matchingColIndices.entrySet()) {
                totCols[i] = new Column(aCols[entry.getKey()].getColumnName(),aCols[entry.getKey()].getDataType(),aCols[entry.getKey()].getData());
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
                totCols[index] = new Column(aCols[i].getColumnName(),aCols[i].getDataType(),aCols[i].getData());
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
        for (int i = 0; i < Math.max(aCols[0].getSize(),bCols[0].getSize()); i++) {
            for (Map.Entry<Integer,List<Integer>> entry: matchingRowIndices.entrySet()) {
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

}