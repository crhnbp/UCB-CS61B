package db;

public class Column<T> {
    private String column_name;    // COLUMN NAME
    private LinkedListDeque<T> column;
    private int size;


    /** COLUMN CONSTRUCTOR */
    public Column (String name, T[] items) {
        column_name = name;
        column = new LinkedListDeque<>();
        for (T item : items){
            column.addLast(item);
        }
    }

    public void printColumn() {
        column.printDeque();
    }

    public static void main(String[] args) {
        String[] s = {"USC", "UCLA", "UCSD"};
        Column<String> testColumn = new Column<>("Test", s);
        testColumn.printColumn();
    }


}
