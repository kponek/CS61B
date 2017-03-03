package db;

import edu.princeton.cs.algs4.*;

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
        String regex = "\\s*(,|\\s)\\s*";
        Pattern p1 = Pattern.compile(regex);
        In inputFile;
        try {
            inputFile = new In(tableName + ".tbl");
        } catch (Exception e) {
            return "ERROR: TBL file not found: " + tableName + ".tbl";
        }
        String[] line = p1.split(inputFile.readLine());
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
        dataTable = new Table(tableName, colTypes, colNames);
        while (inputFile.hasNextLine()) {
            String[] inRow = p2.split(inputFile.readLine());
            dataTable.insertRow(inRow);
        }
        data.add(dataTable);
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
        String rtVal = "";
        return rtVal;
    }

    public static String selectTable(String[] tableName, String[] expr) {
        String rtVal = "";
        return rtVal;
    }

    private static Table join(Table a, Table b) {
        Column[] aCols = a.getCols();
        Column[] bCols = b.getCols();
        ArrayList<Map<Integer, Integer>> matchingColIndices = new ArrayList<>();
        ArrayList<Integer> matchingRowIndices = new ArrayList<>();
        //find indices of matching columns
        for (int i = 0; i < aCols.length; i++) {
            for (int j = 0; j < bCols.length; j++) {
                if (aCols[i].getColumnName().equals(bCols[j].getColumnName())) {
                    matchingColIndices.add(new HashMap<>(i, j));
                }
            }
        }

        //find rows that match
        for (int k = 0; k < matchingColIndices.size(); k++) {
            //get key/value pair
            for (Integer key : matchingColIndices.get(k).keySet()) {
                int value = matchingColIndices.get(k).get(key);
                //this loop is for a
                for (int l = 0; l < aCols[0].getSize(); l++) {
                    if (bCols[value].getData().contains(aCols[key].getItem(l))) {
                        matchingRowIndices.add(l);
                    }
                }
            }
        }
        //Table c = new Table("",)
        return new Table("", "");
    }
}