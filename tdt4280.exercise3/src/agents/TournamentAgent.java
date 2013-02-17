package agents;

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
public class TournamentAgent extends GeneralAgent {
    private List<AMSAgentDescription> contestants;
    private static final int currentFighter = 0;
    private int currentOpponent = -1;
    private int currentRound = -1;
    private int rounds = -1;
    private String fighterResponse = null;
    private String opponentResponse = null;

    @Override
    public void setup() {
        contestants = new ArrayList<AMSAgentDescription>();
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
        super.setup();
        startTournament(5);
    }
    public void startTournament(int rounds){
        System.out.println("Got ordered to start tournament with " + rounds + " rounds!");
        //currentFighter = 0; Turns out this will always be 0 if we drop the old fighter from contestants
        currentOpponent = 1;
        this.rounds = rounds;
        currentRound = 0;
        
        //Send one message only in case we get some issues.
        System.out.println("Starting tournament with " + 
                contestants.get(currentFighter).getName().getLocalName() + " and " + 
                contestants.get(currentOpponent).getName().getLocalName());
        sendMessage(contestants.get(currentFighter).getName(), DILEMMA);
    }
    
    /**
     * Handles the returned message:
     * If the message is from fighter, add his response and contact opponent.<br/>
     * If the message is from opponent, then add his response, send the results
     * back to the agents and go to next round by sending request to fighter. <br/>
     * If the current round is the last round, start fight with new opponent,
     * unless we're out of opponents. In this case remove the current fighter and
     * start with new fighter. If the current fighter is the second last, wrap up
     * the tournament.
     * @param msg 
     */
    //TODO verifiser og fiks kontrollflyten.
    public void handleReturn(ACLMessage msg){
        if(msg.getSender().equals(contestants.get(currentFighter).getName())){
            fighterResponse = msg.getContent();
            //We got an answer from fighter, if we still haven't contacted the opponent, do so now.
            if(opponentResponse == null){
                sendMessage(contestants.get(currentOpponent).getName(), DILEMMA);
                return;
            }
        }else if(msg.getSender().equals(contestants.get(currentOpponent).getName())){
            opponentResponse = msg.getContent();
        }
        //Both have answered, send results. Won't get replies here so can send both without worry?
        if (fighterResponse != null && opponentResponse != null){
            sendMessage(contestants.get(currentFighter).getName(), opponentResponse);
            sendMessage(contestants.get(currentOpponent).getName(), fighterResponse);
            //Analyze results:
            if(fighterResponse.equals(DEFECT) && opponentResponse.equals(DEFECT)) System.out.println("Both defect.");
            else if(fighterResponse.equals(DEFECT) && opponentResponse.equals(COOPERATE)){
                System.out.println(contestants.get(currentFighter).getName().getLocalName() + " wins.");
            }else if(fighterResponse.equals(COOPERATE) && opponentResponse.equals(DEFECT)){
                System.out.println(contestants.get(currentOpponent).getName().getLocalName() + " wins.");
            }else{
                System.out.println("Both cooperate!");
            }
            fighterResponse = null; //Clearing responses.
            opponentResponse = null;
            currentRound++;
        }
        if(currentRound < rounds){
            //One round complete, request new input from fighter:
            sendMessage(contestants.get(currentFighter).getName(), DILEMMA);
        }else{
            /*Done with fight, now we reset and start with a new opponent (or a 
             * new fighter if opponent is last in table. Toss current fighter out of contestant table)*/
            currentRound = 0;
            if(currentOpponent < contestants.size() - 1){
                currentOpponent++;
                System.out.println("Fight over, starting new fight with " + contestants.get(currentFighter).getName().getLocalName() 
                        + " and " + contestants.get(currentOpponent).getName().getLocalName());
                sendMessage(contestants.get(currentFighter).getName(), DILEMMA);
            }else{
                if(contestants.size() == 2){
                    System.out.println("Tournament has ended...");
                }else{
                    System.out.println(contestants.get(currentFighter).getName().getLocalName() + 
                        " done with fighting. New fighter is " + 
                        contestants.get(currentFighter + 1).getName().getLocalName() +" and first opponent is " + 
                        contestants.get(currentFighter + 2).getName().getLocalName());
                    contestants.remove(currentFighter);
                    currentOpponent = 1;
                    sendMessage(contestants.get(currentFighter).getName(), DILEMMA);
                }
                
            }
        }
    }

    @Override
    void handleMessage(ACLMessage msg){
        if(msg.getPerformative() == ACLMessage.REQUEST){
            startTournament(Integer.parseInt(msg.getContent()));
        }
        if(msg.getPerformative() == ACLMessage.CFP){
            handleReturn(msg);
        }
    }
}