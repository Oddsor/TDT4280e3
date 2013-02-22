package agents;

import java.util.ArrayList;

import jade.lang.acl.ACLMessage;

/**
 * An agent that utilizes a mixture of two different tactics.
 * Half the time, the agent will retaliate and half the time
 * it will simply defect regardlessly. 
 * 
 *
 */

@SuppressWarnings("serial")
public class MixedAgent extends GeneralAgent {
	
	private boolean otherPlayerDefected = false; //Positive tit-for-tat.
	
	
	@Override
	void handleMessage(ACLMessage msg) {
                System.out.println(this.getLocalName() + " got message " + msg.getContent() + ", answers.");
		String message = msg.getContent();
		if(message.equals(DEFECT)) otherPlayerDefected = true;
		else if(message.equals(COOPERATE)) otherPlayerDefected = false;

		if(message.equals(DILEMMA)){
			if(Math.random() > 0.5)
				sendMessage(msg.getSender(), otherPlayerDefected ? DEFECT : COOPERATE);

			else sendMessage(msg.getSender(), DEFECT);
		}
	}
}
