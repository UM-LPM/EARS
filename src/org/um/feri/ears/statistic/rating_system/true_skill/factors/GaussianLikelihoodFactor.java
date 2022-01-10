package org.um.feri.ears.statistic.rating_system.true_skill.factors;


import org.um.feri.ears.statistic.rating_system.true_skill.GaussianDistribution;
import org.um.feri.ears.statistic.rating_system.true_skill.factorgraphs.Message;
import org.um.feri.ears.statistic.rating_system.true_skill.factorgraphs.Variable;

import static org.um.feri.ears.statistic.rating_system.true_skill.GaussianDistribution.*;

/**
 * Connects two variables and adds uncertainty.
 * <remarks>See the accompanying math paper for more details.</remarks>
 */
public class GaussianLikelihoodFactor extends GaussianFactor
{
    private final double _Precision;

    public GaussianLikelihoodFactor(double betaSquared, Variable<GaussianDistribution> variable1,
                                    Variable<GaussianDistribution> variable2)
    {
        super(String.format("Likelihood of %s going to %s", variable2, variable1));
        _Precision = 1.0/betaSquared;
        CreateVariableToMessageBinding(variable1);
        CreateVariableToMessageBinding(variable2);
    }

    @Override
    public double getLogNormalization()
    {
        return logRatioNormalization(variables.get(0).getValue(), messages.get(0).getValue());
    }

    private double UpdateHelper(Message<GaussianDistribution> message1, Message<GaussianDistribution> message2,
                                Variable<GaussianDistribution> variable1, Variable<GaussianDistribution> variable2)
    {
        GaussianDistribution message1Value = new GaussianDistribution(message1.getValue());
        GaussianDistribution message2Value = new GaussianDistribution(message2.getValue());

        GaussianDistribution marginal1 = new GaussianDistribution(variable1.getValue());
        GaussianDistribution marginal2 = new GaussianDistribution(variable2.getValue());

        double a = _Precision/(_Precision + marginal2.getPrecision() - message2Value.getPrecision());

        GaussianDistribution newMessage = GaussianDistribution.fromPrecisionMean(
            a*(marginal2.getPrecisionMean() - message2Value.getPrecisionMean()),
            a*(marginal2.getPrecision() - message2Value.getPrecision()));

        GaussianDistribution oldMarginalWithoutMessage = divide(marginal1,message1Value);

        GaussianDistribution newMarginal = mult(oldMarginalWithoutMessage,newMessage);

        // Update the message and marginal

        message1.setValue(newMessage);
        variable1.setValue(newMarginal);

        // Return the difference in the new marginal
        return sub(newMarginal, marginal1);
    }

    @Override
    public double updateMessage(int messageIndex)
    {
        switch (messageIndex)
        {
            case 0:
                return UpdateHelper(getMessages().get(0), getMessages().get(1),
                                    getVariables().get(0), getVariables().get(1));
            case 1:
                return UpdateHelper(getMessages().get(1), getMessages().get(0),
                                    getVariables().get(1), getVariables().get(0));
            default:
                throw new IllegalArgumentException();
        }
    }
}