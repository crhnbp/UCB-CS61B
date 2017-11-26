package db;

import org.junit.Test;

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

    @Override
    public StringEntry plus(Entry e) {
        return new StringEntry(this.getValue() + e.getValue());
    }

    @Override
    public StringEntry minus(Entry e) {
        throw new InputMismatchException("ERROR: string does not support - operator.");
    }

    @Override
    public StringEntry mul(Entry e) {
        throw new InputMismatchException("ERROR: string does not support * operator.");
    }

    @Override
    public StringEntry div(Entry e) {
        throw new InputMismatchException("ERROR: string does not support / operator.");
    }

    @Override
    public boolean equals(Entry e) {
        if (noValue || e.noValue) {
            return false;
        }
        return value.equals(e.value);
    }

    @Override
    public boolean isGreater(Entry e) {
        if (noValue || e.noValue) {
            return false;
        }
        if (e.entryType.equals("string")) {
            return value.compareTo((String) e.value) > 0;
        } else {
            throw new InputMismatchException("Cannot be compared");
        }
    }

    @Override
    public boolean isLess(Entry e) {
        if (noValue || e.noValue) {
            return false;
        }
        if (e.entryType.equals("string")) {
            return value.compareTo((String) e.value) < 0;
        } else {
            throw new InputMismatchException("Cannot be compared");
        }
    }

    @Override
    public boolean equals(String val) {
        if (noValue) {
            return false;
        }
        return value.compareTo(val) == 0;
    }

    @Override
    public boolean isGreater(String val) {
        if (noValue) {
            return false;
        }
        return value.compareTo(val) > 0;
    }

    @Override
    public boolean isLess(String val) {
        if (noValue) {
            return false;
        }
        return value.compareTo(val) < 0;
    }

    public StringEntry duplicate(){
        return new StringEntry(value);
    }
}
