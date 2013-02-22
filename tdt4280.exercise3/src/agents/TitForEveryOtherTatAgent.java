package agents;

import jade.lang.acl.ACLMessage;
/**
 * 
 * Cooperates immediately after other agent cooperates
 * Defects if other agent defects twice in a row.
 *
 */
@SuppressWarnings("serial")
public class TitForEveryOtherTatAgent extends GeneralAgent{

	private boolean otherPlayerDefected = false; //Positive tit-for-tat.
	private boolean semaphore = false;

	@Override
	void handleMessage(ACLMessage msg) {
		String message = msg.getContent();
                System.out.println(this.getLocalName() + " got message " + message + ", answers.");
		if(message.equals(DEFECT)){

			semaphore = otherPlayerDefected;
			otherPlayerDefected = true;
		}
		else if(message.equals(COOPERATE)){

			otherPlayerDefected = false;
		}

		if(message.equals(DILEMMA)){
			sendMessage(msg.getSender(), (otherPlayerDefected && semaphore) ? DEFECT : COOPERATE);
			
		}
	}

}
