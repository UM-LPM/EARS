package org.um.feri.ears.statistic.true_skill.factors;


import org.um.feri.ears.statistic.true_skill.GaussianDistribution;
import org.um.feri.ears.statistic.true_skill.factorgraphs.Factor;
import org.um.feri.ears.statistic.true_skill.factorgraphs.Message;
import org.um.feri.ears.statistic.true_skill.factorgraphs.Variable;

import static org.um.feri.ears.statistic.true_skill.GaussianDistribution.logProductNormalization;


public abstract class GaussianFactor extends Factor<GaussianDistribution> {

    GaussianFactor(String name) { super(name); }

    /** Sends the factor-graph message with and returns the log-normalization constant **/
    @Override
    protected double SendMessage(Message<GaussianDistribution> message,
                                 Variable<GaussianDistribution> variable) {
        GaussianDistribution marginal = variable.getValue();
        GaussianDistribution messageValue = message.getValue();
        double logZ = logProductNormalization(marginal, messageValue);
        variable.setValue(marginal.mult(messageValue));
        return logZ;
    }

    @Override
    public Message<GaussianDistribution> CreateVariableToMessageBinding(
            Variable<GaussianDistribution> variable) {
        return CreateVariableToMessageBinding(variable,
                new Message<>(GaussianDistribution
                        .fromPrecisionMean(0, 0), "message from %s to %s",
                        this, variable));
    }
}