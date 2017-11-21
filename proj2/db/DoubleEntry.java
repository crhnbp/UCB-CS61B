package db;

public class DoubleEntry extends Entry<Double> {
    public DoubleEntry() {
        this("NOVALUE");
    }

    public DoubleEntry(String val) {
        nanValue = false;
        entryType = "double";
        if (val.equals("NOVALUE") || val.equals("")) {
            value = 0.0;
            noValue = false;
        } else {
            value = Double.parseDouble(val);
            noValue = false;
        }
    }
}
