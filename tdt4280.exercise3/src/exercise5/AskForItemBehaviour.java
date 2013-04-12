package exercise5;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.List;

/**
 *
 * @author Odd
 */
public class AskForItemBehaviour extends OneShotBehaviour{
    private IItem item;
    
    public AskForItemBehaviour(Agent agent, IItem item){
        this.item = item;
        this.myAgent = agent;
    }
    
    @Override
    public void action() {
        System.out.println("Asking if anyone has the item " + item.getName());
        ACLMessage ask = new ACLMessage(ACLMessage.QUERY_IF);
        
        List<AID> agents = TradingAgent.getOtherAgents(myAgent);
        for(AID agent: agents){
            ask.addReceiver(agent);
        }
        ask.setContent(item.getName());
        TradingAgent agent = (TradingAgent) myAgent;
        agent.expectOffers(item);
        myAgent.addBehaviour(new HandleOffersBehaviour(myAgent, 2000, item));
    }
    
}
