package exercise4;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
/**
 * Class for creating a postfix-expression
 * @author Andreas
 *
 */
public class PostfixExpression {
	//TODO 	A more generalized version of the tree could be forked from this
	//		and also, a general Expresson-class should be created from which
	//		this inherits to facilitate multiple expression types
	
	Node root;
	String oplist[] = {"+","-","/","*"};
	ArrayList<String> op = new ArrayList<String>();
	ArrayList<String> num = new ArrayList<String>();
	Stack<String> stack;
	String expr;
	public PostfixExpression(String expr) {
		this.expr = expr;
		stack = new Stack<String>(); //Really chars, but whatever.
		
		String exprArray[] = expr.split(" ");
		for (int i = 0; i < exprArray.length; i++) {
			
			stack.push(exprArray[i]);
		}
		//TODO put this inside createTree;
		root = new Node(stack.pop());
		buildTree(root,stack);
		
	}
	
	/**
	 * Traverses the expression tree and creates a string
	 * 
	 * @return String version of the expression
	 */
	public String getExpression(){
		
		
		return depthFirstLTR(root);
	}
	
	private String depthFirstLTR(Node root){
		String s = (String) root.getValue();
		for (Node n : root.getChildren()) {
			s = depthFirstLTR(n)+" "+s;
		}
		
		return s;
	}

	public void buildTree(Node n, Stack<String> stack){
		if(!stack.isEmpty()){
		String candidate = stack.pop();
		if(!isInOplist(candidate)){
			n.addChild(new Node(candidate));
		}
		else{
		Node child = new Node(candidate);
		n.addChild(child);
		buildTree(child,stack);}
	
		buildTree(n, stack);
		}
	}
	
	boolean isInOplist(String n){
		
		for(int j =0; j <oplist.length; j++){
			if(n.equals(oplist[j])) return true;
		}
		
		return false;
	}
	
	public List<Node> getLeafExpr(){
		return depthFirstLTR(root, new ArrayList<Node>());
		
	}
	
	private List<Node> depthFirstLTR(Node root, List<Node> l){
		
		if(root.childrenAreLeaf() && !root.isLeaf()){
			l.add(root);
			System.out.println(root.getValue()+ " added");
		}
		
		for (Node c : root.getChildren()) {
			depthFirstLTR(c, l);
		}
		
		
		return l;
	}
	
	public String printNodeList(List<Node> list){
		String p ="";
		for(Node n : list){
			for(Node c : n.getChildren()){
				p = c.getValue()+ " "+p;
			}
			p+=n.getValue();
			
		}
		return p;
	}
	
	public static void main(String[] args) {
		
		PostfixExpression ex = new PostfixExpression("10 20 +");
//		System.out.println(ex.getExpression());
		PostfixExpression ex2 = new PostfixExpression("7 2 - 5 4 * + 10 +");
		System.out.println(ex2.getExpression());
		PostfixExpression ex3 = new PostfixExpression("5 1 2 + 4 * + 3 -");
//		System.out.println(ex3.getExpression());
		
		List<Node> list = (ArrayList<Node>) ex2.getLeafExpr();
		
		System.out.println(ex3.printNodeList(list));
		
	}
}
