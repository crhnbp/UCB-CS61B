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

    int[] findItems (T item, String cond) {
        int[] pos = new int[100];
        for (int i = 0; i < col.size(); i += 1) {
            if (col.get(i).equals(item)){
                pos[pos.length] = i;
            }
        }
        return pos;
    }

    void compareItem (String cond, String type) {
        if (type.equals("string")){

        }
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
        testColumn.printColumn();
    }


}
