package exercise4;

import java.util.ArrayList;
import java.util.List;
/**
 * Implements a Node-class for building trees.
 * @author Andreas
 *
 */
public class Node {

	Node parent;
	List<Node> children = new ArrayList<Node>();
	Object value;

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public List<Node> getChildren() {
		return children;
	}

	public void addChild(Node n){
		children.add(n);
	}

	public void setChildren(List<Node> children) {
		this.children = children;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Node(Object value) {
		this.value = value;
	}
	
	boolean isLeaf(){
		return children.size()==0 ? true : false;
	}

}
