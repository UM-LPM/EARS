package org.um.feri.ears.individual.btdemo.gp.behaviour;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.um.feri.ears.individual.btdemo.gp.Node;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonProperty;

import static java.util.Objects.requireNonNull;

public class RootNode extends BehaviourTreeNode {

    public RootNode(){
        this(BehaviourTreeNodeType.ROOT);
    }

    public RootNode(BehaviourTreeNodeType nodeType){
        this(nodeType, null);
    }
    public RootNode(BehaviourTreeNodeType nodeType, Node child){
        super(nodeType, null, 1);
    }
}
