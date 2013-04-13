
package exercise5;

import exercise5.behaviours.AskForItemBehaviour;
import exercise5.behaviours.OfferItemBehaviour;
import exercise5.behaviours.NegotiateBehaviour;
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
import java.util.Random;
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
    
    List<IItem> itemsInTransaction;     //Items we are trying to trade
    /**
     * When we've given up on negotiating and price is as low as possible,
     * we add the offers we've received to this list. Finally HandleOffersBehaviour
     * picks the best offer we negotiated, and cancels the others.
     */
    Map<IItem, Map<AID, Integer>> offers;
    
    @Override
    protected void setup() {
        inventory = InventoryProvider.inventory(5, null);
        wishList = InventoryProvider.inventory(2, inventory);
        purchased = new ArrayList<IItem>();
        List<IItem> purchased = new ArrayList<IItem>();
        
        offers = new HashMap<IItem, Map<AID, Integer>>();
        itemsInTransaction = new ArrayList<IItem>();
        registerService(this);
        addBehaviour(new CyclicBehaviour(this) {

            @Override
            public void action() {
                ACLMessage msg = receive();
                if(msg != null){
                    String[] msgArray = msg.getContent().split(";");
                    if(msg.getPerformative() == ACLMessage.QUERY_IF){
                        //Let an agent know that we have this item and we're willing to sell.
                        //Lock the item down during negotiation.
                        //After around two seconds we pick the best deal based on what we've
                        //been able to negotiate.
                        IItem item = stringToItem(msg.getContent(), inventory);
                        if(item != null && !itemsInTransaction.contains(item)){
                            itemsInTransaction.add(item);
                            expectOffers(item);
                            addBehaviour(new OfferItemBehaviour(myAgent, item, msg));
                        }
                    }else if(msg.getPerformative() == ACLMessage.INFORM){
                        //An agent had the item we're looking for.
                        //Start negotiating price of the item
                        //Message format: "banana;10"
                        IItem item = stringToItem(msg.getContent().split(";")[0], wishList);
                        int price = Integer.parseInt(msg.getContent().split(";")[1]);
                        if(item != null){
                            addBehaviour(new NegotiateBehaviour(myAgent, 
                                    item, msg.getSender(), price));
                        }
                    }else if(msg.getPerformative() == ACLMessage.PROPOSE){
                        //TODO we are proposed a new price on item we're selling
                        // content: [itemname;lowerprice]
                    }else if(msg.getPerformative() == ACLMessage.AGREE){
                        //TODO buyer accepts price
                        // content: [itemname;price]
                        ACLMessage accept = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                        accept.addReceiver(msg.getSender());
                        accept.setContent(msg.getContent());
                        sellItem(stringToItem(msgArray[0], inventory), Integer.parseInt(msgArray[1]));
                        send(accept);
                    }else if(msg.getPerformative() == ACLMessage.CANCEL){
                        //TODO cancel transaction, buyer doesn't agree to trade.
                        System.out.println("Trade for " + msg.getContent().split(";")[0] + 
                                " between " + myAgent.getLocalName() + " and " + 
                                msg.getSender().getLocalName() + " canceled.");
                        IItem item = stringToItem(msg.getContent().split(";")[0], null);
                        if(item != null){
                            itemsInTransaction.remove(item);
                        }
                    }else if(msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL){
                        //TODO: Seller accepts proposal
                        int price = Integer.parseInt(msgArray[1]);
                        IItem item = stringToItem(msgArray[0], wishList);
                        if (item != null){
                            purchaseItem(item, price);
                        }
                    }else if(msg.getPerformative() == ACLMessage.REJECT_PROPOSAL){
                        //TODO the price we suggested is rejected!
                        //Negotiate further or just settle? Currently settles.
                        // content: [itemname;price]
                        addOffer(stringToItem(msgArray[0], wishList), 
                                msg.getSender(), Integer.parseInt(msgArray[1]));
                    }
                }
            }
        });
        addBehaviour(new AskForItemBehaviour(this, wishList.get(0)));
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
    
    public IItem stringToItem(String itemName, List<IItem> list){
        if(list == null){
            list = new ArrayList<IItem>(inventory);
            list.addAll(wishList);
        }
        for(IItem item: list){
            if(itemName.equals(item.getName())){
                return item;
            }
        }return null;
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
    
    public void purchaseItem(IItem item, int price){
        money -= price;
        purchased.add(item);
        wishList.remove(item);
    }
    
    public void sellItem(IItem item, int price){
        money += price;
        inventory.remove(item);
    }
    
    public void cancelOrder(IItem item){
        itemsInTransaction.remove(item);
    }
    
    public void expectOffers(IItem item){
        offers.put(item, new HashMap<AID, Integer>());
    }
    
    public void addOffer(IItem item, AID seller, int price){
        if(offers.containsKey(item)){
            offers.get(item).put(seller, price);
        }
    }
    
    public Map<AID, Integer> pullOffers(IItem item){
        return offers.remove(item);
    }
    
    public IItem getRandomDesiredItem(){
        Random rand = new Random();
        return wishList.get(rand.nextInt(wishList.size()));
    }
    
    public int getMoney(){
        return money;
    }
}
