package org.um.feri.ears.individual.btdemo.gp.behaviour;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Tree {
    private String name;
    private RootNode rootNode;

    public Tree(String name) {
        this(name,null);
    }

    public Tree(String name, RootNode rootNode) {
        this.rootNode = rootNode;
        this.name = name;
    }

    @JsonProperty("RootNode")
    public void setRootNode(RootNode rootNode) {
        this.rootNode = rootNode;
    }
    @JsonProperty("RootNode")
    public RootNode getRootNode() {
        return rootNode;
    }
    @JsonProperty("Name")
    public void setName(String name) {
        this.name = name;
    }
    @JsonProperty("Name")
    public String getName() {
        return name;
    }

    public String toJsonString() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
