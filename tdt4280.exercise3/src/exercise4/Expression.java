package exercise4;

import java.util.ArrayList;

public class Expression {

	Node root;
	String oplist[] = {"+","-","/","*"};
	public Expression(String expr) {
		// TODO Auto-generated constructor stub
	}

	public void parse(String expr){
		
		String exprArray[] = expr.split(" ");
	
		ArrayList<String> op = new ArrayList<String>();
		ArrayList<String> num = new ArrayList<String>();

		for (int i = 0; i < exprArray.length; i++) {
			
			if(isInOplist(exprArray[i])) op.add(exprArray[i]);
			else num.add(exprArray[i]);
			
		}
		
		
		
	}
	
	boolean isInOplist(String n){
		
		for(int j =0; j <oplist.length; j++){
			if(n.equals(oplist[j])) return true;
		}
		
		return false;
	}
}
