package exercise5.behaviours;

import exercise5.IItem;
import exercise5.TradingAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Odd
 */
public class AskForItemBehaviour extends WakerBehaviour{
    private TradingAgent ta;
    
    public AskForItemBehaviour(Agent agent, int time){
        super(agent, time);
        this.myAgent = agent;
        ta = (TradingAgent) agent;
    }
    
    @Override
    public void onWake() {
        IItem item = ta.getNextDesiredItem();
        if(item == null){
            System.out.println(ta.getLocalName() + " ends the game.");
            ACLMessage stopGame = new ACLMessage(ACLMessage.CONFIRM);
            List<AID> others = TradingAgent.getOtherAgents(ta);
            for(AID other:others){
                stopGame.addReceiver(other);
            }
            stopGame.addReceiver(ta.getAID());
            ta.send(stopGame);
        }else{
            System.out.println("\n" + myAgent.getLocalName() + " asks if anyone has the item " + item.getName());
            ACLMessage ask = new ACLMessage(ACLMessage.QUERY_IF);

            List<AID> agents = TradingAgent.getOtherAgents(myAgent);
            for(AID agent: agents){
                ask.addReceiver(agent);
            }
            ask.setContent(item.getName());
            TradingAgent agent = (TradingAgent) myAgent;
            agent.expectOffers(item);
            Random rand = new Random();
            agent.addBehaviour(new HandleOffersBehaviour(agent, 2000 + (rand.nextInt(400) - 200), item));
            agent.send(ask);
        }
    }
    
}
