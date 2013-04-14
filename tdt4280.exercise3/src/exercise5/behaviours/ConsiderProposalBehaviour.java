/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exercise5.behaviours;

import exercise5.IItem;
import exercise5.TradingAgent;
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
        double rand = Math.random();
        if(rand <= 0.7){
            ACLMessage accept = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
            accept.addReceiver(buyer);
        }
    }
    
}
