package agents;


import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class DefectingAgent extends GeneralAgent {

    @Override
    public void setup() {
        super.setup(); //To change body of generated methods, choose Tools | Templates.
    }
    

        @Override
	void handleMessage(ACLMessage msg){

		if(msg.getContent().equals(DILEMMA)){
                    System.out.println("Answering with defect");
			ACLMessage reply = new ACLMessage(ACLMessage.CFP);
			reply.setContent(DEFECT);
			reply.addReceiver(msg.getSender());
                        send(msg);
		}	
	}
}
