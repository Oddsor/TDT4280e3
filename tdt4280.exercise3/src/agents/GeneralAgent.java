package agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        private int score = 0;
        
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
        
        public void sendMessage(jade.core.AID receiver, String content, int type){
            ACLMessage msg = new ACLMessage(type);
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
                            ACLMessage msg = blockingReceive();
                            if (msg != null){
                                    try {
                                            ag.handleMessage(msg);
                                            ag.internallyHandleMessage(msg);
                                    } catch (Exception e) {
                                            e.printStackTrace();
                                    }
                            }
                    }
		});
	}
	
	private void internallyHandleMessage(ACLMessage msg){
		
		
		if(msg.getPerformative()== ACLMessage.INFORM){
			score = score+Integer.parseInt(msg.getContent());
			
		}
		//TODO: Resetting the score here isn't really good control flow.
		if(msg.getPerformative() == ACLMessage.QUERY_IF){
			String temp = ""+score;
			sendMessage(msg.getSender(), temp, ACLMessage.AGREE);
			score = 0;
		}
		
	}
	
	abstract void handleMessage(ACLMessage msg); 
	}
	

