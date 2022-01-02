package org.um.feri.ears.statistic.true_skill.factorgraphs;

/**
 * A function that takes no parameters and returns a value of the type specified
 * by the TResult parameter.
 * 
 * @param <TResult>
 *            The return type of the function.
 */
public interface Func<TResult> {

    TResult eval();
}
