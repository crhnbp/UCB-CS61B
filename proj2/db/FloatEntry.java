package db;

import java.util.InputMismatchException;

public class FloatEntry extends Entry<Float> {
    public FloatEntry() {
        this("NOVALUE");
    }

    public FloatEntry(String val) {
        nanValue = false;
        entryType = "float";
        if (val.equals("NOVALUE") || val.equals("")) {
            value = 0.0f;
            noValue = false;
        } else {
            value = Float.parseFloat(val);
            noValue = false;
        }
    }

    @Override
    public Entry plus(Entry e) {
        if (nanValue || e.nanValue) {
            return new FloatEntry("NaN");
        }
        else if (e.entryType.equals("int") || e.entryType.equals("float")) {
            return new FloatEntry(Float.toString(value + (Float) e.value));
        } else {
            throw new InputMismatchException("Wrong type");
        }
    }

    @Override
    public Entry minus(Entry e) {
        if (nanValue || e.nanValue) {
            return new FloatEntry("NaN");
        }
        else if (e.entryType.equals("int") || e.entryType.equals("float")) {
            return new FloatEntry(Float.toString(value - (Float) e.value));
        } else {
            throw new InputMismatchException("Wrong type");
        }
    }

    @Override
    public Entry mul(Entry e) {
        if (nanValue || e.nanValue) {
            return new FloatEntry("NaN");
        }
        else if (e.entryType.equals("int") || e.entryType.equals("float")) {
            return new FloatEntry(Float.toString(value * (Float) e.value));
        } else {
            throw new InputMismatchException("Wrong type");
        }
    }

    @Override
    public Entry div(Entry e) {
        if (nanValue || e.nanValue) {
            return new FloatEntry("NaN");
        }
        else if (e.entryType.equals("int") || e.entryType.equals("float")) {
            if ((float) e.value == 0) {
                return new FloatEntry("NaN");
            }
            return new FloatEntry(Float.toString(value / (Float) e.value));
        } else {
            throw new InputMismatchException("Wrong type");
        }
    }

    @Override
    public boolean equals(Entry e) {
        if (noValue || e.noValue) {
            return false;
        }
        else if (e.entryType.equals("int") || e.entryType.equals("float")){
            return value == (float) e.value;
        } else {
            throw new InputMismatchException("Wrong type");
        }
    }

    @Override
    public boolean isGreater(Entry e) {
        if (noValue || e.noValue) {
            return false;
        }
        else if (e.entryType.equals("int") || e.entryType.equals("float")) {
            return value > (float) e.value;
        } else {
            throw new InputMismatchException("Wrong type");
        }
    }

    @Override
    public boolean isLess(Entry e) {
        if (noValue || e.noValue) {
            return false;
        }
        else if (e.entryType.equals("int") || e.entryType.equals("float")) {
            return value < (float) e.value;
        } else {
            throw new InputMismatchException("Wrong type");
        }
    }

    @Override
    public boolean equals(String val) {
        if (noValue) {
            return false;
        }
        return value == Float.parseFloat(val);
    }

    @Override
    public boolean isGreater(String val) {
        if (noValue) {
            return false;
        }
        return value > Float.parseFloat(val);
    }

    @Override
    public boolean isLess(String val) {
        if (noValue) {
            return false;
        }
        return value < Float.parseFloat(val);
    }

    public FloatEntry duplicate(){
        return new FloatEntry(Float.toString(value));
    }
}
