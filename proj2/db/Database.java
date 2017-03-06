package db;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;

public class Database {
    private ArrayList<Table> tables;

    public Database() {
        tables = new ArrayList<>();
    }

    public ArrayList<Table> getTables() {
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

    public String transact(String query) {
        Matcher m;

        if ((m = CREATE_CMD.matcher(query)).matches()) {
            return createTable(m.group(1));
        } else if ((m = LOAD_CMD.matcher(query)).matches()) {
            return Handler.load(m.group(1), this);
        } else if ((m = STORE_CMD.matcher(query)).matches()) {
            return Handler.store(m.group(1), this);
        } else if ((m = DROP_CMD.matcher(query)).matches()) {
            return Handler.dropTable(m.group(1), this);
        } else if ((m = INSERT_CMD.matcher(query)).matches()) {
            return insertRow(m.group(1));
        } else if ((m = PRINT_CMD.matcher(query)).matches()) {
            return Handler.printTable(m.group(1), this);
        } else if ((m = SELECT_CMD.matcher(query)).matches()) {
            return select(m.group(1));
        } else {
            return "ERROR: Malformed query: " + query + "\n";
        }
    }

    /**
     * Code skeleton taken from Parse.java provided by Josh Hug
     */
    private String createTable(String expr) {
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
    private String createNewTable(String name, String[] cols) {
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
        return Handler.createTable(name, colTypes, colNames, this);
    }

    /**
     * Code skeleton taken from Parse.java provided by Josh Hug
     */
    private String createSelectedTable(String name, String inE, String inT, String inC) {
        Pattern p1 = Pattern.compile(COMMA);
        Pattern p2 = Pattern.compile(AND);
        Pattern p3 = Pattern.compile("([']?\\w+[']?)\\s*"
                + "([><!=]+)\\s*([']?\\s*\\w+\\s*[']?)");
        Pattern p4 = Pattern.compile("([']?\\w+\\.?\\w*[']?)"
                + "\\s*([\\+\\*\\-/]+)\\s*([']?\\w+\\.?\\w*[']?)"
                + "\\s*as\\s*(\\w+)");
        String[] exprs = p1.split(inE);
        String[] tableNames = p1.split(inT);
        String[] conds;
        ArrayList<Table> data = getTables();
        Matcher m1;
        for (String t : tableNames) {
            int i = Handler.isData(t, this);
            if (i != -1) {
                for (String c : exprs) {
                    if ((m1 = p4.matcher(c)).matches()) {
                        int g1 = Handler.isColumn(m1.group(1), i, this);
                        int g2 = Handler.isColumn(m1.group(3), i, this);
                        String val = expressionTypeCheck(g1, g2, m1.group(1),
                                m1.group(3), i, m1.group(2));
                        if (!val.equals("")) {
                            return val;
                        }
                    } else {
                        int k = Handler.isColumn(c, i, this);
                        if (k == -1) {
                            return "ERROR: No column " + c + " in table.";
                        }
                    }
                }
            } else {
                return "ERROR: No table " + t + " in database.";
            }
        }
        if (inC == null) {
            return Handler.createTable(name, Handler.selectTable(tableNames, exprs, this), this);
        } else {
            conds = p2.split(inC);
            for (String c : conds) {
                if (!p3.matcher(c).matches()) {
                    return "ERROR: Incorrect condition statement: " + c;
                }
            }
            return Handler.createTable(name,
                    Handler.selectTable(tableNames, exprs, conds, this), this);
        }
    }

    /**
     * Code skeleton taken from Parse.java provided by Josh Hug
     */
    private String insertRow(String expr) {
        Matcher m = INSERT_CLS.matcher(expr);
        if (!m.matches()) {
            return "ERROR: Malformed insert: " + expr + "\n";
        }
        Pattern p = Pattern.compile(",");
        String[] literals = p.split(m.group(2));
        return Handler.insertInto(m.group(1), literals, this);
    }

    /**
     * Code skeleton taken from Parse.java provided by Josh Hug
     */
    private String select(String expr) {
        Matcher m = SELECT_CLS.matcher(expr);
        if (!m.matches()) {
            return "ERROR: Malformed select: " + expr + "\n";
        }
        Pattern p1 = Pattern.compile(COMMA);
        Pattern p2 = Pattern.compile(AND);
        Pattern p3 = Pattern.compile("([']?\\w+[']?)\\s*"
                + "([><!=]+)\\s*([']?\\s*\\w+\\s*[']?)");
        Pattern p4 = Pattern.compile("([']?\\w+\\.?\\w*[']?)"
                + "\\s*([\\+\\*\\-/]+)\\s*([']?\\w+\\.?\\w*[']?)"
                + "\\s*as\\s*(\\w+)");
        Matcher m1;
        String[] exprs = p1.split(m.group(1));
        String[] tableNames = p1.split(m.group(2));
        for (String t : tableNames) {
            int i = Handler.isData(t, this);
            if (i != -1) {
                for (String c : exprs) {
                    if ((m1 = p4.matcher(c)).matches()) {
                        int g1 = Handler.isColumn(m1.group(1), i, this);
                        int g2 = Handler.isColumn(m1.group(3), i, this);
                        String val = expressionTypeCheck(g1, g2, m1.group(1),
                                m1.group(3), i, m1.group(2));
                        if (!val.equals("")) {
                            return val;
                        }
                    } else {
                        int k = Handler.isColumn(c, i, this);
                        if (k == -1) {
                            return "ERROR: No column " + c + " in table.";
                        }
                    }
                }
            } else {
                return "ERROR: No table " + t + " in database.";
            }
        }
        String conditions = m.group(3);
        if (conditions == null) {
            return Handler.selectTable(tableNames, exprs, this);
        }
        String[] conds = p2.split(conditions);
        for (String c : conds) {
            if (!p3.matcher(c).matches()) {
                return "ERROR: Incorrect condition statement: " + c;
            }
        }
        return Handler.selectTable(tableNames, exprs, conds, this);
    }

    private String expressionTypeCheck(int g1, int g2, String lit1, String lit2, int index, String op) {
        ArrayList<Table> data = getTables();
        String g1Type;
        String g2Type;
        if (g1 != -1 && g2 != -1) {
            g1Type = data.get(index).getCols()[g1].getDataType();
            g2Type = data.get(index).getCols()[g2].getDataType();
            if ((g1Type.equals("string") | g2Type.equals("string"))
                    && !g1Type.equals(g2Type)) {
                return "ERROR: Trying to combine " + g1Type + " with " + g2Type;
            }
        } else if (g1 == -1 && g2 != -1) {
            g2Type = data.get(index).getCols()[g2].getDataType();
            g1Type = typeCheck(lit1);
            if (g1Type.equals("")) {
                return "ERROR: malformed data input." + lit1;
            }
            if ((g1Type.equals("string") | g2Type.equals("string"))
                    && !g1Type.equals(g2Type)) {
                return "ERROR: Trying to combine " + g1Type + " with " + g2Type;
            }
        } else if (g1 != -1) {
            g1Type = data.get(index).getCols()[g1].getDataType();
            g2Type = typeCheck(lit2);
            if (g2Type.equals("")) {
                return "ERROR: malformed data input: " + lit2;
            }
            if ((g1Type.equals("string") | g2Type.equals("string"))
                    && !g1Type.equals(g2Type)) {
                return "ERROR: Trying to combine " + g1Type + " with " + g2Type;
            }

        } else {
            return "ERROR: trying to combine literals in select: " + lit1
                    + " " + lit2;
        }
        if (g1Type.equals("string") && g2Type.equals("string") && !op.equals("+")) {
            return "ERROR: Cannot manipulate strings in that way.";
        }
        return "";
    }

    private String typeCheck(String matcher) {
        if (matcher.matches("[-+]?\\d+")) {
            return "int";
        } else if (matcher.matches("[-+]?\\d+\\.")
                | matcher.matches("[-+]?\\.\\d+")
                | matcher.matches("[-+]?\\d+\\.\\d+")) {
            return "float";
        } else if (matcher.matches("\'\\s*\\S*(\\s*\\S*)+\'")) {
            return "string";
        } else {
            return "";
        }
    }

    public static void main(String[] args) {
        /*System.out.println("Steelers".compareTo("Mets"));
        System.out.println("Golden Bears".compareTo("Mets"));
        System.out.println("Mets".compareTo("Mets"));
        System.out.println("Patriots".compareTo("Mets"));*/
        Database db = new Database();
        Handler.load("T6", db);
        String[] tableName = {"T6"};
        String[] exprs = {"TeamName", "Season", "Wins", "Losses"};
        String[] conds = {"Wins != Losses", "Losses > 6", "Season < 2015"};
        db.transact("select TeamName,Season,Wins,Losses from T6 where Wins != Losses and Losses > 6 and Season < 2015");
        System.out.println(Handler.selectTable(tableName, exprs, conds, db));
        //String[] tableName, String[] expr, String[] cond, Database db
        //Handler.load("T6", db);
        //System.out.println(Handler.cartesian(db.getTables().get(0),
        //db.getTables().get(1)).toString());
    }
}
