package org.um.feri.ears.statistic.rating_system.true_skill.factors;


import org.um.feri.ears.statistic.rating_system.true_skill.GaussianDistribution;
import org.um.feri.ears.statistic.rating_system.true_skill.factorgraphs.Message;
import org.um.feri.ears.statistic.rating_system.true_skill.factorgraphs.Variable;

import static org.um.feri.ears.statistic.rating_system.true_skill.GaussianDistribution.*;
import static org.um.feri.ears.statistic.rating_system.true_skill.TruncatedGaussianCorrectionFunctions.VExceedsMargin;
import static org.um.feri.ears.statistic.rating_system.true_skill.TruncatedGaussianCorrectionFunctions.WExceedsMargin;


/**
 * Factor representing a team difference that has exceeded the draw margin.
 * <remarks>See the accompanying math paper for more details.</remarks>
 */
public class GaussianGreaterThanFactor extends GaussianFactor
{
    private final double _Epsilon;

    public GaussianGreaterThanFactor(double epsilon, Variable<GaussianDistribution> variable)
    {
        super(String.format("%s > %4.3f", variable, epsilon));
        _Epsilon = epsilon;
        CreateVariableToMessageBinding(variable);
    }

    @Override 
    public double getLogNormalization()
    {
        GaussianDistribution marginal = getVariables().get(0).getValue();
        GaussianDistribution message = getMessages().get(0).getValue();
        GaussianDistribution messageFromVariable = divide(marginal,message);
        return -logProductNormalization(messageFromVariable, message)
               +
               Math.log(
                   cumulativeTo((messageFromVariable.getMean() - _Epsilon)/
                                                     messageFromVariable.getStandardDeviation()));
    }

    @Override
    protected double updateMessage(Message<GaussianDistribution> message,
                                   Variable<GaussianDistribution> variable)
    {
        GaussianDistribution oldMarginal = new GaussianDistribution(variable.getValue());
        GaussianDistribution oldMessage = new GaussianDistribution(message.getValue());
        GaussianDistribution messageFromVar = divide(oldMarginal,oldMessage);

        double c = messageFromVar.getPrecision();
        double d = messageFromVar.getPrecisionMean();

        double sqrtC = Math.sqrt(c);

        double dOnSqrtC = d/sqrtC;

        double epsilsonTimesSqrtC = _Epsilon*sqrtC;
        d = messageFromVar.getPrecisionMean();

        double denom = 1.0 - WExceedsMargin(dOnSqrtC, epsilsonTimesSqrtC);

        double newPrecision = c/denom;
        double newPrecisionMean = (d +
                                   sqrtC*
                                   VExceedsMargin(dOnSqrtC, epsilsonTimesSqrtC))/
                                  denom;

        GaussianDistribution newMarginal = fromPrecisionMean(newPrecisionMean, newPrecision);

        GaussianDistribution newMessage = divide(mult(oldMessage,newMarginal),oldMarginal);

        // Update the message and marginal
        message.setValue(newMessage);
        variable.setValue(newMarginal);

        // Return the difference in the new marginal
        return sub(newMarginal, oldMarginal);
    }
}