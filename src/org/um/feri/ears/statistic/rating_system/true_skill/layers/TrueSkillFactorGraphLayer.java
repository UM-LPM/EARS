package org.um.feri.ears.statistic.rating_system.true_skill.layers;

import org.um.feri.ears.statistic.rating_system.true_skill.GaussianDistribution;
import org.um.feri.ears.statistic.rating_system.true_skill.factorgraphs.Factor;
import org.um.feri.ears.statistic.rating_system.true_skill.factorgraphs.FactorGraphLayer;
import org.um.feri.ears.statistic.rating_system.true_skill.factorgraphs.Variable;

public abstract class TrueSkillFactorGraphLayer<TInputVariable extends Variable<GaussianDistribution>,
                                                TFactor extends Factor<GaussianDistribution>,
                                                TOutputVariable extends Variable<GaussianDistribution>>
    extends FactorGraphLayer
        <TrueSkillFactorGraph, GaussianDistribution, Variable<GaussianDistribution>, TInputVariable,
                TFactor, TOutputVariable>
{
    public TrueSkillFactorGraphLayer(TrueSkillFactorGraph parentGraph)
    {
        super(parentGraph);
    }
}