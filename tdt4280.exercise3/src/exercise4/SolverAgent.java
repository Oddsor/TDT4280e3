package exercise4;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Abstract class for handling general communication
 * @author Andreas
 *
 */

@SuppressWarnings("serial")
public abstract class SolverAgent extends Agent {

	public void sendMessage(jade.core.AID receiver, String content){
		ACLMessage msg = new ACLMessage(ACLMessage.CFP);
		msg.addReceiver(receiver);
		msg.setContent(content);
		send(msg);
	}

	public void sendMessage(jade.core.AID receiver, String content, int type){
		ACLMessage msg = new ACLMessage(type);
		msg.addReceiver(receiver);
		msg.setContent(content);
		send(msg);
	}

	//TODO: Add ability to wait through WakerBehaviour.
	@Override
	public void setup(){
		addBehaviour(new CyclicBehaviour(this) {
			public Agent getMyAgent() {
				return myAgent;
			}
			@Override
			public void action() {
				SolverAgent ag = (SolverAgent) getMyAgent();
				ACLMessage msg = blockingReceive();
				if (msg != null){
					try {
						ag.handleMessage(msg);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}


	
	private void handleMessage(ACLMessage msg){
		switch (msg.getPerformative()) {
		case ACLMessage.QUERY_IF:

			String msgArray[] = msg.getContent().split(",");
			double x = Double.parseDouble(msgArray[0]);
			double y = Double.parseDouble(msgArray[1]);
			sendMessage(msg.getSender(), solve(x,y));
			System.out.println(solve(x,y));
			break;
		case ACLMessage.CFP:
			System.out.println("Call for proposal received: "+msg.getContent());
		default:
			break;
		}
	}
	abstract String solve(double x, double y);
}



