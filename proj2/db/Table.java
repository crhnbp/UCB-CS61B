package db;

import java.util.ArrayList;
import java.util.InputMismatchException;

public class Table {
    ArrayList<Column> COLUMNS;
    String Title;
    String[] Tags;

    /** EMPTY TABLE CONSTRUCTOR */
    Table (String ttl) {
        Title = ttl;
    }

    Table (String ttl, String[] tgs) {    //ttl = "test", tgs = ["t1 string", "t2 string"]
        Title = ttl;
        Tags = tgs;

        int colNum = tgs.length;
        COLUMNS = new ArrayList<>();
        for (int i = 0; i < colNum; i++) {
            String[] c = tgs[i].split(" "); //c = ["t1", "string"]
            String c_name = c[0]; //c_name = "t1"
            String c_type = c[1]; //c_type = "string"
            COLUMNS.add(new Column(c_name, c_type));
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
        for (int i = 0; i < COLUMNS.size(); i += 1){
            System.out.println(COLUMNS.get(i).colName +" "+ COLUMNS.get(i).colType);
            COLUMNS.get(i).printColumn();
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
