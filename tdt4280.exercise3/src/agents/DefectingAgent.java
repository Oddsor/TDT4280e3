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
                System.out.println(this.getLocalName() + " got message " + msg.getContent() + ", answers.");
		if(msg.getContent().equals(DILEMMA))
			
			sendMessage(msg.getSender(), DEFECT, ACLMessage.PROPOSE);
		}
}
