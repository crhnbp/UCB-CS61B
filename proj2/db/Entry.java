package db;

public class Entry<T> {
    T value;
    String entryType;
    boolean noValue;
    boolean nanValue;

    /** Empty entry constructor */
    public Entry() {
    }

    /** Entry constructor with specified value */
    public Entry(T val) {
        value = val;
        if (val.equals("NOVALUE")) {
            noValue = true;
        } else {
            noValue = false;
        }
    }

    /** Returns the string representation of the entry
     *  if nanValue, then "NaN".
     *  if noValue, then "NOVALUE" */
    @Override
    public String toString() {
        if (nanValue) {
            return "NaN";
        }
        if (noValue) {
            return "NOVALUE";
        }
        return value.toString();
    }

    /** Return value of the entry */
    public T getValue() {
        return value;
    }

    public Entry<T> plus(Entry other) {
        return null;
    }

    public Entry<T> minus(Entry other) {
        return null;
    }

    public Entry<T> star(Entry other) {
        return null;
    }

    public Entry<T> div(Entry other) {
        return null;
    }

    public boolean equal(Entry other) {
        return true;
    }

    public boolean gt(Entry other) {
        return true;
    }

    public boolean lt(Entry other) {
        return true;
    }

    public boolean equal(String other) {
        return true;
    }

    public boolean gt(String other) {
        return true;
    }

    public boolean lt(String other) {
        return true;
    }

    public Entry duplicate() {
        return new Entry(value);
    }

}
