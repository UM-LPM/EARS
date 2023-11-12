package org.um.feri.ears.individual.representations.gp;

public class TreeAncestor2 {
    private int treeHeightPosition;
    private Node treeNode;

    public TreeAncestor2(int treeHeightPosition, Node treeNode){
        this.treeHeightPosition = treeHeightPosition;
        this.treeNode = treeNode;
    }

    public int getTreeHeightPosition() {
        return treeHeightPosition;
    }

    public Node getTreeNode() {
        return treeNode;
    }

    public void setTreeHeightPosition(int treeHeightPosition) {
        this.treeHeightPosition = treeHeightPosition;
    }

    public void setTreeNode(Node treeNode) {
        this.treeNode = treeNode;
    }
}
