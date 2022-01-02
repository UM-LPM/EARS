package org.um.feri.ears.statistic.true_skill;


import static org.um.feri.ears.statistic.true_skill.GaussianDistribution.inverseCumulativeTo;

public class DrawMargin
{
    public static double getDrawMarginFromDrawProbability(double drawProbability, double beta)
    {
        // Derived from TrueSkill technical report (MSR-TR-2006-80), page 6

        // draw probability = 2 * CDF(margin/(sqrt(n1+n2)*beta)) -1

        // implies
        //
        // margin = inversecdf((draw probability + 1)/2) * sqrt(n1+n2) * beta
        // n1 and n2 are the number of players on each team
        return inverseCumulativeTo(.5*(drawProbability + 1), 0, 1)*Math.sqrt(1 + 1)*
                        beta;
    }
}