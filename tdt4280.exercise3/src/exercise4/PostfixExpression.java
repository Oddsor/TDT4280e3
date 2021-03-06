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
		if(n.getChildren().size() < 2 ){
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
}
	boolean isInOplist(String n){

		for(int j =0; j <oplist.length; j++){
			if(n.equals(oplist[j])) return true;
		}

		return false;
	}

	public List<String> getLeafExpr(){
		 List<Node> list = depthFirstLTR(root, new ArrayList<Node>());
		 return getNodeArray(list);
	}

	private List<Node> depthFirstLTR(Node root, List<Node> l){

		for (Node c : root.getChildren()) {
		}

		if(root.childrenAreLeaf() && !root.isLeaf()){
			l.add(root);
		}

		for (Node c : root.getChildren()) {
			depthFirstLTR(c, l);
		}


		return l;
	}

	public List<String> getNodeArray(List<Node> list){
		String p = "";
		List<String> stringList = new ArrayList<String>();
		for(Node n : list){
			
			p = (String)n.getValue();
			for(Node c : n.getChildren()){
				p =  c.getValue()+ " "+p;
			}
			
			stringList.add(p);

		}
		return stringList;
	}
	
	public String printNodeList(List<Node> list){
		String p = "";
		for(Node n : list){
			
			p = n.getValue()+" "+ p;
			for(Node c : n.getChildren()){
				p =  c.getValue()+ " "+p;
			}
			

		}
		return p;
	}
	
	public void insertPartial(String value, String oldNode){
		insertion(value, oldNode.split(" "), root);
	}
	
	private void insertion(String value, String[] oldNode, Node root){

		
		if(oldNode.length > 2){
			if(oldNode[2].equals(root.getValue())){
				List<Node> children = root.getChildren();
				
				if(oldNode[0].equals(children.get(1).getValue()) && 
						oldNode[1].equals(children.get(0).getValue()) ){
					root.setValue(value);
					root.setChildren(new ArrayList<Node>());
				}		
			}	
		}
		
			for(Node n : root.getChildren()){
				insertion(value, oldNode, n);
		
		}
		
	}

	public static void main(String[] args) {

		PostfixExpression ex = new PostfixExpression("10 20 + 4 4 * +");
		System.out.println(ex.getExpression());
		
		ArrayList<String> aList = (ArrayList<String>) ex.getLeafExpr();
		
//		for (String string : aList) {
//			System.out.println(string);
//		}
		
		ex.insertPartial("30","10 20 +");
		
		System.out.println(ex.getExpression());
		
		ex.insertPartial("16","4 4 *");
		
		System.out.println(ex.getExpression());
		
	}
}
