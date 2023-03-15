package my.ylab.homework02.accumulator;

public class StatsAccumulatorImpl implements StatsAccumulator{
    private int sum;

    private int min;

    private int max;

    private int count;

    private double avg;

    public StatsAccumulatorImpl() {
        this.sum = 0;
        this.min = Integer.MIN_VALUE;
        this.max = Integer.MAX_VALUE;
        this.count = 0;
        this.avg = 0;
    }

    @Override
    public void add(int value) {
        this.sum = this.sum + value;
        this.min = (this.min != Integer.MIN_VALUE) ? Math.min(this.min, value) : value;
        this.max = (this.max != Integer.MAX_VALUE) ? Math.max(this.max, value) : value;
        this.count++;
        this.avg = (this.count != 0) ? (double) this.sum/this.count : 0;
    }
    @Override
    public int getMin() {
        return this.min;
    }

    @Override
    public int getMax() {
        return this.max;
    }

    @Override
    public int getCount() {
        return this.count;
    }

    @Override
    public Double getAvg() {
        return this.avg;
    }
}
