
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
@SuppressWarnings("serial")
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
        int priceSuggestion = strategy.considerSellersBid(item, price);
       
        if(priceSuggestion <0){
            ta.addOffer(item, tradePartner, price);
            System.out.println(ta.getLocalName() + " accepts offer from " + tradePartner.getLocalName() + ". Item: " + item.getName() + ", price: " + price);
        }else{
            ACLMessage reply = new ACLMessage(ACLMessage.PROPOSE);
            reply.addReceiver(tradePartner);
            reply.setContent(item.getName() + ";" + priceSuggestion);
            System.out.println(ta.getLocalName() + " negotiates offer from " + tradePartner.getLocalName() + ". Item: " + item.getName() + ", new price: " + priceSuggestion);
            ta.send(reply);
        }
    }
    
}
