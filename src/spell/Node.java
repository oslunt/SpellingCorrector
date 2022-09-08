package spell;

public class Node implements INode{

    private int count;
    private INode[] children;

    public Node() {
        this.count = 0;
        this.children = new INode[26];
    }

    @Override
    public int getValue() {
        return count;
    }

    @Override
    public void incrementValue() {
        count++;
    }

    @Override
    public INode[] getChildren() {
        return children;
    }
}
