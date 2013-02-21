package agents;


import jade.lang.acl.ACLMessage;
/**
 * 
 * Agent that always defects
 *
 */
@SuppressWarnings("serial")
public class DefectingAgent extends GeneralAgent {
   
	@Override
	void handleMessage(ACLMessage msg){
	
		if(msg.getContent().equals(DILEMMA))
			
			sendMessage(msg.getSender(), DEFECT);
		}
}
