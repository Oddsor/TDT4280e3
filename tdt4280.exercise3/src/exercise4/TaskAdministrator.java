package exercise4;

import jade.core.AID;
import java.util.ArrayList;
import jade.lang.acl.ACLMessage;
import java.util.HashMap;
import java.util.LinkedList;
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
        //Map<AID, String> expectedReturn;
        String currentPartial;
        List<String> solvables;
        
        LinkedList<Object[]> expectedReturn;

    @Override
    public void setup() {
        super.setup(); //To change body of generated methods, choose Tools | Templates.
        expectedReturn = new LinkedList<Object[]>();
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
                for(Object[] o: expectedReturn){
                    if(o[0].equals(msg.getSender())){
                        handleSolvedExpression(msg);
                    }
                }
                break;
            default:
                    System.out.println("Unsupported Performative" + msg.getPerformative(msg.getPerformative()));
                    break;
            }
	}
        
        private void handleSolvedExpression(ACLMessage msg){
            int pointer = 0;
            for(int i = 0; i < expectedReturn.size(); i++){
                if(expectedReturn.get(i)[0].equals(msg.getSender())){
                    pointer = i;
                    break;
                }
            }
            String relatedExpression = (String) expectedReturn.get(pointer)[1];
            
            System.out.println("Msg: " + msg.getContent() + ", expression: " + relatedExpression);
            
            expression.insertPartial(msg.getContent(), relatedExpression);
            
            expectedReturn.remove(pointer);
            if(expectedReturn.isEmpty()){
                System.out.println("Getting new sub-expressions");
                solvables = expression.getLeafExpr();
                if(solvables.size() == 0){
                    System.out.println("Done calculating, answer is: " + expression.root.value);
                }else{
                    auctionJob(solvables.get(0));
                }
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
                if (bestTime > propTime) {
                    best = proposal.getSender();
                }
            }
            sendMessage(best, "", ACLMessage.ACCEPT_PROPOSAL);
            Object[] ob = new Object[2];
            ob[0] = best;
            ob[1] = currentPartial;
            expectedReturn.add(ob);
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
            currentPartial = expression;
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
