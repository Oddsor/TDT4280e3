package agents;

import FIPA.FipaMessage;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;


@SuppressWarnings("serial")
public class GeneralAgent extends Agent {

			
	/**
	 * Turns a String into an ACLMessage and sends it.
	 * @param message
	 */
	public void send(String message){
		
	}
	
	@Override
	public void setup(){
		
		addBehaviour(new CyclicBehaviour() {
			
			@Override
			public void action() {
//			System.out.println("Checking for new messages");
			ACLMessage msg = receive();
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

