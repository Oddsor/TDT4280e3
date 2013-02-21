package agents;

import jade.lang.acl.ACLMessage;
/**
 * 
 * Agent that always cooperates
 *
 */

@SuppressWarnings("serial")
public class CooperatingAgent extends GeneralAgent {

   
	@Override
	void handleMessage(ACLMessage msg) {
		
		if(msg.getContent().equals(DILEMMA)){
			sendMessage(msg.getSender(), COOPERATE);
		}
	}

}