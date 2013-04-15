/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exercise5.behaviours;

import exercise5.IItem;
import exercise5.TradingAgent;
import exercise5.behaviours.BuyItemBehaviour;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.List;
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
        this.agent.waker = this;
    }

    @Override
    public void onWake() {
        System.out.println("\n" + agent.getLocalName() + " is done waiting for offers, "
                + "picking best offer for " + item.getName());
        Map<AID, Integer> offers = agent.pullOffers(item);
        if (offers.size() == 0){
            System.out.println("We've received " + offers.size() + " offers.");
        }else{
            Set<AID> sellers = offers.keySet();
            int lowestPrice = 1000000000;
            AID bestSeller = null;
            for(AID seller: sellers){
                if(offers.get(seller) < lowestPrice){
                    lowestPrice = offers.get(seller);
                    bestSeller = seller;
                }
            }

            //Cancel other transactions
            ACLMessage cancelOrders = new ACLMessage(ACLMessage.CANCEL);
            cancelOrders.setContent(item.getName());
            for(AID seller: sellers){
                if(!seller.equals(bestSeller)){
                    cancelOrders.addReceiver(seller);
                }
            }
            agent.send(cancelOrders);

            System.out.println(bestSeller.getLocalName() + " had the best offer, and we accept it.\n");
            myAgent.addBehaviour(new BuyItemBehaviour(myAgent, bestSeller, item, lowestPrice));
        }
        
        agent.addBehaviour(new AskForItemBehaviour(agent, 100));
    }
}
