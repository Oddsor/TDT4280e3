package exercise5.behaviours;

import exercise5.IItem;
import exercise5.TradingAgent;
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
        agent.addBehaviour(new HandleOffersBehaviour(agent, 2000, item));
    }
    
}
