package db;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.InputMismatchException;

public class Table {
    ArrayList<Column> COLUMNS;
    String Title;
    ArrayList<String> Tags;
    Map<String, Column> colMap;

    /** EMPTY TABLE CONSTRUCTOR */
    Table (String ttl) {
        Title = ttl;
        COLUMNS = new ArrayList<>();
        Tags = new ArrayList<>();
        colMap = new HashMap<>();
    }

    Table (String ttl, String[] tgs) {    //ttl = "test", tgs = ["t1 string", "t2 string"]
        Title = ttl;
        COLUMNS = new ArrayList<>();
        Tags = new ArrayList<>();
        colMap = new HashMap<>();

        int colNum = tgs.length;

        for (int i = 0; i < colNum; i++) {
            String[] c = tgs[i].split(" "); //c = ["t1", "string"]
            String c_name = c[0]; //c_name = "t1"
            String c_type = c[1]; //c_type = "string"
            Tags.add(c_name + " " + c_type);
            Column newCol = constructCol(c_name, c_type);
            COLUMNS.add(newCol);
            colMap.put(c_name, newCol);
        }
    }

    public Column constructCol(String name, String type) {
        if (type.equals("int")) {
            return new Column<Integer>(name, type);
        }
        else if (type.equals("float")) {
            return new Column<Float>(name, type);
        }
        else if (type.equals("string")) {
            return new Column<String>(name, type);
        } else {
            throw new InputMismatchException("Invalid type: type");
        }
    }

    public void insert(Entry[] entries) {
        if (entries.length != COLUMNS.size()) {
            throw new InputMismatchException("Number of items not");
        }

        int i = 0;
        for (Entry e : entries) {
            COLUMNS.get(i).addEntry(e);
            i += 1;
        }
    }

    public int getRowNum() {
        return COLUMNS.get(0).size();
    }

    public String[] getRow(int index) {
        String[] rowEntries = new String[COLUMNS.size()];
        for (int i = 0; i < COLUMNS.size(); i++) {
            for (int j = 0; j < COLUMNS.get(i).size(); j++) {
                if (j == index) {
                    rowEntries[i] = COLUMNS.get(i).col.get(j).toString();
                }
            }
        }
        return rowEntries;
    }

    public void addColumn(Column c) {
        Tags.add(c.colName + " " + c.colType);
        COLUMNS.add(c);
        colMap.put(c.colName, c);
    }

    public Column getColumn(String cn) {
        if (colMap.containsKey(cn)) {
            //System.out.println("Here is: " + cn);
            return colMap.get(cn);
        } else {
            throw new InputMismatchException("No such column: " + cn);
        }
    }

    public ArrayList<Column> getColumns(String[] scn) {
        ArrayList<Column> selected_columns = new ArrayList<>();
        for (Column c : COLUMNS) {
            for (String s : scn) {
                if (c.colName.equals(s)){
                    selected_columns.add(c);
                }
            }
        }
        return selected_columns;
    }

    void printTable() {
        String tags = String.join(",", Tags);
        System.out.println(tags);
        for (int i = 0; i < getRowNum(); i++) {
            String[] re = getRow(i);
            String entries = String.join(",", re);
            System.out.println(entries);
        }
    }

    public static void main(String[] args) {
        String[] tag = {"X string", "Y string"};
        Table testTable = new Table("T1", tag);

        Entry[] testRow1 = new StringEntry[2];
        testRow1[0] = new StringEntry("ha");
        testRow1[1] = new StringEntry("da");
        testTable.insert(testRow1);

        Entry[] testRow2 = new StringEntry[2];
        testRow2[0] = new StringEntry("la");
        testRow2[1] = new StringEntry("ra");
        testTable.insert(testRow2);
        testTable.printTable();

        String[] c = {"X"};
        ArrayList<Column> selectcolumn = testTable.getColumns(c);
        for (Column sc : selectcolumn) {
            sc.printColumn();
        }
        //testTable.printTable();
    }
}
