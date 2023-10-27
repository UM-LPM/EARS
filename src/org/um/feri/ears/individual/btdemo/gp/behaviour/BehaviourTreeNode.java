package org.um.feri.ears.individual.btdemo.gp.behaviour;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.um.feri.ears.individual.btdemo.gp.Node;
import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class BehaviourTreeNode extends Node {

    protected BehaviourTreeNodeType nodeType;
    protected UUID guid;
    protected List<Property> properties;

    public BehaviourTreeNode(BehaviourTreeNodeType name, List<Property> properties) {
        this.nodeType = name;
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

}
