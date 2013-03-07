package exercise4;

import jade.lang.acl.ACLMessage;



@SuppressWarnings("serial")
public class DivisionSolver extends SolverAgent {

    @Override
    public void setup() {
        super.setup(); //To change body of generated methods, choose Tools | Templates.
        registerService("/");
    }

        @Override
	String solve(double x, double y) {
		double result = x/y;
		return ""+result;
	}

	@Override
	void handleCFP(ACLMessage msg) {

		if(msg.getContent().equals("/")) sendMessage(msg.getSender(), "1", ACLMessage.PROPOSE);
		
	}


}
