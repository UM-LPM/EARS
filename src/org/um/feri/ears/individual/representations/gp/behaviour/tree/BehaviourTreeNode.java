package org.um.feri.ears.individual.representations.gp.behaviour.tree;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.um.feri.ears.individual.representations.gp.GraphPrinter;
import org.um.feri.ears.individual.representations.gp.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class BehaviourTreeNode extends Node {

    protected BehaviourTreeNodeType nodeType;
    protected UUID guid;
    protected List<Property> properties;

    public BehaviourTreeNode(BehaviourTreeNodeType nodeType, List<Property> properties) {
        this(nodeType, properties, 0);
    }

    public BehaviourTreeNode(BehaviourTreeNodeType name, List<Property> properties, int arity) {
        this(name, properties, arity, new ArrayList<>(), false);
    }

    public BehaviourTreeNode(BehaviourTreeNodeType nodeType, List<Property> properties, int arity, List<Node> children, boolean fixedNumberOfChildre) {
        super(arity, children, fixedNumberOfChildre, nodeType.getValue());
        this.nodeType = nodeType;
        if(properties == null)
            this.properties = new ArrayList<>();
        else
            this.properties = properties;
        this.guid = java.util.UUID.randomUUID();
    }

    public boolean hasAssignedProperties(){
        if(properties == null || properties.isEmpty()){
            return false;
        }
        return true;
    }

    @JsonProperty("Name")
    public void setNodeType(BehaviourTreeNodeType nodeType){
        this.nodeType = nodeType;
    }
    @JsonIgnore
    public BehaviourTreeNodeType getNodeType(){
        return nodeType;
    }

    @JsonProperty("Name")
    public String getNodeTypeString(){
        return nodeType.getValue();
    }

    @JsonProperty("Guid")
    public String getGuid(){
        return guid.toString();
    }
    @JsonProperty("Guid")
    public void setGuid(String guid){
        this.guid = UUID.fromString(guid);
    }
    public Property getProperty(String propertyName) {
        properties.stream().map(property -> {
            if (property.getName().equals(propertyName)) {
                return property;
            }
            return null;
        });
        return null;
    }

    @JsonProperty("Properties")
    public List<Property> getProperties() {
        return properties;
    }
    @JsonProperty("Properties")
    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    @Override
    public BehaviourTreeNode clone() {
        BehaviourTreeNode cloned = (BehaviourTreeNode) super.clone();
        cloned.guid = UUID.randomUUID();
        cloned.properties = new ArrayList<>();
        for (Property child : this.properties) {
            cloned.properties.add(new Property(child.getName(), child.getMinValue(), child.getMaxValue(), child.getValue()));
        }
        return cloned;
    }

    @Override
    public double evaluate(Map<String, Double> variables) {
        return -1;
    }

    @Override
    public void setTreeNodes(GraphPrinter gp) {
        if(this.properties.size() > 0)
            gp.addln(id + "[label=<"+ this.name +"<BR /><FONT POINT-SIZE=\"10\">" + this.propertiesToString() + "</FONT>>, "+ this.setNodeStyle() + "]");
        else
            gp.addln(id + " [label=\"" + this.name +"\", " + this.setNodeStyle() + "]");

        for (Node next : children) {
            next.setTreeNodes(gp);
        }
    }

    public String propertiesToString(){
        StringBuilder propertiesString = new StringBuilder();
        for(Property property : properties){
            propertiesString.append(property.toString()).append("<BR />");
        }
        return propertiesString.toString();
    }

}
