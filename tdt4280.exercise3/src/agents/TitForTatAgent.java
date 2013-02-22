package agents;



import jade.lang.acl.ACLMessage;
/**
 * 
 * Agent that does what the other player did last round.
 *
 */
@SuppressWarnings("serial")
public class TitForTatAgent extends GeneralAgent {

	private boolean otherPlayerDefected = false; //Positive tit-for-tat.


	@Override
	void handleMessage(ACLMessage msg) {
		String message = msg.getContent();
                System.out.println(this.getLocalName() + " got message " + msg.getContent() + ", answers.");
		if(message.equals(DEFECT)) otherPlayerDefected = true;
		else if(message.equals(COOPERATE)) otherPlayerDefected = false;

		if(message.equals(DILEMMA)){
			sendMessage(msg.getSender(), otherPlayerDefected ? DEFECT : COOPERATE);
		}
	}

}
