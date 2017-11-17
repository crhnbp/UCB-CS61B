package db;
import java.util.ArrayList;

public class Row<T> {
    private ArrayList<T> row;

    /** ROW CONSTRUCTOR */
    public Row() {
        row = new ArrayList<>();
    }

    public Row(T[] items) {
        row = new ArrayList<>();
        for (T item : items){
            row.add(item);
        }
    }

    public void printRow() {
        for (int i = 0; i < row.size(); i += 1){
            System.out.print(row.get(i));
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Row<String> testRow1 = new Row<>();
        String[] s = {"USC", "UCLA", "UCSD"};
        Row<String> testRow2 = new Row<>(s);
        testRow1.printRow();
        testRow2.printRow();
    }
}
