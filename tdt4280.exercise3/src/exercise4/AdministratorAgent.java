package exercise4;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.AMSService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;





@SuppressWarnings("serial")
public abstract class AdministratorAgent extends Agent {

	private List<AMSAgentDescription> solverAgents;
  
	
    @Override
    public void setup() {
    
    	populateSolvers();
        for(AMSAgentDescription a: solverAgents){
            System.out.println(a.getName());
        }
        
        addBehaviour(new CyclicBehaviour(this) {
            public Agent getMyAgent() {
                return myAgent;
            }
	@Override
	public void action() {
                    AdministratorAgent ag = (AdministratorAgent) getMyAgent();
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
    
    public void populateSolvers(){
    	solverAgents = new ArrayList<AMSAgentDescription>();
        SearchConstraints sc = new SearchConstraints(); 
        sc.setMaxResults(100L); // not sure of the default value, but this ensures you get them all. 
        AMSAgentDescription[] evalAgents = null;
        try {
            evalAgents = AMSService.search(this, new AMSAgentDescription(), sc);
        } catch (FIPAException ex) {
            Logger.getLogger(AdministratorAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(int i = 0; i < evalAgents.length; i++){
            if(!evalAgents[i].getName().getLocalName().toString().equals("ams") &&
                    !evalAgents[i].getName().getLocalName().toString().equals("df") &&
                    !evalAgents[i].getName().getLocalName().toString().equals("rma") &&
                    !evalAgents[i].getName().getLocalName().toString().equals(this.getAID().getLocalName()) &&
                    !evalAgents[i].getName().getLocalName().contains("sniffer")){
                solverAgents.add(evalAgents[i]);
            }
        }
    }
    
	abstract void handleMessage(ACLMessage msg);
	
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
    
    public void broadcastMessage(String Content, int type){
    	
    	ACLMessage msg = new ACLMessage(type);
    	msg.setContent(Content);
    	for (AMSAgentDescription ag : solverAgents) {
			msg.addReceiver(ag.getName());
		}
    	
    	send(msg);
    }

}
