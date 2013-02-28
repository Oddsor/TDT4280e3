package exercise4;

public class Node {

	Node parent;
	Node[] children = new Node[2];
	String value;

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public Node[] getChildren() {
		return children;
	}
	
	public void addChildLeft(Node n){
		children[0] = n;
	}

	public void addChildRight(Node n){
		children[1] = n;
	}
	
	public void setChildren(Node[] children) {
		this.children = children;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Node(String value) {
		this.value = value;
	}

}
