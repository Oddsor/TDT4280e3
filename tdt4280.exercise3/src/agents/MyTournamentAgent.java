package agents;

import jade.domain.AMSService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import other.TournamentLogger;

@SuppressWarnings("serial")
public class MyTournamentAgent extends GeneralAgent {
   
	private List<AMSAgentDescription> contestants;
    private static final int currentFighter = 0;
    private int currentOpponent = -1;
    private int currentRound = -1;
    private int rounds = -1;
    private String fighterResponse = null;
    private String opponentResponse = null;
    private TournamentLogger tl = TournamentLogger.getTournamentLogger();

    /**
     * Handles specific stuff needed for tournament agent, for instance the list
     * of agents that are in the tournament. The superclass GeneralAgent handles
     * the behaviour that receives messages.
     * 
     * Also starts a tournament, even though tournaments can be started by 
     * sending a REQUEST-message to tournament agent with the number of rounds as
     * the message body (content).
     */
    @Override
    public void setup() {
    
    	populateContestants();
        for(AMSAgentDescription a: contestants){
            System.out.println(a.getName());
        }
        super.setup();
//      startTournament(5);
    }
    
    public void populateContestants(){
    	contestants = new ArrayList<AMSAgentDescription>();
        SearchConstraints sc = new SearchConstraints(); 
        sc.setMaxResults(100L); // not sure of the default value, but this ensures you get them all. 
        AMSAgentDescription[] evalAgents = null;
        try {
            evalAgents = AMSService.search(this, new AMSAgentDescription(), sc);
        } catch (FIPAException ex) {
            Logger.getLogger(MyTournamentAgent.class.getName()).log(Level.SEVERE, null, ex);
        }
        for(int i = 0; i < evalAgents.length; i++){
            if(!evalAgents[i].getName().getLocalName().toString().equals("ams") &&
                    !evalAgents[i].getName().getLocalName().toString().equals("df") &&
                    !evalAgents[i].getName().getLocalName().toString().equals("rma") &&
                    !evalAgents[i].getName().getLocalName().toString().equals(this.getAID().getLocalName())){
                contestants.add(evalAgents[i]);
            }
        }
    }
    
    public void startTournament(int rounds){
     
    	System.out.println("Got ordered to start tournament with " + rounds + " rounds!");
        currentOpponent = 1;
        this.rounds = rounds;
        currentRound = 0;
        fighterResponse = null;
        opponentResponse = null;
        
        //Send one message only in case we get some issues.
        String temp = "Starting match with " + 
                contestants.get(currentFighter).getName().getLocalName() + " and " + 
                contestants.get(currentOpponent).getName().getLocalName();
                
        System.out.println(temp);
        sendMessage(contestants.get(currentFighter).getName(), DILEMMA);
        
       
        tl.log("New tournament started with "+rounds+" rounds");
        tl.log(temp);
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
    	
    	System.out.println("TournamentAgent received message: "+msg.getContent()+" from "+ msg.getSender().getName());
    	
    	//TODO: Alt er drevet av meldingene man mottar, om en melding forsvinner
    	// stopper alt uten at det blir forsøkt å innhente nytt svar.
    	if(msg.getSender().equals(contestants.get(currentFighter).getName()))
    		handleFighterResponse(msg);
    	
    	if(msg.getSender().equals(contestants.get(currentOpponent).getName()))
    		handleOpponentResponse(msg);
       	
    }
    
private void handleFighterResponse(ACLMessage msg){
    	
	System.out.println("Received fighter response from "+ msg.getSender().getName());
    	//If the answer is from the current fighter:
    	if(msg.getSender().equals(contestants.get(currentFighter).getName()))
    		
    		fighterResponse = msg.getContent();
    		
        //We got an answer from fighter, if we still haven't contacted the opponent, do so now.
        if(opponentResponse == null)
           	System.out.println("Have not received answer from opponent yet. Sending CFP");
        	sendMessage(contestants.get(currentOpponent).getName(), DILEMMA);
               	
    	
    }
private void handleOpponentResponse(ACLMessage msg){
	System.out.println("Received opponent response from "+msg.getSender().getName());	
	opponentResponse = msg.getContent();
	
	handleResults();
}
private void handleResults(){
	//Both have answered, send results. Won't get replies here so can send both without worry?


	sendMessage(contestants.get(currentFighter).getName(), opponentResponse);
	sendMessage(contestants.get(currentOpponent).getName(), fighterResponse);

	//Analyze results:
	if(fighterResponse.equals(DEFECT) && opponentResponse.equals(DEFECT)){

		sendMessage(contestants.get(currentFighter).getName(), "2", ACLMessage.INFORM);
		sendMessage(contestants.get(currentOpponent).getName(), "2", ACLMessage.INFORM);
		String temp = "Both Defected in round "+currentRound;
		tl.log(temp);
		System.out.println(temp);   
	}
	else if(fighterResponse.equals(DEFECT) && opponentResponse.equals(COOPERATE)){

		sendMessage(contestants.get(currentFighter).getName(), "5", ACLMessage.INFORM);
		sendMessage(contestants.get(currentOpponent).getName(), "0", ACLMessage.INFORM);

		String temp = contestants.get(currentFighter).getName().getLocalName() + " wins round "+currentRound;
		tl.log(temp);
		System.out.println(temp);

	}else if(fighterResponse.equals(COOPERATE) && opponentResponse.equals(DEFECT)){

		sendMessage(contestants.get(currentFighter).getName(), "0", ACLMessage.INFORM);
		sendMessage(contestants.get(currentOpponent).getName(), "5", ACLMessage.INFORM);
		String temp = contestants.get(currentOpponent).getName().getLocalName() + " wins round "+currentRound;
		tl.log(temp);

	}else{

		sendMessage(contestants.get(currentFighter).getName(), "3", ACLMessage.INFORM);
		sendMessage(contestants.get(currentOpponent).getName(), "3", ACLMessage.INFORM);
		String temp = "Both cooperated in round "+currentRound;
		tl.log(temp);
		System.out.println(temp);   
	}
	
	System.out.println("\n\n");
	fighterResponse = null; //Clearing responses.
	opponentResponse = null;
	currentRound++;
	handleNextRound();
}
private void handleNextRound(){
		
		System.out.println("Entered handleNextRound. currentRound has value "+currentRound+ "\n Value of rounds: "+rounds);
    	if(currentRound < rounds){
    		int temp = currentRound+1;
    		System.out.println("Starting round "+temp+" of "+rounds);
//    		tl.printLog();
    		//One round complete, request new input from fighter:
    		System.out.println("Still more rounds to go. Sending out new DILEMMA\n\n");
    		sendMessage(contestants.get(currentFighter).getName(), DILEMMA);
    		return;
    	}
    	else{
    		/*Done with fight, now we reset and start with a new opponent (or a 
    		 * new fighter if opponent is last in table. Toss current fighter out of contestant table)*/
    		currentRound = 0;
    		System.out.println("CurrentOpponent: "+currentOpponent);
			System.out.println("Contestant size: "+ contestants.size());
			
    		if(currentOpponent < contestants.size() - 1)
    		{
    			
    			startNextFight();
    		}
    		else
    			startNextFighter();
    	}
    }
    
    
    
    private void startNextFight(){
    	currentOpponent++;

		System.out.println("Fight over, starting new fight with " + contestants.get(currentFighter).getName().getLocalName() 
				+ " and " + contestants.get(currentOpponent).getName().getLocalName());

		sendMessage(contestants.get(currentFighter).getName(), DILEMMA);
    }
    public void startNextFighter(){
		
    	System.out.println("Entered startNextFighter. Contestants.size is "+contestants.size());
		
    	if(contestants.size() == 2){
    		finalizeTournament();
			System.out.println("Tournament has ended...\n Here is the log:");
			tl.printLog();
		}else{
			contestants.remove(currentFighter);
			System.out.println("New fighter enters arena, it's " + 
					contestants.get(currentFighter + 1).getName().getLocalName() +" and first opponent is " + 
					contestants.get(currentFighter + 2).getName().getLocalName());
			
			currentOpponent = 1;
			sendMessage(contestants.get(currentFighter).getName(), DILEMMA);
		}
    }
    
    private void finalizeTournament() {
		populateContestants(); //Re-initializing list of contestants 
		
		System.out.println("Size of contestants is: "+contestants.size());
		for(int i = 0; i <contestants.size(); i++){
			sendMessage(contestants.get(i).getName(), "SCORE", ACLMessage.QUERY_IF);
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
        if(msg.getPerformative()== ACLMessage.AGREE){
       
        	System.out.println("\nAgent "+msg.getSender().getLocalName()+ "Got a total of "+msg.getContent()+ " Points.");
        }
    }
}