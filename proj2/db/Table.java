package db;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

public class Table {
    private LinkedListDeque[] Columns;
    private LinkedListDeque<Row> Rows;
    private String Title;
    private Map<String, Object> Tags;

    /** EMPTY TABLE CONSTRUCTOR */
    private Table (String tl, Map<String, Object> tgs) {
        Title = tl;
        Tags = tgs;
        Rows = new LinkedListDeque<>();
    }

    public Row getRow(int rnum) {
        return Rows.get(rnum);
    }

    private void addRow(Row row) {
        Rows.addLast(row);
    }

    public void printTable() {
        for(Map.Entry<String, Object> entry : Tags.entrySet()){
            System.out.print(entry.getKey() + entry.getValue());
        }

        System.out.println();
        for (int i = 0; i < Rows.size(); i += 1){
            Row r = Rows.get(i);
            r.printRow();
        }

    }

    public static void main(String[] args) {
        Map<String, Object> tag = new HashMap<>();
        tag.put("X", "int");
        tag.put("Y", "int");
        Table testTable = new Table("T1", tag);

        int[] r1 = {2, 5};
        Integer[] r1t = Arrays.stream(r1).boxed().toArray( Integer[]::new );
        Row<Integer> testRow1 = new Row<>(r1t);
        testTable.addRow(testRow1);

        int[] r2 = {8, 3};
        Integer[] r2t = Arrays.stream(r2).boxed().toArray( Integer[]::new );
        Row<Integer> testRow2 = new Row<>(r2t);
        testTable.addRow(testRow2);

        int[] r3 = {13, 7};
        Integer[] r3t = Arrays.stream(r3).boxed().toArray( Integer[]::new );
        Row<Integer> testRow3 = new Row<>(r3t);
        testTable.addRow(testRow3);
        testTable.printTable();
    }
}
