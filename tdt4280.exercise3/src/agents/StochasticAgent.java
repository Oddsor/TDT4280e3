package agents;


import jade.lang.acl.ACLMessage;
/**
 * 
 * Agent that chooses to defect with a probability 
 * proportional to the number of defections it has 
 * encountered in the past. This is based on every 
 * encounter, not just the agent it is currently 
 * playing.
 */
@SuppressWarnings("serial")
public class StochasticAgent extends GeneralAgent {

	double NoFDefections=0;
	double total =0;
	
        @Override
	void handleMessage(ACLMessage msg) {

		String message = msg.getContent();

		if(message.equals(DEFECT)) NoFDefections++;

		if(message.equals(DILEMMA)){
			total++;
			sendMessage(msg.getSender(), lottery());
		}
	}

	private String lottery(){

		if(total !=0){

			return (NoFDefections/total > Math.random() ? DEFECT : COOPERATE);
		}

		return COOPERATE;

	}
}
