package exercise4;

import jade.core.AID;
import java.util.ArrayList;
import jade.lang.acl.ACLMessage;
import java.util.List;
import java.util.Map;

/**
 *TaskAdministrator accepts ACL Messages. It accepts expressions in
 *postfix notation and distributes each subtask to an appropriate agent
 *through an auction procedure. 
 * @author Andreas
 *
 */
@SuppressWarnings("serial")
public class TaskAdministrator extends AdministratorAgent {
        List<AID> solvers;
        List<ACLMessage> proposals;
        Map<AID, Expression> expectedReturn;
        Expression currentExpression;
	
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
            case ACLMessage.PROPOSE:
                handleProposal(msg);
                break;
            case ACLMessage.INFORM:
                if(expectedReturn.containsKey(msg.getSender())){
                    handleSolvedExpression(msg);
                }
            default:
                    System.out.println("Unsupported Performative");
                    break;
            }
	}
        
        private void handleSolvedExpression(ACLMessage msg){
            Expression relatedExpression = expectedReturn.get(msg.getSender());
            double result = Double.parseDouble(msg.getContent());
            
            //TODO: Somehow replace expression with this result.
            expectedReturn.remove(msg.getSender());
        }
        
        private void handleProposal(ACLMessage msg){
            proposals.add(msg);
            if(proposals.size() == solvers.size()){
                declareWinner();
            }
        }
        
        /**
         * Accepts proposal of best candidate
         */
        private void declareWinner(){
            System.out.println("All proposals received");
            AID best = proposals.get(0).getSender();
            for(ACLMessage proposal: proposals){
                String time = proposal.getContent().replaceFirst("bid\\(", "");
                time = time.replaceFirst(" sec\\)", "");
                String bestTimeString = 
                        proposal.getContent().
                            replaceFirst("bid\\(", "").
                            replaceFirst(" sec\\)", "");
                int bestTime = Integer.parseInt(bestTimeString);
                int propTime = Integer.parseInt(time);
                if (bestTime < propTime) {
                    best = proposal.getSender();
                }
            }
            sendMessage(best, "", ACLMessage.ACCEPT_PROPOSAL);
            expectedReturn.put(best, currentExpression);
            System.out.println("Accepting proposal from " + best.getLocalName());
            for(ACLMessage proposal: proposals){
                if(!proposal.getSender().equals(best)){
                    sendMessage(proposal.getSender(), "", ACLMessage.REJECT_PROPOSAL);
                }
            }
            proposals.clear();
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
	private void auctionJob(String operand1, String operand2, String operator){
            solvers = getSolvers(operator);
            for (AID s: solvers){
                System.out.println(s.getLocalName());
            }
            proposals = new ArrayList<ACLMessage>();
            ACLMessage solveRequest = new ACLMessage(ACLMessage.CFP);
            solveRequest.setContent(operand1 + " " + operator + " " + operand2);
            for(AID s: solvers){
                solveRequest.addReceiver(s);
            }
            send(solveRequest);
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
