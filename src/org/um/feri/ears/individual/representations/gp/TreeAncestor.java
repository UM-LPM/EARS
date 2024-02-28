package org.um.feri.ears.individual.representations.gp;

public class TreeAncestor {
    private int treeDepthPosition;
    private Node treeNode;

    public TreeAncestor(int treeDepthPosition, Node treeNode){
        this.treeDepthPosition = treeDepthPosition;
        this.treeNode = treeNode;
    }

    public int getTreeDepthPosition() {
        return treeDepthPosition;
    }

    public Node getTreeNode() {
        return treeNode;
    }

    public void setTreeDepthPosition(int treeDepthPosition) {
        this.treeDepthPosition = treeDepthPosition;
    }

    public void setTreeNode(Node treeNode) {
        this.treeNode = treeNode;
    }
}
