package db;

import java.util.ArrayList;

public class Database {
    private static ArrayList<Table> tables;//change back to private
    private Handler handle;

    public Database() {
        tables = new ArrayList<>();
        handle = new Handler();
    }
    public static ArrayList<Table> getTables(){
        return tables;
    }

    public String transact(String query) {

        return "YOUR CODE HERE";
    }
    public static void main(String[] args) {
        //insertInto, createTable, store, load, dropTables
        Database db = new Database();
        /*String[] colTypes = {"int","int"};
        String[] colNames = {"x","y"};
        String[] data1 = {"4","5"};
        String[] data2 = {"6","7"};
        Handler.createTable("T1",colTypes,colNames);
        Handler.insertInto("T1",data1);
        Handler.insertInto("T1",data2);
        System.out.println(Handler.printTable("T1"));*/
        Handler.load("T1");
        System.out.println("Number of tables in database: " + tables.size());
        System.out.println(Handler.printTable("T1"));
        Handler.dropTable("T1");
        System.out.println("Number of tables in database: " + tables.size());
    }
}
