package db;

import java.util.InputMismatchException;

public class StringEntry extends Entry<String> {

    public StringEntry() {
        this("NOVALUE");
    }

    public StringEntry(String val) {
        nanValue = false;
        entryType = "string";
        if (val.equals("NOVALUE") || val.equals("")) {
            value = "";
            noValue = true;
        } else {
            value = val;
            noValue = false;
        }
    }

    public StringEntry plus(StringEntry se) {
        return new StringEntry(this.getValue() + se.getValue());
    }

    public StringEntry minus(StringEntry se) {
        throw new InputMismatchException("ERROR: string does not support - operator.");
    }

    public StringEntry mul(StringEntry se) {
        throw new InputMismatchException("ERROR: string does not support * operator.");
    }

    public StringEntry div(StringEntry se) {
        throw new InputMismatchException("ERROR: string does not support / operator.");
    }

    public boolean equal(StringEntry se) {
        if (noValue || se.noValue) {
            return false;
        }
        return value.equals(se.value);
    }
}
