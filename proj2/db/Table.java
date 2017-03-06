package db;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.*;

public class Table {
    private Column[] cols;
    private String name;

    /*Table(int numRows, int numCols, String[] colNames, String[] colTypes,  Column[] col) {
        cols = new Column[numRows];
        for (int i = 0; i < numCols; i++) {
            cols[i] = new Column(colNames[i],colTypes[i],)
        }
    }*/
    Table(String tableName, String[] colTypes, String[] colNames) {
        cols = new Column[colNames.length];
        name = tableName;
        for (int i = 0; i < colNames.length; i++) {
            cols[i] = new Column(colNames[i], colTypes[i]);
        }
    }

    Table(String tableName, Column[] c) {
        name = tableName;
        cols = c;
    }

    public String insertRow(String[] literals) {
        for (int i = 0; i < cols.length; i++) {
            if (!literals[i].equals("NOVALUE")) {
                if (cols[i].getDataType().equals("int") &
                        !literals[i].matches("[-+]?\\d+")) {
                    return "ERROR: int data type expected.";
                } else if (cols[i].getDataType().equals("float") &
                        (!literals[i].matches("[-+]?\\d+\\.") &
                                !literals[i].matches("[-+]?\\.\\d+") &
                                !literals[i].matches("[-+]?\\d+\\.\\d+"))) {
                    return "ERROR: float data type expected.";
                } else if (cols[i].getDataType().equals("string") &
                        !literals[i].matches("\'\\s*\\S*(\\s*\\S*)+\'")) {
                    return "ERROR: string data type expected.";
                }
            }
        }
        for (int i = 0; i < cols.length; i++) {
            cols[i].addRow(literals[i]);
        }
        return "";
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
            } else {
                tableString = tableString + "," + cols[k].getColumnName() + " " + cols[k].getDataType();
            }
        }
        //remaining lines are the data
        for (int i = 0; i < cols[0].getSize(); i++) {
            for (int j = 0; j < cols.length; j++) {
                if (j == 0 & cols[j].getDataType().equals("float")
                        & !cols[j].getItem(i).equals("NOVALUE")) {
                    tableString += "\n" + floatFormat((String) cols[j].getItem(i));
                } else if (j == 0) {
                    tableString += "\n" + cols[j].getItem(i);
                } else if (cols[j].getDataType().equals("float")
                        & !cols[j].getItem(i).equals("NOVALUE")) {
                    tableString = tableString + "," + floatFormat((String) cols[j].getItem(i));
                } else {
                    tableString = tableString + "," + cols[j].getItem(i);
                }
            }
        }
        return tableString;
    }

    public Column[] stringToColumnArray(String[] str) {
        Column[] colArr = new Column[str.length];
        for (int j = 0; j < str.length; j++) {
            for (int i = 0; i < cols.length; i++) {
                if (str[j].equals(cols[i].getColumnName())) {
                    colArr[j] = new Column(str[j], cols[i].getDataType(), cols[i].getData());
                }
            }
        }
        return colArr;
    }

    public String selectString(Column[] col) {
        String tableString = "";
        int count = 0;
        for (Column c : col) {
            if (count == 0) {
                tableString = tableString + c.getColumnName() + " " + c.getDataType();
                count++;
            } else {
                tableString = tableString + "," + c.getColumnName() + " " + c.getDataType();
            }
        }
        for (int i = 0; i < cols[0].getSize(); i++) {
            for (int j = 0; j < col.length; j++) {
                if (j == 0 & col[j].getDataType().equals("float")
                        & !col[j].getItem(i).equals("NOVALUE")) {
                    tableString += "\n" + floatFormat((String) col[j].getItem(i));
                } else if (j == 0) {
                    tableString += "\n" + col[j].getItem(i);
                } else if (col[j].getDataType().equals("float")
                        & !col[j].getItem(i).equals("NOVALUE")) {
                    tableString = tableString + "," + floatFormat((String) col[j].getItem(i));
                } else {
                    tableString = tableString + "," + col[j].getItem(i);
                }
            }
        }
        return tableString;
    }

    private String floatFormat(String ff) {
        Pattern BACKDEC = Pattern.compile("(-|\\+)?(\\d+)(\\.)");
        Pattern FRONTDEC = Pattern.compile("(-|\\+)?(\\.)(\\d+)");
        Pattern MIDDLEDEC = Pattern.compile("(-|\\+)?(\\d+)(\\.)(\\d+)");
        String rts = "";
        Matcher m;
        if ((m = BACKDEC.matcher(ff)).matches()) {
            String sign = m.group(1);
            if (sign == null) {
                sign = "";
            }
            String[] digits = m.group(2).split("\\s*");
            String period = m.group(3);
            rts += sign;
            int i = 0;
            for (int k = 0; k < digits.length; k++) {
                if (digits[i].equals("0")) {
                    i++;
                } else {
                    break;
                }
            }
            for (int j = i; j < digits.length; j++) {
                rts += digits[j];
            }
            return rts + period;
        } else if ((m = FRONTDEC.matcher(ff)).matches()) {
            String sign = m.group(1);
            if (sign == null) {
                sign = "";
            }
            String[] digits = m.group(3).split("\\s*");
            String period = m.group(2);
            rts += sign + "0" + period;
            if (digits.length > 2) {
                for (int i = 0; i < 3; i++) {
                    rts += digits[i];
                }
                return rts;
            }
            for (int i = 0; i < digits.length; i++) {
                rts += digits[i];
            }
            for (int i = 0; i < 3 - digits.length; i++) {
                rts += "0";
            }
            return rts;
        } else if ((m = MIDDLEDEC.matcher(ff)).matches()) {
            String sign = m.group(1);
            if (sign == null) {
                sign = "";
            }
            String[] postdigits = m.group(4).split("\\s*");
            String period = m.group(3);
            String[] predigits = m.group(2).split("\\s*");
            rts += sign;
            int i = 0;
            for (int k = 0; k < predigits.length - 1; k++) {
                if (predigits[i].equals("0")) {
                    i++;
                } else {
                    break;
                }
            }
            for (int j = i; j < predigits.length; j++) {
                rts += predigits[j];
            }
            rts += period;
            if (postdigits.length > 2) {
                for (int j = 0; j < 3; j++) {
                    rts += postdigits[j];
                }
                return rts;
            }
            for (int j = 0; j < postdigits.length; j++) {
                rts += postdigits[j];
            }
            for (int j = 0; j < 3 - postdigits.length; j++) {
                rts += "0";
            }
            return rts;
        }
        return "";
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

    public ArrayList<String> getColNames() {
        ArrayList<String> colNames = new ArrayList<>();
        for (int i = 0; i < cols.length; i++) {
            colNames.add(cols[i].getColumnName());
        }
        return colNames;
    }
}
