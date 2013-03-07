package exercise4;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.DataStore;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract class for handling general communication
 * @author Andreas
 *
 */

@SuppressWarnings("serial")
public abstract class SolverAgent extends Agent {
    private WakerBehaviour delayedReply;
    private int replySpeed = 0;
    
    /**
     * Here we register the agent's protocol in the DF service thingamajig.
     * @param operand
     */
    public void registerService(String operator){
        DFAgentDescription dfa = new DFAgentDescription();
        dfa.addLanguages("MATH LOL");
        ServiceDescription sd = new ServiceDescription();
        sd.addProtocols(operator);
        dfa.addServices(sd);
        try {
            DFService.register(this, dfa);
        } catch (FIPAException ex) {
            Logger.getLogger(SolverAgent.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

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
            Random rand = new Random();
            replySpeed = rand.nextInt(4) + 1;
            
            //TODO: if operator-variable isn't updated upwards (from i.e. multiplication solver to solveragent), then fix.
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
			handleCFP(msg);
                    break;
                case ACLMessage.ACCEPT_PROPOSAL:
                    delayedReply = new ReplyBehaviour(this, replySpeed * 1000, msg);
                    addBehaviour(delayedReply);
		default:
			break;
		}
	}
	abstract String solve(double x, double y);
        
        /**
         * We expect messages in the form of "5 + 5"
         * @param msg 
         */
	abstract void handleCFP(ACLMessage msg);
        
        /**
         * Put 
         * @param receiver
         * @param answer
         * @param delay 
         */
        private void delayedReply(AID receiver, double answer,int delay){
            ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
            reply.addReceiver(receiver);
            reply.setContent("" + answer);
            
            DataStore ds = new DataStore();
            ds.put("msg", reply);
        }
}



