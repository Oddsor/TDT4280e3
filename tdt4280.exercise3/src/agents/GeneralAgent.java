package agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;


@SuppressWarnings("serial")
public abstract class GeneralAgent extends Agent {
        static final String DILEMMA = "DILEMMA";
        static final String DEFECT = "DEFECT";
        static final String COOPERATE = "COOPERATE";
        
	@Override
	public void setup(){
		final Agent agent = this;
		addBehaviour(new CyclicBehaviour(agent) {
			
			@Override
			public void action() {
//			System.out.println("Checking for new messages");
                        GeneralAgent ag = (GeneralAgent) agent;
			ACLMessage msg = null;
                        msg = receive();
				if (msg != null){
					try {
                                            System.out.println("Message get!");
						ag.handleMessage(msg);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}
	abstract void handleMessage(ACLMessage msg); 
	}

