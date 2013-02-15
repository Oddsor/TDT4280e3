package agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;


@SuppressWarnings("serial")
public class GeneralAgent extends Agent {

	
	public GeneralAgent() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Turns a String into an ACLMessage and sends it.
	 * @param message
	 */
	public void send(String message){
		
	}
	
	public void setupAgent(){
		
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

}
