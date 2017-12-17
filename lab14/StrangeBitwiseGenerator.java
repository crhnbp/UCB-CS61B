import lab14lib.Generator;

public class StrangeBitwiseGenerator implements Generator{
    private double period;
    private int state;

    public StrangeBitwiseGenerator(double period) {
        state = 0;
        this.period = period;
    }

    private double normalize(int v) {
        return ((v % this.period) * 2 / (this.period - 1)) - 1;
    }

    @Override
    public double next() {
        state = (state + 1);
        //int weirdState = state & (state >>> 3) % (int) period;
        //int weirdState = state & (state >> 3) & (state >> 8) % (int) period;
        int weirdState = state & (state >> 7) % (int) period;
        return normalize(weirdState);
    }
}
