package db; /**
 * Created by kevin on 2/27/2017.
 */

import java.util.ArrayList;

public class Column<T> {
    private String columnName;
    private String dataType;
    private ArrayList<T> data;
    private int size;

    public Column(String colName, String colType, ArrayList<T> colData) {
        dataType = colType;
        columnName = colName;
        data = colData;
        size = colData.size();
    }

    public Column(String colName, String colType) {
        columnName = colName;
        dataType = colType;
        data = new ArrayList<T>();
        size = 0;
    }

    public void addRow(T x) {
        data.add(x);
        size++;
    }

    public void setColumnName(String name) {
        columnName = name;
    }

    public void setDataType(String d) {
        dataType = d;
    }

    public void setData(ArrayList<T> d) {
        data = d;
        size = d.size();
    }

    public void setSize(int s) {
        size = s;
    }

    public void setIndex(int index, T item) {
        data.set(index, item);
    }

    public String getColumnName() {
        return columnName;
    }

    public String getDataType() {
        return dataType;
    }

    public ArrayList<T> getData() {
        return data;
    }

    public int getSize() {
        return size;
    }

    public T getItem(int index) {
        return data.get(index);
    }

    public String toString() {
        String s = columnName;
        for (int i = 0; i < data.size(); i++) {
            s = s + "\n" + data.get(i);
        }
        return s;
    }

    public void removeRow(int index) {
        data.remove(index);
        size--;
    }
}
