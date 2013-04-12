/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exercise5;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

/**
 *
 * @author Odd
 */
class BuyItemBehaviour extends OneShotBehaviour {

    private TradingAgent agent;
    private IItem item;
    private AID seller;
    private int price;
    
    public BuyItemBehaviour(Agent a, AID seller, IItem item, int price) {
        this.agent = (TradingAgent) a;
        this.item = item;
        this.seller = seller;
        this.price = price;
    }

    @Override
    public void action() {
        ACLMessage msg = new ACLMessage(ACLMessage.AGREE);
        msg.addReceiver(seller);
        msg.setContent(item.getName() + ";" + price);
        agent.send(msg);
    }
    
}
