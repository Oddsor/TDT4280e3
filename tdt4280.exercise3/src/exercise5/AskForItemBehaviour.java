package exercise5;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.List;

/**
 *
 * @author Odd
 */
public class AskForItemBehaviour extends OneShotBehaviour{
    private IItem item;
    
    public AskForItemBehaviour(IItem item){
        this.item = item;
    }
    
    @Override
    public void action() {
        ACLMessage ask = new ACLMessage(ACLMessage.QUERY_IF);
        
        List<AID> agents = TradingAgent.getOtherAgents(myAgent);
        for(AID agent: agents){
            ask.addReceiver(agent);
        }
        ask.setContent(item.getName());
    }
    
}
