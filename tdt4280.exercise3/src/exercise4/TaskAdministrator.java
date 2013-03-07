package exercise4;

import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import java.util.ArrayList;

import jade.lang.acl.ACLMessage;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *TaskAdministrator accepts ACL Messages. It accepts expressions in
 *postfix notation and distributes each subtask to an appropriate agent
 *through an auction procedure. 
 * @author Andreas
 *
 */
@SuppressWarnings("serial")
public class TaskAdministrator extends AdministratorAgent {

	
	String oplist[] = {"+","-","/","*"};
	boolean semaphore = false;
	String response;
	
	@Override
	void handleMessage(ACLMessage msg) {
		switch (msg.getPerformative()) {
		case ACLMessage.QUERY_REF:
				System.out.println("Query Ref received");
				sendMessage(msg.getSender(), handleExpression(msg.getContent()));
			break;

		default:
			System.out.println("Unsupported Performative");
			break;
		}

	}
	
	/**
	 * 
	 * @param String expr containing an expression in 
	 *postfix notation
	 * @return String containing solution to expression
	 */
	private String handleExpression(String expr){
		
		//TODO Implement with java stack instead
		//TODO Add check for valid expression
		
		System.out.println("Received expression "+expr);
		String exprArray[] = expr.split(" ");
		ArrayList<String> stack = new ArrayList<String>();
		
		for (int i = 0; i < exprArray.length; i++) {
			if(!isInOplist(exprArray[i])) stack.add(exprArray[i]);
			if(isInOplist(exprArray[i])){
				
				String operand1 = stack.get(stack.size()-1);
				stack.remove(stack.size()-1);
				
				String operand2 = stack.get(stack.size()-1);
				stack.remove(stack.size()-1);
				
				String operator = exprArray[i];
				
				stack.add(auctionJob(operand1, operand2, operator));
				
			}
		}
		System.out.println(stack.get(stack.size()-1));
		return expr;
		
	}
	
        /**
         * Start auctioning by searching for agents with service protocols
         * matching the operator.
         * @param operand1
         * @param operand2
         * @param operator
         * @return 
         */
	private String auctionJob(String operand1, String operand2, String operator){
            DFAgentDescription dfa = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.addProtocols(operator); //Search for agent that has service protocol equal to operator
            dfa.addServices(sd);
            DFAgentDescription[] results = null;
            try {
                results = DFService.search(this, dfa);
            } catch (FIPAException ex) {
                Logger.getLogger(TaskAdministrator.class.getName()).log(Level.SEVERE, null, ex);
            }
            AID[] solvers = new AID[results.length];
            for (int i = 0; i < results.length; i++){
                solvers[i] = results[i].getName();
            }
            
		//TODO Implement actual auction protocol
		response ="";
		semaphore = false;
		broadcastMessage("Solve this ", ACLMessage.CFP);
		
		return solveSimpleExpr(operand1,operand2,operator);
	}
	
	private String solveSimpleExpr(String operand1, String operand2, String operator){
		
		System.out.println("Simple Expression: " +operand1+ " " + operand2+" "+operator);
		
		double o1 = Double.parseDouble(operand1);
		double o2 = Double.parseDouble(operand2);
		
		if(operator.equals("+")){
			double result = o1+o2;
			return ""+result;
		}
		
		if(operator.equals("-")){
			double result = o1-o2;
			return ""+result;
		}
		
		if(operator.equals("*")){
			double result = o1*o2;
			return ""+result;
		}
		
		if(operator.equals("/")){
			double result = o1/o2;
			return ""+result;
		}
		
		return null;
	}
	
	boolean isInOplist(String n){
		
		for(int j =0; j <oplist.length; j++){
			if(n.equals(oplist[j])) return true;
		}
		
		return false;
	}
	

}
