package exercise4;

import java.util.ArrayList;
import java.util.Stack;
/**
 * Class for creating a postfix-expression
 * @author Andreas
 *
 */
public class Expression {
	//TODO 	A more generalized version of the tree could be forked from this
	//		and also, a general Expresson-class should be created from which
	//		this inherits to facilitate multiple expression types
	
	Node root;
	String oplist[] = {"+","-","/","*"};
	ArrayList<String> op = new ArrayList<String>();
	ArrayList<String> num = new ArrayList<String>();
	Stack<String> stack;
	String expr;
	public Expression(String expr) {
		this.expr = expr;
		stack = new Stack<String>(); //Really chars, but whatever.
		
		String exprArray[] = expr.split(" ");
		for (int i = 0; i < exprArray.length; i++) {
			
			stack.push(exprArray[i]);
		}
		//TODO put this inside createTree;
		System.out.println("Stack initialized with size: "+ stack.size());
		root = new Node(stack.pop());
		System.out.println("Root is: "+root.getValue());
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
		
	
	void insert(String oldNode, String newNode ){
		
	
	}
	
	
	
	boolean isInOplist(String n){
		
		for(int j =0; j <oplist.length; j++){
			if(n.equals(oplist[j])) return true;
		}
		
		return false;
	}

	
	public static void main(String[] args) {
		
		Expression ex = new Expression("10 20 +");
		System.out.println(ex.getExpression());
		Expression ex2 = new Expression("7 2 - 5 2 * + 10 +");
		System.out.println(ex2.getExpression());
		Expression ex3 = new Expression("5 1 2 + 4 * + 3 -");
		System.out.println(ex3.getExpression());
	}
}
