package db;

public class IntEntry extends Entry<Integer> {
    public IntEntry() {
        this("NOVALUE");
    }

    public IntEntry(String val) {
        nanValue = false;
        entryType = "int";
        if (val.equals("NOVALUE") || val.equals("")) {
            value = 0;
            noValue = true;
        } else {
            value = Integer.parseInt(val);
            noValue = false;
        }
    }
}
