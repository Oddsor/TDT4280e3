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
                        sendMessage(msg.getSender(), DEFECT);
		}
	}
}
