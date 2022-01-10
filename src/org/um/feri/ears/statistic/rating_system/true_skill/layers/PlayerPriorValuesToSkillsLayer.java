package org.um.feri.ears.statistic.rating_system.true_skill.layers;


import org.um.feri.ears.statistic.rating_system.Player;
import org.um.feri.ears.statistic.rating_system.Rating;
import org.um.feri.ears.statistic.rating_system.true_skill.GaussianDistribution;
import org.um.feri.ears.statistic.rating_system.true_skill.ITeam;
import org.um.feri.ears.statistic.rating_system.true_skill.factorgraphs.*;
import org.um.feri.ears.statistic.rating_system.true_skill.factors.GaussianPriorFactor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static java.lang.Math.pow;

// We intentionally have no Posterior schedule since the only purpose here is to 
public class PlayerPriorValuesToSkillsLayer extends
    TrueSkillFactorGraphLayer<DefaultVariable<GaussianDistribution>,
            GaussianPriorFactor,
            KeyedVariable<Player, GaussianDistribution>>
{
    private final Collection<ITeam> _Teams;

    public PlayerPriorValuesToSkillsLayer(TrueSkillFactorGraph parentGraph,
                                          Collection<ITeam> teams)
    {
        super(parentGraph);
        _Teams = teams;
    }

    @Override
    public void buildLayer()
    {
        for(ITeam currentTeam : _Teams)
        {
            @SuppressWarnings("Convert2Diamond") List<KeyedVariable<Player, GaussianDistribution>> currentTeamSkills = new ArrayList<>();

            for(Entry<Player, Rating> currentTeamPlayer : currentTeam.entrySet())
            {
                KeyedVariable<Player, GaussianDistribution> playerSkill =
                    CreateSkillOutputVariable(currentTeamPlayer.getKey());
                addLayerFactor(CreatePriorFactor(currentTeamPlayer.getValue(), playerSkill));
                currentTeamSkills.add(playerSkill);
            }

            addOutputVariableGroup(currentTeamSkills);
        }
    }

    @Override
    public Schedule<GaussianDistribution> createPriorSchedule()
    {
        Collection<Schedule<GaussianDistribution>> schedules = getLocalFactors().stream().map(prior -> new ScheduleStep<>("Prior to Skill Step", prior, 0)).collect(Collectors.toList());
        return scheduleSequence(schedules, "All priors");
    }

    private GaussianPriorFactor CreatePriorFactor(Rating priorRating,
                                                  Variable<GaussianDistribution> skillsVariable)
    {
        return new GaussianPriorFactor(priorRating.getRating(),
                                       pow(priorRating.getRatingDeviation(), 2) +
                                       pow(getParentFactorGraph().getGameInfo().getDynamicsFactor(), 2), skillsVariable);
    }

    private KeyedVariable<Player, GaussianDistribution> CreateSkillOutputVariable(Player key)
    {
        //noinspection Convert2Diamond
        return new KeyedVariable<>(key, GaussianDistribution.UNIFORM, "%s's skill", key.toString());
    }
}