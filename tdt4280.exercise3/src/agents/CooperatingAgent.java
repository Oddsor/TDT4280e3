package agents;

import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class CooperatingAgent extends GeneralAgent {

    @Override
    public void setup() {
        super.setup(); //To change body of generated methods, choose Tools | Templates.
    }
    

	@Override
	void handleMessage(ACLMessage msg) {
		if(msg.getContent().equals(DILEMMA)){
			ACLMessage reply = new ACLMessage(ACLMessage.CFP);
			reply.setContent(COOPERATE);
			reply.addReceiver(msg.getSender());
                        send(msg);
		}
	}

}