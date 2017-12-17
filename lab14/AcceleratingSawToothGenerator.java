import lab14lib.Generator;

public class AcceleratingSawToothGenerator implements Generator {
    private double period;
    private int state;
    private double factor;

    public AcceleratingSawToothGenerator(double period, double factor) {
        state = 0;
        this.period = period;
        this.factor = factor;
    }

    private double normalize(int v) {
        return ((v % this.period) * 2 / (this.period - 1)) - 1;
    }

    @Override
    public double next() {
        state = (state + 1);
        if (state % this.period == 0) {
            this.period *= this.factor;
            this.period = (int) this.period;
            state = 0;
        }
        return normalize(state);
    }
}
