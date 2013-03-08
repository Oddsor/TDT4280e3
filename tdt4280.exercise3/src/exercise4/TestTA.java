/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exercise4;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Odd
 */
@SuppressWarnings("serial")
public class TestTA extends AdministratorAgent{
    List<ACLMessage> proposals;
    List<AID> solvers;
    List<PostfixExpression> expressions;

    @Override
    public void setup() {
        super.setup(); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    void handleMessage(ACLMessage msg) {
        switch(msg.getPerformative()){
            case ACLMessage.PROPOSE:
                System.out.println("Proposal received: " + msg.getPerformative());
                proposals.add(msg);
                System.out.println("Number of solvers: " + solvers.size() +
                        ", number of proposals: " + proposals.size());
                if(proposals.size() == solvers.size()){
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
                    System.out.println("Accepting proposal from " + best.getLocalName());
                    for(ACLMessage proposal: proposals){
                        if(!proposal.getSender().equals(best)){
                            sendMessage(proposal.getSender(), "", ACLMessage.REJECT_PROPOSAL);
                        }
                    }
                    proposals.clear();
                }
                break;
            case ACLMessage.QUERY_REF:
                System.out.println("Query-Ref received: " + msg.getPerformative());
                
                try{
                	expressions.add(new PostfixExpression(msg.getContent()));
                }
                catch(Exception e){
                	System.out.println("Exception caught");
                }
                
                solvers = getSolvers("+");
                for (AID s: solvers){
                    System.out.println(s.getLocalName());
                }
                proposals = new ArrayList<ACLMessage>();
                //for(int i = 0; i < 3; i++){
                    for(AID s: solvers){
                        sendMessage(s, "5 + 5", ACLMessage.CFP);
                    }
                //}
                break;
        }
    }
    
}
