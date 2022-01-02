package org.um.feri.ears.statistic.true_skill.layers;

import org.um.feri.ears.statistic.true_skill.GaussianDistribution;
import org.um.feri.ears.statistic.true_skill.factorgraphs.*;
import org.um.feri.ears.statistic.true_skill.factors.GaussianWeightedSumFactor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// The whole purpose of this is to do a loop on the bottom
public class IteratedTeamDifferencesInnerLayer extends
    TrueSkillFactorGraphLayer<Variable<GaussianDistribution>, GaussianWeightedSumFactor, Variable<GaussianDistribution>>
{
    private final TeamDifferencesComparisonLayer _TeamDifferencesComparisonLayer;

    private final TeamPerformancesToTeamPerformanceDifferencesLayer
        _TeamPerformancesToTeamPerformanceDifferencesLayer;

    public IteratedTeamDifferencesInnerLayer(TrueSkillFactorGraph parentGraph,
                                             TeamPerformancesToTeamPerformanceDifferencesLayer
                                                 teamPerformancesToPerformanceDifferences,
                                             TeamDifferencesComparisonLayer teamDifferencesComparisonLayer)
    {
        super(parentGraph);
        _TeamPerformancesToTeamPerformanceDifferencesLayer = teamPerformancesToPerformanceDifferences;
        _TeamDifferencesComparisonLayer = teamDifferencesComparisonLayer;
    }

    @Override
    public Collection<Factor<GaussianDistribution>> getUntypedFactors() {
      return new ArrayList<Factor<GaussianDistribution>>() {
          private static final long serialVersionUID = 6370771040490033445L; {
         addAll(_TeamPerformancesToTeamPerformanceDifferencesLayer.getUntypedFactors());
         addAll(_TeamDifferencesComparisonLayer.getUntypedFactors());
      }};
    }

    @Override
    public void buildLayer()
    {
        _TeamPerformancesToTeamPerformanceDifferencesLayer.setRawInputVariablesGroups(getInputVariablesGroups());
        _TeamPerformancesToTeamPerformanceDifferencesLayer.buildLayer();

        _TeamDifferencesComparisonLayer.setRawInputVariablesGroups(
                                                                    _TeamPerformancesToTeamPerformanceDifferencesLayer.getRawOutputVariablesGroups());
        _TeamDifferencesComparisonLayer.buildLayer();
    }

    @Override
    public Schedule<GaussianDistribution> createPriorSchedule()
    {
        Schedule<GaussianDistribution> loop;

        switch (getInputVariablesGroups().size())
        {
            case 0:
            case 1:
                throw new IllegalArgumentException();
            case 2:
                loop = CreateTwoTeamInnerPriorLoopSchedule();
                break;
            default:
                loop = CreateMultipleTeamInnerPriorLoopSchedule();
                break;
        }

        // When dealing with differences, there are always (n-1) differences, so add in the 1
        int totalTeamDifferences = _TeamPerformancesToTeamPerformanceDifferencesLayer.getLocalFactors().size();

        Collection<Schedule<GaussianDistribution>> schedules = new ArrayList<>();
        schedules.add(loop);
        schedules.add(new ScheduleStep<>(
                                          "teamPerformanceToPerformanceDifferenceFactors[0] @ 1",
                                          _TeamPerformancesToTeamPerformanceDifferencesLayer.getLocalFactors().get(0), 1));
        schedules.add(new ScheduleStep<>(
                                          String.format("teamPerformanceToPerformanceDifferenceFactors[teamTeamDifferences = %d - 1] @ 2",
                                                         totalTeamDifferences),
                                          _TeamPerformancesToTeamPerformanceDifferencesLayer.getLocalFactors().get(totalTeamDifferences - 1), 2));

        return new ScheduleSequence<>(
                                       "inner schedule", schedules);
    }

    private Schedule<GaussianDistribution> CreateTwoTeamInnerPriorLoopSchedule()
    {
        Collection<Schedule<GaussianDistribution>> schedules = new ArrayList<>();
        schedules.add(new ScheduleStep<>(
                                          "send team perf to perf differences",
                                          _TeamPerformancesToTeamPerformanceDifferencesLayer.getLocalFactors().get(0),
                                          0));
        schedules.add(new ScheduleStep<>(
                                          "send to greater than or within factor",
                                          _TeamDifferencesComparisonLayer.getLocalFactors().get(0),
                                          0));
        return scheduleSequence(schedules, "loop of just two teams inner sequence");
    }

    private Schedule<GaussianDistribution> CreateMultipleTeamInnerPriorLoopSchedule()
    {
        int totalTeamDifferences = _TeamPerformancesToTeamPerformanceDifferencesLayer.getLocalFactors().size();

        List<Schedule<GaussianDistribution>> forwardScheduleList = new ArrayList<>();

        for (int i = 0; i < totalTeamDifferences - 1; i++)
        {
            Collection<Schedule<GaussianDistribution>> schedules = new ArrayList<>();
            schedules.add(new ScheduleStep<>(
                                              String.format("team perf to perf diff %d",
                                                             i),
                                              _TeamPerformancesToTeamPerformanceDifferencesLayer.getLocalFactors().get(i), 0));
            schedules.add(new ScheduleStep<>(
                                              String.format("greater than or within result factor %d",
                                                             i),
                                              _TeamDifferencesComparisonLayer.getLocalFactors().get(i),
                                              0));
            schedules.add(new ScheduleStep<>(
                                              String.format("team perf to perf diff factors [%d], 2",
                                                             i),
                                              _TeamPerformancesToTeamPerformanceDifferencesLayer.getLocalFactors().get(i), 2));
            Schedule<GaussianDistribution> currentForwardSchedulePiece =
                scheduleSequence(schedules, "current forward schedule piece %d", i);

            forwardScheduleList.add(currentForwardSchedulePiece);
        }

        ScheduleSequence<GaussianDistribution> forwardSchedule = new ScheduleSequence<>(
                                                                                         "forward schedule",
                                                                                         forwardScheduleList);

        List<Schedule<GaussianDistribution>> backwardScheduleList = new ArrayList<>();

        for (int i = 0; i < totalTeamDifferences - 1; i++)
        {
            Collection<Schedule<GaussianDistribution>> schedules = new ArrayList<>();
            schedules.add(new ScheduleStep<>(
                                              String.format("teamPerformanceToPerformanceDifferenceFactors[totalTeamDifferences - 1 - %d] @ 0",
                                                             i),
                                              _TeamPerformancesToTeamPerformanceDifferencesLayer.getLocalFactors().get(
                                                                                                                        totalTeamDifferences - 1 - i), 0));
            schedules.add(new ScheduleStep<>(
                                              String.format("greaterThanOrWithinResultFactors[totalTeamDifferences - 1 - %d] @ 0",
                                                             i),
                                              _TeamDifferencesComparisonLayer.getLocalFactors().get(totalTeamDifferences - 1 - i), 0));
            schedules.add(new ScheduleStep<>(
                                              String.format("teamPerformanceToPerformanceDifferenceFactors[totalTeamDifferences - 1 - %d] @ 1",
                                                             i),
                                              _TeamPerformancesToTeamPerformanceDifferencesLayer.getLocalFactors().get(
                                                                                                                        totalTeamDifferences - 1 - i), 1));

            ScheduleSequence<GaussianDistribution> currentBackwardSchedulePiece = new ScheduleSequence<>(
                                                                                                          "current backward schedule piece", schedules);
            backwardScheduleList.add(currentBackwardSchedulePiece);
        }

        ScheduleSequence<GaussianDistribution> backwardSchedule = new ScheduleSequence<>(
                                                                                          "backward schedule",
                                                                                          backwardScheduleList);

        Collection<Schedule<GaussianDistribution>> schedules = new ArrayList<>();
        schedules.add(forwardSchedule);
        schedules.add(backwardSchedule);
        ScheduleSequence<GaussianDistribution> forwardBackwardScheduleToLoop = new ScheduleSequence<>(
                                                                                                       "forward Backward Schedule To Loop", schedules);

        final double initialMaxDelta = 0.0001;

      return new ScheduleLoop<>(
                                 String.format("loop with max delta of %f",
                                                initialMaxDelta),
                                 forwardBackwardScheduleToLoop,
                                 initialMaxDelta);
    }
}