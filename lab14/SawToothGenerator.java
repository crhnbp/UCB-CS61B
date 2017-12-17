import lab14lib.Generator;

public class SawToothGenerator implements Generator {
    private double period;
    private int state;

    public SawToothGenerator(double period) {
        state = 0;
        this.period = period;
    }

    private double normalize(int v) {
        return ((v % this.period) * 2 / (this.period - 1)) - 1;
    }

    @Override
    public double next() {
        state = (state + 1);
        return normalize(state);
    }
}
