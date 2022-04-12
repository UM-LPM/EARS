package org.um.feri.ears.statistic.rating_system.true_skill;

import static org.um.feri.ears.statistic.rating_system.true_skill.GaussianDistribution.*;


/**
 * These functions from the bottom of page 4 of the TrueSkill paper.
 */
public class TruncatedGaussianCorrectionFunctions
{
    private TruncatedGaussianCorrectionFunctions() { }


    /**
     * The "V" function where the team performance difference is greater than the draw margin.
     * <remarks>In the reference F# implementation, this is referred to as "the additive 
     * correction of a single-sided truncated Gaussian with unit variance."</remarks>
     * @param teamPerformanceDifference 
     * @param drawMargin In the paper, it's referred to as just "ε".
     * @returns 
     */
    public static double VExceedsMargin(double teamPerformanceDifference, double drawMargin, double c)
    {
        return VExceedsMargin(teamPerformanceDifference/c, drawMargin/c);
    }

    public static double VExceedsMargin(double teamPerformanceDifference, double drawMargin)
    {
        double denominator = cumulativeTo(teamPerformanceDifference - drawMargin);

        if (denominator < 2.222758749e-162)
        {
            return -teamPerformanceDifference + drawMargin;
        }

        return at(teamPerformanceDifference - drawMargin)/denominator;
    }

    /**
     * The "W" function where the team performance difference is greater than the draw margin.
     * <remarks>In the reference F# implementation, this is referred to as "the multiplicative 
     * correction of a single-sided truncated Gaussian with unit variance."</remarks>
     * @param teamPerformanceDifference 
     * @param drawMargin 
     * @param c 
     * @returns 
     */
    public static double WExceedsMargin(double teamPerformanceDifference, double drawMargin, double c)
    {
        return WExceedsMargin(teamPerformanceDifference/c, drawMargin/c);
        //var vWin = VExceedsMargin(teamPerformanceDifference, drawMargin, c);
        //return vWin * (vWin + (teamPerformanceDifference - drawMargin) / c);
    }

    public static double WExceedsMargin(double teamPerformanceDifference, double drawMargin)
    {
        double denominator = cumulativeTo(teamPerformanceDifference - drawMargin);

        if (denominator < 2.222758749e-162)
        {
            if (teamPerformanceDifference < 0.0)
            {
                return 1.0;
            }
            return 0.0;
        }

        double vWin = VExceedsMargin(teamPerformanceDifference, drawMargin);
        return vWin*(vWin + teamPerformanceDifference - drawMargin);
    }

    // the additive correction of a double-sided truncated Gaussian with unit variance
    public static double VWithinMargin(double teamPerformanceDifference, double drawMargin, double c)
    {
        return VWithinMargin(teamPerformanceDifference/c, drawMargin/c);
    }

    // from F#:
    public static double VWithinMargin(double teamPerformanceDifference, double drawMargin)
    {
        double teamPerformanceDifferenceAbsoluteValue = Math.abs(teamPerformanceDifference);
        double denominator =
            cumulativeTo(drawMargin - teamPerformanceDifferenceAbsoluteValue) -
            cumulativeTo(-drawMargin - teamPerformanceDifferenceAbsoluteValue);
        if (denominator < 2.222758749e-162)
        {
            if (teamPerformanceDifference < 0.0)
            {
                return -teamPerformanceDifference - drawMargin;
            }

            return -teamPerformanceDifference + drawMargin;
        }

        double numerator = at(-drawMargin - teamPerformanceDifferenceAbsoluteValue) -
                           at(drawMargin - teamPerformanceDifferenceAbsoluteValue);

        if (teamPerformanceDifference < 0.0)
        {
            return -numerator/denominator;
        }

        return numerator/denominator;
    }

    // the multiplicative correction of a double-sided truncated Gaussian with unit variance
    public static double WWithinMargin(double teamPerformanceDifference, double drawMargin, double c)
    {
        return WWithinMargin(teamPerformanceDifference/c, drawMargin/c);
    }

    // From F#:
    public static double WWithinMargin(double teamPerformanceDifference, double drawMargin)
    {
        double teamPerformanceDifferenceAbsoluteValue = Math.abs(teamPerformanceDifference);
        double denominator = cumulativeTo(drawMargin - teamPerformanceDifferenceAbsoluteValue)
                             -
                             cumulativeTo(-drawMargin - teamPerformanceDifferenceAbsoluteValue);

        if (denominator < 2.222758749e-162)
        {
            return 1.0;
        }

        double vt = VWithinMargin(teamPerformanceDifferenceAbsoluteValue, drawMargin);

        return vt*vt +
               (
                   (drawMargin - teamPerformanceDifferenceAbsoluteValue)
                   *
                   at(
                       drawMargin - teamPerformanceDifferenceAbsoluteValue)
                   - (-drawMargin - teamPerformanceDifferenceAbsoluteValue)
                     *
                     at(-drawMargin - teamPerformanceDifferenceAbsoluteValue))/denominator;
    }
}