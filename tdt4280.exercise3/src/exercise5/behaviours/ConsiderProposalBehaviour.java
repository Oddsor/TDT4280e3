/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * @author Andreas
 */
public class ConsiderProposalBehaviour extends OneShotBehaviour{

    private TradingAgent ta;
    private AID buyer;
    private IItem item;
    private int price;
    
    public ConsiderProposalBehaviour(Agent a, AID buyer, IItem item, int price) {
        this.ta = (TradingAgent) a;
        this.buyer = buyer;
        this.item = item;
        this.price = price;
    }
    
    @Override
    public void action() {
        IStrategy strategy = (IStrategy) new RandomStrategy();
        int counterBid = strategy.considerBuyersBid(item, price);
        ACLMessage reply = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
        reply.addReceiver(buyer);
        if(counterBid == price){
            System.out.println(ta.getLocalName() + " accepts offer from " + buyer.getLocalName() + "; item: " + item.getName() + ", price: " + price);
            item.setLowestAccepted(price);
            reply.setContent(item.getName() + ";" + price);
        }else{
            reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
            reply.setContent(item.getName() + ";" + counterBid);
            System.out.println(ta.getLocalName() + " rejects offer from " + buyer.getLocalName() + "; item: " + item.getName() + ", price: " + item.getLowestAccepted());
        }
        ta.send(reply);
    }
    
}
