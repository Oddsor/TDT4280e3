package agents;


import jade.lang.acl.ACLMessage;
/**
 * 
 * Agent that chooses by random. Used as a baseline for other agents.
 *
 */
@SuppressWarnings("serial")
public class RandomAgent extends GeneralAgent {


	@Override
	void handleMessage(ACLMessage msg){
            System.out.println(this.getLocalName() + " got message " + msg.getContent() + ", answers.");
		if(msg.getContent().equals(DILEMMA))
			sendMessage(msg.getSender(), Math.random() < 0.5 ? DEFECT : COOPERATE, ACLMessage.PROPOSE);

	}
}
