
package exercise5;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

/**
 *
 * @author Odd
 */
public class OfferItemBehaviour extends OneShotBehaviour{

    ACLMessage msg;
    IItem item;
    
    public OfferItemBehaviour(Agent myAgent, IItem item, ACLMessage msg) {
        this.myAgent = myAgent;
        this.item = item;
        this.msg = msg;
    }
    
    @Override
    public void action() {
        ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
        reply.addReceiver(msg.getSender());
        reply.setContent(item.getName() + ";" + item.getPrice());
        myAgent.send(reply);
    }
    
}
