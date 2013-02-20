package agents;

import java.util.ArrayList;

import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class TitForTatAgent extends GeneralAgent {

	private boolean otherPlayerDefected = false; //Positive tit-for-tat.
	ArrayList<String> history = new ArrayList<String>();
	private final String DEFECT = "DEFECT";
	private final String COOPERATE = "COOPERATE";
	private final String DILEMMA = "DILEMMA";

	@Override
	void handleMessage(ACLMessage msg) {
		String message = msg.getContent();
		if(message.equals(DEFECT)) otherPlayerDefected = true;
		else if(message.equals(COOPERATE)) otherPlayerDefected = false;
		
		if(message.equals(DILEMMA)){
			
			sendMessage(msg.getSender(), otherPlayerDefected ? DEFECT : COOPERATE);
		}
	}

}
