package org.um.feri.ears.individual.btdemo.gp.behaviour;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.um.feri.ears.individual.btdemo.gp.Node;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonProperty;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

public class DecoratorNode extends BehaviourTreeNode {

    public DecoratorNode(BehaviourTreeNodeType nodeType, int arity){
        this(nodeType, null, arity);
    }

    public DecoratorNode(BehaviourTreeNodeType nodeType, List<Property> properties, int arity){
        this(nodeType, properties , arity, null);
    }

    public DecoratorNode(BehaviourTreeNodeType nodeType, List<Property> properties, int arity, List<Node> children) {
        super(nodeType, properties, arity, children, true);
    }


}
