
package exercise5.behaviours;

import exercise5.IItem;
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
        System.out.println(myAgent.getLocalName() + " offers " + item.getName() 
                + " to " + msg.getSender().getLocalName() + " for " + item.getPrice());
        ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
        reply.addReceiver(msg.getSender());
        reply.setContent(item.getName() + ";" + item.getPrice());
        myAgent.send(reply);
    }
    
}
