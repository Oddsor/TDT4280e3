/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exercise5;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Odd
 */
public class HandleOffersBehaviour extends WakerBehaviour{

    private IItem item;
    private TradingAgent agent;
    
    public HandleOffersBehaviour(Agent a, long timeout, IItem item) {
        super(a, timeout);
        this.agent = (TradingAgent) a;
        this.item = item;
    }

    @Override
    public void onStart() {
        TradingAgent agent = (TradingAgent) myAgent;
        Map<AID, Integer> offers = agent.pullOffers(item);
        Set<AID> sellers = offers.keySet();
        int lowestPrice = 1000000000;
        AID bestSeller = null;
        for(AID seller: sellers){
            if(offers.get(seller) < lowestPrice){
                lowestPrice = offers.get(seller);
                bestSeller = seller;
            }
        }
        if(agent.acceptPrice(item, lowestPrice)){
            myAgent.addBehaviour(new BuyItemBehaviour(myAgent, bestSeller, item, lowestPrice));
        }else{
            //TODO try to get lower price somehow
        }
    }
}
