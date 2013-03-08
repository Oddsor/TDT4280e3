package exercise4;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract class for handling general communication
 *
 * @author Andreas
 *
 */
@SuppressWarnings("serial")
public abstract class SolverAgent extends Agent {

    private int replySpeed = 1;
    
    ACLMessage receivedProblem;
    
    LinkedList<ReplyBehaviour> replyList;

    /**
     * Here we register the agent's protocol in the DF service thingamajig.
     *
     * @param operator
     */
    public void registerService(String operator) {
        DFAgentDescription dfa = new DFAgentDescription();
        dfa.addLanguages("MATH LOL");
        dfa.addOntologies(operator);
        try {
            DFService.register(this, dfa);
        } catch (FIPAException ex) {
            Logger.getLogger(SolverAgent.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

    public void sendMessage(jade.core.AID receiver, String content, int type) {
        ACLMessage msg = new ACLMessage(type);
        msg.addReceiver(receiver);
        msg.setContent(content);
        send(msg);
    }

    @Override
    public void setup() {
        //We may receive more problems and thus end up with a queue of behaviours!
        replyList = new LinkedList<ReplyBehaviour>();
        
        addBehaviour(new CyclicBehaviour(this) {
            public Agent getMyAgent() {
                return myAgent;
            }

            @Override
            public void action() {
                SolverAgent ag = (SolverAgent) getMyAgent();
                ACLMessage msg = receive();
                if (msg != null) {
                    try {
                        ag.handleMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void handleMessage(ACLMessage msg) {
        switch (msg.getPerformative()) {
            case ACLMessage.CFP:
                System.out.println("Call for proposal received: " + msg.getContent());
                receivedProblem = msg;
                bid(msg);
                break;
            case ACLMessage.ACCEPT_PROPOSAL:
                //Proposal was accepted, solve new problem.
                replyList.add(new ReplyBehaviour(this, getRelativeSpeed(true), receivedProblem));
                addBehaviour(replyList.getLast());
                break;
            default:
                break;
        }
    }
    
    public void bid(ACLMessage msg){
        String bidString = "bid(";
        bidString += getRelativeSpeed(false) + " sec)";
        System.out.println(this.getLocalName() + " bids: " + bidString);
        sendMessage(msg.getSender(), bidString, ACLMessage.PROPOSE);
    }
    
    public void problemSolved(ReplyBehaviour problem){
        replyList.remove(problem);
    }
    
    /**
     * Here we take the "normal" time of replySpeed and add up the total wait
     * time that is left in all remaining behaviours.
     * @return 
     */
    public int getRelativeSpeed(boolean milliseconds){
        int returnValue = replySpeed;
        returnValue += replyList.size();
        if(milliseconds) return returnValue * 1000;
        return returnValue;
    }

    abstract String solve(double x, double y);
    

    /**
     * We expect messages in the form of "5 + 5"
     * 
     * @deprecated Should not need this anymore.
     * @param msg
     */
    abstract void handleCFP(ACLMessage msg);
}
