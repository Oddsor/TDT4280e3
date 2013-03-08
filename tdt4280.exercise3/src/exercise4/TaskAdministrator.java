package exercise4;

import jade.core.AID;
import java.util.ArrayList;
import jade.lang.acl.ACLMessage;
import java.util.HashMap;
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
        PostfixExpression expression;
        List<AID> solvers;
        List<ACLMessage> proposals;
        Map<AID, String> expectedReturn;
        String currentPartial;
        List<String> solvables;

    @Override
    public void setup() {
        super.setup(); //To change body of generated methods, choose Tools | Templates.
        expectedReturn = new HashMap<AID, String>();
    }
	
        
        
	@Override
	void handleMessage(ACLMessage msg) {
            switch (msg.getPerformative()) {
            case ACLMessage.QUERY_REF:
                System.out.println("Query Ref received");
                handleExpression(msg.getContent());
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
            String relatedExpression = expectedReturn.get(msg.getSender());
            
            expression.insertPartial(msg.getContent(), relatedExpression);
            
            expectedReturn.remove(msg.getSender());
            if(expectedReturn.isEmpty()){
                solvables = expression.getLeafExpr();
                auctionJob(solvables.get(0));
            }
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
            expectedReturn.put(best, currentPartial);
            System.out.println("Accepting proposal from " + best.getLocalName());
            for(ACLMessage proposal: proposals){
                if(!proposal.getSender().equals(best)){
                    sendMessage(proposal.getSender(), "", ACLMessage.REJECT_PROPOSAL);
                }
            }
            proposals.clear();
            solvables.remove(0);
            if(!solvables.isEmpty()){
                auctionJob(solvables.get(0));
            }
        }
	
	/**
	 * 
	 * @param String expr containing an expression in 
	 *postfix notation
	 * @return String containing solution to expression
	 */
	private void handleExpression(String expr){
		expression = new PostfixExpression(expr);
                solvables = expression.getLeafExpr();
                String firstExp = solvables.get(0);
                auctionJob(firstExp);
	}
	
        /**
         * Start auctioning by searching for agents with service protocols
         * matching the operator.
         * @param expression the postfix expression
         * @return 
         */
	private void auctionJob(String expression){
            solvers = getSolvers(expression.split(" ")[2]);
            for (AID s: solvers){
                System.out.println(s.getLocalName());
            }
            proposals = new ArrayList<ACLMessage>();
            ACLMessage solveRequest = new ACLMessage(ACLMessage.CFP);
            solveRequest.setContent(expression);
            for(AID s: solvers){
                solveRequest.addReceiver(s);
            }
            send(solveRequest);
	}
}
