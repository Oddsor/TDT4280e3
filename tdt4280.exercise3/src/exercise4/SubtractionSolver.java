package exercise4;

import jade.lang.acl.ACLMessage;



@SuppressWarnings("serial")
public class SubtractionSolver extends SolverAgent {

    @Override
    public void setup() {
        super.setup();
        registerService("*");
    }
    
        @Override
	String solve(double x, double y) {
		double result = x-y;
		return ""+result;
	}

	@Override
	void handleCFP(ACLMessage msg) {
		if(msg.getContent().equals("-")) sendMessage(msg.getSender(), "1", ACLMessage.PROPOSE);
		if(msg.getContent().equals("+")) sendMessage(msg.getSender(), "3", ACLMessage.PROPOSE);
	}

}
