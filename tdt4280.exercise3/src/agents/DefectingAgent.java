package agents;


import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class DefectingAgent extends GeneralAgent {

	

	void handleMessage(ACLMessage msg){

		if(msg.getContent() == "DILEMMA"){
			ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
			reply.setContent("DEFECT");
			reply.addReceiver(msg.getSender());
		}
		
		}

}
