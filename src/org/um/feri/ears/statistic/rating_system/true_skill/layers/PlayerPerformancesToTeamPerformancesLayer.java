package org.um.feri.ears.statistic.rating_system.true_skill.layers;

import org.um.feri.ears.statistic.rating_system.Player;
import org.um.feri.ears.statistic.rating_system.true_skill.GaussianDistribution;
import org.um.feri.ears.statistic.rating_system.true_skill.PartialPlay;
import org.um.feri.ears.statistic.rating_system.true_skill.factorgraphs.KeyedVariable;
import org.um.feri.ears.statistic.rating_system.true_skill.factorgraphs.Schedule;
import org.um.feri.ears.statistic.rating_system.true_skill.factorgraphs.ScheduleStep;
import org.um.feri.ears.statistic.rating_system.true_skill.factorgraphs.Variable;
import org.um.feri.ears.statistic.rating_system.true_skill.factors.GaussianWeightedSumFactor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerPerformancesToTeamPerformancesLayer extends
    TrueSkillFactorGraphLayer<KeyedVariable<Player, GaussianDistribution>,
            GaussianWeightedSumFactor,
            Variable<GaussianDistribution>>
{
    public PlayerPerformancesToTeamPerformancesLayer(TrueSkillFactorGraph parentGraph)
    {
        super(parentGraph);
    }

    @Override
    public void buildLayer()
    {
        for(List<KeyedVariable<Player, GaussianDistribution>> currentTeam : getInputVariablesGroups())
        {
            Variable<GaussianDistribution> teamPerformance = createOutputVariable(currentTeam);
            addLayerFactor(createPlayerToTeamSumFactor(currentTeam, teamPerformance));

            // REVIEW: Does it make sense to have groups of one?
            addOutputVariable(teamPerformance);
        }
    }

    @Override
    public Schedule<GaussianDistribution> createPriorSchedule()
    {
        Collection<Schedule<GaussianDistribution>> schedules = getLocalFactors().stream().map(weightedSumFactor -> new ScheduleStep<>("Perf to Team Perf Step", weightedSumFactor, 0)).collect(Collectors.toList());
        return scheduleSequence(schedules, "all player perf to team perf schedule");
    }

    protected GaussianWeightedSumFactor createPlayerToTeamSumFactor(
        List<KeyedVariable<Player, GaussianDistribution>> teamMembers, Variable<GaussianDistribution> sumVariable)
    {
        double[] weights = new double[teamMembers.size()];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = PartialPlay.getPartialPlayPercentage(teamMembers.get(i).getKey());
        }
        return new GaussianWeightedSumFactor(sumVariable, teamMembers, weights);
    }

    @Override
    public Schedule<GaussianDistribution> createPosteriorSchedule()
    {
        List<Schedule<GaussianDistribution>> schedules = new ArrayList<>();
        for (GaussianWeightedSumFactor currentFactor : getLocalFactors()) {
            // TODO is there an off by 1 error here?
            for (int i = 0; i < currentFactor.getNumberOfMessages(); i++) {
                schedules.add(new ScheduleStep<>(
                                                  "team sum perf @" + i,
                                                  currentFactor,
                                                  i));
            }
        }
        return scheduleSequence(schedules,
                                 "all of the team's sum iterations");
    }

    private Variable<GaussianDistribution> createOutputVariable(
                                                                 List<KeyedVariable<Player, GaussianDistribution>> team)
    {
        StringBuilder sb = new StringBuilder();
        for (KeyedVariable<Player, GaussianDistribution> teamMember : team) {
            sb.append(teamMember.getKey());
            sb.append(", ");
        }
        sb.delete(sb.length()-2, sb.length());

        return new Variable<>(GaussianDistribution.UNIFORM, "Team[%s]'s performance", sb.toString());
    }
}