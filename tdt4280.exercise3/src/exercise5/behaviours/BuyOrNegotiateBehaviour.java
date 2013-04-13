
package exercise5.behaviours;

import exercise5.IItem;
import exercise5.Negotiate;
import exercise5.TradingAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

/**
 *
 * @author Odd
 */
public class BuyOrNegotiateBehaviour extends OneShotBehaviour{

    private TradingAgent ta;
    private IItem item;
    private AID tradePartner;
    private int price;
    
    public BuyOrNegotiateBehaviour(Agent ta, IItem item, AID tradePartner, int price) {
        this.ta = (TradingAgent) ta;
        this.item = item;
        this.tradePartner = tradePartner;
        this.price = price;
    }
    
    @Override
    public void action() {
        Negotiate negotiate = null;
        //int priceSuggestion = negotiate.suggestPrice(item, price);
        int priceSuggestion = -1;
        if(priceSuggestion == -1){
            ACLMessage reply = new ACLMessage(ACLMessage.AGREE);
            reply.addReceiver(tradePartner);
            reply.setContent("");
        }else{
            ACLMessage reply = new ACLMessage(ACLMessage.PROPOSE);
            reply.addReceiver(tradePartner);
            reply.setContent(item.getName() + ";" + priceSuggestion);
        }
    }
    
}
