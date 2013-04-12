
package exercise5;

import exercise4.TaskAdministrator;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Odd
 */
public class TradingAgent extends Agent{

    List<IItem> wishList;
    List<IItem> inventory;
    List<IItem> purchased;
    int money = 1000;
    
    Map<IItem, Map<AID, Integer>> offers;
    Map<IItem, Map<AID, Integer>> bids;
    
    @Override
    protected void setup() {
        inventory = InventoryProvider.getInstance().inventory(3, null);
        wishList = InventoryProvider.getInstance().inventory(3, inventory);
        List<IItem> purchased = new ArrayList<IItem>();
        
        offers = new HashMap<IItem, Map<AID,Integer>>();
        bids = new HashMap<IItem, Map<AID,Integer>>();
        registerService(this);
        addBehaviour(new CyclicBehaviour(this) {

            @Override
            public void action() {
                ACLMessage msg = receive();
                if(msg != null){
                    if(msg.getPerformative() == ACLMessage.QUERY_IF){
                        for(IItem item: inventory){
                            if(msg.getContent().equals(item.getName())){
                                addBehaviour(new OfferItemBehaviour(myAgent, item, msg));
                            }
                        }
                    }else if(msg.getPerformative() == ACLMessage.INFORM){
                        addOffer(msg);
                    }else if(msg.getPerformative() == ACLMessage.AGREE){
                        //TODO sell item, must somehow store price
                    }
                }
            }
        });
        addBehaviour(new AskForItemBehaviour(this, wishList.get(0)));
    }
    
    /**
     * We will expect to receive offers on an item. The purpose of this function
     * is to allow for ignoring offers that are received after we've started
     * handling offers in the wakerbehaviour.
     * @param item 
     */
    public void expectOffers(IItem item){
        offers.put(item, new HashMap<AID, Integer>());
    }
    
    public void addOffer(ACLMessage msg){
        String[] msgArray = msg.getContent().split(";");
        IItem desiredItem = null;
        for(IItem item:wishList){
            if (msgArray[0].equals(item.getName())){
                desiredItem = item;
            }
        }
        if(offers.containsKey(desiredItem)){
            offers.get(desiredItem).put(msg.getSender(), Integer.parseInt(msgArray[1]));
        }
        
    }
    
    /**
     * Get all the offers made for an item. This removes the key from the offers-
     * map as well so that further messages with offers are ignored.
     * @param item
     * @return 
     */
    public Map<AID, Integer> pullOffers(IItem item){
        return offers.remove(item);
    }
    
     public static void registerService(Agent myself) {
        DFAgentDescription dfa = new DFAgentDescription();
        dfa.addLanguages("TRADE");
        dfa.addOntologies("trade");
        try {
            DFService.register(myself, dfa);
        } catch (FIPAException ex) {
            ex.printStackTrace();
        }
    }
    
    public static List<AID> getOtherAgents(Agent myself){
        List<AID> agents = new ArrayList<AID>();
        
        DFAgentDescription dfa = new DFAgentDescription();
        dfa.addLanguages("TRADE");
        dfa.addOntologies("trade");
        
        DFAgentDescription[] results = null;
        try {
            results = DFService.search(myself, dfa);
        } catch (FIPAException ex) {
            Logger.getLogger(TaskAdministrator.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        for (int i = 0; i < results.length; i++){
            agents.add(results[i].getName());
        }
        return agents;
    }

    boolean acceptPrice(IItem item, int lowestPrice) {
        //TODO fix logic for either accepting or refusing an offer
        return true;
    }
}
