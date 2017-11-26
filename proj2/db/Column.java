package db;

public class Column<T> {
    String colName;    // COLUMN NAME
    String colType;    // COLUMN TYPE
    LinkedListDeque<Entry> col;


    /** COLUMN CONSTRUCTOR */
    Column (String c_name, String c_type) {
        colName = c_name;
        colType = c_type;
        col = new LinkedListDeque<>();
    }

    /** Add entry to the end of the column. */
    void addEntry (Entry e) {
        if (colType.equals(e.entryType)) {
            col.addLast(e);
        } else {
            System.out.println("Wrong Type, column type is "+colType+" while entry type is "+ e.entryType);
        }
    }

    Entry<T> getEntry(int i) {
        Entry<T> targetEntry = col.get(i);
        Entry<T> newEntry = targetEntry.duplicate();
        return newEntry;
    }

    int size() {
        return col.size();
    }

    void printColumn() {
        col.printDeque();
    }

    public static void main(String[] args) {
        String[] s = {"USC", "UCLA", "UCSD"};
        Column<String> testColumn = new Column<>("Test", "String");
        //testColumn.printColumn();
    }

}
