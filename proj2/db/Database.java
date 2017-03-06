package db;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;

public class Database {
    private static ArrayList<Table> tables;

    public Database() {
        tables = new ArrayList<>();
    }

    public static ArrayList<Table> getTables() {
        return tables;
    }

    /**
     * Code skeleton taken from Parse.java provided by Josh Hug
     */
    // Various common constructs, simplifies parsing.
    private static final String REST = "\\s*(.*)\\s*",
            COMMA = "\\s*,\\s*",
            AND = "\\s+and\\s+";

    /**
     * Code skeleton taken from Parse.java provided by Josh Hug
     */
    // Stage 1 syntax, contains the command name.
    private static final Pattern CREATE_CMD = Pattern.compile("create table " + REST),
            LOAD_CMD = Pattern.compile("load " + REST),
            STORE_CMD = Pattern.compile("store " + REST),
            DROP_CMD = Pattern.compile("drop table " + REST),
            INSERT_CMD = Pattern.compile("insert into " + REST),
            PRINT_CMD = Pattern.compile("print " + REST),
            SELECT_CMD = Pattern.compile("select " + REST);

    /**
     * Code skeleton taken from Parse.java provided by Josh Hug
     */
    // Stage 2 syntax, contains the clauses of commands.
    private static final Pattern CREATE_NEW = Pattern.compile("(\\S+)\\s+\\((\\S+\\s+\\S+\\s*"
            + "(?:,\\s*\\S+\\s+\\S+\\s*)*)\\)"),
            SELECT_CLS = Pattern.compile("([^,]+?(?:,[^,]+?)*)\\s+from\\s+"
                    + "(\\S+\\s*(?:,\\s*\\S+\\s*)*)(?:\\s+where\\s+"
                    + "([\\w\\s+\\-*/'<>=!]+?(?:\\s+and\\s+"
                    + "[\\w\\s+\\-*/'<>=!]+?)*))?"),
            CREATE_SEL = Pattern.compile("(\\S+)\\s+as select\\s+" + SELECT_CLS.pattern()),
            INSERT_CLS = Pattern.compile("(\\S+)\\s+values\\s+(.+?"
                    + "\\s*(?:,\\s*.+?\\s*)*)");

    public static String transact(String query) {
        Matcher m;
        if ((m = CREATE_CMD.matcher(query)).matches()) {
            return createTable(m.group(1));
        } else if ((m = LOAD_CMD.matcher(query)).matches()) {
            return Handler.load(m.group(1));
        } else if ((m = STORE_CMD.matcher(query)).matches()) {
            return Handler.store(m.group(1));
        } else if ((m = DROP_CMD.matcher(query)).matches()) {
            return Handler.dropTable(m.group(1));
        } else if ((m = INSERT_CMD.matcher(query)).matches()) {
            return insertRow(m.group(1));
        } else if ((m = PRINT_CMD.matcher(query)).matches()) {
            return Handler.printTable(m.group(1));
        } else if ((m = SELECT_CMD.matcher(query)).matches()) {
            return select(m.group(1));
        } else {
            return "ERROR: Malformed query: " + query + "\n";
        }
    }

    /**
     * Code skeleton taken from Parse.java provided by Josh Hug
     */
    private static String createTable(String expr) {
        Matcher m;
        if ((m = CREATE_NEW.matcher(expr)).matches()) {
            return createNewTable(m.group(1), m.group(2).split(COMMA));
        } else if ((m = CREATE_SEL.matcher(expr)).matches()) {
            createSelectedTable(m.group(1), m.group(2), m.group(3), m.group(4));
        } else {
            return "ERROR: Malformed create: " + expr + "\n";
        }
        return "";
    }

    /**
     * Code skeleton taken from Parse.java provided by Josh Hug
     */
    private static String createNewTable(String name, String[] cols) {
        String regex = "\\s+";
        Pattern p = Pattern.compile(regex);
        String[] colNames = new String[cols.length];
        String[] colTypes = new String[cols.length];
        for (int i = 0; i < cols.length; i++) {
            String[] line = p.split(cols[i]);
            colNames[i] = line[0];
            colTypes[i] = line[1];
        }
        for (String colT : colTypes) {
            if (!colT.equals("int") && !colT.equals("float") && !colT.equals("string")) {
                return "ERROR: Incorrect type of data.";
            }
        }
        return Handler.createTable(name, colTypes, colNames);
    }

    /**
     * Code skeleton taken from Parse.java provided by Josh Hug
     */
    private static String createSelectedTable(String name, String inE, String inT, String inC) {
        Pattern p1 = Pattern.compile("\\s+|,");
        Pattern p2 = Pattern.compile(AND);
        String[] exprs = p1.split(inE);
        String[] tableNames = p1.split(inT);
        String[] conds = p2.split(inC);
        if (conds == null) {
            return Handler.createTable(name, Handler.selectTable(tableNames, exprs));
        } else {
            return Handler.selectTable(tableNames, exprs, conds);
        }
    }

    /**
     * Code skeleton taken from Parse.java provided by Josh Hug
     */
    private static String insertRow(String expr) {
        Matcher m = INSERT_CLS.matcher(expr);
        if (!m.matches()) {
            return "ERROR: Malformed insert: " + expr + "\n";
        }
        Pattern p = Pattern.compile(",");
        String[] literals = p.split(m.group(2));
        return Handler.insertInto(m.group(1), literals);
    }

    /**
     * Code skeleton taken from Parse.java provided by Josh Hug
     */
    private static String select(String expr) {
        Matcher m = SELECT_CLS.matcher(expr);
        if (!m.matches()) {
            return "ERROR: Malformed select: " + expr + "\n";
        }
        Pattern p1 = Pattern.compile("\\s+|,");
        Pattern p2 = Pattern.compile(AND);
        String[] exprs = p1.split(m.group(1));
        String[] tableNames = p1.split(m.group(2));
        String conditions = m.group(3);
        if (conditions == null) {
            return Handler.selectTable(tableNames, exprs);
        }
        String[] conds = p2.split(conditions);
        return Handler.selectTable(tableNames, exprs, conds);
    }
    public static void main(String[] args) {
        Database db = new Database();
        Handler.load("T7");
        Handler.load("T8");
        System.out.println(Handler.join(tables.get(0),tables.get(1)).toString());
    }
}