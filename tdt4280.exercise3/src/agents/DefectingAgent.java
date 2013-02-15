package agents;


import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class DefectingAgent extends GeneralAgent {

        @Override
	void handleMessage(ACLMessage msg){

		if(msg.getContent().equals(DILEMMA)){
			ACLMessage reply = new ACLMessage(ACLMessage.CFP);
			reply.setContent(DEFECT);
			reply.addReceiver(msg.getSender());
                        send(msg);
		}
		
		}

}
