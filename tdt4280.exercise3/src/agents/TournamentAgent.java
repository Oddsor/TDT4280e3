package agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("serial")
public class TournamentAgent extends Agent {

    private List<AMSAgentDescription> contestants;

    @Override
    public void setup() {
        contestants = new ArrayList<>();
        SearchConstraints sc = new SearchConstraints(); 
        sc.setMaxResults(100L); // not sure of the default value, but this ensures you get them all. 
        AMSAgentDescription[] evalAgents = null;
        try {
            evalAgents = AMSService.search(this, new AMSAgentDescription(), sc);
        } catch (FIPAException ex) {
            Logger.getLogger(TournamentAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(int i = 0; i < evalAgents.length; i++){
            if(!evalAgents[i].getName().getLocalName().toString().equals("ams") &&
                    !evalAgents[i].getName().getLocalName().toString().equals("df") &&
                    !evalAgents[i].getName().getLocalName().toString().equals("rma") &&
                    !evalAgents[i].getName().getLocalName().toString().equals(this.getAID().getLocalName())){
                contestants.add(evalAgents[i]);
            }
        }
        for(AMSAgentDescription a: contestants){
            System.out.println(a.getName());
        }
        final Agent agent = this;
        addBehaviour(new CyclicBehaviour(agent) {
            @Override
            public void action() {
                TournamentAgent ta = (TournamentAgent) agent;
                ACLMessage msg = receive();
                if (msg != null){
                    try{
                        if(msg.getPerformative() == ACLMessage.REQUEST){
                            ta.startTournament(Integer.parseInt(msg.getContent()));
                        }
                        if(msg.getPerformative() == ACLMessage.CFP){
                            ta.handleReturn(msg);
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    public void startTournament(int rounds){
        System.out.println("Got ordered to start tournament with " + rounds + " rounds!");
    }
    public void handleReturn(ACLMessage msg){
        
    }
}