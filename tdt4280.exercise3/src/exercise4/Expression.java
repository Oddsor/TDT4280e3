package exercise4;

import java.util.ArrayList;

public class Expression {

	Node root;
	String oplist[] = {"+","-","/","*"};
	ArrayList<String> op = new ArrayList<String>();
	ArrayList<String> num = new ArrayList<String>();
	String expr;
	public Expression(String expr) {
		this.expr = expr;
	}

	public String parse(){
		
		String exprArray[] = expr.split(" ");
		
					
		for (int i = 0; i < exprArray.length; i++) {
			
			if(isInOplist(exprArray[i])) op.add(exprArray[i]);
			else num.add(exprArray[i]);
			
		}
		createTree();
		return toString();
		
	}
	
	private void createTree(){
		
	}
	
	boolean isInOplist(String n){
		
		for(int j =0; j <oplist.length; j++){
			if(n.equals(oplist[j])) return true;
		}
		
		return false;
	}
	
	public String toString(){
		String opString="";
		String numString="";
		
		
		
		for (String s : op) {
			opString +=s+" ";
		}
	
		for (String s : num) {
			numString +=s+" ";
		}
		
		String toString = "Members of opString: "+opString+
							"\nMembers of numString: "+numString;
		
		return toString;
	}
	
	public static void main(String[] args) {
		
		Expression ex = new Expression("+ + 10 20 10");
		System.out.println(ex.parse());
	}
}
