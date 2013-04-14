
package exercise5.behaviours;

import exercise5.IItem;
import exercise5.TradingAgent;
import exercise5.strategies.IStrategy;
import exercise5.strategies.RandomStrategy;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

/**
 *
 * @author Odd
 */
public class NegotiateBehaviour extends OneShotBehaviour{

    private TradingAgent ta;
    private IItem item;
    private AID tradePartner;
    private int price;
    
    public NegotiateBehaviour(Agent ta, IItem item, AID tradePartner, int price) {
        this.ta = (TradingAgent) ta;
        this.item = item;
        this.tradePartner = tradePartner;
        this.price = price;
    }
    
    @Override
    public void action() {
        IStrategy strategy = (IStrategy) new RandomStrategy();
        //int priceSuggestion = negotiate.suggestPrice(item, price);
        double random = Math.random();
        int priceSuggestion = strategy.suggestPrice(item, price);
        
        if(priceSuggestion <0){
            ta.addOffer(item, tradePartner, price);
        }else{
            ACLMessage reply = new ACLMessage(ACLMessage.PROPOSE);
            reply.addReceiver(tradePartner);
            reply.setContent(item.getName() + ";" + (priceSuggestion - 20));
        }
    }
    
}
