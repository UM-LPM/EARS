package org.um.feri.ears.statistic.rating_system.true_skill.factorgraphs;

public class ScheduleLoop<T> extends Schedule<T> {

    private static final int MAX_ITERATIONS = 100;

    private final double maxDelta;
    private final Schedule<T> scheduleToLoop;

    public ScheduleLoop(String name, Schedule<T> scheduleToLoop, double maxDelta) {
        super(name);
        this.scheduleToLoop = scheduleToLoop;
        this.maxDelta = maxDelta;
    }

    @Override
    public double visit(int depth, int maxDepth) {
        double delta = scheduleToLoop.visit(depth + 1, maxDepth);
        for(int totalIterations = 1; delta > maxDelta; totalIterations++) {
            delta = scheduleToLoop.visit(depth + 1, maxDepth);
            if(totalIterations > MAX_ITERATIONS)
                throw new RuntimeException(String.format(
                        "Maximum iterations (%d) reached.", MAX_ITERATIONS));
        }
        
        return delta;
    }
}