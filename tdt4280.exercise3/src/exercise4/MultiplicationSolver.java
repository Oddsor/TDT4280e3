package exercise4;

import jade.lang.acl.ACLMessage;



@SuppressWarnings("serial")
public class MultiplicationSolver extends SolverAgent {

	String solve(double x, double y) {
		double result = x*y;
		return ""+result;
	}

	@Override
	void handleCFP(ACLMessage msg) {
		if(msg.equals("*")) sendMessage(msg.getSender(), "1", ACLMessage.PROPOSE);
		
	}


}
