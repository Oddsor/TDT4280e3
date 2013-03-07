package exercise4;

import jade.lang.acl.ACLMessage;
/**
 * Implementation of SolverAgent for subtraction 
 * @author Andreas
 *
 */

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
	
	/**
	 * Subtraction agent can handle two operations, subtraction
	 * and addition. Addition is performed by double subtraction.
	 * This is not implemented yet.
	 * @param ACLMessage msg
	 */
	void handleCFP(ACLMessage msg) {
		if(msg.getContent().equals("-")) sendMessage(msg.getSender(), "1", ACLMessage.PROPOSE);
		if(msg.getContent().equals("+")) sendMessage(msg.getSender(), "3", ACLMessage.PROPOSE);
	}

}
