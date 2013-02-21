package agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
/**
 * 
 * General agent class that supports sending and
 * receiving messages.
 */

@SuppressWarnings("serial")
public abstract class GeneralAgent extends Agent {
        static final String DILEMMA = "DILEMMA";
        static final String DEFECT = "DEFECT";
        static final String COOPERATE = "COOPERATE";
        
        /**
         * Sender beskjed med performativen som er bestemt av oppgaven (CFP)
         * Velger mottaker og setter innhold til aa vaere en av konstantene.
         * @param receiver
         * @param content 
         */
        public void sendMessage(jade.core.AID receiver, String content){
            ACLMessage msg = new ACLMessage(ACLMessage.CFP);
            msg.addReceiver(receiver);
            msg.setContent(content);
            send(msg);
        }
        
	@Override
	public void setup(){
		addBehaviour(new CyclicBehaviour(this) {
                    public Agent getMyAgent() {
                        return myAgent;
                    }
			@Override
			public void action() {
                            GeneralAgent ag = (GeneralAgent) getMyAgent();
                            ACLMessage msg = receive();
                            if (msg != null){
                                    try {
                                            ag.handleMessage(msg);
                                    } catch (Exception e) {
                                            e.printStackTrace();
                                    }
                            }
                            block(); //Nyttig for å stoppe cyclicbehaviour fra å loope i evigheter
                    }
		});
	}
	abstract void handleMessage(ACLMessage msg); 
	}

