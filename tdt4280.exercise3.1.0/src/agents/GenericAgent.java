package agents;

import java.util.ArrayList;
import java.util.Iterator;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

public abstract class GenericAgent extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("serial")
	protected void setup() {
		
		System.out.println("Hello!");
		addBehaviour(new CyclicBehaviour() {
			
			@Override
			public void action() {
			jade.lang.acl.ACLMessage msg = receive();
				if (msg != null){
					try {
						System.out.println("Hooray! A message! "+msg.getContent());
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	/**
	 * Sends a message as a textString to all other agents
	 */
	public void sendMSG(String message){
		
//		MapRepresentation map = see();
//		jade.lang.acl.ACLMessage msg = new jade.lang.acl.ACLMessage(jade.lang.acl.ACLMessage.INFORM);
//		Iterator iter = map.getBuddies().iterator();
//		while( iter.hasNext()){
//			String name = (String) iter.next();
//			msg.addReceiver(map.getAID(name));
//		}
//		try {
//			msg.setContent(message);
//		} catch (Exception e){
//			e.printStackTrace();
//		}
//		send(msg);
	}
	
	public abstract String decideResponse();

	
}
