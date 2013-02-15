package agents;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

@SuppressWarnings("serial")
public class DefectingAgent extends GeneralAgent {

	public void setup(){
		
		addBehaviour(new CyclicBehaviour() {
			
			@Override
			public void action() {
//			System.out.println("Checking for new messages");
			ACLMessage msg = receive();
				if (msg != null){
					try {
						handleMessage(msg);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	private void handleMessage(ACLMessage msg){

		if(msg.getContent() == "DILEMMA"){
			ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
			reply.setContent("DEFECT");
			reply.addReceiver(msg.getSender());
		}
		
		}

}
