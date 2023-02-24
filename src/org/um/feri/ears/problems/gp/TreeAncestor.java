package org.um.feri.ears.problems.gp;

public class TreeAncestor<T> {
    private int treeHeightPosition;
    private TreeNode<T> treeNode;

    public TreeAncestor(int treeDepthPosition, TreeNode<T> treeNode){
        this.treeHeightPosition = treeDepthPosition;
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
