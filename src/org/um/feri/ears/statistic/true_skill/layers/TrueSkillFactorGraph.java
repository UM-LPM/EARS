package org.um.feri.ears.statistic.true_skill.layers;


import org.um.feri.ears.statistic.true_skill.*;
import org.um.feri.ears.statistic.true_skill.factorgraphs.*;

import java.util.*;

public class TrueSkillFactorGraph extends FactorGraph<TrueSkillFactorGraph>
{
    private final List<FactorGraphLayerBase<GaussianDistribution>> _Layers = new ArrayList<>();
    private final PlayerPriorValuesToSkillsLayer _PriorLayer;

    public TrueSkillFactorGraph(GameInfo gameInfo, Collection<ITeam> teams, int[] teamRanks)
    {
        _PriorLayer = new PlayerPriorValuesToSkillsLayer(this, teams);
        setGameInfo(gameInfo);

        _Layers.add(_PriorLayer);
        _Layers.add(new PlayerSkillsToPerformancesLayer(this));
        _Layers.add(new PlayerPerformancesToTeamPerformancesLayer(this));
        _Layers.add(new IteratedTeamDifferencesInnerLayer(
                              this,
                              new TeamPerformancesToTeamPerformanceDifferencesLayer(this),
                              new TeamDifferencesComparisonLayer(this, teamRanks)));
    }

    private GameInfo gameInfo;
    public GameInfo getGameInfo() { return gameInfo; }
    private void setGameInfo(GameInfo info) { gameInfo = info; } 

    public void BuildGraph()
    {
        Object lastOutput = null;

        for(FactorGraphLayerBase<GaussianDistribution> currentLayer :_Layers)
        {
            if (lastOutput != null)
            {
                currentLayer.setRawInputVariablesGroups(lastOutput);
            }

            currentLayer.buildLayer();

            lastOutput = currentLayer.getRawOutputVariablesGroups();
        }
    }

    public void RunSchedule()
    {
        Schedule<GaussianDistribution> fullSchedule = CreateFullSchedule();
        @SuppressWarnings("unused") // TODO Maybe something can be done w/ this?
        double fullScheduleDelta = fullSchedule.visit();
    }

    public double GetProbabilityOfRanking()
    {
        FactorList<GaussianDistribution> factorList = new FactorList<>();

        for(FactorGraphLayerBase<GaussianDistribution> currentLayer :_Layers)
        {
            currentLayer.getUntypedFactors().forEach(factorList::addFactor);
        }

        double logZ = factorList.getLogNormalization();
        return Math.exp(logZ);
    }

    private Schedule<GaussianDistribution> CreateFullSchedule()
    {
        List<Schedule<GaussianDistribution>> fullSchedule = new ArrayList<>();

        for(FactorGraphLayerBase<GaussianDistribution> currentLayer :_Layers)
        {
            Schedule<GaussianDistribution> currentPriorSchedule = currentLayer.createPriorSchedule();
            if (currentPriorSchedule != null)
            {
                fullSchedule.add(currentPriorSchedule);
            }
        }

        // Getting as a list to use reverse()
        List<FactorGraphLayerBase<GaussianDistribution>> allLayers = new ArrayList<>(_Layers);
        Collections.reverse(allLayers);

        for(FactorGraphLayerBase<GaussianDistribution> currentLayer : allLayers)
        {
            Schedule<GaussianDistribution> currentPosteriorSchedule = currentLayer.createPosteriorSchedule();
            if (currentPosteriorSchedule != null)
            {
                fullSchedule.add(currentPosteriorSchedule);
            }
        }

        return new ScheduleSequence<>("Full schedule", fullSchedule);
    }

    public Map<IPlayer, Rating> GetUpdatedRatings()
    {
        Map<IPlayer, Rating> result = new HashMap<>();
        for(List<KeyedVariable<IPlayer, GaussianDistribution>> currentTeam : _PriorLayer.getOutputVariablesGroups())
        {
            for(KeyedVariable<IPlayer, GaussianDistribution> currentPlayer : currentTeam)
            {
                result.put(currentPlayer.getKey(), new Rating(currentPlayer.getValue().getMean(),
                                                       currentPlayer.getValue().getStandardDeviation()));
            }
        }

        return result;
    }
}