package org.um.feri.ears.statistic.rating_system.true_skill.layers;

import org.um.feri.ears.statistic.rating_system.Player;
import org.um.feri.ears.statistic.rating_system.true_skill.GaussianDistribution;
import org.um.feri.ears.statistic.rating_system.true_skill.factorgraphs.KeyedVariable;
import org.um.feri.ears.statistic.rating_system.true_skill.factorgraphs.Schedule;
import org.um.feri.ears.statistic.rating_system.true_skill.factorgraphs.ScheduleStep;
import org.um.feri.ears.statistic.rating_system.true_skill.factors.GaussianLikelihoodFactor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.pow;

public class PlayerSkillsToPerformancesLayer extends
    TrueSkillFactorGraphLayer<KeyedVariable<Player, GaussianDistribution>,
            GaussianLikelihoodFactor,
                              KeyedVariable<Player, GaussianDistribution>>
{
    public PlayerSkillsToPerformancesLayer(TrueSkillFactorGraph parentGraph)
    {
        super(parentGraph);
    }

    @Override
    public void buildLayer()
    {
        for(List<KeyedVariable<Player, GaussianDistribution>> currentTeam : getInputVariablesGroups())
        {
            List<KeyedVariable<Player, GaussianDistribution>> currentTeamPlayerPerformances = new ArrayList<>();

            for(KeyedVariable<Player, GaussianDistribution> playerSkillVariable : currentTeam)
            {
                KeyedVariable<Player, GaussianDistribution> playerPerformance =
                    CreateOutputVariable(playerSkillVariable.getKey());
                addLayerFactor(CreateLikelihood(playerSkillVariable, playerPerformance));
                currentTeamPlayerPerformances.add(playerPerformance);
            }

            addOutputVariableGroup(currentTeamPlayerPerformances);
        }
    }

    private GaussianLikelihoodFactor CreateLikelihood(KeyedVariable<Player, GaussianDistribution> playerSkill,
                                                      KeyedVariable<Player, GaussianDistribution> playerPerformance)
    {
        return new GaussianLikelihoodFactor(pow(parentFactorGraph.getGameInfo().getBeta(), 2), playerPerformance, playerSkill);
    }

    private KeyedVariable<Player, GaussianDistribution> CreateOutputVariable(Player key)
    {
        return new KeyedVariable<>(key, GaussianDistribution.UNIFORM, "%s's performance", key);
    }

    @Override
    public Schedule<GaussianDistribution> createPriorSchedule()
    {
        Collection<Schedule<GaussianDistribution>> schedules = getLocalFactors().stream().map(likelihood -> new ScheduleStep<>("Skill to Perf step", likelihood, 0)).collect(Collectors.toList());
        return scheduleSequence(schedules,
                                 "All skill to performance sending");
    }

    @Override
    public Schedule<GaussianDistribution> createPosteriorSchedule()
    {
        Collection<Schedule<GaussianDistribution>> schedules = getLocalFactors().stream().map(likelihood -> new ScheduleStep<>("Skill to Perf step", likelihood, 1)).collect(Collectors.toList());
        return scheduleSequence(schedules,
                                 "All skill to performance sending");
    }
}