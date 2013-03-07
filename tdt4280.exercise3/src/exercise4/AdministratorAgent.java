package exercise4;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.AMSService;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
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
    
    /**
     * Searches for SolverAgents that provide solutions for a given operator.
     * @param operator
     * @return List of solvers
     */
    public List<AID> getSolvers(String operator){
        List<AID> solvers = new ArrayList<AID>();
        
        DFAgentDescription dfa = new DFAgentDescription();
        dfa.addLanguages("MATH LOL");
        dfa.addOntologies(operator);
        
        DFAgentDescription[] results = null;
        try {
            results = DFService.search(this, dfa);
        } catch (FIPAException ex) {
            Logger.getLogger(TaskAdministrator.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        for (int i = 0; i < results.length; i++){
            solvers.add(results[i].getName());
        }
        
        return solvers;
    }
    
    /**
     * @deprecated We should instead search for solvers for a particular problem using getSolvers()
     */
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
	
        /**
         * 
         * @param receiver
         * @param content 
         * @deprecated Should just use the other function since we use many different
         * performatives.
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
    
    public void broadcastMessage(String Content, int type){
    	
    	ACLMessage msg = new ACLMessage(type);
    	msg.setContent(Content);
    	for (AMSAgentDescription ag : solverAgents) {
			msg.addReceiver(ag.getName());
		}
    	
    	send(msg);
    }

}
