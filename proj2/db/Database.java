package db;

import org.junit.Test;
import edu.princeton.cs.algs4.In;

import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;

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
    private static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("-?[0-9]*");
        Matcher isInt = pattern.matcher(str);
        return isInt.matches();
    }

    /** Return true if a string represents a double. */
    private static boolean isFloat(String str) {
        Pattern pattern = Pattern.compile("-?([0-9]*\\.[0-9]+|[0-9]+)");
        Matcher isFlt = pattern.matcher(str);
        return isFlt.matches();
    }

    private static Table getTable(String tabName) {
        return TABLES.get(tabName);
    }

    // Various common constructs, simplifies parsing.
    private static final String REST  = "\\s*(.*)\\s*",
                                COMMA = "\\s*,\\s*",
                                AND   = "\\s+and\\s+",
                                AS    = "\\s+as\\s+",
                                COMP  = "\\s*(?:==|!=|<=|>=|<|>)\\s*",
                                LITERAL = "(?:[+-]?[0-9]*\\.?[0-9]+|'[^'\"\n\t,]*')",
                                ID    = "[A-Za-z](?:\\w|\\d)*",
                                COL_OPER = "\\s*[+\\-*/]\\s*",
                                ARITHMETIC = ID + COL_OPER + ID + "(?:\\s+as\\s+" + ID + ")";

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
            System.err.printf("ERROR: Malformed query: %s\n", query);
        }
    }

    private static void createTable(String expr) {
        Matcher m;
        if ((m = CREATE_NEW.matcher(expr)).matches()) {
            if (TABLES.containsKey(m.group(1))) {
                throw new InputMismatchException("ERROR: Table already exists: " + m.group(1));
            }
            createNewTable(m.group(1), m.group(2).split(COMMA));
        } else if ((m = CREATE_SEL.matcher(expr)).matches()) {
            createSelectedTable(m.group(1), m.group(2), m.group(3), m.group(4));
        } else {
            System.err.printf("ERROR: Malformed create: %s\n", expr);
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
        System.out.printf("You are trying to create a table named %s by selecting these expressions:" +
                 " '%s' from the join of these tables: '%s', filtered by these conditions: '%s'\n", name, exprs, tables, conds);
        if (conds == null) {
            conds = "";
        }
        Table selectTable = select(exprs, tables, conds);
        TABLES.put(name, selectTable); // Add new table into TABLES map

    }

    /** load <table name> */
     private static void loadTable(String tabName) {
        try {
            In readTags = new In(tabName + ".tbl");
            String tagLine = readTags.readLine();
            String[] tags = tagLine.split(COMMA);
            Table loaded = new Table(tabName, tags);
            while (!readTags.isEmpty()) {
                String l = readTags.readLine();
                String[] vals = l.split(COMMA);
                Entry e;
                for (int i = 0; i < vals.length; i++) {
                    if (isInteger(vals[i])) {
                        e = new IntEntry(vals[i]);
                    } else if (isFloat(vals[i])) {
                        e = new FloatEntry(vals[i]);
                    } else {
                        e = new StringEntry(vals[i]);
                    }
                    loaded.COLUMNS.get(i).addEntry(e);
                }
            }
            TABLES.put(tabName, loaded);
        } catch (Exception e) {
            System.out.printf("ERROR: TBL file not found: %s.tbl\n", tabName);
        }
        System.out.printf("You are trying to load the table named %s\n", tabName);
    }

    /** store <table name> */
     private static void storeTable(String tabName) {
        if (TABLES.containsKey(tabName)) {
            Table store = TABLES.get(tabName);
            try {
                File tbl = new File(tabName + ".tbl");
                PrintWriter writer = new PrintWriter(tbl);
                String tags = String.join(",", store.Tags);
                writer.println(tags);
                for (int i = 0; i < store.getRowNum(); i++) {
                    String[] re = store.getRow(i);
                    String entries = String.join(",", re);
                    writer.println(entries);
                }
                writer.close();
            } catch (IOException ie) {
                System.err.printf("ERROR: File not found: %s%n", ie.getMessage());
            }
        } else {
            throw new InputMismatchException("ERROR: No such table: " + tabName);
        }
        System.out.printf("You are trying to store the table named %s\n", tabName);
    }

    /** drop table <table name> */
     private static void dropTable(String name) {
        System.out.printf("You are trying to drop the table named %s\n", name);
        if (TABLES.containsKey(name)) {
            TABLES.remove(name);
        } else {
            throw new InputMismatchException("ERROR: NO SUCH TABLE:" + name);
        }
    }

    /** insert into <table name> values <literal0>,<literal1>,... */
     private static void insertRow(String expr) {
        Matcher m = INSERT_CLS.matcher(expr);
        if (!m.matches()) {
            System.err.printf("ERROR: Malformed insert: %s\n", expr);
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
            else if (isFloat(literals[i])) {    // if the literal represents a double
                row[i] = new FloatEntry(literals[i]);
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
        Table print = TABLES.get(t_name);
        String tags = String.join(",", print.Tags);
        System.out.println(tags);
        for (int i = 0; i < print.getRowNum(); i++) {
            String[] re = print.getRow(i);
            String entries = String.join(",", re);
            System.out.println(entries);
        }
    }

    private static Table merge(Table A, Table B, String TabName) {
        ArrayList<Column> commonColsA = new ArrayList<>();
        ArrayList<Column> commonColsB = new ArrayList<>();
        ArrayList<Column> uniqueColsA = new ArrayList<>();
        ArrayList<Column> uniqueColsB = new ArrayList<>();
        Table mergedTable = new Table(TabName);
        mergedTable.COLUMNS = new ArrayList<>();

        /** Scan all columns in Table A. */
        for (Column colA : A.COLUMNS) {
            try {
                Column colB = B.getColumn(colA.colName);
                if (!colB.colType.equals(colA.colType)) {
                    throw new InputMismatchException("ERROR: Incompatible types: " + colA.colType + " and " + colB.colType);
                }
                commonColsA.add(colA);
                commonColsB.add(colB);
            } catch (InputMismatchException e) {
                uniqueColsA.add(colA);
            }
        }

        /** Scan all columns in Table B. */
        for (Column colB : B.COLUMNS) {
            try {
                A.getColumn(colB.colName);
            } catch (InputMismatchException e) {
                uniqueColsB.add(colB);
            }
        }

        int cColsAnum = commonColsA.size();
        int uColsAnum = uniqueColsA.size();
        int uColsBnum = uniqueColsB.size();

        for (int i = 0; i < cColsAnum; i++) {
            Column comColA = new Column(commonColsA.get(i).colName, commonColsA.get(i).colType);
            mergedTable.addColumn(comColA);
        }

        for (int i = 0; i < uColsAnum; i++) {
            Column uniColA = new Column(uniqueColsA.get(i).colName, uniqueColsA.get(i).colType);
            mergedTable.addColumn(uniColA);
        }

        for (int i = 0; i < uColsBnum; i++) {
            Column uniColB = new Column(uniqueColsB.get(i).colName, uniqueColsB.get(i).colType);
            mergedTable.addColumn(uniColB);
        }

        if (cColsAnum > 0) {  // If two tables have shared columns...
            boolean haveCommon = false;  // whether shared columns have the same values...
            ArrayList<Integer> commonRowsA = new ArrayList<>();
            ArrayList<Integer> commonRowsB = new ArrayList<>();
            for (int i = 0; i < cColsAnum; i++) {
                Column currColA = commonColsA.get(i); // need revised
                Column currColB = commonColsB.get(i); // need revised
                for (int j = 0; j < currColA.size(); j++) {
                    for (int k = 0; k < currColB.size(); k++) {
                        if (currColA.getEntry(j).equals(currColB.getEntry(k))) {
                            commonRowsA.add(j);  // Store row number of the value
                            commonRowsB.add(k);
                            mergedTable.COLUMNS.get(i).addEntry(currColA.getEntry(j));  // Add value into mergedTable
                            haveCommon = true;
                        }
                    }
                }
            }

            if (haveCommon) {
                for (int i = cColsAnum; i < cColsAnum + uColsAnum; i++) {
                    for (int j = 0; j < commonRowsA.size(); j++) {
                        int rowNum = commonRowsA.get(j);
                        mergedTable.COLUMNS.get(i).addEntry(uniqueColsA.get(i - cColsAnum).getEntry(rowNum));
                    }
                }

                for (int i = cColsAnum + uColsAnum; i < cColsAnum + uColsAnum + uColsBnum; i++) {
                    for (int j = 0; j < commonRowsA.size(); j++) {
                        int rowNum = commonRowsB.get(j);
                        mergedTable.COLUMNS.get(i).addEntry(uniqueColsB.get(i - cColsAnum - uColsAnum).getEntry(rowNum));
                    }
                }
            }
        } else {  // Cartesian Product of two tables...
            for (int i = 0; i < A.getRowNum(); i++) {
                for (int j = 0; j < B.getRowNum(); j++) {
                    for (int m = 0; m < uColsAnum; m++) {
                        mergedTable.COLUMNS.get(m).addEntry(uniqueColsA.get(m).getEntry(i));
                    }
                    for (int n = 0; n < uColsBnum; n++) {
                        mergedTable.COLUMNS.get(uColsAnum+n).addEntry(uniqueColsB.get(n).getEntry(j));
                    }
                }
            }
        }
        return mergedTable;
    }

    private static void select(String expr) {
        Matcher m = SELECT_CLS.matcher(expr);
        if (!m.matches()) {
            System.err.printf("ERROR: Malformed select: %s\n", expr);
            return;
        }
        select(m.group(1), m.group(2), m.group(3));
    }

    private static Table selectJoinedTable(String[] tabNames, String rsltabName) {
        Table joinedTable = new Table("TEMP");
        for (int i = 0; i < tabNames.length; i++) {
            if (TABLES.containsKey(tabNames[i])) {
                if (joinedTable.Title.equals("TEMP")) {
                    joinedTable = TABLES.get(tabNames[i]);
                } else {
                    joinedTable = merge(joinedTable, TABLES.get(tabNames[i]), rsltabName);
                }
            } else {
                throw new InputMismatchException("ERROR: No such table: " + tabNames[i]);
            }
        }
        return joinedTable;
    }

    private static Table compoundSelect(String[] exprs, Table joined) {
        Table temp = new Table("TEMP");
        Matcher m;
        Pattern arithmetic = Pattern.compile(ARITHMETIC);
        for (String expr : exprs) {
            if ((m = arithmetic.matcher(expr)).matches()) {
                String[] components = expr.split(AS);
                String[] operands = components[0].split(COL_OPER);
                Column fstCol = joined.getColumn(operands[0]);
                Column secCol = joined.getColumn(operands[1]);
                String type = fstCol.colType;
                if (secCol.colType.equals("float")) {
                    type = secCol.colType;
                }
                Column resultColumn = joined.constructCol(components[1], type);
                for (int row = 0; row < joined.getRowNum(); row++) {
                    if (expr.contains("+")) {
                        resultColumn.addEntry(fstCol.getEntry(row).plus(secCol.getEntry(row)));
                    } else if (expr.contains("-")) {
                        resultColumn.addEntry(fstCol.getEntry(row).minus(secCol.getEntry(row)));
                    } else if (expr.contains("*")) {
                        resultColumn.addEntry(fstCol.getEntry(row).mul(secCol.getEntry(row)));
                    } else {
                        resultColumn.addEntry(fstCol.getEntry(row).div(secCol.getEntry(row)));
                    }
                }
                temp.addColumn(resultColumn);
            } else {
                temp.addColumn(joined.getColumn(expr));
            }
        }
        return temp;
    }

    /** select <column expr0>,<column expr1>,... from <table0>,<table1>,...
     * where <cond0> and <cond1> and ...
     */
     private static Table select(String exprs, String tables, String conds) {
        String[] selected_colName = exprs.split(COMMA); //i.e "X"
        String[] selected_tabName = tables.split(COMMA); //i.e "t"
        ArrayList<Column> selected_cols = new ArrayList<>();
        Table selected_table = selectJoinedTable(selected_tabName, "st");
        Table tempTable;
        Table resultTable;

        if (exprs.charAt(0) == '*') {
            tempTable = selected_table;
            selected_cols = tempTable.getColumns(selected_colName);
        } else {
            //tempTable = new Table("TEMP");
            //tempTable.COLUMNS = new ArrayList<>();
            tempTable = compoundSelect(selected_colName, selected_table);
            //tempTable.printTable();
            selected_cols = tempTable.getColumns(selected_colName);
        }

        /** When condition statements occur. */
        if (conds.length() > 0) {
            Pattern L = Pattern.compile(LITERAL);
            Matcher m;
            String[] conditions = conds.split(AND); //i.e "X > 'ha'"

            /** Construct a result table which has selected columns. */
            resultTable = new Table("RESULT");
            for (int i = 0; i < selected_colName.length; i++){
                Column addc = resultTable.constructCol(selected_cols.get(i).colName, selected_cols.get(i).colType);
                resultTable.addColumn(addc);
            }

            for (int i = 0; i < tempTable.getRowNum(); i++) {
                for (String condition : conditions) { // run through all conditional statements...
                    String[] operands = condition.split(COMP); // cond: ["X", ">", "ha"]
                    boolean flag = false;
                    for (Column tempCol : tempTable.COLUMNS) {
                        if (tempCol.colName.equals(operands[0])) {
                            if ((m = L.matcher(operands[1])).matches()) {
                                if (condition.contains("==")) {
                                    flag = tempCol.getEntry(i).equals(operands[1]);
                                }
                                if (condition.contains("!=")) {
                                    flag = !tempCol.getEntry(i).equals(operands[1]);
                                }
                                if (condition.contains(">")) {
                                    flag = tempCol.getEntry(i).isGreater(operands[1]);
                                }
                                if (condition.contains("<")) {
                                    flag = tempCol.getEntry(i).isLess(operands[1]);
                                }
                                if (condition.contains(">=")) {
                                    flag = tempCol.getEntry(i).equals(operands[1]) || tempCol.getEntry(i).isGreater(operands[1]);
                                }
                                if (condition.contains("<=")) {
                                    flag = tempCol.getEntry(i).equals(operands[1]) || tempCol.getEntry(i).isLess(operands[1]);
                                }
                            } else {
                                Column compCol = tempTable.getColumn(operands[1]);
                                if (condition.contains("==")) {
                                    flag = tempCol.getEntry(i).equals(compCol.getEntry(i));
                                }
                                if (condition.contains("!=")) {
                                    flag = !tempCol.getEntry(i).equals(compCol.getEntry(i));
                                }
                                if (condition.contains(">")) {
                                    flag = tempCol.getEntry(i).isGreater(compCol.getEntry(i));
                                }
                                if (condition.contains("<")) {
                                    flag = tempCol.getEntry(i).isLess(compCol.getEntry(i));
                                }
                                if (condition.contains(">=")) {
                                    flag = tempCol.getEntry(i).equals(compCol.getEntry(i)) || tempCol.getEntry(i).isGreater(compCol.getEntry(i));
                                }
                                if (condition.contains("<=")) {
                                    flag = tempCol.getEntry(i).equals(compCol.getEntry(i)) || tempCol.getEntry(i).isLess(compCol.getEntry(i));
                                }
                            }
                        }
                    }
                    if (flag) {
                        Entry[] addedEntry = new Entry[tempTable.COLUMNS.size()];
                        for (int j = 0; j < tempTable.COLUMNS.size(); j++) {
                            addedEntry[j] = tempTable.COLUMNS.get(j).getEntry(i);
                        }
                        resultTable.insert(addedEntry);
                    }
                }
            }

        } else {
            resultTable = tempTable;
        }

        System.out.printf("You are trying to select these expressions:" +
                 " '%s' from the join of these tables: '%s', filtered by these conditions: '%s'\n", exprs, tables, conds);
        resultTable.printTable();
        return resultTable;

    }

    @Test
    public void testStore() {
        String[] tag1 = {"X int", "Y string", "Z int"};
        Table testTable1 = new Table("T1", tag1);
        TABLES.put("T1", testTable1);
        Entry[] testRow1 = new Entry[3];
        testRow1[0] = new IntEntry("1");
        testRow1[1] = new StringEntry("d");
        testRow1[2] = new IntEntry("3");
        testTable1.insert(testRow1);
        Entry[] testRow2 = new Entry[3];
        testRow2[0] = new IntEntry("2");
        testRow2[1] = new StringEntry("b");
        testRow2[2] = new IntEntry("4");
        testTable1.insert(testRow2);
        testTable1.printTable();
        storeTable("T1");
    }

    @Test
    public void testLoadandPrint() {
        loadTable("records");
        printTable("records");
        loadTable("teams");
        printTable("teams");
    }

    @Test
    public void testSelect() {
        String[] tag1 = {"X int", "Y string", "Z int"};
        Table testTable1 = new Table("T1", tag1);
        TABLES.put("T1", testTable1);
        Entry[] testRow1 = new Entry[3];
        testRow1[0] = new IntEntry("1");
        testRow1[1] = new StringEntry("d");
        testRow1[2] = new IntEntry("3");
        testTable1.insert(testRow1);
        Entry[] testRow2 = new Entry[3];
        testRow2[0] = new IntEntry("2");
        testRow2[1] = new StringEntry("b");
        testRow2[2] = new IntEntry("4");
        testTable1.insert(testRow2);
        testTable1.printTable();

        String[] tag3 = {"M string", "N int"};
        Table testTable3 = new Table("T3", tag3);
        TABLES.put("T3", testTable3);
        Entry[] testRow3 = new Entry[2];
        testRow3[0] = new StringEntry("'da'");
        testRow3[1] = new IntEntry("2");
        testTable3.insert(testRow3);
        //testTable3.printTable();
        //Table r = select("X + Z as W", "T1", "");
        //r.printTable();

        createSelectedTable("yxz", "X, N", "T1, T3", "");
    }

    @Test
    public void testMerge() {
        String[] tag1 = {"X string", "Y string", "Z string"};
        Table testTable1 = new Table("T1", tag1);
        TABLES.put("T1", testTable1);
        Entry[] testRow1 = new Entry[3];
        testRow1[0] = new StringEntry("aa");
        testRow1[1] = new StringEntry("ba");
        testRow1[2] = new StringEntry("ca");
        testTable1.insert(testRow1);
        Entry[] testRow2 = new Entry[3];
        testRow2[0] = new StringEntry("da");
        testRow2[1] = new StringEntry("ea");
        testRow2[2] = new StringEntry("fa");
        testTable1.insert(testRow2);
        //testTable1.printTable();

        String[] tag2 = {"A string", "B string"};
        Table testTable2 = new Table("T2", tag2);
        TABLES.put("T2", testTable2);
        Entry[] testRow3 = new Entry[2];
        testRow3[0] = new StringEntry("ga");
        testRow3[1] = new StringEntry("ha");
        testTable2.insert(testRow3);
        Entry[] testRow4 = new Entry[2];
        testRow4[0] = new StringEntry("ia");
        testRow4[1] = new StringEntry("ja");
        testTable2.insert(testRow4);
        //testTable2.printTable();

        String[] tag3 = {"X string", "M string"};
        Table testTable3 = new Table("T3", tag3);
        TABLES.put("T3", testTable3);
        Entry[] testRow5 = new Entry[2];
        testRow5[0] = new StringEntry("aa");
        testRow5[1] = new StringEntry("ka");
        testTable3.insert(testRow5);
        Entry[] testRow6 = new Entry[2];
        testRow6[0] = new StringEntry("da");
        testRow6[1] = new StringEntry("la");
        testTable3.insert(testRow6);

        Table merged = merge(testTable1, testTable3, "MGD");
        merged.printTable();
    }
}
