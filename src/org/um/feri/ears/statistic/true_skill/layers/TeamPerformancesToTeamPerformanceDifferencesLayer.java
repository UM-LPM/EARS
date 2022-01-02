package org.um.feri.ears.statistic.true_skill.layers;


import org.um.feri.ears.statistic.true_skill.GaussianDistribution;
import org.um.feri.ears.statistic.true_skill.factorgraphs.Variable;
import org.um.feri.ears.statistic.true_skill.factors.GaussianWeightedSumFactor;

import java.util.ArrayList;
import java.util.List;

public class TeamPerformancesToTeamPerformanceDifferencesLayer extends
    TrueSkillFactorGraphLayer<Variable<GaussianDistribution>,
            GaussianWeightedSumFactor,
                              Variable<GaussianDistribution>>
{
    public TeamPerformancesToTeamPerformanceDifferencesLayer(TrueSkillFactorGraph parentGraph)
    {
        super(parentGraph);
    }

    @Override
    public void buildLayer()
    {
        for (int i = 0; i < getInputVariablesGroups().size() - 1; i++)
        {
            Variable<GaussianDistribution> strongerTeam = getInputVariablesGroups().get(i).get(0);
            Variable<GaussianDistribution> weakerTeam = getInputVariablesGroups().get(i + 1).get(0);

            Variable<GaussianDistribution> currentDifference = createOutputVariable();
            addLayerFactor(CreateTeamPerformanceToDifferenceFactor(strongerTeam, weakerTeam, currentDifference));

            // REVIEW: Does it make sense to have groups of one?
            addOutputVariable(currentDifference);
        }
    }

    private GaussianWeightedSumFactor CreateTeamPerformanceToDifferenceFactor(
        final Variable<GaussianDistribution> strongerTeam, final Variable<GaussianDistribution> weakerTeam,
        Variable<GaussianDistribution> output)
    {
        List<Variable<GaussianDistribution>> teams = new ArrayList<Variable<GaussianDistribution>>(){
            private static final long serialVersionUID = -50768988200855179L; {
        add(strongerTeam);
        add(weakerTeam);
        }};
        return new GaussianWeightedSumFactor(output, teams, new double[] {1.0, -1.0});
    }

    private Variable<GaussianDistribution> createOutputVariable()
    {
        return new Variable<>(GaussianDistribution.UNIFORM, "Team performance difference");
    }
}