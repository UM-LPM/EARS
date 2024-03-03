package org.um.feri.ears.individual.representations.gp.behaviour.tree;

import com.google.gson.Gson;
import org.um.feri.ears.individual.representations.gp.HttpResponse;
import org.um.feri.ears.individual.representations.gp.Node;
import org.um.feri.ears.individual.representations.gp.Tree;
import org.um.feri.ears.util.Util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class BehaviourTree extends Tree {
    public BehaviourTree(String name) {
        super(name,null);
    }

    public BehaviourTree(String name, Node rootNode) {
        super(name,rootNode);
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        return -1;
    }

    @Override
    public BehaviourTree clone() {
        BehaviourTree cloned = new BehaviourTree(this.name);
        cloned.rootNode = this.rootNode.clone();
        return cloned;
    }

    @Override
    public String toJsonString(){
        if(rootNode instanceof RootNode)
            return super.toJsonString();
        else
            return insertAdditionalNodes().toJsonString();
    }


    public BehaviourTree insertAdditionalNodes(){
        BehaviourTree newTree = this.clone();
        Node oldRoot = newTree.rootNode;
        RootNode rootNodeNew = new RootNode();
        Repeat repeatNode = new Repeat(BehaviourTreeNodeType.REPEAT, List.of(
                new Property("restartOnSuccess",0, 2, 1),
                new Property("restartOnFailure",0, 2, 1)
        ));
        rootNodeNew.insert(0, repeatNode);
        repeatNode.insert(0, oldRoot);
        newTree.rootNode = rootNodeNew;
        return newTree;
    }
}
