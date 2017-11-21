package db;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.InputMismatchException;

public class Database {
    private static Map<String, Table> TABLES;

    public Database() {
        TABLES = new HashMap<>();
    }

    /** Return true if a string represents an integer. */
    private static boolean isInteger(String str){
        Pattern pattern = Pattern.compile("^-?[1-9]\\d*$");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    /** Return true if a string represents a double. */
    private static boolean isDouble(String str){
        Pattern pattern = Pattern.compile("^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    private static Table getTable(String tabName) {
        return TABLES.get(tabName);
    }

    private static ArrayList<Table> getTables(String[] st) {
        ArrayList<Table> selected_tables = new ArrayList<>();
        for (String s : st) {
            selected_tables.add(TABLES.get(s));
        }
        return selected_tables;
    }

    private static ArrayList<Column> getAllColumns(ArrayList<Table> st, String[] sc) {
        ArrayList<Column> selected_columns = new ArrayList<>();
        for (Table t : st) {
            selected_columns.addAll(t.getColumns(sc));
        }
        return selected_columns;
    }

    // Various common constructs, simplifies parsing.
    private static final String REST  = "\\s*(.*)\\s*",
                                COMMA = "\\s*,\\s*",
                                AND   = "\\s+and\\s+";

    // Stage 1 syntax, contains the command name.
    private static final Pattern CREATE_CMD = Pattern.compile("create table " + REST),
                                 LOAD_CMD   = Pattern.compile("load " + REST),
                                 STORE_CMD  = Pattern.compile("store " + REST),
                                 DROP_CMD   = Pattern.compile("drop table " + REST),
                                 INSERT_CMD = Pattern.compile("insert into " + REST),
                                 PRINT_CMD  = Pattern.compile("print " + REST),
                                 SELECT_CMD = Pattern.compile("select " + REST);

    // Stage 2 syntax, contains the clauses of commands.
    private static final Pattern CREATE_NEW  = Pattern.compile("(\\S+)\\s+\\(\\s*(\\S+\\s+\\S+\\s*" +
                                                               "(?:,\\s*\\S+\\s+\\S+\\s*)*)\\)"),
                                 SELECT_CLS  = Pattern.compile("([^,]+?(?:,[^,]+?)*)\\s+from\\s+" +
                                                               "(\\S+\\s*(?:,\\s*\\S+\\s*)*)(?:\\s+where\\s+" +
                                                               "([\\w\\s+\\-*/'<>=!.]+?(?:\\s+and\\s+" +
                                                               "[\\w\\s+\\-*/'<>=!.]+?)*))?"),
                                 CREATE_SEL  = Pattern.compile("(\\S+)\\s+as select\\s+" +
                                 SELECT_CLS.pattern()),
                                 INSERT_CLS  = Pattern.compile("(\\S+)\\s+values\\s+(.+?" +
                                                               "\\s*(?:,\\s*.+?\\s*)*)");

    public String transact(String query) {
        return null;
    }


    public void eval(String query) {
        Matcher m;
        if ((m = CREATE_CMD.matcher(query)).matches()) {
            createTable(m.group(1));
        } else if ((m = LOAD_CMD.matcher(query)).matches()) {
            loadTable(m.group(1));
        } else if ((m = STORE_CMD.matcher(query)).matches()) {
            storeTable(m.group(1));
        } else if ((m = DROP_CMD.matcher(query)).matches()) {
            dropTable(m.group(1));
        } else if ((m = INSERT_CMD.matcher(query)).matches()) {
            insertRow(m.group(1));
        } else if ((m = PRINT_CMD.matcher(query)).matches()) {
            printTable(m.group(1));
        } else if ((m = SELECT_CMD.matcher(query)).matches()) {
            select(m.group(1));
        } else {
            System.err.printf("Malformed query: %s\n", query);
        }
    }

    private static void createTable(String expr) {
        Matcher m;
        if ((m = CREATE_NEW.matcher(expr)).matches()) {
            createNewTable(m.group(1), m.group(2).split(COMMA));
        } else if ((m = CREATE_SEL.matcher(expr)).matches()) {
            createSelectedTable(m.group(1), m.group(2), m.group(3), m.group(4));
        } else {
            System.err.printf("Malformed create: %s\n", expr);
        }
    }

    /** create table <table name> (<column0 name> <type0>, <column1 name> <type1>, ...) */
     private static void createNewTable(String name, String[] cols) {
        Table testTable = new Table(name, cols);
        TABLES.put(name, testTable);
        testTable.printTable();
        //System.out.printf("You are trying to create a table named %s with the columns %s\n", name, colSentence);
    }

    /** create table <table name> as <select clause> */
     private static void createSelectedTable(String name, String exprs, String tables, String conds) {
        select(name, exprs, tables, conds);
        System.out.printf("You are trying to create a table named %s by selecting these expressions:" +
                " '%s' from the join of these tables: '%s', filtered by these conditions: '%s'\n", name, exprs, tables, conds);
    }

    /** load <table name> */
     private static void loadTable(String name) {
        System.out.printf("You are trying to load the table named %s\n", name);
    }

    /** store <table name> */
     private static void storeTable(String name) {
        System.out.printf("You are trying to store the table named %s\n", name);
    }

    /** drop table <table name> */
     private static void dropTable(String name) {
        System.out.printf("You are trying to drop the table named %s\n", name);
        if (TABLES.containsKey(name)) {
            TABLES.remove(name);
        } else {
            throw new InputMismatchException("NO SUCH TABLE:" + name);
        }
    }

    /** insert into <table name> values <literal0>,<literal1>,... */
     private static void insertRow(String expr) {
        Matcher m = INSERT_CLS.matcher(expr);
        if (!m.matches()) {
            System.err.printf("Malformed insert: %s\n", expr);
            return;
        }

        String tabName = m.group(1);
        Table t = getTable(tabName);

        String[] literals = m.group(2).split(COMMA);
        Entry[] row = new Entry[literals.length];
        for (int i = 0; i < literals.length; i++) {
            if (isInteger(literals[i])) {   // if the literal represents an integer
                row[i] = new IntEntry(literals[i]);
            }
            else if (isDouble(literals[i])) {    // if the literal represents a double
                row[i] = new DoubleEntry(literals[i]);
            } else {
                row[i] = new StringEntry(literals[i]); // if the literal represents a string
            }
        }

        t.insert(row);

        System.out.printf("You are trying to insert the row \"%s\" into the table %s\n", m.group(2), m.group(1));
    }

    /** print <table name> */
    /** Print the selected table stuff. */
    private static void printTable(String t_name) {
        System.out.printf("You are trying to print the table named %s\n", t_name);
        TABLES.get(t_name).printTable();
    }

    private static void select(String expr) {
        Matcher m = SELECT_CLS.matcher(expr);
        if (!m.matches()) {
            System.err.printf("Malformed select: %s\n", expr);
            return;
        }
        select(m.group(1), m.group(2), m.group(3));
    }

    /** create table <table name> as <select clause> */
    private static void select(String name, String exprs, String tables, String conds) {
        String[] selected_colName = exprs.split(COMMA); //i.e "X"
        String[] selected_tabName = tables.split(COMMA); //i.e "t"
        String[] conditions = conds.split(AND);
        Table selectTable = new Table(name); // Create empty table with specified name
        selectTable.COLUMNS = new ArrayList<>();
        ArrayList<Table> selected_tables = getTables(selected_tabName);
        ArrayList<Column> selected_columns = getAllColumns(selected_tables, selected_colName);
        selectTable.COLUMNS.addAll(selected_columns);
        TABLES.put(name, selectTable); // Add new table into TABLES array list
        selectTable.printTable();
        System.out.printf("You are trying to create table '%s' with selecting these expressions:" +
                " '%s' from the join of these tables: '%s', filtered by these conditions: '%s'\n", name, exprs, tables, conds);
    }

    /** select <column expr0>,<column expr1>,... from <table0>,<table1>,...
     * where <cond0> and <cond1> and ...
     */
     private static void select(String exprs, String tables, String conds) {
        String[] selected_colName = exprs.split(","); //i.e "X"
        String[] selected_tabName = tables.split(","); //i.e "t"

        String[] conditions = conds.split("and"); //i.e "X > 'ha'"
        Map<String, String> c = new HashMap<>();
        for (String condition : conditions) {
            String[] cond = condition.split(" "); // ["X", ">", "ha"]
            c.put(cond[0], cond[1]+cond[2]);
        }
        ArrayList<Table> selecttable = getTables(selected_tabName);
        ArrayList<Column> selectcols = new ArrayList<>();
        for (Table st : selecttable){
            ArrayList<Column> selectcolumn = st.getColumns(selected_colName);
            for (Column sc : selectcolumn) {
                selectcols.add(sc);
            }
        }

        System.out.printf("You are trying to select these expressions:" +
                " '%s' from the join of these tables: '%s', filtered by these conditions: '%s'\n", exprs, tables, conds);
    }

    @Test
    public void testSelect() {
        String[] tag1 = {"X string", "Y string"};
        Table testTable1 = new Table("T1", tag1);
        TABLES.put("T1", testTable1);
        Entry[] testRow1 = new Entry[2];
        testRow1[0] = new StringEntry("ha");
        testRow1[1] = new StringEntry("da");
        testTable1.insert(testRow1);
        //testTable1.printTable();

        String[] tag2 = {"A int", "B int"};
        Table testTable2 = new Table("T2", tag2);
        TABLES.put("T2", testTable2);
        //testTable2.printTable();

        String[] tag3 = {"M string", "N int"};
        Table testTable3 = new Table("T3", tag3);
        TABLES.put("T3", testTable3);
        Entry[] testRow2 = new Entry[2];
        testRow2[0] = new StringEntry("da");
        testRow2[1] = new IntEntry("2");
        testTable3.insert(testRow2);
        //testTable3.printTable();

        select("yxz", "X, M", "T1, T3", "");


    }

}
