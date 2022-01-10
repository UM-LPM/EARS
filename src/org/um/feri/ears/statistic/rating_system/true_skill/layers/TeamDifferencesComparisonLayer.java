package org.um.feri.ears.statistic.rating_system.true_skill.layers;

import org.um.feri.ears.statistic.rating_system.true_skill.DrawMargin;
import org.um.feri.ears.statistic.rating_system.GameInfo;
import org.um.feri.ears.statistic.rating_system.true_skill.GaussianDistribution;
import org.um.feri.ears.statistic.rating_system.true_skill.factorgraphs.DefaultVariable;
import org.um.feri.ears.statistic.rating_system.true_skill.factorgraphs.Variable;
import org.um.feri.ears.statistic.rating_system.true_skill.factors.GaussianFactor;
import org.um.feri.ears.statistic.rating_system.true_skill.factors.GaussianGreaterThanFactor;
import org.um.feri.ears.statistic.rating_system.true_skill.factors.GaussianWithinFactor;

public class TeamDifferencesComparisonLayer extends
    TrueSkillFactorGraphLayer<Variable<GaussianDistribution>, GaussianFactor, DefaultVariable<GaussianDistribution>>
{
    private final double epsilon;
    private final int[] teamRanks;

    public TeamDifferencesComparisonLayer(TrueSkillFactorGraph parentGraph, int[] teamRanks)
    {
        super(parentGraph);
        this.teamRanks = teamRanks;
        GameInfo gameInfo = parentFactorGraph.getGameInfo();
        epsilon = DrawMargin.getDrawMarginFromDrawProbability(gameInfo.getDrawProbability(), gameInfo.getBeta());
    }

    @Override
    public void buildLayer()
    {
        for (int i = 0; i < getInputVariablesGroups().size(); i++)
        {
            boolean isDraw = (teamRanks[i] == teamRanks[i + 1]);
            Variable<GaussianDistribution> teamDifference = getInputVariablesGroups().get(i).get(0);

            GaussianFactor factor =
                isDraw
                    ? (GaussianFactor) new GaussianWithinFactor(epsilon, teamDifference)
                    : new GaussianGreaterThanFactor(epsilon, teamDifference);

            addLayerFactor(factor);
        }
    }
}