package org.um.feri.ears.statistic.true_skill.factorgraphs;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for computing the factor graph's normalization constant.
 */
public class FactorList<TValue> {

    private final List<Factor<TValue>> factors = new ArrayList<>();

    public double getLogNormalization() { 
        // TODO can these 3 loops be rolled into 1?
        factors.forEach(Factor::ResetMarginals);

        double sumLogZ = 0.0;
        for(Factor<TValue> f : factors) {
            for (int j = 0; j < f.getNumberOfMessages(); j++)
                sumLogZ += f.SendMessage(j);
        }
        
        double sumLogS = 0;
        for(Factor<TValue> f : factors) sumLogS += f.getLogNormalization();

        return sumLogZ + sumLogS;
    }

    public int size() { return factors.size(); }

    public Factor<TValue> addFactor(Factor<TValue> factor) {
        factors.add(factor);
        return factor;
    }
}