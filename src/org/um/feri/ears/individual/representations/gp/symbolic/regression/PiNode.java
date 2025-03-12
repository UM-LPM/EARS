package org.um.feri.ears.individual.representations.gp.symbolic.regression;

import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.util.random.RNG;

import java.util.Map;

public class PiNode extends ConstNode {
    public PiNode() {
        super(Math.PI);
        this.name = "pi";
    }
}
