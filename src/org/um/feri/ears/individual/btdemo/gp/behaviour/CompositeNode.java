package org.um.feri.ears.individual.btdemo.gp.behaviour;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.um.feri.ears.individual.btdemo.gp.Node;
import org.um.feri.ears.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

public class CompositeNode extends BehaviourTreeNode {

    public CompositeNode(BehaviourTreeNodeType nodeType, int arity){
        this(nodeType, null, arity);
    }

    public CompositeNode(BehaviourTreeNodeType nodeType, List<Property> properties, int arity){
        this(nodeType, properties , arity, null);
    }

    public CompositeNode(BehaviourTreeNodeType nodeType, List<Property> properties, int arity, List<Node> children) {
        super(nodeType, properties, arity, children, false);
    }
}
