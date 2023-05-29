package org.um.feri.ears.individual.representations.gp;

public class TreeAncestor<T> {
    private int treeHeightPosition;
    private TreeNode<T> treeNode;

    public TreeAncestor(int treeHeightPosition, TreeNode<T> treeNode){
        this.treeHeightPosition = treeHeightPosition;
        this.treeNode = treeNode;
    }

    public int getTreeHeightPosition() {
        return treeHeightPosition;
    }

    public TreeNode<T> getTreeNode() {
        return treeNode;
    }

    public void setTreeHeightPosition(int treeHeightPosition) {
        this.treeHeightPosition = treeHeightPosition;
    }

    public void setTreeNode(TreeNode<T> treeNode) {
        this.treeNode = treeNode;
    }
}
